package gui.sgo; 

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

import gui.sgomodel.entities.Adiantamento;
import gui.sgomodel.entities.Empresa;
import gui.sgomodel.services.EmpresaService;
import gui.util.Extenso;
import gui.util.Imprimir;
import javafx.fxml.Initializable;

public class AdiantamentoImprimeController implements Initializable, Serializable {

	private static final long serialVersionUID = 1L;
	
//	private static String COURIER;
//	private static Font getFont(String COURIER, float size) {
//		return null;
//	}

	Locale localeBr = new Locale("pt", "BR");
 	DecimalFormat df = new DecimalFormat("#,###,##0.00"); 
	Calendar cal = Calendar.getInstance();

	String tabelaMeses = "Janeiro,Fevereiro,Março,Abril,Maio,Junho,Julho,Agosto,Setembro,Outubro,Novembro,Dezembro";
 	String mes = "";
  	String path = "c:\\Dataprol\\WINPRINT\\vale.prn";
	String pathI = "C:\\Arqs\\impr\\vale.txt";
	String comp = " ";
	String extso = "";
	String extso1 = "";
	String extso2 = "";
	String nomeFirma = "";
	int diaHj = 0;
	int mesHj = 0;
	int anoHj = 0;
	int tam = 0;

 	public static Integer numEmp = null;

	Adiantamento adianto = new Adiantamento();
	Empresa empresa = new Empresa();
	EmpresaService empService = new EmpresaService();

	String linha01 = "                              R E C I B O";
	String linha02 = "          Recebi de xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ";
	String linha03 = "o valor de R$9.999,99, referente a adiantamento nesta data.";
	String linha4a = "";
	String linha4b = "";
	String linha05 = "                         _________________________";
	String linha06 = "";
	String linha07 = "";
	String linhaBranco = "                                                               ";
	String margem = "  ";

	public void setAdiantamento(Adiantamento obj) {
		this.adianto = obj;
	}
	public void setServices(EmpresaService empService) {
		this.empService = empService;
	}
	
//	@FXML
	public void imprimeAdiantamento() {
		empresa = empService.findById(numEmp);
		nomeFirma = empresa.getNomeEmp();
  		grava();
  		Imprimir.relatorio(path);
   	}

	public void grava() {
	 		try
// 			{	OutputStreamWriter bwP = new OutputStreamWriter(
// 					new FileOutputStream(path), "UTF-8");
 			{	BufferedWriter bwP = new BufferedWriter(new FileWriter(path));
						cal.setTime(adianto.getDataAdi());
 						diaHj = cal.get(Calendar.DAY_OF_MONTH);
 						mesHj = cal.get(Calendar.MONTH);
 						anoHj = cal.get(Calendar.YEAR);
 						String[] meses = tabelaMeses.split(",");
 						mes = meses[mesHj];
 						String dfVlr = df.format(adianto.getAdiantamentoAdi()); 
 						tam = nomeFirma.length();
 						Asteriscos(41, tam);
 						linha02 = String.format("%s%s%s%s%s",   margem,
 								"          Recebi de ", nomeFirma, comp, ","); 
 						linha03 = String.format("%s%s%s%s", margem, 
 								"o valor de R$", dfVlr, ", referente a adiantamento nesta data.");
 						int parou = 0;
 						int i = 0;
 					    extso =  Extenso.valorPorExtenso(adianto.getAdiantamentoAdi());
 						tam = extso.length();
 						if (tam < 63) {
						    Asteriscos(59, extso.length());
 	 						linha4a = String.format("%s%s%s%s%s%s", margem, 
 	 								"(", extso, comp, ")", ".");
 						}
 						if (tam > 62) {
 							while (parou < 1) {
 							    String[] ext = extso.split(" ");
						    	if (ext[i].contains("centavo")) {
						    		parou += 1;
						    	}
						    	if (ext[i].contains("centavos")) {
						    		parou += 1;
						    	}
 							    if (extso1.length() <= 55) {
 							    	extso1 += ext[i] + " ";
 							    } else {	
 							    	extso2 += ext[i] + " ";
 							    }	
 							    if (parou == 0) {
 							    	i += 1; 							    	
 							    }	
 							}
 	 							Asteriscos(60, extso1.length());
 	 	 						linha4a = String.format("%s%s%s%s%s", margem, 
 	 	 								"(", extso1, comp, ",");
 							    Asteriscos(60, extso2.length());
 	 	 						linha4b = String.format("%s%s", margem, extso2) +
 	 	 								  String.format("%s", comp) +
 	 	 								  String.format("%s%s", ")", ".");
 						}	
 						linha06 = String.format("%s%s%s", margem, 
 								"                         ", adianto.getNomeFunAdi());
 						linha07 = String.format
 	 							("%s%s%d%s%s%s%d", margem, "          Contagem, ", 
 	 									diaHj, " de ", mes, " de ", anoHj);
 						bwP.write(linhaBranco);
 						bwP.newLine();
 						bwP.write(linhaBranco);
 						bwP.newLine();
						linha01 = acentos(linha01);
  						bwP.write(linha01);
 						bwP.newLine();
 						bwP.write(linhaBranco);
 						bwP.newLine();
						linha02 = acentos(linha02);
 						bwP.write(linha02);
 						bwP.newLine();
						linha03 = acentos(linha03);
 						bwP.write(linha03);
 						bwP.newLine();
						linha4a = acentos(linha4a);
 						bwP.write(linha4a);
 						bwP.newLine();
						linha4b = acentos(linha4b);
 						bwP.write(linha4b);
						bwP.newLine();
 						bwP.write(linhaBranco);
						bwP.newLine();
 						bwP.write(linhaBranco);
						bwP.newLine();
						linha05 = acentos(linha05);
						bwP.write(linha05);
						bwP.newLine();
						linha06 = acentos(linha06);
						bwP.write(linha06);
						bwP.newLine();
 						bwP.write(linhaBranco);
						bwP.newLine();
 						bwP.write(linhaBranco);
						bwP.newLine();
						linha07 = acentos(linha07);
						bwP.write(linha07);
						bwP.close();
	 			}	
 	 			catch(IllegalArgumentException e1) {
	 				System.out.println(e1.getMessage());
	 			}	catch(	IOException e2) {
	 				System.out.println(e2.getMessage());	 			
	 			}
	 		}

	public String Asteriscos(int lh, int tam) {
		comp = " ";
		int limite = lh - tam;
		for (int i = 1 ; i < limite ; i ++) {
			comp = comp + "*";
		}
		return comp;
	}
	
 	public static String acentos(String str) {
		str = str.replace("â", "a");
		str = str.replace("ã", "a");
		str = str.replace("á", "a");
		str = str.replace("Á", "A");
		str = str.replace("Â", "A");
		str = str.replace("Ã", "A");
		str = str.replace("é", "e");
		str = str.replace("ê", "e");
		str = str.replace("É", "E");
		str = str.replace("Ê", "E");
		str = str.replace("í", "i");
		str = str.replace("Í", "I");
		str = str.replace("ó", "o");
		str = str.replace("ô", "o");
		str = str.replace("õ", "o");
		str = str.replace("Ó", "o");
		str = str.replace("Ô", "o");
		str = str.replace("Õ", "o");
		str = str.replace("ú", "u");
		str = str.replace("Ú", "u");
		str = str.replace("ç", "c");
		str = str.replace("Ç", "C");
		return str;
 	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}

}
