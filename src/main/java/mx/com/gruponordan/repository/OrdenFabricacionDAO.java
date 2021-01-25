package mx.com.gruponordan.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import mx.com.gruponordan.model.OrdenFabricacion;

public interface OrdenFabricacionDAO extends MongoRepository<OrdenFabricacion, String> {
	List<OrdenFabricacion> findByEstatus(String estatus);
}
