package controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import data.HabitacionRepository;
import data.TipoHabitacionRepository;
import entities.Habitacion;
import entities.TipoHabitacion;
import utils.Utils;

public class HabitacionController {

	private Logger logger = LogManager.getLogger(getClass());
	private HabitacionRepository habitacionRepository = new HabitacionRepository();
	private TipoHabitacionRepository tipoHabitacionRepository = new TipoHabitacionRepository();
	
	public Habitacion registrarHabitacion(Habitacion habitacion) throws Exception {
		logger.log(Level.INFO, "Ingresa a registrarHabitacion()");
		preValidation(habitacion, true);
		validHabitacionBeforeSave(habitacion);
		Habitacion habitacionDB = habitacionRepository.save(habitacion);
		return habitacionDB;
	}
	
	public List<Habitacion> getAll() throws Exception {
		logger.log(Level.INFO, "Ingresa a getAll()");
		return habitacionRepository.findAll();
	}
	
	public Habitacion getById(Long id) throws Exception {
		logger.log(Level.INFO, "Ingresa a getById()");
		Habitacion habitacionDb = habitacionRepository.findById(id);
		if (habitacionDb == null) {
			throw new Exception("No existe la habitación en la base de datos!");
		}
		return habitacionDb;
	}
	
	public List<Habitacion> getByTipoHabitacion(TipoHabitacion tipoHabitacion) throws Exception {
		logger.log(Level.INFO, "Ingresa a getByTipoHabitacion()");
		return habitacionRepository.findHabitacionesByIdTipoHabitacion(tipoHabitacion.getId());
	}
	
	public void delete(Long id) throws Exception {
		logger.log(Level.INFO, "Ingresa a delete()");
		Habitacion habitacionDB = habitacionRepository.findById(id);
		//System.out.println(habitacionDB.getId());
		if (habitacionDB.getId() == null) {
			throw new Exception("ERROR - No existe la habitación en la base de datos!");
		}
		habitacionRepository.delete(id);
	}
	
	public Habitacion actualizarHabitacion(Long id, Habitacion habitacion) throws Exception {
		logger.log(Level.INFO, "Ingresa a actualizarHabitacion()");
		preValidation(habitacion, false);
		return actualizar(id, habitacion);
	}
	
	private void preValidation(Habitacion habitacion, boolean isCreate) throws Exception {
		logger.log(Level.INFO, "Ingresa a preValidation()");
		boolean valid;
		if (isCreate) {
			valid = habitacionRepository.existHabitacionByNumeroHabitacion(habitacion);
			if (valid) {
				throw new Exception ("El número de habitación ya existe en la base de datos");
			}		
		}
	}
		
	private void validHabitacionBeforeSave(Habitacion habitacion) throws Exception {
		logger.log(Level.INFO, "Ingresa a validHabitacionBeforeSave()");

				
		// Validamos el numero de habitacion
		if (Utils.cadContainsLetters(habitacion.getNumeroHabitacion().toString())) {
			throw new Exception("Numero de habitación no valido! Ingrese solamente dígitos.");
		}
				
		// Asignamos el tipo de habitación correspondiente a la habitación, si no tiene, por defecto se asigna SIMPLE
		List<TipoHabitacion> tipoHabitaciones= new ArrayList<TipoHabitacion>();
		tipoHabitaciones.add(habitacion.getTipoHabitacion());
		if (Utils.isNullOrEmpty(tipoHabitaciones)) {
			habitacion.setTipoHabitacion(tipoHabitacionRepository.findById(TipoHabitacion.SIMPLE));		
		} else {
			habitacion.setTipoHabitacion(tipoHabitacionRepository.findById(habitacion.getTipoHabitacion().getId()));
		}
		
		habitacion.setFechaCreacion(new Date());
		habitacion.setFechaEliminacion(null);
	}
	
	private Habitacion actualizar(Long id, Habitacion habitacion) throws Exception {
		logger.log(Level.INFO, "Ingresa a actualizar()");

		// Obtenemos la habitación de la BD por su id
		Habitacion habitacionDb = habitacionRepository.findById(id);
		
		if (habitacionDb == null) {
			throw new Exception("No existe la habitación en la base de datos!");
		}
				
		if (Utils.cadContainsLetters(habitacion.getNumeroHabitacion().toString())) {
			throw new Exception("Numero de habitación no valido!");
		}
		
		return habitacionRepository.update(habitacion);
	}
}
