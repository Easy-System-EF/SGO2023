package gui.sgomodel.dao;

import java.util.List;

import gui.sgomodel.entities.Empresa;

public interface EmpresaDao {

 	void insert(Empresa tabend);
 	void update(Empresa tabend);
	Empresa findById(Integer cod);
	List<Empresa> findAll();
}
