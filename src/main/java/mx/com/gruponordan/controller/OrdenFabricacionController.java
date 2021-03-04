package mx.com.gruponordan.controller;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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

import mx.com.gruponordan.model.Cliente;
import mx.com.gruponordan.model.Counter;
import mx.com.gruponordan.model.Eestatus;
import mx.com.gruponordan.model.Estatus;
import mx.com.gruponordan.model.Lote;
import mx.com.gruponordan.model.MatPrimaOrdFab;
import mx.com.gruponordan.model.MateriaPrima;
import mx.com.gruponordan.model.MessageResponse;
import mx.com.gruponordan.model.OrdenCompra;
import mx.com.gruponordan.model.OrdenFabricacion;
import mx.com.gruponordan.model.ProductoTerminado;
import mx.com.gruponordan.model.Sequence;
import mx.com.gruponordan.repository.EstatusDAO;
import mx.com.gruponordan.repository.LoteDAO;
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
	//private double GRAMOS = .001;
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
	
	@Autowired
	LoteDAO loterepo;
	
	@Autowired
	MongoOperations mongoperations;
	
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
	
	@GetMapping("/count")
	public ResponseEntity<?> getMaxOF(){
		Counter c = new Counter(repoOF.count());
		return ResponseEntity.ok(c);
	}
	
	@GetMapping("/validar/{codigo}/{porcentaje}/{piezas}/{presentacion}")
	public ResponseEntity<?> validaMPforOF(@PathVariable final String codigo, @PathVariable final double porcentaje, @PathVariable final double piezas, @PathVariable final double presentacion){
		List<MatPrimaOrdFab> lstRspMPOF = new ArrayList<MatPrimaOrdFab>();
		double cantReq = (porcentaje / PERCENT) * piezas * presentacion * MILILITROS;
		if(codigo.equals(AGUA)) {
			MatPrimaOrdFab mpof = new MatPrimaOrdFab(codigo, "AGUA" ,cantReq,"","OK","");
			lstRspMPOF.add(mpof);
			return ResponseEntity.ok(lstRspMPOF);
		}
		List<MateriaPrima> lstMateriaPrima = repomatprima.findByCodigoAndCantidadMoreThan(codigo,0);
		
		NumberFormat nf = NumberFormat.getInstance(new Locale("es","MX"));
		nf.setMaximumFractionDigits(2);
		for(MateriaPrima matprima : lstMateriaPrima){
			if(matprima.getCantidad() - matprima.getApartado() - cantReq > 0 ) { // El lote tiene suficiente y lo toma
				MatPrimaOrdFab mpof = new MatPrimaOrdFab(matprima.getCodigo(), matprima.getDescripcion(),Double.parseDouble(nf.format(cantReq)),matprima.getLote(),"OK","");
				cantReq = 0;
				lstRspMPOF.add(mpof);				
				break;
			}else if((matprima.getCantidad() - matprima.getApartado()) > 0) { //Este toma lo que queda disponible en el Lote
				MatPrimaOrdFab mpof = new MatPrimaOrdFab(matprima.getCodigo(),matprima.getDescripcion(),Double.parseDouble(nf.format(matprima.getCantidad())),matprima.getLote(),"OK","");
				lstRspMPOF.add(mpof);
				cantReq -= (matprima.getCantidad() - matprima.getApartado());
			}
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
				mpof = new MatPrimaOrdFab(lstMateriaPrima.get(0).getCodigo(), lstMateriaPrima.get(0).getDescripcion(),Double.parseDouble(df.format((porcentaje / PERCENT) * piezas * presentacion * MILILITROS)) , codigo, "ERROR", "MP insuficiente por "+df.format(cantReq));
			}else if(cantReq < 0) {
				DecimalFormat df = new DecimalFormat("###,###,###.##");
				mpof = new MatPrimaOrdFab(lstMateriaPrima.get(0).getCodigo(), lstMateriaPrima.get(0).getDescripcion(),Double.parseDouble(df.format((porcentaje / PERCENT) * piezas * presentacion * MILILITROS)) , codigo, "ERROR", "El lote no cuenta con insuficiente MP, le faltan "+df.format(Math.abs(cantReq)));
			}			
			lstRspMPOF.add(mpof);
			return ResponseEntity.ok(lstRspMPOF);
		}
	}
	
	@PostMapping()
	public ResponseEntity<?> saveOF(@RequestBody final OrdenFabricacion ordenFabricacion) {
		
		/*Validar las cantidades que vienen en la materia prima, para ver si alcanza*/
		List<MatPrimaOrdFab> matprimaof = ordenFabricacion.getMatprima();
		List<MatPrimaOrdFab> matprimaFilted = matprimaof.stream().filter(mp->!mp.getCodigo().equals(AGUA)).filter(mp->{
			 boolean materiaInsuficiente = false;
			if(mp.getDelta() >0) {
				MateriaPrima matprima = repomatprima.findByLote(mp.getLote());
				
				materiaInsuficiente = matprima.getCantidad() - matprima.getApartado() - mp.getDelta() < 0;
			}else {
				materiaInsuficiente = false;
			}
			return materiaInsuficiente;
		}).collect(Collectors.toList());
		if(!matprimaFilted.isEmpty()) {
			for(MatPrimaOrdFab mpof : matprimaFilted ) {
				for(MatPrimaOrdFab mopft :matprimaof) {
					if(mpof.getCodigo().equals(mopft.getCodigo())) {
						mopft.setEstatus("ERROR");
						mopft.setComentarios("La MP disponible es insuficiente para la nueva cantidad ");
					}
				}
			}
			return ResponseEntity.ok(matprimaof);
		}
		/*Actualiza el folio para indicar que ya se fabrico*/
		Optional<Lote> lote = loterepo.findByLote(ordenFabricacion.getLote());
		if(lote.isPresent()) {
			Lote loteU = lote.get();
			loteU.setFabricado(true);
			loteU.setEstatus(Eestatus.TEP);
			loterepo.save(loteU);
		}
		
		Optional<OrdenCompra> oc = repoOC.findById(ordenFabricacion.getOc().getId());
		/* Guarda la cantidad a producir, esta se va ir acomulando */
		if(oc.isPresent()) {
			OrdenCompra ocu = oc.get();
			ocu.setPiezasFabricadas(ocu.getPiezasFabricadas() + ordenFabricacion.getPiezas());
			ocu.setEstatus(Eestatus.TEP);
			repoOC.save(ocu);
		}
		
		ordenFabricacion.setNoConsecutivo(getCounter());
		return ResponseEntity.ok(repoOF.save(ordenFabricacion));
	}
	
	private long getCounter() {
		Sequence c = null;
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is("ordenfab_sequence"));
		FindAndModifyOptions options = new FindAndModifyOptions();
		c = mongoperations.findAndModify(query, new Update().inc("counter",1),options.returnNew(true).upsert(true),Sequence.class );
		return c.getCounter();
	}
	
	@PutMapping("/{idOrdenFabricacion}")
	public ResponseEntity<?> updateOF(@PathVariable final String idOrdenFabricacion, @RequestBody final OrdenFabricacion ordenFabricacion){
		Optional<OrdenFabricacion> off = repoOF.findById(idOrdenFabricacion);
		if(off.isPresent()) {
			OrdenFabricacion ofu = off.get();
			ofu.setOc(ordenFabricacion.getOc());
			ofu.setPiezas(ordenFabricacion.getPiezas());
			ofu.setObservaciones(ordenFabricacion.getObservaciones());
			ofu.setMatprima(ordenFabricacion.getMatprima());
			return ResponseEntity.ok(repoOF.save(ofu));
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("status:error"));
		}
	}
	
	/*
	 * Completa una OF y genera un PT
	 */
	@GetMapping("/complete/{idOrdenFabricacion}")
	public ResponseEntity<?> completeOF(@PathVariable final String idOrdenFabricacion ){
		Optional<OrdenFabricacion> off = repoOF.findById(idOrdenFabricacion);
		List<MatPrimaOrdFab> matPrimOrdFab;
		List<MateriaPrima> mpUpdt = new ArrayList<MateriaPrima>();
		if(off.isPresent()) {			
			OrdenFabricacion ofu = off.get();
			matPrimOrdFab = ofu.getMatprima();
			for(MatPrimaOrdFab mpof : matPrimOrdFab){
				if(mpof.getCodigo().equals(AGUA)) {
					continue;
				}
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
				Cliente cliente = new Cliente();
				cliente.setId(ocu.getCliente().getId());
				ProductoTerminado pt = new ProductoTerminado(wtdl,ocu.getProducto(),
										ofu.getOc().getOc(),
										ofu.getLote(),
										ofu.getPiezas(),
										ocu.getFechaFabricacion(),
										ocu.getFechaEntrega(),
										ofu.getNoConsecutivo(),
										cliente, 
										ocu.getClave());
				repoprodterm.save(pt);
				
			}
			/*Actualizar estatus a Lote*/
			Optional<Lote> lote = loterepo.findByLote(ofu.getLote());
			if(lote.isPresent()) {
				Lote loteU = lote.get();
				loteU.setEstatus(Eestatus.CMPLT);
				loterepo.save(loteU);
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
