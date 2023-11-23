package gui.sgo;

import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.MainSgo;
import gui.sgomodel.entities.Cliente;
import gui.sgomodel.services.ClienteService;
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
 
public class ClienteMVRConsultaListController implements Initializable {

// inje��o de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private ClienteService service;
	private MaterialService matService;
	
	@FXML
 	private TableView<Cliente> tableViewCliente;
 	
// c/ entidade e coluna	
 	@FXML
 	private TableColumn<Cliente, Integer>  tableColumnCodigoCli;
 	
 	@FXML
 	private TableColumn<Cliente, String> tableColumnNomeCli;

   	@FXML
 	private TableColumn<Cliente, Integer> tableColumnVisitaClass;
 	
   	@FXML
 	private TableColumn<Cliente, Double> tableColumnPercentualClass;
 	
   	@FXML
 	private TableColumn<Cliente, String> tableColumnLetraClass;
 	
	@FXML
	private Label labelUser;

	String classe = "Cliente ";
	public String user = "usuário";	
 	public static Integer nivel = null;
 	int ok = 0;
// carrega aqui os fornecedores Updatetableview (metodo)
 	private ObservableList<Cliente> obsList;
 
 /* 
  * ActionEvent - referencia p/ o controle q receber o evento c/ acesso ao stage
  * com currentStage -
  * janela pai - parentstage
  * vamos abrir o forn form	
  */
 	

// injeta a dependencia com set (invers�o de controle de inje�ao)	
 	public void setClienteMVRServices(ClienteService service) {
 		this.service = service;
 	}

 	public void setMaterialMVRServices(MaterialService matService) {
 		this.matService = matService;
 	}

 // inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}
 	
	public void mrvForm() {
		Optional<ButtonType> result = Alerts.showConfirmation("Pode demorar um pouco", "Confirma?");
		if (result.get() == ButtonType.OK) {
			MVRConsultaForm mvr = new MVRConsultaForm();
			mvr.setClienteMVRService(service);
			mvr.setMaterialMVRService(matService);
			mvr.setClienteMVRService(service);
			mvr.clienteSomaReceber();
			mvr.clienteMontaBalcao();
			mvr.clienteMontaOs();
			mvr.clienteSomaTotal();
			mvr.clientePercentual();
			mvr.clienteClassifica();
			ok = 1;
		} else {
			ok = 2;
		}
		try {
			updateTableView();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
// comportamento padr�o para iniciar as colunas 	
 	private void initializeNodes() {
		tableColumnCodigoCli.setCellValueFactory(new PropertyValueFactory<>("codigoCli"));;
		tableColumnNomeCli.setCellValueFactory(new PropertyValueFactory<>("nomeCli"));
 		tableColumnVisitaClass.setCellValueFactory(new PropertyValueFactory<>("visitaClass"));
 		tableColumnPercentualClass.setCellValueFactory(new PropertyValueFactory<>("percentualClass"));
		Utils.formatTableColumnDouble(tableColumnPercentualClass, 2);
 		tableColumnLetraClass.setCellValueFactory(new PropertyValueFactory<>("letraClass"));
  		// para tableview preencher o espa�o da tela scroolpane, referencia do stage		
		Stage stage = (Stage) MainSgo.getMainScene().getWindow();
		tableViewCliente.prefHeightProperty().bind(stage.heightProperty());
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
 		List<Cliente> list = new ArrayList<>();
 		labelUser.setText(user);
 		char nd = ' ';
			if (ok == 0) {
				list.add(new Cliente(null, "processando", null, null, null, null, null, null, null, null, null, null, null, null, nd, null, null, null, null, nd, null));
				list.add(new Cliente(null, "<<<aguarde>>>", null, null, null, null, null, null, null, null, null, null, null, null, nd, null, null, null, null, nd, null));
			}
				
 		if (ok == 1) {
 			double bal = MVRConsultaForm.clientePercentualBalcao;
 			double os = MVRConsultaForm.clientePercentualOs;
 			char letraBal = ' ';
 			char letraOs = ' ';
 			if (bal > os) {
 				letraBal = 'A';
 				letraOs = 'B';
 			} else {
 				letraBal = 'B';
 				letraOs = 'A';
 			}
 			
 			list = service.findABC();

 			list.removeIf(x -> x.getPercentualClass() == null || x.getPercentualClass() < 1.00);		
 			Cliente cli1 = new Cliente(999999, "Balcao", "", 0, "", "", "", "", "", 0, 0, 0, 0, "", ' ', "", "", 0.00, 
 	 				MVRConsultaForm.clientePercentualBalcao, letraBal, 0);
 			Cliente cli2 = new Cliente(999999, "OS", "", 0, "", "", "", "", "", 0, 0, 0, 0, "", ' ', "", "", 0.00, 
 	 				MVRConsultaForm.clientePercentualOs, letraOs, 0);
 			if (bal > os) {
 				letraBal = 'A';
 				letraOs = 'B';
 				list.add(0, cli1);
 				list.add(1, cli2);
 			} else {
 				letraBal = 'B';
 				letraOs = 'A';
 				list.add(0, cli2);
 				list.add(1, cli1);
 			}
 		}
 		if (ok > 1) {
 			list = service.findPesquisa("xyzuems");
 		}
 		obsList = FXCollections.observableArrayList(list);
		tableViewCliente.setItems(obsList);
		initializeNodes();
	}
 }
