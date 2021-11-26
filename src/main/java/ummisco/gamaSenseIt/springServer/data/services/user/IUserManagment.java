package ummisco.gamaSenseIt.springServer.data.services.user;

import ummisco.gamaSenseIt.springServer.data.model.user.UserPrivilege;

public interface IUserManagment {
	void createUser(String givenName, String lastName, String mail, String passwd, UserPrivilege privilege);
	void createIfNotExistUser(String givenName, String lastName, String mail, String passwd, UserPrivilege privilege);
}
