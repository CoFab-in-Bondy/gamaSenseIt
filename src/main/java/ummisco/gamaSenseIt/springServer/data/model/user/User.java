package ummisco.gamaSenseIt.springServer.data.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.ColumnDefault;
import ummisco.gamaSenseIt.springServer.data.model.IView;
import ummisco.gamaSenseIt.springServer.data.model.preference.InteractBase;
import ummisco.gamaSenseIt.springServer.data.model.preference.InteractSensor;
import ummisco.gamaSenseIt.springServer.data.model.preference.InteractUser;
import ummisco.gamaSenseIt.springServer.data.model.preference.Interactible;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "user")
public class User extends Interactible {

    @OneToMany(mappedBy = "user")
    private Set<AccessUser> accessUsers;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonProperty("id")
    @JsonView(IView.Public.class)
    private Long id;

    @Column(length = 60)
    @JsonProperty("firstname")
    @JsonView(IView.Public.class)
    private String firstname;

    @Column(length = 60)
    @JsonProperty("lastname")
    @JsonView(IView.Public.class)
    private String lastname;
    @Column(length = 200, unique = true)
    private String mail;
    @JsonIgnore
    private String password;

    @ColumnDefault("1")
    private UserPrivilege privilege;

    @JsonView(IView.AccessUser.class)
    @Transient
    @JsonProperty("accessUserPrivilege")
    private AccessUserPrivilege accessUserPrivilege = null;

    // ----- interactSensor ----- //
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "target")
    @JsonIgnore
    private final Set<InteractUser> interacts = new HashSet<>();


    public User() {
    }

    public User(String firstname, String lastname, String mail, String password, UserPrivilege priv) {
        super();
        this.firstname = firstname;
        this.lastname = lastname;
        this.mail = mail;
        this.password = password;
        this.privilege = priv;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccessUserPrivilege getAccessUserPrivilege() {
        return accessUserPrivilege;
    }

    public void setAccessUserPrivilege(AccessUserPrivilege accessUserPrivilege) {
        this.accessUserPrivilege = accessUserPrivilege;
    }

    public Set<AccessUser> getAccessUsers() {
        return accessUsers;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserPrivilege getPrivilege() {
        return this.privilege;
    }

    public void setPrivilege(UserPrivilege privilege) {
        this.privilege = privilege;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public Set<? extends InteractBase> getInteracts() {
        return interacts;
    }
}
