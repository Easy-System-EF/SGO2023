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
import java.util.ResourceBundle;

import gui.listerneres.DataChangeListener;
import gui.sgcpmodel.services.ParcelaService;
import gui.sgomodel.entities.Adiantamento;
import gui.sgomodel.entities.Balcao;
import gui.sgomodel.entities.FechamentoAno;
import gui.sgomodel.entities.Funcionario;
import gui.sgomodel.entities.OrcVirtual;
import gui.sgomodel.entities.Orcamento;
import gui.sgomodel.entities.OrdemServico;
import gui.sgomodel.entities.Receber;
import gui.sgomodel.services.AdiantamentoService;
import gui.sgomodel.services.BalcaoService;
import gui.sgomodel.services.FechamentoAnoService;
import gui.sgomodel.services.FuncionarioService;
import gui.sgomodel.services.OrcVirtualService;
import gui.sgomodel.services.OrcamentoService;
import gui.sgomodel.services.OrdemServicoService;
import gui.sgomodel.services.ReceberService;
import gui.util.DataStatic;
import gui.util.Mascaras;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class FechamentoAnoConsultaFormController implements Initializable, Serializable {

	private static final long serialVersionUID = 1L;

	private FechamentoAno entity;
	
	/*
	 * dependencia service com metodo set
	 */
	private FechamentoAnoService service;
	private AdiantamentoService adService;
	private OrdemServicoService osService;
	private OrcamentoService orcService;
	private OrcVirtualService virService;
	private FuncionarioService funService;
	private BalcaoService balService;
	private ParcelaService parService;
	private ReceberService recService;
	private Orcamento orc;
	@SuppressWarnings("unused")
	private Adiantamento adianto;
	@SuppressWarnings("unused")
	private Balcao bal;
	@SuppressWarnings("unused")
	private Receber receber;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

//auxiliar
	String classe = "Fechamento ano ";
	int mm = 0;
	int aa = 0;
	int dd = 0;
	int df = 0;
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	Date dataInicialDespAberto = new Date();
	Date dataFinalDespAberto = new Date();
	Date dataInicialDespPago = new Date();
	Date dataFinalDespPago = new Date();
	Date dataInicialRecAberto = new Date();
	Date dataFinalRecAberto = new Date();
	Date dataInicialRecPago = new Date();
	Date dataFinalRecPago = new Date();

	Calendar cal = Calendar.getInstance();

	public void setDadosEntityes(FechamentoAno entity, Orcamento orc, Adiantamento adianto, Balcao bal, Receber receber) {
		this.entity = entity;
		this.orc = orc;
		this.adianto = adianto;
		this.bal = bal;
		this.receber = receber;
	}

	// * metodo set /p service
	public void setServiceAll(FechamentoAnoService service, AdiantamentoService adService, 
			OrdemServicoService osService, OrcamentoService orcService,
			OrcVirtualService virService, FuncionarioService funService, BalcaoService balService,
			ParcelaService parService, ReceberService recService) {
		this.service = service;
		this.adService = adService;
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
	public void onBtOk() throws ParseException {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula");
		} else {	
			getFormData();
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

	/*
	 * criamos um obj vazio (obj), chamo codigo (em string) e transformamos em int
	 * (la no util) se codigo for nulo insere, se n�o for atz tb verificamos se cpos
	 * obrigat�rios est�o preenchidos, para informar erro(s) para cpos string n�o
	 * precisa tryParse
	 */
	private void getFormData() {
		LocalDate ld = DataStatic.criaLocalAtual();
		aa = ld.getYear();
		mm = ld.getMonthValue();
		
		LocalDate dt1 = DataStatic.criaAnoMesDia(aa, mm, 20);
		df = DataStatic.ultimoDiaMes(dt1);

		dt1 = DataStatic.criaAnoMesDia(2001, 01, 01);
		dataInicialDespAberto = DataStatic.localParaDateSdfAno(dt1);

		dt1 = DataStatic.criaAnoMesDia(aa, mm, df);
		dataFinalDespAberto = DataStatic.localParaDateSdfAno(dt1);

		dt1 = DataStatic.criaAnoMesDia(aa, 01, 01);
		dataInicialDespPago = DataStatic.localParaDateSdfAno(dt1);
		
		dt1 = DataStatic.criaAnoMesDia(aa, mm, df);
		dataFinalDespPago = DataStatic.localParaDateSdfAno(dt1);
		
		dt1 = DataStatic.criaAnoMesDia(2001, 01, 01);
		dataInicialRecAberto = DataStatic.localParaDateSdfAno(dt1);
				
		dt1 = DataStatic.criaAnoMesDia(aa, mm, df);
		dataFinalRecAberto = DataStatic.localParaDateSdfAno(dt1);
		
		dt1 = DataStatic.criaAnoMesDia(aa, 01, 01);
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

	}

	// msm processo save p/ fechar
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
 		if (orcService == null) {
			throw new IllegalStateException("Serviço Orçamento está vazio");
 		}
 		if (virService == null) {
			throw new IllegalStateException("Serviço Virtual está vazio");
 		} 
		try {
 			classe = "Dados Fechamento 1 ";
 			FechamentoAno dados = new FechamentoAno();
 			Double sumMaterial = 0.00;
 			Double sumResultado = 0.00;
 			Double sumAcumulado = 0.00;
 			@SuppressWarnings("unused")
			int nada = 0;
			dados.setValorResultadoAnual(Mascaras.formataValor(0.00));
 			dados.setValorAcumuladoAnual(Mascaras.formataValor(0.00));
 			service.zeraAll();

 			classe = "Parcela sum ";
 			
 			Double sumAberto = parService.findSumAberto(dataInicialRecAberto, dataFinalRecAberto);
 			Double sumPago = parService.findSumPago(dataInicialDespPago, dataFinalDespPago);
 			Double sumFolha = 0.00;
 			
// som salarios		
 			classe = "Funcionario ";
 			List<Funcionario> list = funService.findAll(aa, mm);
 			list.removeIf(x -> x.getNomeFun().equals("Consumo Próprio"));
 			list.removeIf(x -> x.getNomeFun().equals("Consumo Proprio"));
 			list.removeIf(x -> !x.getSituacaoFun().equals("Ativo"));
 			for (Funcionario f: list) {
 					sumFolha += (f.getCargo().getSalarioCargo() * mm);
 					f.setSalarioFun(f.getCargo().getSalarioCargo());
 			}
		
// monta dados OS		
			classe = "OS ";
			int  sumOs = 0;
			Double sumVlrOs = 0.00;
			List<OrdemServico> listOs = osService.findByAno(aa);
			sumOs = listOs.size();
			if (listOs.size() > 0) {
				classe = "Virtual ";
				List<OrcVirtual> listVir = new ArrayList<>();
				for (OrdemServico o : listOs) {
					if (o.getNumeroOS() != null) {
						double sumMatOs = 0.00;
						dados.setBalAnual("   ---");
						dados.setOsAnual(String.valueOf(o.getNumeroOS()));
						dados.setDataAnual(sdf.format(o.getDataOS()));
						dados.setValorOsAnual(Mascaras.formataValor(o.getValorOS()));
						sumVlrOs += o.getValorOS();
						classe = "Orcamento ";
						orc = orcService.findById(o.getOrcamentoOS());
						classe = "OrcVirtual ";
						listVir = virService.findByOrcto(orc.getNumeroOrc());
						dados.setClienteAnual(orc.getClienteOrc());
						dados.setFuncionarioAnual(orc.getFuncionarioOrc().substring(0, 20));

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

						if (dados.getValorResultadoAnual() == null ) {
							dados.setValorResultadoAnual(Mascaras.formataValor(0.00));
							dados.setValorAcumuladoAnual(Mascaras.formataValor(0.00));
						}
						dados.setValorComissaoAnual(Mascaras.formataValor(0.00));
						dados.setValorMaterialAnual(Mascaras.formataValor(sumMatOs));
						
						double com = adService.findByTotalComOS(o.getNumeroOS());
						dados.setValorComissaoAnual(Mascaras.formataValor(com));
						sumResultado = o.getValorOS() - (sumMatOs + com);
						dados.setValorResultadoAnual(Mascaras.formataValor(sumResultado));
						sumAcumulado += sumResultado; 
						dados.setValorAcumuladoAnual(Mascaras.formataValor(sumAcumulado));
						classe = "Dados Fechamento ";
						service.insert(dados);
					}				
				}
			}
		
// monta dados balc�o		
			classe = "Balcão ";
			int sumBal = 0;
			Double sumVlrBal = 0.00;
			List<Balcao> listBal = balService.findByAno(aa);
			sumBal = listBal.size();
			if (listBal.size() > 0) {
				List<OrcVirtual> listVir = new ArrayList<>();
				for (Balcao b : listBal) {
					if (b.getNumeroBal() != null) {
						double sumMatBal = 0.00;
						dados.setOsAnual("   ----");
						dados.setBalAnual(String.valueOf(b.getNumeroBal()));
						dados.setDataAnual(sdf.format(b.getDataBal()));
						dados.setValorOsAnual(Mascaras.formataValor(b.getTotalBal()));
						sumVlrBal += b.getTotalBal(); 
						dados.setClienteAnual("Balcão");
						dados.setFuncionarioAnual(b.getFuncionarioBal().substring(0, 20));
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
						if (dados.getValorResultadoAnual() == null ) {
							dados.setValorResultadoAnual(Mascaras.formataValor(0.00));
							dados.setValorAcumuladoAnual(Mascaras.formataValor(0.00));
						}
						dados.setValorComissaoAnual(Mascaras.formataValor(0.00));
						dados.setValorMaterialAnual(Mascaras.formataValor(sumMatBal));

						double com = adService.findByTotalComBal(b.getNumeroBal());
						dados.setValorComissaoAnual(Mascaras.formataValor(com));
						sumResultado = b.getTotalBal() - (sumMatBal + com);
						dados.setValorResultadoAnual(Mascaras.formataValor(sumResultado));
						sumAcumulado += sumResultado; 
						dados.setValorAcumuladoAnual(Mascaras.formataValor(sumAcumulado));
						classe = "Dados Fechamento ";
						service.insert(dados);
					}				
				}
			}
// monta tributos
			if (sumOs > 0 || sumBal > 0) {
				dados.setNumeroAnual(null);
				dados.setOsAnual("");
				dados.setBalAnual("");
				dados.setDataAnual("");
				dados.setClienteAnual("");
				dados.setFuncionarioAnual("");
				dados.setValorComissaoAnual("");
				dados.setValorResultadoAnual("");
				dados.setValorAcumuladoAnual(""); 
				dados.setValorOsAnual("");
				dados.setValorMaterialAnual("");
				service.insert(dados);

				dados.setNumeroAnual(null);
				dados.setOsAnual("");
				dados.setBalAnual("");
				dados.setDataAnual("");
				dados.setClienteAnual("                ===");
				dados.setFuncionarioAnual("Receita");
				dados.setValorComissaoAnual("");
				dados.setValorResultadoAnual("");
				dados.setValorAcumuladoAnual(""); 
				dados.setValorOsAnual("===");
				dados.setValorMaterialAnual("");
				service.insert(dados);

				dados.setNumeroAnual(null);
				dados.setOsAnual("");
				dados.setBalAnual("");
				dados.setDataAnual("");
				dados.setClienteAnual("Qtd Os: ");
				dados.setFuncionarioAnual(Mascaras.formataMillhar(sumOs));
				dados.setValorComissaoAnual("");
				dados.setValorResultadoAnual("");
				dados.setValorAcumuladoAnual(""); 
				dados.setValorOsAnual("");
				dados.setValorMaterialAnual("");
				service.insert(dados);

				Double recOs = 0.00; 
				Double recOsAberto = 0.00;
				Double recOsRecebido = 0.00;
				recOsAberto = recService.findAbertoOsMes(dataInicialRecAberto, dataFinalRecAberto);		
			
				dados.setNumeroAnual(null);
				dados.setOsAnual("");
				dados.setBalAnual("");
				dados.setDataAnual("");
				dados.setClienteAnual("(+) A Receber");
				dados.setFuncionarioAnual(Mascaras.formataValor(recOsAberto));
				dados.setValorComissaoAnual("");
				dados.setValorResultadoAnual("");
				dados.setValorAcumuladoAnual(""); 
				dados.setValorOsAnual("");
				dados.setValorMaterialAnual("");
				service.insert(dados);

				recOsRecebido = recService.findPagoOsMes(dataInicialRecPago, dataFinalRecPago);		

				dados.setNumeroAnual(null);
				dados.setOsAnual("");
				dados.setBalAnual("");
				dados.setDataAnual("");
				dados.setClienteAnual("(+) Recebido");
				dados.setFuncionarioAnual(Mascaras.formataValor(recOsRecebido));
				dados.setValorComissaoAnual("");
				dados.setValorResultadoAnual("");
				dados.setValorAcumuladoAnual(""); 
				dados.setValorOsAnual("");
				dados.setValorMaterialAnual("");
				service.insert(dados);

				recOs = recOsAberto + recOsRecebido; 

				dados.setNumeroAnual(null);
				dados.setOsAnual("");
				dados.setBalAnual("");
				dados.setDataAnual("===");
				dados.setClienteAnual("(=) Valor Os");
				dados.setFuncionarioAnual(Mascaras.formataValor(recOs));
				dados.setValorComissaoAnual("");
				dados.setValorResultadoAnual("");
				dados.setValorAcumuladoAnual(""); 
				dados.setValorOsAnual("");
				dados.setValorMaterialAnual("");
				service.insert(dados);

				dados.setNumeroAnual(null);
				dados.setOsAnual("");
				dados.setBalAnual("");
				dados.setDataAnual("");
				dados.setClienteAnual("Qtd Balcão: ");
				dados.setFuncionarioAnual(Mascaras.formataMillhar(sumBal));
				dados.setValorComissaoAnual("");
				dados.setValorResultadoAnual("");
				dados.setValorAcumuladoAnual(""); 
				dados.setValorOsAnual("");
				dados.setValorMaterialAnual("");
				service.insert(dados);
		
				Double recBalRecebido = 0.00;
				Double recBalAberto = 0.00; 
				Double recBal = 0.00; 
			
				recBalRecebido = recService.findPagoBalMes(dataInicialRecPago, dataFinalRecPago);
				
				dados.setNumeroAnual(null);
				dados.setOsAnual("");
				dados.setBalAnual("");
				dados.setDataAnual("");
				dados.setClienteAnual("(+) Recebido");
				dados.setFuncionarioAnual(Mascaras.formataValor(recBalRecebido));
				dados.setValorComissaoAnual("");
				dados.setValorResultadoAnual("");
				dados.setValorAcumuladoAnual(""); 
				dados.setValorOsAnual("");
				dados.setValorMaterialAnual("");
				service.insert(dados);

				recBalAberto = recService.findAbertoBalMes(dataInicialRecAberto, dataFinalRecAberto);

				dados.setNumeroAnual(null);
				dados.setOsAnual("");
				dados.setBalAnual("");
				dados.setDataAnual("");
				dados.setClienteAnual("(+) Em Aberto");
				dados.setFuncionarioAnual(Mascaras.formataValor(recBalAberto));
				dados.setValorComissaoAnual("");
				dados.setValorResultadoAnual("");
				dados.setValorAcumuladoAnual(""); 
				dados.setValorOsAnual("");
				dados.setValorMaterialAnual("");
				service.insert(dados);

				recBal = recBalRecebido + recBalAberto;
			
				dados.setNumeroAnual(null);
				dados.setOsAnual("");
				dados.setBalAnual("");
				dados.setDataAnual("===");
				dados.setClienteAnual("(=) Valor Balcao ");
				dados.setFuncionarioAnual(Mascaras.formataValor(recBal));
				dados.setValorComissaoAnual("");
				dados.setValorResultadoAnual("");
				dados.setValorAcumuladoAnual(""); 
				dados.setValorOsAnual("");
				dados.setValorMaterialAnual("");
				service.insert(dados);

				Double recReceita = recOs + recBal;
			
				dados.setNumeroAnual(null);
				dados.setOsAnual("");
				dados.setBalAnual("");
				dados.setDataAnual("===");
				dados.setClienteAnual("(=) Total Receita ");
				dados.setFuncionarioAnual(Mascaras.formataValor(recReceita));
				dados.setValorComissaoAnual("");
				dados.setValorResultadoAnual("");
				dados.setValorAcumuladoAnual(""); 
				dados.setValorOsAnual("");
				dados.setValorMaterialAnual("");
				service.insert(dados);

				dados.setNumeroAnual(null);
				dados.setOsAnual("");
				dados.setBalAnual("");
				dados.setDataAnual("");
				dados.setClienteAnual("                ===");
				dados.setFuncionarioAnual("Despesa");
				dados.setValorComissaoAnual("");
				dados.setValorResultadoAnual("");
				dados.setValorAcumuladoAnual(""); 
				dados.setValorOsAnual("===");
				dados.setValorMaterialAnual("");
				service.insert(dados);

				dados.setNumeroAnual(null);
				dados.setOsAnual("");
				dados.setBalAnual("");
				dados.setDataAnual("");
				dados.setClienteAnual("(#) Valor Folha");
				dados.setFuncionarioAnual(Mascaras.formataValor(sumFolha));
				dados.setValorComissaoAnual("");
				dados.setValorResultadoAnual("");
				dados.setValorAcumuladoAnual(""); 
				dados.setValorOsAnual("");
				dados.setValorMaterialAnual("");
				service.insert(dados);
		
				dados.setNumeroAnual(null);
				dados.setOsAnual("");
				dados.setBalAnual("");
				dados.setDataAnual("");
				dados.setClienteAnual("(+) Despesa Paga   ");
				dados.setFuncionarioAnual(Mascaras.formataValor(sumPago));
				dados.setValorComissaoAnual("");
				dados.setValorResultadoAnual("");
				dados.setValorAcumuladoAnual(""); 
				dados.setValorOsAnual("");
				dados.setValorMaterialAnual("");
				service.insert(dados);
		
				dados.setNumeroAnual(null);
				dados.setOsAnual("");
				dados.setBalAnual("");
				dados.setDataAnual("");
				dados.setClienteAnual("(+) Despesa Aberto ");
				dados.setFuncionarioAnual(Mascaras.formataValor(sumAberto));
				dados.setValorComissaoAnual("");
				dados.setValorResultadoAnual("");
				dados.setValorAcumuladoAnual(""); 
				dados.setValorOsAnual("");
				dados.setValorMaterialAnual("");
				service.insert(dados);
			
				Double recDespesa = sumPago + sumAberto;
				dados.setNumeroAnual(null);
				dados.setOsAnual("");
				dados.setBalAnual("");
				dados.setDataAnual("===");
				dados.setClienteAnual("(=) Total Despesa");
				dados.setFuncionarioAnual(Mascaras.formataValor(recDespesa));
				dados.setValorComissaoAnual("");
				dados.setValorResultadoAnual("");
				dados.setValorAcumuladoAnual(""); 
				dados.setValorOsAnual("");
				dados.setValorMaterialAnual("");
				service.insert(dados);
		
				Double total = recReceita - recDespesa;

				dados.setNumeroAnual(null);
				dados.setOsAnual("");
				dados.setBalAnual("");
				dados.setDataAnual("===");
				dados.setClienteAnual("(=) Receita - Desp");
				dados.setFuncionarioAnual(Mascaras.formataValor(total));
				dados.setValorComissaoAnual("");
				dados.setValorResultadoAnual("");
				dados.setValorAcumuladoAnual(""); 
				dados.setValorOsAnual("");
				dados.setValorMaterialAnual("");
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