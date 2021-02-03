package controller;

import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import data.SalonRepository;
import entities.Salon;
import utils.Utils;

public class SalonController {

	private Logger logger = LogManager.getLogger(getClass());
	private SalonRepository salonRepository = new SalonRepository();
	
	public Salon registrarSalon(Salon salon) throws Exception {
		logger.log(Level.INFO, "Ingresa a registrarSalon()");
		preValidation(salon, true);
		validSalonBeforeSave(salon);
		Salon salonDB = salonRepository.save(salon);
		return salonDB;
	}
	
	public List<Salon> getAll() throws Exception {
		logger.log(Level.INFO, "Ingresa a getAll()");
		return salonRepository.findAll();
	}
	
	public Salon getById(Long id) throws Exception {
		logger.log(Level.INFO, "Ingresa a getById()");
		Salon salonDB = salonRepository.findById(id);
		if (salonDB == null) {
			throw new Exception("No existe el salón en la base de datos!");
		}
		return salonDB;
	}
		
	public void delete(Long id) throws Exception {
		logger.log(Level.INFO, "Ingresa a delete()");
		Salon salonDB = salonRepository.findById(id);
		if (salonDB.getId() == null) {
			throw new Exception("ERROR - No existe el salón en la base de datos!");
		}
		salonRepository.delete(id);
	}
	
	public Salon actualizarSalon(Long id, Salon salon) throws Exception {
		logger.log(Level.INFO, "Ingresa a actualizarSalon()");
		preValidation(salon, false);
		return actualizar(id, salon);
	}
	
	private void preValidation(Salon salon, boolean isCreate) throws Exception {
		logger.log(Level.INFO, "Ingresa a preValidation()");
		boolean valid;
		if (isCreate) {
			valid = salonRepository.existSalon(salon);
			if (valid) {
				throw new Exception ("El salón ya existe en la base de datos");
			}		
		}
	}
		
	private void validSalonBeforeSave(Salon salon) throws Exception {
		logger.log(Level.INFO, "Ingresa a validSalonBeforeSave()");

				
		// Validamos que el atributo precio no tenga letras
		if (Utils.cadContainsLetters(salon.getPrecioPorDia().toString())) {
			throw new Exception("Precio no válido! Ingrese solamente dígitos.");
		}
		
		// Validamos que el atributo capacidad no tenga letras
		if (Utils.cadContainsLetters(salon.getCapacidad().toString())) {
			throw new Exception("Capacidad no válida! Ingrese solamente dígitos.");
		}
		
		salon.setFechaCreacion(new Date());
		salon.setFechaEliminacion(null);
	}
	
	// faltan validaciones
	private Salon actualizar(Long id, Salon salon) throws Exception {
		logger.log(Level.INFO, "Ingresa a actualizar()");

		// Obtenemos salón de la BD por su id
		Salon salonDB = salonRepository.findById(id);
		
		if (salonDB == null) {
			throw new Exception("No existe el salón en la base de datos!");
		}
						
		return salonRepository.update(salon);
	}
	
}
