package controller;

import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import data.EstadoReservaRepository;
import entities.EstadoReserva;

public class EstadoReservaController {

	private Logger logger = LogManager.getLogger(getClass());
	private EstadoReservaRepository estadoReservaRepository = new EstadoReservaRepository();
	
	public List<EstadoReserva> getAll() throws Exception {
		logger.log(Level.INFO, "Ingresa a getAll()");
		return estadoReservaRepository.findAll();
	}
	
	public EstadoReserva getByDescripcion(String descripcion) throws Exception {
		logger.log(Level.INFO, "Ingresa a getByDescripcion()");
		if (!descripcion.equalsIgnoreCase(EstadoReserva.ACTIVA) && !descripcion.equalsIgnoreCase(EstadoReserva.RESERVADA) &&
			!descripcion.equalsIgnoreCase(EstadoReserva.ANULADA) && !descripcion.equalsIgnoreCase(EstadoReserva.CANCELADA)) {
			throw new Exception("Descripcion no valida!");
		}
		return estadoReservaRepository.findByDescripcion(descripcion);
	}
}
