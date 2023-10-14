package gui.sgo;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.MainSgo;
import gui.listerneres.DataChangeListener;
import gui.sgcpmodel.entities.consulta.ParPeriodo;
import gui.sgcpmodel.services.ParPeriodoService;
import gui.sgomodel.entities.Cliente;
import gui.sgomodel.entities.Receber;
import gui.sgomodel.services.ClienteService;
import gui.sgomodel.services.ReceberService;
import gui.util.Alerts;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
 
public class ReceberConsultaListPagoController implements Initializable, DataChangeListener {

	@FXML
 	private TableView<Receber> tableViewReceber;
 	
 	@FXML
 	private Label labelTitulo;
 	
 	@FXML
 	private TableColumn<Receber, Integer>  tableColumnNumeroOsRec;
 	
 	@FXML
 	private TableColumn<Receber, Date>  tableColumnDataOsRec;
 	
 	@FXML
 	private TableColumn<Receber, String> tableColumnClienteRec;

   	@FXML
 	private TableColumn<Receber, String> tableColumnPlacaRec;
 	 	
   	@FXML
 	private TableColumn<Receber, Integer> tableColumnParcelaRec;
 	 	
   	@FXML
 	private TableColumn<Receber, Date> tableColumnVencimentoRec;
 	 	
   	@FXML
 	private TableColumn<Receber, Double> tableColumnValorRec;
 	
   	@FXML
 	private TableColumn<Receber, Date> tableColumnPagamentoRec;
 	 	
   	@FXML
 	private TableColumn<Receber, String> tableColumnFormaRec;
 	 	
  	@FXML
 	private TableColumn<Receber, Receber> tableColumnEDITA;

	@FXML
	private Button btClientePago;

 	@FXML
	private Button btPeriodoPago ;

	@FXML
	private Label labelUser;

	public String user = "";
 	 	
// auxiliar 	
 	String classe = "Receber";
 	String nomeTitulo = "Consulta Contas Recebidas";
 	String opcao = "t";
 	static Integer codCli = null;
 	static String pesquisa = null;
 	 	
// carrega aqui os dados Updatetableview (metodo)
 	private ObservableList<Receber> obsList;
 
 // inje��o de dependenia sem implementar a classe (instanciat)
 // acoplamento forte - implementa via set
 	private ReceberService service;
  	
// injeta a dependencia com set (invers�o de controle de inje�ao)	
 	public void setReceberService(ReceberService service) {
	 		this.service = service;
 	}
 	
	Cliente objCli = new Cliente();
	ParPeriodo objPer = new ParPeriodo();
	
 	@SuppressWarnings("static-access")
	public void onBtClienteAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		nomeTitulo = "Contas Recebidas por Cliente";
		opcao = "c";
		classe = "Cliente";
  		createDialogOpcao(objCli, objPer, "/gui/sgo/PesquisaAlphaForm.fxml", (parentStage), 
  				(RecClienteFormController contC) -> {
  			contC.setDestino("List");
  			contC.setSituacao("Recebido");
  			contC.pesquisa = pesquisa;
			contC.setCliente(objCli);
			contC.setService(new ClienteService());
			contC.loadAssociatedObjects();
			contC.subscribeDataChangeListener(this);
			contC.updateFormData();
 		});
//		updateTableView();
	}
 	
	@FXML
	public void onBtPeriodoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		nomeTitulo = "Contas Recebidas por Periodo";
		opcao = "p";
		classe = "Periodo";
  		createDialogOpcao(objCli, objPer, "/gui/sgo/RecPeriodoForm.fxml", (parentStage), 
  				(RecPeriodoFormController contP) -> {
  		contP.setPeriodo(objPer);
  		contP.setPeriodoService(new ParPeriodoService());
		contP.loadAssociatedObjects();
		contP.subscribeDataChangeListener(this);
		contP.updateFormData();
  		});
//		updateTableView();
  	}
  
 // inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}

// comportamento padr�o para iniciar as colunas 	
 	private void initializeNodes() {
  		labelTitulo.setText(String.format("%s ", nomeTitulo));
 		tableColumnNumeroOsRec.setCellValueFactory(new PropertyValueFactory<>("osRec"));
		tableColumnDataOsRec.setCellValueFactory(new PropertyValueFactory<>("dataOsRec"));
		Utils.formatTableColumnDate(tableColumnDataOsRec, "dd/MM/yyyy");
   		tableColumnClienteRec.setCellValueFactory(new PropertyValueFactory<>("nomeClienteRec"));
		tableColumnPlacaRec.setCellValueFactory(new PropertyValueFactory<>("placaRec"));
		tableColumnParcelaRec.setCellValueFactory(new PropertyValueFactory<>("parcelaRec"));
		tableColumnVencimentoRec.setCellValueFactory(new PropertyValueFactory<>("dataVencimentoRec"));
		Utils.formatTableColumnDate(tableColumnVencimentoRec, "dd/MM/yyyy");
 		tableColumnValorRec.setCellValueFactory(new PropertyValueFactory<>("valorRec"));
 		Utils.formatTableColumnDouble(tableColumnValorRec, 2);
		tableColumnPagamentoRec.setCellValueFactory(new PropertyValueFactory<>("dataPagamentoRec"));
		Utils.formatTableColumnDate(tableColumnPagamentoRec, "dd/MM/yyyy");
   		tableColumnFormaRec.setCellValueFactory(new PropertyValueFactory<>("formaPagamentoRec"));
 		// para tableview preencher o espa�o da tela scroolpane, referencia do stage		
		Stage stage = (Stage) MainSgo.getMainScene().getWindow();
		tableViewReceber.prefHeightProperty().bind(stage.heightProperty());
 	}

