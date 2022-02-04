package ummisco.gamaSenseIt.springServer.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ummisco.gamaSenseIt.springServer.data.repositories.IUserRepository;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
	@Autowired
	UserDetailService userDetailsService;

    @Autowired
    IUserRepository userRepo;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable().authorizeRequests()
                .antMatchers( "/auth/**").permitAll()
                .antMatchers("/private/**", "/public/**").authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                /*.antMatchers("/**")
                .permitAll()*/
                /*
                .and()
            .formLogin()
                .permitAll()
                .and()
            .logout()
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/")
                .permitAll()*/
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        /**
         http
         .authorizeRequests()
         .antMatchers("/", "/public/**", "/qameleo/**" ,"/private/**")
         .permitAll()
         .anyRequest()
         .authenticated()
         .and()
         .formLogin()
         .and()
         .logout()
         .permitAll();
         **/
    }

    @Autowired
    void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
        .userDetailsService(userDetailsService)
        .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
