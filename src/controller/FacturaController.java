package controller;

import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import data.FacturaRepository;
import entities.Factura;
import utils.Utils;

public class FacturaController {
	
	private Logger logger = LogManager.getLogger(getClass());
	private FacturaRepository facturaRepository = new FacturaRepository();
	
	public Factura registrarFactura(Factura factura) throws Exception {
		logger.log(Level.INFO, "Ingresa a registrarFactura()");
		preValidation(factura, true);
		validFacturaBeforeSave(factura);
		Factura facturaDB = facturaRepository.save(factura);
		return facturaDB;
	}
	
	public List<Factura> getAll() throws Exception {
		logger.log(Level.INFO, "Ingresa a getAll()");
		return facturaRepository.findAll();
	}
	
	public Factura getById(Long id) throws Exception {
		logger.log(Level.INFO, "Ingresa a getById()");
		Factura facturaDB = facturaRepository.findById(id);
		if (facturaDB == null) {
			throw new Exception("No existe la factura en la base de datos!");
		}
		return facturaDB;
	}
		
	public void delete(Long id) throws Exception {
		logger.log(Level.INFO, "Ingresa a delete()");
		Factura facturaDB = facturaRepository.findById(id);
		if (facturaDB.getId() == null) {
			throw new Exception("ERROR - No existe la factura en la base de datos!");
		}
		facturaRepository.delete(id);
	}
	
	
	/* Las facturas no se pueden actualizar
	 * 
	 *
	public Factura actualizarFactura(Long id, Factura factura) throws Exception {
		logger.log(Level.INFO, "Ingresa a actualizarFactura()");
		preValidation(factura, true);
		return actualizar(id, factura);
	}
	*/
	
	private void preValidation(Factura factura, boolean isCreate) throws Exception {
		logger.log(Level.INFO, "Ingresa a preValidation()");
		boolean valid;
		if (isCreate) {
			valid = facturaRepository.existFactura(factura);
			if (valid) {
				throw new Exception ("La factura ya existe en la base de datos");
			}		
		}
	}
		
	private void validFacturaBeforeSave(Factura factura) throws Exception {
		logger.log(Level.INFO, "Ingresa a validFacturaBeforeSave()");

				
		// Validamos que el atributo monto no tenga letras
		if (!Utils.validaPrecio(factura.getMonto().toString())) {
			throw new Exception("Monto no válido! Ingrese solamente dígitos.");
		}
		
		// Validamos que el número de la factura no tenga letras
		if (!Utils.cadContainsLetters(factura.getNumeroFactura().toString())) {
			throw new Exception("Numero de factura no válido! Ingrese solamente dígitos.");
		}
		
				
		factura.setFechaCreacion(new Date());
		factura.setFechaEliminacion(null);
	}
	
	/* Las facturas no se deben actualizar
	 * 
	 *
	private Factura actualizar(Long id, Factura factura) throws Exception {
		logger.log(Level.INFO, "Ingresa a actualizar()");

		// Obtenemos salón de la BD por su id
		Factura facturaDB = facturaRepository.findById(id);
		
		if (facturaDB == null) {
			throw new Exception("No existe la factura en la base de datos!");
		}
		
		// Validamos que el atributo monto no tenga letras
		if (!Utils.validaPrecio(factura.getMonto().toString())) {
			throw new Exception("Monto no válido! Ingrese solamente dígitos.");
		}
				
		facturaDB.setNumeroFactura(factura.getNumeroFactura());
		facturaDB.setMonto(factura.getMonto());
						
		return facturaRepository.update(facturaDB);
	}
	*/
}
