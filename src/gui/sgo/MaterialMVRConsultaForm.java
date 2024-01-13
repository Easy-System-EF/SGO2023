package gui.sgo;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import gui.listerneres.DataChangeListener;
import gui.sgomodel.entities.Cliente;
import gui.sgomodel.entities.Material;
import gui.sgomodel.services.MaterialService;
import javafx.fxml.Initializable;

public class MaterialMVRConsultaForm implements Initializable, DataChangeListener {

	private MaterialService matService;

// auxiliar
	String classe = "ClienteMRV List ";
	public String user = "usuário";
	Double clienteValorRecebido = 0.00;
	Double clientePercAcumulado = 0.00;
	Double materialCusto = 0.00;
	double acum = 0.0;
	double perc = 0.0;

	public void setMaterialMVRService(MaterialService matService) {
		this.matService = matService;
	}

	Material mat = new Material();
	List<Material> listM = new ArrayList<>();
	Cliente cli = new Cliente();
	List<Cliente> listC = new ArrayList<>();
		
	public void materialCustoEstoque() {
		listM = matService.findAll();
//		listM.sort((m1, m2) -> m1.getNomeMat().toUpperCase().compareTo(m2.getNomeMat().toUpperCase()));
		listM.removeIf(x -> x.getPrecoMat() == 0.0);
		listM.removeIf(x -> x.getVendaMat() == 0.0);
		listM.removeIf(x -> x.getSaldoMat() == 0.0);
		listM.removeIf(x -> x.getGrupo().getNomeGru().equals("Serviço") || x.getGrupo().getNomeGru().equals("Servico"));
		listM.removeIf(x -> x.getGrupo().getNomeGru().equals("Mão de obra") || x.getGrupo().getNomeGru().equals("Mao de obra"));
//		listM.forEach(System.out::println);
		for (Material m0 : listM) {
				materialCusto += m0.getPrecoMat();				
		}
	}
	
	public void materialPercentual() {
		listM = matService.findABC();
		listM.removeIf(x -> x.getPrecoMat() == 0.0);
		listM.removeIf(x -> x.getVendaMat() == 0.0);
		listM.removeIf(x -> x.getSaldoMat() == 0.0);
		listM.removeIf(x -> x.getGrupo().getNomeGru().equals("Serviço") || x.getGrupo().getNomeGru().equals("Servico"));
		listM.removeIf(x -> x.getGrupo().getNomeGru().equals("Mão de obra") || x.getGrupo().getNomeGru().equals("Mao de obra"));
				
		listM.forEach(m -> { 	
			m.setPercentualMat(0.0);
			m.setPercentualMat(((m.getVendaMat() - m.getPrecoMat()) * 100) / m.getPrecoMat());
			matService.saveOrUpdate(m);
		});
	}

	public void materialClassifica() {
		listM = matService.findABC();
		listM.removeIf(x -> x.getSaldoMat() == 0.0);
		listM.removeIf(x -> x.getPercentualMat() == 0.0);
		listM.removeIf(x -> x.getGrupo().getNomeGru().equals("Serviço") || x.getGrupo().getNomeGru().equals("Servico"));
		listM.removeIf(x -> x.getGrupo().getNomeGru().equals("Mão de obra") || x.getGrupo().getNomeGru().equals("Mao de obra"));
		listM.forEach(m -> m.setLetraMat(' '));
		listM.forEach(m -> {
			m.setLetraMat(' ');
			perc = ((m.getPrecoMat() * 100) / materialCusto);
			acum += perc;
			if (acum < 20.01) {
				m.setLetraMat('C');
			}
			if (acum > 20.00 && acum < 80.01) {
				m.setLetraMat('B');
			}
			if (acum > 80.00) {
				m.setLetraMat('A');
			}
			matService.saveOrUpdate(m);
		});
	}

	@Override
	public void onDataChanged() {
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}
}
