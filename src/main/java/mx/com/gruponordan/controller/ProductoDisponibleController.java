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

import mx.com.gruponordan.interfaz.Definitions;
import mx.com.gruponordan.model.MateriaPrimaDisponible;
import mx.com.gruponordan.model.MessageResponse;
import mx.com.gruponordan.model.ProductoDisponible;
import mx.com.gruponordan.model.UnidadMedida;
import mx.com.gruponordan.repository.MatPrimaDispDAO;
import mx.com.gruponordan.repository.ProdDispDAO;
import mx.com.gruponordan.repository.UnidadesDAO;

@RestController
@RequestMapping("/api/prodisp")
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "*")
public class ProductoDisponibleController implements Definitions{
	
	@Autowired
	ProdDispDAO prddisprepo;
	
	@Autowired
	MatPrimaDispDAO repoMatPrimDisp;
	
	@Autowired
	UnidadesDAO repoUnidad;
	
	@GetMapping
	public ResponseEntity<?> getAllProdDisp(){
		return ResponseEntity.ok( prddisprepo.findAll());
	}
	
	@GetMapping("/{clave}")
	public ResponseEntity<?> getProdDispByClave(@PathVariable final String clave){
		return ResponseEntity.ok(prddisprepo.findByClave(clave));
	}
	
	@PostMapping
	public ResponseEntity<?> saveProdDisp(@RequestBody final ProductoDisponible prddisp){
		ProductoDisponible prdinsrt = prddisprepo.save(prddisp);
		if(prdinsrt!=null) {
			if(prddisp.getTipoProducto().equals("B")) {
				Optional<MateriaPrimaDisponible> mpdisFound = repoMatPrimDisp.findByCodigo(prddisp.getClave());
				UnidadMedida um = repoUnidad.findByUnidadMedida("Kilos");
				if(!mpdisFound.isPresent()) {
					MateriaPrimaDisponible mpdisins = new MateriaPrimaDisponible(prddisp.getNombre(),um,prddisp.getClave(),BASE);
					repoMatPrimDisp.save(mpdisins);
				}
			}
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
			prdDispUpdt.setProdxcaja(prddisp.getProdxcaja());
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
