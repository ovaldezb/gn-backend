package mx.com.gruponordan.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "producto_entregado")
public class ProductoEntregado {

	@Id
	private String id;
	private String oc;
	private String lote;
	private String cliente;
	private String nombreProducto;
	private double piezasEntregadas;
	private double ordenFabricacion;
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private Date fechaEntrega;
	private String remision;
	private String tipoEntrega;
	private int idDireccion;
	@DBRef
	private Estatus estatus;
	@Transient
	private Eestatus codigoEstatus;
	private String motivoCancel;
	private String idProdTerminado;
	
	public ProductoEntregado(String oc, String lote, String cliente, String nombreProducto, double piezasEntregadas,
			double ordenFabricacion, Date fechaEntrega, String remision, String tipoEntrega, 
			int idDireccion, Estatus estatus, String idProdTerminado) {
		super();
		this.oc = oc;
		this.lote = lote;
		this.cliente = cliente;
		this.nombreProducto = nombreProducto;
		this.piezasEntregadas = piezasEntregadas;
		this.ordenFabricacion = ordenFabricacion;
		this.fechaEntrega = fechaEntrega;
		this.remision = remision;
		this.tipoEntrega = tipoEntrega;
		this.idDireccion = idDireccion;
		this.estatus = estatus;
		this.idProdTerminado = idProdTerminado;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOc() {
		return oc;
	}
	public void setOc(String oc) {
		this.oc = oc;
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
	public String getNombreProducto() {
		return nombreProducto;
	}
	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}
	public double getPiezasEntregadas() {
		return piezasEntregadas;
	}
	public void setPiezasEntregadas(double piezasEntregadas) {
		this.piezasEntregadas = piezasEntregadas;
	}
	public double getOrdenFabricacion() {
		return ordenFabricacion;
	}
	public void setOrdenFabricacion(double ordenFabricacion) {
		this.ordenFabricacion = ordenFabricacion;
	}
	public Date getFechaEntrega() {
		return fechaEntrega;
	}
	public void setFechaEntrega(Date fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}
	public String getRemision() {
		return remision;
	}
	public void setRemision(String remision) {
		this.remision = remision;
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

	public Estatus getEstatus() {
		return estatus;
	}

	public void setEstatus(Estatus estatus) {
		this.estatus = estatus;
	}

	public String getMotivoCancel() {
		return motivoCancel;
	}

	public void setMotivoCancel(String motivoCancel) {
		this.motivoCancel = motivoCancel;
	}

	public String getIdProdTerminado() {
		return idProdTerminado;
	}

	public void setIdProdTerminado(String idProdTerminado) {
		this.idProdTerminado = idProdTerminado;
	}

	public Eestatus getCodigoEstatus() {
		return codigoEstatus;
	}

	public void setCodigoEstatus(Eestatus codigoEstatus) {
		this.codigoEstatus = codigoEstatus;
	}

	@Override
	public String toString() {
		return "ProductoEntregado [id=" + id + ", oc=" + oc + ", lote=" + lote + ", cliente=" + cliente
				+ ", nombreProducto=" + nombreProducto + ", piezasEntregadas=" + piezasEntregadas
				+ ", ordenFabricacion=" + ordenFabricacion + ", fechaEntrega=" + fechaEntrega + ", remision=" + remision
				+ ", tipoEntrega=" + tipoEntrega + ", idDireccion=" + idDireccion + ", estatus=" + estatus
				+ ", codigoEstatus=" + codigoEstatus + ", motivoCancel=" + motivoCancel + ", idProdTerminado="
				+ idProdTerminado + "]";
	}

	
	
}
