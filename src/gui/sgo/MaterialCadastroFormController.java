package gui.sgo;

import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listerneres.DataChangeListener;
import gui.sgomodel.entities.Grupo;
import gui.sgomodel.entities.Material;
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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.exception.ValidationException;

public class MaterialCadastroFormController implements Initializable {

	private Material entity;
	
/*
 *  dependencia service com metodo set
 */
	private MaterialService service;
	private GrupoService gruService;

// lista da classe subject (form) - guarda lista de obj p/ receber e emitir o evento
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField textCodigoMat;
	
	@FXML
	private ComboBox<Grupo> comboBoxGrupo;
	
	@FXML
	private TextField textNomeMat;
	
 	@FXML
	private TextField textVendaMat;
	
 	@FXML
	private TextField textEstMinMat;
	
	@FXML
	private TextField textVidaKmMat;
	
	@FXML
	private TextField textVidaMesMat;
	
   	@FXML
	private Button btSaveMat;
	
	@FXML
	private Button btCancelMat;
	
	@FXML
	private Label labelErrorNomeMat;

	@FXML
	private Label labelErrorGrupoMat; 
	
	@FXML
	private Label labelErrorVendaMat; 

	private ObservableList<Grupo>  obsListGru;

	@FXML
	private Label labelUser;

	// auxiliar
	Date dataAnt = null;
	String classe = "Material ";
	public String user = "usuário";		
	
  	public void setMaterial(Material entity) {
		this.entity = entity;
	}

  	// 	 * metodo set /p service
 	public void setMaterialService(MaterialService service) {
		this.service = service;
	}
	
 	public void setGrupoService(GrupoService gruService) {
		this.gruService = gruService;
	}
	
//  * o controlador tem uma lista de eventos q permite distribui��o via metodo abaixo
// * recebe o evento e inscreve na lista
 	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

/* 
 * vamos instanciar um forn e salvar no bco de dados
 * meu obj entity (l� em cima) vai receber uma chamada do getformdata
 *  metodo q busca dados do formulario convertidos getForm (string p/ int ou string)		
 *  pegou no formulario e retornou (convertido) p/ jogar na variavel entity
 *  chamo o service na rotina saveupdate e mando entity
 *  vamos tst entity e service = null -> n�o foi injetado
 *  para fechar a janela, pego a referencia para janela atual (event) e close
 *  DataChangeListner classe subjetc - q emite o evento q muda dados, vai guardar uma lista
 *  qdo ela salvar obj com sucesso, � s� notificar (juntar)
 *  recebe l� no  listController    		 
 */
	@FXML
	public void onBtSaveMatAction(ActionEvent event) {
		if (entity == null)
		{	throw new IllegalStateException("Entidade nula");
		}
		if (service == null)
		{	throw new IllegalStateException("Serviço nulo");
		}
		try {
			int sai = 0;
			if (entity.getCodigoMat() == null) {
				sai = 1;
			}
    		 entity = getFormData();
	    	 service.saveOrUpdate(entity);
	    	 if (sai == 0) {
	    		 Utils.currentStage(event).close();
	    	 } 
    		 entity = new Material();
    		 notifyDataChangeListerners();
    		 updateFormData();
		}
		catch (ValidationException e) {
			setErrorMessages(e.getErros());
		}
		catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
		catch (ParseException p) {
			p.printStackTrace();
		}
	}

// *   um for p/ cada listener da lista, eu aciono o metodo onData no DataChangListner...   
	private void notifyDataChangeListerners() {
		for (DataChangeListener listener: dataChangeListeners) {
			listener.onDataChanged();
		}	
	}

/*
 * criamos um obj vazio (obj), chamo codigo (em string) e transformamos em int (la no util)
 * se codigo for nulo insere, se n�o for atz
 * tb verificamos se cpos obrigat�rios est�o preenchidos, para informar erro(s)
 * para cpos string n�o precisa tryParse	
 */
	private Material getFormData() throws ParseException {
//		Locale ptBR = new Locale("pt", "BR");
		Material obj = new Material();
		obj = entity;
  // instanciando uma exce��o, mas n�o lan�ado - validation exc....		
		ValidationException exception = new ValidationException("Validation exception");
// set CODIGO c/ utils p/ transf string em int \\ ou null		
		obj.setCodigoMat(Utils.tryParseToInt(textCodigoMat.getText()));
		
 		obj.setGrupo(comboBoxGrupo.getValue());
 		Grupo grupo = comboBoxGrupo.getSelectionModel().getSelectedItem();
 		obj.setGrupoMat(grupo.getCodigoGru());

// tst name (trim elimina branco no principio ou final
// lan�a Erros - nome do cpo e msg de erro
		if (textNomeMat.getText() == null || textNomeMat.getText().trim().contentEquals("")) {
			exception.addErros("nome", "Nome é obrigatório");
		} 
		obj.setNomeMat(textNomeMat.getText());
		if (textNomeMat.getText() != null) {
			if (textNomeMat.getText().length() < 3) {
				exception.addErros("nome", "Nome muito curto");
			}	
		}

		if (textEstMinMat.getText() == null || textEstMinMat.getText().trim().contentEquals("")) {
			obj.setEstMinMat(0.00);
		}

		obj.setEstMinMat(Utils.formatDecimalIn(textEstMinMat.getText().replace(".", "")));
		if (textVidaKmMat.getText() == null) {
			obj.setVidaKmMat(0);
		}

		if (obj.getCodigoMat() == null) {
			obj.setEntradaMat(0.0);
			obj.setSaidaMat(0.0);
			obj.getSaldoMat();
			obj.setPrecoMat(0.0);
			obj.getCmmMat();
		}
		
		if (textVendaMat.getText() == null || textVendaMat.getText().trim().contentEquals("")) {
			exception.addErros("venda", "Valor é obrigatório");
			obj.setVendaMat(0.0);
		}
		obj.setVendaMat(Utils.formatDecimalIn(textVendaMat.getText().replace(".", "")));
		if (obj.getVendaMat() == 0.00) {
			exception.addErros("venda", "Valor é obrigatório");
		}

		String kmM = textVidaKmMat.getText();
 		String kmsM2 = kmM.replace(".", "");
 		obj.setVidaKmMat(Utils.tryParseToInt(kmsM2));
 
		if (textVidaMesMat.getText() == null) {
			obj.setVidaMesMat(0);
		}
		obj.setVidaMesMat(Utils.tryParseToInt(textVidaMesMat.getText()));

 		if (dataAnt == null) {
 			obj.setDataCadastroMat(new Date());
 		}
 		else {
 			obj.setDataCadastroMat(dataAnt);
 		}
 		
 		obj.setPercentualMat(0.00);
 		obj.setLetraMat(' ');

 		// tst se houve algum (erro com size > 0)		
		if (exception.getErros().size() > 0)
		{	throw exception;
		}
		return obj;
	}
	
