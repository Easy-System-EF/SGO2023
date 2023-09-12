package gui.copia;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
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

 	@FXML
 	private TableColumn<Copia, String> tableColumnUnidBackUp;

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
 	SimpleDateFormat sdfAno = new SimpleDateFormat("yyyy-MM-dd HH:mm");
 	
 	private CopiaService service;
 	private Copia entity;
 	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

 	public void setBackUpService(CopiaService service) {
 		this.service = service;
 	}
 	
 	public void setEntity(Copia entity) {
 		this.entity = entity;
 	}
	int grw = 0;

 	@FXML
  	public void onBtOkAdiAction(ActionEvent event) throws ParseException {
 		 Stage parentStage = Utils.currentStage(event);
// instanciando novo obj depto e injetando via
 		 createDialogForm("/gui/copia/CopiaForm.fxml", parentStage);
 		 updateTableView();
 		 executaBack();
   	}
 	
 	public void executaBack() throws ParseException {
 		if (unid != null) {
 			count = 0;
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
 			virtual();
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

	public void adiantamento() {
		file = "Adiantamento";
		path = unid + meioSgo + file + ext;
		AdiantamentoService adiService = new AdiantamentoService();
		CargoService carService = new CargoService();
		Cargo cargo = new Cargo();
		SituacaoService sitService = new SituacaoService();
		Situacao sit = new Situacao();
		List<Cargo> listC = carService.findAllId();
		List<Situacao> listS = sitService.findAllId();
		arq = "";
		crip = "";
		List<Adiantamento> listA = adiService.findAllId();
		try {BufferedWriter bwAdi = new BufferedWriter(new FileWriter(path));
			for(Adiantamento a: listA) {
				count += 1;
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
				String data = sdfAno.format(a.getDataAdi());
				arq = (a.getNumeroAdi() + " , " + data + " , " + a.getValeAdi() + " , " +  
						a.getMesAdi() + " , " + a.getAnoAdi() + " , " + a.getValorAdi() + " , " + a.getOsAdi() + " , " + 
						a.getBalcaoAdi() + " , " +  a.getComissaoAdi() + " , " + a.getTipoAdi() + " , " + a.getSalarioAdi()
						+ " , " + a.getCodigoFun() + " , " + a.getNomeFun() + " , " + a.getMesFun() + " , " + a.getAnoFun()
						+ " , " + a.getCargoFun() + " , " + a.getSituacaoFun() + " , " + a.getSalarioFun() + " , " + 
						a.getCargo().getCodigoCargo() + " , " + a.getSituacao().getNumeroSit());
				crip = Cryptograf.criptografa(arq);
				bwAdi.write(crip);
				bwAdi.newLine();				
			}
			bwAdi.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
			labelCount.setText(String.valueOf(count));
			labelCount.viewOrderProperty();
		}
	}
	
	public void anos() {
		file = "Anos";
		path = unid + meioSgo + file + ext;
		AnosService anoService = new AnosService();
		arq = "";
		crip = "";
		List<Anos> listA = anoService.findAllId();
		try {BufferedWriter bwAno = new BufferedWriter(new FileWriter(path));
			for(Anos a: listA) {
				count += 1;
				arq = (a.getNumeroAnos() + " , " + a.getAnoAnos() + " , " + a.getAnoAnos());
				crip = Cryptograf.criptografa(arq);
				bwAno.write(crip);
				bwAno.newLine();
			}
			bwAno.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
			labelCount.setText(String.valueOf(count));
			labelCount.viewOrderProperty();
		}
	}

	public void balcao() {
		file = "Balcao";
		path = unid + meioSgo + file + ext;
		BalcaoService balService = new BalcaoService();
		arq = "";
		crip = "";
		List<Balcao> listB = balService.findAllId();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Balcao b: listB) {
				count += 1;
				String dataBal = sdfAno.format(b.getDataBal());
				String dataPri = sdfAno.format(b.getDataPrimeiroPagamentoBal());
				arq = (b.getNumeroBal() + " , " + dataBal + " , " + b.getFuncionarioBal() + " , " + b.getDescontoBal() 
				+ " , " + b.getTotalBal() + " , " + b.getPagamentoBal() + " , " + dataPri + " , " + 
						b.getNnfBal() + " , " + b.getObservacaoBal() + " , " + b.getMesBal() + " , " + b.getAnoBal() + " , " + 
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
			labelCount.setText(String.valueOf(count));
			labelCount.viewOrderProperty();
		}
	}
	
	public void cargo() {
		file = "Cargo";
		path = unid + meioSgo + file + ext;
		CargoService carService = new CargoService();
		arq = "";
		crip = "";
		List<Cargo> listC = carService.findAllId();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Cargo c: listC) {
				count += 1;
				arq = (c.getCodigoCargo() + " , " + c.getNomeCargo() + " , " + c.getSalarioCargo() + " , " + c.getComissaoCargo());
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
			labelCount.setText(String.valueOf(count));
			labelCount.viewOrderProperty();
		}
	}
	
	public void cliente() {
		file = "Cliente";
		path = unid + meioSgo + file + ext;
		ClienteService cliService = new ClienteService();
		arq = "";
		crip = "";
		List<Cliente> listC = cliService.findAllId();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Cliente c: listC) {
				count += 1;
				arq = (c.getCodigoCli() + " , " + c.getNomeCli() + " , " + c.getRuaCli() + " , " + c.getNumeroCli() + " , " + 
						c.getComplementoCli() + " , " + c.getBairroCli() + " , " + c.getCidadeCli() + " , " + c.getUfCli()
						 + " , " + c.getCepCli() + " , " + c.getDdd01Cli() + " , " + c.getTelefone01Cli() + " , " + 
						c.getDdd02Cli() + " , " + c.getTelefone02Cli() + " , " + c.getEmailCli() + " , " + c.getPessoaCli()
						 + " , " + c.getCpfCli() + " , " + c.getCnpjCli() + " , " + c.getValorClass() + " , " + 
						c.getPercentualClass() + " , " + c.getLetraClass() + " , " + c.getVisitaClass());
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
			labelCount.setText(String.valueOf(count));
			labelCount.viewOrderProperty();
		}
	}
	
	public void empresa() {
		file = "Empresa";
		path = unid + meioSgo + file + ext;
		EmpresaService empService = new EmpresaService();
		arq = "";
		crip = "";
		String crip = "";
		List<Empresa> listV = empService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Empresa e : listV) {
				count += 1;
				arq = (e.getNumeroEmp() + " , " + e.getNomeEmp() + " , " + e.getEnderecoEmp() + " , " + e.getTelefoneEmp()
				 	+ " , " + e.getEmailEmp() + " , " + e.getPixEmp());
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
			labelCount.setText(String.valueOf(count));
			labelCount.viewOrderProperty();
		}
	}
	
	public void entrada() {
		file = "Entrada";
		path = unid + meioSgo + file + ext;
		EntradaService entService = new EntradaService();
		arq = "";
		List<Entrada> listE = entService.findAllId();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Entrada e : listE) {
				count += 1;
				String dataEnt = sdfAno.format(e.getDataEnt());
				arq = (e.getNumeroEnt() + " , " + e.getNnfEnt()  + " , " + dataEnt + " , " + e.getNomeFornEnt() + " , " + 
						e.getNomeMatEnt() + " , " + e.getQuantidadeMatEnt() + " , " + e.getValorMatEnt() + " , " + 
						e.getForn().getCodigo() + " , " + e.getMat().getCodigoMat());
				
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
			labelCount.setText(String.valueOf(count));
			labelCount.viewOrderProperty();
		}
	}
	
	public void funcionario() {
		file = "Funcionario";
		path = unid + meioSgo + file + ext;
		FuncionarioService funService = new FuncionarioService();
		arq = "";
		crip = "";
		List<Funcionario> listF = funService.findAllId();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Funcionario f : listF) {
				count += 1;
				arq = (f.getCodigoFun() + " , " + f.getNomeFun() + " , " + f.getEnderecoFun() + " , " + f.getBairroFun()
				 	+ " , " + f.getCidadeFun() + " , " + f.getUfFun() + " , " + f.getCepFun() + " , " + f.getDddFun()
				 	 + " , " + f.getTelefoneFun() + " , " + f.getCpfFun() + " , " + f.getPixFun() + " , "  + 
				 	 f.getComissaoFun() + " , " + f.getAdiantamentoFun() + " , " + f.getMesFun() + " , " + f.getAnoFun() 
				 	 + " , " + f.getCargoFun() + " , " + f.getSituacaoFun() + " , " + f.getSalarioFun() + " , " +  
				 	 f.getCargo().getCodigoCargo() + " , " + f.getSituacao().getNumeroSit());
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
			labelCount.setText(String.valueOf(count));
			labelCount.viewOrderProperty();
		}
	}
	
	public void grupo() {
		file = "Grupo";
		path = unid + meioSgo + file + ext;
		GrupoService gruService = new GrupoService();
		arq = "";
		crip = "";
		List<Grupo> listF = gruService.findAllId();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Grupo g : listF) {
				count += 1;
				arq = (g.getCodigoGru() + " , " + g.getNomeGru());
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
			labelCount.setText(String.valueOf(count));
			labelCount.viewOrderProperty();
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
				String dataLog = sdfAno.format(l.getDataLog());
				String dataVen = sdfAno.format(l.getVencimentoLog());
				String dataMax = sdfAno.format(l.getMaximaLog());
				String dataAce = sdfAno.format(l.getAcessoLog());

				arq = (l.getNumeroLog() + " , " + l.getSenhaLog() + " , " + l.getNomeLog() + " , " + l.getNivelLog()
				 + " , " + l.getAlertaLog() + " , " + dataLog + " , " + dataVen + " , " + 
						dataMax + " , " + dataAce + " , " + l.getEmpresaLog());
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
			labelCount.setText(String.valueOf(count));
			labelCount.viewOrderProperty();
		}
	}
	
	public void material() {
		file = "Material";
		path = unid + meioSgo + file + ext;
		MaterialService matService = new MaterialService();
		arq = "";
		crip = "";
		List<Material> listM = matService.findAllId();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Material m : listM) {
				count += 1;
				String dataMat = sdfAno.format(m.getDataCadastroMat());
				arq = (m.getCodigoMat() + " , " + m.getGrupoMat() + " , " +  m.getNomeMat() + " , " + m.getEstMinMat() + " , " + 
						m.getSaldoMat() + " , " + m.getSaidaCmmMat() + " , " + m.getCmmMat() + " , " + m.getPrecoMat() 
						+ " , " + m.getVendaMat() + " , " + m.getVidaKmMat() + " , " + m.getVidaMesMat() + " , " + 
						m.getPercentualClass() + " , " + m.getLetraClass() + " , " + dataMat + " , " + 
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
			labelCount.setText(String.valueOf(count));
			labelCount.viewOrderProperty();
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
				arq = (m.getNumeroMes() + " , " + m.getNomeMes());
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
			labelCount.setText(String.valueOf(count));
			labelCount.viewOrderProperty();
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
				arq = (n.getCodigoNF() + " , " + n.getNumeroNF() + " , " + n.getBalcaoNF() + " , " + n.getOsNF());
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
			labelCount.setText(String.valueOf(count));
			labelCount.viewOrderProperty();
		}
	}
	
	public void virtual() {
		file = "OrcVirtual";
		path = unid + meioSgo + file + ext;
		OrcVirtualService virService = new OrcVirtualService();
		arq = "";
		crip = "";
		List<OrcVirtual> listV = virService.findAllId();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(OrcVirtual v : listV) {
				count += 1;
				arq = (v.getNumeroVir() + " , " + v.getMaterial().getNomeMat() + " , " + v.getQuantidadeMatVir() + " , " + 
						v.getPrecoMatVir() + " , " + v.getTotalMatVir() + " , " + v.getNumeroOrcVir() + " , " + 
						v.getNumeroBalVir() + " , " + v.getMaterial().getCodigoMat() + " , " + v.getCustoMatVir());
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
			labelCount.setText(String.valueOf(count));
			labelCount.viewOrderProperty();
		}
	}
	
	public void orcamento() throws ParseException {
		file = "Orcamento";
		path = unid + meioSgo + file + ext;
		OrcamentoService orcService = new OrcamentoService();
		arq = "";
		crip = "";
		List<Orcamento> listO = orcService.findAllId();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Orcamento o : listO) {
				count += 1;
				String dataOrc = sdfAno.format(o.getDataOrc());
				arq = (o.getNumeroOrc() + " , " + dataOrc + " , " + o.getClienteOrc() + " , " + o.getFuncionarioOrc()
				 + " , " + o.getPlacaOrc() + " , " + o.getKmInicialOrc() + " , " + o.getKmFinalOrc() + " , " + 
						o.getDescontoOrc() + " , " + o.getTotalOrc() + " , " + o.getOsOrc() + " , " + o.getMesOrc() + " , " + 
				 o.getAnoOrc() + " , " + o.getCliente().getCodigoCli() + " , " + o.getFuncionario().getCodigoFun());
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
			labelCount.setText(String.valueOf(count));
			labelCount.viewOrderProperty();
		}
	}
	
	public void os() {
		file = "OrdemServico";
		path = unid + meioSgo + file + ext;
		OrdemServicoService osService = new OrdemServicoService();
		arq = "";
		crip = "";
		List<OrdemServico> listO = osService.findAllId();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(OrdemServico o : listO) {
				count += 1;
				String dataOs = sdfAno.format(o.getDataOS());
				String dataPri = sdfAno.format(o.getDataPrimeiroPagamentoOS());
				arq = (o.getNumeroOS() + " , " + dataOs + " , " + o.getOrcamentoOS() + " , " + o.getPlacaOS() + " , " + 
						o.getClienteOS() + " , " + o.getValorOS() + " , " + o.getParcelaOS() + " , " + o.getPrazoOS()
						 + " , " + o.getPagamentoOS() + " , " + dataPri + " , " + o.getNnfOS()
						 + " , " + o.getObservacaoOS() + " , " + o.getKmOS() + " , " + o.getMesOs() + " , " + o.getAnoOs()
						 + " , " + o.getOrcamento().getNumeroOrc());
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
			labelCount.setText(String.valueOf(count));
			labelCount.viewOrderProperty();
		}
	}
	
	public void receber() {
		file = "Receber";
		path = unid + meioSgo + file + ext;
		ReceberService recService = new ReceberService();
		arq = "";
		crip = "";
		List<Receber> listR = recService.findAllId();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Receber r : listR) {
				count += 1;
				String dataOs = sdfAno.format(r.getDataOsRec());
				String dataVen = sdfAno.format(r.getDataVencimentoRec());
				String dataPag = sdfAno.format(r.getDataPagamentoRec());
				arq = (r.getNumeroRec() + " , " + r.getFuncionarioRec() + " , " + r.getClienteRec() + " , " + 
				r.getNomeClienteRec() + " , " + r.getOsRec() + " , " + dataOs + " , " + r.getPlacaRec() 
				+ " , " + r.getParcelaRec() + " , " + r.getFormaPagamentoRec() + " , " + r.getValorRec() + " , " + 
				dataVen + " , " + dataPag + " , " + r.getPeriodo().getIdPeriodo() + " , " + 
				r.getJurosRec() + " , " + r.getDescontoRec() + " , " + r.getTotalRec() + " , " + r.getValorPagoRec());
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
			labelCount.setText(String.valueOf(count));
			labelCount.viewOrderProperty();
		}
	}
	
	public void reposicao() {
		file = "ReposicaoVeiculo";
		path = unid + meioSgo + file + ext;
		ReposicaoVeiculoService repService = new ReposicaoVeiculoService();
		arq = "";
		crip = "";
		List<ReposicaoVeiculo> listR = repService.findAllId();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(ReposicaoVeiculo r : listR) {
				count += 1;
				String dtRep = sdfAno.format(r.getDataRep());
				String dtPro = sdfAno.format(r.getProximaDataRep());
				arq = (r.getNumeroRep() + " , " + r.getOsRep()+ " , " + dtRep + " , " +  r.getPlacaRep() + " , " +
						r.getClienteRep() + " , " + r.getDddClienteRep() + " , " + r.getTelefoneClienteRep() + " , " + 
						r.getCodigoMaterialRep() + " , " + r.getMaterialRep() + " , " +  r.getKmTrocaRep() + " , " + 
						r.getProximaKmRep() + " , " + dtPro);
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
			labelCount.setText(String.valueOf(count));
			labelCount.viewOrderProperty();
		}
	}
	
	public void situacao() {
		file = "Situacao";
		path = unid + meioSgo + file + ext;
		SituacaoService sitService = new SituacaoService();
		arq = "";
		crip = "";
		List<Situacao> listS = sitService.findAllId();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Situacao s : listS) {
				count += 1;
				arq = (s.getNumeroSit() + " , " + s.getNomeSit());
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
			labelCount.setText(String.valueOf(count));
			labelCount.viewOrderProperty();
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
				if (v.getModeloVei() == null) {
					v.setModeloVei(" ");
				}
				arq = (v.getNumeroVei() + " , " + v.getPlacaVei() + " , " + v.getKmInicialVei() + " , " + 
						v.getKmFinalVei() + " , " + v.getModeloVei() + " , " + v.getAnoVei());
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
			labelCount.setText(String.valueOf(count));
			labelCount.viewOrderProperty();
		}
	}
	
	public void compromisso() {
		file = "Compromisso";
		path = unid + meioSgo + file + ext;
		CompromissoService comService = new CompromissoService();
		arq = "";
		crip = "";
		List<Compromisso> listC = comService.findAllId();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Compromisso c : listC) {
				count += 1;
				String dtCom = sdfAno.format(c.getDataCom());
				String dtVen = sdfAno.format(c.getDataVencimentoCom());
				arq = (c.getIdCom() + " , " + c.getFornecedor().getCodigo() + " , " + c.getFornecedor().getRazaoSocial()
						 + " , " + c.getNnfCom() + " , " + dtCom + " , " + dtVen + " , " + 
						c.getValorCom() + " , " + c.getParcelaCom() + " , " + c.getPrazoCom() + " , " + 
						 c.getFornecedor().getCodigo() + " , " + c.getTipoConsumo().getCodigoTipo() + " , " + 
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
			labelCount.setText(String.valueOf(count));
			labelCount.viewOrderProperty();
		}
	}
	
	public void fornecedor() {
		file = "Fornecedor";
		path = unid + meioSgo + file + ext;
		FornecedorService forService = new FornecedorService();
		arq = "";
		crip = "";
		List<Fornecedor> listF = forService.findAllId();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Fornecedor f : listF) {
				count += 1;
				arq = (f.getCodigo() + " , " + f.getRazaoSocial() + " , " + f.getRua() + " , " + 
						f.getNumero() + " , " + f.getComplemento() + " , " + f.getBairro() + " , " + f.getCidade()
						 + " , " + f.getUf() + " , " + f.getCep() + " , " + f.getDdd01() + " , " + f.getTelefone01()
						 + " , " + f.getDdd02() + " , " + f.getTelefone02() + " , " + f.getContato() + " , " + 
						 f.getDddContato() + " , " + f.getTelefoneContato() + " , " + f.getEmail() + " , " + f.getPix()
						 + " , " + f.getObservacao() + " , " + f.getPrazo() + " , " + f.getParcela());
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
			labelCount.setText(String.valueOf(count));
			labelCount.viewOrderProperty();
		}
	}
	
	public void parcela() {
		file = "Parcela";
		path = unid + meioSgo + file + ext;
		ParcelaService parService = new ParcelaService();
		arq = "";
		crip = "";
		List<Parcela> listP = parService.findAllId();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Parcela p : listP) {
				count += 1;
				String dtVen = sdfAno.format(p.getDataVencimentoPar());
				String dtPag = sdfAno.format(p.getDataPagamentoPar());
				arq = (p.getIdPar() + " , " + p.getCodigoFornecedorPar() + " , " + p.getNomeFornecedorPar() + " , " + 
						p.getNnfPar() + " , " + p.getNumeroPar() + " , " + dtVen + " , " + 
						p.getValorPar() + " , " + p.getDescontoPar() + " , " + p.getJurosPar() + " , " + p.getTotalPar()
						 + " , " + p.getPagoPar() + " , " + dtPag + " , " + p.getFornecedor().getCodigo()
						 + " , " + p.getTipoConsumo().getCodigoTipo() + " , " + p.getPeriodo().getIdPeriodo());
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
			labelCount.setText(String.valueOf(count));
			labelCount.viewOrderProperty();
		}
	}
	
	public void periodo() {
		file = "ParPeriodo";
		path = unid + meioSgo + file + ext;
		ParPeriodoService perService = new ParPeriodoService();
		arq = "";
		crip = "";
		List<ParPeriodo> listP = perService.findAllId();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(ParPeriodo p : listP) {
				count += 1;
				String dti = sdfAno.format(p.getDtiPeriodo());
				String dtf = sdfAno.format(p.getDtfPeriodo());
				arq = (p.getIdPeriodo() + " , " + dti + " , " + dtf + " , " + 
						p.getFornecedor().getCodigo() + " , " + p.getTipoConsumo().getCodigoTipo());
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
			labelCount.setText(String.valueOf(count));
			labelCount.viewOrderProperty();
		}
	}
	
	public void tipo() {
		file = "TipoConsumidor";
		path = unid + meioSgo + file + ext;
		TipoConsumoService tipoService = new TipoConsumoService();
		arq = "";
		crip = "";
		List<TipoConsumo> listT = tipoService.findAllId();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(TipoConsumo t : listT) {
				count += 1;
				arq = (t.getCodigoTipo() + " , " + t.getNomeTipo());
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
			labelCount.setText(String.valueOf(count));
			labelCount.viewOrderProperty();
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
		tableColumnUnidBackUp.setCellValueFactory(new PropertyValueFactory<>("UnidadeBackUp"));
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
			int countM = 0;
			int countY = 0;

			if(ano1 == anoB && mes1 == mesB && dia1 == diaB && b.getUnidadeBackUp().equals(unid)) {
				service.remove(b.getIdBackUp());
			} else {
				if (anoB < ano1) {
					countY += 1;
					if (countY > 1) {
						service.remove(b.getIdBackUp());
					}	
				} else {
					if (mesB < mes1) {
						countM += 1;
						if (countM > 1) {
							service.remove(b.getIdBackUp());
						}	
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
		entity.setUnidadeBackUp(unid);
		service.saveOrUpdate(entity);
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
