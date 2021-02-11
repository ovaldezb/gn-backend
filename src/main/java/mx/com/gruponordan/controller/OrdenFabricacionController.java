package mx.com.gruponordan.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

import mx.com.gruponordan.model.Counter;
import mx.com.gruponordan.model.Eestatus;
import mx.com.gruponordan.model.Estatus;
import mx.com.gruponordan.model.MatPrimaOrdFab;
import mx.com.gruponordan.model.MateriaPrima;
import mx.com.gruponordan.model.MessageResponse;
import mx.com.gruponordan.model.OrdenCompra;
import mx.com.gruponordan.model.OrdenFabricacion;
import mx.com.gruponordan.model.ProductoTerminado;
import mx.com.gruponordan.repository.EstatusDAO;
import mx.com.gruponordan.repository.MateriaPrimaDAO;
import mx.com.gruponordan.repository.OrdenCompraDAO;
import mx.com.gruponordan.repository.OrdenFabricacionDAO;
import mx.com.gruponordan.repository.ProductoTerminadoDAO;

@RestController
@RequestMapping("/api/ordenfab")
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "*")
public class OrdenFabricacionController {

	//private static Logger logger = LoggerFactory.getLogger(OrdenFabricacionController.class);
	private double PERCENT = 100;
	private double MILILITROS = .001;
	private String AGUA = "AGUA001";
	@Autowired
	OrdenFabricacionDAO repoOF;
	
	@Autowired
	MateriaPrimaDAO repomatprima;
	
	@Autowired
	EstatusDAO repoestatus;
	
	@Autowired
	ProductoTerminadoDAO repoprodterm;
	
	@Autowired
	OrdenCompraDAO repoOC;
	
	@GetMapping("/active/{active}")
	public ResponseEntity<?> getAllOF(@PathVariable String active){
		if(active.equals("true")) {
			return ResponseEntity.ok(repoOF.findAll());
		}else {
			return ResponseEntity.ok(repoOF.findByEstatus("TEP"));
		}
		
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
	
	@GetMapping("/validar/{codigo}/{porcentaje}/{piezas}/{presentacion}")
	public ResponseEntity<?> validaMPforOF(@PathVariable final String codigo, @PathVariable final double porcentaje, @PathVariable final double piezas, @PathVariable final double presentacion){
		List<MateriaPrima> lstMateriaPrima = repomatprima.findByCodigoAndCantidadMoreThan(codigo,0);
		List<MatPrimaOrdFab> lstRspMPOF = new ArrayList<MatPrimaOrdFab>();
		List<MateriaPrima> lstMPUpdtApartado = new ArrayList<MateriaPrima>();
		double cantReq = (porcentaje / PERCENT) * piezas * presentacion * MILILITROS;
		
		for(MateriaPrima matprima : lstMateriaPrima){
			if(!matprima.getCodigo().equals(AGUA)) {
				if(matprima.getCantidad() - cantReq > 0 ) {
					MatPrimaOrdFab mpof = new MatPrimaOrdFab(matprima.getCodigo(), matprima.getDescripcion(),cantReq,matprima.getLote(),"OK","");
					cantReq = 0;
					lstRspMPOF.add(mpof);
					lstMPUpdtApartado.add(matprima);
					break;
				}else if(matprima.getCantidad() > 0) { //Este toma lo que queda disponible en el Lote
					MatPrimaOrdFab mpof = new MatPrimaOrdFab(matprima.getCodigo(),matprima.getDescripcion(),matprima.getCantidad(),matprima.getLote(),"OK","");
					lstRspMPOF.add(mpof);
					cantReq -= matprima.getCantidad();
				}
			}
		}
		
		/* esto aparta las cantidades necesarias para una OF, las descuenta de la Cantidad y las pone en Apartado*/
		if(!lstMPUpdtApartado.isEmpty()) {
			repomatprima.saveAll(lstMPUpdtApartado);
		}
		
		if(!lstRspMPOF.isEmpty() && cantReq == 0) {
			return ResponseEntity.ok(lstRspMPOF);
		}else {
			MatPrimaOrdFab mpof = null;
			lstRspMPOF.clear();
			if(lstMateriaPrima.isEmpty()) {
				 mpof = new MatPrimaOrdFab(codigo,"", cantReq, codigo, "ERROR", "Materia prima no encontrada");
			}else if(cantReq > 0) {
				DecimalFormat df = new DecimalFormat("###,###,###.##");
				mpof = new MatPrimaOrdFab(lstMateriaPrima.get(0).getCodigo(), lstMateriaPrima.get(0).getDescripcion(),Double.parseDouble(df.format(cantReq)) , codigo, "ERROR", "MP insuficiente por "+df.format(cantReq));
			}			
			lstRspMPOF.add(mpof);
			return ResponseEntity.ok(lstRspMPOF);
		}
		
		
	}
	
	@PostMapping()
	public ResponseEntity<?> saveOF(@RequestBody final OrdenFabricacion ordenFabricacion) {
		List<MatPrimaOrdFab> matPrimOrdFab = ordenFabricacion.getMatprima();
		List<MateriaPrima> mpUpdt = new ArrayList<MateriaPrima>();
		
		matPrimOrdFab.stream().filter(mp->!mp.getCodigo().equals(AGUA)).forEach(mpof -> {
			MateriaPrima mpf = repomatprima.findByLote(mpof.getLote());
			if(mpf!=null) {
				mpf.setApartado(mpof.getCantidad());
				mpUpdt.add(mpf);
			}
		});
		
		Optional<OrdenCompra> oc = repoOC.findByOc(ordenFabricacion.getOc().getOc());
		/* Guarda la cantidad a producir, esta se va ir acomulando */
		if(oc.isPresent()) {
			OrdenCompra ocu = oc.get();
			ocu.setPiezasFabricadas(ocu.getPiezasFabricadas() + ordenFabricacion.getPiezas());
			ocu.setEstatus(Eestatus.TEP);
			repoOC.save(ocu);
		}
		
		repomatprima.saveAll(mpUpdt);
		return ResponseEntity.ok(repoOF.save(ordenFabricacion));
	}
	
	/*@PostMapping("/cancelar")
	public ResponseEntity<?> cancelarOF(@RequestBody final OrdenFabricacion ordenFabricacion) {
		List<MatPrimaOrdFab> matPrimOrdFab = ordenFabricacion.getMatprima();
		List<MateriaPrima> mpUpdt = new ArrayList<MateriaPrima>();
		
		matPrimOrdFab.stream().forEach(mpof -> {
			MateriaPrima mpf = repomatprima.findByLote(mpof.getLote());
			if(mpf!=null) {
				mpf.setApartado(mpf.getApartado() - mpof.getCantidad());
				mpf.setCantidad(mpf.getCantidad() + mpof.getCantidad());				
				mpUpdt.add(mpf);
			}
		});
		return ResponseEntity.ok(repomatprima.saveAll(mpUpdt));
	}*/
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateOF(@PathVariable final String idOrdenFabricacion, @RequestBody final OrdenFabricacion ordenFabricacion){
		Optional<OrdenFabricacion> off = repoOF.findById(idOrdenFabricacion);
		if(off.isPresent()) {
			OrdenFabricacion ofu = off.get();
			ofu.setOc(ordenFabricacion.getOc());
			//ofu.setLote(ordenFabricacion.getLote());
			ofu.setPiezas(ordenFabricacion.getPiezas());
			ofu.setObservaciones(ordenFabricacion.getObservaciones());
			ofu.setMatprima(ordenFabricacion.getMatprima());
			return ResponseEntity.ok(repoOF.save(ofu));
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("status:error"));
		}
	}
	
