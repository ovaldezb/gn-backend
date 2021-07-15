package mx.com.gruponordan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import mx.com.gruponordan.model.ProductoDisponible;

public interface ProdDispDAO extends MongoRepository<ProductoDisponible, String> {
	List<ProductoDisponible> findByClave(String clave);
	Optional<ProductoDisponible> findByClaveAndTipoProducto(String clave, String tipoProducto);
	Optional<ProductoDisponible> findByClaveAndFormula(String clave, String formula);
}
