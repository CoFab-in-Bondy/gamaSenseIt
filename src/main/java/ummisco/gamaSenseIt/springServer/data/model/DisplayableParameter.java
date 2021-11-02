package ummisco.gamaSenseIt.springServer.data.model;

import com.opencsv.bean.CsvBindByPosition;

import java.util.Date;

public class DisplayableParameter {

    @CsvBindByPosition(position = 0)
    public Object value;

    @CsvBindByPosition(position = 1)
    private Date date;

    @CsvBindByPosition(position = 2)
    private String unit;

    @CsvBindByPosition(position = 3)
    private String sensorName;

    @CsvBindByPosition(position = 4)
    private double latitude;

    @CsvBindByPosition(position = 5)
    private double longitude;

    @CsvBindByPosition(position = 6)
    private String measuredParameter;

    public DisplayableParameter(
            Object value,
            Date date,
            String unit,
            String sensorName,
            double latitude,
            double longitude,
            String measuredParameter
    ) {
        super();
        this.value = value;
        this.date = date;
        this.unit = unit;
        this.sensorName = sensorName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.measuredParameter = measuredParameter;
    }

    public DisplayableParameter(Parameter p) {
        this(
                p.getDataObject(),
                p.getCaptureDate(),
                p.getParameterMetadata().getUnit(),
                p.getSensor().getName(),
                p.getSensor().getLatitude(),
                p.getSensor().getLongitude(),
                p.getParameterMetadata().getDataParameter().toString()
        );
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getMeasuredParameter() {
        return measuredParameter;
    }

    public void setMeasuredParameter(String measuredParameter) {
        this.measuredParameter = measuredParameter;
    }

}
