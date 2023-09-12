package gui.sgomodel.dao;

import java.util.List;

import gui.sgomodel.entities.Cliente;

public interface ClienteDao {

	void insert(Cliente obj);
	void insertBackUp(Cliente obj);
	void update(Cliente obj);
	void deleteById(Integer codigo);
	Cliente findById(Integer codigo); 
 	List<Cliente> findAll();
 	List<Cliente> findAllId();
 	List<Cliente> findPesquisa(String str);
 	List<Cliente> findMVR();
 	List<Cliente> findABC();
}
  