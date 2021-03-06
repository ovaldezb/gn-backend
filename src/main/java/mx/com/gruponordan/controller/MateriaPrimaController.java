package mx.com.gruponordan.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
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

import com.mongodb.BasicDBObject;

import io.swagger.annotations.ApiOperation;
import mx.com.gruponordan.interfaz.Definitions;
import mx.com.gruponordan.model.MateriaPrima;
import mx.com.gruponordan.model.MateriaPrimaDisponible;
import mx.com.gruponordan.model.MessageResponse;
import mx.com.gruponordan.repository.MatPrimaDispDAO;
import mx.com.gruponordan.repository.MateriaPrimaDAO;

@RestController
@RequestMapping("/api/matprima")
@CrossOrigin(origins = "*")
public class MateriaPrimaController implements Definitions{

	//private static Logger logger = LoggerFactory.getLogger(MateriaPrima.class);
	
	@Autowired
	MateriaPrimaDAO repoMP;
	
	@Autowired
	MatPrimaDispDAO repoMatPrimDisp;
	
	@Autowired
	MongoOperations mongoperations;
	
	@ApiOperation(value="Regresa todas las materias primas")
	@GetMapping()
	public List<MateriaPrima> getAllMP() {
		Sort sort = Sort.by(Sort.Direction.ASC, "fechaCaducidad");
		return repoMP.findMateriasPrimasGtCantidad(0.0,sort);
	}
	
	@GetMapping("/mp")
	public ResponseEntity<?> getMPExistente(){		
		TypedAggregation<MateriaPrima> mpaggreg = Aggregation.newAggregation(MateriaPrima.class,Aggregation.group("codigo").push(new BasicDBObject("_id","$_id").append("descripcion", "$descripcion")).as("mp"));
		AggregationResults<MateriaPrima> results = mongoperations.aggregate(mpaggreg, MateriaPrima.class);
		return ResponseEntity.ok(results.getMappedResults());
		//return ResponseEntity.ok("OK");
	}
	
	@GetMapping("/ini/{date}")
	public ResponseEntity<List<MateriaPrima>> getMPByCadLt(@PathVariable("date") final String date){
		SimpleDateFormat parser = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
		Date fechaIni = null;
		try {
			fechaIni = parser.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<MateriaPrima> lista = repoMP.findByFechaCaducidadLt(fechaIni);
		List<MateriaPrima> listaFiltrada = lista.stream().filter(mp->mp.getCantidad()>0.0).collect(Collectors.toList());
		listaFiltrada.sort((d1,d2)->d1.getFechaCaducidad().compareTo(d2.getFechaCaducidad()));
		return ResponseEntity.ok(listaFiltrada);
	}
	
	@GetMapping("/fin/{date}")
	public ResponseEntity<List<MateriaPrima>> getMPByCadGt(@PathVariable("date") final String date){
		SimpleDateFormat parser = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
		Date fechaFin = null;
		try {
			fechaFin = parser.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<MateriaPrima> lista = repoMP.findByFechaCaducidadGt(fechaFin);
		List<MateriaPrima> listaFiltrada = lista.stream().filter(mp->mp.getCantidad()>0.0).collect(Collectors.toList());
		listaFiltrada.sort((d1,d2)->d1.getFechaCaducidad().compareTo(d2.getFechaCaducidad()));
		return ResponseEntity.ok(listaFiltrada);
	}
	
	@GetMapping("/btw/{fecini}/{fecfin}")
	public ResponseEntity<List<MateriaPrima>> getMPByCadBtwn(@PathVariable("fecini") final String fecini, @PathVariable("fecfin") final String fecfin){
		SimpleDateFormat parser = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
		Date fechaIni = null;
		Date fechaFin = null;
		try {
			fechaIni = parser.parse(fecini);
			fechaFin = parser.parse(fecfin);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<MateriaPrima> lista = repoMP.findByFechaCaducidadBetweenOrderByFechaCaducidadAsc(fechaIni,fechaFin);
		List<MateriaPrima> listaFiltrada = lista.stream().filter(mp->mp.getCantidad()>0.0).collect(Collectors.toList());
		listaFiltrada.sort((d1,d2)->d1.getFechaCaducidad().compareTo(d2.getFechaCaducidad()));
		return ResponseEntity.ok(listaFiltrada);
	}

	@ApiOperation(value="Obtiene una MP por su ID")
	@GetMapping("/{id}")
	public ResponseEntity<?> getMatPrimaById(@PathVariable("id") final String idMatPrim) {
		Optional<MateriaPrima> mp = repoMP.findById(idMatPrim);
		if(mp !=null) {
			return ResponseEntity.ok().body(mp);
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("error:no se pudo almacenar la mp"));
		}	
	}
	
	@ApiOperation(value="Obtiene una MP por su Codigo")
	@GetMapping("/codigo/{codigo}")
	public ResponseEntity<?> getMatPrimaByCodigo(@PathVariable("codigo") final String codigo) {
		List<MateriaPrima> mp = repoMP.findByCodigo(codigo);
		if(!mp.isEmpty()) {
			return ResponseEntity.ok().body(mp);
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("error:no se pudo recuperar la mp"));
		}	
	}
	
	@GetMapping("/lote/{lote}") 
	public ResponseEntity<?> getMPByLote(@PathVariable final String lote){
		Optional<MateriaPrima> mpf = repoMP.findByLote(lote);
		if(mpf.isPresent()) {
			return ResponseEntity.ok(mpf.get());
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("error"));
		}
	}

