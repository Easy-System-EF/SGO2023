package gui.sgo;

import java.io.Serializable;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listerneres.DataChangeListener;
import gui.sgcpmodel.services.ParcelaService;
import gui.sgomodel.entities.Balcao;
import gui.sgomodel.entities.FechamentoAnual;
import gui.sgomodel.entities.OrdemServico;
import gui.sgomodel.entities.Receber;
import gui.sgomodel.services.AdiantamentoService;
import gui.sgomodel.services.BalcaoService;
import gui.sgomodel.services.ComissaoService;
import gui.sgomodel.services.FechamentoAnualService;
import gui.sgomodel.services.OrcVirtualService;
import gui.sgomodel.services.OrdemServicoService;
import gui.sgomodel.services.ReceberService;
import gui.util.DataStatic;
import gui.util.Mascaras;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class FechamentoAnualConsultaFormController implements Initializable, Serializable {

	private static final long serialVersionUID = 1L;

	private FechamentoAnual entity;
	
	/*
	 * dependencia service com metodo set
	 */
	private FechamentoAnualService service;
	private AdiantamentoService adService;
	private ComissaoService comissaoService;
	private OrdemServicoService osService;
	private OrcVirtualService virService;
	private ParcelaService parService;
	private ReceberService recService;
	private BalcaoService balService;
	
	private OrdemServico os;
	private Balcao bal;
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

//auxiliar
	String classe = "Fechamento ano ";
	int mm = 01;
	int aa = 0;
	int dd = 01;
	int df = 0;
	Date dataInicialDespAberto = new Date();
	Date dataFinalDespAberto = new Date();
	Date dataInicialDespPago = new Date();
	Date dataFinalDespPago = new Date();
	Date dataInicialRecAberto = new Date();
	Date dataFinalRecAberto = new Date();
	Date dataInicialRecPago = new Date();
	Date dataFinalRecPago = new Date();
	Date dataInicialRecebidoMes = new Date();
	Date dataFinalRecebidoMes = new Date();

	Calendar cal = Calendar.getInstance();

	public void setDadosEntityes(FechamentoAnual entity) {
		this.entity = entity;
	}

	// * metodo set /p service
	public void setServiceAll(FechamentoAnualService service, AdiantamentoService adService, ComissaoService comissaoService, 
			OrdemServicoService osService, OrcVirtualService virService, ParcelaService parService, ReceberService recService,
			BalcaoService balService) {
		this.service = service;
		this.adService = adService;
		this.comissaoService = comissaoService;
		this.osService = osService;
		this.virService = virService;
		this.parService = parService;
		this.recService = recService;
		this.balService = balService;
	}

//  * o controlador tem uma lista de eventos q permite distribui��o via metodo abaixo
// * recebe o evento e inscreve na lista
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtOk() throws ParseException {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula");
		} else {	
			montaDadosAnual();
			notifyDataChangeListerners();
		}
	}	

