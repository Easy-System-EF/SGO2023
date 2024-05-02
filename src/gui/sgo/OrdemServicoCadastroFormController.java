package gui.sgo;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listerneres.DataChangeListener;
import gui.sgcpmodel.entities.Compromisso;
import gui.sgcpmodel.entities.Fornecedor;
import gui.sgcpmodel.entities.Parcela;
import gui.sgcpmodel.entities.TipoConsumo;
import gui.sgcpmodel.entities.consulta.ParPeriodo;
import gui.sgcpmodel.services.FornecedorService;
import gui.sgcpmodel.services.ParPeriodoService;
import gui.sgomodel.dao.DaoFactory;
import gui.sgomodel.dao.OSCommitDao;
import gui.sgomodel.entities.Entrada;
import gui.sgomodel.entities.Material;
import gui.sgomodel.entities.NotaFiscal;
import gui.sgomodel.entities.OrcVirtual;
import gui.sgomodel.entities.Orcamento;
import gui.sgomodel.entities.OrdemServico;
import gui.sgomodel.entities.Receber;
import gui.sgomodel.entities.ReposicaoVeiculo;
import gui.sgomodel.entities.Veiculo;
import gui.sgomodel.services.EntradaService;
import gui.sgomodel.services.MaterialService;
import gui.sgomodel.services.NotaFiscalService;
import gui.sgomodel.services.OrcVirtualService;
import gui.sgomodel.services.OrcamentoService;
import gui.sgomodel.services.VeiculoService;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.exception.ValidationException;

public class OrdemServicoCadastroFormController implements Initializable, DataChangeListener {

	private OrdemServico entity;
	@SuppressWarnings("unused")
	private OrdemServico entAnt;
	private Orcamento orcamento;
	@SuppressWarnings("unused")
	private Material material;
	private ParPeriodo periodo;
	private Veiculo veiculo;
	private NotaFiscal nota = new NotaFiscal();

	/*
	 * private OrdemServico entity; dependencia service com metodo set
	 */
	private OrcamentoService orcService;
	private OrcVirtualService virService;
	private MaterialService matService;
	private ParPeriodoService perService;
	private VeiculoService veiService;
	private NotaFiscalService nfService;
	
	List<Receber> listRecCommit = new ArrayList<>();
	List<ReposicaoVeiculo> listRepCommit = new ArrayList<>();
	List<Material> listMatCommit = new ArrayList<>();

// lista da classe subject (form) - guarda lista de obj p/ receber e emitir o evento
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField textNumeroOS;

	@FXML
	private DatePicker dpDataOS;

	@FXML
	private TextField textPesquisa;

	@FXML
	private ComboBox<Orcamento> comboBoxOrcamento;

	@FXML
	private RadioButton rbParcelaUnica;

	@FXML
	private RadioButton rbParcela02;

	@FXML
	private RadioButton rbParcela03;

	@FXML
	private RadioButton rbPrazoAvista;

	@FXML
	private RadioButton rbPrazo10;

	@FXML
	private RadioButton rbPrazo15;

	@FXML
	private RadioButton rbPrazo30;

	@FXML
	private RadioButton rbPagamentoDinheiro;

	@FXML
	private RadioButton rbPagamentoPix;

	@FXML
	private RadioButton rbPagamentoDebito;

	@FXML
	private RadioButton rbPagamentoCC;

	@FXML
	private DatePicker dpData1oOS;

	@FXML
	private TextField textNnfOS;

	@FXML
	private TextField textObservacaoOS;

	@FXML
	private Button btPesquisa;

	@FXML
	private Button btSaveOS;

	@FXML
	private Button btCancelOS;

	@FXML
	private Label labelErrorDataOS;

	@FXML
	private Label labelErrorOrcamentoOS;

	@FXML
	private Label labelErrorParcelaOS;

	@FXML
	private Label labelErrorPrazoOS;

	@FXML
	private Label labelErrorPagamentoOS;

	@FXML
	private Label labelErrorData1oOS;

	@FXML
	private Label labelErrorNnfOS;

	@FXML
	private Label labelUser;

