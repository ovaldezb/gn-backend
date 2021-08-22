package mx.com.gruponordan.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
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
import mx.com.gruponordan.model.Bases;
import mx.com.gruponordan.model.Eestatus;
import mx.com.gruponordan.model.Lote;
import mx.com.gruponordan.model.MatPrimaOrdFab;
import mx.com.gruponordan.model.MateriaPrima;
import mx.com.gruponordan.model.MessageResponse;
import mx.com.gruponordan.model.OrdenCompra;
import mx.com.gruponordan.repository.BasesDAO;
import mx.com.gruponordan.repository.LoteDAO;
import mx.com.gruponordan.repository.MateriaPrimaDAO;
import mx.com.gruponordan.repository.OrdenCompraDAO;

@RestController
@RequestMapping("/api/lote")
@CrossOrigin(origins = "*")
public class LoteController implements Definitions{
	
	@Autowired
	LoteDAO lotesrepo;
	
	@Autowired
	MateriaPrimaDAO repomatprima;
	
	@Autowired
	OrdenCompraDAO ocrepo;
	
	@Autowired
	BasesDAO baserepo;
	
	@GetMapping("/{tipo}")
	public ResponseEntity<?> getAllLotes(@PathVariable final String tipo){
		boolean buscaTodos = Boolean.parseBoolean(tipo);
		if(buscaTodos) {
			return ResponseEntity.ok(lotesrepo.findAll());
		}else {
			return ResponseEntity.ok(lotesrepo.findByEstatusNotLike(Eestatus.CMPLT));
		}
		
	}
	
	@GetMapping("/val/{lote}")
	public ResponseEntity<?> getLotesByType(@PathVariable final String lote){
		if(lote.equals(VACIO)) {
			List<Lote> lotes = lotesrepo.findByEstatusNotLike(Eestatus.CMPLT);
			return ResponseEntity.ok(lotes.removeIf(oc -> oc.getEstatus().equals(Eestatus.CANCEL)));
		}else {
			List<Lote> lista = lotesrepo.findByLoteLikeAndAprobado(lote, true);
			if(!lista.isEmpty()) {
				return ResponseEntity.ok(lista);
			}else {
				return ResponseEntity.badRequest().body(new MessageResponse("error"));
			}

		}
	}
	
	@GetMapping("/existe/{lote}")
	public ResponseEntity<?> existeLote(@PathVariable final String lote){
		return ResponseEntity.ok(lotesrepo.findByLote(lote));
	}
	
	@PostMapping
	public ResponseEntity<?> saveLote(@RequestBody final Lote lote){
		List<MatPrimaOrdFab> matPrimOrdFab = Arrays.asList(lote.getMateriaprima());
		List<MateriaPrima> mpUpdt = new ArrayList<MateriaPrima>();
		List<Bases> basesUpdt = new ArrayList<Bases>();
		try {
			matPrimOrdFab.stream().filter(mp->!mp.getCodigo().equals(AGUA)).forEach(mpof -> {
				if(mpof.getTipo() == null) {
					Optional<MateriaPrima> mpf = repomatprima.findByLote(mpof.getLote());
					if(mpf.isPresent()) {
						MateriaPrima mpu = mpf.get();
						mpu.setApartado(mpu.getApartado() + mpof.getCantidad());
						mpUpdt.add(mpu);
					}
				}else if(mpof.getTipo().equals(BASE)) {
					Optional<Bases> base = baserepo.findByLote(mpof.getLote());
					if(base.isPresent()) {
						Bases baseUpdt = base.get();
						baseUpdt.setApartado(baseUpdt.getApartado() + mpof.getCantidad());
						basesUpdt.add(baseUpdt);
					}
				}
			});
			if(!mpUpdt.isEmpty()) {
				repomatprima.saveAll(mpUpdt);
			}
			if(!basesUpdt.isEmpty()) {
				baserepo.saveAll(basesUpdt);
			}
			Optional<OrdenCompra> ocF = ocrepo.findById(lote.getOc().getId());
			if(ocF.isPresent()) {
				OrdenCompra ocU = ocF.get();
				ocU.setPiezasLote(ocU.getPiezasLote() + lote.getPiezasLote());
				ocrepo.save(ocU);
			}
			return ResponseEntity.ok(lotesrepo.save(lote));
		}catch(IncorrectResultSizeDataAccessException excp) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Llave duplicada "+excp.getMessage()));
		}
	}
	
	@PutMapping("/{idlote}")
	public ResponseEntity<?> updateLote(@PathVariable final String idlote, @RequestBody final Lote lote){
		Optional<Lote> loteF = lotesrepo.findById(idlote);
		if(loteF.isPresent()) {
			Lote loteU = loteF.get();
			loteU.setLote(lote.getLote());
			loteU.setPiezasLote(lote.getPiezasLote());
			loteU.setAprobado(lote.isAprobado());
			loteU.setEstatus(lote.getEstatus());
			return ResponseEntity.ok(lotesrepo.save(loteU));
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("Error"));
		}
	}
	
	@DeleteMapping("/{idlote}")
	public ResponseEntity<?> deleteLote(@PathVariable final String idlote){
		Optional<Lote> loteF = lotesrepo.findById(idlote);
		if(loteF.isPresent()) {
			Lote lote = loteF.get();
			List<MatPrimaOrdFab> matPrimOrdFab = Arrays.asList(lote.getMateriaprima());
			List<MateriaPrima> mpUpdt = new ArrayList<MateriaPrima>();
			matPrimOrdFab.stream().filter(mp->!mp.getCodigo().equals(AGUA)).forEach(mpof -> {
				Optional<MateriaPrima> mpf = repomatprima.findByLote(mpof.getLote());
				if(mpf.isPresent()) {
					MateriaPrima mpu = mpf.get();
					mpu.setApartado(mpu.getApartado() - mpof.getCantidad());
					mpUpdt.add(mpu);
				}
			});
			repomatprima.saveAll(mpUpdt);
			Optional<OrdenCompra> ocF = ocrepo.findById(lote.getOc().getId());
			if(ocF.isPresent()) {
				OrdenCompra ocU = ocF.get();
				ocU.setPiezasLote(ocU.getPiezasLote() - lote.getPiezasLote());
				ocrepo.save(ocU);
			}
			lotesrepo.deleteById(idlote);
			return ResponseEntity.ok().body(new MessageResponse("OK"));
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("OK"));
		}
	}
	
}
