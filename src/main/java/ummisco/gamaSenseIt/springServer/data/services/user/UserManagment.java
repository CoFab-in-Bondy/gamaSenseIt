package ummisco.gamaSenseIt.springServer.data.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ummisco.gamaSenseIt.springServer.data.model.user.User;
import ummisco.gamaSenseIt.springServer.data.model.user.UserPrivilege;
import ummisco.gamaSenseIt.springServer.data.repositories.IUserRepository;

@Service
public class UserManagment implements IUserManagment{

	@Autowired
	IUserRepository repo;

	@Autowired
	PasswordEncoder encoder;

	@Override
	public User createUser(String firstname, String lastName, String mail, String password, UserPrivilege privilege) {
		String hash = encoder.encode(password);
		User user = new User( firstname,  lastName,  mail, hash, privilege);
		repo.save(user);
		return user;
	}

	@Override
	public User createIfNotExistUser(String firstname, String lastName, String mail, String password, UserPrivilege privilege) {
		var user = repo.findByMail(mail);
		return user == null? createUser(firstname,  lastName,  mail,  password, privilege): user;
	}

}
