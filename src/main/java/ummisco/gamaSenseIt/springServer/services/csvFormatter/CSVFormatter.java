package ummisco.gamaSenseIt.springServer.services.csvFormatter;

import java.io.StringWriter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

@Service("CSVFormatter")
public class CSVFormatter implements IFormatter {
    public <T> String format(List<T> list) {
        StringWriter writer = new StringWriter();
        var beanToCsv = new StatefulBeanToCsvBuilder<T>(writer)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .build();
        try {
            beanToCsv.write(list);
            return writer.toString();
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            e.printStackTrace();
            return "";
        }
    }

 
}
