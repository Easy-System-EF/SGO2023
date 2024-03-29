package gui.sgo;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listerneres.DataChangeListener;
import gui.sgcp.FornecedorCadastroFormController;
import gui.sgcpmodel.entities.Fornecedor;
import gui.sgcpmodel.services.FornecedorService;
import gui.sgomodel.entities.Entrada;
import gui.sgomodel.entities.Material;
import gui.sgomodel.services.EntradaService;
import gui.sgomodel.services.GrupoService;
import gui.sgomodel.services.MaterialService;
import gui.util.Alerts;
import gui.util.Constraints;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.exception.ValidationException;

public class EntradaCadastroFormController implements Initializable {

	private Entrada entity;
	private Entrada entityAnterior;
	private Material mat;
 
	/*
	 * dependencia service com metodo set
	 */
	private EntradaService service;
	private FornecedorService fornService;
	private MaterialService matService;
 
// lista da classe subject (form) - guarda lista de obj p/ receber e emitir o evento
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField textNumeroEnt;

	@FXML
	private DatePicker dpDataEnt;

	@FXML
	private TextField textNnfEnt;

	@FXML
	private TextField textIniciaisForn;
	
	@FXML
	private ComboBox<Fornecedor> comboBoxFornEnt;

	@FXML
	private TextField textIniciaisMat;
	
	@FXML
	private ComboBox<Material> comboBoxMatEnt;

	@FXML
	private TextField textQtdMatEnt;

	@FXML
	private TextField textVlrMatEnt;

	@FXML
	private Label labelTotVlrMatEnt;

	@FXML
	private Button btSaveEnt;

	@FXML
	private Button btCancelEnt;

	@FXML
	private Button btSaiEnt;

	@FXML
	private Button btPesqForn;
	
	@FXML
	private Button btPesqMat;
	
	@FXML
	private Label labelErrorDataEnt;

	@FXML
	private Label labelErrorNnfEnt;
	
	@FXML
	private Label labelErrorNomeMat;

	@FXML
	private Label labelErrorNomeForn;

	@FXML
	private Label labelErrorQtdMatEnt;

	@FXML
	private Label labelErrorVlrMatEnt;

 	@FXML
 	private Label labelUser;

 // auxiliar
 	public String user = "usuário";		
 	String classe = "Entrada Form";
 	String pesquisaForn = "";
 	String pesquisaMat = "";
 	String totM = "";
// auxiliares	
	double tot = 0.00;
	double totAnt = 0.00;
	int flagN = 0;
	int flagD = 0;
	int flagAvisoII = 0;
 	int prz = 0;
	private ObservableList<Fornecedor> obsListForn;
	private ObservableList<Material> obsListMat;
	public static Date  data1oOs = null;
	public Date  data1oBal = null;
	public String tipoEnt = null;

	public void setPesquisa(String nome) {
		this.pesquisaMat = nome;
	}
	
	public void setObjects(Entrada entity, Material mat) {
		this.entity = entity;
		this.mat = mat;
	}
	
