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

public class ParcelaImprimePagoController implements Initializable, Serializable {

	private static final long serialVersionUID = 1L;

 	public static Integer numEmp = null;
	Locale localeBr = new Locale("pt", "BR");
 	DecimalFormat df = new DecimalFormat("#,###,##0.00"); 
 	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	Date data1 = new Date();
	String dtpI;
	String dtpF;

	int contf = 0;
	int contl = 77;
	int i = 0;
	int flag = 0;
 
	double totVlr = 0.00;
	double totJur = 0.00;
	double totDes = 0.00;
	double totPag = 0.00;
 
 	String dth = sdf.format(data1);
 	String nomeEmp = null;
	String linha01 = "";
 	String linha02 = "";
	String linha03 = "******************************************************************";
	String linha04 = "Fornecedor                                  Nnf Parcela Vencimento";
	String linha05 = "       Valor        Juros     Desconto        Pagto Data Pagamento";
	String linha51 = "";
	String linha52 = "";
	String linha06 = "------------------------------------------------------------------";
	String linha07 = "";
	String linhaNada = "";

	String path = "c:\\Dataprol\\WINPRINT\\_Pago.prn";
	String pathI = "C:\\Arqs\\impr\\Pago.txt";

	Parcela parcela = new Parcela();
	private ParcelaService parService;
	private Empresa empresa;
	private EmpresaService empService;

 	private char opcao;
	private Integer codFor;
	private Integer codTipo;
	
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
	public void onBtImprimePago() {
		flag = 0;  
		empresa = empService.findById(numEmp);
		nomeEmp = empresa.getNomeEmp();
  		grava();
  		Imprimir.relatorio(path);
   	}

 	public void grava() {
	 		try	{	BufferedWriter bwP = new BufferedWriter(new FileWriter(path));
	 				if (flag == 0) {
 						if (contl == 77) {
 		 					list = listagem(parcela, codFor, codTipo, opcao);	
 						}
 	 	 				for (Parcela p : list) { 
 	 						i += 1;
 	 	 					if (i == 1) {
 	 	 						linha02 = "Contas Pagas";
 	 	 						if (opcao == 'q') {
 	 	 							dtpI = sdf.format(p.getPeriodo().getDtiPeriodo());
 	 	 							dtpF = sdf.format(p.getPeriodo().getDtfPeriodo());
 	 	 							linha02 = String.format("%s", "Contas Pagas por período: ") +
 	 	 									String.format("%s", dtpI) +
 					                      	String.format("%s", " a ") +
 					                      	String.format("%s", dtpF);
 	 	 						}         	
 	 	 						if (opcao == 'g') {
 	 	 							linha02 = "Contas pagas por Fornecedor";
 	 	 						}	
 	 	 						if (opcao == 'u') {
 	 	 							linha02 = String.format("%s", "Contas Pagas por Tipo: ") +
 	 	 									  String.format("%s", p.getTipoConsumo().getNomeTipo());
 	 	 						}
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
  				 				bwP.write(linha05);
  				 				bwP.newLine();
  	 							bwP.write(linha03);
 			 				}
 	 						
			 				linha51 = String.format("%-40s", p.getFornecedor().getRazaoSocial()) +
			 						  String.format("%7d", p.getNnfPar()) +
			 						  String.format("%8d", p.getNumeroPar()) +
			 						  String.format("%11s", sdf.format(p.getDataVencimentoPar()));
			 						
			 				String esp = " ";
			 				linha52 = String.format("%12s%s", df.format(p.getValorPar()), esp) +
			 						  String.format("%12s%s", df.format(p.getJurosPar()), esp) +
			 						  String.format("%12s%s", df.format(p.getDescontoPar()), esp) +
			 						  String.format("%12s", df.format(p.getPagoPar())) +
			 						  String.format("%15s", sdf.format(p.getDataPagamentoPar()));

							totVlr += p.getValorPar();
							totJur += p.getJurosPar();
							totDes += p.getDescontoPar();
							totPag += p.getPagoPar();
							
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
  				 				bwP.write(linha05);
  				 				bwP.newLine();
  	 							bwP.write(linha03);
 				 			}	
 				 			
 				 			linha51 = acentos(linha51);
 				 			linha52 = acentos(linha52);
 				 			linha06 = acentos(linha06);
 				 			
							bwP.newLine();
							bwP.write(linha51);
							bwP.newLine();
							bwP.write(linha52);
							bwP.newLine();
							bwP.write(linha06);
 			 				contl += 3;

		 					if (i == list.size()) {
		 						flag = 1;
		 						linha06 = "-----------------------------Total--------------------------------";
	 	 						linha07 = String.format("%12s%s", df.format(totVlr), esp) +
				 						  String.format("%12s%s", df.format(totJur), esp) +
				 						  String.format("%12s%s", df.format(totDes), esp) +
				 						  String.format("%12s", df.format(totPag));
								bwP.newLine();
	 	 						bwP.write(linha06);
	 	 						bwP.newLine();
 	 	 						bwP.write(linha07);
 			 				}	
	 					}
	 				}
 					bwP.close();
	 			}	
	 			catch(IllegalArgumentException e1) {
	 				System.out.println(e1.getMessage());
	 			}	
	 			catch(	IOException e2) {
					System.out.println(e2.getMessage());	 			
				}
 			}

	private void cabecalho() {
			contf += 01;
			contl = 7;
			linha01 = String.format("%-30s", nomeEmp) +
					  String.format("%s", "Data: ") + 
					  String.format("%-24s", dth) +
					  String.format("%s", "Fl- ") +
					  String.format("%2s", contf);
			
			linha01 = acentos(linha01);
			linha02 = acentos(linha02);
			linha03 = acentos(linha03);
			linha04 = acentos(linha04);
			linha05 = acentos(linha05);
			linha03 = acentos(linha03);
	}

	private List <Parcela> listagem(Parcela parcela, Integer codFor, Integer codTipo, char opcao) {
   		if (opcao == 'o') {
 			list = parService.findAllPago();
  		}
  		if (opcao == 'q') {
 			list = parService.findPeriodoPago();
 		}
  		if (opcao == 'g') {
 		 	list = parService.findByIdFornecedorPago(codFor);
 		}
  		if (opcao == 'u') {
			list = parService.findByIdTipoPago(codTipo);
		}
 		return list;
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
