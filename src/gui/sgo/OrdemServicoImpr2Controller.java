package gui.sgo;

import java.awt.Font;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import gui.sgomodel.entities.Empresa;
import gui.sgomodel.entities.OrcVirtual;
import gui.sgomodel.entities.Orcamento;
import gui.sgomodel.entities.OrdemServico;
import gui.sgomodel.entities.Receber;
import gui.sgomodel.services.EmpresaService;
import gui.sgomodel.services.OrcVirtualService;
import gui.sgomodel.services.OrcamentoService;
import gui.sgomodel.services.OrdemServicoService;
import gui.sgomodel.services.ReceberService;
import gui.util.FormataGabarito;
import gui.util.Imprimir;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class OrdemServicoImpr2Controller implements Initializable, Serializable {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static String COURIER;
	@SuppressWarnings("unused")
	private static Font getFont(String COURIER, float size) {
		return null;
	}

	@FXML
	private Button btImprimeOS;

	public static Integer numEmp = 0;
	public static Integer codOs;

	Locale localeBr = new Locale("pt", "BR");
 	DecimalFormat df = new DecimalFormat("#,###,##0.00"); 
 	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	int tamS = 40;
 
	double valorOs1 = 0.00;
	double valorOs2 = 0.00;
	double valorOs3 = 0.00;

	String dataOs1;
	String dataOs2;
	String dataOs3;
	String numFn;
 	String nomeVai = "";
 	String nomeVem = "";
 	String pagamento = "";
 	String prazo = "";
 	String fS = "";
	
	String path = "c:\\Dataprol\\WINPRINT\\_os.prn";
	String pathI = "C:\\Arqs\\impr\\os.txt";

	String linha01 = "";
	String linha02 = "";
	String linha03 = "";
	String linha04 = "";
	String linha05 = "";
	String linha06 = "";
	String linha07 = "";
	String linha08 = "";
	String linha09 = "";
	String linha10 = "";
	String linha10b = "";
	String linha10c = "";
	String linha11 = "";
	String linha12 = "";
	String linha13 = "";
	String linha14 = "";
	String linha15 = "";
	String linha16 = "";
	String linha17 = "";
	String nada = " ";
	String linhaNome = "";
	String linhaEndereco = "";
	String linhaTelMailPix = "";
	
	OrdemServico ordemServico = new OrdemServico();
	Orcamento orcamento = new Orcamento();
	private Empresa emp;
	StringBuilder sb = new StringBuilder();

	private OrdemServicoService osService;
	private OrcamentoService orcService;
	private OrcVirtualService virService;
	private ReceberService recService;
	private EmpresaService empService;

	public void setOrdemServico(OrdemServico ordemServico) {
		this.ordemServico = ordemServico;
	}
	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}
	public void setEmpresa(Empresa emp) {
		this.emp = emp;
	}

	public void setOSImprime(OrdemServicoService osService, 
									OrcamentoService orcService, 
									OrcVirtualService virService,
									ReceberService recService, 
									EmpresaService empService) {
		this.osService = osService;
		this.orcService = orcService;
		this.virService = virService;
		this.recService = recService;
		this.empService = empService;
	}

	@FXML
	public void onBtImprimeOSAction(ActionEvent event) throws ParseException, IOException {
		montaEmpresa();
   		grava();
  		Imprimir.relatorio(path);
     	Utils.currentStage(event).close();
   	}

	private void montaEmpresa() {
		emp = empService.findById(numEmp);
		linhaNome = emp.getNomeEmp();
		linhaEndereco = emp.getEnderecoEmp();
		linhaTelMailPix = "Tel.: "  + emp.getTelefoneEmp() + 
				" - Email: " + emp.getEmailEmp() + " - Pix: " + emp.getPixEmp();	
	}
	
	List<OrcVirtual> listVir = new ArrayList<>();
	List<Receber> rec = new ArrayList<>();
		
 	public void grava() {
			try {	
				BufferedWriter bwO = new BufferedWriter(new FileWriter(path));
 					{	ordemServico = osService.findById(codOs);
 						orcamento = orcService.findById(ordemServico.getOrcamentoOS());
 						pagtoPrazo();
 						receber();
 						linha01 = String.format("                            Ordem de Serviço");
 						linha02 = String.format("Número...: %6d", orcamento.getNumeroOrc()) +
 						          String.format("%22s%s", "Data: ", sdf.format(orcamento.getDataOrc()));
 						linha03 = String.format("Placa....: %s", orcamento.getPlacaOrc()) +
 								  String.format("%21s%s", "Km..: ", orcamento.getKmFinalOrc());
 						linha04 = String.format("Cliente..: %s", orcamento.getClienteOrc());
 						linha05 = String.format("Atendente: %s", orcamento.getFuncionarioOrc());
 						linha06 = String.format("Pagamento: %s", pagamento) + 
 								  String.format("%21s%s", "Prazo: ", prazo);
 						linha07 = String.format("Parcela..: %2d", ordemServico.getParcelaOS()) +
 								  String.format("%27s%s", "Total: ", df.format(ordemServico.getValorOS()));
 					 	linha08 = String.format("NNF......: %6d", ordemServico.getNnfOS());
 					 	linha09	= String.format("                            Vencimento");
 					 	linha10 = String.format("Data.....: %10s", dataOs1) +
 					 			  String.format("%19s%s", "Valor: ", df.format(valorOs1));
 					 	if (valorOs2 > 0) {
 						 	linha10b = String.format("Data.....: %10s", dataOs2) +
 						 			  String.format("%19s%s", "Valor: ", df.format(valorOs2));	 		
 					 	}
 					 	if (valorOs3 > 0) {
 						 	linha10c = String.format("Data.....: %10s", dataOs3) +
 						 			  String.format("%19s%s", "Valor: ", df.format(valorOs3));	 		
 					 	}
 						linha11 = String.format("                             Material"); 
 					 	linha12 = String.format("*****************************************************************"); 
 					 	linha13 = String.format("Nome                                 Qtd        Preço       Valor");	
 					 	linha14 = String.format("*****************************************************************");

 					 	linha01 = acerta(linha01);
 					 	linha02 = acerta(linha02);
 					 	linha03 = acerta(linha03);
 					 	linha04 = acerta(linha04);
 					 	linha05 = acerta(linha05);
 					 	linha06 = acerta(linha06);
 					 	linha07 = acerta(linha07);
 					 	linha08 = acerta(linha08);
 					 	linha09 = acerta(linha09);
 					 	linha10 = acerta(linha10);
 					 	linha11 = acerta(linha11);
 					 	linha12 = acerta(linha12);
 					 	linha13 = acerta(linha13);
 					 	linha14 = acerta(linha14);
 					 	
					 	bwO.newLine();
					 	bwO.write(linha01);
					 	bwO.newLine();
					 	bwO.write(linhaNome);
					 	bwO.newLine();
						bwO.write(linha02);
					 	bwO.newLine();
						bwO.write(linha03);
					 	bwO.newLine();
						bwO.write(linha04);
					 	bwO.newLine();
						bwO.write(linha05);
					 	bwO.newLine();
						bwO.write(linha06);
					 	bwO.newLine();
						bwO.write(linha07);
					 	bwO.newLine();
						bwO.write(linha08);
					 	bwO.newLine();
						bwO.write(linha09);
					 	bwO.newLine();
						bwO.write(linha10);
					 	bwO.newLine();
						bwO.write(linha10b);
					 	bwO.newLine();
						bwO.write(linha10c);
					 	bwO.newLine();
					 	bwO.write(nada);
					 	bwO.newLine();
						bwO.write(linha11);
					 	bwO.newLine();
						bwO.write(linha12);
					 	bwO.newLine();					 	
						bwO.write(linha13);
					 	bwO.newLine();
						bwO.write(linha14);
					 	bwO.newLine();
 					 	
 						listVir = virService.findByOrcto(orcamento.getNumeroOrc());
 						for (OrcVirtual v : listVir) {
 							tamNome(v.getNomeMatVir());
 							String qtdFd = FormataGabarito.formataQtd5(v.getQuantidadeMatVir());
 							String precoFd = FormataGabarito.formataDouble(v.getPrecoMatVir());
 							String totFd = FormataGabarito.formataDouble(v.getTotalMatVir());
 							String linha15 = "";
 							nomeVem = v.getNomeMatVir();
 							if (nomeVem.length() > 34) { 
 								nomeVem = v.getNomeMatVir().substring(0, 34);
 							}	
 							linha15 = String.format("%-35s", nomeVem) +
 									  String.format("%s", qtdFd) +
 									  String.format("%s", df.format(v.getQuantidadeMatVir())) +
 									  String.format("%s", precoFd) +
 									  String.format("%s", df.format(v.getPrecoMatVir())) +
 									  String.format("%s", totFd) +
 									  String.format("%s", df.format(v.getTotalMatVir()));
 							linha15 = acerta(linha15);
 							bwO.newLine();
 							bwO.write(linha15);
 						}
 						if (orcamento.getDescontoOrc() > 0) {
 							String descFd = FormataGabarito.formataDouble(orcamento.getDescontoOrc());
 							String linha16 = "";
 							linha16 = String.format("%-52s", "Desconto(-)") +
 									  String.format("%s", descFd) +
 									  String.format("%s", df.format(orcamento.getDescontoOrc()));
 							bwO.newLine();
 							bwO.write(linha16);
 						}
 						if (ordemServico.getObservacaoOS() == null) {
 							ordemServico.setObservacaoOS("");
 						}
 						linha17 = String.format("Obs.: " + ordemServico.getObservacaoOS());
						linha17 = acerta(linha17);
						bwO.newLine();
						bwO.write(linha17);
 					}	
					bwO.newLine();
					bwO.write(nada);
					bwO.newLine();
					bwO.write(linhaEndereco);
					bwO.newLine();
					bwO.write(linhaTelMailPix);
				 	bwO.newLine();
					bwO.close();
				}	
	 			catch(	IOException e2) {
	 				e2.getMessage();	 			
	 			}
				finally {		
				}
	 		}

	private void receber() {
			rec = recService.findByAllOs(ordemServico.getNumeroOS());
			for (Receber r : rec) {
				if (r.getOsRec().equals(ordemServico.getNumeroOS())) {
					if (r.getParcelaRec() == 1) {
						dataOs1 = sdf.format(r.getDataVencimentoRec());
						valorOs1 = r.getValorRec();
					}
					if (r.getParcelaRec() == 2) {
						dataOs2 = sdf.format(r.getDataVencimentoRec());
						valorOs2 = r.getValorRec();
					}
					if (r.getParcelaRec() == 3) {
						dataOs3 = sdf.format(r.getDataVencimentoRec());
						valorOs3 = r.getValorRec();
					}
				}	
			}
	}
	private void pagtoPrazo() {
			switch (ordemServico.getPagamentoOS()) {
				case 1:
					pagamento = "Dinheiro";
						break;
				case 2:
					pagamento = "Pix";
						break;
				case 3:
					pagamento = "Débito";
						break;
				case 4:
					pagamento = "Crédito";
						break;
				default :
					pagamento = "Inválido";
						break;
			}			
			switch (ordemServico.getPrazoOS()) {
				case 1:
					prazo = "A vista";
						break;
				case 10:
					prazo = "10 dias";
						break;
				case 15:
					prazo = "15 dias";
						break;
				case 30:
					prazo = "30 dias";
						break;
				default :
					prazo = "Inválido";
						break;
			}			
	}
	
