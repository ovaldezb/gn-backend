package mx.com.gruponordan.controller;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;

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
	
	@GetMapping("/all/{activos}")
	public ResponseEntity<?> getListaBases(@PathVariable boolean activos){
		if(activos) {
			return ResponseEntity.ok(basesrepo.findAll());
		}else {
			return ResponseEntity.ok(basesrepo.findByEstatusNotLike("CMPLT"));
		}
	}
	
	@GetMapping("/{clave}")
	public ResponseEntity<?> getBasesByClave(@PathVariable String clave){
		Optional<ProductoDisponible> proddispfound = proddisprepo.findByClaveAndTipoProducto(clave, "B");
		if(proddispfound.isPresent()) {
			return ResponseEntity.ok(proddispfound.get());	
		}else {
			//return ResponseEntity.status(404).body(null);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cause description here");
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
	
	@GetMapping("/verificar/{codigo}/{porcentaje}/{cantTotal}")
	public ResponseEntity<?> calculaMPBase(@PathVariable String codigo,@PathVariable final double porcentaje,  @PathVariable final double cantTotal){
		List<MatPrimaOrdFab> lstRspMPOF = new ArrayList<MatPrimaOrdFab>();
		double cantReq = (porcentaje / PERCENT) * cantTotal;
		if(codigo.equals(AGUA)) {
			MatPrimaOrdFab mpof = new MatPrimaOrdFab(codigo, "AGUA" ,cantReq,"","OK","",PRODUCTO);
			lstRspMPOF.add(mpof);
			return ResponseEntity.ok(lstRspMPOF);
		}
		List<MateriaPrima> lstMateriaPrima = repomatprima.findByCodigoAndCantidadMoreThanOrderByFechaCaducidad(codigo,0);
		NumberFormat nf = NumberFormat.getInstance(new Locale("es","MX"));
		nf.setMaximumFractionDigits(MAX_NUM_DIGITS);
		List<MateriaPrima> lstRsFilt = lstMateriaPrima.stream().filter(mp->(mp.getCantidad()-mp.getApartado()) > 0).collect(Collectors.toList());
		lstRsFilt.sort((d1,d2)->d1.getFechaCaducidad().compareTo(d2.getFechaCaducidad()));   
		for(MateriaPrima matprima : lstRsFilt ){
			if(matprima.isAprobado()) {
				if(matprima.getCantidad() - matprima.getApartado() - cantReq >= 0 ) { // El lote tiene suficiente y lo toma
					MatPrimaOrdFab mpof = new MatPrimaOrdFab(matprima.getCodigo(), matprima.getDescripcion(),Double.parseDouble(nf.format(cantReq)),matprima.getLote(),"OK","",BASE);
					cantReq = 0;
					lstRspMPOF.add(mpof);				
					break;
				}else if((matprima.getCantidad() - matprima.getApartado()) >= 0) { //Este toma lo que queda disponible en el Lote
					MatPrimaOrdFab mpof = new MatPrimaOrdFab(matprima.getCodigo(),matprima.getDescripcion(),Double.parseDouble(nf.format(matprima.getCantidad() - matprima.getApartado())),matprima.getLote(),"OK","",BASE);
					lstRspMPOF.add(mpof);
					cantReq -= (matprima.getCantidad() - matprima.getApartado());
				}
			}else {
				MatPrimaOrdFab mpof = new MatPrimaOrdFab(matprima.getCodigo(),matprima.getDescripcion(),Double.parseDouble(nf.format(cantReq)),matprima.getLote(),"ERROR","La MP no ha sido aprobada, por lo tanto no es posible utilizarla",BASE);
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
				 mpof = new MatPrimaOrdFab(codigo,"", cantReq, codigo, "ERROR", "Materia prima no encontrada",BASE);
			}else if(cantReq > 0) {
				DecimalFormat df = new DecimalFormat("###,###,###.##");
				mpof = new MatPrimaOrdFab(lstMateriaPrima.get(0).getCodigo(), lstMateriaPrima.get(0).getDescripcion(),Double.parseDouble(df.format((porcentaje / PERCENT) * cantTotal)) , codigo, "ERROR", "MP insuficiente por "+df.format(cantReq),BASE);
			}else if(cantReq < 0) {
				DecimalFormat df = new DecimalFormat("###,###,###.##");
				mpof = new MatPrimaOrdFab(lstMateriaPrima.get(0).getCodigo(), lstMateriaPrima.get(0).getDescripcion(),Double.parseDouble(df.format((porcentaje / PERCENT) * cantTotal)) , codigo, "ERROR", "El lote no cuenta con insuficiente MP, le faltan "+df.format(Math.abs(cantReq)),BASE);
			}			
			lstRspMPOF.add(mpof);
			return ResponseEntity.ok(lstRspMPOF);
		}
	}
	
	@PostMapping
	public ResponseEntity<?> saveBase(@RequestBody Bases base){
		List<MatPrimaOrdFab> matPrimOrdFab = Arrays.asList(base.getMateriaPrimaOrdFab());
		List<MateriaPrima> mpUpdt = new ArrayList<MateriaPrima>();
		matPrimOrdFab.stream().filter(mp->!mp.getCodigo().equals(AGUA)).forEach(mpof -> {
			Optional<MateriaPrima> mpf = repomatprima.findByLote(mpof.getLote());
			if(mpf.isPresent()) {
				MateriaPrima mpu = mpf.get();
				mpu.setApartado(mpu.getApartado() + mpof.getCantidad());
				mpUpdt.add(mpu);
			}
		});
		repomatprima.saveAll(mpUpdt);
		return ResponseEntity.ok(basesrepo.save(base));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteBaseOpen(@PathVariable String id){
		List<MateriaPrima> mpUpdt = new ArrayList<MateriaPrima>();
		Optional<Bases> baseFound = basesrepo.findById(id);
		if(baseFound.isPresent()) {
			Bases baseUpdt = baseFound.get();
			List<MatPrimaOrdFab> matPrimOrdFab = Arrays.asList(baseUpdt.getMateriaPrimaOrdFab());
			matPrimOrdFab.forEach(mp -> {
				Optional<MateriaPrima> mpf = repomatprima.findByLote(mp.getLote());
				if(mpf.isPresent()) {
					MateriaPrima mpu = mpf.get();
					mpu.setApartado(mpu.getApartado() - mp.getCantidad());
					mpUpdt.add(mpu);
				}
			});
			repomatprima.saveAll(mpUpdt);
			basesrepo.deleteById(id);
			return ResponseEntity.ok(baseFound);
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("Base no encontrada"));
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateBase(@PathVariable String id, @RequestBody Bases base){
		Optional<Bases> bf = basesrepo.findById(id);
		if(bf.isPresent()) {
			Bases baseUpdt = bf.get();
			baseUpdt.setLote(base.getLote());
			baseUpdt.setNombre(base.getNombre());
			baseUpdt.setCantidadOriginal(base.getCantidadOriginal());
			baseUpdt.setCantidadRestante(base.getCantidadRestante());
			baseUpdt.setMateriaPrimaOrdFab(base.getMateriaPrimaOrdFab());
			baseUpdt.setEstatus(base.getEstatus());
			baseUpdt.setComentarios(base.getComentarios());
			baseUpdt.setRendimiento(base.getRendimiento());
			baseUpdt.setAprobado(base.isAprobado());
			return ResponseEntity.ok(basesrepo.save(baseUpdt));
		}else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cause description here");
		}
	}
	
	
}
