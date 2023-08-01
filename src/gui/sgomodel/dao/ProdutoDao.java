package gui.sgomodel.dao;

import java.util.List;

import gui.sgomodel.entities.Produto;

public interface ProdutoDao {

	void insert(Produto obj);
	void update(Produto obj);
	void deleteById(Integer codigo);
	void zeraAll();
	Produto findById(Integer codigo); 
 	List<Produto> findAll();
 	List<Produto> findMVR();
 	List<Produto> findPesquisa(String str);
 
}
