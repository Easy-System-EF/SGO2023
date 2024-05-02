package gui.sgcp; 

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import gui.sgcpmodel.entities.Parcela;
import gui.sgcpmodel.services.ParcelaService;
import gui.sgomodel.entities.Empresa;
import gui.sgomodel.services.EmpresaService;
import gui.util.Imprimir;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class ParcelaImprimeAbertoController implements Initializable, Serializable {

	private static final long serialVersionUID = 1L;
	
 	public static Integer numEmp = null; 
 	
	Locale localeBr = new Locale("pt", "BR");
 	DecimalFormat df = new DecimalFormat("#,###,##0.00"); 
 	DecimalFormat dff = new DecimalFormat("00"); 
 	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	Date data1 = new Date();
	String dtHj = sdf.format(data1);
	String dtpI;
	String dtpF;
	String dtv;
 	String nomeCab = null;
 
	String path = "c:\\Dataprol\\WINPRINT\\_aPagar.prn";
	String pathI = "C:\\Arqs\\impr\\aPagar.txt";

	int contf = 0;
	int contl = 90;
	int i = 0;
	int flag = 0;
	int tam = 0;

	double totVlr = 0.00;
	double totDes = 0.00;
	double totJur = 0.00;
	double totTot = 0.00;

	Parcela parcela = new Parcela();
	private ParcelaService parService;
	private Empresa empresa;
	private EmpresaService empService;
 	private char opcao;
	private Integer codFor;
	private Integer codTipo;

	String nomeEmp = null;
	String linha01 = "";
	String linha02 = "";
	String linha03 = "******************************************************************"; 
	String linha04 = "Fornecedor                                  NNF Parcela Vencimento";
	String linha41 = "         Valor         Desconto            Juros             Total";
	String linha05 = "";
	String linha51 = "";
	String linha52 = "";
	String linhaNada = "";

	List<Parcela> list = new ArrayList<>();
 	
   	char setOpcao(char letra) {
 		return opcao = letra;
 	}
  	int setFor(Integer num) {
 		return codFor = num;
 	}  	
  	int setcodTipo(Integer num) {
 		return codTipo = num;
 	}
  	
  	
	public void setParcela(Parcela parcela) {
		this.parcela = parcela;
	}
	public void setServices(ParcelaService parService, EmpresaService empService) {
		this.parService = parService;
		this.empService = empService;
	}
	
	@FXML
	public void onBtImprimeParcelaAberto() {
		flag = 0;
		empresa = empService.findById(numEmp);
		nomeEmp = empresa.getNomeEmp();
  		grava();
  		Imprimir.relatorio(path);
   	}

	public void grava() {
	 		try {
// 			{	OutputStreamWriter bwP = new OutputStreamWriter(
// 					new FileOutputStream(path), "UTF-8");
				BufferedWriter bwP = new BufferedWriter(new FileWriter(path));
 					if (flag == 0) {
 						list = titulo(parcela, codFor, codTipo, opcao);	
 	 					for (Parcela p : list) { 
 	 						i += 1;
							if (i == 1) {
 								if (opcao == 't') {
 									nomeCab += p.getTipoConsumo().getNomeTipo();
 								}
 								if (opcao == 'p') {
 									dtpI = sdf.format(p.getPeriodo().getDtiPeriodo());
									dtpF = sdf.format(p.getPeriodo().getDtfPeriodo());
 									nomeCab +=  dtpI + " a " + dtpF; 
 								}
 								linha02 = String.format("%s", nomeCab);
							}	
 	 	 					if (contl > 63) {
 	 	 						if (contl == 77 && flag == 0) {
									cabecalho();
 	 	 						} 
 	 	 						if (contl > 63 && flag == 0) {
									bwP.newLine();
									bwP.write(".");
									bwP.newLine();
									bwP.write(".");
 	 	 							cabecalho();
 	 	 						}
 								
 							 	bwP.newLine();
 								bwP.write(linha01);
 							 	bwP.newLine();
 								bwP.write(linha02);
 							 	bwP.newLine();
 							 	bwP.write(linhaNada);
 							 	bwP.newLine();
 								bwP.write(linha03);
 							 	bwP.newLine();
 								bwP.write(linha04);
 							 	bwP.newLine();
 								bwP.write(linha41);
 							 	bwP.newLine();
 								bwP.write(linha03);

// 								bwP.write(linha01 +"\n");
// 								bwP.write(linha02 +"\n");
// 								bwP.write("\n");
// 								bwP.write(linha03 +"\n");
// 								bwP.write(linha04 +"\n");
// 								bwP.write(linha41 +"\n");
// 								bwP.write(linha03 +"\n");
// 								bwP.write("\n");
// 								contl = 8;
 							}	
								
 	 						totVlr = totVlr + p.getValorPar();
    						totDes = totDes + p.getDescontoPar();
 	 						totJur = totJur + p.getJurosPar();
 	 						totTot = totTot + p.getTotalPar();
 	 						dtv = sdf.format(p.getDataVencimentoPar());


							String nomeFor = acentos(p.getFornecedor().getRazaoSocial());
 	 						linha05 = String.format("%-41s", nomeFor) +
 	 								  String.format("%6d", p.getNnfPar()) +
  									  String.format("%8d", p.getNumeroPar()) +
  									  String.format("%11s",  dtv);
 	 						
							linha51 = String.format("%s%12s", "  ", df.format(p.getValorPar())) +
 	 								  String.format("%s%12s", complementa(5), df.format(p.getDescontoPar())) +
 	 								  String.format("%s%12s", complementa(5), df.format(p.getJurosPar())) +
 	 								  String.format("%s%12s", complementa(6), df.format(p.getTotalPar()));
 							linha52 = 
	 	 	 					"------------------------------------------------------------------";

 							if (contl > 63 && flag == 0) {
								bwP.newLine();
								bwP.write(".");
								bwP.newLine();
								bwP.write(".");
 	 							cabecalho();

 							 	bwP.newLine();
 								bwP.write(linha01);
 							 	bwP.newLine();
 								bwP.write(linha02);
 							 	bwP.newLine();
 							 	bwP.write(linhaNada);
 							 	bwP.newLine();
 								bwP.write(linha03);
 							 	bwP.newLine();
 								bwP.write(linha04);
 							 	bwP.newLine();
 								bwP.write(linha41);
 							 	bwP.newLine();
 								bwP.write(linha03);

// 								bwP.write(linha01 +"\n");
// 								bwP.write(linha02 +"\n");
// 								bwP.write("\n");
// 								bwP.write(linha03 +"\n");
// 								bwP.write(linha04 +"\n");
// 								bwP.write(linha41 +"\n");
// 								bwP.write(linha03 +"\n");
// 								bwP.write("\n");
// 								contl = 8;
 							}	

							linha05 = acerta(linha05);
							linha51 = acerta(linha51);
							linha52 = acerta(linha52);

							bwP.newLine();
							bwP.write(linha05);
							bwP.newLine();
							bwP.write(linha51);
							bwP.newLine();
							bwP.write(linha52);
							bwP.newLine();
// 							bwP.write(linha05 +"\n");
// 	 						bwP.write(linha51 +"\n");
//	 	 	 				bwP.write(linha52 +"\n");

 				 			contl += 3;
 							if (contl > 63 && flag == 0) {
								bwP.newLine();
								bwP.write(".");
								bwP.newLine();
								bwP.write(".");
 	 							cabecalho();

 							 	bwP.newLine();
 								bwP.write(linha01);
 							 	bwP.newLine();
 								bwP.write(linha02);
 							 	bwP.newLine();
 							 	bwP.write(linhaNada);
 							 	bwP.newLine();
 								bwP.write(linha03);
 							 	bwP.newLine();
 								bwP.write(linha04);
 							 	bwP.newLine();
 								bwP.write(linha41);
 							 	bwP.newLine();
 								bwP.write(linha03);

// 								bwP.write(linha01 +"\n");
// 								bwP.write(linha02 +"\n");
// 								bwP.write("\n");
// 								bwP.write(linha03 +"\n");
// 								bwP.write(linha04 +"\n");
// 								bwP.write(linha41 +"\n");
// 								bwP.write(linha03 +"\n");
// 								bwP.write("\n");
// 								contl = 8;
 							}	
 				 			if (i == list.size()) {
 								String linha06 = String.format("%s%12s", "  ", df.format(totVlr)) +
 	 	 								  String.format("%s%12s", complementa(5), df.format(totDes)) +
 	 	 								  String.format("%s%12s", complementa(5), df.format(totJur)) +
 	 	 								  String.format("%s%12s", complementa(6), df.format(totTot));
 	 							if (contl > 63 && flag == 0) {
 									bwP.newLine();
 									bwP.write(".");
 									bwP.newLine();
 									bwP.write(".");
 	 	 							cabecalho();

 	 							 	bwP.newLine();
 	 								bwP.write(linha01);
 	 							 	bwP.newLine();
 	 								bwP.write(linha02);
 	 							 	bwP.newLine();
 	 							 	bwP.write(linhaNada);
 	 							 	bwP.newLine();
 	 								bwP.write(linha03);
 	 							 	bwP.newLine();
 	 								bwP.write(linha04);
 	 							 	bwP.newLine();
 	 								bwP.write(linha41);
 	 							 	bwP.newLine();
 	 								bwP.write(linha03);

// 	 								bwP.write(linha01 +"\n");
// 	 								bwP.write(linha02 +"\n");
// 	 								bwP.write("\n");
// 	 								bwP.write(linha03 +"\n");
// 	 								bwP.write(linha04 +"\n");
// 	 								bwP.write(linha41 +"\n");
// 	 								bwP.write(linha03 +"\n");
// 	 								bwP.write("\n");
// 	 								contl = 8;
 								} 
 	 							linha06 = acerta(linha06);
 	 							bwP.newLine();
 	 							bwP.write(linha06);
 	 							contl += 1;
	 	 					}	
 	 					}
 					}
 	 				bwP.close();
	 			}	
 	 			catch(IllegalArgumentException e1) {
	 				System.out.println(e1.getMessage());
	 			}	catch(	IOException e2) {
	 				System.out.println(e2.getMessage());	 			
	 			}
	 		}

	private List <Parcela> titulo(Parcela parcela, Integer codFor, Integer codTipo, char opcao) {
   		if (opcao == 'o')
 		{	list = parService.findAllAberto();
 			nomeCab = "Contas a Pagar";
  		}
  		if (opcao == 'p')
 		{	list = parService.findPeriodoAberto();
 			nomeCab = "Contas a Pagar no Periodo: ";
 		}
  		if (opcao == 'f')
 		{ 	list = parService.findByIdFornecedorAberto(codFor);
 			nomeCab = "Contas a Pagar por Fornecedor ";
 		}
  		if (opcao == 't')
		{	list = parService.findByIdTipoAberto(codTipo);
			nomeCab = "Contas a Pagar por Tipo: ";
		}
 		return list;	
	}	
	
	private void cabecalho(){
		contl = 7;
		contf += 1;
		linha01 = String.format("%-30s", nomeEmp) +
				  String.format("%s", "Data: ") + 
				  String.format("%-24s", dtHj) +
				  String.format("%s", "Fl- ") +
				  String.format("%2s", contf);

		linha01 = acerta(linha01);
		linha02 = acerta(linha02);
		linha03 = acerta(linha03);
		linha04 = acerta(linha04);
		linha41 = acerta(linha41);
	}

 	private String complementa(int tam) {
 		String compl = "";
 		for (int ii = 1; ii < tam + 1; ii ++) {
 			compl += " ";
 		}
 		return compl;
 	}
 	
 	public String acerta(String str) {
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
