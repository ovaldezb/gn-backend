package mx.com.gruponordan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.com.gruponordan.model.Sequence;
import mx.com.gruponordan.repository.AreasDAO;
import mx.com.gruponordan.repository.SequenceDAO;
import mx.com.gruponordan.repository.UnidadesDAO;

@RestController
@RequestMapping("/api/utils")
@CrossOrigin(origins = "*")
public class UtilsController {

	@Autowired
	UnidadesDAO unidadrepo;
	
	@Autowired
	AreasDAO repoarea;
	
	@Autowired
	SequenceDAO reposeq;
	
	@Autowired
	MongoOperations mongoperations;
	
	@GetMapping("/unidad")
	public ResponseEntity<?> getAllUnidades(){
		return ResponseEntity.ok(unidadrepo.findAll());
	}
	
	@GetMapping("/unidad/{desc}")
	public ResponseEntity<?> getUnidadByDesc(@PathVariable final String desc){
		return ResponseEntity.ok(unidadrepo.findByUnidadMedida(desc));
	}
	
	@GetMapping("/areas")
	public ResponseEntity<?> getAllAreas(){
		return ResponseEntity.ok(repoarea.findAll());
	}
	
	@GetMapping("/count")
	public ResponseEntity<?> getMaxOF(){
		Sequence c = null;
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is("ordenfab_sequence"));
		FindAndModifyOptions options = new FindAndModifyOptions();
		c = mongoperations.findAndModify(query, new Update().inc("counter",1),options.returnNew(true).upsert(true),Sequence.class );
		return ResponseEntity.ok(c);
	}
	
}