  // msm processo save p/ fechar	
	@FXML
	public void onBtCancelMatAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
/*
 * o contrainsts (confere) impede alfa em cpo numerico e delimita tamanho 
 */
  	@Override
	public void initialize(URL url, ResourceBundle rb) {
 		Constraints.setTextFieldInteger(textCodigoMat);
  		Constraints.setTextFieldMaxLength(textNomeMat, 40);
 		Constraints.setTextFieldMaxLength(textVidaMesMat, 2);
		initializeComboBoxGrupo();
	}

	private void initializeComboBoxGrupo() {
		Callback<ListView<Grupo>, ListCell<Grupo>> factory = lv -> new ListCell<Grupo>() {
			@Override
			protected void updateItem(Grupo item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeGru());
 			}
		};
		
		comboBoxGrupo.setCellFactory(factory);
		comboBoxGrupo.setButtonCell(factory.call(null));
	}		


 /*
  * transforma string da tela p/ o tipo no bco de dados 
  */
  	public void updateFormData() throws ParseException {
 		if (entity == null) {
 			throw new IllegalStateException("Entidade esta nula");
 		}
		labelUser.setText(user);

//  string value of p/ casting int p/ string 		
 		textCodigoMat.setText(String.valueOf(entity.getCodigoMat()));
// se for uma inclusao, vai posicionar no 1o depto//tipo (First)	
 		if (entity.getGrupo() == null) {
			comboBoxGrupo.getSelectionModel().selectFirst();
		} else {
 			comboBoxGrupo.setValue(entity.getGrupo());
		}

 		if (textNomeMat.getText() != null) {
 			textNomeMat.setText(entity.getNomeMat());
  		}
 			
 		if (entity.getVendaMat() == null) {
 			entity.setVendaMat(0.00);
 		}
 		
		String venda = Mascaras.formataValor(entity.getVendaMat());	
		textVendaMat.setText(venda); 
 		
		if (entity.getEstMinMat() == null) {
			entity.setEstMinMat(0.00);
		}
		String estMin = Mascaras.formataValor(entity.getEstMinMat());
		textEstMinMat.setText(estMin);
		
		
		if (entity.getVidaKmMat() == null) {
			entity.setVidaKmMat(0);
		}
		if (entity.getVidaKmMat() > 0) {
			String kmM = "";
			kmM = Mascaras.formataMillhar(entity.getVidaKmMat());
			textVidaKmMat.setText(String.valueOf(kmM));
		}
		else {
			textVidaKmMat.setText(String.valueOf(entity.getVidaKmMat()));
		}
		
		if (entity.getVidaMesMat() == null) {
			entity.setVidaMesMat(0);
		}
		textVidaMesMat.setText(String.valueOf(entity.getVidaMesMat()));

		if (entity.getDataCadastroMat() != null) {
			dataAnt = entity.getDataCadastroMat();
		}
    }

//	carrega dados do bco cargo dentro obslist via
	public void loadAssociatedObjects() {
		if (gruService == null) {
			throw new IllegalStateException("GrupoServiço esta nulo");
		}
// buscando (carregando) os forn q est�o no bco de dados		
		List<Grupo> list = gruService.findAll();
// transf p/ obslist		
		obsListGru = FXCollections.observableArrayList(list);
		comboBoxGrupo.setItems(obsListGru);
 	}
  
// mandando a msg de erro para o labelErro correspondente 	
 	private void setErrorMessages(Map<String, String> erros) {
 		Set<String> fields = erros.keySet();
  		labelErrorNomeMat.setText((fields.contains("nome") ? erros.get("nome") : ""));		
 		labelErrorGrupoMat.setText((fields.contains("grupo") ? erros.get("grupo") : ""));		
		labelErrorVendaMat.setText((fields.contains("venda") ? erros.get("venda") : ""));
   	}	
 } 
