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
import gui.sgomodel.entities.OrdemServico;
import gui.sgomodel.services.EmpresaService;
import gui.sgomodel.services.OrcVirtualService;
import gui.sgomodel.services.OrcamentoService;
import gui.sgomodel.services.OrdemServicoService;
import gui.sgomodel.services.ReceberService;
import gui.util.Alerts;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
 
public class UltimasVisitasConsultaListController implements Initializable, DataChangeListener {

	@FXML
 	private TableView<OrdemServico> tableViewVisitas;
 	
 	@FXML
 	private Label labelTitulo;
 	
 	@FXML
 	private TableColumn<OrdemServico, Integer>  tableColumnNumeroOs;
 	
 	@FXML
 	private TableColumn<OrdemServico, Date>  tableColumnDataOs;
 	
 	@FXML
 	private TableColumn<OrdemServico, String> tableColumnClienteOs;

 	@FXML
 	private TableColumn<OrdemServico, String> tableColumnPlacaOs;

   	@FXML
 	private TableColumn<OrdemServico, Integer> tableColumnKmOs;
 	 	
   	@FXML
 	private TableColumn<OrdemServico, Double> tableColumnValorOs;

  	@FXML
 	private TableColumn<OrdemServico, OrdemServico> tableColumnList;

	@FXML
	private Button btDadosVisitas;
 	
	@FXML
	private Label labelUser;

  	public String user = "";

// auxiliar 	
 	String classe = "Últimas visitas List ";
 	String nomeTitulo = "Consulta últimas Visitas";
 	String opcao = "a";
 	static String placaDado = null;
 	public static Integer numEmp = 0;
 	
 	public static void setDado(String str) {
 		placaDado = str;
 	} 
 	
// carrega aqui os dados Updatetableview (metodo)
 	private ObservableList<OrdemServico> obsList;
 
 // inje��o de dependenia sem implementar a classe (instanciat)
 // acoplamento forte - implementa via set
 	private OrdemServicoService service;
  	
// injeta a dependencia com set (invers�o de controle de inje�ao)	
 	public void setOrdemServicoService(OrdemServicoService service) {
	 		this.service = service;
 	}
 	
 	public void onBtVisitasAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event); 
		nomeTitulo = "Dados da Visita ";
		classe = "OS ult visita ";
  		createDialogOpcao("/gui/sgo/UltimasVisitasDadosConsultaForm.fxml", (parentStage), 
  				(UltimasVisitasConsultaFormController contUV) -> {
			contUV.subscribeDataChangeListener(this);
			contUV.getFormData();
//			contUV.updateTableView();
 		});
  	 	nomeTitulo = "Consulta últimas Visitas ";
  	 	opcao = "p";
		updateTableView();
	}
 	
 // inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}

// comportamento padr�o para iniciar as colunas 	
 	private void initializeNodes() {
  		labelTitulo.setText(String.format("%s ", nomeTitulo));
  		tableColumnNumeroOs.setCellValueFactory(new PropertyValueFactory<>("numeroOS"));
		tableColumnDataOs.setCellValueFactory(new PropertyValueFactory<>("dataOS"));
		Utils.formatTableColumnDate(tableColumnDataOs, "dd/MM/yyyy");
   		tableColumnClienteOs.setCellValueFactory(new PropertyValueFactory<>("clienteOS"));
   		tableColumnPlacaOs.setCellValueFactory(new PropertyValueFactory<>("placaOS"));
  		tableColumnKmOs.setCellValueFactory(new PropertyValueFactory<>("kmOS"));
  		tableColumnValorOs.setCellValueFactory(new PropertyValueFactory<>("valorOS"));
  		Utils.formatTableColumnDouble(tableColumnValorOs, 2);
 		// para tableview preencher o espa�o da tela scroolpane, referencia do stage		
		Stage stage = (Stage) MainSgo.getMainScene().getWindow();
		tableViewVisitas.prefHeightProperty().bind(stage.heightProperty());
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
			throw new IllegalStateException("Serviço Rep está vazio");
 		}
 		labelUser.setText(user);
 		List<OrdemServico> list = new ArrayList<>();
 		if (opcao == "a") {
 			list = service.findAll();
 		} else {
 	 		list = service.findByPlaca(placaDado);
 		}
  		labelTitulo.setText(String.format("%s ", nomeTitulo));
  		obsList = FXCollections.observableArrayList(list);
  		tableViewVisitas.setItems(obsList);
		initListButtons();
	}
	
	private synchronized <T> void createDialogOpcao(String absoluteName,  
					Stage parentStag, Consumer<T> initializeAction) {
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
 	
// lista da classe subject (form) - guarda lista de obj p/ receber e emitir o evento
		private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

// *   um for p/ cada listener da lista, aciona o metodo onData no DataChangListner...   
		@SuppressWarnings("unused")
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
	
	private void initListButtons() {
		  tableColumnList.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		  tableColumnList.setCellFactory(param -> new TableCell<OrdemServico, OrdemServico>() { 
		    private final Button button = new Button("Lista OS");	    

		    @Override 
		    protected void updateItem(OrdemServico obj, boolean empty) { 
		      super.updateItem(obj, empty); 
		      if (obj == null) { 
		        setGraphic(null); 
		        return; 
		      }
		    
		      setGraphic(button); 
		      button.setOnAction( 
		    		  event -> createDialogImprime(obj, "/gui/sgo/OrdemServicoImprime.fxml", Utils.currentStage(event)));
		    }
		 });  
	}
	
	 // Imprimir	
  	@SuppressWarnings("static-access")
	private synchronized void createDialogImprime(OrdemServico obj, String absoluteName, Stage parentStage) {
  			try {
  	 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
  				Pane pane = loader.load();
  				classe = "OS ult visitas ";
  	 			OrdemServicoImprimeController controller = loader.getController();
  	 			controller.setOrdemServico(obj);
  	 			controller.setOSImprime(new OrdemServicoService(),
  	 									new OrcamentoService(), 
  	 									new OrcVirtualService(),
  	 									new ReceberService(),
  	 									new EmpresaService());
  	 			controller.codOs = obj.getNumeroOS();
  	 			controller.numEmp = numEmp;
  	   			Stage dialogStage = new Stage();
  	 			dialogStage.setTitle("Lista Ordem de Serviço                                             ");
  	 			dialogStage.setScene(new Scene(pane));
  	 			dialogStage.setResizable(false);
  	  			dialogStage.initOwner(parentStage);
  	 			dialogStage.initModality(Modality.WINDOW_MODAL);
  				dialogStage.showAndWait();
  			}
  			catch (IOException e) {
  				e.printStackTrace();
  				Alerts.showAlert("IO Exception ", classe + "Erro carregando tela ", e.getMessage(), AlertType.ERROR);
  			}
  	  	}
 }
