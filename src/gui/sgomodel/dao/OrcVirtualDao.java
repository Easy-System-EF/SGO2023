package gui.sgomodel.dao;

import java.util.List;

import gui.sgomodel.entities.OrcVirtual;

public interface OrcVirtualDao {

	void insert(OrcVirtual obj);
	void update(OrcVirtual obj);
	void deleteByIdVir(int numVir);
	void deleteByIdOrc(int numOrc);
	void deleteByIdBal(int numBal);
 	List<OrcVirtual> findAll();
 	List<OrcVirtual> findPesquisa(String str);
 	List<OrcVirtual> findByOrcto(Integer orc);
	List<OrcVirtual> findByBalcao(Integer bal);
	Double findByTotalOrc(Integer orc);
	Double findByTotalBal(Integer bal);
	void zeroAll();
}
  