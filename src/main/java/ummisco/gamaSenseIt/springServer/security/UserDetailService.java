package ummisco.gamaSenseIt.springServer.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ummisco.gamaSenseIt.springServer.data.model.user.User;
import ummisco.gamaSenseIt.springServer.data.repositories.IUserRepository;

@Service
public class UserDetailService implements UserDetailsService {

	@Autowired
	IUserRepository usersRep;

	@Override
	public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
	    User user = usersRep.findByMail(mail);
	    List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
	    grantedAuthorities.add(new SimpleGrantedAuthority(user.getPrivilege().toString()));
	    return new org.springframework.security.core.userdetails.User(user.getMail()
	    		, user.getPassword(), grantedAuthorities);
	}

}