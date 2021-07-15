package mx.com.gruponordan.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "orden_fabricacion")
public class OrdenFabricacion {
	
	@Id
	private String id;
	@DBRef
	private OrdenCompra oc;
	private long noConsecutivo;
	private String lote;
	private int piezas;
	private String observaciones;
	private List<MatPrimaOrdFab> matprima;
	private Eestatus estatus;
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date fechaFin;
	
	@JsonFormat
	(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private Date fechaCreacion;
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public OrdenCompra getOc() {
		return oc;
	}
	public void setOc(OrdenCompra oc) {
		this.oc = oc;
	}
	public long getNoConsecutivo() {
		return noConsecutivo;
	}
	public void setNoConsecutivo(long noConsecutivo) {
		this.noConsecutivo = noConsecutivo;
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
	public List<MatPrimaOrdFab> getMatprima() {
		return matprima;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
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
	public String getLote() {
		return lote;
	}
	public void setLote(String lote) {
		this.lote = lote;
	}
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	@Override
	public String toString() {
		return "OrdenFabricacion [id=" + id + ", oc=" + oc + ", noConsecutivo=" + noConsecutivo 
				+ ", piezas=" + piezas + ", observaciones=" + observaciones + ", matprima=" + matprima + ", estatus="
				+ estatus + "]";
	}
	
	
	
}
