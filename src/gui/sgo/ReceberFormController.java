package gui.sgo;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import gui.sgomodel.entities.Receber;
import gui.sgomodel.services.ReceberService;
import gui.util.Alerts;
import gui.util.Mascaras;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.exception.ValidationException;
 
public class ReceberFormController implements Initializable {

//	lista da classe subject - armazena os dados a serem atz no formulario 
//	recebe e emite eventos
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

   	@FXML
	private TextField textOsRec;

	@FXML
	private DatePicker dpDataOsRec;
 
   	@FXML
	private TextField textClienteRec;

  	@FXML
	private TextField textPlacaRec;

  	@FXML
	private TextField textParcelaRec;

	@FXML
	private DatePicker dpDataVencimentoRec;
 
 	@FXML
	private TextField textValorRec; 

 	@FXML
	private TextField textJurosRec; 

 	@FXML
	private TextField textDescontoRec; 

 	@FXML
	private Label labelTotalRec; 

 	@FXML
	private TextField textValorPagoRec; 

	@FXML
	private DatePicker dpDataPagamentoRec;

	@FXML
	private Label labelErrorDataPagamento;

	@FXML
	private Label labelErrorValor;

	@FXML
	private Button btOk;

	@FXML
	private Button btCancel;

	SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
	String classe = "receber Form ";
	 	
 	Receber receber = new Receber();
   
//	carrega a lista de dados p/ mostrar comboBox
	@SuppressWarnings("unused")
	private ObservableList<Receber> obsList;
  
//	carrega os dados do formulario	
	private Receber entity;
	private Receber parAnterior;
		
//	carrega dados do banco na cri��o do formulario - inje��o de dependencia
 	private ReceberService service;
	  
	public void setReceber(Receber entity) {
		this.entity = entity;
	}
  	
//	busca os dados no bco de dados	
	public void setService (ReceberService service) {
 		this.service = service;
 	}

//	armazena dados a serem atz no bco de dados	
	public void subscribeDataChangeListener(DataChangeListener listener) {
  		dataChangeListeners.add(listener);
	}
 	
	@FXML
	public void onBtOkAction(ActionEvent event) throws ParseException {
		if (entity == null) {
			throw new IllegalStateException("Entidade esta nula");
		}
 		if (service == null) {
			throw new IllegalStateException("Serviço esta nulo");
		} 
		try { 	entity = getFormData();
  				service.insert(entity);
 				notifyDataChangeListeners();
				Utils.currentStage(event).close();
   				
 		} catch (ValidationException e) {
			setErrorMessages(e.getErros());
		} catch (DbException e) {
			Alerts.showAlert("Erro salvando classe objeto ", null, e.getMessage(), AlertType.ERROR);
		}
 	}

//	p/ cada listener da lista, eu aciono o metodo onData...	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

