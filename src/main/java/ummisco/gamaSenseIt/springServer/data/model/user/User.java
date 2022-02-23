package ummisco.gamaSenseIt.springServer.data.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.ColumnDefault;
import ummisco.gamaSenseIt.springServer.data.model.IView;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "user")
public class User {

    @ManyToMany(mappedBy = "users")
    private final Set<Access> accesses = new HashSet<>();
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonProperty("id")
    @JsonView(IView.Public.class)
    private Long id;

    @Column(length = 60)
    private String firstname;
    @Column(length = 60)
    private String lastName;
    @Column(length = 200, unique = true)
    private String mail;
    @JsonIgnore
    private String password;

    @ColumnDefault("1")
    private UserPrivilege privilege;

    public User() {
    }

    public User(String firstname, String lastName, String mail, String password, UserPrivilege priv) {
        super();
        this.firstname = firstname;
        this.lastName = lastName;
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

    public Set<Access> getAccesses() {
        return this.accesses;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
}
