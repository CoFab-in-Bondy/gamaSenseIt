package fr.ummisco.gamasenseit.server.data.services.user;

import fr.ummisco.gamasenseit.server.data.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fr.ummisco.gamasenseit.server.data.model.user.User;
import fr.ummisco.gamasenseit.server.data.model.user.UserPrivilege;

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
		return repo.findByMail(mail).orElseGet(()->createUser(firstname,  lastName,  mail,  password, privilege));
	}

}
