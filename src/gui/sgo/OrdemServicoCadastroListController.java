package gui.sgo;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import application.MainSgo;
import db.DbException;
import db.DbIntegrityException;
import gui.listerneres.DataChangeListener;
import gui.sgcpmodel.entities.consulta.ParPeriodo;
import gui.sgcpmodel.services.ParPeriodoService;
import gui.sgomodel.entities.Adiantamento;
import gui.sgomodel.entities.Grupo;
import gui.sgomodel.entities.Material;
import gui.sgomodel.entities.OrcVirtual;
import gui.sgomodel.entities.Orcamento;
import gui.sgomodel.entities.OrdemServico;
import gui.sgomodel.entities.Receber;
import gui.sgomodel.entities.ReposicaoVeiculo;
import gui.sgomodel.entities.Veiculo;
import gui.sgomodel.services.AdiantamentoService;
import gui.sgomodel.services.EmpresaService;
import gui.sgomodel.services.FuncionarioService;
import gui.sgomodel.services.MaterialService;
import gui.sgomodel.services.NotaFiscalService;
import gui.sgomodel.services.OrcVirtualService;
import gui.sgomodel.services.OrcamentoService;
import gui.sgomodel.services.OrdemServicoService;
import gui.sgomodel.services.ReceberService;
import gui.sgomodel.services.ReposicaoVeiculoService;
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
 
public class OrdemServicoCadastroListController implements Initializable, DataChangeListener {

// inje��o de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private OrdemServicoService service;
	private ReceberService recService;
	private ReposicaoVeiculoService repService;
	private OrcamentoService orcService;
	@SuppressWarnings("unused")
	private OrcVirtualService virService;
	@SuppressWarnings("unused")
	private MaterialService matService;
	private VeiculoService veiService;
	private AdiantamentoService adiService;
 	
	@FXML
 	private TableView<OrdemServico> tableViewOrdemServico;
 	
// c/ entidade e coluna	
 	@FXML
 	private TableColumn<OrdemServico, Integer>  tableColumnNumeroOS;
 	
 	@FXML
 	private TableColumn<OrdemServico, Date>  tableColumnDataOS;
 	
 	@FXML
 	private TableColumn<OrdemServico, Integer>  tableColumnOrcamentoOS;

 	@FXML
 	private TableColumn<OrdemServico, String> tableColumnClienteOS;

   	@FXML
 	private TableColumn<OrdemServico, String> tableColumnPlacaOS;
 	 	
   	@FXML
 	private TableColumn<OrdemServico, Double> tableColumnValorOS;
 	
  	@FXML
 	private TableColumn<OrdemServico, OrdemServico> tableColumnEDITA;

 	@FXML
 	private TableColumn<OrdemServico, OrdemServico> tableColumnREMOVE ;

 	@FXML
 	private TableColumn<OrdemServico, OrdemServico> tableColumnList ;

 	@FXML
 	private Button btNewOS;

 	@FXML
 	private Label labelUser;

 // auxiliar
 	public static Integer nivel = null;
 	public String user = "usuário";		
 	int codOrd = 0;
 	int flagD = 0;
 	int flagR = 0;
 	String classe = "";
 	public static Integer numEmp = 0;
 	
// carrega aqui os fornecedores Updatetableview (metodo)
 	private ObservableList<OrdemServico> obsList;
 
 /* 
   * ActionEvent - referencia p/ o controle q receber o evento c/ acesso ao stage
  * com currentStage -
  * janela pai - parentstage
  * vamos abrir o forn form	
  */
	 Orcamento objOrc = new Orcamento();
	 OrcVirtual objVir = new OrcVirtual();
	 Material objMat = new Material();
	 Receber objRec = new Receber();
	 ReposicaoVeiculo objRep = new ReposicaoVeiculo();
	 Grupo objGru = new Grupo();
	 ParPeriodo objPer = new ParPeriodo();
	 Veiculo objVei = new Veiculo();
	 Adiantamento adiantamento = new Adiantamento();
	 
