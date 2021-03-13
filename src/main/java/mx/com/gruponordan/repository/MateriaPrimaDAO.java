package mx.com.gruponordan.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import mx.com.gruponordan.model.MateriaPrima;


public interface MateriaPrimaDAO extends MongoRepository<MateriaPrima, String> {
	@Query("{codigo : ?0, cantidad : {$gt : ?1}}")
	List<MateriaPrima> findByCodigoAndCantidadMoreThanOrderByFechaCaducidad(String codigo,double cantidad);
	@Query("{cantidad : {$gt : ?0}}")
	List<MateriaPrima> findMateriasPrimasGtCantidad(double cantidad, Sort sort);
	MateriaPrima findByLote(String lote);
	List<MateriaPrima> findByCodigo(String codigo);
	@Query("{fechaCaducidad: {$lt:?0}}")
	List<MateriaPrima> findByFechaCaducidadLt(Date date);
	@Query("{fechaCaducidad: {$gt:?0}}")
	List<MateriaPrima> findByFechaCaducidadGt(Date date);	
	List<MateriaPrima> findByFechaCaducidadBetweenOrderByFechaCaducidadAsc(Date fechaIni, Date fechaFin);
	@Query("{cantidad: {$lte:?0}}")
	void deleteByCantidad(double cantidad);
}
