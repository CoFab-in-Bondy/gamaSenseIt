package ummisco.gamaSenseIt.springServer.data.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.security.core.userdetails.UserDetails;
import ummisco.gamaSenseIt.springServer.data.model.IView;

import javax.persistence.*;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class DatabaseToken {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    @JsonProperty("id")
    @JsonView(IView.Public.class)
    private Long id;

    // ----- user_id ----- //
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    @Column(name = "user_id")
    @JsonProperty("userId")
    @JsonView(IView.Public.class)
    private Long userId;

    // ----- token ----- //
    @Column(nullable = false, unique = true)
    @JsonProperty("token")
    @JsonView(IView.Public.class)
    private String token;

    // ----- expire_at ----- //
    @Column(name = "expiration", nullable = false, columnDefinition = "DATETIME")
    @JsonProperty("expiration")
    @JsonView(IView.Public.class)
    private Date expiration;

    // ----- expire_at ----- //
    @Column(name = "issued_at", nullable = false, columnDefinition = "DATETIME")
    @JsonProperty("issuedAt")
    @JsonView(IView.Public.class)
    private Date issuedAt;

    public DatabaseToken() {
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public boolean isExpired() {
        return expiration.after(new Date());
    }

    public Date getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Date issuedAt) {
        this.issuedAt = issuedAt;
    }

    public boolean validateExpirationAndUser(UserDetails user) {
        return this.user.getMail().equals(user.getUsername()) && !this.isExpired();
    }
}
