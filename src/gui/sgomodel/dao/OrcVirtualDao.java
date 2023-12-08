package gui.sgomodel.dao;

import java.util.List;

import gui.sgomodel.entities.OrcVirtual;

public interface OrcVirtualDao {

	void insert(OrcVirtual obj);
	void insertBackUp(OrcVirtual obj);
	void update(OrcVirtual obj);
	void deleteByIdVir(int numVir);
	void deleteByIdOrc(int numOrc);
	void deleteByIdBal(int numBal);
 	List<OrcVirtual> findAll();
 	List<OrcVirtual> findAllId();
 	List<OrcVirtual> findPesquisa(String str);
 	List<OrcVirtual> findByOrcto(Integer orc);
 	List<OrcVirtual> findByOrctoMat(Integer orc, int cod);
	List<OrcVirtual> findByBalcao(Integer bal);
	List<OrcVirtual> findByBalcaoMat(Integer bal, int cod);
	Double findByTotalOrc(Integer orc);
	Double findByTotalBal(Integer bal);
	Double findByCustoOrc(Integer orc);
	Double findByCustoBal(Integer bal);
	void zeroAll();
}
  