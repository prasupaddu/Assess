package com.equifax.AirPollutionApp.service;

import java.util.ArrayList;
import java.util.List;

import org.jvnet.hk2.annotations.Service;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.equifax.AirPollutionApp.dto.LoginDTO;
import com.equifax.AirPollutionApp.dto.UserRegistrationDTO;
import com.equifax.AirPollutionApp.entity.UserRegistration;
@Service
public interface APService extends UserDetailsService {

	    UserRegistrationDTO registerUser(UserRegistrationDTO dto);
	    String  findUserByUserName(LoginDTO dto);
	    
	    List<UserRegistration> findAllUsers();
	    UserRegistrationDTO updateUser(int userId);
}
