package gui.sgomodel.services;

import java.util.List;

import gui.sgomodel.dao.ComissaoDao;
import gui.sgomodel.dao.DaoFactory;
import gui.sgomodel.entities.Comissao;

public class ComissaoService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private ComissaoDao dao = DaoFactory.createComissaoDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		

	public List<Comissao> findMesAno(Integer mes, Integer ano) {
		return dao.findMesAno(mes, ano);
	}

	public List<Comissao> findByOS(Integer os) {
		return dao.findByOS(os);
	}

	public List<Comissao> findByBalcao(Integer bal) {
		return dao.findByBalcao(bal);
	}

	public List<Comissao> findAll() {
		return dao.findAll();
	}

	public Double comSumTotalFun(int mm, int aa, int codFun) {
		return dao.comSumTotalFun(mm, aa, codFun);
	}

	public Double comSumTotalGeral(int mm, int aa) {
		return dao.comSumTotalGeral(mm, aa);
	}

	public Double oSSumComissao(int mm, int aa, int os) {
		return dao.oSSumComissao(mm, aa, os);
	}

	public Double balcaoSumComissao(int mm, int aa, int bal) {
		return dao.balcaoSumComissao(mm, aa, bal);
	}

// * inserindo ou atualizando via dao
// * se o codigo n�o existe insere, se existe altera 
	public void saveOrUpdate(Comissao obj) {
		if (obj.getNumeroCom() == null) {
			dao.insert(obj);
		}
	}

	public void insertBackUp(Comissao obj) {
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
