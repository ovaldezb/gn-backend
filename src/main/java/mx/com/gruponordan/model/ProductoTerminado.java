package mx.com.gruponordan.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "producto_terminado")
public class ProductoTerminado {

	@Id
	private String id;
	@DBRef
	private Estatus estatus;
	private ProductoDisponible producto;
	private String clave;
	private double piezas;
	private String lote;
	@DBRef
	private Cliente cliente;
	private String oc;
	private String comentario;
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date fechaFabricacion;
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date fechaEntrega;
	private long noConsecutivo;
	private String noRemision;
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private Date fechaRemision;
	private double piezasEntregadas;
	@Transient
	private String tipoEntrega;
	@Transient
	private int idDireccion;
	
	public ProductoTerminado(Estatus estatus,ProductoDisponible producto, String oc, String lote, 
			double piezas, Date fechaFabricacion, Date fechaEntrega, 
			long noConsecutivo, Cliente cliente, String clave ) {
		super();
		this.estatus = estatus;
		this.producto = producto;
		this.oc = oc;
		this.lote = lote;
		this.piezas = piezas;
		this.fechaFabricacion = fechaFabricacion;
		this.fechaEntrega = fechaEntrega;
		this.noConsecutivo = noConsecutivo;
		this.cliente = cliente;
		this.clave = clave;
		//this.enviado = false;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Estatus getEstatus() {
		return estatus;
	}
	public void setEstatus(Estatus estatus) {
		this.estatus = estatus;
	}
	public ProductoDisponible getProducto() {
		return producto;
	}
	public void setProducto(ProductoDisponible producto) {
		this.producto = producto;
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
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public String getOc() {
		return oc;
	}
	public void setOc(String oc) {
		this.oc = oc;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public Date getFechaFabricacion() {
		return fechaFabricacion;
	}
	public void setFechaFabricacion(Date fechaFabricacion) {
		this.fechaFabricacion = fechaFabricacion;
	}
	public Date getFechaEntrega() {
		return fechaEntrega;
	}
	public void setFechaEntrega(Date fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}
	public long getNoConsecutivo() {
		return noConsecutivo;
	}
	public void setNoConsecutivo(long noConsecutivo) {
		this.noConsecutivo = noConsecutivo;
	}
	public String getNoRemision() {
		return noRemision;
	}
	public void setNoRemision(String noRemision) {
		this.noRemision = noRemision;
	}
	public Date getFechaRemision() {
		return fechaRemision;
	}
	public void setFechaRemision(Date fechaRemision) {
		this.fechaRemision = fechaRemision;
	}
	public double getPiezasEntregadas() {
		return piezasEntregadas;
	}
	public void setPiezasEntregadas(double piezasEntregadas) {
		this.piezasEntregadas = piezasEntregadas;
	}
	public String getTipoEntrega() {
		return tipoEntrega;
	}
	public void setTipoEntrega(String tipoEntrega) {
		this.tipoEntrega = tipoEntrega;
	}
	public int getIdDireccion() {
		return idDireccion;
	}
	public void setIdDireccion(int idDireccion) {
		this.idDireccion = idDireccion;
	}
	@Override
	public String toString() {
		return "ProductoTerminado [id=" + id + ", estatus=" + estatus + ", producto=" + producto + ", clave=" + clave
				+ ", piezas=" + piezas + ", lote=" + lote + ", cliente=" + cliente + ", oc=" + oc + ", comentario="
				+ comentario + ", fechaFabricacion=" + fechaFabricacion + ", fechaEntrega=" + fechaEntrega
				+ ", noConsecutivo=" + noConsecutivo + ", noRemision=" + noRemision + ", fechaRemision=" + fechaRemision
				+ "]";
	}
	
	
	
}
