package gui.sgo;

import java.io.IOException;
import java.net.URL;
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

import application.MainSgo;
import db.DbException;
import db.DbIntegrityException;
import gui.listerneres.DataChangeListener;
import gui.sgcpmodel.entities.Compromisso;
import gui.sgcpmodel.entities.Fornecedor;
import gui.sgcpmodel.entities.Parcela;
import gui.sgcpmodel.entities.TipoConsumo;
import gui.sgcpmodel.entities.consulta.ParPeriodo;
import gui.sgcpmodel.services.CompromissoService;
import gui.sgcpmodel.services.FornecedorService;
import gui.sgcpmodel.services.ParPeriodoService;
import gui.sgcpmodel.services.ParcelaService;
import gui.sgcpmodel.services.TipoConsumoService;
import gui.sgomodel.dao.BalcaoCommitDao;
import gui.sgomodel.dao.DaoFactory;
import gui.sgomodel.entities.Adiantamento;
import gui.sgomodel.entities.Balcao;
import gui.sgomodel.entities.Entrada;
import gui.sgomodel.entities.Funcionario;
import gui.sgomodel.entities.Material;
import gui.sgomodel.entities.NotaFiscal;
import gui.sgomodel.entities.OrcVirtual;
import gui.sgomodel.services.BalcaoService;
import gui.sgomodel.services.EntradaService;
import gui.sgomodel.services.FuncionarioService;
import gui.sgomodel.services.MaterialService;
import gui.sgomodel.services.NotaFiscalService;
import gui.sgomodel.services.OrcVirtualService;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.DataStatic;
import gui.util.Mascaras;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.exception.ValidationException;

/*
 * Monta formaul�rio do balc�o e virtual
 */

public class BalcaoCadastroFormController implements Initializable, DataChangeListener  {

	private Balcao entity;
	@SuppressWarnings("unused")
	private OrcVirtual virtual;
	private Material mat = new Material();
	private ParPeriodo periodo;
	private NotaFiscal nota = new NotaFiscal();
	Calendar cal = Calendar.getInstance();
	
	Adiantamento adiantamento = new Adiantamento();
	List<Balcao> listBalCommit = new ArrayList<>();
	List<Material> listMatCommit = new ArrayList<>();

	/*
	 * dependencia service com metodo set
	 */
	private BalcaoService service;
	private OrcVirtualService virService;
	private FuncionarioService funService;
	private MaterialService matService;
	private ParPeriodoService perService;
	private NotaFiscalService nfService;

// lista da classe subject (form) - guarda lista de obj p/ receber e emitir o evento
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField textNumeroBal;

	@FXML
	private DatePicker dpDataBal;

 	@FXML
 	private ComboBox<Funcionario> comboBoxFunBal;
 	
 	@FXML
 	private RadioButton rbPagamentoDinheiro;

 	@FXML
 	private RadioButton rbPagamentoPix;

 	@FXML
 	private RadioButton rbPagamentoDebito;

 	@FXML
 	private RadioButton rbPagamentoCC;

 	@FXML
 	private DatePicker dpData1oBal;
 	
 	@FXML
 	private TextField textNnfBal;
 	
	@FXML
	private TextField textDescontoBal;

	@FXML
	private Label labelTotalBal;

 	@FXML
 	private TextField textObservacaoBal;
 	
	@FXML
	private TableView<OrcVirtual> tableViewOrcVirtual;

	@FXML
	private TableColumn<OrcVirtual, String> tableColumnNomeMatVir;

	@FXML
	private TableColumn<OrcVirtual, Double> tableColumnQtdMatVir;

	@FXML
	private TableColumn<OrcVirtual, Double> tableColumnPrecoMatVir;

	@FXML
	private TableColumn<OrcVirtual, Double> tableColumnTotalMatVir;

	@FXML
	private TableColumn<OrcVirtual, OrcVirtual> tableColumnEditaVir;

	@FXML
	private TableColumn<OrcVirtual, OrcVirtual> tableColumnRemoveVir;

