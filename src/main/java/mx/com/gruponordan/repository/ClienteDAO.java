package mx.com.gruponordan.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import mx.com.gruponordan.model.Cliente;

public interface ClienteDAO extends MongoRepository<Cliente, String> {
	List<Cliente> findByActivo(boolean activo);
}
