package gui.copia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import application.MainSgo;
import db.DbException;
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

public class RestauraSgoController implements Initializable, DataChangeListener {

	@FXML
	private VBox vBoxRestaura;

	@FXML
	private Button buttonOk;

 	@FXML
	private Label labelFile;

	@FXML
	private Label labelUser;

	@FXML
	private Label labelCount;

	@FXML
	private TableView<Restaura> tableViewRestaura;

 	@FXML
 	private TableColumn<Restaura, String> tableColumnFile;

 	@FXML
 	private TableColumn<Restaura, String> tableColumnStatus;

 	@FXML
 	private TableColumn<Restaura, Integer> tableColumnCount;

 	private ObservableList<Restaura> obsList;

	Integer count = 0;
	Integer countAk = 0;
	String arq = "";
	String crip = "";
	String status = "";
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
 	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
 	SimpleDateFormat sdfAno = new SimpleDateFormat("yyyy-MM-dd HH:mm");
 	Calendar cal = Calendar.getInstance();
 	
 	public RestauraService service;
 	private Restaura entity;
 	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

 	public void setRestauraService(RestauraService service) {
 		this.service = service;
 	}
 	
 	public void setEntity(Restaura entity) {
 		this.entity = entity;
 	}

 	@FXML
  	public void onBtOkAdiAction(ActionEvent event) throws ParseException {
 		 Stage parentStage = Utils.currentStage(event);
// instanciando novo obj depto e injetando via
 		 createDialogForm("/gui/copia/RestauraForm.fxml", parentStage);
// 		 service.zeraAll();
 		 updateTableView();
 		 executaBack();
   	}
 	
 	public void executaBack() throws ParseException {
 		if (unid != null) {
 			count = 0;
//			Alerts.showAlert("Atenção", "isoo pode demorar um pouco", null, AlertType.WARNING);
//// 			anos();
 			login();
//// 			meses();
//// 			empresa();
 			tipo();
 			fornecedor();
 			grupo();
 			cargo();
 			notaFiscal();
 			situacao();
 			veiculo();
 			material();
 			funcionario();
 			adiantamento();
 			cliente();
 			virtual();
 			orcamento();
 			os();
 			balcao();
			periodo();
			entrada();
			receber();
			reposicao();
			compromisso();
			parcela();
			labelFile.setText("Kbou!!!");
			labelFile.viewOrderProperty();
			labelCount.viewOrderProperty();
 		}	
 	}

