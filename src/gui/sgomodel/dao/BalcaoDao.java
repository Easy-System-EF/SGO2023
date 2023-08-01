package gui.sgomodel.dao;

import java.util.List;

import gui.sgomodel.entities.Balcao;

public interface BalcaoDao {

	void insert(Balcao obj);
	void update(Balcao obj);
	void deleteById(Integer codBal);
	Balcao findById(Integer codigo); 
 	List<Balcao> findAll();
	List<Balcao> findMesAno(Integer mm, Integer aa);
}
  