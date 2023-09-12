package gui.sgomodel.services;

import java.util.List;

import gui.sgomodel.dao.DaoFactory;
import gui.sgomodel.dao.FuncionarioDao;
import gui.sgomodel.entities.Funcionario;
 
public class FuncionarioService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private FuncionarioDao dao = DaoFactory.createFuncionarioDao();

//    criar no fornecedorlist , uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Funcionario> findAll(int aa, int mm) {
   		return dao.findAll(aa, mm);
	} 
	
	public List<Funcionario> findAllId() {
   		return dao.findAllId();
	} 
	
	public List<Funcionario> findPesquisa(String str, int aa, int mm) {
   		return dao.findPesquisa(str, aa, mm);
	} 
	
	public List<Funcionario> findByAtivo(String situacao, int aa, int mm) {
   		return dao.findByAtivo(situacao, aa, mm);
	} 
	
	public Funcionario findById(Integer cod) {
    		return dao.findById(cod);
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo n�o existe insere, se existe altera 
	public void saveOrUpdate(Funcionario obj) {
		if (obj.getCodigoFun() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}
	
	public void insertBackup(Funcionario obj) {
		dao.insertBackup(obj);
	}

// removendo
	public void remove(int cod) {
		dao.deleteById(cod);
	}

}
