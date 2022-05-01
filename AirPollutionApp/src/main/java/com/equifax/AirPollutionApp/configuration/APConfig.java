package com.equifax.AirPollutionApp.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class APConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService service;
	@Autowired
	private JWTAuthenticationEntryPoint entrypoint;
	@Autowired
	private JWTRequestFilter filter;
	
	@Bean
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
	
	
	
	
	  @Override 
	  protected void configure(HttpSecurity http) throws Exception { //
	  //TODO Auto-generated method stub
		  http.cors();
			http.csrf().disable();
			http.authorizeRequests().antMatchers("/app/authenticate","/app/register").permitAll()
			.antMatchers(HttpHeaders.ALLOW).permitAll()
			.anyRequest().authenticated()
			.and()
			.exceptionHandling().authenticationEntryPoint(entrypoint)
	        .and()
	        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	         ;
			
			http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
		
		 
			 
			 
			  
	  
	  //super.configure(http);
	  
	  }
	 
	 
	 
	
	
	

	
}
