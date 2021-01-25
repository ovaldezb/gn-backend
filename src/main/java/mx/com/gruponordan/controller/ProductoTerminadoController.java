package mx.com.gruponordan.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.com.gruponordan.model.Eestatus;
import mx.com.gruponordan.model.Estatus;
import mx.com.gruponordan.model.MessageResponse;
import mx.com.gruponordan.model.OrdenCompra;
import mx.com.gruponordan.model.ProductoTerminado;
import mx.com.gruponordan.repository.EstatusDAO;
import mx.com.gruponordan.repository.OrdenCompraDAO;
import mx.com.gruponordan.repository.ProductoTerminadoDAO;

@RestController
@RequestMapping("/api/prodterm")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductoTerminadoController {

	@Autowired
	ProductoTerminadoDAO repoPT;
	
	@Autowired
	OrdenCompraDAO repoOC;
	
	@Autowired
	EstatusDAO repoestatus;
	
	@GetMapping()
	public List<ProductoTerminado> getProductoTermAll(){
		return repoPT.findAll();
	}
	
	@GetMapping("/activo")
	public List<ProductoTerminado> getProductoTermActive(){
		return repoPT.findByEstatus(repoestatus.findByCodigo(Eestatus.WTDEL));
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
	
	
	/*
	 * Completa un PT, poniendo el estatus en Entregado
	 * Cuando las piezas entregadas sean iguales a las piezas totales, se cierra la OC
	 * */
	@PutMapping("/{id}")
	public ResponseEntity<?> updatePT(@PathVariable("id") String id,@RequestBody ProductoTerminado prodterm){
		Optional<ProductoTerminado> ptf = repoPT.findById(id);
		Estatus estatusPt = repoestatus.findByCodigo(Eestatus.DELVRD);
		if(ptf.isPresent()) {
			ProductoTerminado ptu = ptf.get();
			ptu.setEstatus(estatusPt);		
			ptu.setComentario(prodterm.getComentario());
			/*Optional<OrdenCompra> oc = repoOC.findByOc(ptu.getOc());
			if(oc.isPresent()) {
				OrdenCompra ocu = oc.get();
				ocu.setPiezasEntregadas(ocu.getPiezasEntregadas() + ptu.getPiezas());
				if(ocu.getPiezasEntregadas() == ocu.getPiezas()) {
					ocu.setEstatus(Eestatus.CMPLT);
				}
				repoOC.save(ocu);
				
			}*/
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
