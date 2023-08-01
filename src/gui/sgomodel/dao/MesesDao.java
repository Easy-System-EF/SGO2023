package gui.sgomodel.dao;

import java.util.List;

import gui.sgomodel.entities.Meses;

public interface MesesDao {

 	List<Meses> findAll();
 	Meses findId(Integer mes);
}
