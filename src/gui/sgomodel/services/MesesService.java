package gui.sgomodel.services;

import java.util.List;

import gui.sgomodel.dao.DaoFactory;
import gui.sgomodel.dao.MesesDao;
import gui.sgomodel.entities.Meses;
  
public class MesesService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private MesesDao dao = DaoFactory.createMesesDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public void insert(Meses obj) {
   		dao.insert(obj);
	} 
	
	public List<Meses> findAll() {
   		return dao.findAll();
	} 
	
	public Meses findId(Integer mes) {
   		return dao.findId(mes);
	} 	
}
