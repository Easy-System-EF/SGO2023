package gui.sgomodel.dao;

import java.util.List;

import gui.sgomodel.entities.Grupo;

public interface GrupoDao {

	void insert(Grupo obj);
	void insertBackUp(Grupo obj);
	void update(Grupo obj);
	void deleteById(Integer codigo);
	Grupo findById(Integer codigo); 
	Integer findIdNome(String str); 
 	List<Grupo> findAll();
 	List<Grupo> findAllId();

}
