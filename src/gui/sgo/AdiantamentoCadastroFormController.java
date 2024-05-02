package gui.sgo;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.text.ParseException;
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
import gui.sgcpmodel.entities.TipoConsumo;
import gui.sgcpmodel.entities.consulta.ParPeriodo;
import gui.sgcpmodel.services.CompromissoService;
import gui.sgcpmodel.services.FornecedorService;
import gui.sgcpmodel.services.ParPeriodoService;
import gui.sgcpmodel.services.TipoConsumoService;
import gui.sgomodel.entities.Adiantamento;
import gui.sgomodel.entities.Funcionario;
import gui.sgomodel.services.AdiantamentoService;
import gui.sgomodel.services.CargoService;
import gui.sgomodel.services.EmpresaService;
import gui.sgomodel.services.FuncionarioService;
import gui.sgomodel.services.SituacaoService;
import gui.util.Alerts;
import gui.util.CalculaParcela;
import gui.util.Constraints;
import gui.util.Mascaras;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.exception.ValidationException;

public class AdiantamentoCadastroFormController implements Initializable, Serializable {

	private static final long serialVersionUID = 1L;

	private Adiantamento entity;
	private Funcionario entFun;
	private Compromisso entCom;
	private ParPeriodo entPer;
	private Fornecedor entFor;
	private TipoConsumo entTip;

	/*
	 * dependencia service com metodo set
	 */
	private AdiantamentoService service;
	private FuncionarioService funService;
	private FornecedorService forService;
	private CompromissoService comService;
	private TipoConsumoService tipoService;
	private ParPeriodoService perService;

// lista da classe subject (form) - guarda lista de obj p/ receber e emitir o evento
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField textNumeroAdi;

	@FXML
	private TextField textIniciais;

	@FXML
	private ComboBox<Funcionario> comboBoxFun;

	@FXML
	private DatePicker dpDataAdi;

	@FXML
	private TextField textAdiantamentoAdi;

	@FXML
	private Button btSaveAdi;

	@FXML
	private Button btCancelAdi;

	@FXML
	private Button btSairAdi;

	@FXML
	private Button btPesquisa;

	@FXML
	private Label labelErrorNomeFun;

	@FXML
	private Label labelErrorDataAdi;

	@FXML
	private Label labelErrorValeAdi;
	
	@FXML
	private Label labelUser;

	// auxiliar
	Calendar cal = Calendar.getInstance();
	Date data = new Date();
	double vale = 0.00;

	public String user = "usuário";
	String pesquisa = "";
	String classe = "Adiantamento Adi Form";
	public static Integer numEmp = null;
	public static String path = null;

	private ObservableList<Funcionario> obsListFun;

	public void setAdiantamento(Adiantamento entity, Funcionario entFun, Compromisso entCom, 
			ParPeriodo entPer, TipoConsumo entTip) {
		this.entity = entity;
		this.entFun = entFun;
		this.entCom = entCom;
		this.entPer = entPer;
		this.entTip = entTip;
	}

