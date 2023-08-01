package gui.sgomodel.dao;

import java.util.List;

import gui.sgomodel.entities.Anos;

public interface AnosDao {

 	List<Anos> findAll();
 	Anos findAno(Integer ano);
}
