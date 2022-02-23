package ummisco.gamaSenseIt.springServer.data.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.Immutable;
import ummisco.gamaSenseIt.springServer.data.model.IView;
import ummisco.gamaSenseIt.springServer.data.model.Sensor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@IdClass(AccessReadView.AccessPK.class)
@Immutable
@Table(name = "view_access_user_sensor")
public class AccessReadView {

    @JoinColumn(name = "sensor_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Sensor sensor;

    @Id
    @Column(name = "sensor_id")
    @JsonProperty("sensorId")
    @JsonView(IView.Public.class)
    private Long sensorId;

    // ----- user_id ----- //
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Sensor user;

    @Id
    @Column(name = "user_id")
    @JsonProperty("userId")
    @JsonView(IView.Public.class)
    private Long userId;

    public Sensor getSensor() {
        return sensor;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public Sensor getUser() {
        return user;
    }

    public Long getUserId() {
        return userId;
    }

    public static class AccessPK implements Serializable {
        protected Long sensorId;
        protected Long userId;

        public AccessPK() {
        }

        public AccessPK(Long sensorId, Long userId) {
            this.sensorId = sensorId;
            this.userId = userId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AccessPK that = (AccessPK) o;
            return Objects.equals(sensorId, that.sensorId) && Objects.equals(userId, that.userId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sensorId, userId);
        }
    }
}