	// * metodo set /p service
	public void setServices(AdiantamentoService service, FuncionarioService funService, FornecedorService forService, 
			CompromissoService comService, TipoConsumoService tipoService, ParPeriodoService perService) {
		this.service = service;
		this.funService = funService;
		this.forService = forService;
		this.comService = comService;
		this.tipoService = tipoService;
		this.perService = perService;
	}

//  * o controlador tem uma lista de eventos q permite distribui��o via metodo abaixo
// * recebe o evento e inscreve na lista
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtPesquisaAction(ActionEvent event) {
		classe = "Adiantamento Adi Form";
		pesquisa = "";
		try {
			pesquisa = textIniciais.getText().toUpperCase().trim();
			if (pesquisa != "") {
				List<Funcionario> listFun = funService.findPesquisa(pesquisa, new Date());
				if (listFun.size() == 0) {
					Optional<ButtonType> result = Alerts.showConfirmation("Pesquisa sem resultado ", "Deseja incluir?");
					if (result.get() == ButtonType.OK) {
						Stage parentStage = Utils.currentStage(event);
						createDialogForm(entFun, "/gui/sgb/FuncionarioForm.fxml", parentStage);
					}
					listFun = funService.findPesquisa(pesquisa, new Date());
				}
				if(listFun.size() > 0) {
					listFun.removeIf(x -> x.getNomeFun().contains("Consumo Próprio"));
					obsListFun = FXCollections.observableArrayList(listFun);
					comboBoxFun.setItems(obsListFun);
					notifyDataChangeListerners();
					updateFormData();
				}	
			}
		} catch (ParseException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto", classe, e.getMessage(), AlertType.ERROR);
		} catch (DbException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	}

	/*
	 * vamos instanciar e salvar no bco de dados meu obj entity (l� em cima) vai
	 * receber uma chamada do getformdata metodo q busca dados do formulario
	 * convertidos getForm (string p/ int ou string) pegou no formulario e retornou
	 * (convertido) p/ jogar na variavel entity chamo o service na rotina saveupdate
	 * e mando entity vamos tst entity e service = null -> n�o foi injetado para
	 * fechar a janela, pego a referencia para janela atual (event) e close
	 * DataChangeListner classe subjetc - q emite o evento q muda dados, vai guardar
	 * uma lista qdo ela salvar obj com sucesso, � s� notificar (juntar) recebe l�
	 * no listController
	 */
	@FXML
	public void onBtSaveAdiAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade Adiantamento nula ");
		}
		if (service == null) {
			throw new IllegalStateException("Serviço nulo");
		}
		try {
			int sai = 0;
			if (entity.getNumeroAdi() != null) {
				sai = 1;
			}
			entity = getFormData();
			service.saveOrUpdate(entity);
			imprimeRecibo(entity);
			entradaCompromisso(entity);
			notifyDataChangeListerners();
			if (sai == 1) {
				Utils.currentStage(event).close();
			}	
			entity = new Adiantamento();
			updateFormData();
		} catch (ValidationException e) {
			setErrorMessages(e.getErros());
		} catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		} catch (ParseException p) {
			p.printStackTrace();
		}
	}

	private void entradaCompromisso(Adiantamento entity2) {
		classe = "Fornecedor Adi Form";
		List<Fornecedor> listFor = forService.findPesquisa("Adiantamento");
		for (Fornecedor f : listFor) {
			if (f.getRazaoSocial().equals("Adiantamento")) {
				entFor = f;
			}	
		}

		classe = "Período Adi Form";		
		List<ParPeriodo> listPer = perService.findAll();
		for (ParPeriodo p : listPer) {
			entPer = p;
			entPer.setFornecedor(p.getFornecedor());
			entPer.setTipoConsumo(p.getTipoConsumo());
			entPer.setIdPeriodo(p.getIdPeriodo());
		}	
			
		classe = "Tipo Consumo Adi Form";		
		List<TipoConsumo> listTipo = tipoService.findAll();
		for (TipoConsumo tc : listTipo) {
			if (tc.getNomeTipo().equals(entPer.getTipoConsumo().getNomeTipo())) {
				entTip = tc;
			}
		}	
		
		classe = "Compromisso Adi Form";
		entCom.setIdCom(null);
		entCom.setNomeFornecedorCom(entFor.getRazaoSocial());
		entCom.setCodigoFornecedorCom(entFor.getCodigo());
		entCom.setNomeFornecedorCom(entFor.getRazaoSocial());
		entCom.setNnfCom(entity.getNumeroAdi());
		entCom.setDataCom(entity.getDataAdi());
		entCom.setDataVencimentoCom(entity.getDataAdi());
		entCom.setValorCom(entity.getAdiantamentoAdi());
		entCom.setParcelaCom(entFor.getParcela());
		entCom.setPrazoCom(entFor.getPrazo());
		entCom.setFornecedor(entFor);
		entCom.setTipoConsumo(entTip);
		entCom.setParPeriodo(entPer);
		entCom.setSituacaoCom(1);
		comService.saveOrUpdate(entCom);
		CalculaParcela.parcelaCreate(entCom);
	}

	private void imprimeRecibo(Adiantamento entity2) {
		AdiantamentoImprimeController adiantoImpr = new AdiantamentoImprimeController();
		AdiantamentoImprimeController.numEmp = numEmp;
		adiantoImpr.setAdiantamento(entity2);
		adiantoImpr.setServices(new EmpresaService());
		adiantoImpr.imprimeAdiantamento();
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
	private Adiantamento getFormData() throws ParseException {
		Adiantamento obj = new Adiantamento();
// instanciando uma exce��o, mas n�o lan�ado - validation exc....
		ValidationException exception = new ValidationException("Validation exception");
// set CODIGO c/ utils p/ transf string em int \\ ou null		
		obj.setNumeroAdi(Utils.tryParseToInt(textNumeroAdi.getText()));
// tst name (trim elimina branco no principio ou final
// lan�a Erros - nome do cpo e msg de erro

		if (dpDataAdi.getValue() != null) {
			Instant instant = Instant.from(dpDataAdi.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataAdi(Date.from(instant));
		}
		if (dpDataAdi.getValue() == null) {
			exception.addErros("data", "Data é obrigatória");
		}
		
		obj.setFunAdi(comboBoxFun.getValue().getCodigoFun());

		obj.setNomeFunAdi(comboBoxFun.getValue().getNomeFun());
		
		obj.setCargoAdi(comboBoxFun.getValue().getCargoFun());
		
		obj.setSituacaoAdi(comboBoxFun.getValue().getSituacaoFun());
		
		textAdiantamentoAdi.getText().replace(".", "");
		obj.setAdiantamentoAdi(Utils.formatDecimalIn(textAdiantamentoAdi.getText().replace(".", "")));
		if (obj.getAdiantamentoAdi() == null || obj.getAdiantamentoAdi() == 0.0) {
			exception.addErros("valor", "Valor é obrigatório");
		} else {
			if (obj.getAdiantamentoAdi() > (40 * comboBoxFun.getValue().getCargo().getSalarioCargo()) / 100) {
				exception.addErros("valor", "Estouro do limite de adiantamento");
			}
		}
		
		Date dataHj = new Date();
		cal = Calendar.getInstance();
		cal.setTime(dataHj);
		int mm = cal.get(Calendar.MONTH) + 1;
		int aa = cal.get(Calendar.YEAR);
		cal.setTime(obj.getDataAdi());
		obj.setMesAdi(cal.get(Calendar.MONTH) + 1);
		obj.setAnoAdi(cal.get(Calendar.YEAR));
		if (mm != obj.getMesAdi() || aa != obj.getAnoAdi()) {
			exception.addErros("data", "mes ou ano inválido!!!");
		}
		
		double soma = service.findByTotalAdi(mm, aa, obj.getFunAdi());
		soma += obj.getAdiantamentoAdi();
		if (soma > (40 * comboBoxFun.getValue().getSalarioFun()) / 100) {
			exception.addErros("valor", "Estouro do limite de adiantamento");
		}
		List<Adiantamento> list = service.findMes(mm, aa);
		list.forEach(a -> {
			if (a.getNumeroAdi() != obj.getNumeroAdi()) {
				if (a.getDataAdi() == obj.getDataAdi()) {
					exception.addErros("data", "Já existe adiantamento nesta data");
				}	
			}
		});
		
		// tst se houve algum (erro com size > 0)
// tst se houve algum (erro com size > 0)
		if (exception.getErros().size() > 0) {
			throw exception;
		}
		return obj;
	}

// msm processo save p/ fechar	
	@FXML
	public void onBtCancelAdiAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	public void onBtSairAdiAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	private void createDialogForm(Funcionario obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

// referencia para o controlador = controlador da tela carregada fornListaForm			
			FuncionarioCadastroFormController controller = loader.getController();
			controller.user = user;
			// injetando passando parametro obj
			controller.setFuncionario(obj);
// injetando tb o forn service vindo da tela de formulario fornform
			controller.setServices(new FuncionarioService(), new CargoService(), new SituacaoService());
			controller.loadAssociatedObjects();
// inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
//			controller.subscribeDataChangeListener(this);
//	carregando o obj no formulario (fornecedorFormControl)			
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Digite Adiantamento                                             ");
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
			Alerts.showAlert("IO Exception", classe + "Erro carregando tela", e.getMessage(), AlertType.ERROR);
		}
	}

	/*
	 * o contrainsts (confere) impede alfa em cpo numerico e delimita tamanho
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setTextFieldInteger(textNumeroAdi);
 		Constraints.setTextFieldDouble(textAdiantamentoAdi);
		Constraints.setTextFieldMaxLength(textIniciais, 7);
		Utils.formatDatePicker(dpDataAdi, "dd/MM/yyyy");
		initializeComboBoxFuncionario();
	}

	private void initializeComboBoxFuncionario() {
		Callback<ListView<Funcionario>, ListCell<Funcionario>> factory = lv -> new ListCell<Funcionario>() {
			@Override
			protected void updateItem(Funcionario item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeFun());
			}
		};

		comboBoxFun.setCellFactory(factory);
		comboBoxFun.setButtonCell(factory.call(null));
	}

	/*
	 * transforma string da tela p/ o tipo no bco de dados
	 */
	public void updateFormData() throws ParseException {
		if (entity == null) {
			throw new IllegalStateException("Entidade esta nula");
		}
		
		labelUser.setText(user);
//  string value of p/ casting int p/ string 		
		textNumeroAdi.setText(String.valueOf(entity.getNumeroAdi()));
		if (entity.getNomeFun() == null) {
			comboBoxFun.getSelectionModel().selectFirst();
		} else {
			comboBoxFun.setValue(entity);
		}

		if (entity.getDataAdi() == null) {
			entity.setDataAdi(new Date());
		}

		if (entity.getDataAdi() != null) {
			dpDataAdi.setValue(LocalDate.ofInstant(entity.getDataAdi().toInstant(), ZoneId.systemDefault()));
		}

		if (entity.getAdiantamentoAdi() == null || entity.getAdiantamentoAdi() == 0) {
			entity.setAdiantamentoAdi(0.00);
		} else {
			vale = entity.getAdiantamentoAdi();
		}
		
		String vlr = Mascaras.formataValor(entity.getAdiantamentoAdi());
		textAdiantamentoAdi.setText(vlr);
		
	}

	public void loadAssociatedObjects() {
		if (funService == null) {
			throw new IllegalStateException("Funcionario Serviço esta nulo");
		}
// buscando (carregando) os cargos q est�o no bco de dados via Dialogform
 		labelUser.setText(user);
		List<Funcionario> list = funService.findByAtivo("Ativo", new Date());
//		"Ativo", cal.get(Calendar.YEAR) , (data.getMonth()+ 1));
// transf p/ obslist		
		list.removeIf(x -> x.getNomeFun().contains("Consumo Próprio"));
		obsListFun = FXCollections.observableArrayList(list);
		comboBoxFun.setItems(obsListFun);
	}

// mandando a msg de erro para o labelErro correspondente 	
	private void setErrorMessages(Map<String, String> erros) {
		Set<String> fields = erros.keySet();
//		labelErrorNomeFun.setText((fields.contains("nome") ? erros.get("nome") : ""));
		labelErrorDataAdi.setText((fields.contains("data") ? erros.get("data") : ""));
		labelErrorValeAdi.setText((fields.contains("valor") ? erros.get("valor") : ""));
	}
}
