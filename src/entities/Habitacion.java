package entities;

import java.util.Date;

public class Habitacion {
	
	private Long id;
	private Integer numeroHabitacion;
	private Date fechaCreacion;
	private Date fechaEliminacion;
	private TipoHabitacion tipoHabitacion;
	private boolean seleccionada;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getNumeroHabitacion() {
		return numeroHabitacion;
	}
	public void setNumeroHabitacion(Integer numeroHabitacion) {
		this.numeroHabitacion = numeroHabitacion;
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
	public TipoHabitacion getTipoHabitacion() {
		return tipoHabitacion;
	}
	public void setTipoHabitacion(TipoHabitacion tipoHabitacion) {
		this.tipoHabitacion = tipoHabitacion;
	}
	public boolean isSeleccionada() {
		return seleccionada;
	}
	public void setSeleccionada(boolean seleccionada) {
		this.seleccionada = seleccionada;
	}
	
}