	public void setServices(EntradaService service, FornecedorService fornService, MaterialService matService) {
		this.service = service;
		this.fornService = fornService;
		this.matService = matService;
	}

//  * o controlador tem uma lista de eventos q permite distribuiééo via metodo abaixo
// * recebe o evento e inscreve na lista
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	/*
	 * vamos instanciar um forn e salvar no bco de dados meu obj entity (lé em cima)
	 * vai receber uma chamada do getformdata metodo q busca dados do formulario
	 * convertidos getForm (string p/ int ou string) pegou no formulario e retornou
	 * (convertido) p/ jogar na variavel entity chamo o service na rotina saveupdate
	 * e mando entity vamos tst entity e service = null -> néo foi injetado para
	 * fechar a janela, pego a referencia para janela atual (event) e close
	 * DataChangeListner classe subjetc - q emite o evento q muda dados, vai guardar
	 * uma lista qdo ela salvar obj com sucesso, é sé notificar (juntar) recebe lé
	 * no listController
	 */
	@FXML
	public void onBtSaveEntAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula");
		}
		if (service == null) {
			throw new IllegalStateException("Serviço nulo");
		}
		try {
			int sai = 0;
			if (entity.getNumeroEnt() != null) {
				sai = 1;
			}
			entity = getFormData();
			acertaMaterial();
			classe = "Entrada Form ";
 			if (flagN == 0) {
 				matService.saveOrUpdate(mat);
 				entity.setMat(mat);
				service.saveOrUpdate(entity);
				EntradaCreate.compromissoCreate(entity, tipoEnt, data1oOs, data1oBal);
				if (flagAvisoII == 0) {
					flagAvisoII = 1;
				}
			}
 			entity = new Entrada();
 			notifyDataChangeListerners();
			updateFormData();
 			if (sai == 1) {
 				Utils.currentStage(event).close();
 			}
		} catch (ValidationException e) {
			setErrorMessages(e.getErros());
		} catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		} catch (ParseException p) {
			p.printStackTrace();
		} 
	}

	private void acertaMaterial() {
		classe = "Material Ent Form ";
		if (entity.getValorMatEnt() >= mat.getVendaMat()) {
			Optional<ButtonType> result = Alerts.showConfirmation("Atenção!!!", "Custo!!! verifique o valor de venda");
			if (result.get() == ButtonType.CANCEL) {
				flagN = 1;
			}	else {
				mat.setPrecoMat(entity.getValorMatEnt());
			}
		}
		if (entityAnterior != null) {
			if (mat.getCodigoMat().equals(entityAnterior.getMat().getCodigoMat())) {
				mat.setEntradaMat(entityAnterior.getQuantidadeMatEnt() * -1);
			}	
		} 
		mat.setEntradaMat(entity.getQuantidadeMatEnt());
		mat.getSaldoMat();
		if(mat.getSaldoMat() < 0) {
			Alerts.showAlert("Erro!!!", "saldo negativo - estornado ", mat.getNomeMat(), AlertType.ERROR);
			mat.setEntradaMat(entity.getQuantidadeMatEnt() + -1);
			flagN = 1;
		}
	}	
	
