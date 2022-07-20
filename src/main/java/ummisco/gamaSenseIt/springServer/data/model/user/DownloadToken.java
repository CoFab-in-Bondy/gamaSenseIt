package ummisco.gamaSenseIt.springServer.data.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import ummisco.gamaSenseIt.springServer.data.model.IView;
import ummisco.gamaSenseIt.springServer.data.model.sensor.Sensor;

import javax.persistence.*;

@Entity
@Table(name = "download_token")
public class DownloadToken extends DatabaseToken{

    // ----- sensor_id ----- //
    @JoinColumn(name = "sensor_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Sensor sensor;

    @Column(name = "sensor_id")
    @JsonProperty("sensorId")
    @JsonView(IView.Public.class)
    private Long sensorId;

    // ----- data ----- //
    @Lob
    @Column(name = "program", nullable = false)
    @JsonIgnore
    private byte[] program;


    public Sensor getSensor() {
        return sensor;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public void setSensorId(Long sensorId) {
        this.sensorId = sensorId;
    }

    public byte[] getProgram() {
        return program;
    }

    public void setProgram(byte[] program) {
        this.program = program;
    }
}
