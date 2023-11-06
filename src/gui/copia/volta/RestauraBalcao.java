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
import gui.sgomodel.entities.Balcao;
import gui.sgomodel.services.BalcaoService;
import gui.sgomodel.services.FuncionarioService;
import gui.util.Cryptograf;

public class RestauraBalcao implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Integer restauraBalcao(Integer count, String unid, String meioSgo, String file, String ext) {
		@SuppressWarnings("unused")
		String status = "Ok";
		count = 0;
		String path = unid + meioSgo + file + ext;
		Balcao bal = new Balcao();		
		BalcaoService balService = new BalcaoService();
		FuncionarioService funService = new FuncionarioService();
	 	SimpleDateFormat sdfAno = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	 	Calendar cal = Calendar.getInstance();

		FileReader fr;
		Scanner sc = null;
		String str = null;
		String str1 = null;
		String[] regBal = null;
		String campoBal = null;

		try {
			fr = new FileReader(path);
			sc = new Scanner(fr);
			while (sc.hasNextLine()) {
				str = sc.nextLine();		
				str1 = Cryptograf.desCriptografa(str);
				regBal = str1.split(" BALCAO ");
				for (int i = 1 ; i < regBal.length ; i++) {
					campoBal = regBal[i];
					String[] campo = campoBal.split(" , ");
					bal.setNumeroBal(Integer.parseInt(campo[0].replace("\s", "")));
					Date dtBal = null;
					try {
						dtBal = sdfAno.parse(campo[1]);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					cal.setTime(dtBal);
					bal.setDataBal(cal.getTime());
					bal.setFuncionarioBal(campo[2]);
					bal.setDescontoBal(Double.parseDouble(campo[3]));
					bal.setTotalBal(Double.parseDouble(campo[4]));
					bal.setPagamentoBal(Integer.parseInt(campo[5]));
					Date dtPri = null;
					try {
						dtPri = sdfAno.parse(campo[6]);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					cal.setTime(dtPri);
					bal.setDataPrimeiroPagamentoBal(cal.getTime());
					bal.setNnfBal(Integer.parseInt(campo[7]));
					bal.setObservacaoBal(campo[8]);
					bal.setMesBal(Integer.parseInt(campo[9]));
					bal.setAnoBal(Integer.parseInt(campo[10]));
					bal.setFuncionario(funService.findById(Integer.parseInt(campo[11])));
					
					balService.insertBackUp(bal);
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
