package ummisco.gamaSenseIt.springServer.data.model.preference;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import ummisco.gamaSenseIt.springServer.data.model.IView;
import ummisco.gamaSenseIt.springServer.data.model.sensor.Sensor;
import ummisco.gamaSenseIt.springServer.data.model.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@IdClass(InteractSensor.InteractSensorPK.class)
@Table(name = "interact_sensor")
public class InteractSensor implements InteractBase {


    // ----- user_id ----- //
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    @Id
    @Column(name = "user_id")
    @JsonProperty("userId")
    @JsonView(IView.Public.class)
    private Long userId;

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

    // ----- date ----- //
    @Column(name = "date", nullable = false, insertable = false, updatable = false,
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @JsonProperty("date")
    @JsonView(IView.Public.class)
    private Date date;

    public User getUser() {
        return user;
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public void setSensorId(Long sensorId) {
        this.sensorId = sensorId;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
    }

    public static class InteractSensorPK implements Serializable {
        protected Long userId;
        protected Long sensorId;

        public InteractSensorPK() {
        }

        public InteractSensorPK(Long userId, Long sensorId) {
            this.userId = userId;
            this.sensorId = sensorId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            InteractSensorPK that = (InteractSensorPK) o;
            return Objects.equals(userId, that.userId) && Objects.equals(sensorId, that.sensorId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, sensorId);
        }
    }

}
