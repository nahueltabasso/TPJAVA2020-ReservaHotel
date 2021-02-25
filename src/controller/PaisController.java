package controller;

import java.util.List;

import data.PaisRepository;
import entities.Pais;

public class PaisController {

	private PaisRepository paisRepository = new PaisRepository();
	
	public List<Pais> getAll() {
		return paisRepository.findAll();
	}
}
