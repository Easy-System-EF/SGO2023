package gui.sgo;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.MainSgo;
import db.DbIntegrityException;
import gui.listerneres.DataChangeListener;
import gui.sgcp.ParFornecedorFormController;
import gui.sgcpmodel.entities.Compromisso;
import gui.sgcpmodel.entities.Fornecedor;
import gui.sgcpmodel.entities.Parcela;
import gui.sgcpmodel.entities.TipoConsumo;
import gui.sgcpmodel.entities.consulta.ParPeriodo;
import gui.sgcpmodel.services.CompromissoService;
import gui.sgcpmodel.services.FornecedorService;
import gui.sgcpmodel.services.ParcelaService;
import gui.sgomodel.entities.Entrada;
import gui.sgomodel.entities.Material;
import gui.sgomodel.services.EntradaService;
import gui.sgomodel.services.MaterialService;
import gui.util.Alerts;
import gui.util.CalculaParcela;
import gui.util.DataIDataF;
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
 
public class EntradaCadastroListController implements Initializable, DataChangeListener {

// inje��o de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private EntradaService service;
	private MaterialService matService;
	private ParcelaService parService = new ParcelaService();
	
	@FXML
 	private TableView<Entrada> tableViewEntrada;
 	
// c/ entidade e coluna	
 	@FXML
 	private TableColumn<Entrada, Date> tableColumnDataEnt; 
 	
 	@FXML
 	private TableColumn<Entrada, Integer>  tableColumnNumeroEnt;

 	@FXML
 	private TableColumn<Entrada, String>  tableColumnNomeFornEnt;

 	@FXML
 	private TableColumn<Entrada, String>  tableColumnNomeMatEnt;

   	@FXML
 	private TableColumn<Entrada, Double> tableColumnQtdMatEnt;
 	
   	@FXML
 	private TableColumn<Entrada, Double> tableColumnVlrMatEnt;
 	
  	@FXML
 	private TableColumn<Entrada, Entrada> tableColumnEDITA;

 	@FXML
 	private TableColumn<Entrada, Entrada> tableColumnREMOVE ;

 	@FXML
 	private Button btNewEnt;
 	
	@FXML
	private Button btFornecedor;

	@FXML
	private Button btMaterial;

 	@FXML
 	private Label labelUser;

 	@FXML
 	private Label labelTitulo;

 // auxiliar
 	public String user = "usuário";
 	public String nomeTitulo = "Entrada de Material";
 	public char opcao = ' ';
 	public Integer nivel = 0;
 	public static Integer codigoFor = null;
 	public static Integer codigoMat = null;
 	String classe = "Entrada List";
 	
// carrega aqui Updatetableview (metodo)
 	private ObservableList<Entrada> obsList;
  
 /* 
  * ActionEvent - referencia p/ o controle q receber o evento c/ acesso ao stage
  * com currentStage -
  * janela pai - parentstage
  * vamos abrir o forn form	
  */
		Material objMat = new Material();
		Fornecedor objForn = new Fornecedor();
		Compromisso objCom = new Compromisso();
		ParPeriodo objPer = new ParPeriodo();
		Parcela objPar = new Parcela();
		TipoConsumo objTipo = new TipoConsumo();

 	@FXML
  	public void onBtNewEntAction(ActionEvent event) {
 		 Stage parentStage = Utils.currentStage(event);
// instanciando novo obj depto e injetando via
 		Entrada obj = new Entrada();
 		createDialogForm(obj, objMat, objCom, objPer, objPar, objForn, objTipo, "/gui/sgo/EntradaCadastroForm.fxml", parentStage);
   	}

 	public void onBtFornecedorAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event); 
		nomeTitulo = "Consulta Entrada por Fornecedor";
		opcao = 'F';
  		createDialogForms("/gui/sgcp/ParFornecedorForm.fxml", objForn, objMat, (parentStage), 
  				(ParFornecedorFormController contF) -> {
			contF.setFornecedor(objForn);
			contF.setService(new FornecedorService());
			contF.loadAssociatedObjects();
			contF.subscribeDataChangeListener(this);
			contF.updateFormData();
 		});
	}
 	
 	
 	public void onBtMaterialAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event); 
		nomeTitulo = "Consulta Entrada por Material";
		opcao = 'M';
  		createDialogForms("/gui/sgo/MaterialPesquisaForm.fxml", objForn, objMat, (parentStage), 
  				(MaterialPesquisaFormController contM) -> {
			contM.setMaterial(objMat);
			contM.setService(new MaterialService());
			contM.loadAssociatedObjects();
			contM.subscribeDataChangeListener(this);
			contM.updateFormData();
 		});
	}
 	
 	// injeta a dependencia com set (invers�o de controle de inje�ao)	
 	public void setServices(MaterialService matService, EntradaService service) {
 		this.matService = matService;
 		this.service = service;
 	}

// inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		codigoFor = null;
		codigoMat = null;
 	}

// comportamento padr�o para iniciar as colunas 	
 	private void initializeNodes() {
		tableColumnNumeroEnt.setCellValueFactory(new PropertyValueFactory<>("numeroEnt"));
		tableColumnDataEnt.setCellValueFactory(new PropertyValueFactory<>("dataEnt"));
		Utils.formatTableColumnDate(tableColumnDataEnt, "dd/MM/yyyy");
		tableColumnNomeFornEnt.setCellValueFactory(new PropertyValueFactory<>("nomeFornEnt"));
		tableColumnNomeMatEnt.setCellValueFactory(new PropertyValueFactory<>("nomeMatEnt"));
 		tableColumnQtdMatEnt.setCellValueFactory(new PropertyValueFactory<>("quantidadeMatEnt"));
		Utils.formatTableColumnDouble(tableColumnQtdMatEnt, 2);
		tableColumnVlrMatEnt.setCellValueFactory(new PropertyValueFactory<>("valorMatEnt"));
		Utils.formatTableColumnDouble(tableColumnVlrMatEnt, 2);
  		// para tableview preencher o espa�o da tela scroolpane, referencia do stage		
		Stage stage = (Stage) MainSgo.getMainScene().getWindow();
		tableViewEntrada.prefHeightProperty().bind(stage.heightProperty());
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
		Date dti = DataIDataF.datai();
		Date dtf = DataIDataF.dataf();
			 		
 		labelUser.setText(user);
 		labelTitulo.setText(nomeTitulo);
		initializeNodes();
 		List<Entrada> list = new ArrayList<>();
 		if (codigoFor != null) {
 			list = service.findByForn(codigoFor);
 	 		list.removeIf(f -> ! f.getForn().getCodigo().equals(codigoFor));
 		} 
 		if (codigoMat != null) {
 			list = service.findByMat(codigoMat);
 	 		list.removeIf(m -> ! m.getMat().getCodigoMat().equals(codigoMat));
 		} 
 		if (codigoFor == null) {
 			if (codigoMat == null) {
				list = service.findAll();
				list.removeIf(x -> x.getDataEnt().before(dti));
				list.removeIf(x -> x.getDataEnt().after(dtf));
 			}	
 		}	
   		obsList = FXCollections.observableArrayList(list);
 		tableViewEntrada.setItems(obsList);
 		initEditButtons();
		initRemoveButtons();
		codigoFor = null;
		codigoMat = null;
	}

