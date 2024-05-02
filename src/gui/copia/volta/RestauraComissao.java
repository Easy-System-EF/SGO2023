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
import gui.sgomodel.entities.Comissao;
import gui.sgomodel.services.ComissaoService;
import gui.util.Cryptograf;

public class RestauraComissao  implements Serializable {
	private static final long serialVersionUID = 1L;

	public static Integer restauraComissao(Integer count, String unid, String meioSgb, String file, String ext) {
		@SuppressWarnings("unused")
		String status = "Ok";
		count = 0;
		String path = unid + meioSgb + file + ext;
		Comissao com = new Comissao();		
		ComissaoService comService = new ComissaoService();
	 	SimpleDateFormat sdfAno = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	 	Calendar cal = Calendar.getInstance();

		FileReader fr;
		Scanner sc = null;
		String str = null;
		String str1 = null;
		String[] regCom = null;
		String campoCom = null;

		try {
			fr = new FileReader(path);
			sc = new Scanner(fr);
			while (sc.hasNextLine()) {
				str = sc.nextLine();	
				str1 = Cryptograf.desCriptografa(str);
				regCom = str1.split(" COMISSAO ");
				for (int i = 1 ; i < regCom.length ; i++) {
					campoCom = regCom[i];
					String[] campo = campoCom.split(" , ");
					com.setNumeroCom(Integer.parseInt(campo[0].replaceAll("\s", "")));
					Date data = sdfAno.parse(campo[1]);
					cal.setTime(data); 
					com.setDataCom(cal.getTime());
					com.setFunCom(Integer.parseInt(campo[2]));
					com.setNomeFunCom(campo[3]);
					com.setCargoCom(campo[4]);
					com.setSituacaoCom(campo[5]);
					com.setOSCom(Integer.parseInt(campo[6]));
					com.setBalcaoCom(Integer.parseInt(campo[7]));
					com.setMesCom(Integer.parseInt(campo[8]));
					com.setAnoCom(Integer.parseInt(campo[9]));
					com.setPercentualCom(Double.parseDouble(campo[10]));
					com.setProdutoCom(Double.parseDouble(campo[11]));
					com.setComissaoCom();
					comService.insertBackUp(com);
					count += 1;
				}
			}	
			return count;
		}	
		catch(	ParseException e3) {
			status = "Er";
			throw new DbException(e3.getMessage());	 			
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
