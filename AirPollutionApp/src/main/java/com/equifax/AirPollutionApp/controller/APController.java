package com.equifax.AirPollutionApp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.equifax.AirPollutionApp.dto.LoginDTO;
import com.equifax.AirPollutionApp.dto.UserRegistrationDTO;
import com.equifax.AirPollutionApp.entity.UserRegistration;

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
	public ResponseEntity<String> registerUser(@RequestParam("file") MultipartFile file, @RequestParam String firstName,
			@RequestParam String lastName, @RequestParam String username, @RequestParam String password,
			@RequestParam String city, @RequestParam String email, @RequestParam String mobile) {
		String response = "";

		if (file.isEmpty()) {
			return new ResponseEntity<String>("Profile Pic Should be Updated", HttpStatus.BAD_REQUEST);
		}
		UserRegistrationDTO dto = new UserRegistrationDTO();
		dto.setFirstName(firstName);
		dto.setLastName(lastName);
		dto.setUsername(username);
		dto.setPassword(encoder.encode(password));
		dto.setCity(city);
		dto.setEmail(email);
		dto.setMobile(mobile);
		dto.setFileName(file.getName());
		try {
			dto.setBytes(file.getBytes());

			UserRegistration reg = service.registerUser(dto);
			return new ResponseEntity<String>("Registration Success", HttpStatus.CREATED);
		} catch (Exception ex) {
			return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("/authenticate")
	public ResponseEntity<String> loginUser(@RequestBody LoginDTO dto) throws Exception {
		try {
			manager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
		} catch (BadCredentialsException ex) {
			return new ResponseEntity<String>("Incorrect UserID or Password",HttpStatus.UNAUTHORIZED);
		}
		if(jwtservice.createJwtToken(dto).equals("Your Profile is not activated! Please try to login after sometime"))
		return new ResponseEntity<String>(jwtservice.createJwtToken(dto),HttpStatus.UNAUTHORIZED);
		else {
			return new ResponseEntity<String>(jwtservice.createJwtToken(dto),HttpStatus.CREATED);
		}
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
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/getCountries")
	public ResponseEntity<String> getCountries(HttpServletRequest request){
		String key=request.getHeader("key");
		if(key==null) {
			return new ResponseEntity<String>("KEY Should not be Empty",HttpStatus.BAD_REQUEST);
		}
		String uri = environment.getProperty("Countries-list");
		ResponseEntity<String> entity = restTemplate.getForEntity(uri+"?key="+key, String.class);
		return entity;
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/getStates/{country}")
	public ResponseEntity<String> getStates(HttpServletRequest request,@PathVariable("country") String country){
		String key=request.getHeader("key");
		if(key==null) {
			return new ResponseEntity<String>("KEY Should not be Empty",HttpStatus.BAD_REQUEST);
		}
		String uri = environment.getProperty("states-list");
		ResponseEntity<String> entity = restTemplate.getForEntity(uri+"?country="+country+"&key="+key, String.class);
		return entity;
	}
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/getCities/{country}/{state}")
	public ResponseEntity<String> getCities(HttpServletRequest request,@PathVariable("country") String country,@PathVariable("state") String state){
		String key=request.getHeader("key");
		if(key==null) {
			return new ResponseEntity<String>("KEY Should not be Empty",HttpStatus.BAD_REQUEST);
		}
		String uri = environment.getProperty("cities-list");
		ResponseEntity<String> entity = restTemplate.getForEntity(uri+"?state="+state+"+&country="+country+"&key="+key, String.class);
		return entity;
	}
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/getAirQuality/{country}/{state}/{city}")
	public ResponseEntity<String> getAirQuality(HttpServletRequest request,@PathVariable("country") String country,@PathVariable("state") String state,@PathVariable("city") String city){
		String key=request.getHeader("key");
		if(key==null) {
			return new ResponseEntity<String>("KEY Should not be Empty",HttpStatus.BAD_REQUEST);
		}
		String uri = environment.getProperty("city-airquality");
		ResponseEntity<String> entity = restTemplate.getForEntity(uri+"?city="+city+"&state="+state+"+&country="+country+"&key="+key, String.class);
		return entity;
		
	}
}