	/*
	 * @Get, porque no recibe parametros en el body
	 * Completa una OF y genera un PT
	 */
	@GetMapping("/complete/{idOrdenFabricacion}")
	public ResponseEntity<?> completeOF(@PathVariable final String idOrdenFabricacion){
		Optional<OrdenFabricacion> off = repoOF.findById(idOrdenFabricacion);
		List<MatPrimaOrdFab> matPrimOrdFab; //= ordenFabricacion.getMatprima();
		List<MateriaPrima> mpUpdt = new ArrayList<MateriaPrima>();
		if(off.isPresent()) {			
			OrdenFabricacion ofu = off.get();
			matPrimOrdFab = ofu.getMatprima();
			for(MatPrimaOrdFab mpof : matPrimOrdFab){
				MateriaPrima mpf = repomatprima.findByLote(mpof.getLote());
				mpf.setApartado(mpf.getApartado()- mpof.getCantidad());
				mpf.setCantidad(mpf.getCantidad() - mpof.getCantidad());
				mpUpdt.add(mpf);
			}
			repomatprima.saveAll(mpUpdt);
			
			Optional<OrdenCompra> oc = repoOC.findByOc(ofu.getOc().getOc());
			Estatus wtdl = repoestatus.findByCodigo(Eestatus.WTDEL);
			if(oc.isPresent()) {
				OrdenCompra ocu = oc.get();
				ocu.setPiezasCompletadas(ocu.getPiezasCompletadas() + ofu.getPiezas());
				repoOC.save(ocu);
				ProductoTerminado pt = new ProductoTerminado(wtdl,ocu.getProducto(),
										ofu.getOc().getOc(),
										ocu.getLote(),
										ofu.getPiezas(),
										ocu.getFechaFabricacion(),
										ocu.getFechaEntrega(),
										ofu.getNoConsecutivo(),
										ocu.getCliente(), 
										ocu.getClave());
				repoprodterm.save(pt);
				
			}
			ofu.setEstatus(Eestatus.CMPLT);
			ofu.setFechaFin(new Date());
			return ResponseEntity.ok(repoOF.save(ofu));
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("status:error"));
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteOF(@PathVariable("id") final String idOrdenFabricacion){
		Optional<OrdenFabricacion> off = repoOF.findById(idOrdenFabricacion);
		Optional<OrdenCompra> oc = repoOC.findByOc(off.get().getOc().getOc());
		if(oc.isPresent()) {
			OrdenCompra ocu = oc.get();
			ocu.setPiezasFabricadas(ocu.getPiezasFabricadas() - off.get().getPiezas());
			repoOC.save(ocu);
		}
		if(off.isPresent()) {
			repoOF.deleteById(idOrdenFabricacion);
			return ResponseEntity.ok().body(new MessageResponse("status:success"));
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("error, no existe esa OF"));
		}
		
	}
}
