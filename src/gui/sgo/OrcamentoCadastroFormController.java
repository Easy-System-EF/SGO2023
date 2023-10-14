package gui.sgo;

import java.io.IOException;
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

import application.MainSgo;
import db.DbException;
import db.DbIntegrityException;
import gui.listerneres.DataChangeListener;
import gui.sgomodel.entities.Cliente;
import gui.sgomodel.entities.Funcionario;
import gui.sgomodel.entities.OrcVirtual;
import gui.sgomodel.entities.Orcamento;
import gui.sgomodel.entities.Veiculo;
import gui.sgomodel.services.ClienteService;
import gui.sgomodel.services.FuncionarioService;
import gui.sgomodel.services.MaterialService;
import gui.sgomodel.services.OrcVirtualService;
import gui.sgomodel.services.OrcamentoService;
import gui.sgomodel.services.VeiculoService;
import gui.util.Alerts;
import gui.util.Constraints;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.exception.ValidationException;

/*
 * Monta formaul�rio do or�amento e virtual
 */

public class OrcamentoCadastroFormController implements Initializable, DataChangeListener  {

	private Orcamento entity;
	private Veiculo veiculo;

	/*
	 * dependencia service com metodo set
	 */
	private OrcamentoService service;
	private ClienteService cliService;
	private FuncionarioService funService;
	private OrcVirtualService virService;
	private VeiculoService veiService;

// lista da classe subject (form) - guarda lista de obj p/ receber e emitir o evento
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private GridPane gridPaneOrc;
	
	@FXML
	private TextField textNumeroOrc;

	@FXML
	private DatePicker dpDataOrc;

	@FXML
	private TextField textPesquisa;
	
	@FXML
	private ComboBox<Cliente> comboBoxCliente;

	@FXML
	private ComboBox<Funcionario> comboBoxFuncionario;

	@FXML
	private TextField textPlacaOrc;

	@FXML
	private TextField textKmFinalOrc;

	@FXML
	private TextField textDescontoOrc;

	@FXML
	private Label labelTotalOrc;

	@FXML
	private TableView<OrcVirtual> tableViewOrcVirtual;

	@FXML
	private TableColumn<OrcVirtual, String> tableColumnNomeMat;

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
	private Button btSaveOrc;

	@FXML
	private Button btCancelOrc;

	@FXML
	private Button btPesquisa;
	
	@FXML
	private Label labelErrorDataOrc;

	@FXML
	private Label labelErrorPlacaOrc;

	@FXML
	private Label labelErrorKmFinalOrc;

	@FXML
	private Button btNewvVir;

 	@FXML
 	private Label labelUser;

 // auxiliar
 	public static Integer nivel = null;
 	public String user = "usuário";		
 	String classe = "Orçamento Form ";
 	String pesquisa = "";
	Double vlrAnt = 0.00;
	Integer numOrc = null;
	String os = "null";
	Integer flag = 0;
	
	private ObservableList<Cliente> obsListCli;
	private ObservableList<Funcionario> obsListFun;
	private ObservableList<OrcVirtual> obsListVir;

	@FXML
	public void onBtNewVirAction(ActionEvent event) throws ParseException {
		try {
			if (numOrc == null) {
				if (dpDataOrc.getValue() == null || textPlacaOrc.getText() == null || textKmFinalOrc.getText() == null) {
				Alerts.showAlert("Atenção", null, "O orçamento tem que estar preenchido", AlertType.INFORMATION);
				} else {
					saveOrc();
				}
			} 
			if (entity.getNumeroOrc() != null) {
				Orcamento orc = service.findById(entity.getNumeroOrc());
				numOrc = orc.getNumeroOrc();
				Stage parentStage = Utils.currentStage(event);
// instanciando novo obj depto e injetando via
				OrcVirtual virtual = new OrcVirtual();
				createDialogForm(numOrc, virtual, "/gui/sgo/OrcVirtualCadastroForm.fxml", parentStage);
				setflag(0);
			}
		} catch (ValidationException e) {
			setErrorMessages(e.getErros());
		} 
	}

	private void setflag(int i) {
		this.flag = i;
	}
	
