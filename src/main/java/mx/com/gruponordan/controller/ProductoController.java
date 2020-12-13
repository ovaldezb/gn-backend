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
import mx.com.gruponordan.model.Producto;
import mx.com.gruponordan.repository.ProductoDAO;

@RestController
@RequestMapping("/api/producto")
public class ProductoController {

	@Autowired
	ProductoDAO repoP;
	
	@GetMapping()
	public ResponseEntity<?> getAllProducts() {
		return ResponseEntity.ok(repoP.findAll());
	}
	
	@GetMapping("{id}")
	public ResponseEntity<?> getProductoById(@PathVariable String id){
		return ResponseEntity.ok(repoP.findById(id));
	}
	
	@PostMapping()
	public ResponseEntity<?> saveProduct(@RequestBody Producto producto){
		Producto sp = repoP.save(producto);
		if(sp!=null) {
			return ResponseEntity.ok(sp);
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("status:error"));
		}
	}
	
	@PutMapping("{/id}")
	public ResponseEntity<?> updateProduct(@PathVariable String id, @RequestBody Producto producto) {
		Optional<Producto> pf = repoP.findById(id);
		if(pf.isPresent()) {
			Producto pu = pf.get();
			pu.setNombre(producto.getNombre());
			pu.setTipo(producto.getTipo());
			pu.setClave(producto.getClave());
			pu.setMateriaPrima(producto.getMateriaPrima());
			return ResponseEntity.ok(repoP.save(pu));
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("status:error"));
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable String id){
		repoP.deleteById(id);
		return ResponseEntity.ok(new MessageResponse("status:success"));
	}
}
