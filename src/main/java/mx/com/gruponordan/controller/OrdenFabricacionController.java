package mx.com.gruponordan.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
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
import mx.com.gruponordan.model.OrdenFabricacion;
import mx.com.gruponordan.model.ProductoTerminado;
import mx.com.gruponordan.repository.EstatusDAO;
import mx.com.gruponordan.repository.MateriaPrimaDAO;
import mx.com.gruponordan.repository.OrdenFabricacionDAO;
import mx.com.gruponordan.repository.ProductoTerminadoDAO;

@RestController
@RequestMapping("/api/ordenfab")
@CrossOrigin(origins = "http://localhost:3000")
public class OrdenFabricacionController {

	//private static Logger logger = LoggerFactory.getLogger(OrdenFabricacionController.class);
	
	@Autowired
	OrdenFabricacionDAO repoOF;
	
	@Autowired
	MateriaPrimaDAO repomatprima;
	
	@Autowired
	EstatusDAO repoestatus;
	
	@Autowired
	ProductoTerminadoDAO repoprodterm;
	
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
	
	@GetMapping("/count")
	public ResponseEntity<?> getMaxOF(){
		Counter c = new Counter(repoOF.count());
		return ResponseEntity.ok(c);
	}
	
	@GetMapping("/validar/{codigo}/{cantidad}/{piezas}")
	public ResponseEntity<?> validaMPforOF(@PathVariable final String codigo, @PathVariable final String cantidad, @PathVariable final double piezas){
		List<MateriaPrima> lstMateriaPrima = repomatprima.findByCodigoAndCantidadMoreThan(codigo,0);
		List<MatPrimaOrdFab> lstRspMPOF = new ArrayList<MatPrimaOrdFab>();
		List<MateriaPrima> lstMPUpdtApartado = new ArrayList<MateriaPrima>();
		double cantReq = Double.parseDouble(cantidad) * piezas;
		
		for(MateriaPrima matprima : lstMateriaPrima){
			if(matprima.getCantidad() - cantReq > 0 ) {
				MatPrimaOrdFab mpof = new MatPrimaOrdFab(matprima.getCodigo(), matprima.getDescripcion(),cantReq,matprima.getLote(),"OK","");
				matprima.setApartado(cantReq);
				matprima.setCantidad(matprima.getCantidad() - cantReq);
				cantReq = 0;
				lstRspMPOF.add(mpof);
				lstMPUpdtApartado.add(matprima);
				break;
			}else if(matprima.getCantidad() > 0) { //Este toma lo que queda disponible en el Lote
				MatPrimaOrdFab mpof = new MatPrimaOrdFab(matprima.getCodigo(),matprima.getDescripcion(),matprima.getCantidad(),matprima.getLote(),"OK","");
				lstRspMPOF.add(mpof);
				cantReq -= matprima.getCantidad();
				matprima.setApartado(matprima.getCantidad());
				matprima.setCantidad(0.0);
			}
		};
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
				mpof = new MatPrimaOrdFab(lstMateriaPrima.get(0).getCodigo(), lstMateriaPrima.get(0).getDescripcion(),Double.parseDouble( df.format(Double.parseDouble(cantidad) * piezas)) , codigo, "ERROR", "MP insuficiente por "+df.format(cantReq));
			}			
			lstRspMPOF.add(mpof);
			return ResponseEntity.ok(lstRspMPOF);
		}
		
		
	}
	
	@PostMapping()
	public ResponseEntity<?> saveOF(@RequestBody final OrdenFabricacion ordenFabricacion) {
		List<MatPrimaOrdFab> matPrimOrdFab = ordenFabricacion.getMatprima();
		List<MateriaPrima> mpUpdt = new ArrayList<MateriaPrima>();
		
		matPrimOrdFab.stream().forEach(mpof -> {
			MateriaPrima mpf = repomatprima.findByLote(mpof.getLote());
			if(mpf!=null) {
				mpf.setApartado(mpf.getApartado()-mpof.getCantidad());
				mpUpdt.add(mpf);
			}
		});
		repomatprima.saveAll(mpUpdt);
		return ResponseEntity.ok(repoOF.save(ordenFabricacion));
	}
	
	@PostMapping("/cancelar")
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
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateOF(@PathVariable final String idOrdenFabricacion, @RequestBody final OrdenFabricacion ordenFabricacion){
		Optional<OrdenFabricacion> off = repoOF.findById(idOrdenFabricacion);
		if(off.isPresent()) {
			OrdenFabricacion ofu = off.get();
			ofu.setClave(ordenFabricacion.getClave());
			ofu.setNombre(ordenFabricacion.getNombre());
			ofu.setOc(ordenFabricacion.getOc());
			ofu.setLote(ordenFabricacion.getLote());
			ofu.setPiezas(ordenFabricacion.getPiezas());
			ofu.setObservaciones(ordenFabricacion.getObservaciones());
			ofu.setFechaEntrega(ordenFabricacion.getFechaEntrega());
			ofu.setFechaFabricacion(ordenFabricacion.getFechaFabricacion());
			ofu.setMatprima(ordenFabricacion.getMatprima());
			return ResponseEntity.ok(repoOF.save(ofu));
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("status:error"));
		}
	}
	
	/*
	 * Completa una OF y genera un PT
	 */
	@PutMapping("/complete/{idOrdenFabricacion}")
	public ResponseEntity<?> completeOF(@PathVariable final String idOrdenFabricacion){
		Optional<OrdenFabricacion> off = repoOF.findById(idOrdenFabricacion);
		if(off.isPresent()) {
			OrdenFabricacion ofu = off.get();
			ofu.setEstatus(Eestatus.CMPLT);
			Estatus estatus = repoestatus.findByCodigo(Eestatus.WTDEL);
			System.out.println(estatus);
			ProductoTerminado pt = new ProductoTerminado(ofu.getNombre(),ofu.getClave(),ofu.getPiezas(),ofu.getLote(),ofu.getCliente(),ofu.getOc(),ofu.getFechaFabricacion(),ofu.getFechaEntrega(),estatus);
			System.out.println(pt);
			repoprodterm.save(pt);
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
