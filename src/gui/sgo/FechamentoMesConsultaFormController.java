package gui.sgo;

import java.io.Serializable;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import gui.listerneres.DataChangeListener;
import gui.sgcpmodel.services.CompromissoService;
import gui.sgcpmodel.services.ParPeriodoService;
import gui.sgcpmodel.services.ParcelaService;
import gui.sgcpmodel.services.TipoConsumoService;
import gui.sgomodel.entities.Anos;
import gui.sgomodel.entities.Balcao;
import gui.sgomodel.entities.FechamentoMes;
import gui.sgomodel.entities.Funcionario;
import gui.sgomodel.entities.Meses;
import gui.sgomodel.entities.OrdemServico;
import gui.sgomodel.entities.Receber;
import gui.sgomodel.services.AdiantamentoService;
import gui.sgomodel.services.AnosService;
import gui.sgomodel.services.BalcaoService;
import gui.sgomodel.services.ComissaoService;
import gui.sgomodel.services.FechamentoMesService;
import gui.sgomodel.services.FuncionarioService;
import gui.sgomodel.services.MesesService;
import gui.sgomodel.services.OrcVirtualService;
import gui.sgomodel.services.OrdemServicoService;
import gui.sgomodel.services.ReceberService;
import gui.util.Alerts;
import gui.util.DataStatic;
import gui.util.Mascaras;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import model.exception.ValidationException;

public class FechamentoMesConsultaFormController implements Initializable, Serializable {

	private static final long serialVersionUID = 1L;

	private FechamentoMes entity;
	/*
	 * dependencia service com metodo set
	 */
	private FechamentoMesService service;
	private AdiantamentoService adService;
	private MesesService mesService;
	private AnosService anoService;
	private OrdemServicoService osService;
	private OrcVirtualService virService;
	private FuncionarioService funService;
	private BalcaoService balService;
	private ParcelaService parService;
	private ReceberService recService;
	private ComissaoService comissaoService;
	private Meses objMes;
	private Anos objAno;
	private Funcionario fun;
	private OrdemServico os;
	private Balcao bal;
	private FechamentoMes dados = new FechamentoMes();

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private ComboBox<Meses> comboBoxMeses;

	@FXML
	private ComboBox<Anos> comboBoxAnos;

	@FXML
	private Button btOk;

	@FXML
	private Button btCancel;

	@FXML
	private Label labelErrorComboBoxMeses;

	@FXML
	private Label labelErrorComboBoxAnos;

	private ObservableList<Meses> obsListMes;
	private ObservableList<Anos> obsListAno;

//auxiliar
	String classe = "Fechamento mes ";
	Double sumFolha = 0.00;
	int mm = 0;
	int aa = 0;
	int dd = 0;
	int df = 0;
	private final int ddInicial = 01;
	private final int mmInicial = 01;
	private final int aaInicial = 2000;
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
	Date dataInicialMes = new Date();
	Date dataFinalMes = new Date();

	Calendar cal = Calendar.getInstance();

	public void setDadosEntityes(FechamentoMes entity) {
		this.entity = entity;
	}

// * metodo set /p service
	public void setServices(FechamentoMesService service, AdiantamentoService adService, MesesService mesService,
			AnosService anoService, OrdemServicoService osService, OrcVirtualService virService, FuncionarioService funService, 
			BalcaoService balService, TipoConsumoService tipoService, CompromissoService comService, ParcelaService parService,
			ParPeriodoService perService, ReceberService recService, ComissaoService comissaoService) {
		this.service = service;
		this.adService = adService;
		this.mesService = mesService;
		this.anoService = anoService;
		this.osService = osService;
		this.virService = virService;
		this.funService = funService;
		this.balService = balService;
		this.parService = parService;
		this.recService = recService;
		this.comissaoService = comissaoService;
	}

//  * o controlador tem uma lista de eventos q permite distribui��o via metodo abaixo
// * recebe o evento e inscreve na lista
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtOkAction(ActionEvent event) throws ParseException {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula");
		}
		try {
			entity = getFormData();
			montaDadosMensal();
			notifyDataChangeListerners();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErros());
		}
	}

