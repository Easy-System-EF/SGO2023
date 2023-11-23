package gui.sgomodel.services;

import java.util.List;

import gui.sgomodel.dao.DaoFactory;
import gui.sgomodel.dao.FechamentoAnoDao;
import gui.sgomodel.entities.FechamentoAno;
  
public class FechamentoAnoService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private FechamentoAnoDao dao = DaoFactory.createFechamentoAnoDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<FechamentoAno> findAll() {
   		return dao.findAll();
	} 
	
	public void zeraAll() {
   		dao.zeraAll();
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo n�o existe insere, se existe altera 
	public void insert(FechamentoAno obj) {
			dao.insert(obj);
	}	
}
