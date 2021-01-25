package mx.com.gruponordan.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import mx.com.gruponordan.model.MateriaPrima;


public interface MateriaPrimaDAO extends MongoRepository<MateriaPrima, String> {
	@Query("{codigo : ?0, cantidad : {$gt : ?1}}")
	List<MateriaPrima> findByCodigoAndCantidadMoreThan(String codigo,double cantidad);
	@Query("{cantidad : {$gt : ?0}}")
	List<MateriaPrima> findMateriasPrimasGtCantidad(double cantidad);
	MateriaPrima findByLote(String lote);
}
