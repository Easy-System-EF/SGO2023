package gui.sgo;

import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.MainSgo;
import gui.listerneres.DataChangeListener;
import gui.sgomodel.entities.Material;
import gui.sgomodel.services.MaterialService;
import gui.util.Alerts;
import gui.util.Utils;
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
 
public class MaterialMVRConsultaListController implements Initializable, DataChangeListener {

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
 	private TableColumn<Material, Double> tableColumnPercentualClass;
 	
   	@FXML
 	private TableColumn<Material, String> tableColumnLetraClass;
 	
	@FXML
	private Label labelUser;

	String classe = "Material ";
	public String user = "usuário";	
 	public static Integer nivel = null;
 	int ok = 0;
	
// carrega aqui os fornecedores Updatetableview (metodo)
 	private ObservableList<Material> obsList;
 
 /* 
  * ActionEvent - referencia p/ o controle q receber o evento c/ acesso ao stage
  * com currentStage -
  * janela pai - parentstage
  * vamos abrir o forn form	
  */
 	

// injeta a dependencia com set (invers�o de controle de inje�ao)	
 	public void setMaterialMVRServices(MaterialService service) {
 		this.service = service;
 	}

 // inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}
 	
	private void mrvForm() {
		Optional<ButtonType> result = Alerts.showConfirmation("Pode demorar um pouco", "Confirma?");
		if (result.get() == ButtonType.OK) {
			MVRConsultaForm mvr = new MVRConsultaForm();
			mvr.setMaterialMVRService(service);
			mvr.materialCustoEstoque();
			mvr.materialPercentual();
			mvr.materialClassifica();
			ok = 1;
		}	
	}
	
// comportamento padr�o para iniciar as colunas 	
 	private void initializeNodes() {
		tableColumnCodigoMat.setCellValueFactory(new PropertyValueFactory<>("codigoMat"));;
		tableColumnNomeMat.setCellValueFactory(new PropertyValueFactory<>("nomeMat"));
 		tableColumnPercentualClass.setCellValueFactory(new PropertyValueFactory<>("percentualClass"));
		Utils.formatTableColumnDouble(tableColumnPercentualClass, 2);
 		tableColumnLetraClass.setCellValueFactory(new PropertyValueFactory<>("letraClass"));
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
 	public void updateTableView() throws ParseException {
 		if (service == null) {
			throw new IllegalStateException("Serviço está vazio");
 		}
		mrvForm();
		List<Material> list = new ArrayList<>();
 		labelUser.setText(user);
		if (ok == 1) {
			labelUser.setText(user);
			list = service.findMVR();
			list.removeIf(x -> x.getPercentualClass() == null || x.getPercentualClass() == 0);		
		}	
 		obsList = FXCollections.observableArrayList(list);
		tableViewMaterial.setItems(obsList);
		initializeNodes();
	}

// *  atualiza minha lista dataChanged com dados novos 	
	@Override
	public void onDataChanged() {
		try {
			updateTableView();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
 }
