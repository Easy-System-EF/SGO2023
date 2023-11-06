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

public class CopiaSgoFormController implements Initializable {
	@FXML
	private Button btOK;

	@FXML
	private ComboBox<Unidade> comboUnid;

	private ObservableList<Unidade> obsListUnid;

	String file = "";
	String unid = null;
	String path = unid +"\\ARQS\\SGO\\" + file;

 	public void recebeUnidAction(ActionEvent event) {
 		try {
			unid = comboUnid.getValue().getLetraUnid();
			CopiaSgoController.unid = unid;
			String pathA = unid + ":\\ARQS";
			@SuppressWarnings("unused")
			String pathB = unid + ":\\ARQS\\Backup";
			@SuppressWarnings("unused")
			String pathC = unid + ":\\ARQS\\Backup\\SGO";
			@SuppressWarnings("unused")
			String pathD = unid + ":\\ARQS\\Backup\\SGOCP";

			File pathU = new File(unid + ":\\");
			File unidade[] = pathU.listFiles(File::isFile);
	 		if (unidade == null) {
				Alerts.showAlert(null, "unidade inválida " , "unidade: " + unid, AlertType.ERROR);				
				CopiaSgoController.unid = null;
				unid = null;
	 		} else {	
//	 		} else {
//	 			File path = new File(unid + ":\\ARQS\\Backup\\SGO");
//	 			File[] folder = path.listFiles(File::isDirectory);
//	 			if (folder == null) {
//	 				Alerts.showAlert(null, "local inválido " , "unidade: " + unid, AlertType.ERROR);				
//	 				CopiaSgoController.unid = null;
//	 				unid = null;
//	 			}
//	 		}	
//	 		if (unid != null) {
 				@SuppressWarnings("unused")
 				boolean exist = false;
 				switch (unid) {
					case "A": exist = new File (pathA).mkdir();
					break;
 					case "B": exist = new File (pathA).mkdir();
 					break;
 					case "C": exist = new File (pathA).mkdir();
 					break;
 					case "D": exist = new File (pathA).mkdir();
 					break;
 				}
 			}	
			Utils.currentStage(event).close();
		} catch (NullPointerException e) {
			Alerts.showAlert(null, "unidade inválida - BackUpForm " , "unidade: " + unid, AlertType.ERROR);
		}
 	}	
 	
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}

	public void loadAssociatedObjects() {
		UnidadeService unidService = new UnidadeService();
//		List<String> list = Arrays.asList("C", "E", "F", "G", "H", "I");
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
