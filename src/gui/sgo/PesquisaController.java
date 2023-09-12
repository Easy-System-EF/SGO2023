package gui.sgo;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listerneres.DataChangeListener;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class PesquisaController implements Initializable {

 	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField  textPesquisa; 
	
  	@FXML
	private Button btOk;
	
// auxiliares	
	String pesquisa = "";
	String pesq = "";
	static String pai = "";
	
	public static void setPai(String pesquisa) {
		pai = pesquisa;
	}
	
//  * o controlador tem uma lista de eventos q permite distribui��o via metodo abaixo
// * recebe o evento e inscreve na lista
 	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtOkAction(ActionEvent event) {
		try {
			pesquisa = getFormDataPesq();
			if (pai == "ReceberListPago") {
				ReceberConsultaListPagoController.pesquisa = pesquisa;
			}	
   	    	notifyDataChangeListerners();
	    	Utils.currentStage(event).close();
		}
		catch (DbException e) {
			e.printStackTrace();
		}
	}

// *   um for p/ cada listener da lista, eu aciono o metodo onData no DataChangListner...   
	private void notifyDataChangeListerners() {
		for (DataChangeListener listener: dataChangeListeners) {
			listener.onDataChanged();
		}	
	}

	public String getFormDataPesq() {
		pesq = textPesquisa.getText().toUpperCase();
		pesquisa = pesq ;
		return pesquisa;
	}
	
/*
 * o contrainsts (confere) impede alfa em cpo numerico e delimita tamanho 
 */
  	@Override
	public void initialize(URL url, ResourceBundle rb) {
 		Constraints.setTextFieldMaxLength(textPesquisa, 4);
    	}

 	public void updateFormData() {
		textPesquisa.setText(pesquisa);
     } 	
}	