// *   um for p/ cada listener da lista, eu aciono o metodo onData no DataChangListner...   
	private void notifyDataChangeListerners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	/*
	 * criamos um obj vazio (obj), chamo codigo (em string) e transformamos em int
	 * (la no util) se codigo for nulo insere, se n�o for atz tb verificamos se cpos
	 * obrigat�rios est�o preenchidos, para informar erro(s) para cpos string n�o
	 * precisa tryParse
	 */
	private FechamentoMes getFormData() throws ParseException {
		FechamentoMes obj = new FechamentoMes();
// instanciando uma exce��o, mas n�o lan�ado - validation exc....
		ValidationException exception = new ValidationException("Validation exception");

		obj.setMes(comboBoxMeses.getValue());
		mm = comboBoxMeses.getValue().getNumeroMes();
		if (obj.getMes() == null) {
			exception.addErros("meses", "mes inválido");
		}
		FechamentoMesConsultaListController.nomeMes = comboBoxMeses.getValue().getNomeMes();

		obj.setAno(comboBoxAnos.getValue());
		aa = comboBoxAnos.getValue().getAnoAnos();
		if (obj.getAno() == null) {
			exception.addErros("anos", "ano inválido");
		}
		
		LocalDate dth = DataStatic.criaLocalAtual();
		int ah = DataStatic.anoDaData(dth);
		int mh = DataStatic.mesDaData(dth);
		if (aa > ah ) {
			exception.addErros("meses", "Data futura");
			exception.addErros("anos", "Sem previsão!!!");
		}
		if (aa == ah ) {
			if (mm > mh) {
				exception.addErros("meses", "Data futura");
				exception.addErros("anos", "Sem previsão!!!");
			}
		}

		LocalDate dt1 = DataStatic.criaAnoMesDia(aa, mm, 20);
		df = DataStatic.ultimoDiaMes(dt1);

		dt1 = DataStatic.criaAnoMesDia(2001, 01, 01);
		dataInicialDespAberto = DataStatic.localParaDateSdfAno(dt1);

		dt1 = DataStatic.criaAnoMesDia(aa, mm, df);
		dataFinalDespAberto = DataStatic.localParaDateSdfAno(dt1);
		dataFinalMes = DataStatic.localParaDateSdfAno(dt1);

		dt1 = DataStatic.criaAnoMesDia(aa, mm, ddInicial);
		dataInicialDespPago = DataStatic.localParaDateSdfAno(dt1);
		dataInicialMes = DataStatic.localParaDateSdfAno(dt1);
		
		dt1 = DataStatic.criaAnoMesDia(aa, mm, df);
		dataFinalDespPago = DataStatic.localParaDateSdfAno(dt1);
		
		dt1 = DataStatic.criaAnoMesDia(aaInicial, mmInicial, ddInicial);
		dataInicialRecAberto = DataStatic.localParaDateSdfAno(dt1);
				
		dt1 = DataStatic.criaAnoMesDia(aa, mm, df);
		dataFinalRecAberto = DataStatic.localParaDateSdfAno(dt1);
		
		dt1 = DataStatic.criaAnoMesDia(aa, mm, ddInicial);
		dataInicialRecPago = DataStatic.localParaDateSdfAno(dt1);
		
		dt1 = DataStatic.criaAnoMesDia(aa, mm, df);
		dataFinalRecPago = DataStatic.localParaDateSdfAno(dt1);
		
		dt1 = DataStatic.criaAnoMesDia(aa, mm, 01);
		dataInicialRec = DataStatic.localParaDateSdfAno(dt1);
		
		dt1 = DataStatic.criaAnoMesDia(aa, mm, df);
		dataFinalRec = DataStatic.localParaDateSdfAno(dt1);
		
		if (exception.getErros().size() > 0) {
			throw exception;
		}
		return obj;
	}

