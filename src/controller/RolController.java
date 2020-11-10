package controller;

import java.util.List;

import data.RolRepository;
import entities.Rol;

public class RolController {

	private RolRepository rolRepository = new RolRepository();
	
	public List<Rol> getAll() throws Exception {
		return rolRepository.findAll();
	}
}
