package mx.com.gruponordan.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.com.gruponordan.model.MessageResponse;
import mx.com.gruponordan.model.ProductoEntregado;
import mx.com.gruponordan.repository.ProductoEntregadoDAO;

@RestController
@RequestMapping("/api/prodent")
@CrossOrigin(origins = "*")
public class ProductoEntregadoController {
	
	//Logger logger = LoggerFactory.getLogger(ProductoEntregadoController.class);

	@Autowired
	ProductoEntregadoDAO repoPE;
	
	@GetMapping
	public ResponseEntity<?> getAllProductosEntregados(){
		return ResponseEntity.ok(repoPE.findAll());
	}
	
	@GetMapping("/rem/{remision}")
	public ResponseEntity<?> getProductoByRemision(@PathVariable final String remision){
		Optional<ProductoEntregado> pe = repoPE.findByRemision(remision);
		if(pe.isPresent()) {
			return ResponseEntity.ok(pe.get());
		}else {
			return ResponseEntity.ok().body(new MessageResponse(""));
		}
	}
	
	
	@GetMapping("/{fecini}/{fecfin}")
	public ResponseEntity<?> getReporteByDates(@PathVariable final String fecini, @PathVariable final String fecfin ){
		//DateTimeFormatter df = DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm:ss");
		SimpleDateFormat parser = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
		Date fechaIni = null;
		Date fechaFin = null;
		try {
			fechaIni = parser.parse(fecini);
			fechaFin = parser.parse(fecfin);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok(repoPE.findByFechaEntregaBetween(fechaIni, fechaFin));
		
	}
	
}
