package entities;

import java.util.Date;

import java.sql.Blob;

public class Salon {
	
	private Long id;
	private String nombreSalon;
	private String descripcion;
	private Integer capacidad;
	private Float precioPorDia;
	
	//FIXME Ver si está bien
	private Blob foto;
	
	private Date fechaCreacion;
	private Date fechaEliminacion;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombreSalon() {
		return nombreSalon;
	}
	public void setNombreSalon(String nombreSalon) {
		this.nombreSalon = nombreSalon;
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
	public Float getPrecioPorDia() {
		return precioPorDia;
	}
	public void setPrecioPorDia(Float precioPorDia) {
		this.precioPorDia = precioPorDia;
	}
	public Blob getFoto() {
		return foto;
	}
	public void setFoto(Blob foto) {
		this.foto = foto;
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
