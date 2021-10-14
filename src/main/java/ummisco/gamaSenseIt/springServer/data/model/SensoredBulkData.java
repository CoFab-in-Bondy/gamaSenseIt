package ummisco.gamaSenseIt.springServer.data.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class SensoredBulkData {

    // TODO why not private
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long idData;
    long token;
    Date captureDate;
    Date receivingDate;
    String contents;
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

    public Long getIdData() {
        return idData;
    }

    public void setIdData(Long idData) {
        this.idData = idData;
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

    public void setToken(long tocken) {
        this.token = tocken;
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
