package gui.sgomodel.services;

import java.util.List;

import gui.sgomodel.dao.DaoFactory;
import gui.sgomodel.dao.OrcamentoDao;
import gui.sgomodel.entities.Orcamento;
 
public class OrcamentoService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private OrcamentoDao dao = DaoFactory.createOrcamentoDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Orcamento> findAll() {
   		return dao.findAll();
	} 
	
	public List<Orcamento> findAllId() {
   		return dao.findAllId();
	} 
	
	public List<Orcamento> findPesquisa(String str) {
   		return dao.findPesquisa(str);
	} 
	
	public Orcamento findById(Integer cod) {
		return dao.findById(cod);
	} 

	public Orcamento findByOS(Integer os) {
		return dao.findById(os);
	} 

	public Orcamento findByPlaca(String placa) {
		return dao.findByPlaca(placa);
	} 

	public List<Orcamento> findByMesAno(int mes, int ano) {
		return dao.findMesAno(mes, ano);
	} 

	public List<Orcamento> findByAberto() {
		return dao.findAberto();
	} 

// * inserindo ou atualizando via dao
// * se o codigo n�o existe insere, se existe altera 
	public void saveOrUpdate(Orcamento obj) {
		if (obj.getNumeroOrc() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void insertBackUp(Orcamento obj) {
		dao.insertBackUp(obj);
	}

// removendo
	public void remove(Orcamento obj) {
		dao.deleteById(obj.getNumeroOrc());
	}
}
