package gui.sgo;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listerneres.DataChangeListener;
import gui.sgomodel.entities.Balcao;
import gui.sgomodel.entities.Material;
import gui.sgomodel.entities.OrcVirtual;
import gui.sgomodel.entities.Orcamento;
import gui.sgomodel.services.BalcaoService;
import gui.sgomodel.services.GrupoService;
import gui.sgomodel.services.MaterialService;
import gui.sgomodel.services.OrcVirtualService;
import gui.sgomodel.services.OrcamentoService;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.exception.ValidationException;

public class OrcVirtualCadastroFormController implements Initializable, DataChangeListener {

	private OrcVirtual entity;
	private OrcVirtual entityAnterior;
	Material mat = new Material();
 
	/*
	 * dependencia service com metodo set
	 */
	private OrcVirtualService service;
	private OrcamentoService orcService;
	private BalcaoService balService;
	private MaterialService matService;
 
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
		try {
			updateFormData();
			getFormData();
		}
		catch (ParseException p) {
			p.printStackTrace();
		}
	}

	@FXML
	private Label labelEntidade;

	@FXML
	private TextField textNumeroOrcBalVir;

	@FXML
	private TextField textPesquisa;

	@FXML
	private ComboBox<Material> comboBoxMatVir;

	@FXML
	private TextField textQtdMatVir;

	@FXML
	private Label labelPrecoMatVir;

	@FXML
	private Label labelTotalMatVir;

	@FXML
	private Button btPesquisa;
	
	@FXML
	private Button btSaveVir;
	
	@FXML
	private Button btCancelVir;

	@FXML
	private Label labelErrorMaterialVir;

	@FXML
	private Label labelErrorQtdMatVir;

	@FXML
	private Label labelErrorPrecoMatVir;
	
	@FXML
	private Label labelErrorTotalMatVir;
	
 	@FXML
 	private Label labelUser;

 // auxiliar
 	public String user = "usuário";		
 	String classe = "OrcVirtual Form ";
 	String pesquisa = "";
	Double totAnt = null;
	int flagGrava = 0;
	public static Integer numOrc = 0;
	public static Integer numBal = 0;
	@SuppressWarnings("unused")
	private double maoObra = 0;
	
	private ObservableList<Material> obsListMat;

	public void setOrcVirtual(OrcVirtual entity) {
		this.entity = entity;
	}

 	// * metodo set /p service
	public void setOrcVirtualService(OrcVirtualService service) {
		this.service = service;
	}

	public void setOrcamentoService(OrcamentoService orcService) {
		this.orcService = orcService;
	}

	public void setBalcaoService(BalcaoService balService) {
		this.balService = balService;
	}

	public void setMaterialService(MaterialService matService) {
		this.matService = matService;
	}
	
	@FXML
	public void onBtPesqMatAction(ActionEvent event) {
		classe = "Material Ent Form";
		try {
	  		pesquisa = textPesquisa.getText().toUpperCase().trim();
	  		if (pesquisa != "") {
	  			List<Material> listMat = matService.findPesquisa(pesquisa);
				if (listMat.size() == 0) { 
					pesquisa = "";
					Optional<ButtonType> result = Alerts.showConfirmation("Pesquisa sem resultado ", "Deseja incluir?");
					if (result.get() == ButtonType.OK) {
				 		 Stage parentStage = Utils.currentStage(event);
		 		 		 Material mat = new Material();
		 		 		 createDialogMat(mat, "/gui/sgo/MaterialCadastroForm.fxml", parentStage);
		 		  	}
					listMat = matService.findPesquisa(pesquisa);
			 	}
	  			obsListMat = FXCollections.observableArrayList(listMat);
	  			comboBoxMatVir.setItems(obsListMat);
	  			notifyDataChangeListerners();
	  			updateFormData();
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

	/*
	 * vamos instanciar um orc e salvar no bco de dados meu obj entity (l� em cima)
	 * vai receber uma chamada do getformdata metodo q busca dados do formulario
	 * convertidos getForm (string p/ int ou string) pegou no formulario e retornou
	 * (convertido) p/ jogar na variavel entity chamo o service na rotina saveupdate
	 * e mando entity vamos tst entity e service = null -> n�o foi injetado para
	 * fechar a janela, pego a referencia para janela atual (event) e close
	 * DataChangeListner classe subjetc - q emite o evento q muda dados, vai guardar
	 * uma lista qdo ela salvar obj com sucesso, � s� notificar (juntar) recebe l�
	 * no listController
	 */
	@FXML
	public void onBtSaveVirAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula");
		}
		if (service == null) {
			throw new IllegalStateException("Serviço nulo");
		}
		try {
			ValidationException exception = new ValidationException("Validation exception");
			int sair = 0;
			if (entity.getNumeroVir() != null) {
				sair = 1;
			}
			if (entity.getTotalMatVir() == 0.00 || entity.getTotalMatVir() == null || sair == 1) {
				entity = getFormData();
				updateMaterial();
				classe = "OrcVirtual Form ";
				if (!entity.getTotalMatVir().equals(totAnt)) {
					String vlr2 = Mascaras.formataValor(entity.getPrecoMatVir());
					labelPrecoMatVir.setText(vlr2);
					vlr2 = Mascaras.formataValor(entity.getTotalMatVir());
					labelTotalMatVir.setText(vlr2);
					labelPrecoMatVir.viewOrderProperty();
					labelTotalMatVir.viewOrderProperty();
					Optional<ButtonType> result = Alerts.showConfirmation("Confirma ", "total");
					if (result.get() == ButtonType.OK) {
						totAnt = entity.getTotalMatVir();
					}	
				}	
			}
			if (flagGrava == 0) {
				if (entity.getTotalMatVir().equals(totAnt)) {
					classe = "OrcVirtual Form ";
					service.saveOrUpdate(entity);
					if (numBal > 0) {	
						classe = "Balcao Virtual Form ";
						Balcao bal = balService.findById(numBal);
						classe = "Virtual Form ";
						bal.setTotalBal(service.findByTotalBal(numBal));
					}	
					if (numOrc > 0 && flagGrava == 0) {
						classe = "Orcamento Virtual Form ";
						Orcamento orc = orcService.findById(entity.getNumeroOrcVir());
						orc.setTotalOrc(entity.getTotalMatVir());
						orcService.saveOrUpdate(orc);
					}	
					if (sair == 1) {
						onBtCancelVirAction(event);
						Utils.currentStage(event).close();
					}	
					entity = new OrcVirtual();
					labelPrecoMatVir.setText("0,00");
					labelTotalMatVir.setText("0,00");
					labelPrecoMatVir.viewOrderProperty();
					labelTotalMatVir.viewOrderProperty();
					textPesquisa.setText("");
					totAnt = 0.00;
					notifyDataChangeListerners();
					updateFormData();
				}	
			}
			if (exception.getErros().size() > 0) {
				throw exception;
			}
		} catch (ValidationException e) {
			setErrorMessages(e.getErros());
		} catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		} catch (ParseException p) {
			p.printStackTrace();
		}
	}

	/*
	 * criamos um obj vazio (obj), chamo codigo (em string) e transformamos em int
	 * (la no util) se codigo for nulo insere, se n�o for atz tb verificamos se cpos
	 * obrigat�rios est�o preenchidos, para informar erro(s) para cpos string n�o
	 * precisa tryParse
	 */
	public OrcVirtual getFormData() throws ParseException {
		ValidationException exception = new ValidationException("Validation exception");
 		OrcVirtual obj = new OrcVirtual();
		// instanciando uma exce��o, mas n�o lan�ado - validation exc....
// set CODIGO c/ utils p/ transf string em int \\ ou null
		obj.setNumeroVir(entity.getNumeroVir());
		if (numOrc > 0) {
			obj.setNumeroOrcVir(numOrc);
			obj.setNumeroBalVir(0);
		} else {
			if (numBal > 0) {
				obj.setNumeroBalVir(numBal);
				obj.setNumeroOrcVir(0);
			}	
		}
// tst name (trim elimina branco no principio ou fKinal
// lan�a Erros - nome do cpo e msg de erro
		Material matForm = comboBoxMatVir.getValue();
		obj.setMaterial(matForm); 
		obj.setNomeMatVir(matForm.getNomeMat());
		obj.setPrecoMatVir(matForm.getVendaMat());
		obj.setCustoMatVir(matForm.getPrecoMat());
		String vlr = Mascaras.formataValor(matForm.getVendaMat());
		labelPrecoMatVir.setText(vlr);
		labelPrecoMatVir.viewOrderProperty();

		if (textQtdMatVir.getText() == null || textQtdMatVir.getText().trim().contentEquals("")) {
			exception.addErros("qtd", "Qtd é obrigatória");
		}	else { 
			if (textQtdMatVir.getText() != null) {
				obj.setQuantidadeMatVir(Utils.formatDecimalIn(textQtdMatVir.getText().replace(".", "")));
			}	
		}
		if (obj.getQuantidadeMatVir() == null) {
			exception.addErros("qtd", "Qtd é obrigatória");			
		}

		List<OrcVirtual> vir = service.findByBalcao(numBal);
		for (OrcVirtual v : vir) {
			if (v.getNumeroOrcVir() == obj.getNumeroOrcVir()) {
				if (v.getMaterial().getCodigoMat().equals(obj.getMaterial().getCodigoMat())) {
					if (obj.getNumeroVir() != v.getNumeroVir()) {
						exception.addErros("material", "Erro!!! já existe material");
					}	
				}
			}	
		}
				
		if (obj.getQuantidadeMatVir() == 0) {
			exception.addErros("qtd", "Quantidade não pode ser 0");
		}	 
  				
// tst se houve algum (erro com size > 0)
		if (exception.getErros().size() > 0) {
			throw exception;
		}
		return obj;
	}

	private void updateMaterial() {
		try {
			ValidationException exception = new ValidationException("Validation exception");
			Material mat3 = new Material();
			classe = "OS Form virtual ";
			List<OrcVirtual> listVir = service.findByOrcto(entity.getNumeroBalVir());
			for (OrcVirtual ov : listVir) {
				if (ov.getNumeroOrcVir().equals(entity.getNumeroBalVir())) {
					mat3 = matService.findById(ov.getMaterial().getCodigoMat());
					if (mat3.getGrupo().getNomeGru().contains("Serviço")) { 
						if (mat3.getSaldoMat() < ov.getQuantidadeMatVir()) {
							mat3.entraSaldo(ov.getQuantidadeMatVir());
						}
					}	
	// serviço não tem estoque - entra no momento da solicitação
					if (mat3.getGrupo().getNomeGru().contains("Mão de obra")) { 
						if (mat3.getSaldoMat() < ov.getQuantidadeMatVir()) {
							mat3.entraSaldo(ov.getQuantidadeMatVir());
						}		
					}	
					if (entityAnterior.getNumeroVir() != null) {
						if (mat3.getCodigoMat() == entityAnterior.getMaterial().getCodigoMat()) {
							mat3.setSaidaCmmMat(-1 * entityAnterior.getQuantidadeMatVir());
							mat3.entraSaldo(entityAnterior.getQuantidadeMatVir());
						}
					}	
					matService.saveOrUpdate(mat3);
					ov.setMaterial(mat3);
				}
				if (exception.getErros().size() > 0) {
					throw exception;
				}
			}
		}		
		catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	}	
				
	// msm processo save p/ fechar
	@FXML
	public void onBtCancelVirAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	/*
	 * o contrainsts (confere) impede alfa em cpo numerico e delimita tamanho
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setTextFieldMaxLength(textPesquisa, 7);
		initializeComboBoxMatVir();
	}

	private void initializeComboBoxMatVir() {
		Callback<ListView<Material>, ListCell<Material>> factory = lv -> new ListCell<Material>() {
			@Override
			protected void updateItem(Material item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeMat());
			}
		};

		comboBoxMatVir.setCellFactory(factory);
		comboBoxMatVir.setButtonCell(factory.call(null));
	}

	/*
	 * transforma string da tela p/ o tipo no bco de dados
	 */
	public void updateFormData() throws ParseException {
		if (entity == null) {
			throw new IllegalStateException("Entidade esta nula");
		}
		entityAnterior = entity; 
		labelUser.setText(user);
//  string value of p/ casting int p/ string
		if (numOrc > 0) {
			textNumeroOrcBalVir.setText(String.valueOf(numOrc));
			labelEntidade.setText("Orçamento");
		}	

		if (numBal > 0) { 
			textNumeroOrcBalVir.setText(String.valueOf(numBal));
			labelEntidade.setText("Balcão");
		}	
		
// se for uma inclusao, vai posicionar no 1o registro (First)
		if (entity.getMaterial() == null) {
			comboBoxMatVir.getSelectionModel().selectFirst();
		} else {
			comboBoxMatVir.setValue(entity.getMaterial());
		}
		
		if (entity.getQuantidadeMatVir() != null) {
			String qtd = Mascaras.formataValor(entity.getQuantidadeMatVir());
			textQtdMatVir.setText(qtd);
		}	else {
				entity.setQuantidadeMatVir(0.0);
				textQtdMatVir.setText(String.valueOf(entity.getQuantidadeMatVir()));
			}
		
		if (entity.getPrecoMatVir() == null) {
			entity.setPrecoMatVir(0.00);
		}

		if (entity.getNumeroOrcVir() != null) {
			String vlr = Mascaras.formataValor(entity.getMaterial().getVendaMat());
			labelPrecoMatVir.setText(vlr);
			labelPrecoMatVir.viewOrderProperty();
		}	
		if (entity.getNumeroBalVir() != null) {
			String vlr = Mascaras.formataValor(entity.getMaterial().getVendaMat());
			labelPrecoMatVir.setText(vlr);
			labelPrecoMatVir.viewOrderProperty();
		}	
		
		if (entity.getTotalMatVir() == null) {
			entity.setTotalMatVir(0.00);
			totAnt = entity.getTotalMatVir();
		}	
		
		if (totAnt == null) {
			totAnt = 0.00;
		}
		String vlr2 = Mascaras.formataValor(totAnt);
		labelTotalMatVir.setText(vlr2);
		labelTotalMatVir.viewOrderProperty();
		textPesquisa.setText(pesquisa);
	}

