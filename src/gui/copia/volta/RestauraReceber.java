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
import gui.sgcpmodel.services.ParPeriodoService;
import gui.sgomodel.entities.Receber;
import gui.sgomodel.services.ReceberService;
import gui.util.Cryptograf;

public class RestauraReceber implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Integer restauraReceber(Integer count, String unid, String meioSgo, String file, String ext) {
		@SuppressWarnings("unused")
		String status = "Ok";
		count = 0;
		String path = unid + meioSgo + file + ext;
		Receber rec = new Receber();		
		ReceberService recService = new ReceberService();
		ParPeriodoService perService = new ParPeriodoService();		
	 	SimpleDateFormat sdfAno = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	 	Calendar cal = Calendar.getInstance();

		FileReader fr;
		Scanner sc = null;
		String str = null;
		String str1 = null;
		String[] regRec = null;
		String campoRec = null;

		try {
			fr = new FileReader(path);
			sc = new Scanner(fr);
			while (sc.hasNextLine()) {
				str = sc.nextLine();		
				str1 = Cryptograf.desCriptografa(str);
				regRec = str1.split(" RECEBER ");
				for (int i = 1 ; i < regRec.length ; i++) {
					campoRec = regRec[i];
					String[] campo = campoRec.split(" , ");
					rec.setNumeroRec(Integer.parseInt(campo[0].replace("\s", "")));
					rec.setFuncionarioRec(Integer.parseInt(campo[1]));
					rec.setClienteRec(Integer.parseInt(campo[2]));
					rec.setNomeClienteRec(campo[3]);
					rec.setOsRec(Integer.parseInt(campo[4]));
					Date dtOs = null;
					try {
						dtOs = sdfAno.parse(campo[5]);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					cal.setTime(dtOs);
					rec.setDataOsRec(cal.getTime());
					rec.setPlacaRec(campo[6]);
					rec.setParcelaRec(Integer.parseInt(campo[7]));
					rec.setFormaPagamentoRec(campo[8]);
					rec.setValorRec(Double.parseDouble(campo[9]));
					Date dtVen = null;
					try {
						dtVen = sdfAno.parse(campo[10]);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					cal.setTime(dtVen);
					rec.setDataVencimentoRec(cal.getTime());
					Date dtPag = null;
					try {
						dtPag = sdfAno.parse(campo[11]);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					cal.setTime(dtPag);
					rec.setDataPagamentoRec(cal.getTime());
					rec.setPeriodo(perService.findById(Integer.parseInt(campo[12])));
					rec.setJurosRec(Double.parseDouble(campo[13]));
					rec.setDescontoRec(Double.parseDouble(campo[14]));
					rec.setTotalRec(Double.parseDouble(campo[15]));
					rec.setValorPagoRec(Double.parseDouble(campo[16]));
					
					recService.insertBackUp(rec);
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
