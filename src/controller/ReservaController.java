package controller;

import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import data.ReservaRepository;
import entities.EstadoReserva;
import entities.Persona;
import entities.Reserva;
import utils.Utils;
import utils.email.EmailService;

public class ReservaController {

	private Logger logger = LogManager.getLogger(getClass());
	private ReservaRepository reservaRepository = new ReservaRepository();
	private EstadoReservaController estadoReservaCtrl = new EstadoReservaController();
	private PersonaController personaCtrl = new PersonaController();
	private EmailService emailService = new EmailService();

	public List<Reserva> getAll() throws Exception {
		return reservaRepository.findAll();
	}
	
	public List<Reserva> getAllByPersona(Persona persona) throws Exception {
		return reservaRepository.findReservasByIdPersona(persona.getId());
	}
	
	public Reserva registrarReserva(Reserva reserva) throws Exception {
		validReservaBeforeSave(reserva);
		Reserva reservaDB = reservaRepository.save(reserva);
		emailService.sendEmail(reservaDB.getPersona(), "La habitacion " + reserva.getHabitacion().getNumeroHabitacion() + " ha sido reservada!", "Aviso de Reserva");
		return reservaRepository.save(reserva);
	}
	
	public void cancelarReserva(Long id) throws Exception {
		Reserva reservaDb = reservaRepository.findById(id);
		if (reservaDb.getId() == null) {
			throw  new Exception("No existe la reserva!");
		}
		int reserva = reservaRepository.cancelar(id);
		// Si reserva es igual a 0 significa que no se modifico ninguna reserva
		if (reserva == 0) {
			throw new Exception("Ocurrio un error al momento de cancelar la reserva");
		}
		emailService.sendEmail(reservaDb.getPersona(), "La reserva ha sido cancelada!", "Aviso de Cancelacion");
	}
	
	private void validReservaBeforeSave(Reserva reserva) throws Exception {
		// Validamos que la persona exista
		Persona persona = personaCtrl.getByDocumento(reserva.getPersona());
		if (persona == null) {
			throw new Exception("La persona no existe en la base de datos del sistema!");
		}
		
		if (reserva.getEstadoReserva() == null) {
			reserva.setEstadoReserva(estadoReservaCtrl.getByDescripcion(EstadoReserva.RESERVADA));
		}
		
		if (reserva.getHabitacion() != null && reserva.getSalon() != null) {
			throw new Exception("No se puede realizar una reserva de una habitacion y de un salon al mismo tiempo");
		}
		
		reserva.setFechaReserva(new Date());
		reserva.setFechaCancelacion(null);
		reserva.setCantDias(Utils.diferenciaEntreDosFechas(reserva.getFechaEntrada(), reserva.getFechaSalida()));
		reserva.setFechaCreacion(new Date());
	}

	public void delete(Long id) throws Exception {
		logger.log(Level.INFO, "Ingresa a delete()");
		Reserva reservaDB = reservaRepository.findById(id);
		if (reservaDB.getId() == null) {
			throw new Exception("ERROR - No existe reserva en la base de datos!");
		}
		reservaRepository.delete(id);		
	}
}
