package mx.com.gruponordan.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import mx.com.gruponordan.model.Cliente;

public interface ClienteDAO extends MongoRepository<Cliente, String> {
	List<Cliente> findByActivoOrderByNombre(boolean activo);
	@Query("{'nombre': {$regex: ?0 }})")
	List<Cliente> findByNombreLike(String nombre);
}
