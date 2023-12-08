package gui.sgomodel.services;

import java.util.List;

import gui.sgomodel.dao.DaoFactory;
import gui.sgomodel.dao.FechamentoAnualDao;
import gui.sgomodel.entities.FechamentoAnual;
  
public class FechamentoAnualService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private FechamentoAnualDao dao = DaoFactory.createFechamentoAnualDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<FechamentoAnual> findAll() {
   		return dao.findAll();
	} 
	
	public void zeraAll() {
   		dao.zeraAll();
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo n�o existe insere, se existe altera 
	public void insert(FechamentoAnual obj) {
			dao.insert(obj);
	}	
}
