package ummisco.gamaSenseIt.springServer.data.model.preference;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.UpdateTimestamp;
import ummisco.gamaSenseIt.springServer.data.model.IView;
import ummisco.gamaSenseIt.springServer.data.model.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@IdClass(InteractUser.InteractUserPK.class)
@Table(name = "interact_user")
public class InteractUser implements InteractBase {


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

    // ----- target_id ----- //
    @JoinColumn(name = "target_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User target;

    @Id
    @Column(name = "target_id")
    @JsonProperty("targetId")
    @JsonView(IView.Public.class)
    private Long targetId;

    // ----- updated_at ----- //
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

    public User getTarget() {
        return target;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
    }

    public static class InteractUserPK implements Serializable {
        protected Long userId;
        protected Long targetId;

        public InteractUserPK() {
        }

        public InteractUserPK(Long userId, Long targetId) {
            this.userId = userId;
            this.targetId = targetId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            InteractUserPK that = (InteractUserPK) o;
            return Objects.equals(userId, that.userId) && Objects.equals(targetId, that.targetId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, targetId);
        }
    }

}