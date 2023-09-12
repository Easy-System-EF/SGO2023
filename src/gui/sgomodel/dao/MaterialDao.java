package gui.sgomodel.dao;

import java.util.List;

import gui.sgomodel.entities.Material;

public interface MaterialDao {

	void insert(Material obj);
	void insertBackUp(Material obj);
	void update(Material obj);
	void deleteById(Integer codigo);
	Material findById(Integer codigo); 
 	List<Material> findAll();
 	List<Material> findAllId();
 	List<Material> findMVR();
 	List<Material> findABC();
 	List<Material> findPesquisa(String str);
 
}
