package gui.copia;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.MainSgo;
import gui.copia.volta.RestauraAdiantamento;
import gui.copia.volta.RestauraBalcao;
import gui.copia.volta.RestauraCargo;
import gui.copia.volta.RestauraCliente;
import gui.copia.volta.RestauraComissao;
import gui.copia.volta.RestauraCompromisso;
import gui.copia.volta.RestauraEntrada;
import gui.copia.volta.RestauraFornecedor;
import gui.copia.volta.RestauraFuncionario;
import gui.copia.volta.RestauraGrupo;
import gui.copia.volta.RestauraLogin;
import gui.copia.volta.RestauraMaterial;
import gui.copia.volta.RestauraNotaFiscal;
import gui.copia.volta.RestauraOS;
import gui.copia.volta.RestauraOrcamento;
import gui.copia.volta.RestauraParcela;
import gui.copia.volta.RestauraPeriodo;
import gui.copia.volta.RestauraReceber;
import gui.copia.volta.RestauraReposicao;
import gui.copia.volta.RestauraSituacao;
import gui.copia.volta.RestauraTipo;
import gui.copia.volta.RestauraVeiculo;
import gui.copia.volta.RestauraVirtual;
import gui.listerneres.DataChangeListener;
import gui.util.Alerts;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RestauraSgoController implements Initializable, DataChangeListener {

	@FXML
	private VBox vBoxRestaura;

	@FXML
	private Button buttonOk;

 	@FXML
	private Label labelFile;

	@FXML
	private Label labelUser;

	@FXML
	private Label labelCount;

	@FXML
	private TableView<Restaura> tableViewRestaura;

 	@FXML
 	private TableColumn<Restaura, String> tableColumnFile;

 	@FXML
 	private TableColumn<Restaura, String> tableColumnStatus;

 	@FXML
 	private TableColumn<Restaura, Integer> tableColumnCount;

 	private ObservableList<Restaura> obsList;

	int flagStart = 0;
	Integer count = 0;
	Integer countAk = 0;
	String arq = "";
	String crip = "";
	String status = "";
	Date dataI = new Date(System.currentTimeMillis());
	Date dataF = new Date();
    String classe = "BackUp SGO ";
	static String meioSgo = ":\\Arqs\\Backup\\SGO\\";
	public String meioSgcp= ":\\Arqs\\Backup\\SGOCP\\";
	public static String ext = ".Bck";
	public static String file = "";
	public static String unid = null;
	public static String path = null;
 	public String user = "usuário";
 	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
 	SimpleDateFormat sdfAno = new SimpleDateFormat("yyyy-MM-dd HH:mm");
 	Calendar cal = Calendar.getInstance();
 	
 	public RestauraService service;
 	private Restaura entity;
 	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

 	public void setRestauraService(RestauraService service) {
 		this.service = service;
 	}
 	
 	public void setEntity(Restaura entity) {
 		this.entity = entity;
 	}

 	@FXML
  	public void onBtOkAdiAction(ActionEvent event) throws ParseException {
		flagStart = 1;
		updateTableView();
		flagStart = 0;
 		 Stage parentStage = Utils.currentStage(event);
 		 createDialogForm("/gui/copia/RestauraForm.fxml", parentStage);
 		 executaBack();
 		 updateTableView();
   	}
 	
 	public void executaBack() throws ParseException {
 		if (unid != null) {
 			count = 0;
 			labelCount.setText("0");
 			labelCount.viewOrderProperty();
 			login();
 			cargo();
 			tipo();
 			fornecedor();
 			grupo();
 			notaFiscal();
 			situacao();
			periodo();
 			cliente();
 			veiculo();
 			material();
			Optional<ButtonType> result = Alerts.showConfirmation(null, "Execute Scrypt II ");
			if (result.get() == ButtonType.OK) {
	 			funcionario();
	 			adiantamento();
	 			comissao();
				orcamento();
				virtual();
				os();
				balcao();
				entrada();
				receber();
				reposicao();
				compromisso();
				parcela();
			}	
			labelFile.setText("Kbou!!!");
			labelFile.viewOrderProperty();
			labelCount.viewOrderProperty();
 		}	
 	}

	public void adiantamento() throws ParseException {
		status = "Ok";
		file = "Adiantamento";
		countAk = 0;
		path = unid + meioSgo + file + ext;
		countAk = RestauraAdiantamento.restauraAdiantamento(countAk, unid, meioSgo, file, ext);
		gravaRestaura();
	}
	
	public void comissao() throws ParseException {
		status = "Ok";
		file = "Comissao";
		countAk = 0;
		path = unid + meioSgo + file + ext;
		countAk = RestauraComissao.restauraComissao(countAk, unid, meioSgo, file, ext);
		gravaRestaura();
	}
	
	public void balcao() throws ParseException {
		status = "Ok";
		file = "Balcao";
		countAk = 0;
		path = unid + meioSgo + file + ext;
		countAk = RestauraBalcao.restauraBalcao(countAk, unid, meioSgo, file, ext);
		gravaRestaura();
	}
	
	public void cargo() throws ParseException {
		status = "Ok";
		file = "Cargo";
		countAk = 0;
		path = unid + meioSgo + file + ext;
		countAk = RestauraCargo.restauraCargo(countAk, unid, meioSgo, file, ext);
		gravaRestaura();
	}
	
	public void cliente() throws ParseException {
		status = "Ok";
		file = "Cliente";
		countAk = 0;
		path = unid + meioSgo + file + ext;
		countAk = RestauraCliente.restauraCliente(countAk, unid, meioSgo, file, ext);
		gravaRestaura();
	}
	
	public void entrada() throws ParseException {
		status = "Ok";
		file = "Entrada";
		countAk = 0;
		path = unid + meioSgo + file + ext;
		countAk = RestauraEntrada.restauraEntrada(countAk, unid, meioSgo, file, ext);
		gravaRestaura();
	}
	
	public void funcionario() throws ParseException {
		status = "Ok";
		file = "Funcionario";
		countAk = 0;
		path = unid + meioSgo + file + ext;
		countAk = RestauraFuncionario.restauraFuncionario(countAk, unid, meioSgo, file, ext);
		gravaRestaura();
	}
	
	public void grupo() throws ParseException {
		status = "Ok";
		file = "Grupo";
		countAk = 0;
		path = unid + meioSgo + file + ext;
		countAk = RestauraGrupo.restauraGrupo(countAk, unid, meioSgo, file, ext);
		gravaRestaura();
	}
	
	public void login() throws ParseException {
		status = "Ok";
		file = "Login";
		countAk = 0;
		path = unid + meioSgo + file + ext;
		countAk = RestauraLogin.restauraLogin(countAk, unid, meioSgo, file, ext);
		gravaRestaura();
	}
	
	public void material() throws ParseException {
		status = "Ok";
		file = "Material";
		countAk = 0;
		path = unid + meioSgo + file + ext;
		countAk = RestauraMaterial.restauraMaterial(countAk, unid, meioSgo, file, ext);
		gravaRestaura();
	}
		
	public void notaFiscal() {
		status = "Ok";
		file = "NotaFiscal";
		countAk = 0;
		path = unid + meioSgo + file + ext;
		countAk = RestauraNotaFiscal.restauraNotaFiscal(countAk, unid, meioSgo, file, ext);
		gravaRestaura();
	}
		
	public void orcamento() {
		status = "Ok";
		file = "Orcamento";
		countAk = 0;
		path = unid + meioSgo + file + ext;
		countAk = RestauraOrcamento.restauraOrcamento(countAk, unid, meioSgo, file, ext);
		gravaRestaura();
	}
		
	public void virtual() throws ParseException {
		status = "Ok";
		file = "OrcVirtual";
		countAk = 0;
		path = unid + meioSgo + file + ext;
		countAk = RestauraVirtual.restauraVirtual(countAk, unid, meioSgo, file, ext);
		gravaRestaura();
	}
		
	public void os() throws ParseException {
		status = "Ok";
		file = "OrdemServico";
		countAk = 0;
		path = unid + meioSgo + file + ext;
		countAk = RestauraOS.restauraOrdemServico(countAk, unid, meioSgo, file, ext);
		gravaRestaura();
	}
		
	public void receber() throws ParseException {
		status = "Ok";
		file = "Receber";
		countAk = 0;
		path = unid + meioSgo + file + ext;
		countAk = RestauraReceber.restauraReceber(countAk, unid, meioSgo, file, ext);
		gravaRestaura();
	}
		
	public void reposicao() throws ParseException {
		status = "Ok";
		file = "ReposicaoVeiculo";
		countAk = 0;
		path = unid + meioSgo + file + ext;
		countAk = RestauraReposicao.restauraReposicao(countAk, unid, meioSgo, file, ext);
		gravaRestaura();
	}
		
	public void situacao() throws ParseException {
		status = "Ok";
		file = "Situacao";
		countAk = 0;
		path = unid + meioSgo + file + ext;
		countAk = RestauraSituacao.restauraSituacao(countAk, unid, meioSgo, file, ext);
		gravaRestaura();
	}
		
	public void veiculo() throws ParseException {
		status = "Ok";
		file = "Veiculo";
		countAk = 0;
		path = unid + meioSgo + file + ext;
		countAk = RestauraVeiculo.restauraVeiculo(countAk, unid, meioSgo, file, ext);
		gravaRestaura();
	}
		
	public void compromisso() throws ParseException {
		status = "Ok";
		file = "Compromisso";
		countAk = 0;
		path = unid + meioSgcp + file + ext;
		countAk = RestauraCompromisso.restauraCompromisso(countAk, unid, meioSgcp, file, ext);
		gravaRestaura();
	}
		
	public void fornecedor() throws ParseException {
		status = "Ok";
		file = "Fornecedor";
		countAk = 0;
		path = unid + meioSgcp + file + ext;
		countAk = RestauraFornecedor.restauraFornecedor(countAk, unid, meioSgcp, file, ext);
		gravaRestaura();
	}
		
	public void parcela() throws ParseException {
		status = "Ok";
		file = "Parcela";
		countAk = 0;
		path = unid + meioSgcp + file + ext;
		countAk = RestauraParcela.restauraParcela(countAk, unid, meioSgcp, file, ext);
		gravaRestaura();
	}
		
	public void periodo() throws ParseException {
		status = "Ok";
		file = "Periodo";
		countAk = 0;
		path = unid + meioSgcp + file + ext;
		countAk = RestauraPeriodo.restauraPeriodo(countAk);
		gravaRestaura();
	}
		
	public void tipo() throws ParseException {
		status = "Ok";
		file = "TipoConsumidor";
		countAk = 0;
		path = unid + meioSgcp + file + ext;
		countAk = RestauraTipo.restauraTipo(countAk, unid, meioSgcp, file, ext);
		gravaRestaura();
	}
		
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}

 	private void initializeNodes() {
		tableColumnFile.setCellValueFactory(new PropertyValueFactory<>("FileUp"));
		tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("StatusUp"));
		tableColumnCount.setCellValueFactory(new PropertyValueFactory<>(String.valueOf("CountUp")));
 		Stage stage = (Stage) MainSgo.getMainScene().getWindow();
 		tableViewRestaura.prefHeightProperty().bind(stage.heightProperty());
 	}

 	public void updateTableView() {
 		if (service == null) {
			throw new IllegalStateException("Serviço está vazio");
 		}
		List<Restaura> listR = service.findAll();
 		labelUser.setText(user);
 		labelFile.setText("<<<aguarde>>>");
 		labelCount.setText(String.valueOf(count));
 		if (flagStart == 1) {
 			listR.add(new Restaura(null, "  Processamento", null, null));
 			listR.add(new Restaura(null, "  <<<aguarde>>>", null, null));
 		}
  		obsList = FXCollections.observableArrayList(listR);
  		tableViewRestaura.setItems(obsList);
	}

	private void notifyDataChangeListerners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	@Override
	public void onDataChanged() {
		initializeNodes();
	}
	
 	public void gravaRestaura() {
		entity.setIdRestauraUp(null);
		entity.setFileUp(file);
		entity.setStatusUp(status);
		entity.setCountUp(countAk);
		service.saveOrUpdate(entity);
		count += 1;
		labelCount.setText(String.valueOf(count));
		labelCount.viewOrderProperty();
		notifyDataChangeListerners();
		updateTableView();
 	}
 	
	private void createDialogForm(String absoluteName, Stage parentStage) {
        String classe = "BackUp SGO ";
		try {
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			RestauraSgoFormController controller = loader.getController();
			controller.loadAssociatedObjects();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Selecione unid para Restaurar                 ");
 			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro carregando tela" + classe, e.getMessage(), AlertType.ERROR);
		}
 	} 
}
