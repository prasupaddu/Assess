package com.equifax.AirPollutionApp.service;

import java.util.List;

import org.jvnet.hk2.annotations.Service;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.equifax.AirPollutionApp.configuration.UserPrincipal;
import com.equifax.AirPollutionApp.dto.LoginDTO;
import com.equifax.AirPollutionApp.dto.UserRegistrationDTO;
import com.equifax.AirPollutionApp.entity.UserRegistration;
import com.equifax.AirPollutionApp.repository.UserAuthenticationRepository;

@Component
public class APServiceImpl implements APService {
    @Autowired
	private UserAuthenticationRepository repository;
	@Override
	public UserRegistrationDTO registerUser(UserRegistrationDTO dto) {
		/*
		 * ModelMapper mapper=new ModelMapper(); UserRegistration reg=mapper.map(dto,
		 * UserRegistration.class);
		 */
		 UserRegistration reg=new  UserRegistration();
		reg.setUsername(dto.getUsername());
		 reg.setCity(dto.getCity());
		 reg.setEmail(dto.getEmail());
		 reg.setMobile(dto.getMobile());
		 reg.setData(dto.getBytes());
		 reg.setFileName(dto.getFileName());
		 reg.setPassword(dto.getPassword());
		UserRegistration regc=repository.save(reg);
		
				return dto;
	}

	
	@Override
	public String findUserByUserName(LoginDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserRegistration> findAllUsers() {
		return repository.findAll();
		
		
	}

	@Override
	public UserRegistrationDTO updateUser(int userId) {
		UserRegistration reg=repository.findById(userId);
		if(reg!=null)
			reg.setActivated(true);
		UserRegistration ureg=repository.save(reg);
		ModelMapper mapper=new ModelMapper();
		UserRegistrationDTO dto=mapper.map(ureg, UserRegistrationDTO.class);
		return dto;
	}
	
	

}
