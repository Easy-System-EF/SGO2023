package gui.sgomodel.services;

import java.util.List;

import gui.sgomodel.dao.DaoFactory;
import gui.sgomodel.dao.SituacaoDao;
import gui.sgomodel.entities.Situacao;
  
public class SituacaoService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private SituacaoDao dao = DaoFactory.createSituacaoDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Situacao> findAll() {
   		return dao.findAll();
	} 
	
	public List<Situacao> findAllId() {
   		return dao.findAllId();
	} 
	
	public Situacao findById(Integer cod) {
   		return dao.findById(cod);
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo n�o existe insere, se existe altera 
	public void saveOrUpdate(Situacao obj) {
		if (obj.getNumeroSit() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void insertBackUp(Situacao obj) {
		dao.insertBackUp(obj);
	}

// removendo
	public void remove(int cod) {
		dao.deleteById(cod);
	}
}
