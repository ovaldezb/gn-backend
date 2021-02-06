package mx.com.gruponordan.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.com.gruponordan.model.MateriaPrimaDisponible;
import mx.com.gruponordan.repository.MatPrimaDispDAO;

@RestController
@RequestMapping("/api/matprimdisp")
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "*")
public class MateriaPrimaDisponibleController {
	
	@Autowired
	MatPrimaDispDAO repoMatPrimDisp;
	
	@GetMapping
	public ResponseEntity<?> getAllMPDis(){
		return ResponseEntity.ok(repoMatPrimDisp.findAll());
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<?> getMPDisByCodigo(@PathVariable final String codigo){
		Optional<MateriaPrimaDisponible> mpdis = repoMatPrimDisp.findByCodigo(codigo);
		if(mpdis.isPresent()) {
			return ResponseEntity.ok(mpdis.get());
		}else {
			return ResponseEntity.badRequest().body("error");
		}
	}
	
	@GetMapping("/filter/{descripcion}")
	public ResponseEntity<?> getMPDispFilter(@PathVariable final String descripcion){
		return ResponseEntity.ok(repoMatPrimDisp.findByDescripcionLike(descripcion));
	}

}
