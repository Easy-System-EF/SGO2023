package gui.sgomodel.services;

import java.util.List;

import gui.sgomodel.dao.ClienteDao;
import gui.sgomodel.dao.DaoFactory;
import gui.sgomodel.entities.Cliente;
 
public class ClienteService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private ClienteDao dao = DaoFactory.createClienteDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Cliente> findAll() {
   		return dao.findAll();
	} 
	
	public List<Cliente> findMVR() {
   		return dao.findMVR();
	} 
	
	public List<Cliente> findABC() {
   		return dao.findABC();
	} 
	
	public List<Cliente> findPesquisa(String str) {
   		return dao.findPesquisa(str);
	} 
	
	public Cliente findById(Integer cod) {
    		return dao.findById(cod);
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo n�o existe insere, se existe altera 
	public void saveOrUpdate(Cliente obj) {
		if (obj.getCodigoCli() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

// removendo
	public void remove(Cliente obj) {
		dao.deleteById(obj.getCodigoCli());
	}
}
