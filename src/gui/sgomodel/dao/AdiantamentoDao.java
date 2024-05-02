package gui.sgomodel.dao;

import java.util.List;

import gui.sgomodel.entities.Adiantamento;

public interface AdiantamentoDao {

	void insert(Adiantamento obj);
	void insertBackUp(Adiantamento obj);
	void deleteById(Integer codigo);
	void zeraAll();
 	List<Adiantamento> findMes(Integer mes, Integer Ano);
 	List<Adiantamento> findPesquisaFun(String str);
 	List<Adiantamento> findAll();
	Double findByTotalAdi(Integer mes, Integer ano, Integer codFun);
	Double findByTotalAdiAnual(Integer ano, Integer codFun);
}
  