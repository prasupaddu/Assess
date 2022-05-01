package com.equifax.AirPollutionApp.controller;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import com.equifax.AirPollutionApp.service.JWTService;

@RestController
@RequestMapping("/app")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class APController {
	@Autowired
	private BCryptPasswordEncoder encoder;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private Environment environment;
	@Autowired
	private APService service;
	@Autowired
	private JWTService jwtservice;
	@Autowired
	private AuthenticationManager manager;

	@PostMapping("/register")
	public String registerUser(@RequestParam("file") MultipartFile file, @RequestParam String username,
			@RequestParam String password, @RequestParam String city, @RequestParam String email,
			@RequestParam String mobile) {
		String response = "";

		if (file == null) {
			return "Please upload profile image.";
		}
		UserRegistrationDTO dto = new UserRegistrationDTO();
		dto.setUsername(username);
		dto.setPassword(encoder.encode(password));
		dto.setCity(city);
		dto.setEmail(email);
		dto.setMobile(mobile);
		dto.setFileName(file.getName());
		try {
			dto.setBytes(file.getBytes());

			UserRegistrationDTO rdto = service.registerUser(dto);
			response = "Register success";
		} catch (Exception ex) {
			response = "Register Failed!!try again";
		}

		return response;
	}

	@PostMapping("/authenticate")
	public String loginUser(@RequestBody LoginDTO dto) throws Exception {
		try {
			manager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
		} catch (BadCredentialsException ex) {
			return "Incorrect UserID or Password";
		}
		return jwtservice.createJwtToken(dto);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/activate/{userid}")
	public ResponseEntity<UserRegistrationDTO> activateUser(@PathVariable("userid") int userId) {

		return ResponseEntity.ok().body(service.updateUser(userId));
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/allUsers")
	public List<UserRegistration> getAllUsers() {
		return service.findAllUsers();
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/callAirAPI")
	public ResponseEntity<String> adminLogin() {

		String uri = environment.getProperty("air-api");

		ResponseEntity<String> entity = restTemplate.getForEntity(uri, String.class);
		return entity;
	}
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/getStates/{country}")
	public ResponseEntity<String> getStates(HttpServletRequest request,@PathVariable("country") String country){
		String key=request.getHeader("key");
		String uri = environment.getProperty("states-list");
		ResponseEntity<String> entity = restTemplate.getForEntity(uri+"?country="+country+"&key="+key, String.class);
		return entity;
	}
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/getCities/{country}/{state}")
	public ResponseEntity<String> getCities(HttpServletRequest request,@PathVariable("country") String country,@PathVariable("state") String state){
		String key=request.getHeader("key");
		String uri = environment.getProperty("cities-list");
		ResponseEntity<String> entity = restTemplate.getForEntity(uri+"?state="+state+"+&country="+country+"&key="+key, String.class);
		return entity;
	}
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/getAirQuality/{country}/{state}/{city}")
	public ResponseEntity<String> getAirQuality(HttpServletRequest request,@PathVariable("country") String country,@PathVariable("state") String state,@PathVariable("city") String city){
		String key=request.getHeader("key");
		String uri = environment.getProperty("city-airquality");
		ResponseEntity<String> entity = restTemplate.getForEntity(uri+"?city="+city+"&state="+state+"+&country="+country+"&key="+key, String.class);
		return entity;
		
	}
}
/*
 * https://cloud.google.com/storage/docs/creating-buckets
 * https://cloud.google.com/storage/docs/creating-buckets
 * https://cloud.google.com/storage/docs/introduction
 */