	@FXML
	private Button btSaveBal;

	@FXML
	private Button btCancelBal;

	@FXML
	private Label labelErrorDataBal;

	@FXML
	private Label labelErrorPagamentoBal;

	@FXML
	private Label labelErrorDataPrimeiroBal;

	@FXML
	private Label labelErrorNFBal;

	@FXML
	private Label labelErrorTotalBal;

	@FXML
	private Button btNewvVir;

 	@FXML
 	private Label labelUser;

 // auxiliar
 	public String user = "usuário";		
 	String classe = "Balcão Form ";
	Integer numBal = null;
	int ultimaNF = 0;
// deetermina se mostra total atz ou grava	
// = 1 mostra alerts de balcao vazio	
	int flagSave = 2;
	int flagPg = 0;
	double maoObra = 0.00;
	Double valor = 0.00;
	public static Date data1oBal = null;
	public static String tipoEnt = null;
	
	private ObservableList<OrcVirtual> obsListVir;
 	private ObservableList<Funcionario> obsListFun;

	public void setBalcao(Balcao entity) {
		this.entity = entity;
	}
	public void setVirtual(OrcVirtual virtual) {
		this.virtual = virtual;
	}

 	// * metodo set /p service
	public void setServices(BalcaoService service,
							OrcVirtualService virService,
							FuncionarioService funService,
							MaterialService matService,
							NotaFiscalService nfService) {
		this.service = service;
		this.virService = virService; 
 		this.funService = funService;
 		this.matService = matService;
 		this.nfService = nfService;
	}
	
	public void setBalcaoServices(ParPeriodoService perService) {
		this.perService = perService;
	}

	@FXML
	public void onBtNewVirAction(ActionEvent event0) {
		if (numBal == null) {
			saveBal();
		} 
		if (entity.getNumeroBal() != null) {
			numBal = entity.getNumeroBal();
			Stage parentStage = Utils.currentStage(event0);
// instanciando novo obj depto e injetando via
			OrcVirtual virtual = new OrcVirtual();
			createDialogForm(numBal, virtual, "/gui/sgo/OrcVirtualCadastroForm.fxml", parentStage);
		}
	}

