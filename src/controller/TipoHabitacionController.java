package controller;

import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import data.TipoHabitacionRepository;
import entities.TipoHabitacion;
import utils.Utils;

public class TipoHabitacionController {

	private Logger logger = LogManager.getLogger(getClass());
	private TipoHabitacionRepository tipoHabitacionRepository = new TipoHabitacionRepository();
	
	public TipoHabitacion registrarTipoHabitacion(TipoHabitacion tipoHabitacion) throws Exception {
		logger.log(Level.INFO, "Ingresa a registrarTipoHabitacion()");
		preValidation(tipoHabitacion, true);
		validTipoHabitacionBeforeSave(tipoHabitacion);
		TipoHabitacion tipoHabitacionDB = tipoHabitacionRepository.save(tipoHabitacion);
		return tipoHabitacionDB;
	}
	
	public List<TipoHabitacion> getAll() throws Exception {
		logger.log(Level.INFO, "Ingresa a getAll()");
		return tipoHabitacionRepository.findAll();
	}
	
	public TipoHabitacion getById(Long id) throws Exception {
		logger.log(Level.INFO, "Ingresa a getById()");
		TipoHabitacion tipoHabitacionDb = tipoHabitacionRepository.findById(id);
		if (tipoHabitacionDb == null) {
			throw new Exception("No existe la habitación en la base de datos!");
		}
		return tipoHabitacionDb;
	}
		
	public void delete(Long id) throws Exception {
		logger.log(Level.INFO, "Ingresa a delete()");
		TipoHabitacion tipoHabitacionDB = tipoHabitacionRepository.findById(id);
		if (tipoHabitacionDB.getId() == null) {
			throw new Exception("ERROR - No existe el tipo de habitación en la base de datos!");
		}
		tipoHabitacionRepository.delete(id);
	}
	
	public TipoHabitacion actualizarTipoHabitacion(Long id, TipoHabitacion tipoHabitacion) throws Exception {
		logger.log(Level.INFO, "Ingresa a actualizarTipoHabitacion()");
		preValidation(tipoHabitacion, false);
		return actualizar(id, tipoHabitacion);
	}
	
	private void preValidation(TipoHabitacion tipoHabitacion, boolean isCreate) throws Exception {
		logger.log(Level.INFO, "Ingresa a preValidation()");
		boolean valid;
		if (isCreate) {
			valid = tipoHabitacionRepository.existTipoHabitacion(tipoHabitacion);
			if (valid) {
				throw new Exception ("El tipo de habitación ya existe en la base de datos");
			}		
		}
	}
		
	private void validTipoHabitacionBeforeSave(TipoHabitacion tipoHabitacion) throws Exception {
		logger.log(Level.INFO, "Ingresa a validTipoHabitacionBeforeSave()");

				
		// Validamos que el precio no tenga letras
		if (Utils.cadContainsLetters(tipoHabitacion.getPrecioPorDia().toString())) {
			throw new Exception("Precio no válido! Ingrese solamente dígitos.");
		}
		
		// Validamos que el precio no tenga letras
		if (Utils.cadContainsLetters(tipoHabitacion.getCapacidad().toString())) {
			throw new Exception("Capacidad no válida! Ingrese solamente dígitos.");
		}
		
		tipoHabitacion.setFechaCreacion(new Date());
		tipoHabitacion.setFechaEliminacion(null);
	}
	
	// faltan validaciones
	private TipoHabitacion actualizar(Long id, TipoHabitacion tipoHabitacion) throws Exception {
		logger.log(Level.INFO, "Ingresa a actualizar()");

		// Obtenemos la habitación de la BD por su id
		TipoHabitacion tipoHabitacionDb = tipoHabitacionRepository.findById(id);
		
		if (tipoHabitacionDb == null) {
			throw new Exception("No existe el tipo de habitación en la base de datos!");
		}
				
				
		return tipoHabitacionRepository.update(tipoHabitacion);
	}
}
