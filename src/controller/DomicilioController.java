package controller;

import java.util.Date;

import data.DomicilioRepository;
import entities.Domicilio;

public class DomicilioController {

	private DomicilioRepository domicilioRepository = new DomicilioRepository();
	
	public Domicilio registrarDomicilio(Domicilio domicilio) throws Exception {
		domicilio.setFechaCreacion(new Date());
		return domicilioRepository.save(domicilio);
	}
}
