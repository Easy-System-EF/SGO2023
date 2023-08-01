package gui.sgomodel.dao;

import java.util.List;

import gui.sgomodel.entities.Funcionario;

public interface FuncionarioDao {

	void insert(Funcionario obj);
	void update(Funcionario obj);
	void deleteById(Integer codigo);
	void zeraAll();
	Funcionario findById(Integer codigo); 
 	List<Funcionario> findAll(int aa, int mm);
 	List<Funcionario> findPesquisa(String str, int aa, int mm);
 	List<Funcionario> findByAtivo(String situacao, int aa, int mm);
}
  