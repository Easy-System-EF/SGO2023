package gui.sgomodel.dao;

import java.util.List;

import gui.sgomodel.entities.Orcamento;

public interface OrcamentoDao {

	void insert(Orcamento obj);
	void insertBackUp(Orcamento obj);
	void update(Orcamento obj);
	void deleteById(Integer codigo);
	Orcamento findById(Integer codigo); 
	Orcamento findByOS(Integer os); 
	Orcamento findByPlaca(String placa); 
 	List<Orcamento> findAll();
 	List<Orcamento> findAllId();
 	List<Orcamento> findPesquisa(String str);
 	List<Orcamento> findMesAno(Integer mes, Integer ano);
 	List<Orcamento> findMesAnoList(Integer mes, Integer ano);
}
  