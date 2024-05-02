package gui.sgomodel.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import db.DbException;
import gui.sgcpmodel.entities.consulta.ParPeriodo;
import gui.sgomodel.dao.BalcaoCommitDao;
import gui.sgomodel.entities.Balcao;
import gui.sgomodel.entities.Comissao;
import gui.sgomodel.entities.Funcionario;
import gui.sgomodel.entities.Material;
import gui.sgomodel.entities.NotaFiscal;
import gui.sgomodel.entities.Receber;
import gui.sgomodel.services.BalcaoService;
import gui.sgomodel.services.ComissaoService;
import gui.sgomodel.services.FuncionarioService;
import gui.sgomodel.services.MaterialService;
import gui.sgomodel.services.NotaFiscalService;
import gui.sgomodel.services.OrcVirtualService;
import gui.sgomodel.services.ReceberService;
import gui.util.Maria;

public class BalcaoCommitDaoJDBC implements BalcaoCommitDao {

	private Connection conn;
	
	public BalcaoCommitDaoJDBC (Connection conn) {
		this.conn = conn;
	}
	
	FuncionarioService funService = new FuncionarioService();
	Funcionario fun = new Funcionario();
	OrcVirtualService virService = new OrcVirtualService();
	ComissaoService comService = new ComissaoService();
	ReceberService recService = new ReceberService();
	Receber rec = new Receber();

	LocalDate ldt = Maria.criaLocalAtual();
	int mm = Maria.mesDaData(ldt);
	int aa = Maria.anoDaData(ldt);

	@SuppressWarnings("unused")
	public void gravaBalcao(Balcao objBal, ParPeriodo objPer, NotaFiscal objNf, double maoObra, List<Material> listMat) {

		String classe = "OS Commit ";

		BalcaoService balService = new BalcaoService(); 
		NotaFiscalService nfService = new NotaFiscalService();
		MaterialService matService = new MaterialService();

		Material mat = new Material();

		try {

			conn.setAutoCommit(false);

			balService.saveOrUpdate(objBal);	
			nfService.saveOrUpdate(objNf);
			for (Material m : listMat) {
				if (m.getCodigoMat() != null) {
					mat = m;
					matService.saveOrUpdate(mat);
				}
			}
			
			if (maoObra > 0) {
				comissao(objBal, maoObra);
			}	
			
			receber(objBal, objPer);
			
			
//int b = 2;
//if (b > 1) {
//	throw new DbException("Erro tst " + classe);
//}

			conn.commit();
			
		} catch (SQLException e) {
			try {	
				conn.rollback();
				throw new DbException("bug rollback CAUSA: " + e.getMessage());
			} catch (SQLException e1) {
				throw new DbException("Erro!!! rollback " + classe + e1.getMessage());
			}
		}
		finally {
		}		
	}

	private void comissao(Balcao objBal, double maoObra) {
		Comissao com = new Comissao();
		fun = funService.findById(objBal.getFuncionario().getCodigoFun());
		try {			
			com.setNumeroCom(null);
			com.setDataCom(new Date());
			com.setFunCom(fun.getCodigoFun());
			com.setNomeFunCom(fun.getNomeFun());
			com.setCargoCom(fun.getCargoFun());
			com.setSituacaoCom(fun.getSituacaoFun());
			com.setOSCom(0);
			com.setBalcaoCom(objBal.getNumeroBal());
			com.setMesCom(mm);
			com.setAnoCom(aa);
			com.setPercentualCom(fun.getCargo().getComissaoCargo());
			com.setProdutoCom(virService.findByCustoBal(objBal.getNumeroBal()));
			com.calculoComissao();
			comService.saveOrUpdate(com);			
		} catch (DbException e1) {
			throw new DbException("Erro!!! rollback comissão, CAUSA: " + e1.getMessage());
		}
	}
	
	private void receber(Balcao objBal, ParPeriodo objPer) {
		rec.setFuncionarioRec(objBal.getFuncionario().getCodigoFun());
		rec.setClienteRec(0);
		rec.setNomeClienteRec("Balcao");
		rec.setOsRec(objBal.getNumeroBal());
		rec.setDataOsRec(objBal.getDataBal());
		rec.setPlacaRec("Balcao");
		rec.setParcelaRec(1);
		rec.setPeriodo(objPer);
		if (objBal.getPagamentoBal() == 1) {
			rec.setFormaPagamentoRec("Dinheiro");
		} else {
			if (objBal.getPagamentoBal() == 2) {
				rec.setFormaPagamentoRec("Pix");
			} else {
				if (objBal.getPagamentoBal() == 3) {
					rec.setFormaPagamentoRec("Débito");
				} else {
					if (objBal.getPagamentoBal() == 4) {
						rec.setFormaPagamentoRec("CC");					
					}
				}
			}
		}	
		rec.setDataPagamentoRec(objBal.getDataPrimeiroPagamentoBal());
		rec.setDataVencimentoRec(objBal.getDataPrimeiroPagamentoBal());
		rec.setValorRec(objBal.getTotalBal());
		rec.setJurosRec(0.00);
		rec.setDescontoRec(objBal.getDescontoBal());
		rec.setTotalRec(objBal.getTotalBal() - objBal.getDescontoBal());
		rec.setValorPagoRec(objBal.getTotalBal() - objBal.getDescontoBal());
		rec.setNumeroRec(null);
		recService.insert(rec);
	}
}
