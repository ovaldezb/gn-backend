package mx.com.gruponordan.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
import mx.com.gruponordan.model.ProductoEntregado;
import mx.com.gruponordan.model.ProductoTerminado;
import mx.com.gruponordan.repository.EstatusDAO;
import mx.com.gruponordan.repository.OrdenCompraDAO;
import mx.com.gruponordan.repository.ProductoEntregadoDAO;
import mx.com.gruponordan.repository.ProductoTerminadoDAO;

@RestController
@RequestMapping("/api/prodterm")
@CrossOrigin(origins = "*")
public class ProductoTerminadoController {
	
	//Logger logger = LoggerFactory.getLogger(ProductoTerminadoController.class);

	@Autowired
	ProductoTerminadoDAO repoPT;
	
	@Autowired
	OrdenCompraDAO repoOC;
	
	@Autowired
	EstatusDAO repoestatus;
	
	@Autowired
	ProductoEntregadoDAO repoPE;
	
	@Autowired
	MongoOperations mongoperations;
	
	@GetMapping()
	public List<ProductoTerminado> getProductoTermAll(){
		return repoPT.findAll();
	}
	
	@GetMapping("/activo")
	public List<ProductoTerminado> getProductoTermActive(){
		Estatus estatus = repoestatus.findByCodigo(Eestatus.DELVRD);
		Query qry = new Query();
		qry.addCriteria(Criteria.where("estatus").ne(estatus));
		return mongoperations.find(qry, ProductoTerminado.class);
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
	@PutMapping("/dlvr")
	public ResponseEntity<?> updatePT(@RequestBody ProductoTerminado[] prodterm){
		for(ProductoTerminado pt : prodterm) {
			Optional<ProductoTerminado> ptf = repoPT.findById(pt.getId());
			if(ptf.isPresent()) {
				ProductoTerminado ptu = ptf.get();
				ptu.setEstatus((ptu.getPiezas() - ptu.getPiezasEntregadas() - pt.getPiezasEntregadas() == 0) ? repoestatus.findByCodigo(Eestatus.DELVRD) : repoestatus.findByCodigo(Eestatus.EEP));
				ptu.setComentario(pt.getComentario());
				ptu.setPiezasEntregadas(ptu.getPiezasEntregadas() + pt.getPiezasEntregadas());
				Optional<OrdenCompra> oc = repoOC.findByOc(ptu.getOc());
				if(oc.isPresent()) {
					OrdenCompra ocu = oc.get();
					/* Aqui es donde se define si se completa la OC con las piezas fabricads o entregadas
					 * 1)ocu.setPiezasEntregadas(ocu.getPiezasEntregadas() + ptu.getPiezas());
					 * 2)ocu.setPiezasEntregadas(ocu.getPiezasEntregadas() + ptu.getPiezasEntregadas());
					 * */
					ocu.setPiezasEntregadas(ocu.getPiezasEntregadas() + pt.getPiezasEntregadas());					
					if(ocu.getPiezasEntregadas() == ocu.getPiezas()) {
						ocu.setEstatus(Eestatus.CMPLT);
					}
					repoOC.save(ocu);
				}
				ProductoEntregado prodent = new ProductoEntregado(pt.getOc(), pt.getLote(), pt.getCliente().getNombre(), 
						pt.getProducto().getNombre(), pt.getPiezasEntregadas(), pt.getNoConsecutivo(), 
						pt.getFechaRemision(), pt.getNoRemision(), 
						pt.getTipoEntrega(),pt.getIdDireccion(),repoestatus.findByCodigo(Eestatus.OPEN), pt.getId());
				repoPE.save(prodent);
				ResponseEntity.ok(repoPT.save(ptu));
			}
		}
		return ResponseEntity.ok(prodterm);
	}
	
	/*
	 * Completa un PT, poniendo el estatus en Entregado
	 * Cuando las piezas entregadas sean iguales a las piezas totales, se cierra la OC
	 * */
	@PutMapping("/updnrem/{id}")
	public ResponseEntity<?> updatePTNoRev(@PathVariable("id") String id,@RequestBody ProductoTerminado prodterm){
		Optional<ProductoTerminado> ptf = repoPT.findById(id);
		if(ptf.isPresent()) {
			ProductoTerminado ptu = ptf.get();
			ptu.setNoRemision(prodterm.getNoRemision());	
			ptu.setFechaRemision(prodterm.getFechaRemision());
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
