package gui.sgo;

import java.io.Serializable;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.MainSgo;
import gui.listerneres.DataChangeListener;
import gui.sgcpmodel.services.ParcelaService;
import gui.sgomodel.entities.FechamentoAnual;
import gui.sgomodel.services.AdiantamentoService;
import gui.sgomodel.services.FechamentoAnualService;
import gui.sgomodel.services.OrcVirtualService;
import gui.sgomodel.services.OrdemServicoService;
import gui.sgomodel.services.ReceberService;
import gui.util.Alerts;
import gui.util.DataStatic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
 
public class FechamentoAnualConsultaListController implements Initializable, DataChangeListener, Serializable {

	private static final long serialVersionUID = 1L;

// inje��o de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private FechamentoAnualService service;
//	private Anual entity;
	
	@FXML
 	private TableView<FechamentoAnual> tableViewAnual;

// c/ entidade e coluna	
	
 	@FXML
 	private TableColumn<FechamentoAnual, Integer> tableColumnMesAnual;

 	@FXML
 	private TableColumn<FechamentoAnual, Integer> tableColumnDoctoAnual;

   	@FXML
   	private TableColumn<FechamentoAnual, String> tableColumnValorAnual;
   	
   	@FXML
   	private TableColumn<FechamentoAnual, String> tableColumnValorCustoAnual;
   	
   	@FXML
   	private TableColumn<FechamentoAnual, String> tableColumnValorComissaoAnual;
   	
   	@FXML
   	private TableColumn<FechamentoAnual, String> tableColumnValorResultadoAnual;

   	@FXML
   	private TableColumn<FechamentoAnual, String> tableColumnValorAcumuladoAnual;   	
   	
	@FXML
	private Label labelTitulo;
	
	@FXML
	private Label labelUser;

// carrega aqui lista Updatetableview (metodo)
 	private ObservableList<FechamentoAnual> obsList;
 
// auxiliar
 	String classe = "Anual";
 	int flagStart = 0;
 	int numAno = 0;
	public String user = "";
		 	 
 	// injeta a dependencia com set (invers�o de controle de inje�ao)	
 	public void setServices(FechamentoAnualService service) {
 		this.service = service;
 	}
 	
	@FXML
	public void montaForm() {
		updateTableView();
		service.zeraAll();
		Optional<ButtonType> result = Alerts.showConfirmation("Pode demorar um pouco", "confirma ?");
		if (result.get() == ButtonType.OK) {
			FechamentoAnual obj = new FechamentoAnual();
			FechamentoAnualConsultaFormController contF = new FechamentoAnualConsultaFormController();
			contF.setDadosEntityes(obj);
			contF.setServiceAll(
		      new FechamentoAnualService(),
			  new AdiantamentoService(),
			  new OrdemServicoService(),
			  new OrcVirtualService(),
			  new ParcelaService(),
			  new ReceberService());
			contF.subscribeDataChangeListener(this);
			try {
				contF.onBtOk();
				contF.updateFormData();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}	
		flagStart = 1;
		updateTableView();
		service.zeraAll();
	}

 	 // inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
			initializeNodes();
 	}

	// comportamento padr�o para iniciar as colunas 	
 	private void initializeNodes() {
		labelTitulo.setText("Fechamento Anual ");
		tableColumnMesAnual.setCellValueFactory(new PropertyValueFactory<>("MesAnual"));
		tableColumnDoctoAnual.setCellValueFactory(new PropertyValueFactory<>("DoctoAnual"));
		tableColumnValorAnual.setCellValueFactory(new PropertyValueFactory<>("ValorAnual"));
		tableColumnValorCustoAnual.setCellValueFactory(new PropertyValueFactory<>("ValorCustoAnual"));
		tableColumnValorComissaoAnual.setCellValueFactory(new PropertyValueFactory<>("ValorComissaoAnual"));
		tableColumnValorResultadoAnual.setCellValueFactory(new PropertyValueFactory<>("ValorResultadoAnual"));
		tableColumnValorAcumuladoAnual.setCellValueFactory(new PropertyValueFactory<>("ValorAcumuladoAnual"));
  		// para tableview preencher o espa�o da tela scroolpane, referencia do stage		
 		Stage stage = (Stage) MainSgo.getMainScene().getWindow();
 		tableViewAnual.prefHeightProperty().bind(stage.heightProperty());
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
		List<FechamentoAnual> list = new ArrayList<>();
 	
		if (numAno == 0) {
			LocalDate ldt = DataStatic.criaLocalAtual();
			numAno = DataStatic.anoDaData(ldt);
		}	
		labelTitulo.setText("Fechamento anual : " + numAno);
		classe = "Fechamento anual ";
		list = service.findAll();
		if (list.size() == 0 && flagStart == 0) {
			list.add(new FechamentoAnual(null, null, null, null, null, null, null, null));
			list.add(new FechamentoAnual(null, null, null, null, null, null, null, null));
			list.add(new FechamentoAnual(null, null, null, null, null, null, null, null));
			list.add(new FechamentoAnual(null, null, "processamento", null, null, null, null, null));
			list.add(new FechamentoAnual(null, null, "<<<aguarde>>>", null, null, null, null, null));
		}
		obsList = FXCollections.observableArrayList(list);
		tableViewAnual.setItems(obsList);
	}

// *  atualiza minha lista dataChanged com dados novos 	
	@Override
	public void onDataChanged() {
		updateTableView();
	}
}
