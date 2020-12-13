package mx.com.gruponordan.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "materiaprima")
public class MateriaPrima {
	
	@Id
	private String id;
	private String descripcion;
	private int piezas;
	private String codigo;
	private String proveedor;
	private Date fechaEtrada;
	private Date fechaCaducidad;
	private String observaciones;
	private String lote;
	//private long abundante; // >300
	private long necesario; //101-300
	private long escaso; // 0-100
	
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
	public int getPiezas() {
		return piezas;
	}
	public void setPiezas(int piezas) {
		this.piezas = piezas;
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
	public Date getFechaEtrada() {
		return fechaEtrada;
	}
	public void setFechaEtrada(Date fechaEtrada) {
		this.fechaEtrada = fechaEtrada;
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
	@Override
	public String toString() {
		return "MateriaPrima [id=" + id + ", descripcion=" + descripcion + ", piezas=" + piezas + ", codigo=" + codigo
				+ ", proveedor=" + proveedor + ", fechaEtrada=" + fechaEtrada + ", fechaCaducidad=" + fechaCaducidad
				+ ", observaciones=" + observaciones + "]";
	}
	
	
	

}