//	public String toLinha() {
//		linha01 = String.format("                               Ordem de Serviço");
//		linha02 = String.format("Número...: %6d", orcamento.getNumeroOrc()) +
//		          String.format("%18s%s", "Data: ", sdf.format(orcamento.getDataOrc()));
//		linha03 = String.format("Placa....: %s", orcamento.getPlacaOrc()) +
//				  String.format("%17s%s", "Km..: ", orcamento.getKmFinalOrc());
//		linha04 = String.format("Cliente..: %s", orcamento.getClienteOrc());
//		linha05 = String.format("Atendente: %s", orcamento.getFuncionarioOrc());
//		linha06 = String.format("Pagamento: %s", pagamento) + 
//				  String.format("%16s%", "Prazo: ", prazo);
//		linha07 = String.format("Parcela..: %2d", ordemServico.getParcelaOS()) +
//				  String.format("%23s", "Total: ", df.format(ordemServico.getValorOS()));
//	 	linha08 = String.format("NNF......: %6d", ordemServico.getNnfOS());
//	 	linha09	= String.format("                               Vencimento");
//	 	linha10 = String.format("Data.....: %10s", dataOs1) +
//	 			  String.format("%21s", "Valor", df.format(valorOs1));
//	 	if (valorOs2 > 0) {
//		 	linha10b = String.format("Data.....: %10s", dataOs2) +
//		 			  String.format("%21s", "Valor", df.format(valorOs2));	 		
//	 	}
//	 	if (valorOs3 > 0) {
//		 	linha10c = String.format("Data.....: %10s", dataOs3) +
//		 			  String.format("%21s", "Valor", df.format(valorOs3));	 		
//	 	}
//		linha11 = String.format("                             Material"); 
//	 	linha12 = String.format("*****************************************************************"); 
//	 	linha13 = String.format("Nome                                 Qtd        Preço       Valor");	
//	 	linha14 = String.format("*****************************************************************");
//
//		listVir = virService.findByOrcto(orcamento.getNumeroOrc());
//		for (OrcVirtual v : listVir) {
//			tamNome(v.getNomeMatVir());
//			String qtdFd = FormataGabarito.formataQtd5(v.getQuantidadeMatVir());
//			String precoFd = FormataGabarito.formataDouble(v.getPrecoMatVir());
//			String totFd = FormataGabarito.formataDouble(v.getTotalMatVir());
//			String linha15 = "";
//			nomeVem = v.getNomeMatVir();
//			if (nomeVem.length() > 34) { 
//				nomeVem = v.getNomeMatVir().substring(0, 34);
//			}	
//			linha15 = String.format("%-35s", nomeVem) +
//					  String.format("%s", qtdFd) +
//					  String.format("%s", df.format(v.getQuantidadeMatVir())) +
//					  String.format("%s", precoFd) +
//					  String.format("%s", df.format(v.getPrecoMatVir())) +
//					  String.format("%s", totFd) +
//					  String.format("%s", df.format(v.getTotalMatVir()));
//		}
//		if (orcamento.getDescontoOrc() > 0) {
//			String descFd = FormataGabarito.formataDouble(orcamento.getDescontoOrc());
//			String linha16 = "";
//			linha16 = String.format("%-52s", "Desconto(-)") +
//					  String.format("%s", descFd) +
//					  String.format("%s", df.format(orcamento.getDescontoOrc()));
//		}
//		if (ordemServico.getObservacaoOS() == null) {
//			ordemServico.setObservacaoOS("");
//		}
//		linha17 = String.format("Obs.: " + ordemServico.getObservacaoOS());
////		linhaEndereco;
////		linhaTelMailPix;
//	}	
	
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
 	
 	private String tamNome(String nomeVai) {
 		tamS = nomeVai.length();
 		if (tamS > 40) {
 			tamS = 39;	
 			nomeVem = nomeVai.substring(0, 39);
 		}	else {
 			nomeVem = nomeVai;
 		}
		return nomeVem;
 	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}
}
