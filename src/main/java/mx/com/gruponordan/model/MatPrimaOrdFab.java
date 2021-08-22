package mx.com.gruponordan.model;

/**
 * Este Bean sera ocupado para almacenar las materias primas usadas en una OF*/
public class MatPrimaOrdFab implements Comparable<MatPrimaOrdFab> {

	private String codigo;
	private String nombre;
	private double cantidad;
	private String lote;
	private String estatus;
	private String comentarios;
	private double delta;
	private String tipo;
	
	public MatPrimaOrdFab(String codigo,String nombre, double cantidad, String lote, String estatus, String comentarios, String tipo) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.cantidad = cantidad;
		this.lote = lote;
		this.estatus = estatus;
		this.comentarios = comentarios;
		this.delta = 0.0;
		this.tipo = tipo;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public double getCantidad() {
		return cantidad;
	}
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}
	public String getLote() {
		return lote;
	}
	public void setLote(String lote) {
		this.lote = lote;
	}
	public String getEstatus() {
		return estatus;
	}
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	public String getComentarios() {
		return comentarios;
	}
	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}
	public double getDelta() {
		return delta;
	}
	public void setDelta(double delta) {
		this.delta = delta;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	@Override
	public String toString() {
		return "MatPrimaOrdFab [codigo=" + codigo + ", nombre=" + nombre + ", cantidad=" + cantidad + ", lote=" + lote
				+ ", estatus=" + estatus + ", comentarios=" + comentarios + "]";
	}
	
	@Override
	public int compareTo(MatPrimaOrdFab o) {
		// TODO Auto-generated method stub
		return (this.nombre.compareTo(o.nombre));
	}
	
}
