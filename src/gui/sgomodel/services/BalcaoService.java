package gui.sgomodel.services;

import java.util.List;

import gui.sgomodel.dao.BalcaoDao;
import gui.sgomodel.dao.DaoFactory;
import gui.sgomodel.entities.Balcao;
 
public class BalcaoService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private BalcaoDao dao = DaoFactory.createBalcaoDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Balcao> findAll() {
   		return dao.findAll();
	} 
	
	public List<Balcao> findAllId() {
   		return dao.findAllId();
	} 
	
	public Balcao findById(Integer cod) {
		return dao.findById(cod);
} 

	public List<Balcao> findByMesAno(int mes, int ano) {
		return dao.findMesAno(mes, ano);
	} 

// * inserindo ou atualizando via dao
// * se o codigo n�o existe insere, se existe altera 
	public void saveOrUpdate(Balcao obj) {
		if (obj.getNumeroBal() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void insertBackUp(Balcao obj) {
		dao.insertBackUp(obj);
	}

// removendo
	public void remove(Integer codBal) {
		dao.deleteById(codBal);
	}
}
