package mx.com.gruponordan.controller;

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

import mx.com.gruponordan.model.MessageResponse;
import mx.com.gruponordan.model.ProductoDisponible;
import mx.com.gruponordan.repository.ProdDispDAO;

@RestController
@RequestMapping("/api/prodisp")
@CrossOrigin(origins = "http://localhost:3000")
public class ProdDispController {
	
	@Autowired
	ProdDispDAO prddisprepo;
	
	@GetMapping
	public ResponseEntity<?> getAllProdDisp(){
		return ResponseEntity.ok( prddisprepo.findAll());
	}
	
	@PostMapping
	public ResponseEntity<?> saveProdDisp(@RequestBody final ProductoDisponible prddisp){
		ProductoDisponible prdinsrt = prddisprepo.save(prddisp);
		if(prdinsrt!=null) {
			return ResponseEntity.ok(prdinsrt);
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("Error al insertar"));
		}
	}
	
	@PutMapping("/{idPrdDisp}")
	public ResponseEntity<?> updateProdDisp(@PathVariable final String idPrdDisp, @RequestBody final ProductoDisponible prddisp){
		Optional<ProductoDisponible> prdFound = prddisprepo.findById(idPrdDisp);
		if(prdFound.isPresent()) {
			ProductoDisponible prdDispUpdt = prdFound.get();
			prdDispUpdt.setClave(prddisp.getClave());
			prdDispUpdt.setNombre(prddisp.getNombre());
			prdDispUpdt.setMateriaPrimaUsada(prddisp.getMateriaPrimaUsada());
			return ResponseEntity.ok(prddisprepo.save(prdDispUpdt));
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("status:error"));
		}
	}
	
	
	@DeleteMapping("/{idProdDisp}")
	private ResponseEntity<?> deleteProdDisp(@PathVariable final String idProdDisp){
		prddisprepo.deleteById(idProdDisp);
		return ResponseEntity.ok().body(new MessageResponse("success"));
	}

}
