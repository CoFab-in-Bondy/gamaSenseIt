package ummisco.gamaSenseIt.springServer.data.services.user;

import ummisco.gamaSenseIt.springServer.data.model.user.User;
import ummisco.gamaSenseIt.springServer.data.model.user.UserPrivilege;

public interface IUserManagment {
	User createUser(String givenName, String lastName, String mail, String passwd, UserPrivilege privilege);
	User createIfNotExistUser(String givenName, String lastName, String mail, String passwd, UserPrivilege privilege);
}
