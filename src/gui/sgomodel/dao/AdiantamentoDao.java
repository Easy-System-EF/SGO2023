package gui.sgomodel.dao;

import java.util.List;

import gui.sgomodel.entities.Adiantamento;

public interface AdiantamentoDao {

	void insert(Adiantamento obj);
	void insertBackUp(Adiantamento obj);
	void deleteById(Integer codigo);
	void zeraAll();
 	Adiantamento findMesIdFun(Integer cod, Integer mes, Integer Ano, String tipo);
 	List<Adiantamento> findMesTipo(Integer mes, Integer Ano, String tipo);
 	List<Adiantamento> findMes(Integer mes, Integer Ano);
 	List<Adiantamento> findPesquisaFun(String str);
 	List<Adiantamento> findByOs(Integer idOs);
 	List<Adiantamento> findByBalcao(Integer idBalcao);
 	List<Adiantamento> findAll();
 	List<Adiantamento> findAllId();
	Double findByTotalCom(Integer mes, Integer ano, Integer codFun);
	Double findByTotalAdi(Integer mes, Integer ano, Integer codFun);
}
  