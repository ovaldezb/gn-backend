package mx.com.gruponordan.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "orden_compra")
public class OrdenCompra {

	@Id
	private String id;
	private String oc;
	@DBRef
	private ProductoDisponible producto;
	private String clave;
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private Date fechaFabricacion;
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private Date fechaEntrega;
	//Piezas totales
	private int piezas;
	private String observaciones;
	//Este va a cambiar a un ID de cliente
	@DBRef
	private Cliente cliente;
	private Eestatus estatus;
	private double presentacion;
	//piezas pendientes de fabricar
	private double piezasFabricadas;
	//piezas que ya se han fabriacado
	private double piezasCompletadas;
	//Piezas que se han entregado;
	private double piezasEntregadas;
	private String tipoPresentacion;
	private String lote;
	private boolean aprobado;
	
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
	public int getPiezas() {
		return piezas;
	}
	public void setPiezas(int piezas) {
		this.piezas = piezas;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public Eestatus getEstatus() {
		return estatus;
	}
	public void setEstatus(Eestatus estatus) {
		this.estatus = estatus;
	}
	public double getPresentacion() {
		return presentacion;
	}
	public void setPresentacion(double presentacion) {
		this.presentacion = presentacion;
	}
	public double getPiezasFabricadas() {
		return piezasFabricadas;
	}
	public void setPiezasFabricadas(double piezasFabricadas) {
		this.piezasFabricadas = piezasFabricadas;
	}
	public double getPiezasCompletadas() {
		return piezasCompletadas;
	}
	public void setPiezasCompletadas(double piezasCompletadas) {
		this.piezasCompletadas = piezasCompletadas;
	}
	public double getPiezasEntregadas() {
		return piezasEntregadas;
	}
	public void setPiezasEntregadas(double piezasEntregadas) {
		this.piezasEntregadas = piezasEntregadas;
	}
	public String getTipoPresentacion() {
		return tipoPresentacion;
	}
	public void setTipoPresentacion(String tipoPresentacion) {
		this.tipoPresentacion = tipoPresentacion;
	}
	public String getLote() {
		return lote;
	}
	public void setLote(String lote) {
		this.lote = lote;
	}
	public boolean isAprobado() {
		return aprobado;
	}
	public void setAprobado(boolean aprobado) {
		this.aprobado = aprobado;
	}
	
}
