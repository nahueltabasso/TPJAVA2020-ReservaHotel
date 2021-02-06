package controller;

import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import data.TarjetaRepository;
import entities.Tarjeta;
import utils.Utils;

public class TarjetaController {
	
	private Logger logger = LogManager.getLogger(getClass());
	private TarjetaRepository tarjetaRepository = new TarjetaRepository();
	
	public Tarjeta registrarTarjeta(Tarjeta tarjeta) throws Exception {
		logger.log(Level.INFO, "Ingresa a registrarTarjeta()");
		preValidation(tarjeta, true);
		validTarjetaBeforeSave(tarjeta);
		Tarjeta tarjetaDB = tarjetaRepository.save(tarjeta);
		return tarjetaDB;
	}
	
	public List<Tarjeta> getAll() throws Exception {
		logger.log(Level.INFO, "Ingresa a getAll()");
		return tarjetaRepository.findAll();
	}
	
	public Tarjeta getById(Long id) throws Exception {
		logger.log(Level.INFO, "Ingresa a getById()");
		Tarjeta tarjetaDB = tarjetaRepository.findById(id);
		if (tarjetaDB == null) {
			throw new Exception("No existe la tarjeta en la base de datos!");
		}
		return tarjetaDB;
	}
		
	public void delete(Long id) throws Exception {
		logger.log(Level.INFO, "Ingresa a delete()");
		Tarjeta tarjetaDB = tarjetaRepository.findById(id);
		if (tarjetaDB.getId() == null) {
			throw new Exception("ERROR - No existe la tarjeta en la base de datos!");
		}
		tarjetaRepository.delete(id);
	}
	
	/* Las tarjetas deben actualizarse? o tienen que ser eliminadas?
	 * 
	public Tarjeta actualizarTarjeta(Long id, Tarjeta tarjeta) throws Exception {
		logger.log(Level.INFO, "Ingresa a actualizarTarjeta()");
		preValidation(tarjeta, true);
		return actualizar(id, tarjeta);
	}
	*/
	
	private void preValidation(Tarjeta tarjeta, boolean isCreate) throws Exception {
		logger.log(Level.INFO, "Ingresa a preValidation()");
		boolean valid;
		if (isCreate) {
			valid = tarjetaRepository.existTarjeta(tarjeta);
			if (valid) {
				throw new Exception ("La tarjeta ya existe en la base de datos");
			}		
		}
	}
		
	private void validTarjetaBeforeSave(Tarjeta tarjeta) throws Exception {
		logger.log(Level.INFO, "Ingresa a validTarjetaBeforeSave()");

		// Validamos que el numero de la tarjeta sea válido
		if (!Utils.validaNumeroTarjeta(tarjeta.getNumeroTarjeta().toString())) {
			throw new Exception("Número de tarjeta no valido.");
		}		
				
		// Validamos que el monto no tenga letras
		if (!Utils.validaPrecio(tarjeta.getMonto().toString())) {
			throw new Exception("Monto no válido! Ingrese solamente dígitos.");
		}
		
		// Validamos la fecha de vencimiento
		if (!Utils.validaVencimientoTarjeta(tarjeta.getFechaVencimiento())) {
			throw new Exception("Fecha de caducidad no válida.");
		}
		
		tarjeta.setFechaCreacion(new Date());
		tarjeta.setFechaEliminacion(null);
	}
	
	/* Las tarjetas deben actualizarse? o tienen que ser eliminadas?
	 * 
	private Tarjeta actualizar(Long id, Tarjeta tarjeta) throws Exception {
		logger.log(Level.INFO, "Ingresa a actualizar()");

		// Obtenemos la tarjeta de la BD por su id
		Tarjeta tarjetaDB = tarjetaRepository.findById(id);
		
		if (tarjetaDB == null) {
			throw new Exception("No existe la tarjeta en la base de datos!");
		}
		
				
		tarjetaDB.setFechaVencimiento(tarjeta.getFechaVencimiento());
		tarjetaDB.setNombreTarjeta(tarjeta.getNombreTarjeta());
								
		return tarjetaRepository.update(tarjetaDB);
	}
	*/

}
