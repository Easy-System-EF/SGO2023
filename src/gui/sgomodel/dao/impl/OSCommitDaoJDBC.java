package gui.sgomodel.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import db.DbException;
import gui.sgcpmodel.entities.consulta.ParPeriodo;
import gui.sgomodel.dao.OSCommitDao;
import gui.sgomodel.entities.Adiantamento;
import gui.sgomodel.entities.Material;
import gui.sgomodel.entities.NotaFiscal;
import gui.sgomodel.entities.OrcVirtual;
import gui.sgomodel.entities.Orcamento;
import gui.sgomodel.entities.OrdemServico;
import gui.sgomodel.entities.Receber;
import gui.sgomodel.entities.ReposicaoVeiculo;
import gui.sgomodel.entities.Veiculo;
import gui.sgomodel.services.AdiantamentoService;
import gui.sgomodel.services.MaterialService;
import gui.sgomodel.services.NotaFiscalService;
import gui.sgomodel.services.OrcVirtualService;
import gui.sgomodel.services.OrcamentoService;
import gui.sgomodel.services.OrdemServicoService;
import gui.sgomodel.services.ReceberService;
import gui.sgomodel.services.ReposicaoVeiculoService;
import gui.sgomodel.services.VeiculoService;
import gui.util.CalculaParcela;
import gui.util.DataStatic;

public class OSCommitDaoJDBC implements OSCommitDao {

	private Connection conn;
	
	public OSCommitDaoJDBC (Connection conn) {
		this.conn = conn;
	}
	
	OrcVirtualService virService = new OrcVirtualService();
	ReceberService recService = new ReceberService();
	ReposicaoVeiculoService repService = new ReposicaoVeiculoService();
	Receber rec = new Receber();
	ReposicaoVeiculo rep = new ReposicaoVeiculo();

