package mx.com.gruponordan.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.com.gruponordan.model.Bitacora;
import mx.com.gruponordan.model.BitacoraActv;
import mx.com.gruponordan.model.MessageResponse;
import mx.com.gruponordan.model.User;
import mx.com.gruponordan.repository.BitacoraActvDAO;
import mx.com.gruponordan.repository.BitacoraDAO;
import mx.com.gruponordan.repository.UserRepository;

@RestController
@RequestMapping("/api/bitacora")
@CrossOrigin(origins = "http://localhost:3000")
public class BitacoraController {

	//private static Logger logger = LoggerFactory.getLogger(Bitacora.class);
	
	@Autowired
	BitacoraDAO bitacorarepo;
	
	@Autowired
	BitacoraActvDAO bitactvrepo;
	
	@Autowired
	UserRepository userrepo;
	
	@PostMapping
	public ResponseEntity<?> insertaRegistro(@RequestBody final Bitacora bitacora){
		
		Optional<BitacoraActv> bitAct = bitactvrepo.findByName(bitacora.getTipoEvento().getName());
		Bitacora bitInsert = new Bitacora(bitacora.getUser(),new Date(),bitAct.get(),bitacora.getValPrevio(),bitacora.getValActual());
		//logger.info(bitInsert.toString());
		return ResponseEntity.ok(bitacorarepo.save(bitInsert));
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
		
		List<Bitacora> lista = bitacorarepo.findByFechaEventoBetween(fechaIni, fechaFin);
		if(lista!= null && lista.size() > 0) {
			return ResponseEntity.ok(lista);
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("error"));
		}
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<?> getBitacoraById(@PathVariable final String userId){
		Optional<User> user = userrepo.findById(userId);
		List<Bitacora> lista = bitacorarepo.findByUser(user.get());
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
