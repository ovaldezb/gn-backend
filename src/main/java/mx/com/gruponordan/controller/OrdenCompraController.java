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

import mx.com.gruponordan.model.Eestatus;
import mx.com.gruponordan.model.MessageResponse;
import mx.com.gruponordan.model.OrdenCompra;
import mx.com.gruponordan.repository.EstatusDAO;
import mx.com.gruponordan.repository.OrdenCompraDAO;

@RestController
@RequestMapping("/api/ordencompra")
@CrossOrigin(origins = "http://localhost:3000")
public class OrdenCompraController {

	@Autowired
	OrdenCompraDAO repoOC;
	
	@Autowired
	EstatusDAO repoestatus;
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllOC(){
		return ResponseEntity.ok(repoOC.findAll());
	}
	
	@GetMapping()
	public ResponseEntity<?> getActiveOC(){
		return ResponseEntity.ok(repoOC.findByEstatusNotLike(Eestatus.CMPLT));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getOCById(@PathVariable final String idOrdenCompra){
		Optional<OrdenCompra> oc = repoOC.findById(idOrdenCompra);
		if(oc.isPresent()) {
			return ResponseEntity.ok(oc);
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("status:error"));
		}
	}
	
	@GetMapping("/oc/{ordenCompra}")
	public ResponseEntity<?> getOCByNumber(@PathVariable final String ordenCompra){
		if(ordenCompra.equals("vacio")) {
			return ResponseEntity.ok(repoOC.findByEstatusNotLike(Eestatus.CMPLT));
		}else {
			return ResponseEntity.ok(repoOC.findByOcLike(ordenCompra));
		}
	}
	
	@PostMapping()
	public ResponseEntity<?> saveOC(@RequestBody final OrdenCompra ordenCompra) {
		//Estatus estatus = repoestatus.findByCodigo(ordenCompra.getEstatus());
		//ordenCompra.setEstatus(estatus);
		return ResponseEntity.ok(repoOC.save(ordenCompra));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateOC(@PathVariable String id, @RequestBody final OrdenCompra ordenCompra) {
		Optional<OrdenCompra> ocf = repoOC.findById(id);
		if(ocf.isPresent()) {
			OrdenCompra ocu = ocf.get();
			ocu.setCliente(ordenCompra.getCliente());
			ocu.setFechaEntrega(ordenCompra.getFechaEntrega());
			ocu.setFechaFabricacion(ordenCompra.getFechaFabricacion());
			ocu.setProducto(ordenCompra.getProducto());
			ocu.setObservaciones(ordenCompra.getObservaciones());
			ocu.setOc(ordenCompra.getOc());
			ocu.setPresentacion(ordenCompra.getPresentacion());
			return ResponseEntity.ok(repoOC.save(ocu));
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("error"));
		}
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteOC(@PathVariable String id) {
		/*Optional<OrdenCompra> ocf = repoOC.findById(id);
		if(ocf.isPresent()) {
			Estatus estatus = repoestatus.findByCodigo(Eestatus.CMPLT);
			OrdenCompra ocu = ocf.get();
			ocu.setEstatus(estatus.getCodigo());
			return ResponseEntity.ok(repoOC.save(ocu));
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("error"));
		}*/
		repoOC.deleteById(id);
		return ResponseEntity.ok(new MessageResponse("deleted"));
	}
}
