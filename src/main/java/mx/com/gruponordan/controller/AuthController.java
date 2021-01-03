package mx.com.gruponordan.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.com.gruponordan.model.ERole;
import mx.com.gruponordan.model.JwtResponse;
import mx.com.gruponordan.model.LoginRequest;
import mx.com.gruponordan.model.MessageResponse;
import mx.com.gruponordan.model.Role;
import mx.com.gruponordan.model.SignupRequest;
import mx.com.gruponordan.model.User;
import mx.com.gruponordan.repository.BitacoraDAO;
import mx.com.gruponordan.repository.RoleRepository;
import mx.com.gruponordan.repository.UserRepository;
import mx.com.gruponordan.security.jwt.JwtUtils;
import mx.com.gruponordan.security.service.UserDetailsImpl;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

	private static Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	BitacoraDAO bitacorarepo;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateJwtToken(authentication);

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
					.collect(Collectors.toList());

			if (userDetails.isActivo()) {
				return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(),
						userDetails.getEmail(), roles, new Date().getTime()));
			} else {
				return ResponseEntity.badRequest().body(new MessageResponse("Error:no activo"));
			}
		} catch (AuthenticationException aex) {
			return new ResponseEntity<>( "error:Bad Credentias",HttpStatus.UNAUTHORIZED);
		}

	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			// return ResponseEntity.badRequest().body(new MessageResponse("Error: Username
			// is already taken!"));

			return new ResponseEntity<>(new MessageResponse("Ya existe"), HttpStatus.CONFLICT);
		}

		/*
		 * if (userRepository.existsByEmail(signUpRequest.getEmail())) { return
		 * ResponseEntity .badRequest() .body(new
		 * MessageResponse("Error: Email is already in use!")); }
		 */

		// Create new user's account
		User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()), signUpRequest.isActivo(), signUpRequest.getNombre(),
				signUpRequest.getApellido(), signUpRequest.getNoEmpleado());
		logger.info(signUpRequest.toString());
		Role[] strRoles = signUpRequest.getRoles();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			for (Role role : strRoles) {
				switch (role.getName()) {
				case ROLE_ADMIN:
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case ROLE_MODERATOR:
					Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			}

		}
		user.setRoles((Role[]) roles.toArray());
		User userSaved = userRepository.save(user);
		if (userSaved != null) {
			return ResponseEntity.ok(userSaved);
		} else {
			return ResponseEntity.badRequest().body(new MessageResponse("Error al guardar"));
		}

	}

}
