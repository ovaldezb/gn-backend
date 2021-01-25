package mx.com.gruponordan.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import mx.com.gruponordan.model.Estatus;
import mx.com.gruponordan.model.ProductoTerminado;


public interface ProductoTerminadoDAO extends MongoRepository<ProductoTerminado, String>{
	public ProductoTerminado findByClave(String clave);
	public List<ProductoTerminado> findByEstatus(Estatus estatus);
}
