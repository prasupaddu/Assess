package com.equifax.AirPollutionApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.equifax.AirPollutionApp.entity.UserRegistration;
import com.equifax.AirPollutionApp.repository.UserAuthenticationRepository;
import com.equifax.AirPollutionApp.util.JWTUtil;
import com.equifax.AirPollutionApp.configuration.UserPrincipal;
import com.equifax.AirPollutionApp.dto.LoginDTO;
@Service
public class JWTService implements UserDetailsService {
    @Autowired
	private UserAuthenticationRepository repository;
    @Autowired
    private JWTUtil util;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserRegistration registration=repository.findByusername(username);
		if(registration==null)
			throw new UsernameNotFoundException("No Records Found");
		return new UserPrincipal(registration);
	}
	
	public String createJwtToken(LoginDTO dto) throws Exception {
        String userName = dto.getUsername();
        String userPassword = dto.getPassword();
       

        UserDetails userDetails = loadUserByUsername(userName);
        UserRegistration registration=repository.findByusername(userName);
        if(registration.isActivated()) {
        String newGeneratedToken = util.generateToken(userDetails);
        return newGeneratedToken ;
        }
        else {
        	return "Your Profile is not activated! Please try to login after sometime";
        }
        

        //User user = userDao.findById(userName).get();
       
    }

}
