package gui.sgomodel.services;

import java.util.List;

import gui.sgomodel.dao.AdiantamentoDao;
import gui.sgomodel.dao.DaoFactory;
import gui.sgomodel.entities.Adiantamento;

public class AdiantamentoService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private AdiantamentoDao dao = DaoFactory.createAdiantamentoDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Adiantamento> findMes(Integer mes, Integer ano) {
		return dao.findMes(mes, ano);
	}

	public Double findByTotalAdi(Integer mes, Integer ano, Integer codFun) {
   		return dao.findByTotalAdi(mes, ano, codFun);
	} 
	
	public Double findByTotalAdiAnual(Integer ano, Integer codFun) {
   		return dao.findByTotalAdiAnual(ano, codFun);
	} 
	
	public List<Adiantamento> findPesquisaFun(String str) {
		return dao.findPesquisaFun(str);
	}

	public List<Adiantamento> findAll() {
		return dao.findAll();
	}

// * inserindo ou atualizando via dao
// * se o codigo n�o existe insere, se existe altera 
	public void saveOrUpdate(Adiantamento obj) {
		if (obj.getNumeroAdi() == null) {
			dao.insert(obj);
		}
	}

	public void insertBackUp(Adiantamento obj) {
		dao.insertBackUp(obj);
	}

// removendo
	public void remove(Integer cod) {
		dao.deleteById(cod);
	}

	public void zeraAll() {
   		dao.zeraAll();
	} 	
}
