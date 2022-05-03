package com.equifax.AirPollutionApp.service;


import java.util.List;

import org.jvnet.hk2.annotations.Service;



import com.equifax.AirPollutionApp.dto.UserRegistrationDTO;
import com.equifax.AirPollutionApp.entity.UserRegistration;
@Service
public interface APService {

	    UserRegistration registerUser(UserRegistrationDTO dto);
	    List<UserRegistration> findAllUsers();
	    UserRegistrationDTO updateUser(int userId);
}
