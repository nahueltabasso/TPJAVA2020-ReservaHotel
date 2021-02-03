package entities;

import java.util.Date;

import java.sql.Blob;

public class TipoHabitacion {
	
	public static final Long SIMPLE = 1L;
	
	private Long id;
	private String descripcion;
	private Integer capacidad;
	
	//FIXME Ver si está bien 
	private Blob foto;
	
	private String denominacion;
	private Double precioPorDia;
	private Date fechaCreacion;
	private Date fechaEliminacion;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Integer getCapacidad() {
		return capacidad;
	}
	public void setCapacidad(Integer capacidad) {
		this.capacidad = capacidad;
	}
	public Blob getFoto() {
		return foto;
	}
	public void setFoto(Blob foto) {
		this.foto = foto;
	}
	public String getDenominacion() {
		return denominacion;
	}
	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}
	public Double getPrecioPorDia() {
		return precioPorDia;
	}
	public void setPrecioPorDia(Double precioPorDia) {
		this.precioPorDia = precioPorDia;
	}
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public Date getFechaEliminacion() {
		return fechaEliminacion;
	}
	public void setFechaEliminacion(Date fechaEliminacion) {
		this.fechaEliminacion = fechaEliminacion;
	}
	
	

}
