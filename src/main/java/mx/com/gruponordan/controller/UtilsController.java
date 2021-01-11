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
@CrossOrigin(origins = "http://localhost:3000")
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
	
	/*
	 * @PostMapping public ResponseEntity<?> saveUnidad(@RequestBody final
	 * UnidadMedida unidadmedida){ UnidadMedida un = unidadrepo.save(unidadmedida);
	 * if(un!=null) { return ResponseEntity.ok(un); }else { return
	 * ResponseEntity.badRequest().body(new
	 * MessageResponse("no se pudo insertar la uniadd")); } }
	 */
	
	/*
	 * @PutMapping("/{id}") public ResponseEntity<?> updateUnidad(@PathVariable
	 * final String id, @RequestBody final UnidadMedida umi){ Optional<UnidadMedida>
	 * um = unidadrepo.findById(id); if(um.isPresent()) { UnidadMedida uma =
	 * um.get(); uma.setUnidadMedida(umi.getUnidadMedida()); unidadrepo.save(uma);
	 * return ResponseEntity.ok(new MessageResponse("Se actualizo con exito"));
	 * }else { return ResponseEntity.badRequest().body(new
	 * MessageResponse("error no se encontro la unidad a actualizar")); } }
	 * 
	 * @DeleteMapping("/{id}") public ResponseEntity<?>
	 * deleteUnidadMedida(@PathVariable final String id){ unidadrepo.deleteById(id);
	 * return ResponseEntity.ok(new MessageResponse("Se elimino")); }
	 */
	
}
