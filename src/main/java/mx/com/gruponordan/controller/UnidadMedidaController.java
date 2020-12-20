package mx.com.gruponordan.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import mx.com.gruponordan.model.UnidadMedida;
import mx.com.gruponordan.repository.UnidadesDAO;

@RestController
@RequestMapping("/api/unidad")
@CrossOrigin(origins = "http://localhost:3000")
public class UnidadMedidaController {

	@Autowired
	UnidadesDAO unidadrepo;
	
	@GetMapping
	public ResponseEntity<?> getAllUnidades(){
		return ResponseEntity.ok(unidadrepo.findAll());
	}
	
	@PostMapping
	public ResponseEntity<?> saveUnidad(@RequestBody final UnidadMedida unidadmedida){
		UnidadMedida un = unidadrepo.save(unidadmedida);
		if(un!=null) {
			return ResponseEntity.ok(un);
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("no se pudo insertar la uniadd"));
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateUnidad(@PathVariable final String id, @RequestBody final UnidadMedida umi){
		Optional<UnidadMedida> um = unidadrepo.findById(id);
		if(um.isPresent()) {
				UnidadMedida uma = um.get();
				uma.setUnidadMedida(umi.getUnidadMedida());
				unidadrepo.save(uma);
				return ResponseEntity.ok(new MessageResponse("Se actualizo con exito"));
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("error no se encontro la unidad a actualizar"));
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUnidadMedida(@PathVariable final String id){
		unidadrepo.deleteById(id);
		return ResponseEntity.ok(new MessageResponse("Se elimino"));
	}
	
}