// msm processo save p/ fechar
	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	
	private void montaDadosMensal() {
 		if (adService == null) {
			throw new IllegalStateException("Serviço Adiantamento está vazio");
 		}
 		if (service == null) { 
			throw new IllegalStateException("Serviço DadosFechamentoMensal está vazio");
 		}
 		if (mesService == null) {
			throw new IllegalStateException("Serviço Meses está vazio");
 		}
 		if (anoService == null) {
			throw new IllegalStateException("Serviço Anos está vazio");
 		}
 		if (osService == null) {
			throw new IllegalStateException("Serviço OS está vazio");
 		}
 		if (virService == null) {
			throw new IllegalStateException("Serviço Virtual está vazio");
 		} 

 		int mesPag = 0;
		int anoPag = 0;
 		int mesVen = 0;
		int anoVen = 0;
		Double sumAcumulado = 0.00;

		try {
 			classe = "Dados Fechamento 1 ";
 			Double sumMaterial = 0.00;
 			Double sumResultado = 0.00;
 			@SuppressWarnings("unused")
			int nada = 0;
			dados.setValorResultadoMensal(Mascaras.formataValor(0.00));
 			dados.setValorAcumuladoMensal(Mascaras.formataValor(0.00));
 			service.zeraAll();

 			classe = "Meses ";
 			objMes = mesService.findId(mm);
 			classe = "Anos dados ";
 			objAno = anoService.findAno(aa);
 			
			int anoAnt = aa;
			int mesAnt = mm - 1;
			int diaAnt = 0;
			if (mesAnt == 0) {
				mesAnt = 12;
				anoAnt = aa - 1;
			}
			if (mesAnt == 01 || mesAnt == 03 || mesAnt == 05 || mesAnt == 07 || mesAnt == 8 || mesAnt == 10 || mesAnt == 12 ) {
				diaAnt = 31;
			}
			if (mesAnt == 04 || mesAnt == 06 || mesAnt == 9 || mesAnt == 11) {
				diaAnt = 30;
			}
			if (mesAnt == 02) {
				int resto = anoAnt % 4;
				if (resto > 0) {
					diaAnt = 28;
				} else {
					diaAnt = 29;
				}	
			}
			LocalDate data = DataStatic.criaAnoMesDia(2001, 01, 01);
			Date dtiAnt = DataStatic.localParaDateSdfAno(data); 			
			data = DataStatic.criaAnoMesDia(anoAnt, mesAnt, diaAnt);
			Date dtfAnt = DataStatic.localParaDateSdfAno(data);

 			double vlrAntPago = parService.findSumPago(dtiAnt, dtfAnt);		
 			double vlrOsRec = recService.findPagoOsMes(dtiAnt, dtfAnt);
 			double vlrBalRec = recService.findPagoBalMes(dtiAnt, dtfAnt);
 			sumAcumulado = (vlrOsRec + vlrBalRec) - (vlrAntPago);
 			
 			entity.setNumeroMensal(null);
 			entity.setOsMensal(null);
 			entity.setBalMensal(null);
 			entity.setDataMensal(null);
 			entity.setClienteMensal(null);
 			entity.setFuncionarioMensal(null);
 			entity.setValorOsMensal(null);
 			entity.setValorMaterialMensal(null);
 			entity.setValorComissaoMensal("     Saldo");
 			entity.setValorResultadoMensal("anterior=>");
 			try {
 				entity.setValorAcumuladoMensal(Mascaras.formataValor(sumAcumulado));
 			} catch (ParseException e1) {
 				e1.printStackTrace();
 			} 
 			service.insert(entity);
 			
 			classe = "Parcela sum ";
 			
 			Double sumAberto = parService.findSumAberto(dataInicialDespAberto, dataFinalDespAberto);
 			Double sumPago = parService.findSumPago(dataInicialDespPago, dataFinalDespPago);
 			
// som salarios		
 			classe = "Funcionario ";
 			List<Funcionario> list = funService.findAll(new Date());
 			list.removeIf(f -> ! f.getSituacao().getNomeSit().equals("Ativo"));
 			list.forEach(f -> {
 				f.totalComissao(comissaoService.comSumTotalFun(mm, aa, f.getCodigoFun()));
 				f.totalAdiantamentoFun(adService.findByTotalAdi(mm, aa, f.getCodigoFun()));
 				sumFolha += ((f.getSalarioFun() + f.getComissaoFun()) - f.getAdiantamentoFun());
 				funService.saveOrUpdate(f);			
 			});	
		
 			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
 			
// monta dados OS		
			int  sumOs = 0;
			Double sumVlrOs = 0.00;
				
			classe = "Receber ";
			List<Receber> listOs = recService.findAll();
			listOs.removeIf(r -> r.getDataPagamentoRec().before(dataInicialRec));
			listOs.removeIf(r -> r.getDataPagamentoRec().after(dataFinalRec));
			listOs.removeIf(r -> r.getPlacaRec().equals("Balcao"));
			listOs.removeIf(r -> r.getPlacaRec().equals("Balcão"));
			if (list.size() > 0) {	
				for (Receber recOs : listOs) {
					double sumVlr = 0.00;
					double sumMatOs = 0.00;
					double sumComOs = 0.00;
					sumResultado = 0.00;
					cal.setTime(recOs.getDataPagamentoRec());
					mesPag = 1 + cal.get(Calendar.MONTH);
					anoPag = cal.get(Calendar.YEAR);
					cal.setTime(recOs.getDataVencimentoRec());
					mesVen= 1 + cal.get(Calendar.MONTH);
					anoVen = cal.get(Calendar.YEAR);
					os = null;

					if (anoPag == aa || anoVen == aa) {
						if (os == null) {
							fun = funService.findById(recOs.getFuncionarioRec());
							os = osService.findById(recOs.getOsRec());
							sumMatOs = virService.findByCustoOrc(os.getOrcamentoOS());
							sumComOs = comissaoService.oSSumComissao(mm, aa, os.getNumeroOS());	
							if (mesPag == mm && recOs.getValorPagoRec() > 0) {
								sumVlr = recOs.getValorPagoRec();
								if (recOs.getParcelaRec() == 1) {
									sumResultado = sumVlrOs - (sumMatOs + sumComOs);
									sumAcumulado += recOs.getValorPagoRec(); 
								} 
								if (recOs.getValorPagoRec() > 0 && recOs.getParcelaRec() > 1) {
									sumAcumulado += recOs.getValorPagoRec(); 
									sumResultado = recOs.getValorPagoRec();
									sumMatOs = 0.0;
									sumComOs = 0.0;
								}
							}	
							if (anoVen == aa && recOs.getValorPagoRec() == 0.0) {
								if (mesVen == mm) {
									if (recOs.getParcelaRec() > 1) {
										sumMatOs = 0.0;
										sumComOs = 0.0;
										sumResultado = 0.0;
									}										
								}
							}
						}
					}
					sumOs += 1;
					dados.setBalMensal("   ---");
					dados.setOsMensal(String.valueOf(os.getNumeroOS()));
					dados.setDataMensal(sdf.format(os.getDataOS()));
					dados.setValorOsMensal(Mascaras.formataValor(sumVlr));
					sumVlrOs += sumVlr;
					dados.setClienteMensal(os.getClienteOS());
					dados.setFuncionarioMensal(fun.getNomeFun().substring(0, 20));
		
					sumMaterial += sumMatOs;
		
					if (dados.getValorResultadoMensal() == null ) {
						dados.setValorResultadoMensal(Mascaras.formataValor(0.00));
						dados.setValorAcumuladoMensal(Mascaras.formataValor(0.00));
					}
					dados.setValorMaterialMensal(Mascaras.formataValor(sumMatOs));
					dados.setValorComissaoMensal(Mascaras.formataValor(sumComOs));
					sumFolha += sumComOs;
					dados.setValorResultadoMensal(Mascaras.formataValor(sumResultado));
					dados.setValorAcumuladoMensal(Mascaras.formataValor(sumAcumulado));
					dados.setMes(objMes);
					dados.setAno(objAno);	
					service.insert(dados);
				}
			}	
// monta dados balc�o		
			classe = "Balcão ";
			int sumBal = 0;
			@SuppressWarnings("unused")
			int numBal = 0;

			classe = "Receber ";
			List<Receber> listBalcao = recService.findAll();
			listBalcao.removeIf(rb -> rb.getDataPagamentoRec().before(dataInicialRec));
			listBalcao.removeIf(rb -> rb.getDataPagamentoRec().after(dataFinalRec));
			for (Receber recBalcao : listBalcao) {
				if (recBalcao.getPlacaRec().equals("Balcao") || recBalcao.getPlacaRec().equals("Balcão")) {
					nada = 1;
				} else {
					recBalcao.setPlacaRec("del");
				}	
				if (!recBalcao.getPlacaRec().contains("del")) { 
					bal = balService.findById(recBalcao.getOsRec());
					double sumMatBal = 0.0;
					double sumVlrBal = 0.0;
					double sumComBal = 0.0;
					sumResultado = 0.00;
					cal.setTime(recBalcao.getDataPagamentoRec());
					mesPag = 1 + cal.get(Calendar.MONTH);
					anoPag = cal.get(Calendar.YEAR);
					cal.setTime(recBalcao.getDataVencimentoRec());
					mesVen = 1 + cal.get(Calendar.MONTH);
					anoVen = cal.get(Calendar.YEAR);
					bal = null;

					if (anoPag == aa || anoVen == aa) {
						if (bal == null) {
							bal = balService.findById(recBalcao.getOsRec());
							sumMatBal = virService.findByCustoOrc(bal.getNumeroBal());
							sumComBal = comissaoService.balcaoSumComissao(mm, aa, bal.getNumeroBal());
							if (mesPag == mm && recBalcao.getValorPagoRec() > 0) {
								sumVlrBal = recBalcao.getValorPagoRec();
								if (recBalcao.getParcelaRec() == 1) {
									sumResultado = sumVlrBal - (sumMatBal + sumComBal);
									sumAcumulado += recBalcao.getValorPagoRec(); 
								} 
								if (recBalcao.getValorPagoRec() > 0 && recBalcao.getParcelaRec() > 1) {
									sumAcumulado += recBalcao.getValorPagoRec(); 
									sumVlrBal = recBalcao.getValorPagoRec();
									sumResultado = recBalcao.getValorPagoRec();
									sumMatBal = 0.0;
									sumComBal = 0.0;
								}
							}	
							if (anoPag == 0 && anoVen == aa) {
								if (mesVen == mm) {
									if (recBalcao.getParcelaRec() == 1) {
										sumResultado -= sumMatBal;
									} else {
										sumMatBal = 0.0;
										sumComBal = 0.0;
										sumResultado = 0.0;
									}										
								}
							}	
						}
					}
					sumBal += 1;
					dados.setOsMensal("   ----");
					dados.setBalMensal(String.valueOf(bal.getNumeroBal()));
					dados.setDataMensal(sdf.format(bal.getDataBal()));
					dados.setValorOsMensal(Mascaras.formataValor(sumVlrBal));
					dados.setClienteMensal("Balcão");
					dados.setFuncionarioMensal(bal.getFuncionarioBal().substring(0, 20));
					sumMaterial += sumMatBal;
					if (dados.getValorResultadoMensal() == null ) {
						dados.setValorResultadoMensal(Mascaras.formataValor(0.00));
						dados.setValorAcumuladoMensal(Mascaras.formataValor(0.00));
					}
					dados.setValorComissaoMensal(Mascaras.formataValor(sumComBal));
					dados.setValorMaterialMensal(Mascaras.formataValor(sumMatBal));
					sumFolha += sumComBal;
					dados.setValorResultadoMensal(Mascaras.formataValor(sumResultado));
					dados.setValorAcumuladoMensal(Mascaras.formataValor(sumAcumulado));
					dados.setMes(objMes);
					dados.setAno(objAno);	
					service.insert(dados);
				}
			}
// monta tributos
			classe = "Fechamento Mensal ";
			dados.setNumeroMensal(null);
			dados.setOsMensal("");
			dados.setBalMensal("");
			dados.setDataMensal("");
			dados.setClienteMensal("                ===");
			dados.setFuncionarioMensal("Receita");
			dados.setValorComissaoMensal("");
			dados.setValorResultadoMensal("");
			dados.setValorAcumuladoMensal(""); 
			dados.setValorOsMensal("===");
			dados.setValorMaterialMensal("");
			dados.setMes(objMes);
			dados.setAno(objAno);	
			service.insert(dados);

			dados.setNumeroMensal(null);
			dados.setOsMensal("");
			dados.setBalMensal("");
			dados.setDataMensal("");
			dados.setClienteMensal("Qtd Os: ");
			dados.setFuncionarioMensal(Mascaras.formataMillhar(sumOs));
			dados.setValorComissaoMensal("");
			dados.setValorResultadoMensal("");
			dados.setValorAcumuladoMensal(""); 
			dados.setValorOsMensal("");
			dados.setValorMaterialMensal("");
			dados.setMes(objMes);
			dados.setAno(objAno);	
			service.insert(dados);

			Double recOs = 0.00; 
			Double recOsAberto = 0.00;
			Double recOsRecebido = 0.00;
			recOsAberto = recService.findAbertoOsMes(dataInicialRecAberto, dataFinalRecAberto);		
		
			dados.setNumeroMensal(null);
			dados.setOsMensal("");
			dados.setBalMensal("");
			dados.setDataMensal("");
			dados.setClienteMensal("(#) A Receber");
			dados.setFuncionarioMensal(Mascaras.formataValor(recOsAberto));
			dados.setValorComissaoMensal("");
			dados.setValorResultadoMensal("");
			dados.setValorAcumuladoMensal(""); 
			dados.setValorOsMensal("");
			dados.setValorMaterialMensal("");
			dados.setMes(objMes);
			dados.setAno(objAno);	
			service.insert(dados);
			recOsRecebido = recService.findPagoOsMes(dataInicialRecPago, dataFinalRecPago);		

			dados.setNumeroMensal(null);
			dados.setOsMensal("");
			dados.setBalMensal("");
			dados.setDataMensal("");
			dados.setClienteMensal("(+) Recebido");
			dados.setFuncionarioMensal(Mascaras.formataValor(recOsRecebido));
			dados.setValorComissaoMensal("");
			dados.setValorResultadoMensal("");
			dados.setValorAcumuladoMensal(""); 
			dados.setValorOsMensal("");
			dados.setValorMaterialMensal("");
			dados.setMes(objMes);
			dados.setAno(objAno);	
			service.insert(dados);

			recOs = recOsRecebido; 
			dados.setNumeroMensal(null);
			dados.setOsMensal("");
			dados.setBalMensal("");
			dados.setDataMensal("===");
			dados.setClienteMensal("(=) Valor Os");
			dados.setFuncionarioMensal(Mascaras.formataValor(recOs));
			dados.setValorComissaoMensal("");
			dados.setValorResultadoMensal("");
			dados.setValorAcumuladoMensal(""); 
			dados.setValorOsMensal("");
			dados.setValorMaterialMensal("");
			dados.setMes(objMes);
			dados.setAno(objAno);	
			service.insert(dados);
			
			dados.setNumeroMensal(null);
			dados.setOsMensal("");
			dados.setBalMensal("");
			dados.setDataMensal("");
			dados.setClienteMensal("Qtd Balcão: ");
			dados.setFuncionarioMensal(Mascaras.formataMillhar(sumBal));
			dados.setValorComissaoMensal("");
			dados.setValorResultadoMensal("");
			dados.setValorAcumuladoMensal(""); 
			dados.setValorOsMensal("");
			dados.setValorMaterialMensal("");
			dados.setMes(objMes);
			dados.setAno(objAno);	
			service.insert(dados);
		
			Double recBalRecebido = 0.00;
			Double recBalAberto = 0.00; 
			Double recBal = 0.00; 
			
			recBalRecebido = recService.findPagoBalMes(dataInicialRecPago, dataFinalRecPago);
			
			dados.setNumeroMensal(null);
			dados.setOsMensal("");
			dados.setBalMensal("");
			dados.setDataMensal("");
			dados.setClienteMensal("(+) Recebido");
			dados.setFuncionarioMensal(Mascaras.formataValor(recBalRecebido));
			dados.setValorComissaoMensal("");
			dados.setValorResultadoMensal("");
			dados.setValorAcumuladoMensal(""); 
			dados.setValorOsMensal("");
			dados.setValorMaterialMensal("");
			dados.setMes(objMes);
			dados.setAno(objAno);	
			service.insert(dados);

			recBalAberto = recService.findAbertoBalMes(dataInicialRecAberto, dataFinalRecAberto);
			dados.setNumeroMensal(null);
			dados.setOsMensal("");
			dados.setBalMensal("");
			dados.setDataMensal("");
			dados.setClienteMensal("(#) Em Aberto");
			dados.setFuncionarioMensal(Mascaras.formataValor(recBalAberto));
			dados.setValorComissaoMensal("");
			dados.setValorResultadoMensal("");
			dados.setValorAcumuladoMensal(""); 
			dados.setValorOsMensal("");
			dados.setValorMaterialMensal("");
			dados.setMes(objMes);
			dados.setAno(objAno);	
			service.insert(dados);

			recBal = recBalRecebido + recBalAberto;
			
			dados.setNumeroMensal(null);
			dados.setOsMensal("");
			dados.setBalMensal("");
			dados.setDataMensal("===");
			dados.setClienteMensal("(=) Valor Balcao ");
			dados.setFuncionarioMensal(Mascaras.formataValor(recBal));
			dados.setValorComissaoMensal("");
			dados.setValorResultadoMensal("");
			dados.setValorAcumuladoMensal(""); 
			dados.setValorOsMensal("");
			dados.setValorMaterialMensal("");
			dados.setMes(objMes);
			dados.setAno(objAno);	
			service.insert(dados);

			Double recReceita = recOs + recBal;
			
			dados.setNumeroMensal(null);
			dados.setOsMensal("");
			dados.setBalMensal("");
			dados.setDataMensal("===");
			dados.setClienteMensal("(=) Total Receita ");
			dados.setFuncionarioMensal(Mascaras.formataValor(recReceita));
			dados.setValorComissaoMensal("");
			dados.setValorResultadoMensal("");
			dados.setValorAcumuladoMensal(""); 
			dados.setValorOsMensal("");
			dados.setValorMaterialMensal("");
			dados.setMes(objMes);
			dados.setAno(objAno);	
			service.insert(dados);

			dados.setNumeroMensal(null);
			dados.setOsMensal("");
			dados.setBalMensal("");
			dados.setDataMensal("");
			dados.setClienteMensal("                ===");
			dados.setFuncionarioMensal("Despesa");
			dados.setValorComissaoMensal("");
			dados.setValorResultadoMensal("");
			dados.setValorAcumuladoMensal(""); 
			dados.setValorOsMensal("===");
			dados.setValorMaterialMensal("");
			dados.setMes(objMes);
			dados.setAno(objAno);	
			service.insert(dados);

			dados.setNumeroMensal(null);
			dados.setOsMensal("");
			dados.setBalMensal("");
			dados.setDataMensal("");
			dados.setClienteMensal("(#) Valor Folha");
			dados.setFuncionarioMensal(Mascaras.formataValor(sumFolha));
			dados.setValorComissaoMensal("");
			dados.setValorResultadoMensal("");
			dados.setValorAcumuladoMensal(""); 
			dados.setValorOsMensal("");
			dados.setValorMaterialMensal("");
			dados.setMes(objMes);
			dados.setAno(objAno);	
			service.insert(dados);
		
			dados.setNumeroMensal(null);
			dados.setOsMensal("");
			dados.setBalMensal("");
			dados.setDataMensal("");
			dados.setClienteMensal("(+) Despesa Paga   ");
			dados.setFuncionarioMensal(Mascaras.formataValor(sumPago));
			dados.setValorComissaoMensal("");
			dados.setValorResultadoMensal("");
			dados.setValorAcumuladoMensal(""); 
			dados.setValorOsMensal("");
			dados.setValorMaterialMensal("");
			dados.setMes(objMes);
			dados.setAno(objAno);	
			service.insert(dados);
		
			dados.setNumeroMensal(null);
			dados.setOsMensal("");
			dados.setBalMensal("");
			dados.setDataMensal("");
			dados.setClienteMensal("(+) Despesa Aberto ");
			dados.setFuncionarioMensal(Mascaras.formataValor(sumAberto));
			dados.setValorComissaoMensal("");
			dados.setValorResultadoMensal("");
			dados.setValorAcumuladoMensal(""); 
			dados.setValorOsMensal("");
			dados.setValorMaterialMensal("");
			dados.setMes(objMes);
			dados.setAno(objAno);	
			service.insert(dados);
			
			Double recDespesa = sumPago + sumAberto;
			dados.setNumeroMensal(null);
			dados.setOsMensal("");
			dados.setBalMensal("");
			dados.setDataMensal("===");
			dados.setClienteMensal("(=) Total Despesa");
			dados.setFuncionarioMensal(Mascaras.formataValor(recDespesa));
			dados.setValorComissaoMensal("");
			dados.setValorResultadoMensal("");
			dados.setValorAcumuladoMensal(""); 
			dados.setValorOsMensal("");
			dados.setValorMaterialMensal("");
			dados.setMes(objMes);
			dados.setAno(objAno);	
			service.insert(dados);
					
			dados.setNumeroMensal(null);
			dados.setOsMensal("");
			dados.setBalMensal("");
			dados.setDataMensal("");
			dados.setClienteMensal("");
			dados.setFuncionarioMensal("");
			dados.setValorComissaoMensal("");
			dados.setValorResultadoMensal("");
			dados.setValorAcumuladoMensal(""); 
			dados.setValorOsMensal("");
			dados.setValorMaterialMensal("");
			dados.setMes(objMes);
			dados.setAno(objAno);	
			service.insert(dados);
			
			dados.setNumeroMensal(null);
			dados.setOsMensal("");
			dados.setBalMensal("");
			dados.setDataMensal("===");
			dados.setClienteMensal("(=) Total ------->");
			dados.setFuncionarioMensal(Mascaras.formataValor(sumAcumulado));
			dados.setValorComissaoMensal("");
			dados.setValorResultadoMensal("");
			dados.setValorAcumuladoMensal(""); 
			dados.setValorOsMensal("");
			dados.setValorMaterialMensal("");
			dados.setMes(objMes);
			dados.setAno(objAno);	
			service.insert(dados);
		} 	
		catch (NullPointerException n) {
			n.printStackTrace();
			Alerts.showAlert(null, "Erro!!!", n.getMessage(), AlertType.ERROR);
//			n.printStackTrace();
		}
		catch (ParseException e) {
			e.printStackTrace();
		}	
	}

	/*
	 * o contrainsts (confere) impede alfa em cpo numerico e delimita tamanho
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeComboBoxMeses();
		initializeComboBoxAnos();
	}

	private void initializeComboBoxMeses() {
		Callback<ListView<Meses>, ListCell<Meses>> factory = lv -> new ListCell<Meses>() {
			@Override
			protected void updateItem(Meses item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeMes());
			}
		};

		comboBoxMeses.setCellFactory(factory);
		comboBoxMeses.setButtonCell(factory.call(null));
	}

	private void initializeComboBoxAnos() {
		Callback<ListView<Anos>, ListCell<Anos>> factory = lv -> new ListCell<Anos>() {
			@Override
			protected void updateItem(Anos item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getAnoStrAnos());
			}
		};

		comboBoxAnos.setCellFactory(factory);
		comboBoxAnos.setButtonCell(factory.call(null));
	}

	/*
	 * transforma string da tela p/ o tipo no bco de dados
	 */
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entidade esta nula");
		}
