package com.equifax.AirPollutionApp.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.equifax.AirPollutionApp.entity.UserRegistration;

@Repository
@Transactional
public interface UserAuthenticationRepository extends JpaRepository<UserRegistration, Integer> {
	
	UserRegistration findByusername(String username);
	UserRegistration findById(int id);

}
