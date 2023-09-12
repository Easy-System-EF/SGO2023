package gui.sgomodel.dao;

import java.util.List;

import gui.sgomodel.entities.ReposicaoVeiculo;

public interface ReposicaoVeiculoDao {

	void insert(ReposicaoVeiculo obj);
	void deleteByOs(int numOs);
	List<ReposicaoVeiculo> findByPlaca(String placa, Integer km); 
 	List<ReposicaoVeiculo> findAllData();
 	List<ReposicaoVeiculo> findAllId();

}
 