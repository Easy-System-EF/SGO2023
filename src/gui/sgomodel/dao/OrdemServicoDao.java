package gui.sgomodel.dao;

import java.util.List;

import gui.sgomodel.entities.OrdemServico;

public interface OrdemServicoDao {

	void insert(OrdemServico obj);
	void update(OrdemServico obj);
	void deleteById(Integer codigo);
	OrdemServico findById(Integer codigo); 
 	List<OrdemServico> findAll();
 	List<OrdemServico> findPlaca(String str);
 	List<OrdemServico> findMesAno(Integer mes, Integer ano);
 	List<OrdemServico> findMesAnoList(Integer mes, Integer ano);
}
  