	@SuppressWarnings("unused")
	public void gravaOS(OrdemServico objOs, Orcamento objOrc, ParPeriodo objPer, NotaFiscal objNf, Veiculo objVei, 
			Adiantamento objAdi, List<Material> listMat) {

		String classe = "OS Commit ";

		OrdemServicoService osService = new OrdemServicoService(); 
		OrcamentoService orcService = new OrcamentoService(); 
		NotaFiscalService nfService = new NotaFiscalService();
		VeiculoService veiService = new VeiculoService();
		MaterialService matService = new MaterialService();
		AdiantamentoService adiService = new AdiantamentoService();

		Material mat = new Material();

		try {
			conn.setAutoCommit(false);
			osService.saveOrUpdate(objOs);	
			objOrc.setOsOrc(objOs.getNumeroOS());
			orcService.saveOrUpdate(objOrc);
			
			objNf.setOsNF(objOs.getNumeroOS());
			nfService.saveOrUpdate(objNf);
			
			veiService.saveOrUpdate(objVei);
			
			for (Material m : listMat) {
				if (m.getCodigoMat() != null) {
					mat = m;
					matService.saveOrUpdate(mat);
				}
			}
			
			if (objAdi.getCodigoFun() == null) {
				int nada = 0;
			} else {
				objAdi.setOsAdi(objOs.getNumeroOS());
				objAdi.setDataAdi(new Date());
				adiService.saveOrUpdate(objAdi);
			}	
			
			receber(objOrc, objOs, objPer);
			
			reposicao(objOs, objOrc);

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

	private void receber(Orcamento objOrc, OrdemServico objOs, ParPeriodo objPer) {
		try {
			rec.setFuncionarioRec(objOrc.getFuncionario().getCodigoFun());
			rec.setClienteRec(objOrc.getCliente().getCodigoCli());
			rec.setNomeClienteRec(objOrc.getCliente().getNomeCli());
			rec.setOsRec(objOs.getNumeroOS());
			rec.setDataOsRec(objOs.getDataOS());
			rec.setPlacaRec(objOs.getPlacaOS());
			rec.setPeriodo(objPer);
			if (objOs.getPagamentoOS() == 1) {
				rec.setFormaPagamentoRec("Dinheiro");
			} else {
				if (objOs.getPagamentoOS() == 2) {
					rec.setFormaPagamentoRec("Pix");
				} else {
					if (objOs.getPagamentoOS() == 3) {
						rec.setFormaPagamentoRec("Débito");
					} else {
						if (objOs.getPagamentoOS() == 4) {
							rec.setFormaPagamentoRec("CC");
						}
					}
				}
			}
			for (int i = 1; i < objOs.getParcelaOS() + 1; i++) {
				rec.setParcelaRec(i);
				if (objOs.getPrazoOS() == 1) {
					rec.setDataVencimentoRec(objOs.getDataPrimeiroPagamentoOS());
					rec.setDataPagamentoRec(objOs.getDataPrimeiroPagamentoOS());
					rec.setValorRec(objOs.getValorOS());
					rec.setJurosRec(0.00);
					rec.setDescontoRec(0.00);
					rec.setTotalRec(objOs.getValorOS());
					rec.setValorPagoRec(0.00);
					if (rec.getDataVencimentoRec().equals(objOs.getDataPrimeiroPagamentoOS())) {
						rec.setValorPagoRec(objOs.getValorOS());
					}	
				}
				if (objOs.getPrazoOS() > 1 ) {
					rec.setDataVencimentoRec(CalculaParcela
							.CalculaVencimentoDia(objOs.getDataPrimeiroPagamentoOS(), i, objOs.getPrazoOS()));
					rec.setValorRec(CalculaParcela.calculaParcelas(objOs.getValorOS(), objOs.getParcelaOS(), i));
					rec.setDataPagamentoRec(rec.getDataVencimentoRec());
					rec.setJurosRec(0.00);
					rec.setDescontoRec(0.00);
					rec.setTotalRec(0.00);
					rec.setValorPagoRec(0.00);
				}	
				rec.setNumeroRec(null);
				recService.insert(rec);
			}	
		} catch (DbException e1) {
			throw new DbException("Erro!!! rollback receber, CAUSA: " + e1.getMessage());
		}
	}
	
	private void reposicao(OrdemServico objOs, Orcamento objOrc) {
		List<OrcVirtual> listVir = virService.findByOrcto(objOrc.getNumeroOrc());
		try {
			for (int i = 0; i < listVir.size(); i++) {
				if (listVir.get(i).getNumeroOrcVir().equals(objOs.getOrcamentoOS())) {
					if (listVir.get(i).getMaterial().getVidaKmMat() > 0
							|| listVir.get(i).getMaterial().getVidaMesMat() > 0) {
						rep.setNumeroRep(null);
						rep.setOsRep(objOs.getNumeroOS());
						rep.setDataRep(objOs.getDataOS());
						rep.setPlacaRep(objOs.getPlacaOS());
						rep.setClienteRep(objOs.getClienteOS());
						rep.setDddClienteRep(objOrc.getCliente().getDdd01Cli());
						rep.setTelefoneClienteRep(objOrc.getCliente().getTelefone01Cli());
						rep.setCodigoMaterialRep(listVir.get(i).getMaterial().getCodigoMat());
				
						if (rep.getCodigoMaterialRep() != null) {
							rep.setMaterialRep(listVir.get(i).getMaterial().getNomeMat());
							rep.setKmTrocaRep(objOrc.getKmFinalOrc());
							if (listVir.get(i).getMaterial().getVidaKmMat() > 0) {
								rep.setProximaKmRep(
										objOrc.getKmFinalOrc() + listVir.get(i).getMaterial().getVidaKmMat());
							} else {
								rep.setProximaKmRep(0);
							}
							if (listVir.get(i).getMaterial().getVidaMesMat() > 0) {
								rep.setProximaDataRep(DataStatic.somaMesDate(
									objOs.getDataOS(), listVir.get(i).getMaterial().getVidaMesMat()));
							} else {
								rep.setProximaDataRep(objOs.getDataOS());
							}
							repService.insert(rep);
						}	
					}
				}
			}		
		} catch (DbException e1) {
			throw new DbException("Erro!!! rollback reposição, CAUSA: " + e1.getMessage());
		}
	}	
}
