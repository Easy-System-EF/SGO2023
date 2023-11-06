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
import gui.sgomodel.entities.Material;
import gui.sgomodel.services.GrupoService;
import gui.sgomodel.services.MaterialService;
import gui.util.Cryptograf;

public class RestauraMaterial implements Serializable {
	private static final long serialVersionUID = 1L;

	public static Integer restauraMaterial(Integer count, String unid, String meioSgo, String file, String ext) {
		@SuppressWarnings("unused")
		String status = "Ok";
		count = 0;
		String path = unid + meioSgo + file + ext;
		Material mat = new Material();		
		MaterialService matService = new MaterialService();
		GrupoService gruService = new GrupoService();
	 	SimpleDateFormat sdfAno = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	 	Calendar cal = Calendar.getInstance();

		FileReader fr;
		Scanner sc = null;
		String str = null;
		String str1 = null;
		String[] regMat = null;
		String campoSit = null;

		try {
			fr = new FileReader(path);
			sc = new Scanner(fr);
			while (sc.hasNextLine()) {
				str = sc.nextLine();		
				str1 = Cryptograf.desCriptografa(str);
				regMat = str1.split(" MATERIAL ");
				for (int i = 1 ; i < regMat.length ; i++) {
					campoSit = regMat[i];
					String[] campo = campoSit.split(" , ");
					mat.setCodigoMat(Integer.parseInt(campo[0].replaceAll("\s", "")));
					mat.setGrupoMat(Integer.parseInt(campo[1]));
					mat.setNomeMat(campo[2]);
					mat.setEstMinMat(Double.parseDouble(campo[3]));
					mat.setSaldoMat(Double.parseDouble(campo[4]));
					mat.setSaidaCmmMat(Double.parseDouble(campo[5]));
					mat.setCmmMat(Double.parseDouble(campo[6]));
					mat.setPrecoMat(Double.parseDouble(campo[7]));
					mat.setVendaMat(Double.parseDouble(campo[8]));
					mat.setVidaKmMat(Integer.parseInt(campo[9]));
					mat.setVidaMesMat(Integer.parseInt(campo[10]));
					mat.setPercentualClass(Double.parseDouble(campo[11]));
					mat.setLetraClass(campo[12].charAt(0));
					Date data = sdfAno.parse(campo[13]);
					cal.setTime(data);
					mat.setDataCadastroMat(cal.getTime());
					mat.setGrupo(gruService.findById(Integer.parseInt(campo[14])));
					matService.insertBackUp(mat);
					count += 1;
				}
			}	
			return count;
		}	
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		catch(ParseException e3) {
			status = "Er";
			throw new DbException(e3.getMessage());	 			
		}
		finally {
			sc.close();
		}
	}
}	
