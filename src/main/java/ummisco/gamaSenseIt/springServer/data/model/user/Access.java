package ummisco.gamaSenseIt.springServer.data.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import ummisco.gamaSenseIt.springServer.data.model.IView;
import ummisco.gamaSenseIt.springServer.data.model.Sensor;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "access")
public class Access {

    // ----- users ----- //
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "access_user",
            joinColumns = {@JoinColumn(name = "access_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private final Set<User> users = new HashSet<>();
    // ----- sensors ----- //
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "access_sensor",
            joinColumns = {@JoinColumn(name = "access_id")},
            inverseJoinColumns = {@JoinColumn(name = "sensor_id")}
    )
    @JsonIgnore
    private final Set<Sensor> sensors = new HashSet<>();

    // ----- category ----- //
    @Column(name = "category", nullable = false)
    @JsonProperty("category")
    @JsonView(IView.Public.class)
    private AccessCategory category;

    // ----- parameter_id ----- //
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonProperty("id")
    @JsonView(IView.Public.class)
    private Long id;
    // ----- name ----- //
    @Column(name = "name", nullable = false)
    @JsonProperty("name")
    @JsonView(IView.Public.class)
    private String name;
    // ----- created_at ----- //
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false,
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @JsonProperty("createdAt")
    @JsonView(IView.Public.class)
    private Date createdAt;
    // ----- updated_at ----- //
    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false,
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @JsonProperty("updatedAt")
    @JsonView(IView.Public.class)
    private Date updatedAt;

    public Access() {
    }

    public Access(String name, AccessCategory category) {
        this.name = name;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public Set<Sensor> getSensors() {
        return sensors;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
