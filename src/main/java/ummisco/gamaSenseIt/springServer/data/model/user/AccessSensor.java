package ummisco.gamaSenseIt.springServer.data.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import ummisco.gamaSenseIt.springServer.data.model.IView;
import ummisco.gamaSenseIt.springServer.data.model.sensor.Sensor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@IdClass(AccessSensor.AccessSensorPK.class)
@Table(name = "access_sensor")
public class AccessSensor {

    // ----- access_id ----- //
    @JoinColumn(name = "access_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Access access;

    @Id
    @Column(name = "access_id")
    @JsonProperty("accessId")
    @JsonView(IView.Public.class)
    private Long accessId;

    // ----- sensor_id ----- //
    @JoinColumn(name = "sensor_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Sensor sensor;

    @Id
    @Column(name = "sensor_id")
    @JsonProperty("sensorId")
    @JsonView(IView.Public.class)
    private Long sensorId;

    // ----- created_at ----- //
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false,
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @JsonProperty("createdAt")
    @JsonView(IView.Public.class)
    private Date createdAt;

    public AccessSensor(long accessId, long sensorId) {
        this.accessId = accessId;
        this.sensorId = sensorId;
    }

    public AccessSensor() {

    }

    public Long getAccessId() {
        return accessId;
    }

    public void setAccessId(Long accessId) {
        this.accessId = accessId;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public void setSensorId(Long sensorId) {
        this.sensorId = sensorId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Access getAccess() {
        return access;
    }

    public void setAccess(Access access) {
        this.access = access;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public static class AccessSensorPK implements Serializable {
        protected Long accessId;
        protected Long sensorId;

        public AccessSensorPK() {
        }

        public AccessSensorPK(Long accessId, Long sensorId) {
            this.accessId = accessId;
            this.sensorId = sensorId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AccessSensorPK that = (AccessSensorPK) o;
            return Objects.equals(accessId, that.accessId) && Objects.equals(sensorId, that.sensorId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(accessId, sensorId);
        }
    }
}