//	carrega dados do bco cargo dentro obslist via
	public void loadAssociatedObjects() {
		if (numOrc > 0 && orcService == null) {
			throw new IllegalStateException("OrçamentoServiço esta nulo");
		} else {
			if (numBal > 0 && balService == null) {
				throw new IllegalStateException("Balcão Serviço esta nulo");
			}
		}
		if (service == null) {
			throw new IllegalStateException("VirtualServiço esta nulo");
		}
		labelUser.setText(user);
 // buscando (carregando) os dados do bco de dados	
		List<Material> listMat = matService.findAll();
// transf p/ obslist
		obsListMat = FXCollections.observableArrayList(listMat);
		comboBoxMatVir.setItems(obsListMat);
	}

// mandando a msg de erro para o labelErro correspondente 	
	private void setErrorMessages(Map<String, String> erros) {
		Set<String> fields = erros.keySet();
		labelErrorMaterialVir.setText((fields.contains("material") ? erros.get("material") : ""));
		labelErrorQtdMatVir.setText((fields.contains("qtd") ? erros.get("qtd") : ""));
		labelErrorPrecoMatVir.setText((fields.contains("preco") ? erros.get("preco") : ""));
		labelErrorTotalMatVir.setText((fields.contains("total") ? erros.get("total") : ""));
	}

	private void createDialogMat(Material obj, String absoluteName, Stage parentStage) {
		try {
			classe = "Material ";
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
