package ummisco.gamaSenseIt.springServer.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.nio.ByteBuffer;
import java.util.Date;

@Entity
public class Parameter {

    // ----- parameter_id ----- //
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonProperty("id")
    @JsonView(IView.Public.class)
    private Long id;

    // ----- data ----- //
    @Lob
    @Column(name = "data")
    @JsonIgnore
    private byte[] data;

    // ----- captureDate ----- //
    @Column(name = "capture_date")
    @JsonProperty("captureDate")
    @JsonView(IView.Public.class)
    private Date captureDate;

    // ----- sensor_id ----- //
    @JoinColumn(name = "sensor_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Sensor sensor;

    @Column(name = "sensor_id", nullable = false, insertable = false, updatable = false)
    @JsonProperty("sensorId")
    @JsonView(IView.Public.class)
    private Long sensorId;

    // ----- parameter_metadata_id ----- //
    @JoinColumn(name = "parameter_metadata_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private ParameterMetadata parameterMetadata;

    @Column(name = "parameter_metadata_id", nullable = false, insertable = false, updatable = false)
    @JsonProperty("parameterMetadataId")
    @JsonView(IView.Public.class)
    private Long parameterMetadataId;

    public Parameter() {
    } // for JSON compatibility

    private Parameter(byte[] data, Date captureDate, ParameterMetadata parameterMetadata, Sensor sensor) {
        super();
        this.data = data;
        this.sensor = sensor;
        this.captureDate = captureDate;
        this.parameterMetadata = parameterMetadata;
    }

    public Parameter(double data, Date captureDate, ParameterMetadata parameterMetadata, Sensor sensor) {
        this(ByteBuffer.allocate(Double.BYTES).putDouble(data).array(), captureDate, parameterMetadata, sensor);
    }

    public Parameter(long data, Date captureDate, ParameterMetadata parameterMetadata, Sensor sensor) {
        this(ByteBuffer.allocate(Integer.BYTES).putLong(data).array(), captureDate, parameterMetadata, sensor);
    }

    public Parameter(String data, Date captureDate, ParameterMetadata parameterMetadata, Sensor sensor) {
        this(data.getBytes(), captureDate, parameterMetadata, sensor);
    }

    @JsonProperty("value")
    @JsonView(IView.Public.class)
    public Object value() {
        ParameterMetadata.DataFormat df;
        if ((df = parameterMetadata.getDataType()) != null)
            return df.convertToObject(data);
        else
            return null;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCaptureDate() {
        return captureDate;
    }

    public void setCaptureDate(Date captureDate) {
        this.captureDate = captureDate;
    }

    @JsonView(IView.SensorOfParameter.class)
    @JsonProperty("sensor")
    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public void setSensorId(Long sensorId) {
        this.sensorId = sensorId;
    }

    @JsonView(IView.ParameterMetadataOfParameter.class)
    @JsonProperty("parameterMetadata")
    public ParameterMetadata getParameterMetadata() {
        return parameterMetadata;
    }

    public void setParameterMetadata(ParameterMetadata parameterMetadata) {
        this.parameterMetadata = parameterMetadata;
    }

    public Long getParameterMetadataId() {
        return parameterMetadataId;
    }

    public void setParameterMetadataId(Long parameterMetadataId) {
        this.parameterMetadataId = parameterMetadataId;
    }
}
