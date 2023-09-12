package gui.sgomodel.services;

import java.util.List;

import gui.sgomodel.dao.DaoFactory;
import gui.sgomodel.dao.OrcVirtualDao;
import gui.sgomodel.entities.OrcVirtual;
 
public class OrcVirtualService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private OrcVirtualDao dao = DaoFactory.createOrcVirtualDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<OrcVirtual> findAll() {
   		return dao.findAll();
	} 
	
	public List<OrcVirtual> findAllId() {
   		return dao.findAllId();
	} 
	
	public List<OrcVirtual> findPesquisa(String str) {
   		return dao.findPesquisa(str);
	} 
	
	public List<OrcVirtual> findByOrcto(Integer orc) {
   		return dao.findByOrcto(orc);
	} 
	
	public Double findByTotalOrc(Integer orc) {
   		return dao.findByTotalOrc(orc);
	} 
	
	public Double findByTotalBal(Integer bal) {
   		return dao.findByTotalBal(bal);
	} 
	
	public void zeraAll() {
   		dao.zeroAll();
	} 
	
	public List<OrcVirtual> findByBalcao(Integer bal) {
   		return dao.findByBalcao(bal);
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo n�o existe insere, se existe altera 
	public void saveOrUpdate(OrcVirtual obj) {
		if (obj.getNumeroVir() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void insertBackUp(OrcVirtual obj) {
		dao.insertBackUp(obj);
	}

// removendo
	public void removeVir(int numVir) {
		dao.deleteByIdVir(numVir);
	}
	public void removeOrc(int numOrc) {
		dao.deleteByIdOrc(numOrc);
	}
	public void removeBal(int numBal) {
		dao.deleteByIdBal(numBal);
	}
}
