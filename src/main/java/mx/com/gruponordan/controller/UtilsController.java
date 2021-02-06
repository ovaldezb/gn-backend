package mx.com.gruponordan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.com.gruponordan.repository.AreasDAO;
import mx.com.gruponordan.repository.UnidadesDAO;

@RestController
@RequestMapping("/api/utils")
@CrossOrigin(origins = "*")
public class UtilsController {

	@Autowired
	UnidadesDAO unidadrepo;
	
	@Autowired
	AreasDAO repoarea;
	
	@GetMapping("/unidad")
	public ResponseEntity<?> getAllUnidades(){
		return ResponseEntity.ok(unidadrepo.findAll());
	}
	
	@GetMapping("/areas")
	public ResponseEntity<?> getAllAreas(){
		return ResponseEntity.ok(repoarea.findAll());
	}
	
}
