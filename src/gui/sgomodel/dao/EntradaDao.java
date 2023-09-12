package gui.sgomodel.dao;

import java.util.List;

import gui.sgomodel.entities.Entrada;

public interface EntradaDao {

	void insert(Entrada obj);
	void insertBackUp(Entrada obj);
	void update(Entrada obj);
	void deleteById(int cod);
	void zeraAll();
	List<Entrada> findByNnf(int nnf); 
	List<Entrada> findByForNnf(int forn, int nnf); 
	Entrada findById(int cod); 
 	List<Entrada> findAll();
 	List<Entrada> findAllId();
}
  