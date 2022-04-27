package ummisco.gamaSenseIt.springServer.data.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import ummisco.gamaSenseIt.springServer.data.model.IView;
import ummisco.gamaSenseIt.springServer.data.model.preference.InteractAccess;
import ummisco.gamaSenseIt.springServer.data.model.preference.InteractBase;
import ummisco.gamaSenseIt.springServer.data.model.preference.InteractSensor;
import ummisco.gamaSenseIt.springServer.data.model.preference.Interactible;
import ummisco.gamaSenseIt.springServer.data.model.sensor.Parameter;
import ummisco.gamaSenseIt.springServer.data.model.sensor.Sensor;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "access")
public class Access extends Interactible {

    // ----- users ----- //
    // @ManyToMany(cascade = {CascadeType.ALL})
    @ManyToMany()
    @JoinTable(
            name = "access_user",
            joinColumns = {@JoinColumn(name = "access_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private final Set<User> users = new HashSet<>();
    // ----- sensors ----- //
    // @ManyToMany(cascade = {CascadeType.ALL})
    @ManyToMany()
    @JoinTable(
            name = "access_sensor",
            joinColumns = {@JoinColumn(name = "access_id")},
            inverseJoinColumns = {@JoinColumn(name = "sensor_id")}
    )
    @JsonIgnore
    private final Set<Sensor> sensors = new HashSet<>();

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

    // ----- accessUser ----- //
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "access")
    @JsonIgnore
    private Set<AccessUser> accessUsers;

    // ----- accessSensor ----- //
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "access")
    @JsonIgnore
    private Set<AccessSensor> accessSensors;

    // ----- privilege ----- //
    @Column(name = "privilege", nullable = false)
    @JsonProperty("privilege")
    @JsonView(IView.Public.class)
    private AccessPrivilege privilege;


    // ----- interacts ----- //
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "access")
    @JsonIgnore
    private final Set<InteractAccess> interacts = new HashSet<>();

    public Access() {
    }

    public Access(String name, AccessPrivilege privilege) {
        this.name = name;
        this.privilege = privilege;
    }

    public AccessPrivilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(AccessPrivilege privilege) {
        this.privilege = privilege;
    }

    public Set<AccessUser> getAccessUsers() {
        return accessUsers;
    }

    public void setAccessUsers(Set<AccessUser> accessUsers) {
        this.accessUsers = accessUsers;
    }

    public Set<AccessSensor> getAccessSensors() {
        return accessSensors;
    }

    public void setAccessSensors(Set<AccessSensor> accessSensors) {
        this.accessSensors = accessSensors;
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

    @JsonView(IView.AccessCount.class)
    public int sizeSensors() {
        return this.sensors.size();
    }

    @JsonView(IView.AccessCount.class)
    public int sizeUsers() {
        return this.users.size();
    }

    public boolean manageableBy(long userId) {
        for (var acu : getAccessUsers())
            if (acu.getUserId() == userId)
                return acu.getPrivilege() == AccessUserPrivilege.MANAGE;
        return false;
    }

    @Override
    public Set<? extends InteractBase> getInteracts() {
        return interacts;
    }
}
