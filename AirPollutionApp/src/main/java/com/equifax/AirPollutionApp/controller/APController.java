package com.equifax.AirPollutionApp.controller;

import java.io.InputStream;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.equifax.AirPollutionApp.dto.LoginDTO;
import com.equifax.AirPollutionApp.dto.UserRegistrationDTO;
import com.equifax.AirPollutionApp.entity.UserRegistration;
import com.equifax.AirPollutionApp.model.UserLoginModel;
import com.equifax.AirPollutionApp.model.UserRegistrationModel;
import com.equifax.AirPollutionApp.service.APService;

@RestController
@RequestMapping("/app")
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class APController {
	@Autowired
	private BCryptPasswordEncoder encoder; 
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private Environment environment;
	@Autowired
	private APService service;
    @RequestMapping(value="/register",method=RequestMethod.POST,consumes= {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_UTF8_VALUE})
	public String registerUser(@RequestParam("file") MultipartFile file,@RequestParam String username,@RequestParam String password,@RequestParam String city,@RequestParam String email,@RequestParam String mobile){
		String response="";
		//System.out.println(firstName+" "+lastName);
		
		if(file == null) {
			return "Please upload profile image.";
		}
		//InputStream firstName=file.getInputStream();
		//System.out.println(firstName.read()));
		
		  UserRegistrationDTO dto=new UserRegistrationDTO();
			/*
			 * dto.setFirstName(firstName); dto.setLastName(lastName);
			 */
		  dto.setUsername(username);
		  dto.setPassword(encoder.encode(password));
		  dto.setCity(city);
		  dto.setEmail(email);
		  dto.setMobile(mobile);
		  dto.setFileName(file.getName()); 
		  try {
		  dto.setBytes(file.getBytes());
		  
		  UserRegistrationDTO rdto=service.registerUser(dto);
		  response="Register success"; } catch(Exception ex) {
		  response="Register Failed!!try again";
		 }
		 
		return response;
	}
	
    @PostMapping("/login")
    public String loginUser(@RequestBody UserLoginModel model) {
    	ModelMapper mapper=new ModelMapper();
        LoginDTO dto=mapper.map(model, LoginDTO.class)   ;
       
        return  service.findUserByUserName(dto);
    }
    //@PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user/{userid}")
    public ResponseEntity<UserRegistrationDTO> activateUser(@PathVariable("userid") int userId) {
    	
    return ResponseEntity.ok().body(service.updateUser(userId))	;
    }
    @GetMapping("/allUsers")
    public List<UserRegistration> getAllUsers() {
    	return service.findAllUsers();
    }
    @GetMapping("/activateUser")
    public String admin() {
    	return "Success";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("callAirAPI")
    public ResponseEntity<String> adminLogin() {
       	
    	String uri=environment.getProperty("air-api");
    	
    	ResponseEntity<String> entity=restTemplate.getForEntity(uri, String.class);
    	return entity;
    }
}
/*
 * https://cloud.google.com/storage/docs/creating-buckets
 * https://cloud.google.com/storage/docs/creating-buckets
 * https://cloud.google.com/storage/docs/introduction
 */