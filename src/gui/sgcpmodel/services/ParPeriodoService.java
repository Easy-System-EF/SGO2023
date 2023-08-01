package gui.sgcpmodel.services;

import java.util.List;

import gui.sgcpmodel.dao.DaoFactory;
import gui.sgcpmodel.dao.ParPeriodoDao;
import gui.sgcpmodel.entities.consulta.ParPeriodo;
 
public class ParPeriodoService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private ParPeriodoDao dao = DaoFactory.createConsultaDao();
 
	public List<ParPeriodo> findAll(){
 		return dao.findAll();
	}
	
	public ParPeriodo findById(int cod) {
   		return dao.findById(cod);
	} 
	
 // * inserindo ou atualizando via dao
// * se o codigo n�o existe insere, se existe altera 
	public void update(ParPeriodo obj) {
		dao.update(obj);
 	}
 
// removendo
	public void remove() {
   			dao.deleteByAll();
 	}
}
