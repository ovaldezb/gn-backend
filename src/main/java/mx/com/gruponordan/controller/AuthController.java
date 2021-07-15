package mx.com.gruponordan.controller;

import java.util.Date;

import javax.validation.Valid;


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


import mx.com.gruponordan.model.JwtResponse;
import mx.com.gruponordan.model.LoginRequest;
import mx.com.gruponordan.model.MessageResponse;
import mx.com.gruponordan.model.SignupRequest;
import mx.com.gruponordan.model.User;
import mx.com.gruponordan.repository.BitacoraDAO;
import mx.com.gruponordan.repository.UserRepository;
import mx.com.gruponordan.security.jwt.JwtUtils;
import mx.com.gruponordan.security.service.UserDetailsImpl;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins =  "*")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

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
			

			if (userDetails.isActivo()) {
				
				return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(),
						  new Date().getTime(),userDetails.getArea(),userDetails.getCodeArea() ));
			} else {
				return ResponseEntity.badRequest().body(new MessageResponse("Error:no activo"));
			}
		} catch (AuthenticationException aex) {
			System.out.println(aex);
			return new ResponseEntity<>( "error:Bad Credentias",HttpStatus.UNAUTHORIZED);
		}

	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return new ResponseEntity<>(new MessageResponse("Ya existe"), HttpStatus.CONFLICT);
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(),encoder.encode(signUpRequest.getPassword()), signUpRequest.isActivo(), signUpRequest.getNombre(),
				signUpRequest.getApellido(), signUpRequest.getArea());
		
		User userSaved = userRepository.save(user);
		if (userSaved != null) {
			return ResponseEntity.ok(userSaved);
		} else {
			return ResponseEntity.badRequest().body(new MessageResponse("Error al guardar"));
		}

	}

}