	@FXML
  	public void onBtNewOSAction(ActionEvent event) {
 		 Stage parentStage = Utils.currentStage(event);
 		 OrdemServico obj = new OrdemServico();
 		 createDialogForm(obj, objOrc, objVir, objMat, objRec, objRep, objGru, objPer, 
			 "/gui/sgo/OrdemServicoCadastroForm.fxml", parentStage);
   	}
 	
// injeta a dependencia com set (invers�o de controle de inje�ao)	
 	public void setServices(OrdemServicoService service, ReceberService recService, ReposicaoVeiculoService repService,
 			OrcamentoService orcService, OrcVirtualService virService, MaterialService matService, VeiculoService veiService,
 			AdiantamentoService adiService) {
 		this.service = service;
 		this.recService = recService;
 		this.repService = repService;
 		this.orcService = orcService;
 		this.virService = virService;
 		this.matService = matService;
 		this.veiService = veiService;
 		this.adiService = adiService;
 	}

 // inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}

// comportamento padr�o para iniciar as colunas 	
 	private void initializeNodes() {
		tableColumnNumeroOS.setCellValueFactory(new PropertyValueFactory<>("numeroOS"));
		tableColumnDataOS.setCellValueFactory(new PropertyValueFactory<>("dataOS"));
		Utils.formatTableColumnDate(tableColumnDataOS, "dd/MM/yyyy");
		tableColumnOrcamentoOS.setCellValueFactory(new PropertyValueFactory<>("orcamentoOS"));
   		tableColumnClienteOS.setCellValueFactory(new PropertyValueFactory<>("clienteOS"));
		tableColumnPlacaOS.setCellValueFactory(new PropertyValueFactory<>("placaOS"));
 		tableColumnValorOS.setCellValueFactory(new PropertyValueFactory<>("valorOS"));
		Utils.formatTableColumnDouble(tableColumnValorOS, 2);
 		// para tableview preencher o espa�o da tela scroolpane, referencia do stage		
		Stage stage = (Stage) MainSgo.getMainScene().getWindow();
		tableViewOrdemServico.prefHeightProperty().bind(stage.heightProperty());
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
			throw new IllegalStateException("Serviço OS está vazio");
 		}
 		labelUser.setText(user);

		Date dataHj = new Date();
		Calendar cal = Calendar.getInstance(); 
		cal = Calendar.getInstance();
		cal.setTime(dataHj);
		int mm = cal.get(Calendar.MONTH) + 1;
		int aa = cal.get(Calendar.YEAR) - 1;
 		List<OrdemServico> list = service.findByMesAnoList(mm, aa);
  		obsList = FXCollections.observableArrayList(list);
  		tableViewOrdemServico.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
		initListButtons();
	}

