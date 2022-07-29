package fr.ummisco.gamasenseit.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import fr.ummisco.gamasenseit.server.data.repositories.IUserRepository;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    IUserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        var user = userRepo.findByMail(mail).orElseThrow(() -> {throw new UsernameNotFoundException("Not found");});
        return User.builder()
                .username(user.getMail())
                .password(user.getPassword())
                .authorities(user.getPrivilege().toString())
                .build();
    }
}
