package gui.sgomodel.services;

import java.util.List;

import gui.sgomodel.dao.CargoDao;
import gui.sgomodel.dao.DaoFactory;
import gui.sgomodel.entities.Cargo;
  
public class CargoService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private CargoDao dao = DaoFactory.createCargoDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Cargo> findAll() {
   		return dao.findAll();
	} 
	
	public List<Cargo> findAllId() {
   		return dao.findAllId();
	} 
	
	public Cargo findById(Integer cod) {
   		return dao.findById(cod);
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo n�o existe insere, se existe altera 
	public void saveOrUpdate(Cargo obj) {
		if (obj.getCodigoCargo() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void insertBackUp(Cargo obj) {
		dao.insertBackUp(obj);
	}

// removendo
	public void remove(int cod) {
		dao.deleteById(cod);
	}
}
