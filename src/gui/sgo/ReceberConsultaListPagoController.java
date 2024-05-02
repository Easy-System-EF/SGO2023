package gui.sgo;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.MainSgo;
import gui.listerneres.DataChangeListener;
import gui.sgcpmodel.entities.consulta.ParPeriodo;
import gui.sgcpmodel.services.ParPeriodoService;
import gui.sgomodel.entities.Cliente;
import gui.sgomodel.entities.Receber;
import gui.sgomodel.services.ClienteService;
import gui.sgomodel.services.ReceberService;
import gui.util.Alerts;
import gui.util.Maria;
import gui.util.Mascaras;
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
 
public class ReceberConsultaListPagoController implements Initializable, DataChangeListener {

	@FXML
 	private TableView<Receber> tableViewReceber;
 	
 	@FXML
 	private Label labelTitulo;
 	
 	@FXML
 	private TableColumn<Receber, Integer>  tableColumnNumeroOsRec;
 	
 	@FXML
 	private TableColumn<Receber, Date>  tableColumnDataOsRec;
 	
 	@FXML
 	private TableColumn<Receber, String> tableColumnClienteRec;

   	@FXML
 	private TableColumn<Receber, String> tableColumnPlacaRec;
 	 	
   	@FXML
 	private TableColumn<Receber, Integer> tableColumnParcelaRec;
 	 	
   	@FXML
 	private TableColumn<Receber, Date> tableColumnVencimentoRec;
 	 	
   	@FXML
 	private TableColumn<Receber, Double> tableColumnValorRec;
 	
   	@FXML
 	private TableColumn<Receber, Date> tableColumnPagamentoRec;
 	 	
   	@FXML
 	private TableColumn<Receber, String> tableColumnFormaRec;
 	 	
  	@FXML
 	private TableColumn<Receber, Receber> tableColumnEDITA;

	@FXML
	private Button btClientePago;

 	@FXML
	private Button btPeriodoPago ;

	@FXML
	private Label labelUser;

	public String user = "";
 	 	
// auxiliar 	
 	String classe = "Receber";
 	String nomeTitulo = "Consulta Contas Recebidas";
 	String opcao = "t";
 	static Integer codCli = null;
 	static String pesquisa = null;
 	 	
// carrega aqui os dados Updatetableview (metodo)
 	private ObservableList<Receber> obsList;
 
 // inje��o de dependenia sem implementar a classe (instanciat)
 // acoplamento forte - implementa via set
 	private ReceberService service;
  	
// injeta a dependencia com set (invers�o de controle de inje�ao)	
 	public void setReceberService(ReceberService service) {
	 		this.service = service;
 	}
 	
	Cliente objCli = new Cliente();
	ParPeriodo objPer = new ParPeriodo();
	
 	@SuppressWarnings("static-access")
	public void onBtClienteAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		nomeTitulo = "Contas Recebidas por Cliente";
		opcao = "c";
		classe = "Cliente";
  		createDialogOpcao(objCli, objPer, "/gui/sgo/PesquisaAlphaForm.fxml", (parentStage), 
  				(RecClienteFormController contC) -> {
  			contC.setDestino("List");
  			contC.setSituacao("Recebido");
  			contC.pesquisa = pesquisa;
			contC.setCliente(objCli);
			contC.setService(new ClienteService());
			contC.loadAssociatedObjects();
			contC.subscribeDataChangeListener(this);
			contC.updateFormData();
 		});
	}
 	
	@FXML
	public void onBtPeriodoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		nomeTitulo = "Contas Recebidas por Periodo";
		opcao = "p";
		classe = "Periodo";
  		createDialogOpcao(objCli, objPer, "/gui/sgo/RecPeriodoForm.fxml", (parentStage), 
  				(RecPeriodoFormController contP) -> {
  		contP.setPeriodo(objPer);
  		contP.setPeriodoService(new ParPeriodoService());
		contP.loadAssociatedObjects();
		contP.subscribeDataChangeListener(this);
		contP.updateFormData();
  		});
  	}
  
 // inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}

