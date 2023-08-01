package gui.sgcpmodel.dao;

import java.util.Date;
import java.util.List;

import gui.sgcpmodel.entities.Parcela;

public interface ParcelaDao {

	void insert(Parcela obj);
	void update(Parcela obj);
 	void deleteByNnf(int nnf, int codFor);
 	Double findSumAberto(Date dti, Date dtf);
 	Double findSumPago(Date dti, Date dtf);
 	Double findSumAll(int ano, int mes);
  	Parcela findByIdForn(int cod);
  	List<Parcela> findAll();
  	List<Parcela> findAllAberto();
  	List<Parcela> findAllPago();
  	List<Parcela> findPeriodoAberto();
  	List<Parcela> findPeriodoPago();
   	List<Parcela> findByIdFornecedorAberto(int cod);
   	List<Parcela> findByIdFornecedorPago(int cod);
   	List<Parcela> findByIdFornecedorNnf(Integer cod, Integer nnf);
  	List<Parcela> findByIdTipoAberto(int cod);
  	List<Parcela> findByIdTipoPago(int cod);
 }
