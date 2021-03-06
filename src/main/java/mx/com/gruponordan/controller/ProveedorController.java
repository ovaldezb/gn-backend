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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import mx.com.gruponordan.model.MessageResponse;
import mx.com.gruponordan.model.Proveedor;
import mx.com.gruponordan.repository.ProveedorDAO;

@RestController
@RequestMapping("/api/proveedor")
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "*")
public class ProveedorController {
	
	@Autowired
	ProveedorDAO repoprove;
	
	@GetMapping
	@ApiOperation(value="Obtiene la lista de proveedores activos")
	public ResponseEntity<?> getAllProveedores(){
		return ResponseEntity.ok(repoprove.findByActivoOrderByNombre(true));
	}
	
	@GetMapping("/{proveedor}")
	public ResponseEntity<?> getClienteNombre(@PathVariable String proveedor){
		if(proveedor.trim().equals("vacio")) {
			return ResponseEntity.ok(repoprove.findByActivoOrderByNombre(true));
		}else {
			return ResponseEntity.ok(repoprove.findByNombreLikeOrderByNombre(proveedor));
		}
	}
	
	@PostMapping
	@ApiParam(required = true, name = "Proveedor")
	public ResponseEntity<?> saveProveedor(@RequestBody final Proveedor proveedor){
		return ResponseEntity.ok(repoprove.save(proveedor));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateProveedor(@PathVariable final String id, @RequestBody Proveedor proveedor){
		Optional<Proveedor> prf = repoprove.findById(id);
		if(prf.isPresent()) {
			Proveedor pru = prf.get();
			pru.setNombre(proveedor.getNombre());
			pru.setContactos(proveedor.getContactos());
			pru.setTelefonoPrincipal(proveedor.getTelefonoPrincipal());
			pru.setDireccion(proveedor.getDireccion());
			return ResponseEntity.ok(repoprove.save(pru));
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("Error al actualizar"));
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProveedor(@PathVariable String id){
		Optional<Proveedor> prf = repoprove.findById(id);
		if(prf.isPresent()) {
			Proveedor pru = prf.get();
			pru.setActivo(false);
			return ResponseEntity.ok(repoprove.save(pru));
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("Error al eliminar"));
		}
		
	}

}
