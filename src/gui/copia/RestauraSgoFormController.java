
package gui.copia;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class RestauraSgoFormController implements Initializable {
	@FXML
	private Button btOK;

	@FXML
	private ComboBox<Unidade> comboUnid;

	private ObservableList<Unidade> obsListUnid;

	String unid = null;
	static String meioSgo = ":\\Arqs\\Backup\\SGO\\";
	public String meioSgcp= ":\\Arqs\\Backup\\SGOCP\\";

 	public void recebeUnidAction(ActionEvent event) {
 		try {
			unid = comboUnid.getValue().getLetraUnid();
			RestauraSgoController.unid = unid;
			File path = new File(unid + ":\\Arqs\\Backup\\SGO\\");
			File[] folders = path.listFiles(File::isDirectory);
			File pathP = new File(unid + ":\\Arqs\\Backup\\SGOCP\\");
			File[] foldersP = pathP.listFiles(File::isDirectory);
	 		if (folders == null) {
				Alerts.showAlert(null, "local não existe " , "caminho: " + path, AlertType.ERROR);				
				RestauraSgoController.unid = null;
				unid = null;
	 		} else {
	 			if (foldersP == null) {
	 				Alerts.showAlert(null, "local não existe " , "caminho: " + pathP, AlertType.ERROR);				
	 				RestauraSgoController.unid = null;
	 				unid = null;
	 			}	
	 		}
			Utils.currentStage(event).close();
		} catch (NullPointerException e) {
			Alerts.showAlert(null, "local inválido " , "unidade: " + unid, AlertType.ERROR);
		}
 	}	
 	
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}

	public void loadAssociatedObjects() {
		UnidadeService unidService = new UnidadeService();
		List<Unidade> listUnid = unidService.findAll(); 
		obsListUnid = FXCollections.observableArrayList(listUnid);
		comboUnid.setItems(obsListUnid);
	}
	
 	private void initializeNodes() {
		initializeComboBoxFuncionario();
 	}
 	
	private void initializeComboBoxFuncionario() {
		Callback<ListView<Unidade>, ListCell<Unidade>> factory = lv -> new ListCell<Unidade>() {
			@Override
			protected void updateItem(Unidade item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getLetraUnid());
			}
		};

		comboUnid.setCellFactory(factory);
		comboUnid.setButtonCell(factory.call(null));
	}
}
