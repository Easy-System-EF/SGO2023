package gui.sgomodel.dao;

import gui.sgomodel.entities.Empresa;

public interface EmpresaDao {

 	void insert(Empresa tabend);
 	void update(Empresa tabend);
	Empresa findById(Integer cod);
}
