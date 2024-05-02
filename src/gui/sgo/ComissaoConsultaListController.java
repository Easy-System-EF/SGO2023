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
import gui.sgomodel.entities.Comissao;
import gui.sgomodel.entities.MesAno;
import gui.sgomodel.services.ComissaoService;
import gui.sgomodel.services.AnosService;
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
 
public class ComissaoConsultaListController implements Initializable, DataChangeListener {

// inje��o de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private ComissaoService service;
	
	@FXML
 	private TableView<Comissao> tableViewComissao;

	@FXML
	private Label labelTitulo;
	
// c/ entidade e coluna	
 	@FXML
 	private TableColumn<Comissao, String> tableColumnFuncionarioCom;

 	@FXML
 	private TableColumn<Comissao, String> tableColumnBalCom;

 	@FXML
 	private TableColumn<Comissao, String> tableColumnOSCom;

 	@FXML
 	private TableColumn<Comissao, Date> tableColumnDataCom;

   	@FXML
 	private TableColumn<Comissao, String> tableColumnCargoCom;
 	
   	@FXML
   	private TableColumn<Comissao, String> tableColumnSituacaoCom;
   	
   	@FXML
   	private TableColumn<Comissao, Double> tableColumnValorCom;
   	
   	@FXML
   	private Button btMesesCom;
   	 	
	@FXML
	private Label labelUser;

	public String user = "";
 	 		
// carrega aqui os dados Updatetableview (metodo)
 	private ObservableList<Comissao> obsList;
 
// auxiliar
 	String classe = "Comissao Comissão List";
 	static Integer mesConsulta = null;
 	static Integer anoConsulta = null; 	
 	
// injeta a dependencia com set (invers�o de controle de inje�ao)	
 	public void setServices(ComissaoService service) {
 		this.service = service;
 	}
 	
	@FXML
	public void onBtMesesComAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		MesAno obj = new MesAno();
		classe = "Meses Comissão List ";
  		createDialogOpcao("/gui/sgo/MesAnoForm.fxml", parentStage, (MesAnoFormController contM) -> {
			contM.setMesAno(obj);
			contM.setServices(new MesesService(),
							  new AnosService());
			contM.loadAssociatedObjects();
			contM.updateFormData();
 		});
		updateTableView();
  		initializeNodes();
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
		labelTitulo.setText("Comissão");
		tableColumnFuncionarioCom.setCellValueFactory(new PropertyValueFactory<>("NomeFunCom"));
		tableColumnBalCom.setCellValueFactory(new PropertyValueFactory<>("BalcaoCom"));
		tableColumnOSCom.setCellValueFactory(new PropertyValueFactory<>("OSCom"));
		tableColumnDataCom.setCellValueFactory(new PropertyValueFactory<>("dataCom"));
		Utils.formatTableColumnDate(tableColumnDataCom, "dd/MM/yyyy");
		tableColumnCargoCom.setCellValueFactory(new PropertyValueFactory<>("CargoCom"));
		tableColumnSituacaoCom.setCellValueFactory(new PropertyValueFactory<>("SituacaoCom"));
		tableColumnValorCom.setCellValueFactory(new PropertyValueFactory<>("comissaoCom"));
		Utils.formatTableColumnDouble(tableColumnValorCom, 2);
  		// para tableview preencher o espa�o da tela scroolpane, referencia do stage		
 		Stage stage = (Stage) MainSgo.getMainScene().getWindow();
 		tableViewComissao.prefHeightProperty().bind(stage.heightProperty());
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
			throw new IllegalStateException("Serviço está vazio");
 		}
 		labelUser.setText(user);
 		List<Comissao> list = new ArrayList<>();
 		if (mesConsulta != null) {
 			list = service.findMesAno(mesConsulta, anoConsulta);
 			if (list.size() > 0) {
 				list.removeIf(x -> x.getComissaoCom() == 0.00);
 			}	
 		} 				
  		obsList = FXCollections.observableArrayList(list);
  		tableViewComissao.setItems(obsList);
	}

// *  atualiza minha lista dataChanged com dados novos 	
	@Override
	public void onDataChanged() {
		updateTableView();
	}
}
