package ummisco.gamaSenseIt.springServer.data.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import ummisco.gamaSenseIt.springServer.data.model.IView;
import ummisco.gamaSenseIt.springServer.data.model.Sensor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@IdClass(AccessUser.AccessUserPK.class)
@Table(name = "access_user")
public class AccessUser {

    // ----- access_id ----- //
    @JoinColumn(name = "access_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Sensor access;

    @Id
    @Column(name = "access_id")
    @JsonProperty("accessId")
    @JsonView(IView.Public.class)
    private Long accessId;

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

    // ----- category ----- //
    @Column(name = "privilege", nullable = false)
    @JsonProperty("privilege")
    @JsonView(IView.Public.class)
    private AccessUserPrivilege privilege;

    // ----- created_at ----- //
    @Column(name = "created_at", nullable = false, updatable = false, insertable = false,
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @JsonProperty("createdAt")
    @JsonView(IView.Public.class)
    private Date createdAt;

    public AccessUser() {
    }

    public AccessUser(Long accessId, Long userId, AccessUserPrivilege privilege) {
        this.accessId = accessId;
        this.userId = userId;
        this.privilege = privilege;
    }

    public Long getAccessId() {
        return accessId;
    }

    public void setAccessId(Long accessId) {
        this.accessId = accessId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public AccessUserPrivilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(AccessUserPrivilege privilege) {
        this.privilege = privilege;
    }

    public Sensor getAccess() {
        return access;
    }

    public void setAccess(Sensor access) {
        this.access = access;
    }

    public Sensor getUser() {
        return user;
    }

    public void setUser(Sensor user) {
        this.user = user;
    }

    public static class AccessUserPK implements Serializable {
        protected Long accessId;
        protected Long userId;

        public AccessUserPK() {
        }

        public AccessUserPK(Long accessId, Long userId) {
            this.accessId = accessId;
            this.userId = userId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AccessUserPK that = (AccessUserPK) o;
            return Objects.equals(accessId, that.accessId) && Objects.equals(userId, that.userId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(accessId, userId);
        }
    }
}
