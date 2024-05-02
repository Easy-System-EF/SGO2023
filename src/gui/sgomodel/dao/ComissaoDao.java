package gui.sgomodel.dao;

import java.util.List;

import gui.sgomodel.entities.Comissao;

public interface ComissaoDao {

	void insert(Comissao obj);
	void insertBackUp(Comissao obj);
	void deleteById(Integer codigo);
	void zeraAll();
 	List<Comissao> findByOS(Integer os);
 	List<Comissao> findByBalcao(Integer bal);
 	List<Comissao> findMesAno(int mm, int aa);
 	List<Comissao> findAll();
 	Double comSumTotalFun(int mm, int aa, int codFun);
 	Double comSumTotalGeral(int mm, int aa);
 	Double oSSumComissao(int mm, int aa, int os);
 	Double balcaoSumComissao(int mm, int aa, int bal);
}
  