package gui.sgomodel.dao;

import java.util.List;

import gui.sgomodel.entities.FechamentoAnual;

public interface FechamentoAnualDao {

	void insert(FechamentoAnual obj);
 	List<FechamentoAnual> findAll();
	void zeraAll();
}
