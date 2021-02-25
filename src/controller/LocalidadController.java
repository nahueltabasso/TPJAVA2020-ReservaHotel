package controller;

import java.util.List;

import data.LocalidadRepository;
import entities.Localidad;

public class LocalidadController {

	private LocalidadRepository localidadRepository = new LocalidadRepository();
	
	public List<Localidad> getLocalidadesByProvincia(Long idProvincia) {
		return localidadRepository.findAllByIdProvincia(idProvincia);
	}
}
