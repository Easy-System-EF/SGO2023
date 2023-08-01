package gui.sgo;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.MainSgo;
import gui.listerneres.DataChangeListener;
import gui.sgomodel.entities.FolhaMes;
import gui.sgomodel.services.AdiantamentoService;
import gui.sgomodel.services.AnosService;
import gui.sgomodel.services.FolhaMesService;
import gui.sgomodel.services.FuncionarioService;
import gui.sgomodel.services.MesesService;
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
 
public class FolhaMesConsultaListController implements Initializable, DataChangeListener {

// inje��o de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private FolhaMesService service;
	
	@FXML
 	private TableView<FolhaMes> tableViewDados;

	@FXML
	private Label labelTitulo;
	
// c/ entidade e coluna	
	
 	@FXML
 	private TableColumn<FolhaMes, String> tableColumnNomeDados;

   	@FXML
 	private TableColumn<FolhaMes, String> tableColumnCargoDados;
 	
   	@FXML
   	private TableColumn<FolhaMes, String> tableColumnSituacaoDados;
   	
   	@FXML
   	private TableColumn<FolhaMes, Double> tableColumnSalarioDados;
   	
   	@FXML
   	private TableColumn<FolhaMes, Double> tableColumnComissaoDados;
   	
   	@FXML
   	private TableColumn<FolhaMes, Double> tableColumnAdiantamentoDados;

   	@FXML
   	private TableColumn<FolhaMes, Double> tableColumnReceberDados;   	
   	
   	@FXML
   	private TableColumn<FolhaMes, Double> tableColumnTotalDados;   	
   	
   	@FXML
   	private Button btMeses;
   	 	
	@FXML
	private Label labelUser;

	public String user = "";
 	public static String nomeMes = null;
 	public static Integer numAno = null;
// carrega aqui lista Updatetableview (metodo)
 	private ObservableList<FolhaMes> obsList;
 
// auxiliar
 	String classe = "Dados Folha ";
 	 
 	// injeta a dependencia com set (invers�o de controle de inje�ao)	
 	public void setServices(FolhaMesService service) {
 		this.service = service;
 	}
 	
	@FXML
	public void onBtMesesAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		FolhaMes obj = new FolhaMes();
		classe = "Meses ";
  		createDialogOpcao("/gui/sgo/MesAnoFForm.fxml", parentStage, (FolhaMesConsultaFormController contM) -> {
			contM.setFolhaMes(obj);		
			contM.setServices(new FolhaMesService(),
						      new AdiantamentoService(),
						      new FuncionarioService(),
							  new MesesService(),
							  new AnosService());
			contM.loadAssociatedObjects();
			contM.updateFormData();
 		});
		updateTableView();
	}
 	 	
 	private synchronized <T> void createDialogOpcao(String absoluteName, Stage parentStage, Consumer<T> initializeAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			 
			T cont = loader.getController();
 			initializeAction.accept(cont);

 			Stage dialogStage = new Stage();
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", classe, e.getMessage(), AlertType.ERROR);
		}
	}
 	
 	 // inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}

// comportamento padr�o para iniciar as colunas 	
 	private void initializeNodes() {
		labelTitulo.setText("Folha Mes ");
		tableColumnNomeDados.setCellValueFactory(new PropertyValueFactory<>("NomeDados"));
		tableColumnCargoDados.setCellValueFactory(new PropertyValueFactory<>("CargoDados"));
		tableColumnSituacaoDados.setCellValueFactory(new PropertyValueFactory<>("SituacaoDados"));
		tableColumnSalarioDados.setCellValueFactory(new PropertyValueFactory<>("SalarioDados"));
		tableColumnComissaoDados.setCellValueFactory(new PropertyValueFactory<>("ComissaoDados"));
		tableColumnAdiantamentoDados.setCellValueFactory(new PropertyValueFactory<>("ValeDados"));
		tableColumnReceberDados.setCellValueFactory(new PropertyValueFactory<>("ReceberDados"));
		tableColumnTotalDados.setCellValueFactory(new PropertyValueFactory<>("TotalDados"));
  		// para tableview preencher o espa�o da tela scroolpane, referencia do stage		
 		Stage stage = (Stage) MainSgo.getMainScene().getWindow();
 		tableViewDados.prefHeightProperty().bind(stage.heightProperty());
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
			throw new IllegalStateException("Serviço Dados está vazio");
 		}
 		labelUser.setText(user);
 		List<FolhaMes> list = new ArrayList<>();
 		if (nomeMes != null) {
 			labelTitulo.setText("Folha mes : " + nomeMes + "/" + numAno);
 			classe = "Dados Folha";
 			list = service.findAll();
 			obsList = FXCollections.observableArrayList(list);
 		}	
		tableViewDados.setItems(obsList);
	}

// *  atualiza minha lista dataChanged com dados novos 	
	@Override
	public void onDataChanged() {
		updateTableView();
	}
}