	// auxiliar
	public String user = "usuário";
	String pesquisa = "";
// saber se func tem comissao	
	String classe = "";
	Double maoObra = 0.00;
	Double servico = 0.00;
	Date data1oOs = null;
	String tipoEnt = null;
	String grava = null;
	int ultimaNF = 0;
	int flagAlert = 0;
	Calendar cal = Calendar.getInstance();

	private ObservableList<Orcamento> obsListOrc;

	public void setOrdemEntity(OrdemServico entity, Orcamento orcamento, Material material, ParPeriodo periodo, 
			Veiculo veiculo) {
		this.entity = entity;
		this.orcamento = orcamento;
		this.material = material;
		this.periodo = periodo;
		this.veiculo = veiculo;
	}

	// * metodo set /p service
	public void setServices(OrcamentoService orcService, OrcVirtualService virService,
			MaterialService matService, ParPeriodoService perService, VeiculoService veiService, 
			NotaFiscalService nfService) {
		this.orcService = orcService;
		this.virService = virService;
		this.matService = matService;
		this.perService = perService;
		this.veiService = veiService;
		this.nfService = nfService;
	}

	@FXML
	public void onBtPesquisaAction(ActionEvent event) throws ParseException {
		classe = "Orçamento";
		pesquisa = "";
		@SuppressWarnings("unused")
		String placa = null;
		try {
			ValidationException exception = new ValidationException("Validation exception");
			pesquisa = textPesquisa.getText().toUpperCase().trim();
			if (pesquisa != "") {
				List<Orcamento> listOrcP = orcService.findPesquisa(pesquisa);
				if (listOrcP.size() == 0) { 
					exception.addErros("orcto", "orçamento não existe ou fechado ");
					exception.addErros("dataOS", "Não existe Orçamento ");
					listOrcP = orcService.findByAberto();
			 	}
				for (Orcamento o : listOrcP) {
					placa = o.getPlacaOrc();
				}	
				pesquisa = "";
				listOrcP.removeIf(o -> o.getOsOrc() > 0);
				obsListOrc = FXCollections.observableArrayList(listOrcP);
				comboBoxOrcamento.setItems(obsListOrc);
				notifyDataChangeListerners();
				updateFormData();
			}
			if (exception.getErros().size() > 0) {
				throw exception;
			}	
		} 
		catch (ValidationException e) {
			setErrorMessages(e.getErros());
		}
		catch (ParseException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto", classe, e.getMessage(), AlertType.ERROR);
		} 
		catch (DbException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	}

	/*
	 * vamos instanciar um forn e salvar no bco de dados meu obj entity (l� em cima)
	 * vai receber uma chamada do getformdata metodo q busca dados do formulario
	 * convertidos getForm (string p/ int ou string) pegou no formulario e retornou
	 * (convertido) p/ jogar na variavel entity chamo o service na rotina saveupdate
	 * e mando entity vamos tst entity e service = null -> n�o foi injetado para
	 * fechar a janela, pego a referencia para janela atual (event) e close
	 * DataChangeListner classe subjetc - q emite o evento q muda dados, vai guardar
	 * uma lista qdo ela salvar obj com sucesso, � s� notificar (juntar) recebe l�
	 * no listController
	 */
	@FXML
	public void onBtSaveOSAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade OS nula");
		}
		if (orcService == null) {
			throw new IllegalStateException("Serviço orçamento nulo");
		}
		try {
			if (entity.getNumeroOS() != null) {
				entAnt = entity;
			}
			entity = getFormData();
			confereSaldo(event);
			if (grava == "sim") {
				classe = "OS Form Material ";
				updateMaterialOS();
				classe = "Ordem de Serviço Form ";
				nota.setCodigoNF(null);
				nota.setOsNF(null);
				nota.setNumeroNF(entity.getNnfOS());
				nota.setBalcaoNF(0);
				classe = "OS Form NF ";
				classe = "OS Form Orçamento ";
				orcamento = orcService.findById(entity.getOrcamentoOS());
				porPeriodo();
				updateOsOrcamento();
				updateKmVeiculo();
				classe = "OS Commit ";
				OSCommitDao osCommit = DaoFactory.createOSCommitDao();
				osCommit.gravaOS(entity, orcamento, periodo, nota, veiculo, maoObra, listMatCommit);
			}	
			labelErrorNnfOS.setText("");
			labelErrorNnfOS.viewOrderProperty();
			notifyDataChangeListerners();
			updateFormData();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErros());
		} catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		} catch (ParseException p) {
			p.printStackTrace();
		}
	}

	private void confereSaldo(ActionEvent event) {
		try {
			classe = "Material";
			Material mat1;
			List<OrcVirtual> vir = virService.findByOrcto(entity.getOrcamentoOS());
			for (OrcVirtual v : vir) {
				if (v.getNumeroOrcVir().equals(entity.getOrcamentoOS())) {
					mat1 = matService.findById(v.getMaterial().getCodigoMat());
					if (mat1.getGrupo().getNomeGru().contains("Mão de obra") || 
							mat1.getGrupo().getNomeGru().contains("Mão de obra") ||
							mat1.getGrupo().getNomeGru().contains("Serviço") ||
							mat1.getGrupo().getNomeGru().contains("Servico")) { 
						if (mat1.getSaldoMat() < v.getQuantidadeMatVir()) {
							mat1.setEntradaMat(v.getQuantidadeMatVir());
							maoObra += mat1.getVendaMat();
						}	
					}	
					matService.saveOrUpdate(mat1);
					if(v.getQuantidadeMatVir() > mat1.getSaldoMat()) {
						grava = "nao";
						somaSaldo(event, v.getQuantidadeMatVir(), mat1.getCodigoMat());
						if (grava == "sim") {
							mat1 = matService.findById(v.getMaterial().getCodigoMat());
							v.setMaterial(mat1);
							v.setCustoMatVir(mat1.getPrecoMat());
							virService.saveOrUpdate(v);
							Orcamento orc1 = orcService.findById(v.getNumeroOrcVir());
							orcService.saveOrUpdate(orc1);
						}
// saidaCmmProd p/ calculo cmmm
					} else {
						grava = "sim";
					}
				}
			}	
		} catch (DbException e) {
			Alerts.showAlert("Erro saldo OS ", classe, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void somaSaldo(ActionEvent event, double qtd, int cod) {
		Material mat2;
		mat2 = matService.findById(cod);
		Optional<ButtonType> result = 
				Alerts.showConfirmation("Saldo insuficiente ", "Deseja incluir? - "  + mat2.getNomeMat());
		if (result.get() == ButtonType.OK) {
	 		Stage parentStage = Utils.currentStage(event);
			Compromisso com = new Compromisso();
			Parcela par = new Parcela();
			Fornecedor forn =new Fornecedor();
			TipoConsumo tipo = new TipoConsumo();
			Entrada ent = new Entrada();
			data1oOs = entity.getDataPrimeiroPagamentoOS();
			tipoEnt = "os";
			createDialogEnt(ent, mat2, com, periodo, par, forn, tipo, data1oOs, tipoEnt,     
							"/gui/sgo/EntradaCadastroForm.fxml", parentStage);
			mat2 = matService.findById(cod);
			if (qtd <= mat2.getSaldoMat()) {
				grava = "sim";
			}
			notifyDataChangeListerners();
			try {
				updateFormData();
			} catch (ParseException e) {
				e.printStackTrace();
			}	
		}
	}	
	
	private void updateMaterialOS() {
		try {
			ValidationException exception = new ValidationException("Validation exception");
			Material mat3;
			classe = "OS Form virtual ";
			if (grava == "sim") {
				List<OrcVirtual> listVir = virService.findByOrcto(entity.getOrcamentoOS());
				for (OrcVirtual ov : listVir) {
					if (ov.getNumeroOrcVir().equals(entity.getOrcamentoOS())) {
						mat3 = matService.findById(ov.getMaterial().getCodigoMat());
						mat3.setSaidaMat(ov.getQuantidadeMatVir());
						mat3.getSaldoMat();
						mat3.getCmmMat();
						matService.saveOrUpdate(mat3);
						ov.setMaterial(mat3);
						listMatCommit.add(mat3);
						@SuppressWarnings("unused")
						int nada = 0;
						if (mat3.getGrupo().getNomeGru().contains("Mão de obra") || 
								mat3.getGrupo().getNomeGru().contains("Mão de obra") ||
								mat3.getGrupo().getNomeGru().contains("Serviço") ||
								mat3.getGrupo().getNomeGru().contains("Servico")) { 
							nada = 1;
						} else {
							if (mat3.getSaldoMat() <= mat3.getEstMinMat()) {
								if (mat3.getSaldoMat() == 0.00) {
									Alerts.showAlert("Atenção!!! ", "Estoque zerou ", mat3.getNomeMat(), AlertType.WARNING);
								} else {	
									Alerts.showAlert("Atenção!!! ", "Recompor estoque ", mat3.getNomeMat(), AlertType.WARNING);
								}	
							}
						}	
					}	
				}
				if (exception.getErros().size() > 0) {
					throw exception;
				}
			}
		}		
		catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	}	
			
	private ParPeriodo porPeriodo() {
		try {
			classe = "OS Form Período";
			SimpleDateFormat sdfi = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			SimpleDateFormat sdff = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date dti = sdfi.parse("01/01/2001 00:00:00");
			Date dtf = sdff.parse("31/01/2041 00:00:00");

			Integer cod = 1;
			periodo = new ParPeriodo();
			periodo = perService.findById(cod);
			periodo.setIdPeriodo(1);
			periodo.setDtiPeriodo(dti);
			periodo.setDtfPeriodo(dtf);
			perService.update(periodo);
		} catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		} catch (ParseException e) {
			Alerts.showAlert("ParseException ", "Erro Data Periodo ", e.getMessage(), AlertType.ERROR);
		}
		return periodo;
	}

	private void updateOsOrcamento() {
		try {
			classe = "Orçamento OS Form";
			orcamento.setOsOrc(null);
//			orcService.saveOrUpdate(orcamento);
		} catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	}

	private void updateKmVeiculo()
	{
		try {
			classe = "Veiculo OS Form ";
			veiculo = veiService.findByPlaca(entity.getPlacaOS());
			if (veiculo.getPlacaVei() == entity.getPlacaOS()) {
				veiculo.setKmInicialVei(veiculo.getKmFinalVei());
				veiculo.setKmFinalVei(entity.getKmOS());
			} else {
				veiculo.setPlacaVei(entity.getPlacaOS());
				veiculo.setKmInicialVei(entity.getKmOS());
				veiculo.setKmFinalVei(entity.getKmOS());
				veiculo.setModeloVei(null);
				veiculo.setAnoVei(0);
			}
//			veiService.saveOrUpdate(veiculo);
		} catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	}

// *   um for p/ cada listener da lista, eu aciono o metodo onData no DataChangListner...   
	private void notifyDataChangeListerners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

//  * o controlador tem uma lista de eventos q permite distribui��o via metodo abaixo
// * recebe o evento e inscreve na lista
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	/*
	 * criamos um obj vazio (obj), chamo codigo (em string) e transformamos em int
	 * (la no util) se codigo for nulo insere, se n�o for atz tb verificamos se cpos
	 * obrigat�rios est�o preenchidos, para informar erro(s) para cpos string n�o
	 * precisa tryParse
	 */
	private OrdemServico getFormData() throws ParseException {
		OrdemServico obj = new OrdemServico();
// instanciando uma exce��o, mas n�o lan�ado - validation exc....
		ValidationException exception = new ValidationException("Validation exception");
// set CODIGO c/ utils p/ transf string em int \\ ou null		
		obj.setNumeroOS(Utils.tryParseToInt(textNumeroOS.getText()));
// tst name (trim elimina branco no principio ou final
// lan�a Erros - nome do cpo e msg de erro
		if (dpDataOS.getValue() != null) {
			Instant instant = Instant.from(dpDataOS.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataOS(Date.from(instant));
		}
		if (dpDataOS.getValue() == null) {
			exception.addErros("dataOS", "Data é obrigatória");
		}

		if (comboBoxOrcamento.getValue() != null) {
			obj.setOrcamento(comboBoxOrcamento.getValue());
			obj.setOrcamentoOS(obj.getOrcamento().getNumeroOrc());
			obj.setPlacaOS(obj.getOrcamento().getPlacaOrc());
			obj.setClienteOS(obj.orcamento.getClienteOrc());
			obj.setValorOS(obj.getOrcamento().getTotalOrc());
		} else {
			exception.addErros("orcto", "Orçamento é obrigatório");			
		}
		if (obj.getNumeroOS() != null) {
			exception.addErros("dataOS", "Orçamento Fechado");
			exception.addErros("orcto", "Sem acesso");
			exception.addErros("parcela", "Ordem de Serviço No: " + obj.getNumeroOS());
		}

		if (comboBoxOrcamento.getValue() != null) {
			obj.setOrcamentoOS(comboBoxOrcamento.getValue().getNumeroOrc());
			obj.setPlacaOS(comboBoxOrcamento.getValue().getPlacaOrc());
			obj.setClienteOS(comboBoxOrcamento.getValue().getClienteOrc());
			obj.setValorOS(comboBoxOrcamento.getValue().getTotalOrc());
			obj.setKmOS(comboBoxOrcamento.getValue().getKmFinalOrc());
		}
		
			int flag1 = 0;
			if (rbParcelaUnica.isSelected()) {
				obj.setParcelaOS(1);
				flag1 += 1;
			}
			if (rbParcela02.isSelected()) {
				obj.setParcelaOS(2);
				flag1 += 1;
			}
			if (rbParcela03.isSelected()) {
				obj.setParcelaOS(3);
				flag1 += 1;
			}
			if (obj.getParcelaOS() == null) {
				exception.addErros("parcela", "Parcela obrigatória");
			}
			if (flag1 > 1) {
				exception.addErros("parcela", "Só pode uma opção");
			}

			int flag2 = 0;
			if (rbPrazoAvista.isSelected()) {
				obj.setPrazoOS(1);
				flag2 += 1;
			}
			if (rbPrazo10.isSelected()) {
				obj.setPrazoOS(10);
				flag2 += 1;
			}
			if (rbPrazo15.isSelected()) {
				obj.setPrazoOS(15);
				flag2 += 1;
			}
			if (rbPrazo30.isSelected()) {
				obj.setPrazoOS(30);
				flag2 += 1;
			}
			if (obj.getPrazoOS() == null) {
				exception.addErros("prazo", "Prazo obrigatório");
			}
			if (flag2 > 1) {
				exception.addErros("prazo", "Só pode uma opção");
			}
			if (obj.getParcelaOS() != null) {
				if (obj.getParcelaOS() > 1 && obj.getPrazoOS() == 1) {
					exception.addErros("prazo", "Prazo incompatível");
				}	
			}

		int flag3 = 0;
		if (rbPagamentoDinheiro.isSelected()) {
			obj.setPagamentoOS(1);
			flag3 += 1;
		}
		if (rbPagamentoPix.isSelected()) {
			obj.setPagamentoOS(2);
			flag3 += 1;
		}
		if (rbPagamentoDebito.isSelected()) {
			obj.setPagamentoOS(3);
			flag3 += 1;
		}
		if (rbPagamentoCC.isSelected()) {
			obj.setPagamentoOS(4);
			flag3 += 1;
		}
		if (obj.getPagamentoOS() == null) {
			exception.addErros("pagamento", "Pagamento obrigatório");
		}
		if (flag3 > 1) {
			exception.addErros("pagamento", "Só pode uma opção");
		}
		if (obj.getPagamentoOS() != null) {
			if (obj.getPagamentoOS() == 4 && obj.getPrazoOS() > 1) {
				exception.addErros("pagamento", "CC prazo é igual a vista");
			}
		}

		if (dpData1oOS.getValue() != null) {
			Instant instant = Instant.from(dpData1oOS.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataPrimeiroPagamentoOS(Date.from(instant));
		}
		if (dpData1oOS.getValue() == null) {
			exception.addErros("data1o", "Data é obrigatória");
		}
		if (obj.getDataPrimeiroPagamentoOS().before(obj.getDataOS())) {
			exception.addErros("data1o", "Data menor que Data da OS");
		}

		if (textNnfOS.getText() == null || textNnfOS.getText().trim().contentEquals("")) {
			obj.setNnfOS(0);
		} else {
			obj.setNnfOS(Utils.tryParseToInt(textNnfOS.getText()));
		}

		List<NotaFiscal>  listNF = nfService.findAll();
		for (NotaFiscal nf : listNF) {
			if (ultimaNF < nf.getNumeroNF()) {
				ultimaNF = nf.getNumeroNF();
			}
		}
		
		if (obj.getNnfOS() < ultimaNF) {
			exception.addErros("nf", "NF menor a última " + ultimaNF);
		}	
		if (obj.getNnfOS() == ultimaNF) {
			exception.addErros("nf", "NF igual a última " + ultimaNF);
		}	

		obj.setObservacaoOS(textObservacaoOS.getText());
		Calendar cal = Calendar.getInstance();
		cal.setTime(obj.getDataOS());
		obj.setMesOs(cal.get(Calendar.MONTH) + 1);
		obj.setAnoOs(cal.get(Calendar.YEAR));

// tst se houve algum (erro com size > 0)
		if (exception.getErros().size() > 0) {
			throw exception;
		}
		return obj;		
	}

// msm processo save p/ fechar
	@FXML
	public void onBtCancelOSAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	/*
	 * o contrainsts (confere) impede alfa em cpo numerico e delimita tamanho
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setTextFieldInteger(textNumeroOS);
		Utils.formatDatePicker(dpDataOS, "dd/MM/yyyy");
		Utils.formatDatePicker(dpData1oOS, "dd/MM/yyyy");
		Constraints.setTextFieldInteger(textNnfOS);
		Constraints.setTextFieldMaxLength(textObservacaoOS, 60);
		Constraints.setTextFieldMaxLength(textNnfOS, 06);
		Constraints.setTextFieldMaxLength(textPesquisa, 7);
		initializeComboBoxOrcamento();
	}

	private void initializeComboBoxOrcamento() {
	 	DecimalFormat df = new DecimalFormat("#,###,##0.00"); 
		Callback<ListView<Orcamento>, ListCell<Orcamento>> factory = lv -> new ListCell<Orcamento>() {
			@Override
			protected void updateItem(Orcamento item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getPlacaOrc() + " - " + item.dataFormatada() + " - " + 
							item.getClienteOrc().substring(0, 10) + " - " + ("R$" + df.format(item.getTotalOrc())));
			}
		};

		comboBoxOrcamento.setCellFactory(factory);
		comboBoxOrcamento.setButtonCell(factory.call(null));
	}

//	carrega dados do bco cargo dentro obslist via
	public void loadAssociatedObjects() {
		if (orcService == null) {
			throw new IllegalStateException("Orçamento Serviço esta nulo");
		}
		List<Orcamento> listOrc = orcService.findByAberto();
// transf p/ obslist		
		obsListOrc = FXCollections.observableArrayList(listOrc);
		comboBoxOrcamento.setItems(obsListOrc);
	}

	public void updateFormData() throws ParseException {
		if (entity == null) {
			throw new IllegalStateException("Entidade esta nula");
		}
		labelUser.setText(user);
//  string value of p/ casting int p/ string 		
		textNumeroOS.setText(String.valueOf(entity.getNumeroOS()));
		if (entity.getDataOS() == null) {
			entity.setDataOS(new Date());
		}
		if (entity.getDataOS() != null) {
			dpDataOS.setValue(LocalDate.ofInstant(entity.getDataOS().toInstant(), ZoneId.systemDefault()));
		}

		if (entity.getOrcamento() == null) {
			comboBoxOrcamento.getSelectionModel().selectFirst();
		} else {
			comboBoxOrcamento.setValue(entity.getOrcamento());
		}

		rbParcelaUnica.setSelected(false);
		rbParcela02.setSelected(false);
		rbParcela03.setSelected(false);
		if (entity.getParcelaOS() != null) {
			if (entity.getParcelaOS() == 1) {
				rbParcelaUnica.setSelected(true);
			} else {
				if (entity.getParcelaOS() == 2) {
					rbParcela02.setSelected(true);
				} else {
					if (entity.getParcelaOS() == 3) {
						rbParcela03.setSelected(true);
					}
				}
			}
		}
		rbPrazoAvista.setSelected(false);
		rbPrazo10.setSelected(false);
		rbPrazo15.setSelected(false);
		rbPrazo30.setSelected(false);
		if (entity.getPrazoOS() != null) {
			if (entity.getPrazoOS() == 1) {
				rbPrazoAvista.setSelected(true);
			} else {
				if (entity.getPrazoOS() == 10) {
					rbPrazo10.setSelected(true);
				} else {
					if (entity.getPrazoOS() == 15) {
						rbPrazo15.setSelected(true);
					} else {
						if (entity.getPrazoOS() == 30) {
							rbPrazo30.setSelected(true);
						}
					}
				}
			}
		}
		rbPagamentoDinheiro.setSelected(false);
		rbPagamentoPix.setSelected(false);
		rbPagamentoDebito.setSelected(false);
		rbPagamentoCC.setSelected(false);
		if (entity.getPagamentoOS() != null) {
			if (entity.getPagamentoOS() == 1) {
				rbPagamentoDinheiro.setSelected(true);
			} else {
				if (entity.getPagamentoOS() == 2) {
					rbPagamentoPix.setSelected(true);
				} else {
					if (entity.getPagamentoOS() == 3) {
						rbPagamentoDebito.setSelected(true);
					} else {
						if (entity.getPagamentoOS() == 4) {
							rbPagamentoCC.setSelected(true);
						}
					}
				}
			}
		}
		if (entity.getDataPrimeiroPagamentoOS() == null) {
			entity.setDataPrimeiroPagamentoOS(entity.getDataOS());
		}
		dpData1oOS
				.setValue(LocalDate.ofInstant(entity.getDataPrimeiroPagamentoOS().toInstant(), ZoneId.systemDefault()));

		textNnfOS.setText(String.valueOf(entity.getNnfOS()));
		textObservacaoOS.setText(entity.getObservacaoOS());
		textPesquisa.setText(pesquisa);
	}

// mandando a msg de erro para o labelErro correspondente 	
	private void setErrorMessages(Map<String, String> erros) {
		Set<String> fields = erros.keySet();
		labelErrorDataOS.setText((fields.contains("dataOS") ? erros.get("dataOS") : ""));
		labelErrorOrcamentoOS.setText((fields.contains("orcto") ? erros.get("orcto") : ""));
		labelErrorParcelaOS.setText((fields.contains("parcela") ? erros.get("parcela") : ""));
		labelErrorPrazoOS.setText((fields.contains("prazo") ? erros.get("prazo") : ""));
		labelErrorPagamentoOS.setText((fields.contains("pagamento") ? erros.get("pagamento") : ""));
		labelErrorData1oOS.setText((fields.contains("data1o") ? erros.get("data1o") : ""));
		labelErrorNnfOS.setText((fields.contains("nf") ? erros.get("nf") : ""));
		labelErrorOrcamentoOS.setText((fields.contains("orcto") ? erros.get("orcto") : ""));
	}

	@Override
	public void onDataChanged() {
	}
	
	@SuppressWarnings("static-access")
	private void createDialogEnt(Entrada obj, Material mat, Compromisso objCom, ParPeriodo objPer, Parcela objPar, 
			Fornecedor objFor, TipoConsumo objTipo, Date data1oOs, String tipoEnt, String absoluteName, Stage parentStage) {
		try {
			classe = "OS Form Entrada ";
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
// referencia para o controlador = controlador da tela carregada fornListaForm			
			EntradaCadastroFormController controller = loader.getController();
			controller.user = user;
			controller.data1oOs = data1oOs;
			controller.tipoEnt = tipoEnt;
 // injetando passando parametro obj
			
			controller.setPesquisa(mat.getNomeMat());
			controller.setObjects(obj, mat);
			controller.setServices(new EntradaService(), new FornecedorService(), new MaterialService());
 // injetando tb o forn service vindo da tela de formulario fornform
			controller.loadAssociatedObjects();
// inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
			controller.subscribeDataChangeListener(this);
//	carregando o obj no formulario (fornecedorFormControl)			
			controller.updateFormData();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Digite Entrada                                             ");
 			dialogStage.setScene(new Scene(pane));
// pode redimencionar a janela: s/n?
			dialogStage.setResizable(false);
// quem e o stage pai da janela?
			dialogStage.initOwner(parentStage);
// travada enquanto n�o sair da tela
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", classe + "Erro carregando tela ", e.getMessage(), AlertType.ERROR);
		}
 	} 	
}