// se for uma inclusao, vai posicionar no 1o depto//tipo (First)	
		if (entity.getMes() == null) {
			comboBoxMeses.getSelectionModel().selectFirst();
		} else {
			comboBoxMeses.setValue(entity.getMes());
		}
		if (entity.getAno() == null) {
			comboBoxAnos.getSelectionModel().selectFirst();
		} else {
			comboBoxAnos.setValue(entity.getAno());
		}
	}
//	carrega dados do bco  dentro obslist
	public void loadAssociatedObjects() {
		if (mesService == null) {
			throw new IllegalStateException("MesesServiço esta nulo");
		}
// buscando (carregando) os forn q est�o no bco de dados		
		List<Meses> listMes = mesService.findAll();
		obsListMes = FXCollections.observableArrayList(listMes);
		comboBoxMeses.setItems(obsListMes);
		List<Anos> listAno = anoService.findAll();
		obsListAno = FXCollections.observableArrayList(listAno);
		comboBoxAnos.setItems(obsListAno);
	}

	// mandando a msg de erro para o labelErro correspondente
	private void setErrorMessages(Map<String, String> erros) {
		Set<String> fields = erros.keySet();
		labelErrorComboBoxMeses.setText((fields.contains("meses") ? erros.get("meses") : ""));
		labelErrorComboBoxAnos.setText((fields.contains("anos") ? erros.get("anos") : ""));
	}
}