package mx.com.gruponordan.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import mx.com.gruponordan.model.ProductoTerminado;


public interface ProductoTerminadoDAO extends MongoRepository<ProductoTerminado, String>{
	public ProductoTerminado findByCodigo(String codigo);
}
