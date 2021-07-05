package mx.com.gruponordan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import mx.com.gruponordan.model.Bases;

public interface BasesDAO extends MongoRepository<Bases, String> {
	Optional<Bases> findByLote(String lote);
	List<Bases> findByEstatusNotLike(String estatus);
	@Query("{codigo : ?0, cantidadRestante : {$gt : ?1}}")
	List<Bases> findByCodigoAndCantidadRestanteMoreThan(String codigo, double cantidadRestante);
}
