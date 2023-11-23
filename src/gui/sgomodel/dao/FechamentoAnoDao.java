package gui.sgomodel.dao;

import java.util.List;

import gui.sgomodel.entities.FechamentoAno;

public interface FechamentoAnoDao {

	void insert(FechamentoAno obj);
 	List<FechamentoAno> findAll();
	void zeraAll();
}
