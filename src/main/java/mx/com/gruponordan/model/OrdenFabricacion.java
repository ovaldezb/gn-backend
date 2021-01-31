package mx.com.gruponordan.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "orden_fabricacion")
public class OrdenFabricacion {
	
	@Id
	private String id;
	private String oc;
	private long noConsecutivo;
	private String lote;
	private int piezas;
	private String observaciones;
	private List<MatPrimaOrdFab> matprima;
	private Eestatus estatus;
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date fechaFin;
	
	
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
	@Override
	public String toString() {
		return "OrdenFabricacion [id=" + id + ", oc=" + oc + ", noConsecutivo=" + noConsecutivo + ", lote=" + lote
				+ ", piezas=" + piezas + ", observaciones=" + observaciones + ", matprima=" + matprima + ", estatus="
				+ estatus + "]";
	}
	
	
	
}
