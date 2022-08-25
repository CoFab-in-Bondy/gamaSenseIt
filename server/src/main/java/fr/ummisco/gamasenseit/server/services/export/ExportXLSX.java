package fr.ummisco.gamasenseit.server.services.export;


import fr.ummisco.gamasenseit.server.data.model.sensor.DataFormat;
import fr.ummisco.gamasenseit.server.data.model.sensor.ParameterMetadata;
import fr.ummisco.gamasenseit.server.data.model.sensor.Sensor;
import fr.ummisco.gamasenseit.server.data.services.record.RecordManager;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.XDDFColor;
import org.apache.poi.xddf.usermodel.XDDFLineProperties;
import org.apache.poi.xddf.usermodel.XDDFShapeProperties;
import org.apache.poi.xddf.usermodel.XDDFSolidFillProperties;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

@Service("ExportXLSX")
public class ExportXLSX extends Export {

    private static final XDDFLineProperties[] COLORS = {
            hex2XDDFColor("#5470c6"),
            hex2XDDFColor("#91cc75"),
            hex2XDDFColor("#fac858"),
            hex2XDDFColor("#ee6666"),
            hex2XDDFColor("#73c0de"),
            hex2XDDFColor("#fc8452"),
            hex2XDDFColor("#9a60b4"),
            hex2XDDFColor("#ea7ccc")
    };

    @Autowired
    private RecordManager recordManager;

    public ExportXLSX() {
        super(new MediaType("application", "vnd.ms-excel"), "xlsx", false);
    }

    private static XDDFLineProperties hex2XDDFColor(String colorStr) {
        int r = Integer.valueOf(colorStr.substring(1, 3), 16);
        int g = Integer.valueOf(colorStr.substring(3, 5), 16);
        int b = Integer.valueOf(colorStr.substring(5, 7), 16);
        XDDFColor color = XDDFColor.from(new byte[]{(byte) r, (byte) g, (byte) b});
        XDDFSolidFillProperties fill = new XDDFSolidFillProperties(color);
        XDDFLineProperties line = new XDDFLineProperties();
        line.setFillProperties(fill);
        return line;
    }

    private CellStyle createHeaderStyle(XSSFWorkbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = workbook.createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 11);
        font.setBold(true);
        headerStyle.setFont(font);
        return headerStyle;
    }

    private void createScatter(XSSFSheet sheet, String title, String unit, int row, int col, int index, int size, int width, int height) {
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = drawing.createAnchor(
                0, 0, 0, 0, col, row, col + width, row + height);

        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleText("Measures of " + title + " each days");
        chart.setTitleOverlay(false);

        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP_RIGHT);

        XDDFValueAxis bottomAxis = chart.createValueAxis(AxisPosition.BOTTOM);
        bottomAxis.setTitle("Date");
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        leftAxis.setTitle(title + " (" + unit + ")");
        XDDFScatterChartData data = (XDDFScatterChartData) chart.createData(ChartTypes.SCATTER, bottomAxis, leftAxis);

        XDDFNumericalDataSource<Double> dates = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
                new CellRangeAddress(1, size, 0, 0));

        XDDFNumericalDataSource<Double> xs = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
                new CellRangeAddress(1, size, index + 1, index + 1));
        XDDFScatterChartData.Series series = (XDDFScatterChartData.Series) data.addSeries(dates, xs);
        series.setTitle(title + " (" + unit + ")", null);

        XDDFShapeProperties properties = series.getShapeProperties();
        if (properties == null)
            properties = new XDDFShapeProperties();
        properties.setLineProperties(COLORS[index % 8]);
        series.setShapeProperties(properties);

        chart.plot(data);

        // Understand series as one line and not many parts
        chart.getCTChart().getPlotArea().getScatterChartArray(0).getSerArray(0).addNewSmooth().setVal(false);
        chart.getCTChart().getPlotArea().getScatterChartArray(0).addNewVaryColors().setVal(false);

    }

    @Override
    protected byte[] toBytes(Sensor sensor, ParameterMetadata parameterMetadata, Date start, Date end) throws IOException {
        var smd = sensor.getSensorMetadata();
        var records = recordManager.getRecords(sensor, parameterMetadata, start, end);
        records.sortByDate();
        var pmds = smd.getParametersMetadata();
        var metadata = records.metadata();

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Capteur");
        CellStyle headerStyle = createHeaderStyle(workbook);

        // add header information
        Row header = sheet.createRow(0);
        CellStyle[] styles = new CellStyle[metadata.width()];
        CreationHelper createHelper = workbook.getCreationHelper();
        var headers = metadata.headers();
        for (int i = 0; i < metadata.width(); i++) {
            int size = 3000;
            Cell headerCell = header.createCell(i);
            headerCell.setCellValue(headers[i]);
            headerCell.setCellStyle(headerStyle);

            styles[i] = workbook.createCellStyle();
            styles[i].setWrapText(true);
            switch (metadata.types()[i]) {
                case STRING -> size = 7000;
                case DATE -> {
                    short format = createHelper.createDataFormat().getFormat("yyyy-mm-dd hh:mm");
                    styles[i].setDataFormat(format);
                    size = 5000;
                }
            }
            sheet.setColumnWidth(i, size);
        }


        for (int i = 0; i < records.size(); i++) {
            Row row = sheet.createRow(i + 1);
            var record = records.get(i).asObjects();
            for (int j = 0; j < record.length; j++) {
                Cell cell = row.createCell(j);
                // Replace standard by NUMERIC
                metadata.types()[j].switchValuesWithNullCase(
                        record[j],
                        cell::setCellValue,
                        cell::setCellValue,
                        cell::setCellValue,
                        cell::setCellValue,
                        () -> cell.setCellErrorValue(FormulaError.NA.getCode())
                );
                cell.setCellStyle(styles[j]);
            }
        }

        int height = 12;
        for (int i = 0; i < pmds.size(); i++) {
            createScatter(
                    sheet, records.metadata().headers()[i + 1],
                    records.metadata().units()[i + 1],
                    1 + (height + 1) * i,
                    metadata.width() + 1,
                    i, records.size(),
                    15,
                    height
            );
        }

        var byteArray = new ByteArrayOutputStream();
        try {
            workbook.write(byteArray);
        } finally {
            workbook.close();
        }
        return byteArray.toByteArray();
    }
}
