package mx.com.gruponordan.controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

import mx.com.gruponordan.model.ClienteProducto;
import mx.com.gruponordan.model.Eestatus;
import mx.com.gruponordan.model.MessageResponse;
import mx.com.gruponordan.model.OrdenCompra;
import mx.com.gruponordan.repository.ClienteProductoDAO;
import mx.com.gruponordan.repository.EstatusDAO;
import mx.com.gruponordan.repository.OrdenCompraDAO;

@RestController
@RequestMapping("/api/ordencompra")
@CrossOrigin(origins = "*")
public class OrdenCompraController {

	@Autowired
	OrdenCompraDAO repoOC;
	
	@Autowired
	ClienteProductoDAO repoCP;
	
	@Autowired
	EstatusDAO repoestatus;
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllOC(){
		return ResponseEntity.ok(repoOC.findAll());
	}
	
	@GetMapping()
	public ResponseEntity<?> getActiveOC(){
		List<OrdenCompra> lista = repoOC.findByEstatusNotLikeOrderByFechaEntrega(Eestatus.CMPLT); 
		return ResponseEntity.ok(lista.stream().filter(oc -> !oc.getEstatus().equals(Eestatus.CANCEL)).collect(Collectors.toList()) );
	}
	
	@GetMapping("/{idOrdenCompra}")
	public ResponseEntity<?> getOCById(@PathVariable final String idOrdenCompra){
		Optional<OrdenCompra> oc = repoOC.findById(idOrdenCompra);
		if(oc.isPresent()) {
			return ResponseEntity.ok(oc);
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("status:error"));
		}
	}
	
	@GetMapping("/cp/{idCliente}")
	public ResponseEntity<?> getProductoByCliente(@PathVariable final String idCliente){
		return ResponseEntity.ok(repoCP.findByIdCliente(idCliente));
	}
	
	@GetMapping("/clave/{oc}")
	public ResponseEntity<?> getOCByClave(@PathVariable final String oc){
		return ResponseEntity.ok(repoOC.findByOc(oc));
	}
	
	/*@GetMapping("/oc/{ordenCompra}")
	public ResponseEntity<?> getOCByNumber(@PathVariable final String ordenCompra){
		if(ordenCompra.equals("vacio")) {
			return ResponseEntity.ok(repoOC.findByEstatusNotLikeOrderByFechaEntrega(Eestatus.CMPLT));
		}else {
			
			List<OrdenCompra> ordenescompra = repoOC.findAll();
			if(!ordenescompra.isEmpty()) {
				return ResponseEntity.ok(ordenescompra);
			}else {
				return ResponseEntity.badRequest().body(new MessageResponse("No se encontro"));
			}	
		}
	}*/
	
	@PostMapping()
	public ResponseEntity<?> saveOC(@RequestBody final OrdenCompra ordenCompra) {
		//Estatus estatus = repoestatus.findByCodigo(ordenCompra.getEstatus());
		//ordenCompra.setEstatus(estatus);
		Optional<ClienteProducto> cpf = repoCP.findByIdClienteAndClave(ordenCompra.getCliente().getId(), ordenCompra.getProducto().getClave());
		if(cpf.isEmpty()) {
			ClienteProducto cp = new ClienteProducto(ordenCompra.getCliente().getNombre(),ordenCompra.getCliente().getId(),ordenCompra.getProducto().getNombre(),ordenCompra.getProducto().getId(),ordenCompra.getProducto().getClave());
			repoCP.save(cp);
		}
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
			//ocu.setAprobado(ordenCompra.isAprobado());
			ocu.setEstatus(ordenCompra.getEstatus());
			return ResponseEntity.ok(repoOC.save(ocu));
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("error"));
		}
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteOC(@PathVariable String id) {
		repoOC.deleteById(id);
		return ResponseEntity.ok(new MessageResponse("deleted"));
	}
}