/* 	
 * carregar o obsList para atz tableview	
 * tst de seguran�a p/ servi�o vazio
 *  criando uma lista para receber os services
 *  instanciando o obsList
 *  acrescenta o botao edit e remove
 */  
 	public void updateTableView() {
 		if (service == null) {
			throw new IllegalStateException("Serviço Rec está vazio");
 		}
 		labelUser.setText(user);
 		List<Receber> list = new ArrayList<>();
 		if (opcao == "t") {
 			list = service.findAllPago();
 		}
 		if (codCli != null) {
 			if (opcao == "c") {
 				list = service.findByIdClientePago(codCli);
 			}	
 		} 
		if (opcao == "p") {
			list = service.findPeriodoPago();
		}	
		if (list.size() == 0) {
			Alerts.showAlert(null, "Contas Recebidas ", "Não há registro para opção ", AlertType.INFORMATION);
		}	
  		labelTitulo.setText(String.format("%s ", nomeTitulo));
  		obsList = FXCollections.observableArrayList(list);
  		tableViewReceber.setItems(obsList);
		notifyDataChangeListerners();
//		initEditButtons();
	}
 	
	@SuppressWarnings("unused")
	private synchronized <T> void createDialogPesquisa(String absoluteName,  Stage parentStag, 
			Consumer<T> initializeAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			 
			T cont = loader.getController();
 			initializeAction.accept(cont);

 			Stage dialogStage = new Stage();
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStag);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", classe, e.getMessage(), AlertType.ERROR);
		}
	}

	private synchronized <T> void createDialogOpcao(Cliente objCli, ParPeriodo objPer, 
			String absoluteName,  Stage parentStag, Consumer<T> initializeAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			 
			T cont = loader.getController();
 			initializeAction.accept(cont);

 			Stage dialogStage = new Stage();
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStag);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", classe, e.getMessage(), AlertType.ERROR);
		}
	}
 	
	
/* 	
* parametro informando qual stage criou essa janela de dialogo - stage parent
* nome da view - absolutename
* carregando uma janela de dialogo modal (s� sai qdo sair dela, tem q instaciar um stage e dps a janela dialog
*/
// 	private synchronized void createDialogForm(Receber obj, String absoluteName, Stage parentStage) {
// 		try {
// 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
//			Pane pane = loader.load();
//			
//// referencia para o controlador = controlador da tela carregada fornListaForm			
//			ReceberFormController controller = loader.getController();
//// injetando passando parametro obj 		
//// injetando servi�os vindo da tela de formulario fornform
//			controller.setReceber(obj);
//			controller.setService(new ReceberService());
//			controller.loadAssociatedObjects();
//// inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
//			controller.subscribeDataChangeListener(this);
////	carregando o obj no formulario (fornecedorFormControl)			
//			controller.updateFormData();
//			
// 			Stage dialogStage = new Stage();
// 			dialogStage.setTitle("Informe data de pagamento                                      ");
// 			dialogStage.setScene(new Scene(pane));
//// pode redimencionar a janela: s/n?
//			dialogStage.setResizable(false);
//// quem e o stage pai da janela?
//			dialogStage.initOwner(parentStage);
//// travada enquanto n�o sair da tela
//			dialogStage.initModality(Modality.WINDOW_MODAL);
//			dialogStage.showAndWait();
//		} catch (IOException e) {
//			e.printStackTrace();
//			Alerts.showAlert("IO Exception", classe, e.getMessage(), AlertType.ERROR);
//		}
// 		catch (ParseException p) {
// 			p.printStackTrace();
// 		}
// 	} 
 	 	
// lista da classe subject (form) - guarda lista de obj p/ receber e emitir o evento
		private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

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
		
//  atualiza minha lista dataChanged com dados novos 	
	@Override
	public void onDataChanged() {
		updateTableView();
	}

/*
 * metodo p/ botao edit do frame
 * ele cria bot�o em cada linha 
 * o cell instancia e cria
*/	
//	private void initEditButtons() {
//		  tableColumnEDITA.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
//		  tableColumnEDITA.setCellFactory(param -> new TableCell<Receber, Receber>() { 
//		    private final Button button = new Button("edita"); 
//		    @Override 
//		    protected void updateItem(Receber obj, boolean empty) { 
//		      super.updateItem(obj, empty); 
//		      if (obj == null) { 
//		        setGraphic(null); 
//		        return; 
//		      } 
//		      setGraphic(button); 
//		      button.setOnAction( 
//						event -> createDialogForm(obj, "/gui/sgo/ReceberForm.fxml", Utils.currentStage(event)));
//			}
//		});
//	}		  
 }
