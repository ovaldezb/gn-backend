package mx.com.gruponordan.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "materiaprima")
public class MateriaPrima {
	
	@Id
	private String id;
	private String descripcion;
	private int cantidad;
	@DBRef
	private UnidadMedida unidad;
	private String codigo;
	private String proveedor;
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private Date fechaEntrada;
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private Date fechaCaducidad;
	private String observaciones;
	private String lote;
	//private long abundante; // >300
	private long necesario; //101-300
	private long escaso; // 0-100
	private boolean activo;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
	public UnidadMedida getUnidad() {
		return unidad;
	}
	public void setUnidad(UnidadMedida unidad) {
		this.unidad = unidad;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getProveedor() {
		return proveedor;
	}
	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}
	public Date getFechaEntrada() {
		return fechaEntrada;
	}
	public void setFechaEntrada(Date fechaEntrada) {
		this.fechaEntrada = fechaEntrada;
	}
	public Date getFechaCaducidad() {
		return fechaCaducidad;
	}
	public void setFechaCaducidad(Date fechaCaducidad) {
		this.fechaCaducidad = fechaCaducidad;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public String getLote() {
		return lote;
	}
	public void setLote(String lote) {
		this.lote = lote;
	}
	public long getNecesario() {
		return necesario;
	}
	public void setNecesario(long necesario) {
		this.necesario = necesario;
	}
	public long getEscaso() {
		return escaso;
	}
	public void setEscaso(long escaso) {
		this.escaso = escaso;
	}
	public boolean isActivo() {
		return activo;
	}
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	

}
