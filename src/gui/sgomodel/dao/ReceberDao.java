package gui.sgomodel.dao;

import java.util.Date;
import java.util.List;

import gui.sgomodel.entities.Receber;

public interface ReceberDao {

	void insert(Receber obj);
	void update(Receber obj);
 	void removeOS(int numOs);
   	List<Receber> findByAllOs(Integer os);
  	List<Receber> findAllAberto();
  	List<Receber> findAllPago();
  	Double sumPagoCli(int cod);
  	Double findPagoOsMes(Date dti, Date dtf);
  	Double findPagoBalMes(Date dti, Date dtf);
  	Double findAbertoOsMes(Date dti, Date dtf);
  	Double findAbertoBalMes(Date dti, Date dtf);
  	List<Receber> findPeriodoAberto();
  	List<Receber> findPeriodoPago();
   	List<Receber> findByIdClienteAberto(Integer cod);
   	List<Receber> findByIdClientePago(Integer cod);
   	List<Receber> findAll();
 }