	@FXML
	public void saveBal() {
		if (entity == null) {
			throw new IllegalStateException("Entidade balcão nula");
		}
		if (service == null) {
			throw new IllegalStateException("Serviço balcão nulo");
		}
		try {
			flagSave = 1;
			entity = getFormData();
			entity.setTotalBal(0.00);
			classe = "Balcão Form ";
			service.saveOrUpdate(entity);
			numBal = entity.getNumeroBal();
			notifyDataChangeListerners();
			updateFormData();
		} 
		catch (ValidationException e) {
			setErrorMessages(e.getErros());
		} 
		catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		} 
		catch (ParseException p) {
			p.printStackTrace();
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
	public void onBtSaveBalAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade balcão nula");
		}
		if (service == null) {
			throw new IllegalStateException("Serviço balcão nulo");
		}
		ValidationException exception = new ValidationException("Validation exception");
		try {
			if (numBal == null) {
				Alerts.showAlert("Balcão!!!", "Não preenchido", "sem dados", AlertType.INFORMATION);
				Utils.currentStage(event).close();	
			} else {
				entity = getFormData();
				entity.setTotalBal(virService.findByTotalBal(numBal));
				if (entity.getTotalBal() != 0.00 || entity.getTotalBal() != null) {		
					confereSaldo(event);					
					if (flagSave == 2) {
						updateMaterialBal(event);
						classe = "Balcao Form ";
					}	
					classe = "NF Balcão Form ";
					nota.setCodigoNF(null);
					nota.setNumeroNF(entity.getNnfBal());
					nota.setBalcaoNF(numBal);
					nota.setOsNF(0);
					setBalcaoServices(new ParPeriodoService());
					porPeriodo();
					if (maoObra > 0) {
						gravaComissaoBal();
					}		
					BalcaoCommitDao balCommit = DaoFactory.createBalcaoCommitDao();
					balCommit.gravaBalcao(entity, periodo, nota, adiantamento, listMatCommit);
					notifyDataChangeListerners();
					Utils.currentStage(event).close();
				}	
				if (exception.getErros().size() > 0) {
					throw exception;
				}
			}	
		}	
		catch (ValidationException e) {
			setErrorMessages(e.getErros());
		} 
		catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		} 
		catch (ParseException p) {
			p.printStackTrace();
		}
	}

	@SuppressWarnings("static-access")
	private void gravaComissaoBal() {
		classe = "Balcão Adianto From ";
		classe = "Adiantamento balcão From ";
		Funcionario fun = funService.findById(entity.getFuncionario().getCodigoFun());
		adiantamento.setCodigoFun(fun.getCodigoFun());
		adiantamento.setNomeFun(fun.getNomeFun());
		adiantamento.setCargo(fun.getCargo());
		adiantamento.setSituacao(fun.getSituacao());
		adiantamento.setMesFun(fun.getMesFun());
		adiantamento.setAnoFun(fun.getAnoFun());
		adiantamento.setCargoFun(fun.getCargo().getNomeCargo());
		adiantamento.setSituacaoFun(fun.getSituacao().getNomeSit());
		adiantamento.setSalarioFun(fun.getCargo().getSalarioCargo());
		adiantamento.setComissaoFun(0.00);
				
		adiantamento.setNumeroAdi(null);
		adiantamento.setDataAdi(new Date());
		adiantamento.percComissao = fun.getCargo().getComissaoCargo();
		adiantamento.setValeAdi(0.00);
		adiantamento.setValorAdi(maoObra);
		adiantamento.setComissaoAdi(0.00);
//		cal.setTime(adiantamento.getDataAdi());
		LocalDate dt1 = DataStatic.dateParaLocal(adiantamento.getDataAdi());
//		adiantamento.setMesAdi(cal.get(Calendar.MONTH) + 1);
		adiantamento.setAnoAdi(DataStatic.anoDaData(dt1));
		adiantamento.setMesAdi(DataStatic.mesDaData(dt1));
//		adiantamento.setAnoAdi(cal.get(Calendar.YEAR));
		adiantamento.setSalarioAdi(fun.getCargo().getSalarioCargo());
		adiantamento.setBalcaoAdi(entity.getNumeroBal());
		adiantamento.setOsAdi(0);
		adiantamento.setTipoAdi("C");
		adiantamento.setCargo(fun.getCargo());
		adiantamento.setSituacao(fun.getSituacao());
		adiantamento.calculaComissao();
	}

	public ParPeriodo porPeriodo() {
		try {
			classe = "Período balcão Form ";
			SimpleDateFormat sdfi = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			SimpleDateFormat sdff = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date dti = sdfi.parse("01/01/2001 00:00:00");
			Date dtf = sdff.parse("31/01/2041 00:00:00");

			Integer cod = 1;
			periodo = perService.findById(cod);
			periodo.setIdPeriodo(1);
			periodo.setDtiPeriodo(dti);
			periodo.setDtfPeriodo(dtf);
			perService.update(periodo);
		} 
		catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
		catch (ParseException e) {
			Alerts.showAlert("ParseException ", "Erro Data Periodo ", e.getMessage(), AlertType.ERROR);
		}
		return periodo;
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
	private Balcao getFormData() throws ParseException {
		Balcao obj = new Balcao();
		// instanciando uma exce��o, mas n�o lan�ado - validation exc....
		ValidationException exception = new ValidationException("Validation exception");
// set CODIGO c/ utils p/ transf string em int \\ ou null
		obj.setNumeroBal(Utils.tryParseToInt(textNumeroBal.getText()));
// tst name (trim elimina branco no principio ou final
// lan�a Erros - nome do cpo e msg de erro
 
		if (dpDataBal.getValue() != null) {
			Instant instant = Instant.from(dpDataBal.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataBal(Date.from(instant));
		}
		else {	
			Instant instant = Instant.from(dpDataBal.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataBal(Date.from(instant));
		}	
		if (obj.getDataBal() == null) {
			exception.addErros("data", "Data é obrigatória");
		}

		obj.setFuncionario(comboBoxFunBal.getValue());
		obj.setFuncionarioBal(comboBoxFunBal.getValue().getNomeFun());

		flagPg = 0;
 		if (rbPagamentoDinheiro.isSelected()) {
 		 	obj.setPagamentoBal(1);
 		 	flagPg += 1;
 		}	
 		if (rbPagamentoPix.isSelected()) {
 		 	obj.setPagamentoBal(2);			
 		 	flagPg += 1;
 		}
 		if (rbPagamentoDebito.isSelected()) {
 		 	obj.setPagamentoBal(3);			
 		 	flagPg += 1;
 		}
		if (rbPagamentoCC.isSelected()) {
			obj.setPagamentoBal(4);
 		 	flagPg += 1;
		}
 		if (flagPg == 0) { 
			exception.addErros("pagamento", "Pagamento obrigatório");
		} 		
 		if (flagPg > 1) { 
			exception.addErros("pagamento", "Só pode uma opção");
		}
 		
		if (dpData1oBal.getValue() != null) { 
			Instant instant = Instant.from(dpData1oBal.getValue().atStartOfDay(ZoneId.systemDefault())); 
			obj.setDataPrimeiroPagamentoBal(Date.from(instant));		
		} else {	
			Instant instant = Instant.from(dpData1oBal.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataPrimeiroPagamentoBal(Date.from(instant));
		}

		if (obj.getDataPrimeiroPagamentoBal() == null) { 		
			exception.addErros("data1o", "Data 1o pagto é obrigatória");
		}	
 
		if (obj.getDataPrimeiroPagamentoBal() != null) {
			if (obj.getDataPrimeiroPagamentoBal().before(obj.getDataBal())) {
				exception.addErros("data1o", "Data menor que Data do docto");
			}	
 		}

 		if (textNnfBal.getText() == null || textNnfBal.getText().trim().contentEquals("")) {
 			obj.setNnfBal(0);
 		}
 		else {
 			obj.setNnfBal(Utils.tryParseToInt(textNnfBal.getText()));
 		}

		List<NotaFiscal>  listNF = nfService.findAll();
		for (NotaFiscal nf : listNF) {
			if (nf.getNumeroNF() > ultimaNF) {
				ultimaNF = nf.getNumeroNF();
			}
		}
 		
		if (obj.getNnfBal() <= ultimaNF) {
			exception.addErros("nf", "NF menor/igual a última " + ultimaNF);
		}	
 		 		
		obj.setObservacaoBal(textObservacaoBal.getText());
			
		if (textDescontoBal.getText() == null || textDescontoBal.getText().trim().contentEquals("")) {
			obj.setDescontoBal(0.00);
		}	else {
				obj.setDescontoBal(Utils.tryParseToDouble(textDescontoBal.getText().replace(",", ".")));
			}

			obj.setTotalBal(0.00);
		
		if (obj.getDataBal() != null) {
			cal.setTime(obj.getDataBal());
			obj.setMesBal(cal.get(Calendar.MONTH) + 1);
			obj.setAnoBal(cal.get(Calendar.YEAR));
		}

		if (numBal != null) {
			obj.setTotalBal(virService.findByTotalBal(numBal));
			if (!valor.equals(obj.getTotalBal())) {
				String vlr = Mascaras.formataValor(obj.getTotalBal());
				labelTotalBal.setText(vlr);
				labelTotalBal.viewOrderProperty(); 
				valor = obj.getTotalBal();
				exception.addErros("total", "Conferindo total - Ok ");
			}	
		}
		
		// tst se houve algum (erro com size > 0)
		if (exception.getErros().size() > 0) {
			throw exception;
		}
		return obj;
	}

	private void confereSaldo(ActionEvent event1) {
		try {
			classe = "Material";
			Material mat1 = new Material();
			List<OrcVirtual> vir1 = virService.findByBalcao(numBal);
			for (OrcVirtual ov : vir1) {
				if (ov.getNumeroBalVir().equals(numBal)) {
					mat1 = matService.findById(ov.getMaterial().getCodigoMat());
					if (mat1.getGrupo().getNomeGru().equals("Mão de obra") ||
							mat1.getGrupo().getNomeGru().equals("Mao de obra")) {
						if (mat1.getSaldoMat() < ov.getQuantidadeMatVir()) {
							mat1.setSaldoMat(0.00);
							mat1.entraSaldo(ov.getQuantidadeMatVir());
						}	
						maoObra += mat1.getVendaMat();
					}	
					if (mat1.getGrupo().getNomeGru().equals("Serviço") ||
							mat1.getGrupo().getNomeGru().equals("Servico")) {
						if (mat1.getSaldoMat() < ov.getQuantidadeMatVir()) {
							mat1.setSaldoMat(0.00);
							mat1.entraSaldo(ov.getQuantidadeMatVir());
						}	
						maoObra += mat1.getVendaMat();
					}	
				    matService.saveOrUpdate(mat1);
					if(ov.getQuantidadeMatVir() > mat1.getSaldoMat()) {
					   flagSave = 1;
					   somaSaldo(event1, ov.getQuantidadeMatVir(), mat1.getCodigoMat());
					   if (flagSave == 2) {
						   mat1 = matService.findById(ov.getMaterial().getCodigoMat());
						   ov.setMaterial(mat1);
						   ov.setCustoMatVir(mat1.getPrecoMat());
						   virService.saveOrUpdate(ov);						   
					   }   
					}
				}	
			}
		} catch (DbException e) {
			Alerts.showAlert("Erro saldo OS ", classe, e.getMessage(), AlertType.ERROR);
		}
	}

	private void somaSaldo(ActionEvent event2, double qtd, int cod) {
		Material mat2 = new Material();
		mat2 = matService.findById(cod);
		Optional<ButtonType> result = 
				Alerts.showConfirmation("Saldo insuficiente ", "Deseja incluir? - "  + mat2.getNomeMat());
		if (result.get() == ButtonType.OK) {
	 		Stage parentStage = Utils.currentStage(event2);
			Compromisso com = new Compromisso();
			Parcela par = new Parcela();
			Fornecedor forn =new Fornecedor();
			TipoConsumo tipo = new TipoConsumo();
			Entrada ent = new Entrada();
			data1oBal = entity.getDataPrimeiroPagamentoBal();
			tipoEnt = "bal";
			createDialogEnt(ent, mat2, com, periodo, par, forn, tipo, data1oBal, tipoEnt,     
							"/gui/sgo/EntradaCadastroForm.fxml", parentStage);
			mat2 = matService.findById(cod);
			if (qtd <= mat2.getSaldoMat()) {
				flagSave = 2;
			}
			notifyDataChangeListerners();
			try {
				updateFormData();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}	
	
	private void updateMaterialBal(ActionEvent event3) {
		try {
			Material mat3 = new Material();
			List<OrcVirtual> vir3 = new ArrayList<>();
			vir3 = virService.findByBalcao(numBal);
			for (OrcVirtual v3 : vir3) {				
				if (v3.getNumeroBalVir().equals(numBal)) {
					mat3 = matService.findById(v3.getMaterial().getCodigoMat());
					if (flagSave == 2) {
						mat3.saidaSaldo(v3.getQuantidadeMatVir());
						mat3.getSaldoMat();
						mat3.setSaidaCmmMat(v3.getQuantidadeMatVir());
						classe = "Virtual Form Material ";
						listMatCommit.add(mat3);
						v3.setMaterial(mat3);
// saidaCmmProd p/ calculo cmmm
						int nada = 0;
						if (mat3.getGrupo().getNomeGru().equals("Mão de obra") ||
								mat3.getGrupo().getNomeGru().equals("Mao de obra")) {
							nada = 1;
						} 
						if (mat3.getGrupo().getNomeGru().equals("Serviço") ||
								mat3.getGrupo().getNomeGru().equals("Servico")) {
							nada = 1;
						} 
						if (nada == 0) {	
							if (mat3.getSaldoMat() <= mat3.getEstMinMat()) {
								String nomeMat = mat3.getNomeMat();
								if (mat3.getSaldoMat() == 0.00) {
									Alerts.showAlert("Atenção!!! ", "Estoque zerou ", nomeMat, AlertType.WARNING);
								} else {	
									Alerts.showAlert("Atenção!!! ", "Recompor estoque ", nomeMat, AlertType.WARNING);
								}	
							}
						}
					}	
				}	
			}
		}	
		catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	}
		
	public void updateTableView() {
		if (virService == null) {
			throw new IllegalStateException("Serviço virtual está vazio");
		}
		labelUser.setText(user);
		List<OrcVirtual> listVir = new ArrayList<>();
		if (entity.getNumeroBal() != null) {
			listVir = virService.findByBalcao(entity.getNumeroBal());
			obsListVir = FXCollections.observableArrayList(listVir);
			tableViewOrcVirtual.setItems(obsListVir);
			initEditButtons();
			initRemoveButtons();
		}	
	}	

	// msm processo save p/ fechar
	@FXML
	public void onBtCancelBalAction(ActionEvent event) {
		if (numBal != null) {
			service.remove(numBal);
		}	
		Utils.currentStage(event).close();
	}

	/*
	 * o contrainsts (confere) impede alfa em cpo numerico e delimita tamanho
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setTextFieldInteger(textNumeroBal);
 		Utils.formatDatePicker(dpDataBal, "dd/MM/yyyy");
		Constraints.setTextFieldDouble(textDescontoBal);
		initializeNodes();
		initializeComboBoxFuncionario();
	}

	// comportamento padr�o para iniciar as colunas
	private void initializeNodes() {
		tableColumnNomeMatVir.setCellValueFactory(new PropertyValueFactory<>("NomeMatVir"));
		tableColumnQtdMatVir.setCellValueFactory(new PropertyValueFactory<>("QuantidadeMatVir"));
		Utils.formatTableColumnDouble(tableColumnQtdMatVir, 2);
		tableColumnPrecoMatVir.setCellValueFactory(new PropertyValueFactory<>("PrecoMatVir"));
		Utils.formatTableColumnDouble(tableColumnPrecoMatVir, 2);
		tableColumnTotalMatVir.setCellValueFactory(new PropertyValueFactory<>("TotalMatVir"));
		Utils.formatTableColumnDouble(tableColumnTotalMatVir, 2);
		// para tableview preencher o espa�o da tela scroolpane, referencia do stage
		Stage stage = (Stage) MainSgo.getMainScene().getWindow();
		tableViewOrcVirtual.prefHeightProperty().bind(stage.heightProperty());
	}

	private void initializeComboBoxFuncionario() {
		Callback<ListView<Funcionario>, ListCell<Funcionario>> factory = lv -> new ListCell<Funcionario>() {
			@Override
			protected void updateItem(Funcionario item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeFun());
			}
		};

		comboBoxFunBal.setCellFactory(factory);
		comboBoxFunBal.setButtonCell(factory.call(null));
	}

	@SuppressWarnings("static-access")
	private void createDialogForm(Integer numBal, OrcVirtual virtual, 
						String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			classe = "OrcVirtual Balcão Form ";
//referencia para o controlador = controlador da tela carregada fornListaForm			
			OrcVirtualCadastroFormController controller = loader.getController();
			controller.user = user;
// injetando passando parametro obj 			
			controller.setOrcVirtual(virtual);
			controller.numBal = numBal;
			controller.numOrc = 0;

// injetando servi�os vindo da tela de formulario fornform
			controller.setOrcVirtualService(new OrcVirtualService());
			controller.setBalcaoService(new BalcaoService());
			controller.setMaterialService(new MaterialService());
			controller.loadAssociatedObjects();
//inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
			controller.subscribeDataChangeListener(this);
//carregando o obj no formulario (fornecedorFormControl)			
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Digite Material                                             ");
			dialogStage.setScene(new Scene(pane));
//pode redimencionar a janela: s/n?
			dialogStage.setResizable(false);
//quem e o stage pai da janela?
			dialogStage.initOwner(parentStage);
//travada enquanto n�o sair da tela
 			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Erro carregando tela " + classe, e.getMessage(), AlertType.ERROR);
		}
	}

	private void createDialogEnt(Entrada obj, Material mat2, Compromisso objCom, ParPeriodo objPer, Parcela objPar, 
			Fornecedor objFor, TipoConsumo objTipo, Date data1oBal, String tipoEnt, String absoluteName, Stage parentStage) {
		try {
			classe = "OS Form Entrada ";
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
// referencia para o controlador = controlador da tela carregada fornListaForm			
			EntradaCadastroFormController controller = loader.getController();
			controller.user = user;
			controller.data1oBal = data1oBal;
			controller.tipoEnt = tipoEnt;
 // injetando passando parametro obj
			controller.setPesquisa(mat2.getNomeMat());
			controller.setObjects(obj, mat, objCom, objPer, objPar, objFor, objTipo);
			controller.setServices(new EntradaService(), new FornecedorService(), new MaterialService(), 
					new CompromissoService(), new TipoConsumoService(), new ParPeriodoService(),
					new ParcelaService());
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
	private void initEditButtons() {
		tableColumnEditaVir.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEditaVir.setCellFactory(param -> new TableCell<OrcVirtual, OrcVirtual>() {
			private final Button button = new Button("edita");

			@Override
			protected void updateItem(OrcVirtual virtual, boolean empty) {
				super.updateItem(virtual, empty);

				if (virtual == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> createDialogForm(numBal, virtual, 
						"/gui/sgo/OrcVirtualCadastroForm.fxml", Utils.currentStage(event)));
			}
	 	});
	}

	private void initRemoveButtons() {
		tableColumnRemoveVir.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemoveVir.setCellFactory(param -> new TableCell<OrcVirtual, OrcVirtual>() {
			private final Button button = new Button("exclui");

			@Override
			protected void updateItem(OrcVirtual virtual, boolean empty) {
				super.updateItem(virtual, empty);

				if (virtual == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> removeEntity(virtual));
			}
		});
	}

	private void removeEntity(OrcVirtual virtual) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que deseja excluir?");
		if (result.get() == ButtonType.OK) {
			if (virService == null) {
				throw new IllegalStateException("Serviço virtual está vazio");
			}
			try {
				classe = "OrcVirtual";
				acertaBal(virtual);
				virService.removeVir(virtual.getNumeroVir());
				updateFormData();
				updateTableView();
			} catch (DbIntegrityException e) {
				Alerts.showAlert("Erro removendo objeto", classe, e.getMessage(), AlertType.ERROR);
			} catch (ParseException p) {
				p.getStackTrace();
			}
		}
	}

	private void acertaBal(OrcVirtual virtual) {
			Balcao bal = service.findById(numBal);
			if (bal != null) {
				bal.setTotalBal(virService.findByTotalBal(numBal));
				service.saveOrUpdate(bal);
			}
			try {
				if (bal != null) {
					String vlr = Mascaras.formataValor(bal.getTotalBal());
					labelTotalBal.setText(vlr);
					labelTotalBal.viewOrderProperty();
				}	
			} catch (ParseException e) {
				throw new DbException(e.getMessage()); 
			}
	}

	/*
	 * transforma string da tela p/ o tipo no bco de dados
	 */
	public void updateFormData() throws ParseException {
		if (entity == null) {
			throw new IllegalStateException("Entidade balcão esta nula");
		}
		//  string value of p/ casting int p/ string
			textNumeroBal.setText(String.valueOf(entity.getNumeroBal()));
	// se for uma inclusao, vai posicionar no 1o depto//tipo (First)
			
		if (entity.getDataBal() == null) {
			entity.setDataBal(new Date());
		}
			
		if (entity.getDataBal() != null) {
			dpDataBal.setValue(LocalDate.ofInstant(entity.getDataBal().toInstant(), ZoneId.systemDefault()));
		}

		if (entity.getFuncionario() == null) {
			comboBoxFunBal.getSelectionModel().selectFirst();
		} else {
			comboBoxFunBal.setValue(entity.getFuncionario());
		}

  	 	rbPagamentoDinheiro.setSelected(false);
  	 	rbPagamentoPix.setSelected(false);
  	 	rbPagamentoDebito.setSelected(false);
  	 	rbPagamentoCC.setSelected(false);
  	 	if (entity.getPagamentoBal() != null) {
 			if (entity.getPagamentoBal() == 1) {
 				rbPagamentoDinheiro.setSelected(true);
 			}
 			else {
 				if (entity.getPagamentoBal() == 2) {
 					rbPagamentoPix.setSelected(true);
 				}
 				else {
 					if (entity.getPagamentoBal() == 3) {
 						rbPagamentoDebito.setSelected(true);
 					} else {
 						if (entity.getPagamentoBal() == 4) {
 							rbPagamentoCC.setSelected(true);
 						}
 					}
 				}
 			}	
 		}
 		if (entity.getDataPrimeiroPagamentoBal() == null) {
 	  	 	entity.setDataPrimeiroPagamentoBal(new Date());
  	 	}

		if (entity.getDataPrimeiroPagamentoBal() != null) {
			dpData1oBal.setValue(LocalDate.ofInstant(entity.getDataPrimeiroPagamentoBal().toInstant(), ZoneId.systemDefault()));
		}

		if (entity.getDescontoBal() == null) {
			entity.setDescontoBal(0.00);
		}
		if (entity.getTotalBal() == null) {
			entity.setTotalBal(0.00);
		} 
		if (entity.getNnfBal() == null) {
			entity.setNnfBal(0);
		}

		textNnfBal.setText(String.valueOf(entity.getNnfBal()));
 		textObservacaoBal.setText(entity.getObservacaoBal());
 		
		textDescontoBal.setText(Mascaras.formataValor(entity.getDescontoBal()));

		labelTotalBal.setText(Mascaras.formataValor(entity.getTotalBal()));
		String vlr = Mascaras.formataValor(entity.getTotalBal());
		labelTotalBal.setText(vlr);
		labelTotalBal.viewOrderProperty();
	}	

//	carrega dados do bco cargo dentro obslist via
	public void loadAssociatedObjects() {
		if (funService == null) {
			throw new IllegalStateException("Funcionário Serviço esta nulo");
		}
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int mm = cal.get(Calendar.MONTH) + 1;
		int aa = cal.get(Calendar.YEAR);
// buscando (carregando) bco de dados		
		List<Funcionario> listFun = funService.findByAtivo("Ativo", aa, mm);
		// transf p/ obslist		
				obsListFun = FXCollections.observableArrayList(listFun);
				comboBoxFunBal.setItems(obsListFun);
	}

// mandando a msg de erro para o labelErro correspondente 	
	private void setErrorMessages(Map<String, String> erros) {
		Set<String> fields = erros.keySet();
		labelErrorDataBal.setText((fields.contains("data") ? erros.get("data") : ""));
		labelErrorDataPrimeiroBal.setText((fields.contains("data1o") ? erros.get("data1o") : ""));
		labelErrorPagamentoBal.setText((fields.contains("pagamento") ? erros.get("pagamento") : ""));
		labelErrorNFBal.setText((fields.contains("nf") ? erros.get("nf") : ""));
		labelErrorTotalBal.setText((fields.contains("total") ? erros.get("total") : ""));
		flagSave = 2;
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}
}