// *   um for p/ cada listener da lista, eu aciono o metodo onData no DataChangListner...   
	private void notifyDataChangeListerners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	/*
	 * criamos um obj vazio (obj), chamo codigo (em string) e transformamos em int
	 * (la no util) se codigo for nulo insere, se néo for atz tb verificamos se cpos
	 * obrigatérios estéo preenchidos, para informar erro(s) para cpos string néo
	 * precisa tryParse
	 */
	private Entrada getFormData() throws ParseException {
		ValidationException exception = new ValidationException("Validation exception");
		Entrada obj = new Entrada();
		// instanciando uma exceééo, mas néo lanéado - validation exc....
// set CODIGO c/ utils p/ transf string em int \\ ou null		
		obj.setNumeroEnt(Utils.tryParseToInt(textNumeroEnt.getText()));
// tst name (trim elimina branco no principio ou final
// lanéa Erros - nome do cpo e msg de erro

		if (dpDataEnt.getValue() != null) {
			Instant instant = Instant.from(dpDataEnt.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataEnt(Date.from(instant));
		}
		if (dpDataEnt.getValue() == null) {
			exception.addErros("data", "Data é obrigatória");
		}
		
		obj.setNnfEnt(Utils.tryParseToInt(textNnfEnt.getText()));
		if (textNnfEnt.getText() == null || textNnfEnt.getText().trim().contentEquals("")) {
			exception.addErros("nnf", "Nota fiscal é obrigatória");
		} else {
			entity.setNnfEnt(obj.getNnfEnt());
		}

		obj.setForn(comboBoxFornEnt.getValue());
		obj.setNomeFornEnt(comboBoxFornEnt.getValue().getRazaoSocial());
		if (obj.getNomeFornEnt() == null) {
			exception.addErros("forn", "Fornecedor é obrigatório");			
		}
		
		mat = (comboBoxMatEnt.getValue());
		obj.setMat(comboBoxMatEnt.getValue());
		obj.setNomeMatEnt(comboBoxMatEnt.getValue().getNomeMat());
		
		if (textVlrMatEnt.getText() == null || 
				textVlrMatEnt.getText() == "0.0" ||
				textVlrMatEnt.getText().trim().contentEquals("")) {
			exception.addErros("vlr", "Preço é obrigatório");
		} else {
			textVlrMatEnt.getText().replace(".", "");
			obj.setValorMatEnt(Utils.formatDecimalIn(textVlrMatEnt.getText()));
		}
		
		if (obj.getValorMatEnt() == 0.00) {
			exception.addErros("vlr", "Preço é obrigatório");			
		} else {
			mat.setPrecoMat(obj.getValorMatEnt());
		}	

		if (textQtdMatEnt.getText() == null || textQtdMatEnt.getText().trim().contentEquals("")) {
			exception.addErros("qtd", "Qtd é obrigatória");
		} else {
			textQtdMatEnt.getText().replace(".", "");
			obj.setQuantidadeMatEnt(Utils.formatDecimalIn(textQtdMatEnt.getText())); 	 		
		}
		
		if (obj.getQuantidadeMatEnt() == 0.00 || obj.getQuantidadeMatEnt() == null) {
			exception.addErros("qtd", "Qtd é obrigatória");
		} 

		tot = 0.0;
		tot = obj.getQuantidadeMatEnt() * obj.getValorMatEnt();
		if (tot != totAnt) {
			totM = Mascaras.formataValor(tot);
			totAnt = tot;
			labelTotVlrMatEnt.setText(totM);
			labelTotVlrMatEnt.viewOrderProperty();
			exception.addErros("vlr", "Conferindo total - Ok");			
		}	
  				
		// tst se houve algum (erro com size > 0)
		if (exception.getErros().size() > 0) {
			throw exception;
		}
		return obj;
	}
	
	@FXML
	public void onBtPesqFornAction(ActionEvent event) {
		classe = "Fornecedor Ent Form";
		pesquisaForn = "";
		try {
	  		pesquisaForn = textIniciaisForn.getText().toUpperCase().trim();
	  		if (pesquisaForn != "") {
	  			List<Fornecedor> listFor = fornService.findPesquisa(pesquisaForn);
				if (listFor.size() == 0) { 
					Optional<ButtonType> result = Alerts.showConfirmation("Pesquisa sem resultado ", "Deseja incluir?");
					if (result.get() == ButtonType.OK) {
				 		 Stage parentStage = Utils.currentStage(event);
		 		 		 Fornecedor obj = new Fornecedor();
		 		 		 createDialogForn(obj, "/gui/sgcp/FornecedorCadastroForm.fxml", parentStage);
		 		  	}
					listFor = fornService.findPesquisa(pesquisaForn);
			 	}
				if (listFor.size() > 0) {
					obsListForn = FXCollections.observableArrayList(listFor);
					comboBoxFornEnt.setItems(obsListForn);
					notifyDataChangeListerners();
					updateFormData();
				}	
	  		}	
		} 
		catch (ParseException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
		catch (DbException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	} 	

	@FXML
	public void onBtPesqMatAction(ActionEvent event) {
		classe = "Material Ent Form";
		try {
			pesquisaMat = "";
	  		pesquisaMat = textIniciaisMat.getText().toUpperCase().trim();
	  		if (pesquisaMat != "") {
	  			List<Material> listMat = matService.findPesquisa(pesquisaMat);
				if (listMat.size() == 0) { 
					Optional<ButtonType> result = Alerts.showConfirmation("Pesquisa sem resultado ", "Deseja incluir?");
					if (result.get() == ButtonType.OK) {
				 		 Stage parentStage = Utils.currentStage(event);
		 		 		 Material mat = new Material();
		 		 		 createDialogMat(mat, "/gui/sgo/MaterialCadastroForm.fxml", parentStage);
		 		  	}
					listMat = matService.findPesquisa(pesquisaMat);
			 	}
				if(listMat.size() > 0) {
					obsListMat = FXCollections.observableArrayList(listMat);
					comboBoxMatEnt.setItems(obsListMat);
					notifyDataChangeListerners();
					updateFormData();
				}	
	  		}	
		} catch (ParseException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
		catch (DbException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	} 	

	// msm processo save p/ fechar
	@FXML
	public void onBtCancelEntAction(ActionEvent event) {
		if (flagAvisoII == 1) {
			Alerts.showAlert("Atenção", "Edite seu Contas a Pagar", "Para conferir: Tipo de Consumo, "
					+ " data de vencimento e parcela(s)", AlertType.WARNING);
			flagAvisoII += 1;
		}
		Utils.currentStage(event).close();
	}

	@FXML
	public void onBtSaiEntAction(ActionEvent event) {
		if (flagAvisoII == 1) {
			Alerts.showAlert("Atenção", "Verifique Contas a Pagar", "Confera: Tipo de Consumo, "
					+ " data de vencimento e parcela(s)", AlertType.WARNING);
			flagAvisoII += 1;;
		}
		Utils.currentStage(event).close();
	}

	/*
	 * o contrainsts (confere)	 impede alfa em cpo numerico e delimita tamanho
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setTextFieldInteger(textNumeroEnt);
		Utils.formatDatePicker(dpDataEnt, "dd/MM/yyyy");
		Constraints.setTextFieldInteger(textNnfEnt);
 		Constraints.setTextFieldMaxLength(textNnfEnt, 6);
 		Constraints.setTextFieldMaxLength(textIniciaisForn, 7);
 		Constraints.setTextFieldMaxLength(textIniciaisMat, 7);
		initializeComboBoxFornEnt();
		initializeComboBoxMatEnt();
	}

	private void initializeComboBoxFornEnt() {
		Callback<ListView<Fornecedor>, ListCell<Fornecedor>> factory = lv -> new ListCell<Fornecedor>() {
			@Override
			protected void updateItem(Fornecedor item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getRazaoSocial());
			}
		};

		comboBoxFornEnt.setCellFactory(factory);
		comboBoxFornEnt.setButtonCell(factory.call(null));
	}

	private void initializeComboBoxMatEnt() {
		Callback<ListView<Material>, ListCell<Material>> factory = lv -> new ListCell<Material>() {
			@Override
			protected void updateItem(Material item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeMat() + 
						(String.format(" - R$%.2f", item.getVendaMat())));
			}
		};

		comboBoxMatEnt.setCellFactory(factory);
		comboBoxMatEnt.setButtonCell(factory.call(null));
	}

	/*
	 * transforma string da tela p/ o tipo no bco de dados
	 */
	public void updateFormData() throws ParseException {
		if (entity == null) {
			throw new IllegalStateException("Entidade esta nula");
		}
		
		labelUser.setText(user);
		labelUser.viewOrderProperty();
//  string value of p/ casting int p/ string 		
		textNumeroEnt.setText(String.valueOf(entity.getNumeroEnt()));
		if (entity.getNumeroEnt() != null) {
			entityAnterior = entity;
		}

		if (entity.getDataEnt() == null) {
			entity.setDataEnt(new Date());
		}
		
		if (entity.getDataEnt() != null) {
			dpDataEnt.setValue(LocalDate.ofInstant(entity.getDataEnt().toInstant(), ZoneId.systemDefault()));
		}

		textNnfEnt.setText(String.valueOf(entity.getNnfEnt()));
		
		// se for uma inclusao, vai posicionar no 1o registro (First)
		if (entity.getForn() == null) {
			comboBoxFornEnt.getSelectionModel().selectFirst();
		} else {
			comboBoxFornEnt.setValue(entity.getForn());
		}

		if (entity.getMat() == null) {
			comboBoxMatEnt.getSelectionModel().selectFirst();
		} else {
			comboBoxMatEnt.setValue(entity.getMat());
		}
		
		if (entity.getQuantidadeMatEnt() == null) {
			entity.setQuantidadeMatEnt(0.00);
		}
		if (entity.getQuantidadeMatEnt() != null) {
			String qtd = Mascaras.formataValor(entity.getQuantidadeMatEnt());
			textQtdMatEnt.setText(qtd);
		}

		if (entity.getValorMatEnt() == null) {
			entity.setValorMatEnt(0.00);
		}
		if (entity.getValorMatEnt() != null) {
			String vlr = Mascaras.formataValor(entity.getValorMatEnt());
			textVlrMatEnt.setText(vlr);
		}

		totAnt = entity.getQuantidadeMatEnt() * entity.getValorMatEnt();

		totM = Mascaras.formataValor(totAnt);
		labelTotVlrMatEnt.setText(totM);
		labelTotVlrMatEnt.viewOrderProperty();
		labelErrorVlrMatEnt.setText("");
		labelErrorVlrMatEnt.viewOrderProperty();
		textIniciaisForn.setText(pesquisaForn);
		textIniciaisMat.setText(pesquisaMat);
	}

//	carrega dados do bco cargo dentro obslist via
	public void loadAssociatedObjects() {
		if (fornService == null) {
			throw new IllegalStateException("FornecedorServiço esta nulo");
		}
		if (matService == null) {
			throw new IllegalStateException("MaterialServiço esta nulo");
		}
 // buscando (carregando) os dados do bco de dados		
		List<Fornecedor> listForn = fornService.findAll();
		listForn.removeIf(x -> x.getRazaoSocial().equals("Adiantamento"));
		List<Material> listMat = matService.findAll();
		
// transf p/ obslist
		obsListForn = FXCollections.observableArrayList(listForn);
		comboBoxFornEnt.setItems(obsListForn);
		obsListMat = FXCollections.observableArrayList(listMat);
		comboBoxMatEnt.setItems(obsListMat);
	}

// mandando a msg de erro para o labelErro correspondente 	
	private void setErrorMessages(Map<String, String> erros) {
		Set<String> fields = erros.keySet();
		labelErrorNnfEnt.setText((fields.contains("nnf") ? erros.get("nnf") : ""));
		labelErrorDataEnt.setText((fields.contains("data") ? erros.get("data") : ""));
		labelErrorQtdMatEnt.setText((fields.contains("qtd") ? erros.get("qtd") : ""));
		labelErrorVlrMatEnt.setText((fields.contains("vlr") ? erros.get("vlr") : ""));
	}
	
	private synchronized void createDialogForn(Fornecedor obj, String absoluteName, Stage parentStage) {
		try {
			classe = "Fornecedor Ent Form";
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			FornecedorCadastroFormController controller = loader.getController();
			controller.user = user;
			controller.setFornecedor(obj);
			controller.setFornecedorService(new FornecedorService());
			controller.updateFormData();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Digite Fornecedor                                             ");
 			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		}
		catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", classe + "Erro carregando tela", e.getMessage(), AlertType.ERROR);
		}
		catch (ParseException p) {
			p.printStackTrace();
		}
 	}

	private synchronized void createDialogMat(Material obj, String absoluteName, Stage parentStage) {
		try {
			classe = "Material Ent Form ";
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			MaterialCadastroFormController controller = loader.getController();
			controller.user = user;
			controller.setMaterial(obj);
			controller.setMaterialService(new MaterialService());
			controller.setGrupoService(new GrupoService());
			controller.loadAssociatedObjects();
			controller.updateFormData();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Digite Material                                             ");
 			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Erro carregando tela" + classe, e.getMessage(), AlertType.ERROR);
		}
 	} 	
}
