package gui.sgo;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.MainSgo;
import gui.listerneres.DataChangeListener;
import gui.sgcpmodel.services.CompromissoService;
import gui.sgcpmodel.services.ParPeriodoService;
import gui.sgcpmodel.services.ParcelaService;
import gui.sgcpmodel.services.TipoConsumoService;
import gui.sgomodel.entities.Adiantamento;
import gui.sgomodel.entities.Balcao;
import gui.sgomodel.entities.FechamentoMes;
import gui.sgomodel.entities.Funcionario;
import gui.sgomodel.entities.Orcamento;
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
 
public class FechamentoMesConsultaListController implements Initializable, DataChangeListener, Serializable {

	private static final long serialVersionUID = 1L;

// inje��o de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private FechamentoMesService service;
	
	@FXML
 	private TableView<FechamentoMes> tableViewMensal;

	@FXML
	private Label labelTitulo;
	
// c/ entidade e coluna	
	
 	@FXML
 	private TableColumn<FechamentoMes, Integer> tableColumnOsMensal;

 	@FXML
 	private TableColumn<FechamentoMes, String> tableColumnBalMensal;

   	@FXML
 	private TableColumn<FechamentoMes, String> tableColumnDataMensal;
 	
   	@FXML
 	private TableColumn<FechamentoMes, String> tableColumnClienteMensal;
 	
   	@FXML
 	private TableColumn<FechamentoMes, String> tableColumnFuncionarioMensal;
 	
   	@FXML
   	private TableColumn<FechamentoMes, String> tableColumnValorOsMensal;
   	
   	@FXML
   	private TableColumn<FechamentoMes, String> tableColumnValorMaterialMensal;
   	
   	@FXML
   	private TableColumn<FechamentoMes, String> tableColumnValorComissaoMensal;
   	
   	@FXML
   	private TableColumn<FechamentoMes, String> tableColumnValorResultadoMensal;

   	@FXML
   	private TableColumn<FechamentoMes, String> tableColumnValorAcumuladoMensal;   	
   	
   	@FXML
   	private Button btMesesMensal;
   	 	
	@FXML
	private Label labelUser;

	public String user = "";
 	 		
// carrega aqui lista Updatetableview (metodo)
 	private ObservableList<FechamentoMes> obsList;
 
// auxiliar
 	String classe = "Dados Fechamento Mensal";
 	public static String nomeMes = null;
 	public static Integer numAno = null;
 	 
 	// injeta a dependencia com set (invers�o de controle de inje�ao)	
 	public void setServices(FechamentoMesService service) {
 		this.service = service;
 	}
 	
	@FXML
	public void onBtMesesMensalAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		FechamentoMes obj = new FechamentoMes();
		Adiantamento objAdi = new Adiantamento();
		Orcamento objOrc = new Orcamento();
		Funcionario objFun = new Funcionario();
		Balcao objBal = new Balcao();
		Receber objRec = new Receber();
		classe = "Meses ";
  		createDialogOpcao("/gui/sgo/MesAnoMForm.fxml", parentStage, 
  				(FechamentoMesConsultaFormController contF) -> {
			contF.setDadosEntityes(obj, objOrc, objAdi, objFun, objBal, objRec);
			contF.setServices(new FechamentoMesService(),
						      new AdiantamentoService(),
							  new MesesService(),
							  new AnosService(),
							  new OrdemServicoService(),
							  new OrcamentoService(),
							  new OrcVirtualService(),
							  new FuncionarioService(),
							  new BalcaoService(),
							  new TipoConsumoService(),
							  new CompromissoService(),
	 						  new ParcelaService(),
	 						  new ParPeriodoService(),
	 						  new ReceberService());
			contF.loadAssociatedObjects();
			contF.updateFormData();
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
		labelTitulo.setText("Fechamento Mensal ");
		tableColumnOsMensal.setCellValueFactory(new PropertyValueFactory<>("OsMensal"));
		tableColumnBalMensal.setCellValueFactory(new PropertyValueFactory<>("BalMensal"));
		tableColumnDataMensal.setCellValueFactory(new PropertyValueFactory<>("DataMensal"));
//		Utils.formatTableColumnDate(tableColumnDataMensal, "dd/MM/yyyy");
		tableColumnClienteMensal.setCellValueFactory(new PropertyValueFactory<>("ClienteMensal"));
		tableColumnFuncionarioMensal.setCellValueFactory(new PropertyValueFactory<>("FuncionarioMensal"));
		tableColumnValorOsMensal.setCellValueFactory(new PropertyValueFactory<>("ValorOsMensal"));
//		Utils.formatTableColumnDouble(tableColumnValorOsMensal, 2);
		tableColumnValorMaterialMensal.setCellValueFactory(new PropertyValueFactory<>("ValorMaterialMensal"));
//		Utils.formatTableColumnDouble(tableColumnValorMaterialMensal, 2);
		tableColumnValorComissaoMensal.setCellValueFactory(new PropertyValueFactory<>("ValorComissaoMensal"));
//		Utils.formatTableColumnDouble(tableColumnValorComissaoMensal, 2);
		tableColumnValorResultadoMensal.setCellValueFactory(new PropertyValueFactory<>("ValorResultadoMensal"));
//		Utils.formatTableColumnDouble(tableColumnValorResultadoMensal, 2);
		tableColumnValorAcumuladoMensal.setCellValueFactory(new PropertyValueFactory<>("ValorAcumuladoMensal"));
//		Utils.formatTableColumnDouble(tableColumnValorAcumuladoMensal, 2);
  		// para tableview preencher o espa�o da tela scroolpane, referencia do stage		
 		Stage stage = (Stage) MainSgo.getMainScene().getWindow();
 		tableViewMensal.prefHeightProperty().bind(stage.heightProperty());
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
			throw new IllegalStateException("Servi�o Dados est� vazio");
 		}
 		labelUser.setText(user);
 		List<FechamentoMes> list = new ArrayList<>();
 		if (numAno != null) {
 			labelTitulo.setText("Fechamento mes : " + nomeMes + "/" + numAno);
 			classe = "Dados Folha";
 			list = service.findAll();
 			obsList = FXCollections.observableArrayList(list);
 		}	
		tableViewMensal.setItems(obsList);
	}

// *  atualiza minha lista dataChanged com dados novos 	
	@Override
	public void onDataChanged() {
		updateTableView();
	}
}
