package mx.com.gruponordan.controller;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.com.gruponordan.interfaz.Definitions;
import mx.com.gruponordan.model.Bases;
import mx.com.gruponordan.model.MatPrimaOrdFab;
import mx.com.gruponordan.model.MateriaPrima;
import mx.com.gruponordan.model.MessageResponse;
import mx.com.gruponordan.model.ProductoDisponible;
import mx.com.gruponordan.repository.BasesDAO;
import mx.com.gruponordan.repository.MateriaPrimaDAO;
import mx.com.gruponordan.repository.ProdDispDAO;

@RestController
@RequestMapping("/api/bases")
@CrossOrigin(origins = "*")
public class BasesController implements Definitions{

	@Autowired
	BasesDAO basesrepo;
	
	@Autowired
	ProdDispDAO proddisprepo;
	
	@Autowired
	MateriaPrimaDAO repomatprima;
	
	@GetMapping
	public ResponseEntity<?> getListaBases(){
		return ResponseEntity.ok(basesrepo.findAll());
	}
	
	@GetMapping("/{clave}")
	public ResponseEntity<?> getBasesByClave(@PathVariable String clave){
		Optional<ProductoDisponible> proddispfound = proddisprepo.findByClaveAndTipoProducto(clave, "B");
		if(proddispfound.isPresent()) {
			return ResponseEntity.ok(proddispfound.get());	
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("Error"));
		}	
	}
	
	@GetMapping("/lote/{lote}")
	public ResponseEntity<?> getBasesByLote(@PathVariable String lote){
		Optional<Bases> basefound = basesrepo.findByLote(lote);
		if(basefound.isPresent()) {
			return ResponseEntity.ok(basefound.get());	
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("Error"));
		}	
	}
	
	@GetMapping("/validar/{codigo}/{porcentaje}/{cantTotal}")
	public ResponseEntity<?> calculaMPBase(@PathVariable String codigo,@PathVariable final double porcentaje,  @PathVariable final double cantTotal){
		List<MatPrimaOrdFab> lstRspMPOF = new ArrayList<MatPrimaOrdFab>();
		double cantReq = (porcentaje / PERCENT) * cantTotal;
		if(codigo.equals(AGUA)) {
			MatPrimaOrdFab mpof = new MatPrimaOrdFab(codigo, "AGUA" ,cantReq,"","OK","");
			lstRspMPOF.add(mpof);
			return ResponseEntity.ok(lstRspMPOF);
		}
		List<MateriaPrima> lstMateriaPrima = repomatprima.findByCodigoAndCantidadMoreThanOrderByFechaCaducidad(codigo,0);
		NumberFormat nf = NumberFormat.getInstance(new Locale("es","MX"));
		nf.setMaximumFractionDigits(MAX_NUM_DIGITS);
		lstMateriaPrima.sort((d1,d2)->d1.getFechaCaducidad().compareTo(d2.getFechaCaducidad()));   
		for(MateriaPrima matprima : lstMateriaPrima ){
			if(matprima.isAprobado()) {
				if(matprima.getCantidad() - matprima.getApartado() - cantReq >= 0 ) { // El lote tiene suficiente y lo toma
					MatPrimaOrdFab mpof = new MatPrimaOrdFab(matprima.getCodigo(), matprima.getDescripcion(),Double.parseDouble(nf.format(cantReq)),matprima.getLote(),"OK","");
					cantReq = 0;
					lstRspMPOF.add(mpof);				
					break;
				}else if((matprima.getCantidad() - matprima.getApartado()) >= 0) { //Este toma lo que queda disponible en el Lote
					MatPrimaOrdFab mpof = new MatPrimaOrdFab(matprima.getCodigo(),matprima.getDescripcion(),Double.parseDouble(nf.format(matprima.getCantidad())),matprima.getLote(),"OK","");
					lstRspMPOF.add(mpof);
					cantReq -= (matprima.getCantidad() - matprima.getApartado());
				}
			}else {
				MatPrimaOrdFab mpof = new MatPrimaOrdFab(matprima.getCodigo(),matprima.getDescripcion(),Double.parseDouble(nf.format(cantReq)),matprima.getLote(),"ERROR","La MP no ha sido aprobada, por lo tanto no es posible utilizarla");
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
				 mpof = new MatPrimaOrdFab(codigo,"", cantReq, codigo, "ERROR", "Materia prima no encontrada");
			}else if(cantReq > 0) {
				DecimalFormat df = new DecimalFormat("###,###,###.##");
				mpof = new MatPrimaOrdFab(lstMateriaPrima.get(0).getCodigo(), lstMateriaPrima.get(0).getDescripcion(),Double.parseDouble(df.format((porcentaje / PERCENT) * cantTotal)) , codigo, "ERROR", "MP insuficiente por "+df.format(cantReq));
			}else if(cantReq < 0) {
				DecimalFormat df = new DecimalFormat("###,###,###.##");
				mpof = new MatPrimaOrdFab(lstMateriaPrima.get(0).getCodigo(), lstMateriaPrima.get(0).getDescripcion(),Double.parseDouble(df.format((porcentaje / PERCENT) * cantTotal)) , codigo, "ERROR", "El lote no cuenta con insuficiente MP, le faltan "+df.format(Math.abs(cantReq)));
			}			
			lstRspMPOF.add(mpof);
			return ResponseEntity.ok(lstRspMPOF);
		}
		//return ResponseEntity.ok(basesrepo.save(base));
	}
	
	@PostMapping
	public ResponseEntity<?> saveBase(@RequestBody Bases base){
		return ResponseEntity.ok(basesrepo.save(base));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateBase(@PathVariable String id, @RequestBody Bases base){
		Optional<Bases> bf = basesrepo.findById(id);
			Bases baseUpdt = bf.get();
			baseUpdt.setLote(base.getLote());
			baseUpdt.setNombre(base.getNombre());
			return ResponseEntity.ok(basesrepo.save(baseUpdt));
	}
	
	
}
