package com.spares.dealer.configuration;

import com.spares.dealer.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter{

	@Autowired
	private MyUserDetailsService myUserDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myUserDetailsService);
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors().disable();

		http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/dealer").hasRole("DEALER")
                .anyRequest().authenticated() .and().formLogin();
        http.httpBasic();

	}
	
	@Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}