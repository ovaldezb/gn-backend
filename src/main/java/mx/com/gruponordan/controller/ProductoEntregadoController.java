package mx.com.gruponordan.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.com.gruponordan.model.Eestatus;
import mx.com.gruponordan.model.MessageResponse;
import mx.com.gruponordan.model.OrdenCompra;
import mx.com.gruponordan.model.ProductoEntregado;
import mx.com.gruponordan.model.ProductoTerminado;
import mx.com.gruponordan.repository.EstatusDAO;
import mx.com.gruponordan.repository.OrdenCompraDAO;
import mx.com.gruponordan.repository.ProductoEntregadoDAO;
import mx.com.gruponordan.repository.ProductoTerminadoDAO;

@RestController
@RequestMapping("/api/prodent")
@CrossOrigin(origins = "*")
public class ProductoEntregadoController {
	
	//Logger logger = LoggerFactory.getLogger(ProductoEntregadoController.class);

	@Autowired
	ProductoEntregadoDAO repoPE;
	
	@Autowired
	ProductoTerminadoDAO repoPT;
	
	@Autowired
	EstatusDAO repoestatus;
	
	@Autowired
	OrdenCompraDAO repoOC;
	
	@GetMapping
	public ResponseEntity<?> getAllProductosEntregados(){
		return ResponseEntity.ok(repoPE.findAll());
	}
	
	@GetMapping("/rem/{remision}")
	public ResponseEntity<?> getProductoByRemision(@PathVariable final String remision){
		Optional<ProductoEntregado> pe = repoPE.findByRemision(remision);
		if(pe.isPresent()) {
			return ResponseEntity.ok(pe.get());
		}else {
			return ResponseEntity.ok().body(new MessageResponse(""));
		}
	}
	
	
	@GetMapping("/{fecini}/{fecfin}")
	public ResponseEntity<?> getReporteByDates(@PathVariable final String fecini, @PathVariable final String fecfin ){
		//DateTimeFormatter df = DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm:ss");
		SimpleDateFormat parser = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
		Date fechaIni = null;
		Date fechaFin = null;
		try {
			fechaIni = parser.parse(fecini);
			fechaFin = parser.parse(fecfin);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(repoPE.findByFechaEntregaBetween(fechaIni, fechaFin));
	}
	
	@PutMapping("/{idproducto}")
	public ResponseEntity<?> updateProductoEntregado(@PathVariable final String idproducto){
		Optional<ProductoEntregado> pef = repoPE.findById(idproducto);
		if(pef.isPresent()) {
			ProductoEntregado peu = pef.get();
			peu.setEstatus(repoestatus.findByCodigo(Eestatus.CMPLT));
			return ResponseEntity.ok(repoPE.save(peu));
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("Error"));
		}
	}
	
	@PutMapping("/cancel/{idproducto}")
	public ResponseEntity<?> cancelProductoEntregado(@PathVariable final String idproducto, @RequestBody final ProductoEntregado productoentregado ){
		Optional<ProductoEntregado> pef = repoPE.findById(idproducto);
		if(pef.isPresent()) {
			ProductoEntregado peu = pef.get();
			Optional<ProductoTerminado> prodterm = repoPT.findById(peu.getIdProdTerminado());
			if(prodterm.isPresent()) {
				ProductoTerminado ptu = prodterm.get();
				ptu.setEstatus(repoestatus.findByCodigo(Eestatus.EEP));
				ptu.setPiezasEntregadas(ptu.getPiezasEntregadas() - peu.getPiezasEntregadas());
				repoPT.save(ptu);
				Optional<OrdenCompra> oc = repoOC.findByOc(ptu.getOc());
				if(oc.isPresent()) {
					OrdenCompra ocu = oc.get();
					ocu.setPiezasEntregadas(ocu.getPiezasEntregadas() - peu.getPiezasEntregadas());
					ocu.setEstatus(Eestatus.TEP);
					repoOC.save(ocu);
				}
			}
			peu.setMotivoCancel(productoentregado.getMotivoCancel());
			peu.setEstatus(repoestatus.findByCodigo(Eestatus.CANCEL));
			return ResponseEntity.ok(repoPE.save(peu));
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("Error"));
		}
	}
	
}
