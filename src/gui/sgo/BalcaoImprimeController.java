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
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import gui.sgomodel.entities.Balcao;
import gui.sgomodel.entities.Empresa;
import gui.sgomodel.entities.OrcVirtual;
import gui.sgomodel.services.BalcaoService;
import gui.sgomodel.services.EmpresaService;
import gui.sgomodel.services.OrcVirtualService;
import gui.util.FormataGabarito;
import gui.util.Imprimir;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class BalcaoImprimeController implements Initializable, Serializable {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static String COURIER;
	@SuppressWarnings("unused")
	private static Font getFont(String COURIER, float size) {
		return null;
	}

 	@FXML
	private Button btImprimeBalcao;

	public static Integer numEmp = 0;
	public static Integer numBal;

	Locale localeBr = new Locale("pt", "BR");
 	DecimalFormat df = new DecimalFormat("##,##0.00"); 
 	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	Date data1 = new Date();
 
	double totalBal = 0.00;
	double subTotalBal = 0.00;
	double descontoBal = 0.00;

 	String nomeMat = "";
	
	String path = "c:\\Dataprol\\WINPRINT\\_balcao.prn";
	String pathI = "C:\\Arqs\\impr\\balcao.txt";

	String linha01 = "";
	String linhaNome = "";
	String linhaEndereco = "";
	String linhaTelMailPix = "";
	String linha02 = "";
	String linha03 = "";
	String linha04 = "";
	String linha05 = "";
	String linha06 = "";
	String linha07 = "";
	String linha08 = "";
	String linha09 = "";
	String nada = " ";
	
	private Balcao balcao;
	private Empresa emp;
	
	private BalcaoService balService;
	private EmpresaService empService;

	public void setBalcao(Balcao balcao) {
		this.balcao = balcao;
	}
	public void setEmpresa(Empresa emp) {
		this.emp = emp;
	}

	public void setBalcaoService(BalcaoService balService, EmpresaService empService) {
		this.balService = balService;
		this.empService = empService;
	}

	OrcVirtual virtual = new OrcVirtual();
	private OrcVirtualService virService;

	public void setOrcVirtual(OrcVirtual virtual) {
		this.virtual = virtual;
	}

	public void setOrcVirtualService(OrcVirtualService virService) {
		this.virService = virService;
	}

	@FXML
	public void onBtImprimeBalcaoAction(ActionEvent event) throws ParseException, IOException {
		@SuppressWarnings("unused")
		Stage parentStage = Utils.currentStage(event) ;
		montaEmpresa();
   		grava();
		Imprimir.relatorio(path);
//		File file = new File(pathI);
     	Utils.currentStage(event).close();
//		Utils.deleteArq(file);
   	}

	private void montaEmpresa() {
		emp = empService.findById(numEmp);
		linhaNome = emp.getNomeEmp();
		linhaEndereco = emp.getEnderecoEmp();
		linhaTelMailPix = "Tel.: " + emp.getTelefoneEmp() + 
				" - Email: " + emp.getEmailEmp() + " - Pix: " + emp.getPixEmp();	
	}
	
	List<OrcVirtual> listVir = new ArrayList<>();

 	public void grava() {
			try {	
				BufferedWriter bwO = new BufferedWriter(new FileWriter(path));
 					{	balcao = balService.findById(numBal);
 					
 						linha01 = String.format("                             Balcão");
						linha02 = String.format("Número...: %6d", balcao.getNumeroBal()) +
						          String.format("%18s%s", "Data: ", sdf.format(balcao.getDataBal()));
 					 	linha03 = String.format("                             Material"); 
					 	linha04 = "*****************************************************************"; 
					 	linha05 = "Nome                                 Qtd        Preço       Valor";					 			

					 	linha01 = acerta(linha01);
					 	linhaNome = acerta(linhaNome);
					 	linha02 = acerta(linha02);
					 	linha03 = acerta(linha03);
					 	linha04 = acerta(linha04);
					 	linha05 = acerta(linha05);
					 	
					 	bwO.newLine();
					 	bwO.write(linha01);
					 	bwO.newLine();
					 	bwO.write(linhaNome);
					 	bwO.newLine();
						bwO.write(linha02);
					 	bwO.newLine();
					 	bwO.write(nada);
					 	bwO.newLine();
						bwO.write(linha03);
					 	bwO.newLine();
						bwO.write(linha04);
					 	bwO.newLine();
						bwO.write(linha05);
					 	bwO.newLine();
						bwO.write(linha04);
					 	bwO.newLine();
							
						listVir = virService.findAll();
						for (OrcVirtual v : listVir) {
							if (v.getNumeroBalVir().equals(numBal)) {
								if (v.getMaterial().getNomeMat().length() > 35) {
									nomeMat = v.getMaterial().getNomeMat().substring(0, 35);
								} else {
									nomeMat = v.getMaterial().getNomeMat();
								}
								String qtdFd = FormataGabarito.formataQtd5(v.getQuantidadeMatVir());
								totalBal += v.getTotalMatVir();
								String linha06 = String.format("%-36s", nomeMat) +
												 String.format("%s%s", qtdFd, v.getQuantidadeMatVir()) +
												 String.format("%13s", df.format(v.getPrecoMatVir())) +
												 String.format("%12s", df.format(v.getTotalMatVir()));
								linha06 = acerta(linha06);
								bwO.write(linha06); 
							 	bwO.newLine();
							}
 						}
						if (balcao.getDescontoBal() > 0) {
							descontoBal = balcao.getDescontoBal();
							subTotalBal = totalBal;
							linha07 = String.format("%s%57s", "SubTotal", df.format(subTotalBal));
							linha08 = String.format("%s%57s", "Desconto", df.format(descontoBal));
							totalBal -= descontoBal;
							linha09 = String.format("%s%60s", "Total", df.format(totalBal));

							linha07 = acerta(linha07);
						 	linha08 = acerta(linha08);
						 	linha09 = acerta(linha09);
						 	
							bwO.write(linha07);
							bwO.newLine();
							bwO.write(linha08);
							bwO.newLine();
							bwO.write(linha09);
							bwO.newLine();
						} else {
							linha07 = String.format("%s%60s", "Total", df.format(totalBal));
							bwO.write(linha07);
							bwO.newLine();
						}
 					}
				 	bwO.newLine();
				 	bwO.write(nada);
				 	bwO.newLine();
				 	linhaEndereco = acerta(linhaEndereco);
				 	bwO.write(linhaEndereco);
				 	bwO.newLine();
				 	linhaTelMailPix = acerta(linhaTelMailPix);
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
 	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
}
