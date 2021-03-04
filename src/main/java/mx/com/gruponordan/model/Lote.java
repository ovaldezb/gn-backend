package mx.com.gruponordan.model;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "lote")
public class Lote {

	private String id;
	private String lote;
	@DBRef
	private OrdenCompra oc;
	private double piezasLote;
	private Eestatus estatus;
	private boolean aprobado;
	private boolean fabricado;
	private MatPrimaOrdFab[] materiaprima;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLote() {
		return lote;
	}
	public void setLote(String lote) {
		this.lote = lote;
	}
	public OrdenCompra getOc() {
		return oc;
	}
	public void setOc(OrdenCompra oc) {
		this.oc = oc;
	}
	public double getPiezasLote() {
		return piezasLote;
	}
	public void setPiezasLote(double piezasLote) {
		this.piezasLote = piezasLote;
	}
	public Eestatus getEstatus() {
		return estatus;
	}
	public void setEstatus(Eestatus estatus) {
		this.estatus = estatus;
	}
	public boolean isAprobado() {
		return aprobado;
	}
	public void setAprobado(boolean aprobado) {
		this.aprobado = aprobado;
	}
	public boolean isFabricado() {
		return fabricado;
	}
	public void setFabricado(boolean fabricado) {
		this.fabricado = fabricado;
	}
	public MatPrimaOrdFab[] getMateriaprima() {
		return materiaprima;
	}
	public void setMateriaprima(MatPrimaOrdFab[] materiaprima) {
		this.materiaprima = materiaprima;
	}
	
}
