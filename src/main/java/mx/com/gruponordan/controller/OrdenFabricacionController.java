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
import mx.com.gruponordan.model.OrdenFabricacion;
import mx.com.gruponordan.repository.OrdenFabricacionDAO;

@RestController
@RequestMapping("/api/ordenfab")
public class OrdenFabricacionController {

	@Autowired
	OrdenFabricacionDAO repoOF;
	
	@GetMapping()
	public ResponseEntity<?> getAllOF(){
		return ResponseEntity.ok(repoOF.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getOFById(@PathVariable final String idOrdenFabricacion){
		Optional<OrdenFabricacion> of = repoOF.findById(idOrdenFabricacion);
		if(of.isPresent()) {
			return ResponseEntity.ok(of);
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("status:error"));
		}
	}
	
	@PostMapping()
	public ResponseEntity<?> saveOF(@RequestBody final OrdenFabricacion ordenFabricacion) {
		return ResponseEntity.ok(repoOF.save(ordenFabricacion));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateOF(@PathVariable final String idOrdenFabricacion, @RequestBody final OrdenFabricacion ordenFabricacion){
		Optional<OrdenFabricacion> off = repoOF.findById(idOrdenFabricacion);
		if(off.isPresent()) {
			OrdenFabricacion ofu = off.get();
			ofu.setClave(ordenFabricacion.getClave());
			ofu.setNombreProducto(ordenFabricacion.getNombreProducto());
			ofu.setNumLote(ordenFabricacion.getNumLote());
			ofu.setPiezasAFabricar(ordenFabricacion.getPiezasAFabricar());
			ofu.setObservaciones(ordenFabricacion.getObservaciones());
			ofu.setFechaEntrega(ordenFabricacion.getFechaEntrega());
			ofu.setFechaFabricacion(ordenFabricacion.getFechaFabricacion());
			return ResponseEntity.ok(repoOF.save(ofu));
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("status:error"));
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteOF(@PathVariable("id") final String idOrdenFabricacion){
		Optional<OrdenFabricacion> off = repoOF.findById(idOrdenFabricacion);
		if(off.isPresent()) {
			repoOF.deleteById(idOrdenFabricacion);
			return ResponseEntity.ok().body(new MessageResponse("status:success"));
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("error, no existe esa OF"));
		}
		
	}
}
