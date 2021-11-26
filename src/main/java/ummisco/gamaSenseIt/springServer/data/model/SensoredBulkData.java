package ummisco.gamaSenseIt.springServer.data.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class SensoredBulkData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long sensoredBulkDataId;

    private long token;
    private Date captureDate;
    private Date receivingDate;
    private String contents;

    @ManyToOne
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;

    public SensoredBulkData(Sensor sensor, long token, Date captureDate, Date receivingDate, String contents) {
        super();
        this.sensor = sensor;
        this.token = token;
        this.captureDate = captureDate;
        this.receivingDate = receivingDate;
        this.contents = contents;
    }

    public SensoredBulkData() {
        super();
    }

    public Long getSensoredBulkDataId() {
        return sensoredBulkDataId;
    }

    public void setSensoredBulkDataId(Long sensoredBulkDataId) {
        this.sensoredBulkDataId = sensoredBulkDataId;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public long getToken() {
        return token;
    }

    public void setToken(long token) {
        this.token = token;
    }

    public Date getCaptureDate() {
        return captureDate;
    }

    public void setCaptureDate(Date captureDate) {
        this.captureDate = captureDate;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Date getReceivingDate() {
        return receivingDate;
    }

    public void setReceivingDate(Date receivingDate) {
        this.receivingDate = receivingDate;
    }

}
