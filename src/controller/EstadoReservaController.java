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
}
