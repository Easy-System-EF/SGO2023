package gui.sgomodel.services;

import java.util.List;

import gui.sgomodel.dao.DaoFactory;
import gui.sgomodel.dao.EntradaDao;
import gui.sgomodel.entities.Entrada;
 
public class EntradaService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private EntradaDao dao = DaoFactory.createEntradaDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Entrada> findAll() {
   		return dao.findAll();
	} 
	
	public List<Entrada> findAllId() {
   		return dao.findAllId();
	} 
	
	public List<Entrada> findByNnf(int nnf) {
		return dao.findByNnf(nnf);	
	} 

	public List<Entrada> findByForNnf(int forn, int nnf) {
		return dao.findByForNnf(forn, nnf);	
	} 

	public List<Entrada> findByForn(int forn) {
		return dao.findByForn(forn);	
	} 

	public List<Entrada> findByMat(int mat) {
		return dao.findByMat(mat);	
	} 

	public Entrada findById(int cod) {
		return dao.findById(cod);	
	} 

// * inserindo ou atualizando via dao
// * se o codigo n�o existe insere, se existe altera 
	public void saveOrUpdate(Entrada obj) {
		if (obj.getNumeroEnt() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void insertBackUp(Entrada obj) {
		dao.insertBackUp(obj);
	}

// removendo
	public void remove(Entrada obj) {
		dao.deleteById(obj.getNumeroEnt());
	}
}
