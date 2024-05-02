package gui.sgo;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.time.LocalDate;
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
import gui.sgomodel.entities.FechamentoMes;
import gui.sgomodel.services.AdiantamentoService;
import gui.sgomodel.services.AnosService;
import gui.sgomodel.services.BalcaoService;
import gui.sgomodel.services.ComissaoService;
import gui.sgomodel.services.FechamentoMesService;
import gui.sgomodel.services.FuncionarioService;
import gui.sgomodel.services.MesesService;
import gui.sgomodel.services.OrcVirtualService;
import gui.sgomodel.services.OrdemServicoService;
import gui.sgomodel.services.ReceberService;
import gui.util.Alerts;
import gui.util.Maria;
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
 	int flagStart = 0;
 	 
 	// injeta a dependencia com set (invers�o de controle de inje�ao)	
 	public void setServices(FechamentoMesService service) {
 		this.service = service;
 	}
 	
	@FXML
	public void onBtMesesMensalAction(ActionEvent event) {
		updateTableView();
  		flagStart = 1;
		Stage parentStage = Utils.currentStage(event);
		FechamentoMes obj = new FechamentoMes();
		classe = "Meses ";
  		createDialogOpcao("/gui/sgo/MesAnoMForm.fxml", parentStage, 
  				(FechamentoMesConsultaFormController contF) -> {
			contF.setDadosEntityes(obj);
			contF.setServices(new FechamentoMesService(),
						      new AdiantamentoService(),
							  new MesesService(),
							  new AnosService(),
							  new OrdemServicoService(),
							  new OrcVirtualService(),
							  new FuncionarioService(),
							  new BalcaoService(),
							  new TipoConsumoService(),
							  new CompromissoService(),
	 						  new ParcelaService(),
	 						  new ParPeriodoService(),
	 						  new ReceberService(),
							  new ComissaoService());
			contF.loadAssociatedObjects();
 			contF.subscribeDataChangeListener(this);
			contF.updateFormData();
 		});
		updateTableView();
		service.zeraAll();
  		flagStart = 0;
  		nomeMes = null;
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
		tableColumnClienteMensal.setCellValueFactory(new PropertyValueFactory<>("ClienteMensal"));
		tableColumnFuncionarioMensal.setCellValueFactory(new PropertyValueFactory<>("FuncionarioMensal"));
		tableColumnValorOsMensal.setCellValueFactory(new PropertyValueFactory<>("ValorOsMensal"));
		tableColumnValorMaterialMensal.setCellValueFactory(new PropertyValueFactory<>("ValorMaterialMensal"));
		tableColumnValorComissaoMensal.setCellValueFactory(new PropertyValueFactory<>("ValorComissaoMensal"));
		tableColumnValorResultadoMensal.setCellValueFactory(new PropertyValueFactory<>("ValorResultadoMensal"));
		tableColumnValorAcumuladoMensal.setCellValueFactory(new PropertyValueFactory<>("ValorAcumuladoMensal"));
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
			throw new IllegalStateException("Serviço Dados está vazio");
 		}
 		labelUser.setText(user);
 		int ano = 0;
 		List<FechamentoMes> list = new ArrayList<>();
 		if (ano == 0) {
 			LocalDate ldt = Maria.criaLocalAtual();
 			ano = Maria.anoDaData(ldt);
 			labelTitulo.setText("Fechamento mes ");
 		} 
 		if (nomeMes != null) {		
 			labelTitulo.setText("Fechamento mes : " + nomeMes + "/" + ano);
 		} else {
 			labelTitulo.setText("Fechamento mes :                           "); 			
 		}
 		
 		classe = "Dados Folha";
 		list = service.findAll();
 		if (list.size() == 0 && flagStart == 0) {
 			
// 			list.add(new FechamentoMes(num,  os,  bal,   dt,   cli,  fun, vlr, mat, com, res, acu, mes, ano));
 			
// 			list.add(new FechamentoMes(null, null, null, null, null, null, null, null, null, null, null, null, null));
// 			list.add(new FechamentoMes(null, null, null, null, null, null, null, null, null, null, null, null, null));
 			list.add(new FechamentoMes(null, null, null, null, null, null, null, null, null, null, null, null, null));
 			list.add(new FechamentoMes(null, null, null, null, null, null, null, null, null, null, null, null, null));
 			list.add(new FechamentoMes(null, null, null, null, null, null, null, null, null, null, null, null, null));
 			list.add(new FechamentoMes(null, null, null, null, null, null, null, null, null, null, null, null, null));
 			list.add(new FechamentoMes(null, null, null, null, null, null, null, null, null, null, null, null, null));
 			list.add(new FechamentoMes(null, null, null, null, "processamento", null, null, null, null, null, null, null, null));
 			list.add(new FechamentoMes(null, null, null, null, "<<<aguarde>>>", null, null, null, null, null, null, null, null));
 		}
 		obsList = FXCollections.observableArrayList(list);
		tableViewMensal.setItems(obsList);
	}

// *  atualiza minha lista dataChanged com dados novos 	
	@Override
	public void onDataChanged() {
		updateTableView();
	}
}
