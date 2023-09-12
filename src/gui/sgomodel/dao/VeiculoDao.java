package gui.sgomodel.dao;

import java.util.List;

import gui.sgomodel.entities.Veiculo;

public interface VeiculoDao {

	void insert(Veiculo obj);
	void insertBackUp(Veiculo obj);
	void update(Veiculo obj);
	void deleteById(String placa);
 	List<Veiculo> findAll();
	Veiculo findByPlaca(String placa);
}
