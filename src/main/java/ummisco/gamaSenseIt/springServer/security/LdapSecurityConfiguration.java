package ummisco.gamaSenseIt.springServer.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;

import java.util.List;

@Configuration
public class LdapSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/", "/public/**", "/qameleo/**" /*,"/private/**"*/).permitAll().anyRequest()
                .authenticated().and().formLogin().and().logout().permitAll();

//	http
//			.authorizeRequests()
//				.anyRequest().fullyAuthenticated()
//				.and()
//			.formLogin();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.ldapAuthentication().userDnPatterns("uid={0},ou=people").groupSearchBase("ou=groups")
                .contextSource(contextSource()).passwordCompare().passwordEncoder(getPasswordEncoder()) //new LdapShaPasswordEncoder())
                .passwordAttribute("userPassword");
    }

    @Bean
    public DefaultSpringSecurityContextSource contextSource() {
        return new DefaultSpringSecurityContextSource(List.of("ldap://localhost:8389/"), "dc=springframework,dc=org");

        //return new DefaultSpringSecurityContextSource(Arrays.asList("ldaps://ldap.intranet.ird.fr:636"), "u=people,dc=global,dc=ird,dc=fr");
        //ldaps://ldap.intranet.ird.fr:636
        //u=people,dc=global,dc=ird,dc=fr
    }

    @Bean(name = "passwordEncoder")
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

//     http.authorizeRequests().antMatchers("/", "/home").permitAll().anyRequest().authenticated();
    // http.formLogin().loginPage("/login").permitAll().and().logout().logoutSuccessUrl("/");

}