// comportamento padr�o para iniciar as colunas 	
 	private void initializeNodes() {
  		labelTitulo.setText(String.format("%s ", nomeTitulo));
 		tableColumnNumeroOsRec.setCellValueFactory(new PropertyValueFactory<>("osRec"));
		tableColumnDataOsRec.setCellValueFactory(new PropertyValueFactory<>("dataOsRec"));
		Utils.formatTableColumnDate(tableColumnDataOsRec, "dd/MM/yyyy");
   		tableColumnClienteRec.setCellValueFactory(new PropertyValueFactory<>("nomeClienteRec"));
		tableColumnPlacaRec.setCellValueFactory(new PropertyValueFactory<>("placaRec"));
		tableColumnParcelaRec.setCellValueFactory(new PropertyValueFactory<>("parcelaRec"));
		tableColumnVencimentoRec.setCellValueFactory(new PropertyValueFactory<>("dataVencimentoRec"));
		Utils.formatTableColumnDate(tableColumnVencimentoRec, "dd/MM/yyyy");
 		tableColumnValorRec.setCellValueFactory(new PropertyValueFactory<>("valorRec"));
 		Utils.formatTableColumnDouble(tableColumnValorRec, 2);
		tableColumnPagamentoRec.setCellValueFactory(new PropertyValueFactory<>("dataPagamentoRec"));
		Utils.formatTableColumnDate(tableColumnPagamentoRec, "dd/MM/yyyy");
   		tableColumnFormaRec.setCellValueFactory(new PropertyValueFactory<>("formaPagamentoRec"));
 		// para tableview preencher o espa�o da tela scroolpane, referencia do stage		
		Stage stage = (Stage) MainSgo.getMainScene().getWindow();
		tableViewReceber.prefHeightProperty().bind(stage.heightProperty());
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
			throw new IllegalStateException("Serviço Rec está vazio");
 		}
 		labelUser.setText(user);
 		List<Receber> list = new ArrayList<>();
 		if (opcao == "t") {
 			LocalDate ldt = Maria.criaLocalAtual();
 			int mm = Maria.mesDaData(ldt);
 			int aa = Maria.anoDaData(ldt);
 			int df = Maria.ultimoDiaMes(ldt);
 			LocalDate dt1 = Maria.criaAnoMesDia(aa, mm, 01);
 			df = Maria.ultimoDiaMes(dt1);
 			Date dti = Maria.localParaDateSdfAno(dt1);
 			dt1 = Maria.criaAnoMesDia(aa, mm, df);
 			Date dtf = Maria.localParaDateSdfAno(dt1);
 			list = service.findAllPago(dti, dtf);
 		}
 		if (codCli != null) {
 			if (opcao == "c") {
 				list = service.findByIdClientePago(codCli);
 			}	
 		} 
		if (opcao == "p") {
			list = service.findPeriodoPago();
		}	
		if (list.size() == 0) {
			Alerts.showAlert(null, "Contas Recebidas ", "Não há registro para opção ", AlertType.INFORMATION);
		} else {
			Date dt  = new Date();
			double pix = 0.0;
			double deb = 0.0;
			double din = 0.0;
			double cc  = 0.0;
			double tot = 0.0;
			
			for (Receber r : list) {
				if (r.getFormaPagamentoRec().equals("Dinheiro")) {
					din += r.getValorPagoRec();
				}
				if (r.getFormaPagamentoRec().equals("Pix")) {
					pix += r.getValorPagoRec();
				}
				if (r.getFormaPagamentoRec().equals("Débito") || r.getFormaPagamentoRec().equals("Debito")) {
					deb += r.getValorPagoRec();
				}
				if (r.getFormaPagamentoRec().equals("CC")) {
					cc += r.getValorPagoRec();
				}
				tot +=r.getValorPagoRec();
			}
			String vlrDin = "";
			String vlrPix = "";
			String vlrDeb = "";
			String vlrCC = "";
			String vlrTot = "";
			String total = "";
			try {
				vlrDin = Mascaras.formataValor(din);
				vlrPix = Mascaras.formataValor(pix);
				vlrDeb = Mascaras.formataValor(deb);
				vlrCC = Mascaras.formataValor(cc);
				vlrTot = Mascaras.formataValor(tot);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (din > 0) {
				total = "Total dinheiro... " + "R$" + vlrDin;
				list.add(new Receber(null, null, null, total, null, dt, null, null, null, 0.00, dt, dt, null, null, null, null, null));
			}
			if (pix > 0) {
				total = "Total pix.......... " + "R$" + vlrPix;
				list.add(new Receber(null, null, null, total, null, dt, null, null, null, 0.00, dt, dt, null, null, null, null, null));
			}
			if (deb > 0) {
				total = "Total débito..... " + "R$" + vlrDeb;
				list.add(new Receber(null, null, null, total, null, dt, null, null, null, 0.00, dt, dt, null, null, null, null, null));
			}
			if (cc > 0) {
				total = "Total CC.......... " + "R$" + vlrCC;
				list.add(new Receber(null, null, null, total, null, dt, null, null, null, 0.00, dt, dt, null, null, null, null, null));
			}
			if (tot > 0) {
				total = "Total geral....... " + "R$" + vlrTot;
				list.add(new Receber(null, null, null, total, null, dt, null, null, null, 0.00, dt, dt, null, null, null, null, null));
			}	
		}

  		labelTitulo.setText(String.format("%s ", nomeTitulo));
  		obsList = FXCollections.observableArrayList(list);
  		tableViewReceber.setItems(obsList);
		notifyDataChangeListerners();
	}
 	
	@SuppressWarnings("unused")
	private synchronized <T> void createDialogPesquisa(String absoluteName,  Stage parentStag, 
			Consumer<T> initializeAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			 
			T cont = loader.getController();
 			initializeAction.accept(cont);

 			Stage dialogStage = new Stage();
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStag);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", classe, e.getMessage(), AlertType.ERROR);
		}
	}

	private synchronized <T> void createDialogOpcao(Cliente objCli, ParPeriodo objPer, 
			String absoluteName,  Stage parentStag, Consumer<T> initializeAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			 
			T cont = loader.getController();
 			initializeAction.accept(cont);

 			Stage dialogStage = new Stage();
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStag);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", classe, e.getMessage(), AlertType.ERROR);
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
 }
