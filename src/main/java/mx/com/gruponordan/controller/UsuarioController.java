package mx.com.gruponordan.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
public class UsuarioController {

	@Autowired
	UserRepository userrepo;

	@GetMapping
	public ResponseEntity<?> getAllUsers() {
		return ResponseEntity.ok(userrepo.findByActivo(true));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getUserById(@PathVariable final String id){
		return ResponseEntity.ok(userrepo.findById(id));
	}
	
	@PostMapping
	public ResponseEntity<?> updateUser(@RequestBody final User user) {
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
			usu.setEmail(user.getEmail());
			usu.setPassword(user.getPassword());
			usu.setRoles(user.getRoles());
			return userrepo.save(usu);
		}).orElseGet(() -> {
			return new User();
		}));
	}
	
	@DeleteMapping()
	public ResponseEntity<?> deleteUserById(@PathVariable final String id){
		return ResponseEntity.ok(userrepo.findById(id).map(usu -> {
			usu.setActivo(false);
			return userrepo.save(usu);
		}).orElseGet(() -> {
			return new User();
		}));
	}
	
	

}
