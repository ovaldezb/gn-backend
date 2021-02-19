package mx.com.gruponordan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.com.gruponordan.repository.ProductoEntregadoDAO;

@RestController
@RequestMapping("/api/prodent")
@CrossOrigin(origins = "*")
public class ProductoEntregadoController {

	@Autowired
	ProductoEntregadoDAO repoPE;
	
	@GetMapping
	public ResponseEntity<?> getProductosEntregados(){
		return ResponseEntity.ok(repoPE.findAll());
	}
	
}
