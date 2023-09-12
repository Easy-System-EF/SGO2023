package gui.sgomodel.dao;

import java.util.List;

import gui.sgomodel.entities.NotaFiscal;

public interface NotaFiscalDao {

	void insert(NotaFiscal obj);
	void insertBackUp(NotaFiscal obj);
	void deleteById(Integer nf);
	void zeraAll();
 	List<NotaFiscal> findAll();
}
