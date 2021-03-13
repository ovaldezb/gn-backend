package mx.com.gruponordan.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import mx.com.gruponordan.model.ProductoEntregado;

public interface ProductoEntregadoDAO extends MongoRepository<ProductoEntregado, String> {
	public List<ProductoEntregado> findByFechaEntregaBetween(Date fecini, Date fecfin);
	public Optional<ProductoEntregado> findByRemision(String remision);
}
