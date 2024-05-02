package gui.sgomodel.services;

import java.util.Date;
import java.util.List;

import gui.sgomodel.dao.DaoFactory;
import gui.sgomodel.dao.ReceberDao;
import gui.sgomodel.entities.Receber;
 
public class ReceberService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private ReceberDao dao = DaoFactory.createReceberDao();

//    criar no parcelalist uma dependencia na parc controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Receber> findAllId() {
   		return dao.findAllId();
	} 
	
	public List<Receber> findByAllOs(Integer os) {
   		return dao.findByAllOs(os);
	} 
	
	public List<Receber> findByAllBal(Integer bal) {
   		return dao.findByAllBal(bal);
	} 
	
	public List<Receber> findAllAberto() {
   		return dao.findAllAberto();
	} 
	
	public List<Receber> findAllPago(Date dti, Date dtf) {
   		return dao.findAllPago(dti, dtf);
	} 
	
	public Double sumPagoCli(int cod) {
   		return dao.sumPagoCli(cod);
	} 
	
	public Double findPagoOsMes(Date dti, Date dtf) {
   		return dao.findPagoOsMes(dti, dtf);
	} 
	
	public Double findPagoBalMes(Date dti, Date dtf) {
   		return dao.findPagoBalMes(dti, dtf);
	} 
	
	public Double findAbertoOsMes(Date dti, Date dtf) {
   		return dao.findAbertoOsMes(dti, dtf);
	} 
	
	public Double findAbertoBalMes(Date dti, Date dtf) {
   		return dao.findAbertoBalMes(dti, dtf);
	} 
	
	public List<Receber> findPeriodoAberto() {
   		return dao.findPeriodoAberto();
	} 

	public List<Receber> findPeriodoPago() {
   		return dao.findPeriodoPago();
	} 

	public List<Receber> findByIdClienteAberto(Integer cod) {
   		return dao.findByIdClienteAberto(cod);
	} 

	public List<Receber> findByIdClientePago(Integer cod) {
   		return dao.findByIdClientePago(cod);
	} 
	
	public List<Receber> findAll() {
   		return dao.findAll();
	} 
	
	public List<Receber> findAllBalcao() {
   		return dao.findAllBalcao();
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo n�o existe insere, se existe altera 
	public void insert(Receber obj) {
		if (obj.getNumeroRec() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
 	}

	public void insertBackUp(Receber obj) {
		dao.insertBackUp(obj);
 	}

	public void update(Receber obj) {
		dao.update(obj);
 	}

// removendo
	public void removeOS(int numOs) {
			dao.removeOS(numOs);
	}
}