package ummisco.gamaSenseIt.springServer.data.model.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
public class User {
	@Id
	long idUser;
	@Column(length = 60)
	String firstname;
	@Column(length = 60)
	String lastName;
	@Column(length = 200, unique = true)
	String mail;
	String password;
	
	UserPrivilege privilege;
	
	@OneToMany(mappedBy = "user")
	List<Role> myRoleInGroups;
	
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
	
	public long getIdUser() {
		return idUser;
	}
	public void setIdUser(long idUser) {
		this.idUser = idUser;
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
	public List<Role> getMyRoles() {
		return myRoleInGroups;
	}
	public HashMap<UserGroup,Role> getRoleInGroups(){
		HashMap<UserGroup,Role> res=new HashMap<UserGroup, Role>();
		for(var i:this.myRoleInGroups){
			res.put(i.userGroup, i);
		}
		return res;
	}
	public void joinToGroup(UserGroup g, ERole rle)
	{
		if(this.myRoleInGroups==null) this.myRoleInGroups = new ArrayList<Role>();
		this.myRoleInGroups.add(new Role(g,this,rle));
	}
	
	
}
