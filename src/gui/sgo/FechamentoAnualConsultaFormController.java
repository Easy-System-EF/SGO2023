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
import gui.sgomodel.entities.FechamentoAnual;
import gui.sgomodel.entities.OrdemServico;
import gui.sgomodel.entities.Receber;
import gui.sgomodel.services.AdiantamentoService;
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
	private OrdemServicoService osService;
	private OrcVirtualService virService;
	private ParcelaService parService;
	private ReceberService recService;
	
	private OrdemServico os;
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
	Date dataInicialRec = new Date();
	Date dataFinalRec = new Date();

	Calendar cal = Calendar.getInstance();

	public void setDadosEntityes(FechamentoAnual entity) {
		this.entity = entity;
	}

	// * metodo set /p service
	public void setServiceAll(FechamentoAnualService service, AdiantamentoService adService, 
			OrdemServicoService osService, OrcVirtualService virService, ParcelaService parService, ReceberService recService) {
		this.service = service;
		this.adService = adService;
		this.osService = osService;
		this.virService = virService;
		this.parService = parService;
		this.recService = recService;
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
//			Utils.currentStage().close();
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
 		if (service == null) { 
			throw new IllegalStateException("Serviço DadosFechamentoAnual está vazio");
 		}
 		if (osService == null) {
			throw new IllegalStateException("Serviço OS está vazio");
 		}
 		if (virService == null) {
			throw new IllegalStateException("Serviço Virtual está vazio");
 		} 
		LocalDate ld = DataStatic.criaLocalAtual();
		aa = DataStatic.anoDaData(ld);
		
  		String tabelaMeses = "Jan, Fev, Mar, Abr, Mai, Jun, Jul, Ago, Set, Out, Nov, Dez";
  		String[] tabMes = tabelaMeses.split(",");
  		
  		Calendar cal = Calendar.getInstance();

		Double sumOs = 0.00;
		Double sumCusto = 0.00;
		Double sumComissao = 0.00;
		Double sumAcumulado = 0.00;
		int mesPag = 0;
		int anoPag = 0;
		int mesOs = 0;
		int anoOs = 0;
		int mesBal = 0;
		int anoBal = 0;
		@SuppressWarnings("unused")
		int numBal = 0;

		LocalDate data = DataStatic.criaAnoMesDia(2001, 01, 01);
		Date dtiAnt = DataStatic.localParaDateSdfAno(data);
		int aaAnt = aa -1;
		data = DataStatic.criaAnoMesDia(aaAnt, 12, 31);
		Date dtfAnt = DataStatic.localParaDateSdfAno(data);
		double vlrAntPago = parService.findSumPago(dtiAnt, dtfAnt);		
		double vlrOsRec = recService.findPagoOsMes(dtiAnt, dtfAnt);
		double vlrBalRec = recService.findPagoBalMes(dtiAnt, dtfAnt);
		sumAcumulado = (vlrOsRec + vlrBalRec) - (vlrAntPago);
		
		entity.setDoctoAnual(null);
		entity.setValorAnual(null);
		entity.setValorCustoAnual(null);
		entity.setValorComissaoAnual("Resultado ");
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
				df = DataStatic.ultimoDiaMes(dt1);

				if (mm == 01) {
					dt1 = DataStatic.criaAnoMesDia(2001, 01, 01);
					dataInicialDespAberto = DataStatic.localParaDateSdfAno(dt1);
				} else {
					dt1 = DataStatic.criaAnoMesDia(aa, mm, dd);
					dataInicialDespAberto = DataStatic.localParaDateSdfAno(dt1);
				}
				dt1 = DataStatic.criaAnoMesDia(aa, mm, df);
				dataFinalDespAberto = DataStatic.localParaDateSdfAno(dt1);

				dt1 = DataStatic.criaAnoMesDia(aa, mm, dd);
				dataInicialDespPago = DataStatic.localParaDateSdfAno(dt1);
				
				dt1 = DataStatic.criaAnoMesDia(aa, mm, df);
				dataFinalDespPago = DataStatic.localParaDateSdfAno(dt1);
				
				if (mm == 01) {
					dt1 = DataStatic.criaAnoMesDia(2001, 01, 01);
					dataInicialRecAberto = DataStatic.localParaDateSdfAno(dt1);
				} else {	
					dt1 = DataStatic.criaAnoMesDia(aa, mm, dd);
					dataInicialRecAberto = DataStatic.localParaDateSdfAno(dt1);
				}		
				dt1 = DataStatic.criaAnoMesDia(aa, mm, df);
				dataFinalRecAberto = DataStatic.localParaDateSdfAno(dt1);
				
				dt1 = DataStatic.criaAnoMesDia(aa, mm, dd);
				dataInicialRecPago = DataStatic.localParaDateSdfAno(dt1);
				
				dt1 = DataStatic.criaAnoMesDia(aa, mm, df);
				dataFinalRecPago = DataStatic.localParaDateSdfAno(dt1);
			
				dt1 = DataStatic.criaAnoMesDia(aa, mm, dd);
				dataInicialRec = DataStatic.localParaDateSdfAno(dt1);
				
				dt1 = DataStatic.criaAnoMesDia(aa, mm, df);
				dataFinalRec = DataStatic.localParaDateSdfAno(dt1);
			
				entity = new FechamentoAnual();
				entity.setMesAnual(tabMes[mm - 1]);
				
				List<Receber> listRec = recService.findAll();
				listRec.removeIf(r -> r.getPlacaRec().equals("Balcão"));
				listRec.removeIf(r -> r.getPlacaRec().equals("Balcao"));
				
				Double sumVlrOs = 0.00;
				double sumCst = 0.00;
				double sumCom = 0.00;
				double sumRes = 0.00;
				
				for (Receber r : listRec) {
					cal.setTime(r.getDataOsRec());
					mesOs = 1 + cal.get(Calendar.MONTH);
					anoOs = cal.get(Calendar.YEAR);
					cal.setTime(r.getDataPagamentoRec());
					mesPag = 1 + cal.get(Calendar.MONTH);
					anoPag = cal.get(Calendar.YEAR);
					if (r.getParcelaRec() == 1 && mesOs == mm && anoOs == aa) {
						os = osService.findById(r.getOsRec());
						sumCst += virService.findByCustoOrc(os.getOrcamentoOS());
						sumCom += adService.findByTotalComOS(os.getNumeroOS());
					}
					if (mesPag == mm && anoPag == aa ) {
						sumVlrOs += r.getValorPagoRec();
					}	
				} 
				sumOs += sumVlrOs;
				sumCusto += sumCst; 
				sumComissao += sumCom;
				sumRes = sumVlrOs - (sumCst + sumCom);
				sumAcumulado += sumRes;
				entity.setDoctoAnual("OS");
				entity.setValorAnual(Mascaras.formataValor(sumVlrOs));
				entity.setValorCustoAnual(Mascaras.formataValor(sumCst));
				entity.setValorComissaoAnual(Mascaras.formataValor(sumCom));
				entity.setValorResultadoAnual(Mascaras.formataValor(sumRes));
				entity.setValorAcumuladoAnual(Mascaras.formataValor(sumAcumulado));						
				sumVlrOs = 0.00;
				sumCst = 0.00;
				sumCom = 0.00;
				sumRes = 0.00;
				service.insert(entity);
	
// monta dados balc�o		
				List<Receber> listRecBal = recService.findAll();
				for (Receber r1 : listRecBal) {
					if (r1.getPlacaRec().equals("Balcao") || r1.getPlacaRec().equals("Balcão")) {
						numBal = 1;
					} else {
						r1.setPlacaRec("del");
					}	
				}
				
				listRecBal.removeIf(r -> r.getPlacaRec().equals("del"));
				
				Double sumVlrBal = 0.00;
				sumCst = 0.00;

				for (Receber r : listRecBal) {
					cal.setTime(r.getDataOsRec());
					mesBal = 1 + cal.get(Calendar.MONTH);
					anoBal = cal.get(Calendar.YEAR);
					cal.setTime(r.getDataPagamentoRec());
					mesPag = 1 + cal.get(Calendar.MONTH);
					anoPag = cal.get(Calendar.YEAR);					
					if (mesBal == mm && anoBal == aa) {
						sumCst += virService.findByCustoBal(r.getOsRec());
						sumCom += adService.findByTotalComBal(r.getOsRec());
					}
					if (mesPag == mm && anoPag == aa ) {
						sumVlrBal += r.getValorPagoRec();
					}
				} 
				sumRes = sumVlrBal - (sumCst + sumCom);
				sumCusto += sumCst;
				sumAcumulado += sumRes;
				entity.setMesAnual(null);
				entity.setDoctoAnual("Balcão");
				entity.setValorAnual(Mascaras.formataValor(sumVlrBal));
				entity.setValorCustoAnual(Mascaras.formataValor(sumCst));
				entity.setValorComissaoAnual(Mascaras.formataValor(sumCom));
				entity.setValorResultadoAnual(Mascaras.formataValor(sumRes));
				entity.setValorAcumuladoAnual(Mascaras.formataValor(sumAcumulado));						
				sumVlrBal = 0.00;
				sumCst = 0.00;
				sumRes = 0.00;
				sumCom = 0.00;
				service.insert(entity);
					
				Double sumAberto = parService.findSumAberto(dataInicialDespAberto, dataFinalDespAberto);
				if (sumAberto > 0.00) {
					sumRes = sumAcumulado - sumAberto ;
				}	
				sumAcumulado -= sumAberto;
				entity.setDoctoAnual("Desp em Aberto");
				entity.setValorAnual(Mascaras.formataValor(sumAberto));
				entity.setValorCustoAnual(Mascaras.formataValor(0.00));
				entity.setValorComissaoAnual(Mascaras.formataValor(0.00));
				entity.setValorResultadoAnual(Mascaras.formataValor(0.00));
				entity.setValorAcumuladoAnual(Mascaras.formataValor(sumAcumulado));						
				sumRes = 0.00;
				service.insert(entity);

				Double sumPago = parService.findSumPago(dataInicialDespPago, dataFinalDespPago);
				if (sumPago > 0.00) {
					sumRes = sumAcumulado - sumPago;
				}
				sumAcumulado -= sumPago;
				entity.setDoctoAnual("Desp Paga");
				entity.setValorAnual(Mascaras.formataValor(sumPago));
				entity.setValorCustoAnual(Mascaras.formataValor(0.00));
				entity.setValorComissaoAnual(Mascaras.formataValor(0.00));
				entity.setValorResultadoAnual(Mascaras.formataValor(0.00));
				entity.setValorAcumuladoAnual(Mascaras.formataValor(sumAcumulado));	
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