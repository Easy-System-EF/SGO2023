package gui.sgo;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import gui.listerneres.DataChangeListener;
import gui.sgomodel.entities.Cliente;
import gui.sgomodel.entities.Material;
import gui.sgomodel.services.ClienteService;
import gui.sgomodel.services.MaterialService;
import gui.sgomodel.services.ReceberService;
import gui.util.DataStatic;
import javafx.fxml.Initializable;

public class MVRConsultaForm implements Initializable, DataChangeListener {

// inje��o de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private ClienteService cliService;
	private MaterialService matService;

	/*
	 * incl moka material - produto ak renomear materia p/ mat alguma coisa renomear
	 * produto para material c/ tds os metodos - entra sai ........
	 */
// auxiliar
	String classe = "ClienteMRV List ";
	public String user = "usuário";
	Double clienteValorRecebido = 0.00;
	Double clientePercAcumulado = 0.00;
	Double materialCusto = 0.00;
	public static Double clientePercentualBalcao = 0.00;
	public static double clientePercentualOs = 0.00;;
	public static double clientePercentualAcum = 0.00;;
	public static char clienteLetraBalcao = ' ';
	public static Double clienteTotalReceberOs = 0.00;
	public static Double clienteTotalReceberBal = 0.00;
	public static Double clienteTotalReceber = 0.00;
	public static char clienteLetraOs = ' ';

// injeta a dependencia com set (invers�o de controle de inje�ao)	
	public void setClienteMVRService(ClienteService cliService) {
		this.cliService = cliService;
	}

	public void setMaterialMVRService(MaterialService matService) {
		this.matService = matService;
	}

	Material mat = new Material();
	List<Material> listM = new ArrayList<>();
	Cliente cli = new Cliente();
	List<Cliente> listC = new ArrayList<>();
	
	public void clienteSomaReceber() {
		ReceberService service = new ReceberService();
		LocalDate dt1 = DataStatic.criaAnoMesDia(2000, 01, 01);
		Date dti = DataStatic.localParaDateFormatada(dt1);
		LocalDate dt2 = DataStatic.criaLocalAtual();
		Date dtf = DataStatic.localParaDateFormatada(dt2);
		clienteTotalReceberBal = service.findPagoBalMes(dti, dtf);
		clienteTotalReceberOs = service.findPagoOsMes(dti, dtf);
		clienteTotalReceber = clienteTotalReceberOs + clienteTotalReceberBal;
	}

	public void clienteMontaBalcao() {
		clientePercentualBalcao = (clienteTotalReceberBal * 100) / clienteTotalReceber;
	}

	public void clienteMontaOs() {
		clientePercentualOs = (clienteTotalReceberOs * 100) / clienteTotalReceber;
	}

	public void clienteSomaTotal() {
		listC = cliService.findAll();
//		listC.sort((c1, c2) -> c1.getNomeCli().toUpperCase().compareTo(c2.getNomeCli().toUpperCase()));
//		listC.forEach(System.out::println);		
		for (Cliente c : listC) {
			c.setValorClass(0.00);
			c.setVisitaClass(c.getVisitaClass() * -1);
			c.setPercentualClass(0.00);
			c.setLetraClass(' ');
			c.sumTotalReceberCli();
			if (c.getValorClass() > 0) {
				cliService.saveOrUpdate(c);
			}	
		}
	}

	public void clientePercentual() {
		listC = cliService.findABC();
		listC.removeIf(c -> c.getValorClass() == 0.00);
		for (Cliente c : listC) {
			c.percClassCli();
			c.setVisitaClass(1);
			cliService.saveOrUpdate(c);
		}
	}

	public void clienteClassifica() {
		listC = cliService.findMVR();
//		listC.sort((c1, c2) -> - c1.getPercentualClass().compareTo(c2.getPercentualClass()));
//		listC.removeIf(x -> x.getPercentualClass() == 0);
//		listC.removeIf(x -> x.getValorClass() == 0);
//		listC.forEach(c -> c.setLetraClass(' '));
		for (Cliente c : listC) {
			c.letraClassCli();
			cliService.saveOrUpdate(c);
		}
	}

	/*
	 * MATERIAL
	 */
	
	public void materialCustoEstoque() {
		listM = matService.findAll();
//		listM.sort((m1, m2) -> m1.getNomeMat().toUpperCase().compareTo(m2.getNomeMat().toUpperCase()));
		listM.removeIf(x -> x.getPrecoMat() == 0);
		listM.removeIf(x -> x.getVendaMat() == 0);
		listM.removeIf(x -> x.getSaldoMat() == 0);
//		listM.forEach(System.out::println);
		for (Material m0 : listM) {
//			if (p0.getPrecoMat() > 0.00 && p0.getVendaMat() > 0.00 && p0.getSaldoMat() > 0) {
				materialCusto += m0.getPrecoMat();				
//			}	
		}
	}
	
	public void materialPercentual() {
		listM = matService.findABC();
//		listM.sort((m1, m2) -> m1.getNomeMat().toUpperCase().compareTo(m2.getNomeMat().toUpperCase()));
//		listM.forEach(System.out::println);
//		List<Material> listMat1 = matService.findAll();
		listM.removeIf(x -> x.getPrecoMat() == 0);
		listM.removeIf(x -> x.getVendaMat() == 0);
		listM.removeIf(x -> x.getSaldoMat() == 0);
		for (Material m : listM) {
			m.calculaPercentual();
			matService.saveOrUpdate(m);
		}
	}

	public void materialClassifica() {
		double acum = 0.00;
		listM = matService.findABC();
//		listM.sort((m1, m2) -> m1.getPrecoMat().compareTo(m2.getPrecoMat()));
		listM.removeIf(x -> x.getSaldoMat() == 0);
		listM.removeIf(x -> x.getPercentualClass() == 0);
		listM.forEach(m -> m.setLetraClass(' '));
//		List<Material> listMat2 = matService.findABC();
		for (Material m2 : listM) {
			acum = m2.letraClassMat(acum, materialCusto);
			matService.saveOrUpdate(m2);
		}	
	}

	@Override
	public void onDataChanged() {
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
}
