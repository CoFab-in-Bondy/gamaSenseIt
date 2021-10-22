package ummisco.gamaSenseIt.springServer.data.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ummisco.gamaSenseIt.springServer.data.model.user.User;
import ummisco.gamaSenseIt.springServer.data.model.user.UserPrivilege;
import ummisco.gamaSenseIt.springServer.data.repositories.IUserRepository;

@Service
public class UserManagment implements IUserManagment{

	@Autowired
	IUserRepository repo;
	@Override
	public void createUser(String firstname, String lastName, String mail, String password, UserPrivilege privilege) {
		
		User u = new User( firstname,  lastName,  mail,  password, privilege);
		repo.save(u);
		
	}

}
