package entities;

import java.util.Date;

public class Tarjeta {
	
	private Long id;
	private Long numeroTarjeta;
	private Date fechaVencimiento;
	private String nombreTarjeta;
	private Float monto;
	private Date fechaCreacion;
	private Date fechaEliminacion;
	private Persona persona;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getNumeroTarjeta() {
		return numeroTarjeta;
	}
	public void setNumeroTarjeta(Long numeroTarjeta) {
		this.numeroTarjeta = numeroTarjeta;
	}
	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}
	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	public String getNombreTarjeta() {
		return nombreTarjeta;
	}
	public void setNombreTarjeta(String nombreTarjeta) {
		this.nombreTarjeta = nombreTarjeta;
	}
	public Float getMonto() {
		return monto;
	}
	public void setMonto(Float monto) {
		this.monto = monto;
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
	public Persona getPersona() {
		return persona;
	}
	public void setPersona(Persona persona) {
		this.persona = persona;
	}
	
	

}
