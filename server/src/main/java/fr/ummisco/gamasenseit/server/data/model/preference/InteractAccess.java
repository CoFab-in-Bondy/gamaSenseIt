package fr.ummisco.gamasenseit.server.data.model.preference;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import fr.ummisco.gamasenseit.server.data.model.IView;
import fr.ummisco.gamasenseit.server.data.model.user.Access;
import fr.ummisco.gamasenseit.server.data.model.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@IdClass(InteractAccess.InteractAccessPK.class)
@Table(name = "interact_access")
public class InteractAccess implements InteractBase {

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

    // ----- date ----- //
    @Column(name = "date", nullable = false, insertable = false, updatable = false,
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @JsonProperty("date")
    @JsonView(IView.Public.class)
    private Date date;

    public User getUser() {
        return user;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Access getAccess() {
        return access;
    }

    public Long getAccessId() {
        return accessId;
    }

    public void setAccessId(Long accessId) {
        this.accessId = accessId;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
    }

    public static class InteractAccessPK implements Serializable {
        protected Long userId;
        protected Long accessId;

        public InteractAccessPK() {
        }

        public InteractAccessPK(Long userId, Long accessId) {
            this.userId = userId;
            this.accessId = accessId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            InteractAccess.InteractAccessPK that = (InteractAccess.InteractAccessPK) o;
            return Objects.equals(userId, that.userId) && Objects.equals(accessId, that.accessId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, accessId);
        }
    }
}
