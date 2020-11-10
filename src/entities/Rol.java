package entities;

import java.io.Serializable;
import java.util.Date;

public class Rol implements Serializable {

	public static final String ADMINISTRADOR = "Administrador";
	public static final String EMPLEADO = "Empleado";
	public static final String CLIENTE = "Cliente";
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private String nombreRol;
	private Date fechaCreacion;
	private Date fechaEliminacion;
	
	public Rol() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombreRol() {
		return nombreRol;
	}

	public void setNombreRol(String nombreRol) {
		this.nombreRol = nombreRol;
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
