package gui.copia;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import application.MainSgo;
import gui.listerneres.DataChangeListener;
import gui.sgcpmodel.entities.Compromisso;
import gui.sgcpmodel.entities.Fornecedor;
import gui.sgcpmodel.entities.Parcela;
import gui.sgcpmodel.entities.TipoConsumo;
import gui.sgcpmodel.entities.consulta.ParPeriodo;
import gui.sgcpmodel.services.CompromissoService;
import gui.sgcpmodel.services.FornecedorService;
import gui.sgcpmodel.services.ParPeriodoService;
import gui.sgcpmodel.services.ParcelaService;
import gui.sgcpmodel.services.TipoConsumoService;
import gui.sgomodel.entities.Adiantamento;
import gui.sgomodel.entities.Anos;
import gui.sgomodel.entities.Balcao;
import gui.sgomodel.entities.Cargo;
import gui.sgomodel.entities.Cliente;
import gui.sgomodel.entities.Empresa;
import gui.sgomodel.entities.Entrada;
import gui.sgomodel.entities.Funcionario;
import gui.sgomodel.entities.Grupo;
import gui.sgomodel.entities.Login;
import gui.sgomodel.entities.Material;
import gui.sgomodel.entities.Meses;
import gui.sgomodel.entities.NotaFiscal;
import gui.sgomodel.entities.OrcVirtual;
import gui.sgomodel.entities.Orcamento;
import gui.sgomodel.entities.OrdemServico;
import gui.sgomodel.entities.Receber;
import gui.sgomodel.entities.ReposicaoVeiculo;
import gui.sgomodel.entities.Situacao;
import gui.sgomodel.entities.Veiculo;
import gui.sgomodel.services.AdiantamentoService;
import gui.sgomodel.services.AnosService;
import gui.sgomodel.services.BalcaoService;
import gui.sgomodel.services.CargoService;
import gui.sgomodel.services.ClienteService;
import gui.sgomodel.services.EmpresaService;
import gui.sgomodel.services.EntradaService;
import gui.sgomodel.services.FuncionarioService;
import gui.sgomodel.services.GrupoService;
import gui.sgomodel.services.LoginService;
import gui.sgomodel.services.MaterialService;
import gui.sgomodel.services.MesesService;
import gui.sgomodel.services.NotaFiscalService;
import gui.sgomodel.services.OrcVirtualService;
import gui.sgomodel.services.OrcamentoService;
import gui.sgomodel.services.OrdemServicoService;
import gui.sgomodel.services.ReceberService;
import gui.sgomodel.services.ReposicaoVeiculoService;
import gui.sgomodel.services.SituacaoService;
import gui.sgomodel.services.VeiculoService;
import gui.util.Alerts;
import gui.util.Cryptograf;
import gui.util.DataStatic;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CopiaSgoController implements Initializable, DataChangeListener {

	@FXML
	private VBox vBoxBackUp;

	@FXML
	private Button buttonOk;

 	@FXML
	private Label labelFile;

	@FXML
	private Label labelUser;

	@FXML
	private Label labelCount;

	@FXML
	private TableView<Copia> tableViewBackUp;

 	@FXML
 	private TableColumn<Copia, Date> tableColumnDateIBackUp;

 	@FXML
 	private TableColumn<Copia, Date> tableColumnDateFBackUp;

 	@FXML
 	private TableColumn<Copia, String> tableColumnUserBackUp;

 	private ObservableList<Copia> obsList;

	int count = 0;
	String arq = "";
	String crip = "";
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
 	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
 	
 	private CopiaService service;
 	private Copia entity;
 	private Copia entityDel;
 	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

 	public void setBackUpService(CopiaService service) {
 		this.service = service;
 	}
 	
 	public void setEntity(Copia entity) {
 		this.entity = entity;
 	}
	int grw = 0;

 	@FXML
  	public void onBtOkAdiAction(ActionEvent event) {
 		 Stage parentStage = Utils.currentStage(event);
// instanciando novo obj depto e injetando via
 		 createDialogForm("/gui/copia/CopiaForm.fxml", parentStage);
 		 updateTableView();
 		 executaBack();
   	}
 	
 	public void executaBack() {
 		if (unid != null) { 
 //			Alerts.showAlert("Atenção", "isoo pode demorar um pouco", null, AlertType.WARNING);
 			backUp(); 			
 			adiantamento();
 			anos();
 			balcao();
 			cargo();
 			cliente();
 			empresa();
 			entrada();
 			funcionario();
 			grupo();
 			login();
 			material();
 			meses();
 			notaFiscal();
 			orcamento();
 			os();
 			receber();
 			reposicao();
 			situacao();
 			veiculo();
 			compromisso();
 			fornecedor();
 			parcela();
 			periodo();
 			tipo();
			limpaBackUp();
			gravaBackUp();
 	 		labelFile.setText("Kbou!!!");
 	 		labelFile.viewOrderProperty();
 	 		labelCount.viewOrderProperty();
 		}	
 	}

	public void backUp() {
		file = "BackUp";
		path = unid + meioSgo + file + ext;
		CopiaService backService = new CopiaService();
		arq = "";
		crip = "";
		List<Copia> listB = backService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Copia b: listB) {
				count += 1;
				labelCount.setText(String.valueOf(count));
				labelCount.viewOrderProperty();
				arq = (b.getIdBackUp() + " | " + b.getDataIBackUp() + " | " + b.getUserBackUp() + " | " + b.getDataFBackUp());
				crip = Cryptograf.criptografa(arq);
//				System.out.println(crip);
//				String bc = Cryptograf.desCriptografa(crip);
//				System.out.println(crip);
				bwC.write(crip);
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void adiantamento() {
		file = "Adiantamento";
		path = unid + meioSgo + file + ext;
		AdiantamentoService adiService = new AdiantamentoService();
		CargoService carService = new CargoService();
		Cargo cargo = new Cargo();
		SituacaoService sitService = new SituacaoService();
		Situacao sit = new Situacao();
		List<Cargo> listC = carService.findAll();
		List<Situacao> listS = sitService.findAll();
		arq = "";
		crip = "";
		List<Adiantamento> listA = adiService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Adiantamento a: listA) {
				count += 1;
				labelCount.setText(String.valueOf(count));
				labelCount.viewOrderProperty();
				for (Cargo c : listC) {
					if (c.getNomeCargo().equals(a.getCargoFun())) {
						cargo = c;
						a.setCargo(cargo);
					}
				}
				for (Situacao s : listS) {
					if (s.getNomeSit().equals(a.getSituacaoFun())) {
						sit = s;
						a.setSituacao(sit);
					}
				}
				arq = (a.getNumeroAdi() + " | " + a.getDataAdi() + " | " + a.getValeAdi() + " | " +  
						a.getMesAdi() + " | " + a.getAnoAdi() + " | " + a.getValorAdi() + " | " + a.getOsAdi() + " | " + 
						a.getBalcaoAdi() + " | " +  a.getComissaoAdi() + " | " + a.getTipoAdi() + " | " + a.getSalarioAdi()
						+ " | " + a.getCodigoFun() + " | " + a.getNomeFun() + " | " + a.getMesFun() + " | " + a.getAnoFun()
						+ " | " + a.getCargoFun() + " | " + a.getSituacaoFun() + " | " + a.getSalarioFun() + " | " + 
						a.getCargo().getCodigoCargo() + " | " + a.getSituacao().getNumeroSit());
				
				crip = Cryptograf.criptografa(arq);
//				System.out.println(crip);
//				String bc = Cryptograf.desCriptografa(crip);
//				System.out.println(crip);
				bwC.write(crip);
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void anos() {
		file = "Anos";
		path = unid + meioSgo + file + ext;
		AnosService anoService = new AnosService();
		arq = "";
		crip = "";
		List<Anos> listA = anoService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Anos a: listA) {
				count += 1;
				labelCount.setText(String.valueOf(count));
				labelCount.viewOrderProperty();
				arq = (a.getNumeroAnos() + " | " + a.getAnoAnos() + " | " + a.getAnoAnos());
				crip = Cryptograf.criptografa(arq);
				bwC.write(crip);
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void balcao() {
		file = "Balcao";
		path = unid + meioSgo + file + ext;
		BalcaoService balService = new BalcaoService();
		arq = "";
		crip = "";
		List<Balcao> listB = balService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Balcao b: listB) {
				count += 1;
				labelCount.setText(String.valueOf(count));
				labelCount.viewOrderProperty();
				arq = (b.getNumeroBal() + " | " + b.getDataBal() + " | " + b.getFuncionarioBal() + " | " + b.getDescontoBal() 
				+ " | " + b.getTotalBal() + " | " + b.getPagamentoBal() + " | " + b.getDataPrimeiroPagamentoBal() + " | " + 
						b.getNnfBal() + " | " + b.getObservacaoBal() + " | " + b.getMesBal() + " | " + b.getAnoBal() + " | " + 
				 b.getFuncionario().getCodigoFun());
				crip = Cryptograf.criptografa(arq);
				bwC.write(crip);
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void cargo() {
		file = "Cargo";
		path = unid + meioSgo + file + ext;
		CargoService cartelaService = new CargoService();
		arq = "";
		crip = "";
		List<Cargo> listC = cartelaService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Cargo c: listC) {
				count += 1;
				labelCount.setText(String.valueOf(count));
				labelCount.viewOrderProperty();
				arq = (c.getCodigoCargo() + " | " + c.getNomeCargo() + " | " + c.getSalarioCargo() + " | " + c.getComissaoCargo());
				crip = Cryptograf.criptografa(arq);
				bwC.write(crip);
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void cliente() {
		file = "Cliente";
		path = unid + meioSgo + file + ext;
		ClienteService cliService = new ClienteService();
		arq = "";
		crip = "";
		List<Cliente> listC = cliService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Cliente c: listC) {
				count += 1;
				labelCount.setText(String.valueOf(count));
				labelCount.viewOrderProperty();
				arq = (c.getCodigoCli() + " | " + c.getNomeCli() + " | " + c.getRuaCli() + " | " + c.getNumeroCli() + " | " + 
						c.getComplementoCli() + " | " + c.getBairroCli() + " | " + c.getCidadeCli() + " | " + c.getUfCli()
						 + " | " + c.getCepCli() + " | " + c.getDdd01Cli() + " | " + c.getTelefone01Cli() + " | " + 
						c.getDdd02Cli() + " | " + c.getTelefone02Cli() + " | " + c.getEmailCli() + " | " + c.getPessoaCli()
						 + " | " + c.getCpfCli() + " | " + c.getCnpjCli() + " | " + c.getValorClass() + " | " + 
						c.getPercentualClass() + " | " + c.getLetraClass() + " | " + c.getVisitaClass());
				crip = Cryptograf.criptografa(arq);
				bwC.write(crip);
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void empresa() {
		file = "Empresa";
		path = unid + meioSgo + file + ext;
		EmpresaService empService = new EmpresaService();
		arq = "";
		crip = "";
		List<Empresa> listV = empService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Empresa e : listV) {
				count += 1;
				labelCount.setText(String.valueOf(count));
				labelCount.viewOrderProperty();
				arq = (e.getNumeroEmp() + " | " + e.getNomeEmp() + " | " + e.getEnderecoEmp() + " | " + e.getTelefoneEmp()
				 	+ " | " + e.getEmailEmp() + " | " + e.getPixEmp());
				crip = Cryptograf.criptografa(arq);
				bwC.write(crip);
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void entrada() {
		file = "Entrada";
		path = unid + meioSgo + file + ext;
		EntradaService entService = new EntradaService();
		arq = "";
		crip = "";
		List<Entrada> listE = entService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Entrada e : listE) {
				count += 1;
				labelCount.setText(String.valueOf(count));
				labelCount.viewOrderProperty();
				arq = (e.getNumeroEnt() + " | " + e.getDataEnt() + " | " + e.getForn().getRazaoSocial() + " | " + 
						e.getMat().getNomeMat() + " | " + e.getQuantidadeMatEnt() + " | " + e.getValorMatEnt()
						 + " | " + 	e.getForn().getCodigo() + " | " + e.getMat().getCodigoMat());
				crip = Cryptograf.criptografa(arq);
				bwC.write(crip);
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void funcionario() {
		file = "Funcionario";
		path = unid + meioSgo + file + ext;
		FuncionarioService funService = new FuncionarioService();
		arq = "";
		crip = "";
		List<Funcionario> listF = funService.findAll(2001, 01);
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Funcionario f : listF) {
				count += 1;
				labelCount.setText(String.valueOf(count));
				labelCount.viewOrderProperty();
				arq = (f.getCodigoFun() + " | " + f.getNomeFun() + " | " + f.getEnderecoFun() + " | " + f.getBairroFun()
				 	+ " | " + f.getCidadeFun() + " | " + f.getUfFun() + " | " + f.getCepFun() + " | " + f.getDddFun()
				 	 + " | " + f.getTelefoneFun() + " | " + f.getCpfFun() + " | " + f.getPixFun() + " | "  + " | " +  
				 	 f.getComissaoFun() + " | " + f.getAdiantamentoFun() + " | " + f.getMesFun() + " | " + f.getAnoFun() 
				 	 + " | " + f.getCargoFun() + " | " + f.getSituacaoFun() + " | " + f.getSalarioFun() + " | " +  
				 	 f.getCargo().getCodigoCargo() + " | " + f.getSituacao().getNumeroSit());
				crip = Cryptograf.criptografa(arq);
				bwC.write(crip);
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void grupo() {
		file = "Grupo";
		path = unid + meioSgo + file + ext;
		GrupoService gruService = new GrupoService();
		arq = "";
		crip = "";
		List<Grupo> listF = gruService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Grupo g : listF) {
				count += 1;
				labelCount.setText(String.valueOf(count));
				labelCount.viewOrderProperty();
				arq = (g.getCodigoGru() + " | " + g.getNomeGru());
				crip = Cryptograf.criptografa(arq);
				bwC.write(crip);
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void login() {
		file = "Login";
		path = unid + meioSgo + file + ext;
		LoginService logService = new LoginService();
		arq = "";
		crip = "";
		List<Login> listF = logService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Login l : listF) {
				count += 1;
				labelCount.setText(String.valueOf(count));
				labelCount.viewOrderProperty();
				arq = (l.getNumeroLog() + " | " + l.getSenhaLog() + " | " + l.getNomeLog() + " | " + l.getNivelLog()
				 + " | " + l.getAlertaLog() + " | " + l.getDataLog() + " | " + l.getVencimentoLog() + " | " + 
						l.getMaximaLog() + " | " + l.getAcessoLog() + " | " + l.getEmpresaLog());
//				crip = Cryptograf.criptografa(arq);
				bwC.write(arq);
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void material() {
		file = "Material";
		path = unid + meioSgo + file + ext;
		MaterialService matService = new MaterialService();
		arq = "";
		crip = "";
		List<Material> listM = matService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Material m : listM) {
				count += 1;
				labelCount.setText(String.valueOf(count));
				labelCount.viewOrderProperty();
				arq = (m.getCodigoMat() + " | " + m.getGrupo() + " | " +  m.getNomeMat() + " | " + m.getEstMinMat() + " | " + 
						m.getSaldoMat() + " | " + m.getSaidaCmmMat() + " | " + m.getCmmMat() + " | " + m.getPrecoMat() 
						+ " | " + m.getVendaMat() + " | " + m.getVidaKmMat() + " | " + m.getVidaMesMat() + " | " + 
						m.getPercentualClass() + " | " + m.getLetraClass() + " | " + m.getDataCadastroMat() + " | " + 
						m.getGrupo().getCodigoGru());
				crip = Cryptograf.criptografa(arq);
				bwC.write(crip);
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void meses() {
		file = "Meses";
		path = unid + meioSgo + file + ext;
		MesesService mesService = new MesesService();
		arq = "";
		crip = "";
		List<Meses> listM = mesService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Meses m : listM) {
				count += 1;
				labelCount.setText(String.valueOf(count));
				labelCount.viewOrderProperty();
				arq = (m.getNumeroMes() + " | " + m.getNomeMes());
				crip = Cryptograf.criptografa(arq);
				bwC.write(crip);
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void notaFiscal() {
		file = "NotaFiscal";
		path = unid + meioSgo + file + ext;
		NotaFiscalService nfService = new NotaFiscalService();
		arq = "";
		crip = "";
		List<NotaFiscal> listN = nfService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(NotaFiscal n : listN) {
				count += 1;
				labelCount.setText(String.valueOf(count));
				labelCount.viewOrderProperty();
				arq = (n.getCodigoNF() + " | " + n.getNumeroNF() + " | " + n.getBalcaoNF() + " | " + n.getOsNF());
				crip = Cryptograf.criptografa(arq);
				bwC.write(crip);
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void orcamento() {
		file = "Orcamento";
		path = unid + meioSgo + file + ext;
		OrcamentoService orcService = new OrcamentoService();
		arq = "";
		crip = "";
		List<Orcamento> listO = orcService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Orcamento o : listO) {
				count += 1;
				labelCount.setText(String.valueOf(count));
				labelCount.viewOrderProperty();
				arq = (o.getNumeroOrc() + " | " + o.getDataOrc() + " | " + o.getClienteOrc() + " | " + o.getFuncionarioOrc()
				 + " | " + o.getPlacaOrc() + " | " + o.getKmInicialOrc() + " | " + o.getKmFinalOrc() + " | " + 
						o.getDescontoOrc() + " | " + o.getTotalOrc() + " | " + o.getOsOrc() + " | " + o.getMesOrc() + " | " + 
				 o.getAnoOrc() + " | " + o.getCliente().getCodigoCli() + " | " + o.getFuncionario().getCodigoFun());
				crip = Cryptograf.criptografa(arq);
				bwC.write(crip);
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void virtual() {
		file = "OrcVirtual";
		path = unid + meioSgo + file + ext;
		OrcVirtualService virService = new OrcVirtualService();
		arq = "";
		crip = "";
		List<OrcVirtual> listV = virService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(OrcVirtual v : listV) {
				count += 1;
				labelCount.setText(String.valueOf(count));
				labelCount.viewOrderProperty();
				arq = (v.getNumeroVir() + " | " + v.getMaterial().getNomeMat() + " | " + v.getQuantidadeMatVir() + " | " + 
						v.getPrecoMatVir() + " | " + v.getTotalMatVir() + " | " + v.getNumeroOrcVir() + " | " + 
						v.getNumeroBalVir() + " | " + v.getMaterial().getCodigoMat() + " | " + v.getCustoMatVir());
				crip = Cryptograf.criptografa(arq);
				bwC.write(crip);
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void os() {
		file = "OrdemServico";
		path = unid + meioSgo + file + ext;
		OrdemServicoService osService = new OrdemServicoService();
		arq = "";
		crip = "";
		List<OrdemServico> listO = osService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(OrdemServico o : listO) {
				count += 1;
				labelCount.setText(String.valueOf(count));
				labelCount.viewOrderProperty();
				arq = (o.getNumeroOS() + " | " + o.getDataOS() + " | " + o.getOrcamentoOS() + " | " + o.getPlacaOS() + " | " + 
						o.getClienteOS() + " | " + o.getValorOS() + " | " + o.getParcelaOS() + " | " + o.getPrazoOS()
						 + " | " + o.getPagamentoOS() + " | " + o.getDataPrimeiroPagamentoOS() + " | " + o.getNnfOS()
						 + " | " + o.getObservacaoOS() + " | " + o.getKmOS() + " | " + o.getMesOs() + " | " + o.getAnoOs()
						 + " | " + o.getOrcamento().getNumeroOrc());
				crip = Cryptograf.criptografa(arq);
				bwC.write(crip);
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void receber() {
		file = "Receber";
		path = unid + meioSgo + file + ext;
		ReceberService recService = new ReceberService();
		arq = "";
		crip = "";
		List<Receber> listR = recService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Receber r : listR) {
				count += 1;
				labelCount.setText(String.valueOf(count));
				labelCount.viewOrderProperty();
				arq = (r.getNumeroRec() + " | " + r.getFuncionarioRec() + " | " + r.getClienteRec() + " | " + 
				r.getNomeClienteRec() + " | " + r.getOsRec() + " | " + r.getDataOsRec() + " | " + r.getPlacaRec() 
				+ " | " + r.getParcelaRec() + " | " + r.getFormaPagamentoRec() + " | " + r.getValorRec() + " | " + 
				r.getDataVencimentoRec() + " | " + r.getDataPagamentoRec() + " | " + r.getPeriodo().getIdPeriodo() + " | " + 
				r.getJurosRec() + " | " + r.getDescontoRec() + " | " + r.getTotalRec() + " | " + r.getValorPagoRec());
				
				crip = Cryptograf.criptografa(arq);
				bwC.write(crip);
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void reposicao() {
		file = "ReposicaoVeiculo";
		path = unid + meioSgo + file + ext;
		ReposicaoVeiculoService repService = new ReposicaoVeiculoService();
		arq = "";
		crip = "";
		List<ReposicaoVeiculo> listR = repService.findAllData();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(ReposicaoVeiculo r : listR) {
				count += 1;
				labelCount.setText(String.valueOf(count));
				labelCount.viewOrderProperty();
				arq = (r.getNumeroRep() + " | " + r.getOsRep()+ " | " + r.getDataRep() + " | " +  r.getPlacaRep() + " | " +
						r.getClienteRep() + " | " + r.getDddClienteRep() + " | " + r.getTelefoneClienteRep() + " | " + 
						r.getCodigoMaterialRep() + " | " + r.getMaterialRep() + " | " +  r.getKmTrocaRep() + " | " + 
						r.getProximaKmRep() + " | " + r.getProximaDataRep());
				crip = Cryptograf.criptografa(arq);
				bwC.write(crip);
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void situacao() {
		file = "Situacao";
		path = unid + meioSgo + file + ext;
		SituacaoService sitService = new SituacaoService();
		arq = "";
		crip = "";
		List<Situacao> listS = sitService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Situacao s : listS) {
				count += 1;
				labelCount.setText(String.valueOf(count));
				labelCount.viewOrderProperty();
				arq = (s.getNumeroSit() + " | " + s.getNomeSit());
				crip = Cryptograf.criptografa(arq);
				bwC.write(crip);
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void veiculo() {
		file = "Veiculo";
		path = unid + meioSgo + file + ext;
		VeiculoService veiService = new VeiculoService();
		arq = "";
		crip = "";
		List<Veiculo> listV = veiService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Veiculo v : listV) {
				count += 1;
				labelCount.setText(String.valueOf(count));
				labelCount.viewOrderProperty();
				arq = (v.getNumeroVei() + " | " + v.getPlacaVei() + " | " + v.getKmInicialVei() + " | " + 
						v.getKmFinalVei() + " | " + v.getModeloVei() + " | " + v.getAnoVei());
				crip = Cryptograf.criptografa(arq);
				bwC.write(crip);
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void compromisso() {
		file = "Compromisso";
		path = unid + meioSgcp + file + ext;
		CompromissoService comService = new CompromissoService();
		arq = "";
		crip = "";
		List<Compromisso> listC = comService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Compromisso c : listC) {
				count += 1;
				labelCount.setText(String.valueOf(count));
				labelCount.viewOrderProperty();
				arq = (c.getIdCom() + " | " + c.getFornecedor().getCodigo() + " | " + c.getFornecedor().getRazaoSocial()
						 + " | " + c.getNnfCom() + " | " + c.getDataCom() + " | " + c.getDataVencimentoCom() + " | " + 
						c.getValorCom() + " | " + c.getParcelaCom() + " | " + c.getPrazoCom() + " | " + 
						 c.getFornecedor().getCodigo() + " | " + c.getTipoConsumo().getCodigoTipo() + " | " + 
						c.getParPeriodo().getIdPeriodo());
				crip = Cryptograf.criptografa(arq);
				bwC.write(crip);
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void fornecedor() {
		file = "Fornecedor";
		path = unid + meioSgcp + file + ext;
		FornecedorService forService = new FornecedorService();
		arq = "";
		crip = "";
		List<Fornecedor> listF = forService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Fornecedor f : listF) {
				count += 1;
				labelCount.setText(String.valueOf(count));
				labelCount.viewOrderProperty();
				arq = (f.getCodigo() + " | " + f.getRazaoSocial() + " | " + f.getRua() + " | " + 
						f.getNumero() + " | " + f.getComplemento() + " | " + f.getBairro() + " | " + f.getCidade()
						 + " | " + f.getUf() + " | " + f.getCep() + " | " + f.getDdd01() + " | " + f.getTelefone01()
						 + " | " + f.getDdd02() + " | " + f.getTelefone02() + " | " + f.getContato() + " | " + 
						 f.getDddContato() + " | " + f.getTelefoneContato() + " | " + f.getEmail() + " | " + f.getPix()
						 + " | " + f.getObservacao() + " | " + f.getPrazo() + " | " + f.getParcela());
				crip = Cryptograf.criptografa(arq);
				bwC.write(crip);
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void parcela() {
		file = "Parcela";
		path = unid + meioSgcp + file + ext;
		ParcelaService parService = new ParcelaService();
		arq = "";
		crip = "";
		List<Parcela> listP = parService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Parcela p : listP) {
				count += 1;
				labelCount.setText(String.valueOf(count));
				labelCount.viewOrderProperty();
				arq = (p.getIdPar() + " | " + p.getCodigoFornecedorPar() + " | " + p.getNomeFornecedorPar() + " | " + 
						p.getNnfPar() + " | " + p.getNumeroPar() + " | " + p.getDataVencimentoPar() + " | " + 
						p.getValorPar() + " | " + p.getDescontoPar() + " | " + p.getJurosPar() + " | " + p.getTotalPar()
						 + " | " + p.getPagoPar() + " | " + p.getDataPagamentoPar() + " | " + p.getFornecedor().getCodigo()
						 + " | " + p.getTipoConsumo().getCodigoTipo() + " | " + p.getPeriodo().getIdPeriodo());
				crip = Cryptograf.criptografa(arq);
				bwC.write(crip);
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void periodo() {
		file = "ParPeriodo";
		path = unid + meioSgcp + file + ext;
		ParPeriodoService perService = new ParPeriodoService();
		arq = "";
		crip = "";
		List<ParPeriodo> listP = perService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(ParPeriodo p : listP) {
				count += 1;
				labelCount.setText(String.valueOf(count));
				labelCount.viewOrderProperty();
				arq = (p.getIdPeriodo() + " | " + p.getDtiPeriodo() + " | " + p.getDtfPeriodo() + " | " + 
						p.getFornecedor().getCodigo() + " | " + p.getTipoConsumo().getCodigoTipo());
				crip = Cryptograf.criptografa(arq);
				bwC.write(crip);
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void tipo() {
		file = "TipoConsumidor";
		path = unid + meioSgcp + file + ext;
		TipoConsumoService tipoService = new TipoConsumoService();
		arq = "";
		crip = "";
		List<TipoConsumo> listT = tipoService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(TipoConsumo t : listT) {
				count += 1;
				labelCount.setText(String.valueOf(count));
				labelCount.viewOrderProperty();
				arq = (t.getCodigoTipo() + " | " + t.getNomeTipo());
				crip = Cryptograf.criptografa(arq);
				bwC.write(crip);
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}

 	private void initializeNodes() {
		tableColumnDateIBackUp.setCellValueFactory(new PropertyValueFactory<>("DataIBackUp"));
		tableColumnUserBackUp.setCellValueFactory(new PropertyValueFactory<>("UserBackUp"));
		tableColumnDateFBackUp.setCellValueFactory(new PropertyValueFactory<>("DataFBackUp"));
 		Stage stage = (Stage) MainSgo.getMainScene().getWindow();
 		tableViewBackUp.prefHeightProperty().bind(stage.heightProperty());
 	}

 	public void updateTableView() {
 		if (service == null) {
			throw new IllegalStateException("Serviço está vazio");
 		}
 		labelUser.setText(user);
 		labelFile.setText("<<<aguarde>>>");
 		labelCount.setText(String.valueOf(count));
 		List<Copia> list = new ArrayList<>();
		list = service.findAll();
  		obsList = FXCollections.observableArrayList(list);
  		tableViewBackUp.setItems(obsList);
	}

	private void notifyDataChangeListerners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}
	
	public void limpaBackUp() {
		LocalDate dt1 = DataStatic.dateParaLocal(dataI);
		int ano1 = DataStatic.anoDaData(dt1);
		int mes1 = DataStatic.mesDaData(dt1);
		int dia1 = DataStatic.diaDaData(dt1);
		
		List<Copia> listLimpa = service.findAll();
		for (Copia b : listLimpa) {
			LocalDate dtB = DataStatic.converteTimeFormataString(b.getDataIBackUp());
			int anoB = DataStatic.anoDaData(dtB);
			int mesB = DataStatic.mesDaData(dtB);
			int diaB = DataStatic.diaDaData(dtB);

			if(ano1 == anoB && mes1 == mesB && dia1 == diaB) {
				service.remove(b.getIdBackUp());
			} else {
				if (anoB < ano1) {
					service.remove(b.getIdBackUp());
				} else {
					if (mesB < mes1) {
						service.remove(b.getIdBackUp());
					}	
				}	
			}			
		}
	}
	
 	public void gravaBackUp() {
 		grw += 1;
		String dti = sdf.format(dataI); 
		entity.setIdBackUp(null);
		entity.setDataIBackUp(dti);
		entity.setUserBackUp(user);
		dataF = new Date(System.currentTimeMillis());
		String dtf = sdf.format(dataF);
		entity.setDataFBackUp(dtf);
		entityDel = entity;
		service.saveOrUpdate(entity);
		if (entity.getDataIBackUp() == entityDel.getDataIBackUp()) {
			service.remove(entity.getIdBackUp());
		}
		notifyDataChangeListerners();
		updateTableView();
 	}
 	
	private void createDialogForm(String absoluteName, Stage parentStage) {
        String classe = "BackUp SGO ";
		try {
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			CopiaSgoFormController controller = loader.getController();
			controller.loadAssociatedObjects();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Selecione unidade para BackUp                 ");
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
