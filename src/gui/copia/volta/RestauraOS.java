package gui.copia.volta;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import db.DbException;
import gui.sgomodel.entities.OrdemServico;
import gui.sgomodel.services.OrcamentoService;
import gui.sgomodel.services.OrdemServicoService;
import gui.util.Cryptograf;

public class RestauraOS implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Integer restauraOrdemServico(Integer count, String unid, String meioSgo, String file, String ext) {
		@SuppressWarnings("unused")
		String status = "Ok";
		count = 0;
		String path = unid + meioSgo + file + ext;
		OrdemServico os = new OrdemServico();		
		OrdemServicoService osService = new OrdemServicoService();
		OrcamentoService orcService = new OrcamentoService();
	 	SimpleDateFormat sdfAno = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	 	Calendar cal = Calendar.getInstance();

		FileReader fr;
		Scanner sc = null;
		String str = null;
		String str1 = null;
		String[] regOs = null;
		String campoOs = null;

		try {
			fr = new FileReader(path);
			sc = new Scanner(fr);
			while (sc.hasNextLine()) {
				str = sc.nextLine();		
				str1 = Cryptograf.desCriptografa(str);
				regOs = str1.split(" ORDEMSERVICO ");
				for (int i = 1 ; i < regOs.length ; i++) {
					campoOs = regOs[i];
					String[] campo = campoOs.split(" , ");
					os.setNumeroOS(Integer.parseInt(campo[0].replace("\s", "")));
					Date dtOs = null;
					try {
						dtOs = sdfAno.parse(campo[1]);
					} 
					catch (ParseException e1) {
						e1.printStackTrace();
					}
					cal.setTime(dtOs);
					os.setDataOS(cal.getTime());
					os.setOrcamentoOS(Integer.parseInt(campo[2]));
					os.setPlacaOS(campo[3]);										
					os.setClienteOS(campo[4]);
					os.setValorOS(Double.parseDouble(campo[5]));
					os.setParcelaOS(Integer.parseInt(campo[6]));
					os.setPrazoOS(Integer.parseInt(campo[7]));				
					os.setPagamentoOS(Integer.parseInt(campo[8]));
					Date dtPri = null;
					try {
						dtPri = sdfAno.parse(campo[9]);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					cal.setTime(dtPri);
					os.setDataPrimeiroPagamentoOS(dtPri);
					os.setNnfOS(Integer.parseInt(campo[10]));
					os.setObservacaoOS(campo[11]);
					os.setKmOS(Integer.parseInt(campo[12]));
					os.setMesOs(Integer.parseInt(campo[13]));
					os.setAnoOs(Integer.parseInt(campo[14]));					
					os.setOrcamento(orcService.findById(Integer.parseInt(campo[15])));
					osService.insertBackUp(os);
					count += 1;
				}
			}	
			return count;
		}	
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {
			sc.close();
		}
	}
}	
