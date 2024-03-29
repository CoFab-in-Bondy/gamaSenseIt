package fr.ummisco.gamasenseit.server.data.model.sensor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import fr.ummisco.gamasenseit.server.data.model.IView;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Parameter {

    // ----- parameter_id ----- //
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonProperty("id")
    @JsonView(IView.Public.class)
    private Long id;

    // ----- data ----- //
    @Lob
    @Column(name = "data", nullable = false)
    @JsonIgnore
    private byte[] data;

    // ----- captureDate ----- //
    @Column(name = "capture_date", nullable = false)
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

    public Parameter(byte[] data, Date captureDate, ParameterMetadata parameterMetadata, Sensor sensor) {
        super();
        this.data = data;
        this.sensor = sensor;
        this.captureDate = captureDate;
        this.parameterMetadata = parameterMetadata;
    }

    @JsonProperty("value")
    @JsonView(IView.Public.class)
    public Object value() {
        DataFormat df;
        if ((df = parameterMetadata.getDataType()) != null)
            return df.bytesToObject(data);
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