	@FXML
	public void saveOrc() {
		if (entity == null) {
			throw new IllegalStateException("Entidade orçamento nula");
		}
		if (service == null) {
			throw new IllegalStateException("Serviço orçamento nulo");
		}
		try {
			entity = getFormData();
			classe = "Orçamento Form ";
			entity.setTotalOrc(0.00);
			setflag(0);
			service.saveOrUpdate(entity);
			updateFormData();
			notifyDataChangeListerners();
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

	public void setOrcamento(Orcamento entity) {
		this.entity = entity;
	}
	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

 	// * metodo set /p service
	public void setOrcamentoService(OrcamentoService service) {
		this.service = service;
	}

	public void setClienteService(ClienteService cliService) {
		this.cliService = cliService;
	}

	public void setFuncionarioService(FuncionarioService funService) {
		this.funService = funService;
	}

	public void setOrcVirtualService(OrcVirtualService virService) {
		this.virService = virService;
	}

	public void setVeiculoService(VeiculoService veiService) {
		this.veiService = veiService;
	}

	@FXML
	public void onBtPesquisaAction(ActionEvent event) {
		classe = "Cliente";
		pesquisa = "";
		try {
	  		pesquisa = textPesquisa.getText().toUpperCase().trim();
	  		if (pesquisa != "") {
	  			List<Cliente> listCli = cliService.findPesquisa(pesquisa);
				if (listCli.size() == 0) { 
					pesquisa = "";
					Optional<ButtonType> result = Alerts.showConfirmation("Pesquisa sem resultado ", "Deseja incluir?");
					if (result.get() == ButtonType.OK) {
				 		 Stage parentStage = Utils.currentStage(event);
		 		 		 Cliente obj = new Cliente();
		 		 		 createDialogCli(obj, "/sgo/gui/ClienteCadastroForm.fxml", parentStage);
					}
					listCli = cliService.findPesquisa(pesquisa);
			 	}
	  			obsListCli = FXCollections.observableArrayList(listCli);
	  			comboBoxCliente.setItems(obsListCli);
	  			notifyDataChangeListerners();
	  			updateFormData();
	  		}	
		} catch (ParseException e) {
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
	public void onBtSaveOrcAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade orçamento nula");
		}
		if (service == null) {
			throw new IllegalStateException("Serviço orçamento nulo");
		}
		try {
			classe = "Orçamento Form ";
			entity = getFormData();
			
			if (entity.getNumeroOrc() != null) {
				entity.setTotalOrc(virService.findByTotalOrc(entity.getNumeroOrc()));
				if (!entity.getTotalOrc().equals(vlrAnt)) {
					String vlr = Mascaras.formataValor(entity.getTotalOrc());
					classe = "Orçamento Form ";
					labelTotalOrc.setText(vlr);
					labelTotalOrc.viewOrderProperty();
					vlrAnt = 0.01;
					getFormData();
					vlrAnt = entity.getTotalOrc();
				}	
			}
			service.saveOrUpdate(entity);
			updateFormData();
			notifyDataChangeListerners();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErros());
		} 
		catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		} 
		catch (ParseException p) {
			p.printStackTrace();
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
	private Orcamento getFormData() throws ParseException {
		Orcamento obj = new Orcamento();
// instanciando uma exce��o, mas n�o lan�ado - validation exc....
		ValidationException exception = new ValidationException("Validation exception");
// set CODIGO c/ utils p/ transf string em int \\ ou null
		if (numOrc != null) {
			obj.setNumeroOrc(numOrc);
		}
		else {
			obj.setNumeroOrc(Utils.tryParseToInt(textNumeroOrc.getText()));
		}	
// tst name (trim elimina branco no principio ou final
// lan�a Erros - nome do cpo e msg de erro

		if (dpDataOrc.getValue() != null) {
			Instant instant = Instant.from(dpDataOrc.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataOrc(Date.from(instant));
		}
		else {	
			if (dpDataOrc.getValue() == null) {
				exception.addErros("data", "Data é obrigatória");
			}
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(obj.getDataOrc());
		obj.setMesOrc(cal.get(Calendar.MONTH) + 1);
		obj.setAnoOrc(cal.get(Calendar.YEAR));

		obj.setCliente(comboBoxCliente.getValue());
		obj.setClienteOrc(comboBoxCliente.getValue().getNomeCli());

		obj.setFuncionario(comboBoxFuncionario.getValue());
		obj.setFuncionarioOrc(comboBoxFuncionario.getValue().getNomeFun());

		if (textPlacaOrc.getText() == null || textPlacaOrc.getText().trim().contentEquals("")) {
			exception.addErros("placa", "Placa é obrigatória");
		}	else
			{	obj.setPlacaOrc(textPlacaOrc.getText().toUpperCase());
			}
		
		if (textKmFinalOrc.getText() == null || textKmFinalOrc.getText().trim().contentEquals("")) {
			exception.addErros("km", "Km é obrigatória");
		}	else 
			{	obj.setKmFinalOrc(Utils.tryParseToInt(textKmFinalOrc.getText()));
			}
		
		if (textDescontoOrc.getText() == null || textDescontoOrc.getText().trim().contentEquals("")) {
			obj.setDescontoOrc(0.00);
		}	else {
				obj.setDescontoOrc(Utils.tryParseToDouble(textDescontoOrc.getText().replace(",", ".")));
			}

		if (obj.getPlacaOrc() != null) {
			if (obj.getKmInicialOrc() == null) {
				veiculo = veiService.findByPlaca(obj.getPlacaOrc());
				if (veiculo == null) {
					Veiculo veiculo = new Veiculo();
					veiculo.setPlacaVei(obj.getPlacaOrc());
					veiculo.setKmInicialVei(0);
					veiculo.setKmFinalVei(obj.getKmFinalOrc());
					veiculo.setModeloVei(" ");
					veiculo.setAnoVei(0);
					classe = "Veiculo";
					veiService.saveOrUpdate(veiculo);
					obj.setKmInicialOrc(veiculo.getKmInicialVei());
				}
				else {
					obj.setKmInicialOrc(veiculo.getKmInicialVei());
				}	
			}
		}	
		
		if (obj.getPlacaOrc() != null) {
			if (obj.getKmInicialOrc() != null) {
				if (obj.getKmFinalOrc() < obj.getKmInicialOrc()) {
					exception.addErros("km", "Km atual menor que o anterior ");
				}	
			}	
		}
		
		obj.setOsOrc(0);

		if (numOrc != null) {
			obj.setTotalOrc(virService.findByTotalOrc(numOrc));
		}


		if (vlrAnt == 0.01) {
			vlrAnt = entity.getTotalOrc();
			exception.addErros("km", "Conferindo total - Ok");			
		}	
		
// tst se houve algum (erro com size > 0)
		if (exception.getErros().size() > 0) {
			throw exception;
		}
		return obj;
	}

	public void updateTableView() {
		if (virService == null) {
			throw new IllegalStateException("Serviço virtual está vazio");
		}
		labelUser.setText(user);
		if (entity.getNumeroOrc() != null) {
			List<OrcVirtual> listVir = virService.findByOrcto(entity.getNumeroOrc());
			obsListVir = FXCollections.observableArrayList(listVir);
			tableViewOrcVirtual.setItems(obsListVir);
			initEditButtons();
			initRemoveButtons();
		}	
	}

	// msm processo save p/ fechar
	@FXML
	public void onBtCancelOrcAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	/*
	 * o contrainsts (confere) impede alfa em cpo numerico e delimita tamanho
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setTextFieldInteger(textNumeroOrc);
 		Utils.formatDatePicker(dpDataOrc, "dd/MM/yyyy");
		Constraints.setTextFieldInteger(textKmFinalOrc);
		Constraints.setTextFieldDouble(textDescontoOrc);
		Constraints.setTextFieldMaxLength(textPlacaOrc, 07);
		Constraints.setTextFieldMaxLength(textKmFinalOrc, 06);
		Constraints.setTextFieldMaxLength(textPesquisa, 7);
		initializeNodes();
		initializeComboBoxCliente();
		initializeComboBoxFuncionario();
	}

	// comportamento padr�o para iniciar as colunas
	private void initializeNodes() {
		tableColumnNomeMat.setCellValueFactory(new PropertyValueFactory<>("NomeMatVir"));
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

	private void initializeComboBoxCliente() {
		Callback<ListView<Cliente>, ListCell<Cliente>> factory = lv -> new ListCell<Cliente>() {
			@Override
			protected void updateItem(Cliente item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeCli());
			}
		};

		comboBoxCliente.setCellFactory(factory);
		comboBoxCliente.setButtonCell(factory.call(null));
	}

	private void initializeComboBoxFuncionario() {
		Callback<ListView<Funcionario>, ListCell<Funcionario>> factory = lv -> new ListCell<Funcionario>() {
			@Override
			protected void updateItem(Funcionario item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeFun());
			}
		};

		comboBoxFuncionario.setCellFactory(factory);
		comboBoxFuncionario.setButtonCell(factory.call(null));
	}

	@SuppressWarnings("static-access")
	private void createDialogForm(Integer numOrc, OrcVirtual virtual, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			classe = "OrcVirtual";
//referencia para o controlador = controlador da tela carregada fornListaForm			
			OrcVirtualCadastroFormController controller = loader.getController();
			controller.user = user;
// injetando passando parametro obj 			
			controller.setOrcVirtual(virtual);
			controller.numOrc = numOrc;
			controller.numBal = 0;

// injetando servi�os vindo da tela de formulario fornform
			controller.setOrcVirtualService(new OrcVirtualService());
			controller.setOrcamentoService(new OrcamentoService());
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
				button.setOnAction(event -> createDialogForm(numOrc, virtual, 
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
		if (nivel > 1 && nivel < 9) {
			Alerts.showAlert(null, "Atenção", "Operaçaoo não permitida", AlertType.INFORMATION);
		} else {
			Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que deseja excluir?");
			if (result.get() == ButtonType.OK) {
				if (virService == null) {
					throw new IllegalStateException("Serviço virtual está vazio");
				}
				try {
					acertaOrc(virtual);
					classe = "OrcVirtual";
					virService.removeVir(virtual.getNumeroVir());
					setflag(0);
					updateFormData();
					updateTableView();
				} catch (DbIntegrityException e) {
					Alerts.showAlert("Erro removendo objeto", classe, e.getMessage(), AlertType.ERROR);
				} catch (ParseException p) {
					p.getStackTrace();
				}
			}
		}	
	}

	private void acertaOrc(OrcVirtual obj) {
			Orcamento orc = service.findById(obj.getNumeroOrcVir());
			orc.setTotalOrc(0.0);
			orc.setTotalOrc(virService.findByTotalOrc(obj.getNumeroOrcVir()));
  			service.saveOrUpdate(orc);
	}

	/*
	 * transforma string da tela p/ o tipo no bco de dados
	 */
	public void updateFormData() throws ParseException {
		if (entity == null) {
			throw new IllegalStateException("Entidade orçamento esta nula");
		}
		//  string value of p/ casting int p/ string
			textNumeroOrc.setText(String.valueOf(entity.getNumeroOrc()));
			numOrc = entity.getNumeroOrc();
	// se for uma inclusao, vai posicionar no 1o depto//tipo (First)
			
		if (entity.getDataOrc() == null) {
			entity.setDataOrc(new Date());
		}
			
		if (entity.getDataOrc() != null) {
			dpDataOrc.setValue(LocalDate.ofInstant(entity.getDataOrc().toInstant(), ZoneId.systemDefault()));
		}
		if (entity.getCliente() == null) {
			comboBoxCliente.getSelectionModel().selectFirst();
		} else {
			comboBoxCliente.setValue(entity.getCliente());
		}

		if (entity.getFuncionario() == null) {
			comboBoxFuncionario.getSelectionModel().selectFirst();
		} else {
			comboBoxFuncionario.setValue(entity.getFuncionario());
		}

		textPlacaOrc.setText(entity.getPlacaOrc());
		textKmFinalOrc.setText(String.valueOf(entity.getKmFinalOrc()));
		String vlr = "0.01";
		if (entity.getDescontoOrc() == null) {
			entity.setDescontoOrc(0.00);
		}

		if (entity.getTotalOrc() == null) {
			entity.setTotalOrc(0.00);
		}

		vlr = Mascaras.formataValor(entity.getDescontoOrc());
		textDescontoOrc.setText(vlr);

		vlrAnt = entity.getTotalOrc();
		vlr = Mascaras.formataValor(entity.getTotalOrc());
		labelTotalOrc.setText(vlr);
		labelTotalOrc.viewOrderProperty();
		textPesquisa.setText(pesquisa);
	}	

//	carrega dados do bco cargo dentro obslist via
	public void loadAssociatedObjects() {
		if (cliService == null) {
			throw new IllegalStateException("Virtual Serviço esta nulo");
		}
		if (funService == null) {
			throw new IllegalStateException("Funcionario Serviço esta nulo");
		}
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int mm = cal.get(Calendar.MONTH) + 1;
		int aa = cal.get(Calendar.YEAR);
// buscando (carregando) bco de dados		
		List<Cliente> listCli = cliService.findAll();
		List<Funcionario> listFun = funService.findByAtivo("Ativo", aa, mm);
// transf p/ obslist		
		obsListCli = FXCollections.observableArrayList(listCli);
		obsListFun = FXCollections.observableArrayList(listFun);
		comboBoxCliente.setItems(obsListCli);
		comboBoxFuncionario.setItems(obsListFun);
	}

// mandando a msg de erro para o labelErro correspondente 	
	private void setErrorMessages(Map<String, String> erros) {
		Set<String> fields = erros.keySet();
		labelErrorDataOrc.setText((fields.contains("data") ? erros.get("data") : ""));
		labelErrorPlacaOrc.setText((fields.contains("placa") ? erros.get("placa") : ""));
		labelErrorKmFinalOrc.setText((fields.contains("km") ? erros.get("km") : ""));
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}
	
	private void createDialogCli(Cliente obj, String absoluteName, Stage parentStage) {
		try {
			classe = "Cliente ";
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			ClienteCadastroFormController controller = loader.getController();
			controller.user = user;
			controller.setCliente(obj);
			controller.setClienteService(new ClienteService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Digite Cliente                                             ");
 			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", classe + "Erro carregando tela", e.getMessage(), AlertType.ERROR);
		}
 	} 
}