	@PostMapping()
	public ResponseEntity<?> saveMP(@RequestBody(required = true) MateriaPrima matprima) {
		MateriaPrima mp = repoMP.save(matprima);
		if(mp!=null) {
			Optional<MateriaPrimaDisponible> mpdisFound = repoMatPrimDisp.findByCodigo(mp.getCodigo());
			if(!mpdisFound.isPresent()) {
				MateriaPrimaDisponible mpdisins = new MateriaPrimaDisponible(mp.getDescripcion(),mp.getUnidad(),mp.getCodigo(),PRODUCTO);
				repoMatPrimDisp.save(mpdisins);
			}
			return ResponseEntity.ok().body(repoMP.save(matprima)) ;
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("error al guardar la materia prima disponible"));
		}
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateMP(@PathVariable(required = true) String id, @RequestBody MateriaPrima matprima){
		Optional<MateriaPrima> mpf = repoMP.findById(id);
		if(mpf.isPresent()) {
			MateriaPrima mpu = mpf.get();
			mpu.setDescripcion(matprima.getDescripcion());
			mpu.setCodigo(matprima.getCodigo());
			mpu.setCantidad(matprima.getCantidad());
			mpu.setCantidadOriginal(matprima.getCantidadOriginal());
			mpu.setObservaciones(matprima.getObservaciones());
			mpu.setProveedor(matprima.getProveedor());
			mpu.setUnidad(matprima.getUnidad());
			mpu.setEscaso(matprima.getEscaso());
			mpu.setNecesario(matprima.getNecesario());
			mpu.setFechaEntrada(matprima.getFechaEntrada());
			mpu.setFechaCaducidad(matprima.getFechaCaducidad());
			mpu.setTipo(matprima.getTipo());
			mpu.setLote(matprima.getLote());
			mpu.setAprobado(matprima.isAprobado());
			mpu.setFechaAprobacion(matprima.getFechaAprobacion());
			return ResponseEntity.ok(repoMP.save(mpu));
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("status:error"));
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteMPById(@PathVariable("id") final String idMatPrima) {
		repoMP.deleteById(idMatPrima);
		return ResponseEntity.ok().body(new MessageResponse("success"));
	}
	
	@DeleteMapping
	public ResponseEntity<?> deleteAllMP() {
		repoMP.deleteAll();
		return ResponseEntity.ok().body(new MessageResponse("success"));
	}
	
	
}
