package gui.sgomodel.dao;

import java.util.List;

import gui.sgomodel.entities.Cargo;

public interface CargoDao {

	void insert(Cargo obj);
	void update(Cargo obj);
	void deleteById(Integer codigo);
	Cargo findById(Integer codigo); 
 	List<Cargo> findAll();

}
