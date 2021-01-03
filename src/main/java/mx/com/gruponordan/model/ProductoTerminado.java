package mx.com.gruponordan.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "producto_terminado")
public class ProductoTerminado {
	
	@Id
	private String id;
	private String nombre;
	private String clave;
	private double piezas;
	private String lote;
	private String cliente;
	private String oc;
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private Date fehaFabricacion;
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private Date fechaEntrega;
	@DBRef
	private Estatus estatus;
	private String comentario;
	
	public ProductoTerminado(String nombre, String clave, double piezas, String lote, String cliente, String oc,
		Date fehaFabricacion, Date fechaEntrega, Estatus estatus) {
		super();
		this.nombre = nombre;
		this.clave = clave;
		this.piezas = piezas;
		this.lote = lote;
		this.cliente = cliente;
		this.oc = oc;
		this.fehaFabricacion = fehaFabricacion;
		this.fechaEntrega = fechaEntrega;
		this.estatus = estatus;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public double getPiezas() {
		return piezas;
	}
	public void setPiezas(double piezas) {
		this.piezas = piezas;
	}
	public String getLote() {
		return lote;
	}
	public void setLote(String lote) {
		this.lote = lote;
	}
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public String getOc() {
		return oc;
	}
	public void setOc(String oc) {
		this.oc = oc;
	}
	public Date getFehaFabricacion() {
		return fehaFabricacion;
	}
	public void setFehaFabricacion(Date fehaFabricacion) {
		this.fehaFabricacion = fehaFabricacion;
	}
	public Date getFechaEntrega() {
		return fechaEntrega;
	}
	public void setFechaEntrega(Date fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}
	
	public Estatus getEstatus() {
		return estatus;
	}
	public void setEstatus(Estatus estatus) {
		this.estatus = estatus;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	
	@Override
	public String toString() {
		return "ProductoTerminado [id=" + id + ", nombre=" + nombre + ", clave=" + clave + ", piezas=" + piezas
				+ ", lote=" + lote + ", cliente=" + cliente + ", oc=" + oc + ", fehaFabricacion=" + fehaFabricacion
				+ ", fechaEntrega=" + fechaEntrega + ", estatus=" + estatus + ", comentario=" + comentario + "]";
	}
	
	

}
