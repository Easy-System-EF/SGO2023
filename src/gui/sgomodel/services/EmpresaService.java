package gui.sgomodel.services;

import java.util.List;

import gui.sgomodel.dao.DaoFactory;
import gui.sgomodel.dao.EmpresaDao;
import gui.sgomodel.entities.Empresa;
  
public class EmpresaService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private EmpresaDao dao = DaoFactory.createEmpresaDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public void insertOrUpdate(Empresa obj) {
		if (obj.getNumeroEmp() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	} 

	public Empresa findById(Integer cod) {
   		return dao.findById(cod);
	}

	public List<Empresa> findAll() {
   		return dao.findAll();
	}
}
