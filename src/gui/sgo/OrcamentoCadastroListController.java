package gui.sgo;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.MainSgo;
import db.DbIntegrityException;
import gui.listerneres.DataChangeListener;
import gui.sgomodel.entities.Empresa;
import gui.sgomodel.entities.OrcVirtual;
import gui.sgomodel.entities.Orcamento;
import gui.sgomodel.entities.Veiculo;
import gui.sgomodel.services.ClienteService;
import gui.sgomodel.services.EmpresaService;
import gui.sgomodel.services.FuncionarioService;
import gui.sgomodel.services.OrcVirtualService;
import gui.sgomodel.services.OrcamentoService;
import gui.sgomodel.services.VeiculoService;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
 
public class OrcamentoCadastroListController implements Initializable, DataChangeListener {

// inje��o de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private OrcamentoService service;
	private OrcVirtualService virService;
	@SuppressWarnings("unused")
	private EmpresaService empService;

	@FXML
 	private TableView<Orcamento> tableViewOrcamento;
 	
// c/ entidade e coluna	
 	@FXML
 	private TableColumn<Orcamento, Integer>  tableColumnNumeroOrc;
 	
 	@FXML
 	private TableColumn<Orcamento, Date>  tableColumnDataOrc;
 	
 	@FXML
 	private TableColumn<Orcamento, String> tableColumnClienteOrc;

   	@FXML
 	private TableColumn<Orcamento, String> tableColumnPlacaOrc;
 	
   	@FXML
 	private TableColumn<Orcamento, Integer> tableColumnKmFinalOrc;
 	
   	@FXML
 	private TableColumn<Orcamento, Double> tableColumnTotalOrc;
 	
  	@FXML
 	private TableColumn<Orcamento, Orcamento> tableColumnEDITA;

 	@FXML
 	private TableColumn<Orcamento, Orcamento> tableColumnREMOVE ;

 	@FXML
 	private TableColumn<Orcamento, Orcamento> tableColumnList ;

 	@FXML
 	private Button btNewOrc;

 	@FXML
 	private Label labelUser;

 // auxiliar
 	public static Integer nivel = null;
 	public String user = "usuário";		
 	String classe = "Orçamento List ";
 	public static Integer numEmp = 0;
 	
 	
// carrega aqui os fornecedores Updatetableview (metodo)
 	private ObservableList<Orcamento> obsList;

 /* 
   * ActionEvent - referencia p/ o controle q receber o evento c/ acesso ao stage
  * com currentStage -
  * janela pai - parentstage
  * vamos abrir o forn form	
  */
 	Veiculo objVei = new Veiculo();
 	OrcVirtual objVir = new OrcVirtual();
	@FXML
  	public void onBtNewOrcAction(ActionEvent event) {
 		 Stage parentStage = Utils.currentStage(event);
// instanciando novo obj depto e injetando via
 		 Orcamento obj = new Orcamento();
 		 createDialogForm(obj, objVei, objVir, "/gui/sgo/OrcamentoCadastroForm.fxml", parentStage);
 		 initializeNodes();
   	}
 	
// injeta a dependencia com set (invers�o de controle de inje�ao)	
 	public void setServices(OrcamentoService service, OrcVirtualService virService) {
 		this.service = service;
 		this.virService = virService;
 	}

 // inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}

// comportamento padr�o para iniciar as colunas 	
 	private void initializeNodes() {
		tableColumnNumeroOrc.setCellValueFactory(new PropertyValueFactory<>("numeroOrc"));
		tableColumnDataOrc.setCellValueFactory(new PropertyValueFactory<>("dataOrc"));
		Utils.formatTableColumnDate(tableColumnDataOrc, "dd/MM/yyyy");
   		tableColumnClienteOrc.setCellValueFactory(new PropertyValueFactory<>("clienteOrc"));
		tableColumnPlacaOrc.setCellValueFactory(new PropertyValueFactory<>("placaOrc"));
		tableColumnKmFinalOrc.setCellValueFactory(new PropertyValueFactory<>("kmFinalOrc"));
 		tableColumnTotalOrc.setCellValueFactory(new PropertyValueFactory<>("totalOrc"));
		Utils.formatTableColumnDouble(tableColumnTotalOrc, 2);
 // para tableview preencher o espa�o da tela scroolpane, referencia do stage		
		Stage stage = (Stage) MainSgo.getMainScene().getWindow();
		 tableViewOrcamento.prefHeightProperty().bind(stage.heightProperty());
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
		List<Orcamento> list = service.findByAberto();
 		obsList = FXCollections.observableArrayList(list);
		tableViewOrcamento.setItems(obsList);
		notifyDataChangeListerners();
		initEditButtons();
		initRemoveButtons();
		initListButtons();
	}

/* 	
* parametro informando qual stage criou essa janela de dialogo - stage parent
* nome da view - absolutename
* carregando uma janela de dialogo modal (s� sai qdo sair dela, tem q instaciar um stage e dps a janela dialog
*/
	@SuppressWarnings("static-access")
	private synchronized void createDialogForm(Orcamento obj, Veiculo objVei, OrcVirtual objVir,
						String absoluteName, Stage parentStage) {
		try {
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			classe = "Orçamento";
// referencia para o controlador = controlador da tela carregada fornListaForm			
			OrcamentoCadastroFormController controller = loader.getController();
			controller.user = user;
			controller.nivel = nivel;
 // injetando passando parametro obj 		
			controller.setOrcamento(obj);
			controller.setVeiculo(objVei);

 // injetando servi�os vindo da tela de formulario fornform
			controller.setOrcamentoService(new OrcamentoService());
			controller.setClienteService(new ClienteService());
			controller.setFuncionarioService(new FuncionarioService());
			controller.setOrcVirtualService(new OrcVirtualService());
			controller.setVeiculoService(new VeiculoService());
			controller.loadAssociatedObjects();
// inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
			controller.subscribeDataChangeListener(this);
//	carregando o obj no formulario (fornecedorFormControl)			
			if (obj.getOsOrc() != null && obj.getOsOrc() > 0) {
				String os = String.format("OS núumero : %d", obj.getOsOrc());
				Alerts.showAlert("Orçamento fechado ", "Concluído - Sem acesso ", os, AlertType.ERROR);
			}
			else {
				controller.updateTableView();
				controller.updateFormData();
			
				Stage dialogStage = new Stage();
				dialogStage.setTitle("Digite Material                                             ");
				dialogStage.setScene(new Scene(pane));
// pode redimencionar a janela: s/n?
				dialogStage.setResizable(false);
// quem e o stage pai da janela?
				dialogStage.initOwner(parentStage);
// travada enquanto n�o sair da tela
				dialogStage.initModality(Modality.WINDOW_MODAL);
				dialogStage.showAndWait();
			}	
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Erro carregando tela " + classe, e.getMessage(), AlertType.ERROR);
		}
 	} 

// Imprimir	
 	@SuppressWarnings("static-access")
	private synchronized void createDialogImprime(Orcamento obj, Empresa emp, Integer codOrc,   
 					String absoluteName, Stage parentStage) {
 			try {
 	 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
 				Pane pane = loader.load();
 				classe = "Orçamento Impr";
 	 			OrcamentoImprimeController controller = loader.getController();
 	 			controller.setOrcamento(obj);
 	 			controller.numEmp = numEmp;
 	 			controller.codOrc = codOrc;
 	 			controller.setOrcamentoService(new OrcamentoService(), new EmpresaService());
 	 			controller.setOrcVirtualService(new OrcVirtualService());
 	   			Stage dialogStage = new Stage();
 	 			dialogStage.setTitle("Lista Orçamento                                             ");
 	 			dialogStage.setScene(new Scene(pane));
 	 			dialogStage.setResizable(false);
 	  			dialogStage.initOwner(parentStage);
 	 			dialogStage.initModality(Modality.WINDOW_MODAL);
 				dialogStage.showAndWait();
 			}
 			catch (IOException e) {
 				e.printStackTrace();
 				Alerts.showAlert("IO Exception", "Erro carregando tela " + classe, e.getMessage(), AlertType.ERROR);
 			}
 	  	}

	
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
	private void initEditButtons() {
		  tableColumnEDITA.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		  tableColumnEDITA.setCellFactory(param -> new TableCell<Orcamento, Orcamento>() { 
		    private final Button button = new Button("edita"); 
		 
		    @Override 
		    protected void updateItem(Orcamento obj, boolean empty) { 
		      super.updateItem(obj, empty); 
		      if (obj == null) { 
				 setGraphic(null); 
				 return; 
		      }
		 
		      setGraphic(button); 
		      button.setOnAction( 
					event -> createDialogForm(obj, objVei, objVir,
							"/gui/sgo/OrcamentoCadastroForm.fxml", Utils.currentStage(event)));
		    }
		  }); 
		}
	
	private void initListButtons() {
		  tableColumnList.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		  tableColumnList.setCellFactory(param -> new TableCell<Orcamento, Orcamento>() { 
		  private final Button button = new Button("lista");
		 
		  @Override 
		  protected void updateItem(Orcamento obj, boolean empty) { 
		     super.updateItem(obj, empty); 
		     if (obj == null) { 
		    	  setGraphic(null); 
		    	  return; 
		     }
		     @SuppressWarnings("unused")
			Integer codOrc = obj.getNumeroOrc();
		     Empresa emp = new Empresa();
		     setGraphic(button); 
		     button.setOnAction( 
		    	  event -> createDialogImprime(obj, emp, obj.getNumeroOrc(), 
		    			  "/gui/sgo/OrcamentoImprime.fxml", Utils.currentStage(event)));
		  }
		  }); 
		}
	
	private void initRemoveButtons() {		
		  tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		  tableColumnREMOVE.setCellFactory(param -> new TableCell<Orcamento, Orcamento>() { 
		        private final Button button = new Button("exclui"); 
		 
		        @Override 
		        protected void updateItem(Orcamento obj, boolean empty) { 
		            super.updateItem(obj, empty); 
		 
		            if (obj == null) { 
		                setGraphic(null); 
		                return; 
		            } 
		 
		            setGraphic(button); 
		            button.setOnAction(eivent -> removeEntity(obj)); 
		        } 
		    });
 		} 

	private void removeEntity(Orcamento obj) {
		if (nivel > 1 && nivel < 9) {
			Alerts.showAlert(null, "Exclusão", "Operaçaoo não permitida", AlertType.INFORMATION);
		} else {
			if (obj.getOsOrc() != null && obj.getOsOrc() > 0) {
				String os = String.format("OS número: %d", obj.getOsOrc());
				Alerts.showAlert("Orçamento fechado ", "Concluído - Sem acesso ", os, AlertType.ERROR);
			}
			else {
				Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que deseja excluir?");
				if (result.get() == ButtonType.OK) {
					if (service == null) {
						throw new IllegalStateException("Serviço está vazio");
					}
					try {
						virService.removeOrc(obj.getNumeroOrc());
						service.remove(obj);
						updateTableView();
					}
					catch (DbIntegrityException e) {
						Alerts.showAlert("Erro removendo objeto", classe, e.getMessage(), AlertType.ERROR);
					}	
				}
			}
		}
	}
}