package mx.com.gruponordan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import mx.com.gruponordan.model.MateriaPrimaDisponible;

public interface MatPrimaDispDAO extends MongoRepository<MateriaPrimaDisponible, String>{
	Optional<MateriaPrimaDisponible> findByCodigo(String codigo);
	List<MateriaPrimaDisponible> findByDescripcionLike(String descripcion);
}
