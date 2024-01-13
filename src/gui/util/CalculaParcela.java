package gui.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import gui.sgcpmodel.entities.Compromisso;
import gui.sgcpmodel.entities.Parcela;
import gui.sgcpmodel.services.ParcelaService;
import javafx.scene.control.Alert.AlertType;

public class CalculaParcela {

	String classe = "Parcela Create";
	static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	public static Date CalculaVencimentoDia(Date data, int parcela, int dia) {
		Date dataVen = data;
		if (dia > 1 ) {
			dataVen = DataStatic.somaDiasDate(data, (parcela * dia));
		}	
		return dataVen;
	}
	
	public static Date CalculaVencimentoMes(Date data, int mes) {
		Date dataVen = DataStatic.somaMesDate(data, mes);
		return dataVen;
	}
	
/*
 * r = resto
 * a = parcela sem resto
 * b = conferindo total
 *     se tem resto
 * c = valor da parcela
 * d = se resto > 0, parcela mais resto
 */
	
	public static Double calculaParcelas(Double valor, int qtd, int parc) {
		double r = valor % qtd;
		double a = (valor - r) / qtd;
		double b = a;
		if (a != valor) {
			b += r;
		}	
		if (parc == 1) {
		 	valor = b;
		} else {
			valor = a;
		}
		return valor;
	}

	public static void parcelaCreate(Compromisso objCom) {
  		ParcelaService parService = new ParcelaService();
  		List<Parcela> list = parService.findByIdFornecedorNnf(objCom.getCodigoFornecedorCom(), objCom.getNnfCom());
  		for (Parcela p : list) {
  			if (p.getIdPar() != null) {
				parService.removeNnf(objCom.getNnfCom(), objCom.getCodigoFornecedorCom());
  			}
  		}	

  		Calendar cal = Calendar.getInstance(); 
   		if (objCom.getDataVencimentoCom().after(objCom.getDataCom())) {
   			cal.setTime(objCom.getDataVencimentoCom());
   		} else {
   			cal.setTime(objCom.getDataCom());
   		}
   		
   		if (objCom.getDataCom().after(objCom.getDataVencimentoCom())) {
   			Alerts.showAlert("Data inválida!!!", "vencimento maior que data da operação", null, AlertType.ERROR);   			
   		} else {
   			for (int i = 1; i < objCom.getParcelaCom() + 1; i ++) {
   				if (objCom.getDataCom().equals(objCom.getDataVencimentoCom())) {
   					if (objCom.getPrazoCom() > 1) {
   						cal.add(Calendar.DAY_OF_MONTH, (objCom.getPrazoCom()));   					
   					}
   				}
   				if (objCom.getDataCom().before(objCom.getDataVencimentoCom())) {
   					if (objCom.getParcelaCom() > 1 && i > 1) {
   						cal.add(Calendar.DAY_OF_MONTH, (objCom.getPrazoCom()));   					
   					}
   				}
   				Parcela parcela = new Parcela();
   				parcela.setIdPar(null);
   				parcela.setCodigoFornecedorPar(objCom.getCodigoFornecedorCom());
   				parcela.setNomeFornecedorPar(objCom.getNomeFornecedorCom());
   				parcela.setNnfPar(objCom.getNnfCom());
   				parcela.setNumeroPar(i);
   				parcela.setDataVencimentoPar(cal.getTime());
   				parcela.setValorPar(calculaParcelas(objCom.getValorCom(), objCom.getParcelaCom(), i ));
   				parcela.setDescontoPar(0.00);
   				parcela.setJurosPar(0.00);
   				parcela.setTotalPar(parcela.getValorPar()); 
   				parcela.setPagoPar(0.00);
   				if (objCom.getPrazoCom() == 1 || objCom.getParcelaCom() == 1) {
   	   				if (objCom.getDataCom().equals(parcela.getDataVencimentoPar())) {
   						parcela.setPagoPar(objCom.getValorCom());
   					}	
   				}
   				parcela.setDataPagamentoPar(cal.getTime());
   				parcela.setFornecedor(objCom.getFornecedor());
   				parcela.setTipoConsumo(objCom.getTipoConsumo());
   				parcela.setPeriodo(objCom.getParPeriodo());
   				cal.setTime(parcela.getDataVencimentoPar());
   				parService.saveUpdate(parcela);
   			}	
   		}
	}
}
