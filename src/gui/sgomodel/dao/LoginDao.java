package gui.sgomodel.dao;

import java.util.List;

import gui.sgomodel.entities.Login;

public interface LoginDao {

 	void insert(Login login);
 	void insertBackUp(Login login);
 	void update(Login login);
	Login findBySenha(String senha);
	Login findById(Integer cod);
	List<Login> findAll();
}
