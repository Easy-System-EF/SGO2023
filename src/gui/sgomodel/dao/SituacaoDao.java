package gui.sgomodel.dao;

import java.util.List;

import gui.sgomodel.entities.Situacao;

public interface SituacaoDao {

	void insert(Situacao obj);
	void insertBackUp(Situacao obj);
	void update(Situacao obj);
	void deleteById(Integer codigo);
	Situacao findById(Integer codigo); 
 	List<Situacao> findAll();
 	List<Situacao> findAllId();

}
