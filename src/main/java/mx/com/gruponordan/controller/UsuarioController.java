package mx.com.gruponordan.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.com.gruponordan.model.MessageResponse;
import mx.com.gruponordan.model.User;
import mx.com.gruponordan.repository.UserRepository;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "http://localhost:3000")
public class UsuarioController {

	@Autowired
	UserRepository userrepo;
	
	@Autowired
	PasswordEncoder encoder;

	@GetMapping
	public ResponseEntity<?> getAllUsers() {
		List<User> users = userrepo.findAll().stream().map(u ->{
			User userRet =  new User(u.getId(), u.getUsername(),u.getNombre(),u.getApellido(),u.isActivo(), u.getArea());
			userRet.setPassword("");
			return userRet;
		}).collect(Collectors.toList());
		return ResponseEntity.ok(users);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getUserById(@PathVariable final String id){
		return ResponseEntity.ok(userrepo.findById(id));
	}
	
	@PostMapping
	public ResponseEntity<?> saveUser(@RequestBody final User user) {
		User us = userrepo.save(user);
		if (us != null) {
			return ResponseEntity.ok(us);
		} else {
			return ResponseEntity.badRequest().body(new MessageResponse("error al insertar"));
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateUser(@PathVariable final String id, @RequestBody final User user) {
		
		return ResponseEntity.ok(userrepo.findById(id).map(usu -> {
			usu.setUsername(user.getUsername());
			//usu.setPassword(encoder.encode(user.getPassword()));
			usu.setNombre(user.getNombre());
			usu.setApellido(user.getApellido());
			usu.setActivo(user.isActivo());
			usu.setArea(user.getArea());
			if(!usu.getPassword().equals("")) {
				usu.setPassword(encoder.encode(user.getPassword()));
			}
			return userrepo.save(usu);
		}).orElseGet(() -> {
			return new User();
		}));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUserById(@PathVariable final String id){
		userrepo.deleteById(id);
		return ResponseEntity.ok(new MessageResponse("eliminado")) ;
		
	}
	
	

}