 // carrega o obj com os dados do formulario retornando	obj
	 private Receber getFormData() throws ParseException {
			Receber obj = new Receber();
			obj = entity;
 			ValidationException exception = new ValidationException("Validation error");
		
			Instant instant = Instant.from(dpDataPagamentoRec.getValue().atStartOfDay(ZoneId.systemDefault()));
// 			instant = Instant.from(dpDataPagamentoRec.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataPagamentoRec(Date.from(instant));
			if (dpDataPagamentoRec.getValue() == null) { 
				exception.addErros("dataPagamento", "data é obrigatória");
 			}
			if (obj.getDataPagamentoRec().before(obj.getDataVencimentoRec())) { 
				exception.addErros("dataPagamento", "data menor que vencimento");
 			}
			
			textValorRec.getText().replace(".", "");
			textValorPagoRec.getText().replace(".", "");
			textJurosRec.getText().replace(".", "");
			textDescontoRec.getText().replace(".", "");
 			
 			if (textJurosRec.getText() == null || textJurosRec.getText().trim().contentEquals("")) {
 				obj.setJurosRec(0.00);
 			} else {
 				obj.setJurosRec(Utils.formatDecimalIn(textJurosRec.getText()));
 			}

 			if (textDescontoRec.getText() == null || textDescontoRec.getText().trim().contentEquals("")) {
 				obj.setDescontoRec(0.00);
 			} else {
 				obj.setDescontoRec(Utils.formatDecimalIn(textDescontoRec.getText()));
 			}
 			
 			double tot = 0.00;
 			tot = (obj.getValorRec() + obj.getJurosRec()) - obj.getDescontoRec();
 			String totM = Mascaras.formataValor(tot);
 			labelTotalRec.setText(totM);
 			labelTotalRec.viewOrderProperty();
 			obj.setTotalRec(tot);
 			
 			if (textValorPagoRec.getText() == null || textValorPagoRec.getText().trim().contentEquals("")) {
 				obj.setValorPagoRec(0.00);
 			} else {
 				obj.setValorPagoRec(Utils.formatDecimalIn(textValorPagoRec.getText()));
 			}
 			if (obj.getValorPagoRec() > 0) {
				Optional<ButtonType> result = Alerts.showConfirmation("Conferindo ", "total");
				if (result.get() != ButtonType.OK) {
					obj.setValorPagoRec(0.00);
				}	
 			}	
 			if (obj.getValorPagoRec() < obj.getTotalRec()) {
				exception.addErros("valor", "Erro!!! valor menor que total");
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
		initializeNodes();
 	}
 	
	private void initializeNodes() {
 		Utils.formatDatePicker(dpDataOsRec, "dd/MM/yyyy");
 		Utils.formatDatePicker(dpDataVencimentoRec, "dd/MM/yyyy");
 		Utils.formatDatePicker(dpDataPagamentoRec, "dd/MM/yyyy");
 	}

 //  dados do obj p/ preencher o formulario		 
	public void updateFormData() throws ParseException {
		if (entity == null) {
			throw new IllegalStateException("Entidade esta vazia ");
		}
  //  string value of p/ casting int p/ string
		if (parAnterior == null)
		{	parAnterior = entity;
		}
  		textOsRec.setText(String.valueOf(entity.getOsRec()));
		dpDataOsRec.setValue(LocalDate.ofInstant(entity.getDataOsRec().toInstant(), ZoneId.systemDefault()));
		dpDataVencimentoRec.setValue(LocalDate.ofInstant(entity.getDataVencimentoRec().toInstant(), ZoneId.systemDefault()));
		textClienteRec.setText(entity.getNomeClienteRec());
		textPlacaRec.setText(entity.getPlacaRec());
		textParcelaRec.setText(String.valueOf(entity.getParcelaRec()));
		entity.setDataPagamentoRec(entity.getDataPagamentoRec());
		dpDataVencimentoRec.setValue(LocalDate.ofInstant(entity.getDataVencimentoRec().toInstant(), ZoneId.systemDefault()));
		
		String vlrM = null;
   		vlrM = Mascaras.formataValor(entity.getValorRec()); 
   		textValorRec.setText(vlrM);
   		if (entity.getDataPagamentoRec() != null) {
			dpDataPagamentoRec.setValue(LocalDate.ofInstant(entity.getDataPagamentoRec().toInstant(), ZoneId.systemDefault()));
 		}
   		
   		vlrM = Mascaras.formataValor(entity.getJurosRec());
   		textJurosRec.setText(vlrM);
   		vlrM = Mascaras.formataValor(entity.getDescontoRec());
   		textDescontoRec.setText(vlrM);
   		if (entity.getTotalRec() == null) {
   			entity.setTotalRec(0.00);
   		}
   		vlrM = Mascaras.formataValor(entity.getTotalRec());
   		labelTotalRec.setText(vlrM);
		labelTotalRec.viewOrderProperty();
		if (entity.getValorPagoRec() == null) {
			entity.setValorPagoRec(0.00);
		}
		vlrM = Mascaras.formataValor(entity.getValorPagoRec());
   		textValorPagoRec.setText(vlrM);
  	}
	
  //	carrega dados do bco fornecedor dentro obslist
	public void loadAssociatedObjects() {
		if (service == null) {
			throw new IllegalStateException("Receber Serviço esta nulo");
		}
  		List<Receber> list = service.findAllAberto();
 		obsList = FXCollections.observableArrayList(list);
}
 
 // informa��o de campos labelErro(msg)	
	private void setErrorMessages(Map<String, String> erros) {
		Set<String> fields = erros.keySet();
		labelErrorDataPagamento.setText((fields.contains("dataPagamento") ? erros.get("dataPagamento") : ""));
		labelErrorValor.setText((fields.contains("valor") ? erros.get("valor") : ""));
//		if (fields.contains("confirma")) {
//			labelTotalRec.viewOrderProperty();
//		}
  	}
}