// *   um for p/ cada listener da lista, eu aciono o metodo onData no DataChangListner...   
	private void notifyDataChangeListerners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	private void montaDadosAnual() {
 		if (adService == null) {
			throw new IllegalStateException("Serviço Adiantamento está vazio");
 		}
 		if (comissaoService == null) {
			throw new IllegalStateException("Serviço Comissão está vazio"); 			
 		}
 		if (service == null) { 
			throw new IllegalStateException("Serviço DadosFechamentoAnual está vazio");
 		}
 		if (osService == null) {
			throw new IllegalStateException("Serviço OS está vazio");
 		}
 		if (virService == null) {
			throw new IllegalStateException("Serviço Virtual está vazio");
 		} 
 		
 		service.zeraAll();
 		
		LocalDate ld = DataStatic.criaLocalAtual();
		aa = DataStatic.anoDaData(ld);
		
  		String tabelaMeses = "Jan, Fev, Mar, Abr, Mai, Jun, Jul, Ago, Set, Out, Nov, Dez";
  		String[] tabMes = tabelaMeses.split(",");
  		
  		Calendar cal = Calendar.getInstance();

		Double sumAcumulado = 0.00;
		Double sumVlrOs = 0.00;
		Double sumVlrBal = 0.00;
		double sumCst = 0.00;
		double sumCom = 0.00;
		double sumRes = 0.00;
		double vlrOs = 0.00;
		double vlrBal = 0.00;
		double vlrCusto = 0.00;
		double vlrComissao = 0.00;
		double vlrResultado = 0.00;

		LocalDate data = DataStatic.criaAnoMesDia(2001, 01, 01);
		Date dtiAnt = DataStatic.localParaDateSdfAno(data);
		int aaAnt = aa -1;
		data = DataStatic.criaAnoMesDia(aaAnt, 12, 31);
		Date dtfAnt = DataStatic.localParaDateSdfAno(data);
		double vlrAntPago = parService.findSumPago(dtiAnt, dtfAnt);		
		double vlrOsRec = recService.findPagoOsMes(dtiAnt, dtfAnt);
		double vlrBalRec = recService.findPagoBalMes(dtiAnt, dtfAnt);
		sumAcumulado = (vlrOsRec + vlrBalRec) - (vlrAntPago);
		Integer mesPag = 0;
		Integer anoPag = 0;
		Integer mesVen = 0;
		Integer anoVen = 0;
		
		entity.setDoctoAnual(null);
		entity.setValorAnual(null);
		entity.setValorCustoAnual(null);
		entity.setValorComissaoAnual("    Saldo ");
		entity.setValorResultadoAnual("anterior=>");
		try {
			entity.setValorAcumuladoAnual(Mascaras.formataValor(sumAcumulado));
		} catch (ParseException e1) {
			e1.printStackTrace();
		} 
		service.insert(entity);
		
		try { 	
			while(mm < 13) {
				LocalDate dt1 = DataStatic.criaAnoMesDia(aa, mm, 20);
				if (mm == 01) {
					dt1 = DataStatic.criaAnoMesDia(2001, 01, 01);
					dataInicialDespAberto = DataStatic.localParaDateSdfAno(dt1);
					dataInicialRecAberto = DataStatic.localParaDateSdfAno(dt1);
				} else {
					dt1 = DataStatic.criaAnoMesDia(aa, mm, dd);
					dataInicialDespAberto = DataStatic.localParaDateSdfAno(dt1);
					dataInicialRecAberto = DataStatic.localParaDateSdfAno(dt1);
				}
				df = DataStatic.ultimoDiaMes(dt1);
				dt1 = DataStatic.criaAnoMesDia(aa, mm, df);
				dataFinalDespAberto = DataStatic.localParaDateSdfAno(dt1);
				dataFinalRecAberto = DataStatic.localParaDateSdfAno(dt1);

				dt1 = DataStatic.criaAnoMesDia(aa, mm, 01);
				dataInicialDespPago = DataStatic.localParaDateSdfAno(dt1);
				dataInicialRecPago = DataStatic.localParaDateSdfAno(dt1);

				dataInicialRecebidoMes = DataStatic.localParaDateSdfAno(dt1);

				dt1 = DataStatic.criaAnoMesDia(aa, mm, df);
				dataFinalDespPago = DataStatic.localParaDateSdfAno(dt1);
				dataFinalRecPago = DataStatic.localParaDateSdfAno(dt1);

				dataFinalRecebidoMes = DataStatic.localParaDateSdfAno(dt1);
				
				entity = new FechamentoAnual();
				entity.setMesAnual(tabMes[mm - 1]);
				
				List<Receber> listOs = recService.findAll();
				listOs.removeIf(r -> r.getPlacaRec().contains("Balcão"));
				listOs.removeIf(r -> r.getPlacaRec().contains("Balcao"));
				listOs.removeIf(r -> r.getDataPagamentoRec().before(dataInicialRecebidoMes));
				listOs.removeIf(r -> r.getDataPagamentoRec().after(dataFinalRecebidoMes));
				for (Receber recOs : listOs) {
					cal.setTime(recOs.getDataPagamentoRec());
					mesPag = 1 + cal.get(Calendar.MONTH);
					anoPag = cal.get(Calendar.YEAR);
					cal.setTime(recOs.getDataVencimentoRec());
					mesVen = 1 + cal.get(Calendar.MONTH);
					anoVen = cal.get(Calendar.YEAR);
					os = null;

					if (anoPag.equals(aa) || anoVen.equals(aa)) {
						if (os == null) {
							os = osService.findById(recOs.getOsRec());
							sumCst = virService.findByCustoOrc(os.getOrcamentoOS());
							sumCom = comissaoService.oSSumComissao(mm, aa, os.getNumeroOS());	
							if (mesPag == mm && recOs.getValorPagoRec() > 0) {
								sumVlrOs = recOs.getValorPagoRec();
								if (recOs.getParcelaRec() == 1) {
									sumRes = sumVlrOs - (sumCst + sumCom);
									sumAcumulado += recOs.getValorPagoRec(); 
								} 
								if (recOs.getValorPagoRec() > 0 && recOs.getParcelaRec() > 1) {
									sumAcumulado += recOs.getValorPagoRec(); 
									sumRes = recOs.getValorPagoRec();
									sumCst = 0.0;
									sumCom = 0.0;
								}
							}	
							if (anoVen == aa && recOs.getValorPagoRec() == 0.0) {
								if (mesVen == mm) {
									if (recOs.getParcelaRec() > 1) {
										sumCst = 0.0;
										sumRes = 0.0;
									}										
								}
							}
							vlrOs += sumVlrOs;
							vlrCusto += sumCst;
							vlrComissao += sumCom;
							vlrResultado += sumRes;
							sumVlrOs = 0.0;
							sumCst = 0.0;
							sumCom = 0.0;
							sumRes = 0.0;
						}
					}
				}	
				entity.setDoctoAnual("OS");
				entity.setValorAnual(Mascaras.formataValor(vlrOs));
				entity.setValorCustoAnual(Mascaras.formataValor(vlrCusto));
				entity.setValorComissaoAnual(Mascaras.formataValor(vlrComissao));
				entity.setValorResultadoAnual(Mascaras.formataValor(vlrResultado));
				entity.setValorAcumuladoAnual(Mascaras.formataValor(sumAcumulado));
				service.insert(entity);
				
				vlrOs = 0.0;
				vlrBal = 0.0;
				vlrCusto = 0.0;
				vlrComissao = 0.0;
				vlrResultado = 0.0;
				entity.setMesAnual("");
					
				List<Receber> listBal = recService.findAllBalcao();
				listBal.removeIf(y -> y.getDataPagamentoRec().before(dataInicialRecebidoMes));
				listBal.removeIf(y -> y.getDataPagamentoRec().after(dataFinalRecebidoMes));
				for (Receber recBal : listBal) {
					cal.setTime(recBal.getDataPagamentoRec());
					mesPag = 1 + cal.get(Calendar.MONTH);
					anoPag = cal.get(Calendar.YEAR);
					cal.setTime(recBal.getDataVencimentoRec());
					mesVen = 1 + cal.get(Calendar.MONTH);
					anoVen = cal.get(Calendar.YEAR);
					bal = null;

					if (anoPag.equals(aa) || anoVen.equals(aa)) {
						if (bal == null) {
							bal = balService.findById(recBal.getOsRec());
							sumCst = virService.findByCustoOrc(bal.getNumeroBal());
							sumCom = comissaoService.balcaoSumComissao(mm, aa, bal.getNumeroBal());
						
							if (mesPag == mm && recBal.getValorPagoRec() > 0) {
								sumVlrBal = recBal.getValorPagoRec();
								if (recBal.getParcelaRec() == 1) {
									sumRes = sumVlrBal - (sumCst + sumCom);
									sumAcumulado += recBal.getValorPagoRec(); 
								} 
								if (recBal.getValorPagoRec() > 0 && recBal.getParcelaRec() > 1) {
									sumAcumulado += recBal.getValorPagoRec(); 
									sumRes = recBal.getValorPagoRec();
									sumCst = 0.0;
									sumCom = 0.0;
								}
							}	
							if (anoPag == 0 && anoVen == aa) {
								if (mesVen == mm) {
									if (recBal.getParcelaRec() == 1) {
										sumRes -= sumCst;
									} else {
										sumCst = 0.0;
										sumRes = 0.0;
									}										
								}
							}	
						}
						vlrBal += sumVlrBal;
						vlrCusto += sumCst;
						vlrComissao += sumCom;
						vlrResultado += sumRes;
						sumVlrBal = 0.0;
						sumCst = 0.0;
						sumCom = 0.0;
						sumRes = 0.0;
					}		
				}	
				entity.setDoctoAnual("Balcão");
				entity.setValorAnual(Mascaras.formataValor(vlrBal));
				entity.setValorCustoAnual(Mascaras.formataValor(vlrCusto));
				entity.setValorComissaoAnual(Mascaras.formataValor(vlrComissao));
				entity.setValorResultadoAnual(Mascaras.formataValor(vlrResultado));
				entity.setValorAcumuladoAnual(Mascaras.formataValor(sumAcumulado));
				service.insert(entity);
				
				vlrBal = 0.0;
				vlrCusto = 0.0;
				vlrComissao = 0.0;
				vlrResultado = 0.0;
				entity.setMesAnual("");

				Double sumPago = -1 * (parService.findSumPago(dataInicialDespPago, dataFinalDespPago));
				sumRes = sumPago;
				sumAcumulado += sumRes;
				entity.setDoctoAnual("Desp Paga");
				entity.setValorAnual(Mascaras.formataValor(sumPago));
				entity.setValorCustoAnual(Mascaras.formataValor(0.00));
				entity.setValorComissaoAnual(Mascaras.formataValor(0.00));
				entity.setValorResultadoAnual(Mascaras.formataValor(sumRes));
				entity.setValorAcumuladoAnual(Mascaras.formataValor(sumAcumulado));	
				sumRes = 0.0;
				service.insert(entity);
				mm += 1;	
			}	
		}
		catch (ParseException e) {
			throw new DbException(e.getMessage());
		}
	}

	/*
	 * o contrainsts (confere) impede alfa em cpo numerico e delimita tamanho
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		updateFormData();
	}	

	/*
	 * transforma string da tela p/ o tipo no bco de dados
	 */
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entidade esta nula");
		}
// se for uma inclusao, vai posicionar no 1o depto//tipo (First)	
	}
}