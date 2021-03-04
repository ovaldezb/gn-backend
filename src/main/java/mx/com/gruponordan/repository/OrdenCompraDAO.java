package mx.com.gruponordan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import mx.com.gruponordan.model.Eestatus;
import mx.com.gruponordan.model.OrdenCompra;

public interface OrdenCompraDAO extends MongoRepository<OrdenCompra, String> {
	public Optional<OrdenCompra> findByOc(String oc);
	//public List<OrdenCompra> findByOcAndAprobado(String oc, boolean aprobado);
	public List<OrdenCompra> findByEstatusNotLikeOrderByFechaEntrega(Eestatus estatus);
}
