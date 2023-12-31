package gui.sgcpmodel.services;

import java.util.List;

import gui.sgcpmodel.dao.DaoFactory;
import gui.sgcpmodel.dao.TipoConsumidorDao;
import gui.sgcpmodel.entities.TipoConsumo;
 
public class TipoConsumoService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private TipoConsumidorDao dao = DaoFactory.createTipoFornecedorDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public Integer findPesquisa(String nome) {
   		return dao.findPesquisa(nome);
	} 
	
	public List<TipoConsumo> findAll() {
   		return dao.findAll();
	} 
	
	public List<TipoConsumo> findAllId() {
   		return dao.findAllId();
	} 
	
	public TipoConsumo findById(int cod) {
   		return dao.findById(cod);
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo n�o existe insere, se existe altera 
	public void saveOrUpdate(TipoConsumo obj) {
		if (obj.getCodigoTipo() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void insertBackUp(TipoConsumo obj) {
		dao.insertBackUp(obj);
	}

// removendo
	public void remove(int tipo) {
		dao.deleteById(tipo);
	}
}
