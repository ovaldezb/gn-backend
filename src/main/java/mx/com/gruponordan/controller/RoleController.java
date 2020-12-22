package mx.com.gruponordan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.com.gruponordan.repository.RolesDAO;

@RestController
@RequestMapping("/api/role")
@CrossOrigin(origins = "http://localhost:3000")
public class RoleController {

	@Autowired
	RolesDAO rolerepo;
	
	@GetMapping
	public ResponseEntity<?> getAllRoles(){
		return ResponseEntity.ok(rolerepo.findAll());
	}
	
}
