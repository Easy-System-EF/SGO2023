package gui.sgomodel.dao;

import java.util.List;

import gui.sgomodel.entities.FolhaMes;

public interface FolhaMesDao {

	void insert(FolhaMes obj);
 	List<FolhaMes> findAll();
	void zeraAll();
}
