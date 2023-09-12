package gui.sgomodel.dao;

import java.util.List;

import gui.sgomodel.entities.Anos;

public interface AnosDao {

 	List<Anos> findAll();
 	List<Anos> findAllId();
 	Anos findAno(Integer ano);
 	void insert(Anos obj);
 	void update(Anos obj); 	
}
