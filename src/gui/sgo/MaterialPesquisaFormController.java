package gui.sgo;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listerneres.DataChangeListener;
import gui.sgomodel.entities.Material;
import gui.sgomodel.services.MaterialService;
import gui.util.Alerts;
import gui.util.Constraints;
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

public class MaterialPesquisaFormController implements Initializable {

	private Material entity;
/*
 *  dependencia service com metodo set
 */
 	private MaterialService matService;

 	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
 	@FXML
 	private TextField textPesquisa;
 	
	@FXML
	private ComboBox<Material>  comboBoxMaterial; 
	
  	@FXML
	private Button btOk;
	
	@FXML
	private Button btCancel;
	
	@FXML
	private Button btPesquisa;
	
	@FXML
	private Label labelErrorComboBoxMaterial;

 	private ObservableList<Material> obsListMat;
	private Integer codigo;
	String pesquisa = "";
	String classe = "Material";
 
 	public void setMaterial(Material entity) {		
		this.entity = entity;
	}

 // 	 * metodo set /p service
 	public void setService(MaterialService matService) {
 		this.matService = matService;
	}
  	
//  * o controlador tem uma lista de eventos q permite distribui��o via metodo abaixo
// * recebe o evento e inscreve na lista
 	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
 	
	@FXML
	public void onBtPesquisaAction(ActionEvent event) {
		try {
			pesquisa = "";
	  		pesquisa = textPesquisa.getText().toUpperCase().trim();
	  		if (pesquisa != "") {
	  			List<Material> list = matService.findPesquisa(pesquisa);
				if (list.size() == 0) { 
					Alerts.showAlert("Material ", null, "Não encontrado ", AlertType.INFORMATION);
					list = matService.findAll();
			 	}
				if(list.size() > 0) {
					obsListMat = FXCollections.observableArrayList(list);
					comboBoxMaterial.setItems(obsListMat);
					comboBoxMaterial.getSelectionModel().selectFirst();
					notifyDataChangeListerners();
				}	
	  		}	
		}
		catch (DbException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	}

	@FXML
	public void onBtOkAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula");
		}
		try {
     		entity = getFormData();
     		EntradaCadastroListController.codigoMat = codigo;
   	    	notifyDataChangeListerners();
	    	Utils.currentStage(event).close();
		}
		catch (ValidationException e) {
			setErrorMessages(e.getErros());
		}
		catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	}

// *   um for p/ cada listener da lista, eu aciono o metodo onData no DataChangListner...   
	private void notifyDataChangeListerners() {
		for (DataChangeListener listener: dataChangeListeners) {
			listener.onDataChanged();
		}	
	}

	private Material getFormData() {
		Material obj = new Material();
 // instanciando uma exce��o, mas n�o lan�ado - validation exc....		
		ValidationException exception = new ValidationException("Validation exception");

 		if (comboBoxMaterial.getValue().getCodigoMat() == null) {
 		 	exception.addErros("fornecedor", "fornecedor inválido");
		} else {
			codigo = comboBoxMaterial.getValue().getCodigoMat();
			obj = comboBoxMaterial.getValue();
		}
  		if (exception.getErros().size() > 0) {
			throw exception;
		}
		return obj;
	}
	
	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
  	@Override
	public void initialize(URL url, ResourceBundle rb) {
  		Constraints.setTextFieldMaxLength(textPesquisa, 7);
		initializeComboBoxMaterial();
    	}

	private void initializeComboBoxMaterial() {
		Callback<ListView<Material>, ListCell<Material>> factory = lv -> new ListCell<Material>() {
			@Override
			protected void updateItem(Material item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeMat());
 			}
		};
		
		comboBoxMaterial.setCellFactory(factory);
		comboBoxMaterial.setButtonCell(factory.call(null));
	}		
   	
 /*
  * transforma string da tela p/ o tipo no bco de dados 
  */
 	public void updateFormData() {
 		if (entity == null)
 		{	throw new IllegalStateException("Entidade esta nula");
 		}
// se for uma inclusao, vai posicionar no 1o depto//tipo (First)	
		comboBoxMaterial.getSelectionModel().selectFirst();
     }
 	
//	carrega dados do bco  dentro obslist
	public void loadAssociatedObjects() {
// buscando (carregando) os forn q est�o no bco de dados		
		List<Material> listMat = matService.findAll();
 		obsListMat = FXCollections.observableArrayList(listMat);
		comboBoxMaterial.setItems(obsListMat);
  	}
  	
 // mandando a msg de erro para o labelErro correspondente 	
 	private void setErrorMessages(Map<String, String> erros) {
 		Set<String> fields = erros.keySet();
		labelErrorComboBoxMaterial.setText((fields.contains("fornecedor") ? erros.get("fornecedor") : ""));
  	}
}	
