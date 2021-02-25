package controller;

import java.util.List;

import data.ProvinciaRepository;
import entities.Provincia;

public class ProvinciaController {

	private ProvinciaRepository provinciaRepository = new ProvinciaRepository();
	
	public List<Provincia> getAllProvinciasByPais(Long idPais) {
		return provinciaRepository.findProvinciasByIdPais(idPais);
	}
}
