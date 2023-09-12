package gui.sgomodel.services;

import java.util.List;

import gui.sgomodel.dao.DaoFactory;
import gui.sgomodel.dao.GrupoDao;
import gui.sgomodel.entities.Grupo;
  
public class GrupoService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private GrupoDao dao = DaoFactory.createGrupoDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Grupo> findAll() {
   		return dao.findAll();
	} 
	
	public List<Grupo> findAllId() {
   		return dao.findAllId();
	} 
	
	public Grupo findById(Integer cod) {
   		return dao.findById(cod);
	} 
	
	public Integer findIdNome(String str) {
   		return dao.findIdNome(str);
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo n�o existe insere, se existe altera 
	public void saveOrUpdate(Grupo obj) {
		if (obj.getCodigoGru() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void insertBackUp(Grupo obj) {
		dao.insertBackUp(obj);
	}

// removendo
	public void remove(int cod) {
		dao.deleteById(cod);
	}
}
