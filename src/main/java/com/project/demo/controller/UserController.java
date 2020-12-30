package com.project.demo.controller;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.demo.dto.JWTResponse;
import com.project.demo.dto.RegisterForm;
import com.project.demo.dto.StatusMessageDto;
import com.project.demo.entity.ERole;
import com.project.demo.entity.Role;
import com.project.demo.entity.User;
import com.project.demo.repository.RoleRepository;
import com.project.demo.repository.UserRepository;
import com.project.demo.security.jwt.JwtUtils;
import com.project.demo.security.service.UserDetailsImpl;

@RestController
@RequestMapping("/api/users")
public class UserController {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> login(@RequestBody User user) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		Set<String> roles = userDetails.getAuthorities().stream().map(role -> role.getAuthority())
				.collect(Collectors.toSet());

		return ResponseEntity.ok(new JWTResponse(jwt, userDetails.getUsername(), userDetails.getEmail(), roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> register(@RequestBody RegisterForm dto) {
		User user = userRepository.findByUsername(dto.getUsername());
		StatusMessageDto response = new StatusMessageDto();
		if (user != null) {
			response.setMessage("Username telah dipakai");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(response);
		}

//		register account
		User userCreated = new User(dto.getUsername(), dto.getEmail(), passwordEncoder.encode(dto.getPassword()));

		Set<String> roleString = dto.getRole();
		Set<Role> roles = new HashSet<>();

		if (roleString == null) {
			try {
				Role userRole = roleRepository.findByName(ERole.USER);
				roles.add(userRole);
			} catch (RuntimeException e) {
				// TODO: handle exception
				throw new RuntimeException("Role not found.");
			}
		} else {
			roleString.forEach(role -> {
				switch (role) {
				case "admin":
					try {
						Role adminRole = roleRepository.findByName(ERole.ADMIN);
						roles.add(adminRole);
					} catch (RuntimeException e) {
						// TODO: handle exception
						throw new RuntimeException("Role not found.");
					}
					break;

				default:
					try {
						Role userRole = roleRepository.findByName(ERole.USER);
						roles.add(userRole);
					} catch (RuntimeException e) {
						// TODO: handle exception
						throw new RuntimeException("Role not found.");
					}
					break;
				}
			});
		}
		
		userCreated.setRoles(roles);
		userRepository.save(userCreated);

		response.setMessage("User created!");
		response.setData(userCreated);

		return ResponseEntity.ok(response);
	}
}
