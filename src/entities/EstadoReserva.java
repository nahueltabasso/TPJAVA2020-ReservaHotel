package entities;

import java.util.Date;

public class EstadoReserva {

	public static final String RESERVADA = "Reservada";
	public static final String CANCELADA = "Cancelada";
	public static final String ANULADA = "Anulada";
	public static final String ACTIVA = "Activa";
	
	private Long id;
	private String descripcion;
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