/* 	
* parametro informando qual stage criou essa janela de dialogo - stage parent
* nome da view - absolutename
* carregando uma janela de dialogo modal (só sai qdo sair dela, tem q instaciar um stage e dps a janela dialog
*/
 	private synchronized void createDialogForm(OrdemServico obj, Orcamento objOrc, 
 			OrcVirtual objVir, Material objMat, Receber objRec, ReposicaoVeiculo objRep, 
 			Grupo objGru, ParPeriodo objPer,
 			String absoluteName, Stage parentStage) {
 		try {
 				FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
 				Pane pane = loader.load();
			
// referencia para o controlador = controlador da tela carregada ListForm			
 				OrdemServicoCadastroFormController controller = loader.getController();
 				controller.user = user;
// injetando passando parametro obj 		
// injetando servi�os vindo da tela de formulario form
 				controller.setOrdemEntity(obj, objOrc, objMat, objPer, objVei);
 				controller.setServices(new OrdemServicoService(), new OrcamentoService(), new OrcVirtualService(), 
 						new MaterialService(), new ReceberService(), new ReposicaoVeiculoService(), new ParPeriodoService(),
 						new VeiculoService(), new FuncionarioService(), new AdiantamentoService(), new NotaFiscalService());
 	 			if (obj.getNumeroOS() != null) {
	 				objOrc = orcService.findById(obj.getOrcamentoOS());
	 				if (! objOrc.getOsOrc().equals(null)) {
	 					Alerts.showAlert("Atenção!!!", "OS fechada ", "Sem acesso ", AlertType.INFORMATION);
	 				} 
	 			} else {
 				controller.loadAssociatedObjects();
// inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
 				controller.subscribeDataChangeListener(this);
//	carregando o obj no formulario (FormControl)			
 				controller.updateFormData();
			
 				Stage dialogStage = new Stage();
 				dialogStage.setTitle("Digite Ordem de Serviço                                      ");
 				dialogStage.setScene(new Scene(pane));
// pode redimencionar a janela: s/n?
 				dialogStage.setResizable(false);
// quem e o stage pai da janela?
 				dialogStage.initOwner(parentStage);
// travada enquanto n�o sair da tela
 				dialogStage.initModality(Modality.WINDOW_MODAL);
 				dialogStage.showAndWait();
 			}	
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", classe + "Erro carregando tela", e.getMessage(), AlertType.ERROR);
		}
 		catch (ParseException p) {
 			p.printStackTrace();
 		}
 	} 
 	
 // Imprimir	
  	@SuppressWarnings("static-access")
	private synchronized void createDialogImprime(OrdemServico obj, OrcVirtual objVir, String absoluteName, Stage parentStage) {
  			try {
  	 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
  				Pane pane = loader.load();
  				classe = "OrdemServiço List ";
  	 			OrdemServicoImprimeController controller = loader.getController();
  	 			controller.setOrdemServico(obj);
  	 			controller.setOSImprime(new OrdemServicoService(),
  	 									new OrcamentoService(), 
  	 									new OrcVirtualService(),
  	 									new ReceberService(),
  	 									new EmpresaService());
  	 			controller.codOs = obj.getNumeroOS();
  	 			controller.numEmp = numEmp;
  	   			Stage dialogStage = new Stage();
  	 			dialogStage.setTitle("Lista Ordem de Serviço                                             ");
  	 			dialogStage.setScene(new Scene(pane));
  	 			dialogStage.setResizable(false);
  	  			dialogStage.initOwner(parentStage);
  	 			dialogStage.initModality(Modality.WINDOW_MODAL);
  				dialogStage.showAndWait();
  			}
  			catch (IOException e) {
  				e.printStackTrace();
  				Alerts.showAlert("IO Exception", classe + "Erro carregando tela " + classe, e.getMessage(), AlertType.ERROR);
  			}
  	  	}

// lista da classe subject (form) - guarda lista de obj p/ receber e emitir o evento
		private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

