package gui.sgo;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listerneres.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ReposicaoDadosFormController implements Initializable {

/*
 *  dependencia service com metodo set
 */

 	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField  textPlacaDado; 
	
	@FXML
	private TextField  textKmDado; 
	
  	@FXML
	private Button btOk;
	
	@FXML
	private Button btCancel;
	
// auxiliares	
	String classe = "ReposicaoDados";
	String placa = "";
	int km = 1;
	
//  * o controlador tem uma lista de eventos q permite distribui��o via metodo abaixo
// * recebe o evento e inscreve na lista
 	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtOkAction(ActionEvent event) {
		try {
			getFormData();
   	    	notifyDataChangeListerners();
	    	Utils.currentStage(event).close();
		}
		catch (DbException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	}

	public void getFormData() {
		placa = textPlacaDado.getText().toUpperCase();
		km = Utils.tryParseToInt(textKmDado.getText());
		ReposicaoVeiculoConsultaListController.placaDado =  placa;
		ReposicaoVeiculoConsultaListController.kmDado = km;
	}

// *   um for p/ cada listener da lista, eu aciono o metodo onData no DataChangListner...   
	private void notifyDataChangeListerners() {
		for (DataChangeListener listener: dataChangeListeners) {
			listener.onDataChanged();
		}	
	}

 	public void updateTableView() {
  		textPlacaDado.setText("placaDado");
  		textKmDado.setText(String.valueOf("999999"));
	}

  // msm processo save p/ fechar	
	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
/*
 * o contrainsts (confere) impede alfa em cpo numerico e delimita tamanho 
 */
  	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setTextFieldInteger(textKmDado);
		Constraints.setTextFieldMaxLength(textKmDado, 6);
		Constraints.setTextFieldMaxLength(textPlacaDado, 07);
    	}
}	