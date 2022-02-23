package ummisco.gamaSenseIt.springServer.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ummisco.gamaSenseIt.springServer.data.repositories.IUserRepository;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    IUserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        var user = userRepo.findByMail(mail);
        if (user == null) throw new UsernameNotFoundException("Not found");
        return User.builder()
                .username(user.getMail())
                .password(user.getPassword())
                .authorities(user.getPrivilege().toString())
                .build();
    }
}
