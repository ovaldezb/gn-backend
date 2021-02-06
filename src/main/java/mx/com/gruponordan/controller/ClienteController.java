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

import mx.com.gruponordan.model.Cliente;
import mx.com.gruponordan.model.MessageResponse;
import mx.com.gruponordan.repository.ClienteDAO;

@RestController
@RequestMapping("/api/cliente")
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "*")
public class ClienteController {

	@Autowired
	ClienteDAO repocliente;
	
	@GetMapping
	public ResponseEntity<?> getAllClientes(){
		return ResponseEntity.ok(repocliente.findByActivo(true));
	}
	
	@GetMapping("/{nombre}")
	public ResponseEntity<?> getClienteNombre(@PathVariable String nombre){
		if(nombre.trim().equals("vacio")) {
			return ResponseEntity.ok(repocliente.findByActivo(true));
		}else {
			return ResponseEntity.ok(repocliente.findByNombreLike(nombre));
		}	
	}
	
	@PostMapping
	public ResponseEntity<?> saveCliente(@RequestBody Cliente cliente){
		return ResponseEntity.ok(repocliente.save(cliente));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateCliente(@PathVariable String id, @RequestBody Cliente cliente){
		Optional<Cliente> cltFound = repocliente.findById(id);
		if(cltFound.isPresent()) {
			Cliente cliupdt = cltFound.get();
			cliupdt.setEmail(cliente.getEmail());
			cliupdt.setNombre(cliente.getNombre());
			cliupdt.setRfc(cliente.getRfc());
			cliupdt.setTelefono(cliente.getTelefono());
			cliupdt.setContacto(cliente.getContacto());
			cliupdt.setDireccion(cliente.getDireccion());
			return ResponseEntity.ok(repocliente.save(cliupdt));
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("Error"));
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCliente(@PathVariable String id){
		Optional<Cliente> cltFound = repocliente.findById(id);
		if(cltFound.isPresent()) {
			Cliente cliupdt = cltFound.get();
			cliupdt.setActivo(false);
			return ResponseEntity.ok(repocliente.save(cliupdt));
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("Error"));
		}
	}
	
}
