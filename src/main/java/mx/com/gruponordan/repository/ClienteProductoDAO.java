package mx.com.gruponordan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import mx.com.gruponordan.model.ClienteProducto;

public interface ClienteProductoDAO extends MongoRepository<ClienteProducto, String> {
	public List<ClienteProducto> findByIdCliente(String idCliente);
	public Optional<ClienteProducto> findByIdClienteAndClave(String idCliente, String clave);
}
