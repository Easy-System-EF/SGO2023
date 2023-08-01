package gui.sgo;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.MainSgo;
import db.DbIntegrityException;
import gui.listerneres.DataChangeListener;
import gui.sgomodel.entities.Material;
import gui.sgomodel.services.GrupoService;
import gui.sgomodel.services.MaterialService;
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
 
public class MaterialCadastroListController implements Initializable, DataChangeListener {

// inje��o de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private MaterialService service;
	
	@FXML
 	private TableView<Material> tableViewMaterial;
 	
// c/ entidade e coluna	
 	@FXML
 	private TableColumn<Material, Integer>  tableColumnCodigoMat;
 	
 	@FXML
 	private TableColumn<Material, String> tableColumnNomeMat;

   	@FXML
 	private TableColumn<Material, Double> tableColumnSaldoMat;
 	
   	@FXML
 	private TableColumn<Material, Double> tableColumnVendaMat;
 	
   	@FXML
 	private TableColumn<Material, Double> tableColumnCmmMat;
 	
  	@FXML
 	private TableColumn<Material, Material> tableColumnEDITA;

 	@FXML
 	private TableColumn<Material, Material> tableColumnREMOVE ;

 	@FXML
 	private Button btNewMat;

	@FXML
	private Label labelUser;

	// Auxiliar
 	public static Integer nivel = null;
 	String classe = "Material List ";
	public String user = "usuário";		
 	
// carrega aqui os fornecedores Updatetableview (metodo)
 	private ObservableList<Material> obsList;
 
 /* 
  * ActionEvent - referencia p/ o controle q receber o evento c/ acesso ao stage
  * com currentStage -
  * janela pai - parentstage
  * vamos abrir o forn form	
  */

 	@FXML
  	public void onBtNewMatAction(ActionEvent event) {
 		 Stage parentStage = Utils.currentStage(event);
// instanciando novo obj depto e injetando via
 		 Material obj = new Material();
 		 createDialogForm(obj, "/gui/sgo//MaterialCadastroForm.fxml", parentStage);
   	}
 	
// injeta a dependencia com set (invers�o de controle de inje�ao)	
 	public void setMaterialService(MaterialService service) {
 		this.service = service;
 	}

 // inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}

// comportamento padr�o para iniciar as colunas 	
 	private void initializeNodes() {
		tableColumnCodigoMat.setCellValueFactory(new PropertyValueFactory<>("codigoMat"));
  		tableColumnNomeMat.setCellValueFactory(new PropertyValueFactory<>("nomeMat"));
		tableColumnSaldoMat.setCellValueFactory(new PropertyValueFactory<>("saldoMat"));
		Utils.formatTableColumnDouble(tableColumnSaldoMat, 2);
		tableColumnVendaMat.setCellValueFactory(new PropertyValueFactory<>("vendaMat"));
		Utils.formatTableColumnDouble(tableColumnVendaMat, 2);
 		tableColumnCmmMat.setCellValueFactory(new PropertyValueFactory<>("cmmMat"));
 		Utils.formatTableColumnDouble(tableColumnCmmMat, 2);
 		// para tableview preencher o espa�o da tela scroolpane, referencia do stage		
		Stage stage = (Stage) MainSgo.getMainScene().getWindow();
		tableViewMaterial.prefHeightProperty().bind(stage.heightProperty());
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
		List<Material> list = service.findAll();
 		obsList = FXCollections.observableArrayList(list);
		tableViewMaterial.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

/* 	
* parametro informando qual stage criou essa janela de dialogo - stage parent
* nome da view - absolutename
* carregando uma janela de dialogo modal (s� sai qdo sair dela, tem q instaciar um stage e dps a janela dialog
*/

	private synchronized void createDialogForm(Material obj, String absoluteName, Stage parentStage) {
		try {
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
// referencia para o controlador = controlador da tela carregada fornListaForm			
			MaterialCadastroFormController controller = loader.getController();
			controller.user = user;
 // injetando passando parametro obj 			
			controller.setMaterial(obj);
 // injetando tb o forn service vindo da tela de formulario fornform
			controller.setMaterialService(new MaterialService());
			controller.setGrupoService(new GrupoService());
			controller.loadAssociatedObjects();
// inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
			controller.subscribeDataChangeListener(this);
//	carregando o obj no formulario (fornecedorFormControl)			
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
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Erro carregando tela" + classe, e.getMessage(), AlertType.ERROR);
		}
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
		  tableColumnEDITA.setCellFactory(param -> new TableCell<Material, Material>() { 
		    private final Button button = new Button("edita"); 
		 
		    @Override 
		    protected void updateItem(Material obj, boolean empty) { 
		      super.updateItem(obj, empty); 
		 
		      if (obj == null) { 
		        setGraphic(null); 
		        return; 
		      } 
		 
		      setGraphic(button); 
		      button.setOnAction( 
		      event -> createDialogForm( 
		        obj, "/gui/sgo/MaterialCadastroForm.fxml",Utils.currentStage(event)));
 		    } 
		  }); 
		}
	

	private void initRemoveButtons() {		
		  tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		  tableColumnREMOVE.setCellFactory(param -> new TableCell<Material, Material>() { 
		        private final Button button = new Button("exclui"); 
		 
		        @Override 
		        protected void updateItem(Material obj, boolean empty) { 
		            super.updateItem(obj, empty); 
		 
		            if (obj == null) { 
		                setGraphic(null); 
		                return; 
		            } 
		 
		            setGraphic(button); 
		            button.setOnAction(event -> removeEntity(obj)); 
		        } 
		    });
 		} 

	private void removeEntity(Material obj) {
		if (nivel > 1 && nivel < 9) {
			Alerts.showAlert(null, "Atenção", "Operaçaoo não permitida", AlertType.INFORMATION);
		} else {
			Optional<ButtonType> result = Alerts.showConfirmation("Confirmção", "Tem certeza que deseja excluir?");
			if (result.get() == ButtonType.OK) {
				if (service == null) {
					throw new IllegalStateException("Serviço está vazio");
				}
				try {
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