// *   um for p/ cada listener da lista, eu aciono o metodo onData no DataChangListner...   
		@SuppressWarnings("unused")
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
		  tableColumnEDITA.setCellFactory(param -> new TableCell<OrdemServico, OrdemServico>() { 
		    private final Button button = new Button("edita"); 
		    @Override 
		    protected void updateItem(OrdemServico obj, boolean empty) { 
		      super.updateItem(obj, empty); 
		      if (obj == null) { 
		        setGraphic(null); 
		        return; 
		      } 
	    	  setGraphic(button); 
		    	  button.setOnAction( 
						event -> createDialogForm(obj, objOrc, objVir, objMat, objRec, objRep, objGru, objPer, 
								"/gui/sgo/OrdemServicoCadastroForm.fxml", Utils.currentStage(event)));
		      		
		    } 
		  	});
	}
		  
	private void initRemoveButtons() {		
		  tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		  tableColumnREMOVE.setCellFactory(param -> new TableCell<OrdemServico, OrdemServico>() { 
		        private final Button button = new Button("exclui"); 
		 
		        @Override 
		        protected void updateItem(OrdemServico obj, boolean empty) { 
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

	private void removeEntity(OrdemServico obj) {
		if (nivel > 1 && nivel < 9) {
			Alerts.showAlert(null, "Atenção", "Operaçaoo não permitida", AlertType.INFORMATION);
		} else {
			conferePagamento(flagD, obj);
			if (flagD == 0) {
				Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que deseja excluir?");
				if (result.get() == ButtonType.OK) {
					if (service == null) {
						throw new IllegalStateException("Serviço está vazio");
					}
					try {
						if (flagD == 0) {
							updateEstoqueMaterial(obj.getOrcamentoOS());
							classe = "Receber OS List ";
							recService.removeOS(obj.getNumeroOS());
							classe = "Reposição OS List ";
							repService.remove(obj.getNumeroOS());
							updateOsOrcamento(obj.getOrcamentoOS());
							updateAdiantamento(obj.getNumeroOS());						
							classe = "Ordem de serviço List ";
							service.remove(obj);
							updateKmVeiculo(obj);
							updateTableView();
						}	
					}
					catch (DbException e) {
						Alerts.showAlert("Erro removendo objeto", classe, e.getMessage(), AlertType.ERROR);
					}	
					catch (DbIntegrityException e) {
						Alerts.showAlert("Erro removendo objeto", classe, e.getMessage(), AlertType.ERROR);
					}	
				}
			}	
		}
	}
	
	private void updateEstoqueMaterial(int numOrc) {
		Material mat1 = new Material();
		OrcVirtualService virService = new OrcVirtualService();
		List<OrcVirtual> listVirtual = virService.findByOrcto(numOrc);
		for (OrcVirtual cv : listVirtual) {
			if (cv.getNumeroVir() != null) {
				if (cv.getNumeroOrcVir() == numOrc) {
					mat1 = matService.findById(cv.getMaterial().getCodigoMat());
					if (!mat1.getGrupo().getNomeGru().equals("Serviços") || !mat1.getGrupo().getNomeGru().equals("Mão de obra")) {
						mat1.entraSaldo(cv.getQuantidadeMatVir());
						if (mat1.getSaidaCmmMat() >= cv.getQuantidadeMatVir()) {
							mat1.setSaidaCmmMat(-1 *  cv.getQuantidadeMatVir());
						}	
						mat1.getSaldoMat();
						matService.saveOrUpdate(mat1);
					}	
				}
			}	
		}
	}

	private void updateAdiantamento(int numOs) {
		List<Adiantamento> adianto = adiService.findByOs(numOs);
		for (Adiantamento a : adianto) {
			if (a.getOsAdi().equals(numOs)) {
				adiService.remove(a.getNumeroAdi());
			}	
		}
	}

	private void updateKmVeiculo(OrdemServico obj) {
		classe = "Veiculo OS List ";
		objOrc = orcService.findById(obj.getOrcamentoOS());		
		objVei = veiService.findByPlaca(obj.getPlacaOS());
		objVei.setKmInicialVei(objOrc.getKmInicialOrc());
		objVei.setKmFinalVei(objOrc.getKmFinalOrc());
		veiService.saveOrUpdate(objVei);
	}
	
	private void updateOsOrcamento(int numOrc) {
		classe = "Orçamento OS List ";
		objOrc = orcService.findById(numOrc);
		objOrc.setOsOrc(0);
		orcService.saveOrUpdate(objOrc);
	}

	private Integer conferePagamento(Integer flag, OrdemServico obj) {
		flagD = 0;
		classe = "Receber OS List ";
		List<Receber> list = new ArrayList<>(); 		
		list = recService.findByAllOs(obj.getNumeroOS());
		if (list.size() > 0) {
			List<Receber> st = list.stream()
				.filter(p -> p.getOsRec() == (obj.getNumeroOS()))
				.filter(p -> p.getDataPagamentoRec().after(p.getDataOsRec()))
				.collect(Collectors.toList());	
			if (st.size() > 0) {
				Alerts.showAlert("Erro!!! ", "Exclusão inválida " + classe, "Existe(m) parcela(s) paga(s) para OS !!!", AlertType.ERROR);
				flagD = 1;
			}
		}	
		return flagD;
	}	

	private void initListButtons() {
		  tableColumnList.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		  tableColumnList.setCellFactory(param -> new TableCell<OrdemServico, OrdemServico>() { 
		    private final Button button = new Button("lista");	    

		    @Override 
		    protected void updateItem(OrdemServico obj, boolean empty) { 
		      super.updateItem(obj, empty); 
		      if (obj == null) { 
		        setGraphic(null); 
		        return; 
		      }
		    
		      setGraphic(button); 
		      button.setOnAction( 
		    		  event -> createDialogImprime(obj, objVir, "/gui/sgo/OrdemServicoImprime.fxml",Utils.currentStage(event)));
		    }
		 });  
	}
 }
