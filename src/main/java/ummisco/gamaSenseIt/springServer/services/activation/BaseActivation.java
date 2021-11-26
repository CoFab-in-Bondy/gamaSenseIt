package ummisco.gamaSenseIt.springServer.services.activation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ummisco.gamaSenseIt.springServer.data.model.user.UserPrivilege;
import ummisco.gamaSenseIt.springServer.data.services.user.IUserManagment;

@Service("BasicActivation")
public class BaseActivation implements IActivation {

	@Autowired
	IUserManagment userM;


	@Override
	public void activate() {
		String password = new BCryptPasswordEncoder().encode("123456");
		userM.createIfNotExistUser("luis","bondel","nmarilleau@gmail.com",password,UserPrivilege.ADMIN);

		String passwordEmpty = new BCryptPasswordEncoder().encode(" ");
		userM.createIfNotExistUser(" "," "," ",passwordEmpty,UserPrivilege.ADMIN);
	}
}
