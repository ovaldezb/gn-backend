package mx.com.gruponordan.controller;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import mx.com.gruponordan.interfaz.Definitions;
import mx.com.gruponordan.model.Bases;
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
import mx.com.gruponordan.model.ProductoDisponible;
import mx.com.gruponordan.model.ProductoTerminado;
import mx.com.gruponordan.model.Sequence;
import mx.com.gruponordan.repository.BasesDAO;
import mx.com.gruponordan.repository.EstatusDAO;
import mx.com.gruponordan.repository.LoteDAO;
import mx.com.gruponordan.repository.MateriaPrimaDAO;
import mx.com.gruponordan.repository.OrdenCompraDAO;
import mx.com.gruponordan.repository.OrdenFabricacionDAO;
import mx.com.gruponordan.repository.ProdDispDAO;
import mx.com.gruponordan.repository.ProductoTerminadoDAO;

@RestController
@RequestMapping("/api/ordenfab")
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "*")
public class OrdenFabricacionController implements Definitions {

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
	
	@Autowired
	BasesDAO repobase;
	
	@Autowired
	ProdDispDAO repoproddisp;
	
	//Logger logger = LoggerFactory.getLogger(OrdenFabricacionController.class);
	
	
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
	
	@GetMapping("/{idOrdenFabricacion}/print")
	public ResponseEntity<?> getDesgloceMPImpresion(@PathVariable final String idOrdenFabricacion){
		Optional<OrdenFabricacion> of = repoOF.findById(idOrdenFabricacion);
		
		if(of.isPresent()) {
			OrdenFabricacion off = of.get();
			Map<String,Double> formulaProductoTerminado = new HashMap<>();
			Map<String,Double> formulaBaseMap = new HashMap<>();
			Map<String, Double> mpTotalByCodigo = new HashMap<>();
			List<MatPrimaOrdFab> lstPrint = new ArrayList<>();
			Map<String,MatPrimaOrdFab> mapPrint = new HashMap<>();
			Arrays.asList(off.getOc().getProducto().getMateriaPrimaUsada()).forEach(mpUsed ->{
				formulaProductoTerminado.put(mpUsed.getMateriaprimadisponible().getCodigo(), mpUsed.getPorcentaje());
			});
			
			off.getMatprima().forEach(mpused -> {
				//Si es Base, tengo que buscar la información del lote, necesito saber la fórmula de la base también
				if(mpused.getTipo() != null && mpused.getTipo().equals("B")) {
					Optional<Bases> base = repobase.findByLote(mpused.getLote());
					Optional<ProductoDisponible> formulaBase = repoproddisp.findByClaveAndTipoProducto(mpused.getCodigo(),"B");
					if(formulaBase.isPresent()) {
						Arrays.asList(formulaBase.get().getMateriaPrimaUsada()).forEach(fb->{
							formulaBaseMap.put(fb.getMateriaprimadisponible().getCodigo(), fb.getPorcentaje());
						});
					}
					if(base.isPresent()) {
						Arrays.asList(base.get().getMateriaPrimaOrdFab()).forEach(mpLote ->{
							mpLote.setDelta(formulaProductoTerminado.get(mpused.getCodigo()) * formulaBaseMap.get(mpLote.getCodigo()) / 10000);
							//lstPrint.add(mpLote);
							if(mapPrint.get(mpLote.getCodigo()+mpLote.getLote()) != null) {
								MatPrimaOrdFab mpof = mapPrint.get(mpLote.getCodigo()+mpLote.getLote());
								mpof.setCantidad(mpof.getCantidad() + mpLote.getCantidad());
								mapPrint.put(mpLote.getCodigo()+mpLote.getLote(), mpof);
							}else {
								mapPrint.put(mpLote.getCodigo()+mpLote.getLote(), mpLote);
							}
							if(mpTotalByCodigo.get(mpLote.getCodigo()) != null) {
								mpTotalByCodigo.put(mpLote.getCodigo(), mpTotalByCodigo.get(mpLote.getCodigo()) + mpLote.getCantidad());
							}else {
								mpTotalByCodigo.put(mpLote.getCodigo(), mpLote.getCantidad() );
							}
						});
					}
				}else {
					mpused.setDelta(formulaProductoTerminado.get(mpused.getCodigo()) / 100);
					if(mpTotalByCodigo.get(mpused.getCodigo()) != null) {
						mpTotalByCodigo.put(mpused.getCodigo(), mpTotalByCodigo.get(mpused.getCodigo()) + mpused.getCantidad());
					}else {
						mpTotalByCodigo.put(mpused.getCodigo(), mpused.getCantidad() );
					}
					mapPrint.put(mpused.getCodigo()+mpused.getLote(), mpused);
				}
			});
			
			Iterator<String> it = mapPrint.keySet().iterator();
			while(it.hasNext()) {
				lstPrint.add(mapPrint.get(it.next()));
			}
			return ResponseEntity.ok(lstPrint.stream().map( mpPrint -> {
				mpPrint.setDelta(mpPrint.getDelta() * mpPrint.getCantidad() / mpTotalByCodigo.get(mpPrint.getCodigo()));
				return mpPrint;
				}).sorted().collect(Collectors.toList()));
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("Error"));
		}
		
		
		
		
	}
	
	@GetMapping("/validar/{codigo}/{porcentaje}/{piezas}/{presentacion}/{tipo}")
	public ResponseEntity<?> validaMPforOF(@PathVariable final String codigo, @PathVariable final double porcentaje, @PathVariable final double piezas, @PathVariable final double presentacion, @PathVariable final String tipo){
		
		List<MatPrimaOrdFab> lstRspMPOF = new ArrayList<MatPrimaOrdFab>();
		List<MateriaPrima> lstMateriaPrima = new ArrayList<MateriaPrima>();
		final List<MateriaPrima> lstMateriaPrimaBase = new ArrayList<MateriaPrima>();
		double cantReq = (porcentaje / PERCENT) * piezas * presentacion * MILILITROS;
		
		if(codigo.equals(AGUA)) {
			MatPrimaOrdFab mpof = new MatPrimaOrdFab(codigo, "AGUA" ,cantReq,"","OK","",PRODUCTO);
			lstRspMPOF.add(mpof);
			return ResponseEntity.ok(lstRspMPOF);
		}
		if(tipo.equals(PRODUCTO)) {
			lstMateriaPrima = repomatprima.findByCodigoAndCantidadMoreThanOrderByFechaCaducidad(codigo,0);	
		}else {
			List<Bases> bases = repobase.findByCodigoAndCantidadRestanteMoreThan(codigo, 0);
			bases.forEach(base ->{
				MateriaPrima mp = new MateriaPrima();
				mp.setAprobado(base.isAprobado());
				mp.setCantidad(base.getCantidadRestante());
				mp.setApartado(base.getApartado());
				mp.setCodigo(base.getCodigo());
				mp.setDescripcion(base.getNombre());
				mp.setLote(base.getLote());
				mp.setFechaCaducidad(base.getFechaProducccion());
				mp.setTipoMP(BASE);
				lstMateriaPrimaBase.add(mp);
			});
			lstMateriaPrima = lstMateriaPrimaBase;
		}
		
		
		NumberFormat nf = NumberFormat.getInstance(new Locale("es","MX"));
		nf.setMaximumFractionDigits(MAX_NUM_DIGITS);
		lstMateriaPrima.sort((d1,d2)->d1.getFechaCaducidad().compareTo(d2.getFechaCaducidad()));   
		for(MateriaPrima matprima : lstMateriaPrima ){
			if(matprima.isAprobado()) {
				if(matprima.getCantidad() - matprima.getApartado() - cantReq >= 0 ) { // El lote tiene suficiente y lo toma
					MatPrimaOrdFab mpof = new MatPrimaOrdFab(matprima.getCodigo(), matprima.getDescripcion(),Double.parseDouble(nf.format(cantReq)),matprima.getLote(),"OK","",matprima.getTipoMP());
					cantReq = 0;
					lstRspMPOF.add(mpof);				
					break;
				}else if((matprima.getCantidad() - matprima.getApartado()) >= 0) { //Este toma lo que queda disponible en el Lote
					MatPrimaOrdFab mpof = new MatPrimaOrdFab(matprima.getCodigo(),matprima.getDescripcion(),Double.parseDouble(nf.format(matprima.getCantidad())),matprima.getLote(),"OK","",matprima.getTipoMP());
					lstRspMPOF.add(mpof);
					cantReq -= (matprima.getCantidad() - matprima.getApartado());
				}
			}else {
				/*
				 * Cuando viene de una Base, pueden haber muchos lotes
				 * yo creo que esta bien que regrese todos los lotes, para que se sepa porque no 
				 * hay uno que cumpla
				 */
				MatPrimaOrdFab mpof = new MatPrimaOrdFab(matprima.getCodigo(),matprima.getDescripcion(),Double.parseDouble(nf.format(cantReq)),matprima.getLote(),"ERROR","La MP no ha sido aprobada, por lo tanto no es posible utilizarla",matprima.getTipoMP());
				cantReq = 0;
				lstRspMPOF.add(mpof);
			}
		}
		
		if(!lstRspMPOF.isEmpty() && cantReq == 0) {
			return ResponseEntity.ok(lstRspMPOF);
		}else {
			MatPrimaOrdFab mpof = null;
			lstRspMPOF.clear();
			if(lstMateriaPrima.isEmpty()) {
				Optional<ProductoDisponible> pd = Optional.ofNullable(repoproddisp.findByClave(codigo).get(0));
				 mpof = new MatPrimaOrdFab(codigo,pd.get().getNombre(), cantReq, codigo, "ERROR", "Materia prima no encontrada en Almacen",PRODUCTO);
			}else if(cantReq > 0) {
				DecimalFormat df = new DecimalFormat("###,###,###.##");
				mpof = new MatPrimaOrdFab(lstMateriaPrima.get(0).getCodigo(), lstMateriaPrima.get(0).getDescripcion(),(porcentaje / PERCENT) * piezas * presentacion * MILILITROS , codigo, "ERROR", "MP insuficiente por "+df.format(cantReq),PRODUCTO);
			}else if(cantReq < 0) {
				DecimalFormat df = new DecimalFormat("###,###,###.##");
				mpof = new MatPrimaOrdFab(lstMateriaPrima.get(0).getCodigo(), lstMateriaPrima.get(0).getDescripcion(),Double.parseDouble(df.format((porcentaje / PERCENT) * piezas * presentacion * MILILITROS)) , codigo, "ERROR", "El lote no cuenta con insuficiente MP, le faltan "+df.format(Math.abs(cantReq)),PRODUCTO);
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
				if(mp.getTipo().equals(PRODUCTO)) {
					Optional<MateriaPrima> matprima = repomatprima.findByLote(mp.getLote());
					materiaInsuficiente = matprima.get().getCantidad() - matprima.get().getApartado() - mp.getDelta() < 0;
				}else {
					Optional<Bases> base = repobase.findByLote(mp.getLote());
					materiaInsuficiente = base.get().getCantidadRestante() - base.get().getApartado() - mp.getDelta() < 0;
				}
				
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
		
		/*Actualizar la MP reservada */
		ordenFabricacion.getMatprima().forEach(mp->{
			if(mp.getDelta() != 0) {
				if(mp.getTipo().equals(PRODUCTO)) {
					Optional<MateriaPrima> mpu = repomatprima.findByLote(mp.getLote());
					MateriaPrima mpf = mpu.get();
					mpf.setApartado(mpf.getApartado() + mp.getDelta());
					repomatprima.save(mpf);
				}else if(mp.getTipo().equals(BASE)) {
					Optional<Bases> basef = repobase.findByLote(mp.getLote());
					Bases baseu = basef.get();
					baseu.setApartado(baseu.getApartado() + mp.getDelta());
					repobase.save(baseu);
				}
			}
		});
		
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
	 * Quita la cantidad que estaba en apartado y lo descuenta de MP disponible
	 */
	@GetMapping("/complete/{idOrdenFabricacion}")
	public ResponseEntity<?> completeOF(@PathVariable final String idOrdenFabricacion ){
		Optional<OrdenFabricacion> off = repoOF.findById(idOrdenFabricacion);
		List<MatPrimaOrdFab> matPrimOrdFab;
		List<MateriaPrima> mpUpdt = new ArrayList<MateriaPrima>();
		List<Bases> baseUpdt = new ArrayList<Bases>();
		if(off.isPresent()) {			
			OrdenFabricacion ofu = off.get();
			matPrimOrdFab = ofu.getMatprima();
			for(MatPrimaOrdFab mpof : matPrimOrdFab){
				if(mpof.getCodigo().equals(AGUA)) {
					continue;
				}
				/*Cuando el tipo es NULL significa ques es un Producto */
				if(mpof.getTipo()==null) {
					Optional<MateriaPrima> mpf = repomatprima.findByLote(mpof.getLote());
					if(mpf.isPresent()) {
						MateriaPrima mpu = mpf.get();
						mpu.setApartado(mpu.getApartado()- mpof.getCantidad());
						mpu.setCantidad(mpu.getCantidad() - mpof.getCantidad());
						mpUpdt.add(mpu);
					}
				/*Es una base*/	
				}else if(mpof.getTipo().equals(BASE)) {
					Optional<Bases> basef = repobase.findByLote(mpof.getLote());
					if(basef.isPresent()) {
						Bases baseu = basef.get();
						baseu.setApartado(baseu.getApartado() - mpof.getCantidad());
						baseu.setCantidadRestante(baseu.getCantidadRestante() - mpof.getCantidad());
						if(baseu.getCantidadRestante()==0) {
							baseu.setEstatus(Eestatus.PCLOSE.toString());
						}
						baseUpdt.add(baseu);
					}
					
				}
			}
			repomatprima.saveAll(mpUpdt);
			if(!baseUpdt.isEmpty()) {
				repobase.saveAll(baseUpdt);
			}
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
			/*Eliminar las MPs que estan en 0*/
			//repomatprima.deleteByCantidad(0);
			// Preguntar si esto se debe hacer, esto elimina todas las materias primas que ya no tienen cantidad
			List<MateriaPrima> matprimdelete = repomatprima.findByCantidad(0);
			for(MateriaPrima mpd : matprimdelete) {
				repomatprima.deleteById(mpd.getId());
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
