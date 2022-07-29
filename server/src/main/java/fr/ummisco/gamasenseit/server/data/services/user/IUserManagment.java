package fr.ummisco.gamasenseit.server.data.services.user;

import fr.ummisco.gamasenseit.server.data.model.user.User;
import fr.ummisco.gamasenseit.server.data.model.user.UserPrivilege;

public interface IUserManagment {
	User createUser(String givenName, String lastName, String mail, String passwd, UserPrivilege privilege);
	User createIfNotExistUser(String givenName, String lastName, String mail, String passwd, UserPrivilege privilege);
}
