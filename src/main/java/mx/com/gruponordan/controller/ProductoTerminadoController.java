package mx.com.gruponordan.controller;

import java.util.List;
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
import mx.com.gruponordan.model.ProductoTerminado;
import mx.com.gruponordan.repository.ProductoTerminadoDAO;

@RestController
@RequestMapping("/api/prodterm")
public class ProductoTerminadoController {

	@Autowired
	ProductoTerminadoDAO repoPT;
	
	@GetMapping()
	public List<ProductoTerminado> getProductoTermAll(){
		return repoPT.findAll();
	} 
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getPtById(@PathVariable final String id_ProductoTerminad){
		Optional<ProductoTerminado> pt = repoPT.findById(id_ProductoTerminad);
		
		if(pt.isPresent()) {
			return ResponseEntity.ok(pt);
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("error:no se pudo encontrar el PT"));
		}
	}
	
	@GetMapping("/{clave}")
	public ResponseEntity<?> getPtByCodigo(@PathVariable final String clave){
		ProductoTerminado pt = repoPT.findByClave(clave);
		
		if(pt!=null) {
			return ResponseEntity.ok(pt);
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("error:no se pudo encontrar el PT"));
		}
	}
	
	@PostMapping
	public ResponseEntity<?> savePT(@RequestBody(required = true) ProductoTerminado pt){
		return ResponseEntity.ok(repoPT.save(pt));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updatePT(@PathVariable("id") String id, @RequestBody(required = true) ProductoTerminado pt){
		Optional<ProductoTerminado> ptf = repoPT.findById(id);
		
		if(ptf.isPresent()) {
			ProductoTerminado ptu = ptf.get();
			ptu.setNombre(pt.getNombre());
			ptu.setClave(pt.getClave());
			ptu.setCantidad(pt.getCantidad());
			ptu.setTipo(pt.getTipo());
			
			return ResponseEntity.ok(repoPT.save(ptu));
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("error:no se pudo actualizar"));
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable(required = true) String id){
		repoPT.deleteById(id);
		return ResponseEntity.ok(new MessageResponse("status:success"));
	}
	
}
