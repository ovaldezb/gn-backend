package mx.com.gruponordan.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "orden_fabriacion")
public class OrdenFabricacion {
	
	@Id
	private String id;
	private String oc;
	private String nombre;
	private String clave;
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private Date fechaFabricacion;
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private Date fechaEntrega;
	private long noConsecutivo;
	private String lote;
	private int piezas;
	private String observaciones;
	private String cliente;
	private List<MatPrimaOrdFab> matprima;
	private Eestatus estatus;
	
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
	public String getLote() {
		return lote;
	}
	public void setLote(String lote) {
		this.lote = lote;
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
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public List<MatPrimaOrdFab> getMatprima() {
		return matprima;
	}
	public void setMatprima(List<MatPrimaOrdFab> matprima) {
		this.matprima = matprima;
	}
	public Eestatus getEstatus() {
		return estatus;
	}
	public void setEstatus(Eestatus estatus) {
		this.estatus = estatus;
	}

	
	

}
