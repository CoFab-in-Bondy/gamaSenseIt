package ummisco.gamaSenseIt.springServer.data.model.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class UserGroup {

	@Id
	long groupId;
	
	@Column(length = 60)
	String name;
	
	@OneToMany
	List<Role> roles;
	
	public UserGroup() {
		super();
	}
	
	public UserGroup(String name) {
		super();
		this.name = name;
	}
	public long getGroupId() {
		return groupId;
	}
	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public void joinUser(User s, ERole r)
	{
		if(this.roles == null) this.roles = new ArrayList<Role>();
		this.roles.add(new Role(this,s,r));
	}
	
}
