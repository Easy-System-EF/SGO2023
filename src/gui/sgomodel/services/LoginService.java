package gui.sgomodel.services;

import java.util.List;

import gui.sgomodel.dao.DaoFactory;
import gui.sgomodel.dao.LoginDao;
import gui.sgomodel.entities.Login;
  
public class LoginService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private LoginDao dao = DaoFactory.createLoginDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public Login findBySenha(String senha) {
   		return dao.findBySenha(senha);
	}

	public Login findById(Integer cod) {
   		return dao.findById(cod);
	}

	public List<Login> findAll() {
   		return dao.findAll();
	}

	public void insertOrUpdate(Login obj) {
		if (obj.getNumeroLog() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	} 
	
	public void insertBackUp(Login obj) {
		dao.insertBackUp(obj);
	} 
}
