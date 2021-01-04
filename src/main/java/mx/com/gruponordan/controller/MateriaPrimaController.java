package mx.com.gruponordan.controller;

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

import io.swagger.annotations.ApiOperation;
import mx.com.gruponordan.model.MateriaPrima;
import mx.com.gruponordan.model.MateriaPrimaDisponible;
import mx.com.gruponordan.model.MessageResponse;
import mx.com.gruponordan.repository.MatPrimaDispDAO;
import mx.com.gruponordan.repository.MateriaPrimaDAO;

@RestController
@RequestMapping("/api/matprima")
@CrossOrigin(origins = "http://localhost:3000")
public class MateriaPrimaController {

	//private static Logger logger = LoggerFactory.getLogger(MateriaPrima.class);
	
	@Autowired
	MateriaPrimaDAO repoMP;
	
	@Autowired
	MatPrimaDispDAO repoMatPrimDisp;
	
	@ApiOperation(value="Regresa todas las materias primas")
	@GetMapping()
	public List<MateriaPrima> getAllMP() {
		return repoMP.findAll();
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

	@PostMapping()
	public ResponseEntity<?> saveMP(@RequestBody(required = true) MateriaPrima matprima) {
		MateriaPrima mp = repoMP.save(matprima);
		if(mp!=null) {
			Optional<MateriaPrimaDisponible> mpdisFound = repoMatPrimDisp.findByCodigo(mp.getCodigo());
			if(!mpdisFound.isPresent()) {
				MateriaPrimaDisponible mpdisins = new MateriaPrimaDisponible(mp.getDescripcion(),mp.getUnidad(),mp.getCodigo());
				repoMatPrimDisp.save(mpdisins);
			}
			return ResponseEntity.ok().body(repoMP.save(matprima)) ;
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("error al guardar la materia prima"));
		}
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateMP(@PathVariable(required = true) String id, @RequestBody MateriaPrima matprima){
		//System.out.println(matprima);
		Optional<MateriaPrima> mpf = repoMP.findById(id);
		if(mpf.isPresent()) {
			MateriaPrima mpu = mpf.get();
			mpu.setDescripcion(matprima.getDescripcion());
			mpu.setCodigo(matprima.getCodigo());
			mpu.setCantidad(matprima.getCantidad());
			mpu.setObservaciones(matprima.getObservaciones());
			mpu.setProveedor(matprima.getProveedor());
			mpu.setUnidad(matprima.getUnidad());
			mpu.setEscaso(matprima.getEscaso());
			mpu.setNecesario(matprima.getNecesario());
			mpu.setFechaEntrada(matprima.getFechaEntrada());
			mpu.setFechaCaducidad(matprima.getFechaCaducidad());
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
