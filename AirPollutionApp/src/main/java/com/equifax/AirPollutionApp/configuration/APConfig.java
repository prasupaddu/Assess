package com.equifax.AirPollutionApp.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class APConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService service;
	/*
	 * @Bean public AuthenticationProvider getAuthenticationProvider() {
	 * 
	 * DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
	 * provider.setUserDetailsService(service);
	 * provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance()); return
	 * provider; }
	 */
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		
		return super.authenticationManagerBean();
	}
	

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
				
		auth.userDetailsService(service).passwordEncoder(passwordEncoder());
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	/*
	 * @Bean public PasswordEncoder passwordEncoder() { return
	 * PasswordEncoderFactories.createDelegatingPasswordEncoder();
	 * 
	 * 
	 * }
	 */
	
	
	  @Override 
	  protected void configure(HttpSecurity http) throws Exception { //
	  //TODO Auto-generated method stub
		
		/* http.cors().disable(); http.csrf().disable(); */
		 
			
			
			
		
		  http.authorizeHttpRequests().antMatchers(HttpMethod.GET,"/app/admin").
		  hasAnyRole("USER");
		 
			 
			 
			  
	  
	  super.configure(http);
	  
	  }
	 
	 
	  @Override
	  public void configure(WebSecurity web) throws Exception {
			 web.ignoring().antMatchers(HttpMethod.POST, "/app/register"); 
			 }
	
	
	
	/*
	 * @Bean
	 * 
	 * @Override protected UserDetailsService userDetailsService() { // TODO
	 * Auto-generated method stub List<UserDetails> userDetails=new
	 * ArrayList<UserDetails>();
	 * userDetails.add(User.withDefaultPasswordEncoder().username("prasanna").
	 * password("prasanna").build()); return new
	 * InMemoryUserDetailsManager(userDetails); }
	 */

	
}
