package ummisco.gamaSenseIt.springServer.data.model.user;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

enum ERole{
	ADMIN,
	VIEWER,
	OWNER
}

@Entity
public class Role {
	@Id
	long roleId;

	@ManyToOne
	@JoinColumn(name = "group_id")
	UserGroup userGroup;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	User user;
	ERole role;
	
	
	public Role() {}
	
	public Role(UserGroup group, User user, ERole role) {
		super();
		this.userGroup = group;
		this.user = user;
		this.role = role;
	}
	
	public UserGroup getUserGroup() {
		return userGroup;
	}
	public void setUserGroup(UserGroup group) {
		this.userGroup = group;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public ERole getRole() {
		return role;
	}
	public void setRole(ERole role) {
		this.role = role;
	}
	
	
	
}
