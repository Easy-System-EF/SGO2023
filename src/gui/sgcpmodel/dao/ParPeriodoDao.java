package gui.sgcpmodel.dao;

import java.util.List;

import gui.sgcpmodel.entities.consulta.ParPeriodo;

public interface ParPeriodoDao {

	void update(ParPeriodo obj);
  	void deleteByAll();
  	ParPeriodo findById(int cod);
   	List<ParPeriodo> findAll();
 }
