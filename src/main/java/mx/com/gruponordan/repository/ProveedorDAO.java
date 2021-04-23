package mx.com.gruponordan.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import mx.com.gruponordan.model.Proveedor;

public interface ProveedorDAO extends MongoRepository<Proveedor, String> {
	List<Proveedor> findByActivoOrderByNombre(boolean activo);
	List<Proveedor> findByNombreLikeOrderByNombre(String nombre);
}
