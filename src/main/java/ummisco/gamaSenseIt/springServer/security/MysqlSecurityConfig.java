package ummisco.gamaSenseIt.springServer.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class MysqlSecurityConfig extends WebSecurityConfigurerAdapter {
    
	@Autowired
	UserDetailService userDetailsService;
	
	protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/", "/public/**", "/qameleo/**" /*,"/private/**"*/).permitAll().anyRequest()
                .authenticated().and().formLogin().and().logout().permitAll();

    }
    
    @Autowired
    void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
      auth
      .userDetailsService(userDetailsService)
      .passwordEncoder(new BCryptPasswordEncoder());
    }
        
}