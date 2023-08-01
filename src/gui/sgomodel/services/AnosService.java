package gui.sgomodel.services;

import java.util.List;

import gui.sgomodel.dao.AnosDao;
import gui.sgomodel.dao.DaoFactory;
import gui.sgomodel.entities.Anos;
  
public class AnosService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private AnosDao dao = DaoFactory.createAnosDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Anos> findAll() {
   		return dao.findAll();
	} 

	public Anos findAno(Integer ano) {
   		return dao.findAno(ano);
	} 	
}