	public void adiantamento() throws ParseException {
		status = "Ok";
		file = "Adiantamento";
		path = unid + meioSgo + file + ext;
		countAk = 0;
		Funcionario fun = new Funcionario();
		FuncionarioService funService = new FuncionarioService();
		CargoService carService = new CargoService();
		SituacaoService sitService = new SituacaoService();
		Adiantamento adi = new Adiantamento();
		AdiantamentoService adiService = new AdiantamentoService();
		String arqAdi = "";
		try {BufferedReader brAdi = new BufferedReader(new FileReader(path));
			String lineAdi = brAdi.readLine();
			while (lineAdi != null) { 
				countAk +=1;
				arqAdi = Cryptograf.desCriptografa(lineAdi);
				String[] campoAdi = arqAdi.split(" , ");
				adi.setNumeroAdi(Integer.parseInt(campoAdi[0]));
				Date dt = sdfAno.parse(campoAdi[1]);
				cal.setTime(dt);
				adi.setDataAdi(cal.getTime());
				adi.setValeAdi(Double.parseDouble(campoAdi[2]));
				adi.setMesAdi(Integer.parseInt(campoAdi[3]));
				adi.setAnoAdi(Integer.parseInt(campoAdi[4]));
				adi.setValorAdi(Double.parseDouble(campoAdi[5]));
				adi.setOsAdi(Integer.parseInt(campoAdi[6]));
				adi.setBalcaoAdi(Integer.parseInt(campoAdi[7]));
				adi.setComissaoAdi(Double.parseDouble(campoAdi[8]));
				adi.setTipoAdi(campoAdi[9]);
				adi.setSalarioAdi(Double.parseDouble(campoAdi[10]));
				adi.setCodigoFun(Integer.parseInt(campoAdi[11]));
				adi.setNomeFun(campoAdi[12]);
				adi.setMesFun(Integer.parseInt(campoAdi[13]));
				adi.setAnoFun(Integer.parseInt(campoAdi[14]));
				adi.setCargoFun(campoAdi[15]);
				adi.setSituacaoFun(campoAdi[16]);
				adi.setSalarioFun(Double.parseDouble(campoAdi[17]));
				fun = funService.findById(Integer.parseInt(campoAdi[11]));
				fun.setCargo(carService.findById(Integer.parseInt(campoAdi[18])));
				fun.setSituacao(sitService.findById(Integer.parseInt(campoAdi[19])));
				adi.setCargo(carService.findById(Integer.parseInt(campoAdi[18])));
				adi.setSituacao(sitService.findById(Integer.parseInt(campoAdi[19])));
				adiService.insertBackUp(adi);
				lineAdi = brAdi.readLine();
			}
			brAdi.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {	
			gravaRestaura();
		}
	}
	
	public void anos() {
		status = "Ok";
		status = "Ok";
		countAk = 0;
		file = "Anos";
		path = unid + meioSgo + file + ext;
		AnosService anoService = new AnosService();
		Anos ano = new Anos();
		try {BufferedReader brAno = new BufferedReader(new FileReader(path));
			String lineAno = brAno.readLine();
			while (lineAno != null) { 
				countAk += 1;
				crip = Cryptograf.desCriptografa(lineAno);
				String[] campoAno = crip.split(" , ");
				ano.setNumeroAnos(null);
				ano.setAnoAnos(Integer.parseInt(campoAno[1]));
				ano.setAnoStrAnos(campoAno[2]);
				anoService.saveOrUpdate(ano);
				lineAno = brAno.readLine();
			}
			brAno.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}
	
	public void balcao() throws ParseException {
		status = "Ok";
		file = "Balcao";
		path = unid + meioSgo + file + ext;
		FuncionarioService funService = new FuncionarioService();
		BalcaoService balService = new BalcaoService();
		countAk = 0;
		Balcao bal = new Balcao();
		try {BufferedReader brBal = new BufferedReader(new FileReader(path));
			String lineBal = brBal.readLine();
			while (lineBal != null) { 
				countAk += 1;
				crip = Cryptograf.desCriptografa(lineBal);
				String[] campoBal = crip.split(" , ");
				bal.setNumeroBal(Integer.parseInt(campoBal[0]));
				Date dtBal = sdfAno.parse(campoBal[1]);
				cal.setTime(dtBal);
				bal.setDataBal(cal.getTime());
				bal.setFuncionarioBal(campoBal[2]);
				bal.setDescontoBal(Double.parseDouble(campoBal[3]));
				bal.setTotalBal(Double.parseDouble(campoBal[4]));
				bal.setPagamentoBal(Integer.parseInt(campoBal[5]));
				Date dtPri = sdfAno.parse(campoBal[6]);
				cal.setTime(dtPri);
				bal.setDataPrimeiroPagamentoBal(cal.getTime());
				bal.setNnfBal(Integer.parseInt(campoBal[7]));
				bal.setObservacaoBal(campoBal[8]);
				bal.setMesBal(Integer.parseInt(campoBal[9]));
				bal.setAnoBal(Integer.parseInt(campoBal[10]));
				bal.setFuncionario(funService.findById(Integer.parseInt(campoBal[11])));
				balService.insertBackUp(bal);
				lineBal = brBal.readLine();
			}
			brBal.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}
	
	public void cargo() {
		status = "Ok";
		countAk = 0;
		file = "Cargo";
		path = unid + meioSgo + file + ext;
		Cargo car = new Cargo();
		CargoService carService = new CargoService();
		
		try {BufferedReader brCar = new BufferedReader(new FileReader(path));
		String lineCar = brCar.readLine();
		while (lineCar != null) { 
			countAk += 1;
			crip = Cryptograf.desCriptografa(lineCar);
			String[] campoCar = crip.split(" , ");
			car.setCodigoCargo(Integer.parseInt(campoCar[0]));
			car.setNomeCargo(campoCar[1]);
			car.setSalarioCargo(Double.parseDouble(campoCar[2]));
			car.setComissaoCargo(Double.parseDouble(campoCar[3]));
			carService.insertBackUp(car);
			lineCar = brCar.readLine();
		}
		brCar.close();
	}
	catch(	IOException e2) {
		status = "Er";
		throw new DbException(e2.getMessage());	 			
	}
	finally {					
		gravaRestaura();
	}
}
	
	public void cliente() {
		status = "Ok";
		file = "Cliente";
		path = unid + meioSgo + file + ext;
		Cliente cli = new Cliente();
		ClienteService cliService = new ClienteService();
		countAk = 0;
		try {BufferedReader brCli = new BufferedReader(new FileReader(path));
		String lineCli = brCli.readLine();
		while (lineCli != null) { 
			countAk += 1;
			crip = Cryptograf.desCriptografa(lineCli);
			String[] campoCli = crip.split(" , ");
			cli.setCodigoCli(Integer.parseInt(campoCli[0]));
			cli.setNomeCli(campoCli[1]);
			cli.setRuaCli(campoCli[2]);
			cli.setNumeroCli(Integer.parseInt(campoCli[3]));
			cli.setComplementoCli(campoCli[4]);
			cli.setBairroCli(campoCli[5]);
			cli.setCidadeCli(campoCli[6]);
			cli.setUfCli(campoCli[7]);
			cli.setCepCli(campoCli[8]);
			cli.setDdd01Cli(Integer.parseInt(campoCli[9]));
			cli.setTelefone01Cli(Integer.parseInt(campoCli[10]));
			cli.setDdd02Cli(Integer.parseInt(campoCli[11]));
			cli.setTelefone02Cli(Integer.parseInt(campoCli[12]));
			cli.setEmailCli(campoCli[13]);
			cli.setPessoaCli(campoCli[14].charAt(0));
			cli.setCpfCli(campoCli[15]);
			cli.setCnpjCli(campoCli[16]);
			cli.setValorClass(Double.parseDouble(campoCli[17]));
			cli.setPercentualClass(Double.parseDouble(campoCli[18]));
			cli.setLetraClass(campoCli[19].charAt(0));
			cli.setVisitaClass(Integer.parseInt(campoCli[20]));
			cliService.insertBackUp(cli);
			lineCli = brCli.readLine();
		}
		brCli.close();
	}
	catch(	IOException e2) {
		status = "Er";
		throw new DbException(e2.getMessage());	 			
	}
	finally {					
		gravaRestaura();
	}
}
		
	public void empresa() {
		status = "Ok";
		countAk = 0;
		file = "Empresa";
		path = unid + meioSgo + file + ext;
		EmpresaService empService = new EmpresaService();
		Empresa emp = new Empresa();
		try {BufferedReader brEmp = new BufferedReader(new FileReader(path));
			String lineEmp = brEmp.readLine();
			while (lineEmp != null) {
				countAk += 1;
				String crip = Cryptograf.desCriptografa(lineEmp);
				String[] campoEmp = crip.split(" , ");
				emp.setNumeroEmp(Integer.parseInt(campoEmp[0]));
				emp.setNomeEmp(campoEmp[1]);
				emp.setEnderecoEmp(campoEmp[2]);
				emp.setTelefoneEmp(campoEmp[3]);
				emp.setEmailEmp(campoEmp[4]);
				emp.setPixEmp(campoEmp[5]);
				
				empService.insertBackUp(emp);
				lineEmp = brEmp.readLine();
			}
			brEmp.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}
	
	public void entrada() throws ParseException {
		status = "Ok";
		countAk = 0;
		file = "Entrada";
		path = unid + meioSgo + file + ext;
		MaterialService matService = new MaterialService();
		FornecedorService forService = new FornecedorService();
		Entrada ent = new Entrada();
		EntradaService entService = new EntradaService();
		try {BufferedReader brEnt = new BufferedReader(new FileReader(path));
			String lineEnt = brEnt.readLine();
			while (lineEnt != null) {
				countAk += 1;
				String crip = Cryptograf.desCriptografa(lineEnt);
				String[] campoEnt = crip.split(" , ");
				ent.setNumeroEnt(Integer.parseInt(campoEnt[0]));
				ent.setNnfEnt(Integer.parseInt(campoEnt[1]));
				Date dtEnt = sdfAno.parse(campoEnt[2]);
				cal.setTime(dtEnt);
				ent.setDataEnt(cal.getTime());
				ent.setNomeFornEnt(campoEnt[3]);
				ent.setNomeMatEnt(campoEnt[4]);
				ent.setQuantidadeMatEnt(Double.parseDouble(campoEnt[5]));
				ent.setValorMatEnt(Double.parseDouble(campoEnt[6]));
				ent.setForn(forService.findById(Integer.parseInt(campoEnt[7])));
				ent.setMat(matService.findById(Integer.parseInt(campoEnt[8])));

				entService.insertBackUp(ent);
				lineEnt = brEnt.readLine();
			}
			brEnt.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}
	
	public void funcionario() {
		status = "ok";
		countAk = 0;
		file = "Funcionario";
		path = unid + meioSgo + file + ext;
		CargoService carService = new CargoService();
		SituacaoService sitService = new SituacaoService();
		Funcionario fun = new Funcionario();
		FuncionarioService funService = new FuncionarioService();
		
		try {BufferedReader brFun = new BufferedReader(new FileReader(path));
			String lineFun = brFun.readLine();
			while (lineFun != null) {
				countAk += 1;
				String crip = Cryptograf.desCriptografa(lineFun);
				String[] campoFun = crip.split(" , ");
				fun.setCodigoFun(Integer.parseInt(campoFun[0]));
				fun.setNomeFun(campoFun[1]);
				fun.setEnderecoFun(campoFun[2]);
				fun.setBairroFun(campoFun[3]);
				fun.setCidadeFun(campoFun[4]);
				fun.setUfFun(campoFun[5]);
				fun.setCepFun(campoFun[6]);
				fun.setDddFun(Integer.parseInt(campoFun[7]));
				fun.setTelefoneFun(Integer.parseInt(campoFun[8]));
				fun.setCpfFun(campoFun[9]);
				fun.setPixFun(campoFun[10]);
				fun.setComissaoFun(Double.parseDouble(campoFun[11]));
				fun.setAdiantamentoFun(Double.parseDouble(campoFun[12]));
				fun.setMesFun(Integer.parseInt(campoFun[13]));
				fun.setAnoFun(Integer.parseInt(campoFun[14]));
				fun.setCargoFun(campoFun[15]);
				fun.setSituacaoFun(campoFun[16]);
				fun.setSalarioFun(Double.parseDouble(campoFun[17]));
				fun.setCargo(carService.findById(Integer.parseInt(campoFun[18])));
				fun.setSituacao(sitService.findById(Integer.parseInt(campoFun[19])));
				funService.insertBackup(fun);
				lineFun = brFun.readLine();
			}
			brFun.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());
		}
		finally {			
			gravaRestaura();
		}
	}
	
	public void grupo() {
		status = "Ok";
		countAk = 0;
		file = "Grupo";
		path = unid + meioSgo + file + ext;
		Grupo gru = new Grupo();
		GrupoService gruService = new GrupoService();
		
		try { BufferedReader brGru = new BufferedReader(new FileReader(path));
			  String lineGru = brGru.readLine();
			  while (lineGru != null) {
				countAk += 1;
				String crip = Cryptograf.desCriptografa(lineGru);
				String[] campoGru = crip.split(" , ");
				gru.setCodigoGru(Integer.parseInt(campoGru[0]));
				gru.setNomeGru(campoGru[1]);
				gruService.insertBackUp(gru);
					lineGru = brGru.readLine();
			  }
				brGru.close();
			}
			catch(	IOException e2) {
				status = "Er";
				throw new DbException(e2.getMessage());	 			
			}
			finally {					
				gravaRestaura();
			}
		}
		
	@SuppressWarnings("null")
	public void login() throws ParseException {
		status = "Ok";
		file = "Login";
		path = unid + meioSgo + file + ext;
		Login log = new Login();
		LoginService logService = new LoginService();
		countAk = 0;
		try {BufferedReader brLog = new BufferedReader(new FileReader(path));
			String lineLog = brLog.readLine();
			while (lineLog != null) {
				countAk += 1;
				crip = Cryptograf.desCriptografa(lineLog);
				String[] campoLog = crip.split(" , ");
				log.setNumeroLog(Integer.parseInt(campoLog[0]));
				log = logService.findById(log.getNumeroLog());
				if (log == null) {
					log.setNumeroLog(Integer.parseInt(campoLog[0]));
					log.setSenhaLog(campoLog[1]);
					log.setNomeLog(campoLog[2]);
					log.setNivelLog(Integer.parseInt(campoLog[3]));
					log.setAlertaLog(Integer.parseInt(campoLog[4]));
					Date dtLog = sdfAno.parse(campoLog[5]);
					Date dtVen = sdfAno.parse(campoLog[6]);
					Date dtMax = sdfAno.parse(campoLog[7]);
					Date dtAce = sdfAno.parse(campoLog[8]);
					cal.setTime(dtLog);
					log.setDataLog(cal.getTime());
					cal.setTime(dtVen);
					log.setDataVencimentoLog(cal.getTime());
					cal.setTime(dtMax);
					log.setDataMaximaLog(cal.getTime());
					cal.setTime(dtAce);
					log.setAcessoLog(cal.getTime());
					log.setEmpresaLog(Integer.parseInt(campoLog[9]));
					if (log.getNumeroLog() == 1 || log.getNumeroLog() == 2) {
						logService.insertOrUpdate(log);
					} else { 
						logService.insertBackUp(log);
					}	
				}	
				lineLog = brLog.readLine();
			}
			brLog.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}
	
	public void material() throws ParseException {
		status = "Ok";
		countAk = 0;
		file = "Material";
		path = unid + meioSgo + file + ext;
		GrupoService gruService = new GrupoService();
		Material mat = new Material();
		MaterialService matService = new MaterialService();
		
		try { BufferedReader brMat = new BufferedReader(new FileReader(path));
		  String lineMat = brMat.readLine();
		  while (lineMat != null) {
			countAk += 1;
			String crip = Cryptograf.desCriptografa(lineMat);
			String[] campoMat = crip.split(" , ");

			mat.setCodigoMat(Integer.parseInt(campoMat[0]));
			mat.setGrupoMat(Integer.parseInt(campoMat[1]));
			mat.setNomeMat(campoMat[2]);
			mat.setEstMinMat(Double.parseDouble(campoMat[3]));
			mat.setSaldoMat(Double.parseDouble(campoMat[4]));
			mat.setSaidaCmmMat(Double.parseDouble(campoMat[5]));
			mat.setCmmMat(Double.parseDouble(campoMat[6]));
			mat.setPrecoMat(Double.parseDouble(campoMat[7]));
			mat.setVendaMat(Double.parseDouble(campoMat[8]));
			mat.setVidaKmMat(Integer.parseInt(campoMat[9]));
			mat.setVidaMesMat(Integer.parseInt(campoMat[10]));
			mat.setPercentualClass(Double.parseDouble(campoMat[11]));
			mat.setLetraClass(campoMat[12].charAt(0));
			Date dtMat = sdfAno.parse(campoMat[13]);
			cal.setTime(dtMat);
			mat.setDataCadastroMat(cal.getTime());
			mat.setGrupo(gruService.findById(Integer.parseInt(campoMat[14])));
			
			matService.insertBackUp(mat);
			lineMat = brMat.readLine();
		  }
			brMat.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}
	
	public void meses() {
		status = "Ok";
		file = "Meses";
		path = unid + meioSgo + file + ext;
		Meses mes = new Meses();
		MesesService mesService = new MesesService();
		countAk = 0;
		try { BufferedReader brMes = new BufferedReader(new FileReader(path));
		  String lineMes = brMes.readLine();
		  while (lineMes != null) {
			countAk += 1;
			String crip = Cryptograf.desCriptografa(lineMes);
			String[] campoMes = crip.split(" , ");
			mes.setNumeroMes(null);
			mes.setNomeMes(campoMes[1]);

			mesService.insert(mes);
			lineMes = brMes.readLine();
		  }
			brMes.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}
			
	
	public void notaFiscal() {
		status = "Ok";
		file = "NotaFiscal";
		path = unid + meioSgo + file + ext;
		NotaFiscal nf = new NotaFiscal();
		NotaFiscalService nfService = new NotaFiscalService();
		countAk = 0;
		try { BufferedReader brNf = new BufferedReader(new FileReader(path));
			String lineNf = brNf.readLine();
			while (lineNf != null) {
				countAk += 1;
				String crip = Cryptograf.desCriptografa(lineNf);
				String[] campoNf = crip.split(" , ");
				nf.setCodigoNF(Integer.parseInt(campoNf[0]));
				nf.setNumeroNF(Integer.parseInt(campoNf[1]));
				nf.setBalcaoNF(Integer.parseInt(campoNf[2]));
				nf.setOsNF(Integer.parseInt(campoNf[3]));
				nfService.insertBackUp(nf);
				lineNf = brNf.readLine();
			}
			brNf.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}

	public void orcamento() throws ParseException {
		status = "Ok";
		file = "Orcamento";
		path = unid + meioSgo + file + ext;
		ClienteService cliService = new ClienteService();
		FuncionarioService funService = new FuncionarioService(); 
		Orcamento orc = new Orcamento();
		OrcamentoService orcService = new OrcamentoService();
		countAk = 0;
		try { BufferedReader brOrc = new BufferedReader(new FileReader(path));
			String lineOrc = brOrc.readLine();
			while (lineOrc != null) {
				countAk += 1;
				String crip = Cryptograf.desCriptografa(lineOrc);
				String[] campoOrc = crip.split(" , ");

				orc.setNumeroOrc(Integer.parseInt(campoOrc[0]));
				Date dataOrc = sdfAno.parse(campoOrc[1]);
				cal.setTime(dataOrc);
				orc.setDataOrc(cal.getTime());
				orc.setClienteOrc(campoOrc[2]);
				orc.setFuncionarioOrc(campoOrc[3]);
				orc.setPlacaOrc(campoOrc[4]);
				orc.setKmInicialOrc(Integer.parseInt(campoOrc[5]));
				orc.setKmFinalOrc(Integer.parseInt(campoOrc[6]));
				orc.setDescontoOrc(Double.parseDouble(campoOrc[7]));
				orc.setTotalOrc(Double.parseDouble(campoOrc[8]));
				orc.setOsOrc(Integer.parseInt(campoOrc[9]));
				orc.setMesOrc(Integer.parseInt(campoOrc[10]));
				orc.setAnoOrc(Integer.parseInt(campoOrc[11]));
				orc.setCliente(cliService.findById(Integer.parseInt(campoOrc[12])));
				orc.setFuncionario(funService.findById(Integer.parseInt(campoOrc[13])));
				orcService.insertBackUp(orc);
				lineOrc = brOrc.readLine();
			}
			brOrc.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}

	public void virtual() {
		status = "Ok";
		file = "OrcVirtual";
		path = unid + meioSgo + file + ext;
		MaterialService matService = new MaterialService();
		OrcVirtual vir = new OrcVirtual();
		OrcVirtualService virService = new OrcVirtualService();
		countAk = 0;
		try { BufferedReader brVir = new BufferedReader(new FileReader(path));
			String lineVir = brVir.readLine();
			while (lineVir != null) {
				countAk += 1;
				String crip = Cryptograf.desCriptografa(lineVir);
				String[] campoVir = crip.split(" , ");
				vir.setNumeroVir(Integer.parseInt(campoVir[0]));
				vir.setNomeMatVir(campoVir[1]);
				vir.setQuantidadeMatVir(Double.parseDouble(campoVir[2]));
				vir.setPrecoMatVir(Double.parseDouble(campoVir[3]));
				vir.setTotalMatVir(Double.parseDouble(campoVir[4]));
				vir.setNumeroOrcVir(Integer.parseInt(campoVir[5]));
				vir.setNumeroBalVir(Integer.parseInt(campoVir[6]));
				vir.setMaterial(matService.findById(Integer.parseInt(campoVir[7])));
				vir.setCustoMatVir(Double.parseDouble(campoVir[8]));
				
				virService.insertBackUp(vir);
				lineVir = brVir.readLine();
			}
			brVir.close();
		}
		catch(RuntimeException e3) {
			status = "Er";
			throw new DbException(e3.getMessage());	 			
		}
		catch(IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}

	
	public void os() throws ParseException {
		status = "Ok";
		file = "OrdemServico";
		path = unid + meioSgo + file + ext;
		OrcamentoService orcService = new OrcamentoService();
		OrdemServico os = new OrdemServico();
		OrdemServicoService osService = new OrdemServicoService();
		countAk = 0;
		try { BufferedReader brOs = new BufferedReader(new FileReader(path));
			String lineOs = brOs.readLine();
			while (lineOs != null) {
				countAk += 1;
				String crip = Cryptograf.desCriptografa(lineOs);
				String[] campoOs = crip.split(" , ");

				os.setNumeroOS(Integer.parseInt(campoOs[0]));
				Date dtOs = sdfAno.parse(campoOs[1]);
				cal.setTime(dtOs);
				os.setDataOS(cal.getTime());
				os.setOrcamentoOS(Integer.parseInt(campoOs[2]));
				os.setPlacaOS(campoOs[3]);
				os.setClienteOS(campoOs[4]);
				os.setValorOS(Double.parseDouble(campoOs[5]));
				os.setParcelaOS(Integer.parseInt(campoOs[6]));
				os.setPrazoOS(Integer.parseInt(campoOs[7]));				
				os.setPagamentoOS(Integer.parseInt(campoOs[8]));
				Date dtPri = sdfAno.parse(campoOs[9]);
				cal.setTime(dtPri);
				os.setDataPrimeiroPagamentoOS(dtPri);
				os.setNnfOS(Integer.parseInt(campoOs[10]));
				os.setObservacaoOS(campoOs[11]);
				os.setKmOS(Integer.parseInt(campoOs[12]));
				os.setMesOs(Integer.parseInt(campoOs[13]));
				os.setAnoOs(Integer.parseInt(campoOs[14]));
				
				os.setOrcamento(orcService.findById(Integer.parseInt(campoOs[15])));
				osService.insertBackUp(os);
				lineOs = brOs.readLine();
			}
			brOs.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}

	
	public void receber() throws ParseException {
		status = "Ok";
		file = "Receber";
		path = unid + meioSgo + file + ext;
		ParPeriodoService perService = new ParPeriodoService();		
		Receber rec = new Receber();
		ReceberService recService = new ReceberService();
		arq = "";
		crip = "";
		countAk = 0;
		try {BufferedReader brRec = new BufferedReader(new FileReader(path));
			String lineRec = brRec.readLine();
			while (lineRec != null) {
				countAk += 1;
				String crip = Cryptograf.desCriptografa(lineRec);
				String[] campoRec = crip.split(" , ");
				rec.setNumeroRec(Integer.parseInt(campoRec[0]));
				rec.setFuncionarioRec(Integer.parseInt(campoRec[1]));
				rec.setClienteRec(Integer.parseInt(campoRec[2]));
				rec.setNomeClienteRec(campoRec[3]);
				rec.setOsRec(Integer.parseInt(campoRec[4]));
				Date dtOs = sdfAno.parse(campoRec[5]);
				cal.setTime(dtOs);
				rec.setDataOsRec(cal.getTime());
				rec.setPlacaRec(campoRec[6]);
				rec.setParcelaRec(Integer.parseInt(campoRec[7]));
				rec.setFormaPagamentoRec(campoRec[8]);
				rec.setValorRec(Double.parseDouble(campoRec[9]));
				Date dtVen = sdfAno.parse(campoRec[10]);
				cal.setTime(dtVen);
				rec.setDataVencimentoRec(cal.getTime());
				Date dtPag = sdfAno.parse(campoRec[11]);
				cal.setTime(dtPag);
				rec.setDataPagamentoRec(cal.getTime());
				rec.setPeriodo(perService.findById(Integer.parseInt(campoRec[12])));
				rec.setJurosRec(Double.parseDouble(campoRec[13]));
				rec.setDescontoRec(Double.parseDouble(campoRec[14]));
				rec.setTotalRec(Double.parseDouble(campoRec[15]));
				rec.setValorPagoRec(Double.parseDouble(campoRec[16]));
				
				recService.insertBackUp(rec);
				lineRec = brRec.readLine();
			}
			brRec.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}
	
	public void reposicao() throws ParseException {
		status = "Ok";
		file = "ReposicaoVeiculo";
		path = unid + meioSgo + file + ext;
		ReposicaoVeiculo rep = new ReposicaoVeiculo();
		ReposicaoVeiculoService repService = new ReposicaoVeiculoService();
		countAk = 0;
		try { BufferedReader brRep = new BufferedReader(new FileReader(path));
			String lineRep = brRep.readLine();
			while (lineRep != null) {
				countAk += 1;
				String crip = Cryptograf.desCriptografa(lineRep);
				String[] campoRep = crip.split(" , ");

				rep.setNumeroRep(null);
				rep.setOsRep(Integer.parseInt(campoRep[1]));
				Date dtRep = sdfAno.parse(campoRep[2]);
				cal.setTime(dtRep);
				rep.setDataRep(cal.getTime());
				rep.setPlacaRep(campoRep[3]);
				rep.setClienteRep(campoRep[4]);
				rep.setDddClienteRep(Integer.parseInt(campoRep[5]));
				rep.setTelefoneClienteRep(Integer.parseInt(campoRep[6]));
				rep.setCodigoMaterialRep(Integer.parseInt(campoRep[7]));
				rep.setMaterialRep(campoRep[8]);
				rep.setKmTrocaRep(Integer.parseInt(campoRep[9]));
				rep.setProximaKmRep(Integer.parseInt(campoRep[10]));
				Date dtPro = sdfAno.parse(campoRep[11]);
				cal.setTime(dtPro);
				rep.setProximaDataRep(cal.getTime());
				
				repService.insert(rep);
				lineRep = brRep.readLine();
			}
			brRep.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}
	
	public void situacao() {
		status = "Ok";
		file = "Situacao";
		path = unid + meioSgo + file + ext;
		Situacao sit = new Situacao();
		SituacaoService sitService = new SituacaoService();		
		countAk = 0;
		try { BufferedReader brSit = new BufferedReader(new FileReader(path));
			String lineSit = brSit.readLine();
			while (lineSit != null) {
				countAk += 1;
				String crip = Cryptograf.desCriptografa(lineSit);
				String[] campoSit = crip.split(" , ");
				
				sit.setNumeroSit(Integer.parseInt(campoSit[0]));
				sit.setNomeSit(campoSit[1]);
				
				sitService.insertBackUp(sit);
				lineSit = brSit.readLine();
			}
			brSit.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}
	
	public void veiculo() {
		status = "Ok";
		file = "Veiculo";
		path = unid + meioSgo + file + ext;
		Veiculo vei = new Veiculo();
		VeiculoService veiService = new VeiculoService();
		countAk = 0;
		try { BufferedReader brVei = new BufferedReader(new FileReader(path));
			String lineVei = brVei.readLine();
			while (lineVei != null) {
				countAk += 1;
				String crip = Cryptograf.desCriptografa(lineVei);
				String[] campoVei = crip.split(" , ");
				
				vei.setNumeroVei(Integer.parseInt(campoVei[0]));
				vei.setPlacaVei(campoVei[1]);
				vei.setKmInicialVei(Integer.parseInt(campoVei[2]));
				vei.setKmFinalVei(Integer.parseInt(campoVei[3]));
				vei.setModeloVei(campoVei[4]);
				vei.setAnoVei(Integer.parseInt(campoVei[5]));

				veiService.insertBackUp(vei);
				
				lineVei = brVei.readLine();
			}
			brVei.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}
	
	public void compromisso() throws ParseException {
		status = "Ok";
		file = "Compromisso";
		path = unid + meioSgo + file + ext;
		FornecedorService forService = new FornecedorService();
		TipoConsumoService tipoService = new TipoConsumoService();
		ParPeriodoService perService = new ParPeriodoService();
		Compromisso comp = new Compromisso();
		CompromissoService comService = new CompromissoService();
		countAk = 0;
		try { BufferedReader brCom = new BufferedReader(new FileReader(path));
			String lineCom = brCom.readLine();
			while (lineCom != null) {
				countAk += 1;
				String crip = Cryptograf.desCriptografa(lineCom);
				String[] campoCom = crip.split(" , ");
				
				comp.setIdCom(Integer.parseInt(campoCom[0]));
				comp.setCodigoFornecedorCom(Integer.parseInt(campoCom[1]));
				comp.setNomeFornecedorCom(campoCom[2]);
				comp.setNnfCom(Integer.parseInt(campoCom[3]));
				Date dtCom = sdfAno.parse(campoCom[4]);
				cal.setTime(dtCom);
				comp.setDataCom(cal.getTime());
				Date dtVen = sdfAno.parse(campoCom[5]);
				cal.setTime(dtVen);
				comp.setDataVencimentoCom(cal.getTime());
				comp.setValorCom(Double.parseDouble(campoCom[6]));
				comp.setParcelaCom(Integer.parseInt(campoCom[7]));
				comp.setPrazoCom(Integer.parseInt(campoCom[8]));
				comp.setFornecedor(forService.findById(Integer.parseInt(campoCom[9])));
				comp.setTipoConsumo(tipoService.findById(Integer.parseInt(campoCom[10])));
				comp.setParPeriodo(perService.findById(Integer.parseInt(campoCom[10])));
				
				comService.insertBackUp(comp);
				lineCom = brCom.readLine();
			}
			brCom.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}
	
	public void fornecedor() {
		status = "Ok";
		file = "Fornecedor";
		path = unid + meioSgo + file + ext;
		Fornecedor forn = new Fornecedor();
		FornecedorService forService = new FornecedorService();
		countAk = 0;
		try { BufferedReader brFor = new BufferedReader(new FileReader(path));
			String lineFor = brFor.readLine();
			while (lineFor != null) {
				countAk += 1;
				String crip = Cryptograf.desCriptografa(lineFor);
				String[] campoFor = crip.split(" , ");

				forn.setCodigo(Integer.parseInt(campoFor[0]));
				forn.setRazaoSocial(campoFor[1]);
				forn.setRua(campoFor[2]);
				forn.setNumero(Integer.parseInt(campoFor[3]));
				forn.setComplemento(campoFor[4]);
				forn.setBairro(campoFor[5]);
				forn.setCidade(campoFor[6]);
				forn.setUf(campoFor[7]);
				forn.setCep(campoFor[8]);
				forn.setDdd01(Integer.parseInt(campoFor[9]));
				forn.setTelefone01(Integer.parseInt(campoFor[10]));
				forn.setDdd02(Integer.parseInt(campoFor[11]));
				forn.setTelefone02(Integer.parseInt(campoFor[12]));
				forn.setContato(campoFor[13]);
				forn.setDddContato(Integer.parseInt(campoFor[14]));
				forn.setTelefoneContato(Integer.parseInt(campoFor[15]));
				forn.setEmail(campoFor[16]);
				forn.setPix(campoFor[17]);
				forn.setObservacao(campoFor[18]);
				forn.setPrazo(Integer.parseInt(campoFor[19]));
				forn.setParcela(Integer.parseInt(campoFor[20]));
				
				forService.insertBackUp(forn);
				lineFor = brFor.readLine();
			}
			brFor.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}
		
	public void parcela() throws ParseException {
		status = "Ok";
		file = "Parcela";
		path = unid + meioSgo + file + ext;
		FornecedorService forService = new FornecedorService();
		TipoConsumoService tipoService = new TipoConsumoService();
		ParPeriodoService perService = new ParPeriodoService();
		Parcela par = new Parcela();
		ParcelaService parService = new ParcelaService();
		countAk = 0;
		try { BufferedReader brPar = new BufferedReader(new FileReader(path));
			String linePar = brPar.readLine();
			while (linePar != null) {
				countAk += 1;
				String crip = Cryptograf.desCriptografa(linePar);
				String[] campoPar = crip.split(" , ");

				par.setIdPar(Integer.parseInt(campoPar[0]));
				par.setCodigoFornecedorPar(Integer.parseInt(campoPar[1]));
				par.setNomeFornecedorPar(campoPar[2]);
				par.setNnfPar(Integer.parseInt(campoPar[3]));
				par.setNumeroPar(Integer.parseInt(campoPar[4]));
				Date dtVen = sdfAno.parse(campoPar[5]);
				cal.setTime(dtVen);
				par.setDataVencimentoPar(cal.getTime());
				par.setValorPar(Double.parseDouble(campoPar[6]));
				par.setDescontoPar(Double.parseDouble(campoPar[7]));
				par.setJurosPar(Double.parseDouble(campoPar[8]));
				par.setTotalPar(Double.parseDouble(campoPar[9]));
				par.setPagoPar(Double.parseDouble(campoPar[10]));
				Date dtPag = sdfAno.parse(campoPar[11]);
				cal.setTime(dtPag);
				par.setDataPagamentoPar(cal.getTime());
				par.setFornecedor(forService.findById(Integer.parseInt(campoPar[12])));
				par.setTipoConsumo(tipoService.findById(Integer.parseInt(campoPar[13])));
				par.setPeriodo(perService.findById(Integer.parseInt(campoPar[14])));
				
				parService.insertBackUp(par);
				linePar = brPar.readLine();
			}
			brPar.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}
		
	
	public void periodo() throws ParseException {
		status = "Ok";
		file = "ParPeriodo";
		path = unid + meioSgo + file + ext;
		FornecedorService forService = new FornecedorService();
		TipoConsumoService tipoService = new TipoConsumoService();
		ParPeriodo per = new ParPeriodo();
		ParPeriodoService perService = new ParPeriodoService();
		
		countAk = 0;
		try { BufferedReader brPer = new BufferedReader(new FileReader(path));
			String linePer = brPer.readLine();
			while (linePer != null) {
				countAk += 1;
				String crip = Cryptograf.desCriptografa(linePer);
				String[] campoPer = crip.split(" , ");
				per.setIdPeriodo(Integer.parseInt(campoPer[0]));
				Date dti = sdfAno.parse(campoPer[1]);
				cal.setTime(dti);
				per.setDtiPeriodo(cal.getTime());
				Date dtf = sdfAno.parse(campoPer[2]);
				cal.setTime(dtf);
				per.setDtfPeriodo(cal.getTime());
				per.setFornecedor(forService.findById(Integer.parseInt(campoPer[3])));
				per.setTipoConsumo(tipoService.findById(Integer.parseInt(campoPer[4])));
				perService.update(per);
				linePer = brPer.readLine();
			}
			brPer.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}
			
	public void tipo() {
		status = "Ok";
		file = "TipoConsumidor";
		path = unid + meioSgo + file + ext;
		TipoConsumo tipo = new TipoConsumo();
		TipoConsumoService tipoService = new TipoConsumoService();
		countAk = 0;
		try { BufferedReader brTp = new BufferedReader(new FileReader(path));
			String lineTp = brTp.readLine();
			while (lineTp != null) {
				countAk += 1;
				String crip = Cryptograf.desCriptografa(lineTp);
				String[] campoTp = crip.split(" , ");

				tipo.setCodigoTipo(Integer.parseInt(campoTp[0]));
				tipo.setNomeTipo(campoTp[1]);

				tipoService.insertBackUp(tipo);
				lineTp = brTp.readLine();
			}
			brTp.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}
		
	
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}

 	private void initializeNodes() {
		tableColumnFile.setCellValueFactory(new PropertyValueFactory<>("FileUp"));
		tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("StatusUp"));
		tableColumnCount.setCellValueFactory(new PropertyValueFactory<>(String.valueOf("CountUp")));
 		Stage stage = (Stage) MainSgo.getMainScene().getWindow();
 		tableViewRestaura.prefHeightProperty().bind(stage.heightProperty());
 	}

 	public void updateTableView() {
 		if (service == null) {
			throw new IllegalStateException("Serviço está vazio");
 		}
		List<Restaura> listR = service.findAll();
 		labelUser.setText(user);
 		labelFile.setText("<<<aguarde>>>");
 		labelCount.setText(String.valueOf(count));
  		obsList = FXCollections.observableArrayList(listR);
  		tableViewRestaura.setItems(obsList);
	}

	private void notifyDataChangeListerners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	@Override
	public void onDataChanged() {
		initializeNodes();
	}
	
 	public void gravaRestaura() {
		entity.setIdRestauraUp(null);
		entity.setFileUp(file);
		entity.setStatusUp(status);
		entity.setCountUp(countAk);
		service.saveOrUpdate(entity);
		count += 1;
		labelCount.setText(String.valueOf(count));
		labelCount.viewOrderProperty();
		notifyDataChangeListerners();
		updateTableView();
 	}
 	
	private void createDialogForm(String absoluteName, Stage parentStage) {
        String classe = "BackUp SGO ";
		try {
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			RestauraSgoFormController controller = loader.getController();
			controller.loadAssociatedObjects();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Selecione unid para Restaurar                 ");
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
