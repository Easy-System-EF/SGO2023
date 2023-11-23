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
import gui.sgomodel.entities.Adiantamento;
import gui.sgomodel.entities.Balcao;
import gui.sgomodel.entities.FechamentoAno;
import gui.sgomodel.entities.Orcamento;
import gui.sgomodel.entities.Receber;
import gui.sgomodel.services.AdiantamentoService;
import gui.sgomodel.services.BalcaoService;
import gui.sgomodel.services.FechamentoAnoService;
import gui.sgomodel.services.FuncionarioService;
import gui.sgomodel.services.OrcVirtualService;
import gui.sgomodel.services.OrcamentoService;
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
 
public class FechamentoAnoConsultaListController implements Initializable, DataChangeListener, Serializable {

	private static final long serialVersionUID = 1L;

// inje��o de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private FechamentoAnoService service;
//	private FechamentoAno entity;
	
	@FXML
 	private TableView<FechamentoAno> tableViewAnual;

// c/ entidade e coluna	
	
 	@FXML
 	private TableColumn<FechamentoAno, Integer> tableColumnOsAnual;

 	@FXML
 	private TableColumn<FechamentoAno, String> tableColumnBalAnual;

   	@FXML
 	private TableColumn<FechamentoAno, String> tableColumnDataAnual;
 	
   	@FXML
 	private TableColumn<FechamentoAno, String> tableColumnClienteAnual;
 	
   	@FXML
 	private TableColumn<FechamentoAno, String> tableColumnFuncionarioAnual;
 	
   	@FXML
   	private TableColumn<FechamentoAno, String> tableColumnValorOsAnual;
   	
   	@FXML
   	private TableColumn<FechamentoAno, String> tableColumnValorMaterialAnual;
   	
   	@FXML
   	private TableColumn<FechamentoAno, String> tableColumnValorComissaoAnual;
   	
   	@FXML
   	private TableColumn<FechamentoAno, String> tableColumnValorResultadoAnual;

   	@FXML
   	private TableColumn<FechamentoAno, String> tableColumnValorAcumuladoAnual;   	
   	
	@FXML
	private Label labelTitulo;
	
	@FXML
	private Label labelUser;

// carrega aqui lista Updatetableview (metodo)
 	private ObservableList<FechamentoAno> obsList;
 
// auxiliar
 	String classe = "Dados Fechamento Anual";
 	int flagStart = 0;
 	int numAno = 0;
	public String user = "";
		 	 
 	// injeta a dependencia com set (invers�o de controle de inje�ao)	
 	public void setServices(FechamentoAnoService service) {
 		this.service = service;
 	}
 	
	@FXML
	public void montaForm() {
		updateTableView();
		service.zeraAll();
		Optional<ButtonType> result = Alerts.showConfirmation("Processamento", "<<<aguarde>>>");
		if (result.get() == ButtonType.OK) {
			FechamentoAno obj = new FechamentoAno();
			Adiantamento objAdi = new Adiantamento();
			Orcamento objOrc = new Orcamento();
			Balcao objBal = new Balcao();
			Receber objRec = new Receber();
			FechamentoAnoConsultaFormController contF = new FechamentoAnoConsultaFormController();
			contF.setDadosEntityes(obj, objOrc, objAdi, objBal, objRec);
			contF.setServiceAll(
		      new FechamentoAnoService(),
			  new AdiantamentoService(),
			  new OrdemServicoService(),
			  new OrcamentoService(),
			  new OrcVirtualService(),
			  new FuncionarioService(),
			  new BalcaoService(),
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
		tableColumnOsAnual.setCellValueFactory(new PropertyValueFactory<>("OsAnual"));
		tableColumnBalAnual.setCellValueFactory(new PropertyValueFactory<>("BalAnual"));
		tableColumnDataAnual.setCellValueFactory(new PropertyValueFactory<>("DataAnual"));
		tableColumnClienteAnual.setCellValueFactory(new PropertyValueFactory<>("ClienteAnual"));
		tableColumnFuncionarioAnual.setCellValueFactory(new PropertyValueFactory<>("FuncionarioAnual"));
		tableColumnValorOsAnual.setCellValueFactory(new PropertyValueFactory<>("ValorOsAnual"));
		tableColumnValorMaterialAnual.setCellValueFactory(new PropertyValueFactory<>("ValorMaterialAnual"));
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
		List<FechamentoAno> list = new ArrayList<>();
 	
		if (numAno == 0) {
			LocalDate ldt = DataStatic.criaLocalAtual();
			numAno = DataStatic.anoDaData(ldt);
		}	
		labelTitulo.setText("Fechamento anual : " + numAno);
		classe = "Fechamento anual ";
		list = service.findAll();
		if (list.size() == 0 && flagStart == 0) {
			list.add(new FechamentoAno(null, null, null, null, null, null, null, null, null, null, null));
			list.add(new FechamentoAno(null, null, null, null, null, null, null, null, null, null, null));
			list.add(new FechamentoAno(null, null, null, null, null, null, null, null, null, null, null));
			list.add(new FechamentoAno(null, null, null, null, null, null, null, null, null, null, null));
			list.add(new FechamentoAno(null, null, null, null, null, null, null, null, null, null, null));
			list.add(new FechamentoAno(null, null, null, null, null, null, null, null, null, null, null));
			list.add(new FechamentoAno(null, null, null, null, null, null, null, null, null, null, null));
			list.add(new FechamentoAno(null, null, null, null, null, "processamento", null, null, null, null, null));
			list.add(new FechamentoAno(null, null, null, null, null, "<<<aguarde>>>", null, null, null, null, null));
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
