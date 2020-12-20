package mx.com.gruponordan.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.com.gruponordan.model.Bitacora;
import mx.com.gruponordan.model.MessageResponse;
import mx.com.gruponordan.repository.BitacoraDAO;

@RestController
@RequestMapping("/api/bitacora")
public class BitacoraController {

	//private static Logger logger = LoggerFactory.getLogger(Bitacora.class);
	
	@Autowired
	BitacoraDAO bitacorarepo;
	
	@PostMapping
	public ResponseEntity<?> insertaRegistro(@RequestBody final Bitacora bitacora){
		return ResponseEntity.ok(bitacorarepo.save(bitacora));
	}
	
	@GetMapping
	public ResponseEntity<?> getAllBitacoras(){
		List<Bitacora> lista = bitacorarepo.findAll();
		if(lista!=null && lista.size() > 0) {
			return ResponseEntity.ok(lista);
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("No existen datos en la bitacora"));
		}
		
	}
	
	@GetMapping("/{fechaIni}/{fechaFin}")
	public ResponseEntity<?> getBitacoraByFechas(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") final Date fechaIni, 
												 @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") final Date fechaFin){
		//logger.info(fechaIni+" "+fechaFin);
		
		List<Bitacora> lista = bitacorarepo.findByFechaEventoBetween(fechaIni, fechaFin);
		if(lista!= null && lista.size() > 0) {
			return ResponseEntity.ok(lista);
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("error"));
		}
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<?> getBitacoraById(@PathVariable final String userId){
		List<Bitacora> lista = bitacorarepo.findByUserId(userId);
		if(lista!= null && lista.size() > 0) {
			return ResponseEntity.ok(lista);
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("error"));
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable(required = true) final String id){
		bitacorarepo.deleteById(id);
		return ResponseEntity.ok().body(new MessageResponse("success"));
	}
	
	
}
