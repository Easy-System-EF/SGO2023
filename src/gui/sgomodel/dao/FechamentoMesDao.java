package gui.sgomodel.dao;

import java.util.List;

import gui.sgomodel.entities.FechamentoMes;

public interface FechamentoMesDao {

	void insert(FechamentoMes obj);
 	List<FechamentoMes> findAll();
	void zeraAll();
}
