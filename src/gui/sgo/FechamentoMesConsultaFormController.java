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
import gui.sgomodel.entities.Adiantamento;
import gui.sgomodel.entities.Anos;
import gui.sgomodel.entities.Balcao;
import gui.sgomodel.entities.FechamentoMes;
import gui.sgomodel.entities.Funcionario;
import gui.sgomodel.entities.Meses;
import gui.sgomodel.entities.OrcVirtual;
import gui.sgomodel.entities.Orcamento;
import gui.sgomodel.entities.OrdemServico;
import gui.sgomodel.entities.Receber;
import gui.sgomodel.services.AdiantamentoService;
import gui.sgomodel.services.AnosService;
import gui.sgomodel.services.BalcaoService;
import gui.sgomodel.services.FechamentoMesService;
import gui.sgomodel.services.FuncionarioService;
import gui.sgomodel.services.MesesService;
import gui.sgomodel.services.OrcVirtualService;
import gui.sgomodel.services.OrcamentoService;
import gui.sgomodel.services.OrdemServicoService;
import gui.sgomodel.services.ReceberService;
import gui.util.DataStatic;
import gui.util.Mascaras;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
	private OrcamentoService orcService;
	private OrcVirtualService virService;
	private FuncionarioService funService;
	private BalcaoService balService;
	private ParcelaService parService;
	private ReceberService recService;
	private Meses objMes;
	private Anos objAno;
	private Orcamento orc;
	@SuppressWarnings("unused")
	private Adiantamento adianto;
	private Funcionario fun;
	@SuppressWarnings("unused")
	private Balcao bal;
	@SuppressWarnings("unused")
	private Receber receber;

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
	int mm = 0;
	int aa = 0;
	int dd = 0;
	int df = 0;
	private final int ddInicial = 01;
	private final int mmInicial = 01;
	private final int aaInicial = 2000;
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//	private static SimpleDateFormat sdfAno = new SimpleDateFormat("yyyyy/MM/yyyy HH:mm:ss");
//	private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//	private static DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
// data inicial e final - aberto e pago	
	Date dataInicialDespAberto = new Date();
	Date dataFinalDespAberto = new Date();
	Date dataInicialDespPago = new Date();
	Date dataFinalDespPago = new Date();
	Date dataInicialRecAberto = new Date();
	Date dataFinalRecAberto = new Date();
	Date dataInicialRecPago = new Date();
	Date dataFinalRecPago = new Date();

	Calendar cal = Calendar.getInstance();

	public void setDadosEntityes(FechamentoMes entity, Orcamento orc, Adiantamento adianto, Funcionario fun, Balcao bal,
			Receber receber) {
		this.entity = entity;
		this.orc = orc;
		this.adianto = adianto;
		this.fun = fun;
		this.bal = bal;
		this.receber = receber;
	}

	// * metodo set /p service
	public void setServices(FechamentoMesService service, AdiantamentoService adService, MesesService mesService,
			AnosService anoService, OrdemServicoService osService, OrcamentoService orcService,
			OrcVirtualService virService, FuncionarioService funService, BalcaoService balService,
			TipoConsumoService tipoService, CompromissoService comService, ParcelaService parService,
			ParPeriodoService perService, ReceberService recService) {
		this.service = service;
		this.adService = adService;
		this.mesService = mesService;
		this.anoService = anoService;
		this.osService = osService;
		this.orcService = orcService;
		this.virService = virService;
		this.funService = funService;
		this.balService = balService;
		this.parService = parService;
		this.recService = recService;
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
		FechamentoMesConsultaListController.numAno = aa;

		LocalDate dt1 = DataStatic.criaAnoMesDia(aa, mm, 20);
		df = DataStatic.ultimoDiaMes(dt1);

		dt1 = DataStatic.criaAnoMesDia(2001, 01, 01);
		dataInicialDespAberto = DataStatic.localParaDateSdfAno(dt1);

		dt1 = DataStatic.criaAnoMesDia(aa, mm, df);
		dataFinalDespAberto = DataStatic.localParaDateSdfAno(dt1);

		dt1 = DataStatic.criaAnoMesDia(aa, mm, ddInicial);
		dataInicialDespPago = DataStatic.localParaDateSdfAno(dt1);
		
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
		
//		cal.setTime(data1);
//		cal.set(Calendar.DAY_OF_MONTH, 1);
//		cal.set(Calendar.MONTH, 0);
//		cal.set(Calendar.YEAR, 2001);
//		dtIA = cal.getTime();
//
//		cal.setTime(data1);
//		cal.set(Calendar.DAY_OF_MONTH, df);
//		dtFA = cal.getTime();
//
//		cal.setTime(data1);
//		cal.set(Calendar.DAY_OF_MONTH, 1);
//		dtIP = cal.getTime();
//
//		cal.setTime(data1);
//		cal.set(Calendar.DAY_OF_MONTH, df);
//		dtFP = cal.getTime();

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
 		if (orcService == null) {
			throw new IllegalStateException("Serviço Orçamento está vazio");
 		}
 		if (virService == null) {
			throw new IllegalStateException("Serviço Virtual está vazio");
 		} 
		try {
 			classe = "Dados Fechamento 1 ";
 			FechamentoMes dados = new FechamentoMes();
 			Double sumMaterial = 0.00;
 			Double sumResultado = 0.00;
 			Double sumAcumulado = 0.00;
 			@SuppressWarnings("unused")
			int nada = 0;
			dados.setValorResultadoMensal(Mascaras.formataValor(0.00));
 			dados.setValorAcumuladoMensal(Mascaras.formataValor(0.00));
 			service.zeraAll();

 			classe = "Meses ";
 			objMes = mesService.findId(mm);
 			classe = "Anos dados ";
 			objAno = anoService.findAno(aa);

 			classe = "Parcela sum ";
 			
 			Double sumAberto = parService.findSumAberto(dataInicialRecAberto, dataFinalRecAberto);
 			Double sumPago = parService.findSumPago(dataInicialDespPago, dataFinalDespPago);
 			Double sumFolha = 0.00;
 			
// som salarios		
 			classe = "Funcionario ";
 			List<Funcionario> list = funService.findAll(aa, mm);
 			for (Funcionario f: list) {
 				if (f.getSituacao().getNomeSit().equals("Ativo")) {
 					sumFolha += f.getCargo().getSalarioCargo();
 					f.setSalarioFun(f.getCargo().getSalarioCargo());
 				}				
 			}
		
// zera comiss�o		
 			classe = "Adiantamento ";
 			List<Adiantamento> adZera = adService.findMes(mm, aa);
 			for (Adiantamento a : adZera) {
 				if (a.getCodigoFun() != null) {
 					if (a.getComissaoAdi() > 0 || a.getValeAdi() > 0) {
 						classe = "Funcionario Dados 1 ";
 						fun = funService.findById(a.getCodigoFun());
 						fun.setComissaoFun(adService.findByTotalCom(mm, aa, fun.getCodigoFun()));
 						fun.setAdiantamentoFun(adService.findByTotalAdi(mm, aa, fun.getCodigoFun()));
 						funService.saveOrUpdate(fun);
 					}	
 				}	
 			}
		
// monta dados OS		
			classe = "OS ";
			int  sumOs = 0;
			Double sumVlrOs = 0.00;
			List<OrdemServico> listOs = osService.findByMesAno(mm, aa);
			sumOs = listOs.size();
			if (listOs.size() > 0) {
				classe = "Virtual ";
				List<OrcVirtual> listVir = new ArrayList<>();
				for (OrdemServico o : listOs) {
					if (o.getNumeroOS() != null) {
						double sumMatOs = 0.00;
						dados.setBalMensal("   ---");
						dados.setOsMensal(String.valueOf(o.getNumeroOS()));
						dados.setDataMensal(sdf.format(o.getDataOS()));
						dados.setValorOsMensal(Mascaras.formataValor(o.getValorOS()));
						sumVlrOs += o.getValorOS();
						classe = "Orcamento ";
						orc = orcService.findById(o.getOrcamentoOS());
						classe = "OrcVirtual ";
						listVir = virService.findByOrcto(orc.getNumeroOrc());
						dados.setClienteMensal(orc.getClienteOrc());
						dados.setFuncionarioMensal(orc.getFuncionarioOrc().substring(0, 20));

						for (OrcVirtual v : listVir) {
							if (v.getNumeroOrcVir() != null) {
								if (v.getMaterial().getGrupo().getNomeGru().equals("Mão de obra") || 
										v.getMaterial().getGrupo().getNomeGru().equals("Serviço")) {
										nada = 0;
								} else {	
									sumMatOs += (v.getCustoMatVir() * v.getQuantidadeMatVir());
								}
							}
						}
						sumMaterial += sumMatOs;

						if (dados.getValorResultadoMensal() == null ) {
							dados.setValorResultadoMensal(Mascaras.formataValor(0.00));
							dados.setValorAcumuladoMensal(Mascaras.formataValor(0.00));
						}
						dados.setValorComissaoMensal(Mascaras.formataValor(0.00));
						dados.setValorMaterialMensal(Mascaras.formataValor(sumMatOs));
						dados.setValorComissaoMensal(Mascaras.formataValor(orc.getFuncionario().getComissaoFun()));
						sumResultado = o.getValorOS() - (sumMatOs + orc.getFuncionario().getComissaoFun());
						dados.setValorResultadoMensal(Mascaras.formataValor(sumResultado));
						sumAcumulado += sumResultado; 
						dados.setValorAcumuladoMensal(Mascaras.formataValor(sumAcumulado));
						dados.setMes(objMes);
						dados.setAno(objAno);	
						classe = "Dados Fechamento ";
						service.insert(dados);
					}				
				}
			}
		
// monta dados balc�o		
			classe = "Balcão ";
			int sumBal = 0;
			Double sumVlrBal = 0.00;
			List<Balcao> listBal = balService.findByMesAno(mm, aa);
			sumBal = listBal.size();
			if (listBal.size() > 0) {
				List<OrcVirtual> listVir = new ArrayList<>();
				for (Balcao b : listBal) {
					if (b.getNumeroBal() != null) {
						double sumMatBal = 0.00;
						dados.setOsMensal("   ----");
						dados.setBalMensal(String.valueOf(b.getNumeroBal()));
						dados.setDataMensal(sdf.format(b.getDataBal()));
						dados.setValorOsMensal(Mascaras.formataValor(b.getTotalBal()));
						sumVlrBal += b.getTotalBal(); 
						dados.setClienteMensal("Balcão");
						dados.setFuncionarioMensal(b.getFuncionarioBal().substring(0, 20));
						classe = "OrcVirtual ";
						listVir = virService.findByBalcao(b.getNumeroBal());
						for (OrcVirtual vB : listVir) {
							if (vB.getNumeroOrcVir() != null) {	
								if (vB.getMaterial().getGrupo().getNomeGru().equals("Mão de obra") || 
									vB.getMaterial().getGrupo().getNomeGru().equals("Serviço")) {
									nada = 0;
								} else {	
									sumMatBal += (vB.getCustoMatVir() * vB.getQuantidadeMatVir());
								}	
							}
						}
						sumMaterial += sumMatBal;
						if (dados.getValorResultadoMensal() == null ) {
							dados.setValorResultadoMensal(Mascaras.formataValor(0.00));
							dados.setValorAcumuladoMensal(Mascaras.formataValor(0.00));
						}
						dados.setValorComissaoMensal(Mascaras.formataValor(0.00));
						dados.setValorMaterialMensal(Mascaras.formataValor(sumMatBal));
						dados.setValorComissaoMensal(Mascaras.formataValor(b.getFuncionario().getComissaoFun()));
						sumResultado = b.getTotalBal() - (sumMatBal + b.getFuncionario().getComissaoFun());
						dados.setValorResultadoMensal(Mascaras.formataValor(sumResultado));
						sumAcumulado += sumResultado; 
						dados.setValorAcumuladoMensal(Mascaras.formataValor(sumAcumulado));
						dados.setMes(objMes);
						dados.setAno(objAno);	
						classe = "Dados Fechamento ";
						service.insert(dados);
					}				
				}
			}
// monta tributos
			if (sumOs > 0 || sumBal > 0) {
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
				service.insert(dados);

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
				service.insert(dados);

				Double recOs = 0.00; 
				Double recOsAberto = 0.00;
				Double recOsRecebido = 0.00;
				recOsAberto = recService.findAbertoOsMes(dataInicialRecAberto, dataFinalRecAberto);		
			
				dados.setNumeroMensal(null);
				dados.setOsMensal("");
				dados.setBalMensal("");
				dados.setDataMensal("");
				dados.setClienteMensal("(+) A Receber");
				dados.setFuncionarioMensal(Mascaras.formataValor(recOsAberto));
				dados.setValorComissaoMensal("");
				dados.setValorResultadoMensal("");
				dados.setValorAcumuladoMensal(""); 
				dados.setValorOsMensal("");
				dados.setValorMaterialMensal("");
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
				service.insert(dados);

				recOs = recOsAberto + recOsRecebido; 

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
				service.insert(dados);

				recBalAberto = recService.findAbertoBalMes(dataInicialRecAberto, dataFinalRecAberto);

				dados.setNumeroMensal(null);
				dados.setOsMensal("");
				dados.setBalMensal("");
				dados.setDataMensal("");
				dados.setClienteMensal("(+) Em Aberto");
				dados.setFuncionarioMensal(Mascaras.formataValor(recBalAberto));
				dados.setValorComissaoMensal("");
				dados.setValorResultadoMensal("");
				dados.setValorAcumuladoMensal(""); 
				dados.setValorOsMensal("");
				dados.setValorMaterialMensal("");
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
				service.insert(dados);
		
				Double total = recReceita - recDespesa;

				dados.setNumeroMensal(null);
				dados.setOsMensal("");
				dados.setBalMensal("");
				dados.setDataMensal("===");
				dados.setClienteMensal("(=) Receita - Desp");
				dados.setFuncionarioMensal(Mascaras.formataValor(total));
				dados.setValorComissaoMensal("");
				dados.setValorResultadoMensal("");
				dados.setValorAcumuladoMensal(""); 
				dados.setValorOsMensal("");
				dados.setValorMaterialMensal("");
				service.insert(dados);
			}	
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