/* 	
* parametro informando qual stage criou essa janela de dialogo - stage parent
* nome da view - absolutename
* carregando uma janela de dialogo modal (s� sai qdo sair dela, tem q instaciar um stage e dps a janela dialog
*/

	private synchronized void createDialogForm(Entrada obj, Material mat, Compromisso objCom, ParPeriodo objPer, Parcela objPar, 
			Fornecedor objFor, TipoConsumo objTipo, String absoluteName, Stage parentStage) {
		try {
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
// referencia para o controlador = controlador da tela carregada fornListaForm			
			EntradaCadastroFormController controller = loader.getController();
			controller.user = user;
 // injetando passando parametro obj 			
			controller.setObjects(obj, mat);
			controller.setServices(new EntradaService(), new FornecedorService(), new MaterialService());
 // injetando tb o forn service vindo da tela de formulario fornform
			controller.loadAssociatedObjects();
// inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
			controller.subscribeDataChangeListener(this);
//	carregando o obj no formulario (fornecedorFormControl)			
			controller.updateFormData();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Digite Entrada                                             ");
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
			Alerts.showAlert("IO Exception", classe + "Erro carregando tela ", e.getMessage(), AlertType.ERROR);
		}
 	} 
  	
	private synchronized <T> void createDialogForms(String absoluteName, Fornecedor obj, Material objMat, 
			Stage parentStage, Consumer<T> initializeAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			 
			T cont = loader.getController();
 			initializeAction.accept(cont);

 			Stage dialogStage = new Stage();
			if (opcao == 'F') {
				dialogStage.setTitle("Selecione Fornecdor                                             ");
			}
			if (opcao == 'M') {
				dialogStage.setTitle("Selecione Material                                             ");
			}
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", classe + "Erro carregando tela", e.getMessage(), AlertType.ERROR);
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
		  tableColumnEDITA.setCellFactory(param -> new TableCell<Entrada, Entrada>() { 
		    private final Button button = new Button("edita"); 
		 
		    @Override 
		    protected void updateItem(Entrada obj, boolean empty) { 
		      super.updateItem(obj, empty); 
		 
		      if (obj == null) { 
		        setGraphic(null); 
		        return; 
		      } 
		 
		      setGraphic(button); 
		      button.setOnAction( 
		      event -> createDialogForm(obj, objMat, objCom, objPer, objPar, 
		  			objForn, objTipo, "/gui/sgo/EntradaCadastroForm.fxml",Utils.currentStage(event)));
 		    } 
		  }); 
		}
 
	private void initRemoveButtons() {		
		  tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		  tableColumnREMOVE.setCellFactory(param -> new TableCell<Entrada, Entrada>() { 
		        private final Button button = new Button("exclui"); 
		 
		        @Override 
		        protected void updateItem(Entrada obj, boolean empty) { 
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

	private void removeEntity(Entrada obj) {
		if (nivel > 1 && nivel < 9) {
			Alerts.showAlert(null, "Exclusão", "Operaçaoo não permitida", AlertType.INFORMATION);
		} else {
			classe = "Entrada List";
			Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que deseja excluir?");
			if (result.get() == ButtonType.OK) {
				if (service == null) {
					throw new IllegalStateException("Serviço está vazio");
				}
				try {
					String simNao = "sim";
					simNao = verificaPago(obj, simNao);
					if (simNao == "nao") {
						Alerts.showAlert("Parcela Paga", "Existe(m) parcela(s) paga(s) ", "sem exclusão", AlertType.ERROR);
					}
					if (simNao == "sim") {
						if (obj.getForn().getCodigo() != null) {
							simNao = confereSaldo(obj.getNnfEnt(), obj.getForn().getCodigo(), simNao);
							acertaCompromisso(obj, simNao);
							saiMaterial(obj.getNnfEnt(), obj.getForn().getCodigo(), simNao);
							service.remove(obj);
						}	
		   			}	
					updateTableView();
				}
				catch (DbIntegrityException e) {
					Alerts.showAlert("Erro removendo objeto", classe, e.getMessage(), AlertType.ERROR);
				}
			}
		}	
	}

	private String  verificaPago(Entrada obj, String simNao) {
		ParcelaService parService = new ParcelaService();
		List<Parcela> listPar = parService.findByIdFornecedorNnf(obj.getForn().getCodigo(), obj.getNnfEnt());	
		if (listPar.size() != 0) {
			for (Parcela p : listPar) {
				if (p.getFornecedor().getCodigo().equals(obj.getForn().getCodigo()) || 
						p.getNnfPar().equals(obj.getNnfEnt())) {
					if (p.getPagoPar() > 0) {
						simNao = "nao";
					}	
				}
			}
		}
		return simNao;
	}
	
	private String confereSaldo(int nnf, int forn, String simNao) {
		Material mat1;
		List<Entrada> listEnt2 = service.findByNnf(nnf);
		if (listEnt2.size() > 0) {
			for (Entrada e2 : listEnt2) {
				if (e2.getForn().getCodigo() == forn) {
					mat1 = matService.findById(e2.getMat().getCodigoMat());
					if (e2.getQuantidadeMatEnt() > mat1.getSaldoMat()) {
						Alerts.showAlert("Atenção ","Saldo é menor que a quantidade ", "sem exclusão ", AlertType.ERROR);
						simNao = "nao";
					}	
				}
			}
		}	
		return simNao;
	}	
	
	private void saiMaterial(int nnf, Integer forn, String simNao) {
		if (simNao == "sim") {
			Material mat1;
			List<Entrada> listEnt2 = service.findByNnf(nnf);
			for (Entrada e2 : listEnt2) {
				if (e2.getForn().getCodigo() == forn) {
					mat1 = matService.findById(e2.getMat().getCodigoMat()); 
					if (mat1.getSaldoMat() >= e2.getQuantidadeMatEnt()) {
						mat1.setEntradaMat(e2.getQuantidadeMatEnt() * -1);
						mat1.getSaldoMat();
						matService.saveOrUpdate(mat1);
					}	
				}
			}
		}	
	}	
	
	private void acertaCompromisso(Entrada obj, String simNao){
		if (simNao == "sim") {
			Compromisso com = new Compromisso();
			CompromissoService comService = new CompromissoService();
			com = comService.findById(obj.getForn().getCodigo(), obj.getNnfEnt());
			if (com != null) {
				double vlrMat = (obj.getQuantidadeMatEnt() * obj.getValorMatEnt());
				if (com.getValorCom() >= vlrMat) {
					double vlrFim = com.getValorCom() - vlrMat;
					com.setValorCom(vlrFim);
				}	
			}	
			if (com != null) {
				if (com.getValorCom() > 0.00) {
					if (com.getPrazoCom() == 1 && com.getParcelaCom() == 1) {
						com.setSituacaoCom(1);
					} else {
						com.setSituacaoCom(0);
					}
					comService.saveOrUpdate(com);
					CalculaParcela.parcelaCreate(com);
				} else {	
					parService.removeNnf(obj.getNnfEnt(), obj.getForn().getCodigo());
					comService.remove(obj.getForn().getCodigo(), obj.getNnfEnt());
				}	
			}	
		}
	}
 }
