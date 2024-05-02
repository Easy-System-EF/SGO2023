package gui.sgomodel.dao;

import java.util.Date;
import java.util.List;

import gui.sgomodel.entities.Funcionario;

public interface FuncionarioDao {

	void insert(Funcionario obj);
	void insertBackUp(Funcionario obj);
	void update(Funcionario obj);
	void deleteById(Integer codigo);
	void zeraAll();
	Funcionario findById(Integer codigo); 
 	List<Funcionario> findAll(Date data);
 	List<Funcionario> findPesquisa(String str, Date data);
 	List<Funcionario> findByAtivo(String situacao, Date data);
}
  