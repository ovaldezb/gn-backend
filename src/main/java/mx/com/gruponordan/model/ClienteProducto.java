package mx.com.gruponordan.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="cliente_producto")
public class ClienteProducto {
	
	@Id
	private String id;
	private String cliente;
	private String idCliente;
	private String producto;
	private String idProducto;
	private String clave;
	
	
	public ClienteProducto(String cliente, String idCliente, String producto, String idProducto, String clave) {
		super();
		this.cliente = cliente;
		this.idCliente = idCliente;
		this.producto = producto;
		this.idProducto = idProducto;
		this.clave = clave;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public String getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}
	public String getProducto() {
		return producto;
	}
	public void setProducto(String producto) {
		this.producto = producto;
	}
	public String getIdProducto() {
		return idProducto;
	}
	public void setIdProducto(String idProducto) {
		this.idProducto = idProducto;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}

}
