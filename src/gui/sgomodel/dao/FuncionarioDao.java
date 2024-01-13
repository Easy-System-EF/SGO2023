package gui.sgomodel.dao;

import java.util.Date;
import java.util.List;

import gui.sgomodel.entities.Funcionario;

public interface FuncionarioDao {

	void insert(Funcionario obj);
	void insertBackup(Funcionario obj);
	void update(Funcionario obj);
	void deleteById(Integer codigo);
	Funcionario findById(Integer codigo); 
 	List<Funcionario> findAll(Date data);
// 	List<Funcionario> findAll(int aa, int mm);
 	List<Funcionario> findAllId();
	List<Funcionario> findPesquisa(String str, Date data);
//	List<Funcionario> findPesquisa(String str, int aa, int mm);
 	List<Funcionario> findByAtivo(String situacao, Date data);
// 	List<Funcionario> findByAtivo(String situacao, int aa, int mm);
}
  