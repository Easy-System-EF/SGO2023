package gui.sgomodel.dao;

import java.util.List;

import gui.sgomodel.entities.CargoNew;

public interface CargoNewDao {

	void insert(CargoNew obj);
	void insertBackUp(CargoNew obj);
	void update(CargoNew obj);
	void deleteById(Integer codigo);
	CargoNew findById(Integer codigo); 
 	List<CargoNew> findAll();
 	List<CargoNew> findAllId();

}
