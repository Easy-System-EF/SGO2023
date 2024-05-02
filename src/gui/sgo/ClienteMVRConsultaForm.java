package gui.sgo;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import gui.listerneres.DataChangeListener;
import gui.sgomodel.entities.Cliente;
import gui.sgomodel.services.ClienteService;
import gui.sgomodel.services.ReceberService;
import gui.util.Maria;
import javafx.fxml.Initializable;

public class ClienteMVRConsultaForm implements Initializable, DataChangeListener {

// inje��o de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private ClienteService cliService;

	/*
	 * incl moka material - produto ak renomear materia p/ mat alguma coisa renomear
	 * produto para material c/ tds os metodos - entra sai ........
	 */
// auxiliar
	String classe = "ClienteMRV List ";
	public String user = "usuário";
	Double clienteValorRecebido = 0.00;
	Double clientePercAcumulado = 0.00;
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

	Cliente cli = new Cliente();
	List<Cliente> listC = new ArrayList<>();
	
	public void clienteSomaReceber() {
		
		ReceberService service = new ReceberService();
		LocalDate dt1 = Maria.criaAnoMesDia(2000, 01, 01);
		Date dti = Maria.localParaDateFormatada(dt1);
		LocalDate dt2 = Maria.criaLocalAtual();
		Date dtf = Maria.localParaDateFormatada(dt2);
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
		listC.removeIf(x -> x.getValorClass() == 0.0);
		
		listC.forEach(c -> {
			c.setValorClass(0.0);
			c.setVisitaClass(c.getVisitaClass() * - 1);
			c.setPercentualClass(0.0);
			c.setLetraClass(' ');
			c.sumTotalReceberCli();
			if (c.getValorClass() > 0) {
				cliService.saveOrUpdate(c);
			}				
		});		
	}

	public void clientePercentual() {
		listC = cliService.findABC();
		listC.removeIf(c -> c.getValorClass() == 0.00);
		listC.forEach(c -> {
			c.setPercentualClass((c.getValorClass() * 100) / clienteTotalReceberOs);
			c.setVisitaClass(1);
			cliService.saveOrUpdate(c);
		});
	}

	public void clienteClassifica() {
		listC = cliService.findMVR();
//		listC.sort((c1, c2) -> - c1.getPercentualClass().compareTo(c2.getPercentualClass()));
		listC.removeIf(x -> x.getValorClass() == 0);
		listC.forEach(c -> {
			c.setLetraClass(' ');
			clientePercentualAcum += c.getPercentualClass();
			if (clientePercentualAcum < 20.01) {
				c.setLetraClass('C');
			}	
			if (clientePercentualAcum > 20 && clientePercentualAcum < 80.01) {
					c.setLetraClass('B');
			} 
			if (clientePercentualAcum > 80) {
					c.setLetraClass('A');
			}
			cliService.saveOrUpdate(c);
		});
	}

	@Override
	public void onDataChanged() {
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}
}
