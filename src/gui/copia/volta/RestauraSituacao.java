package gui.copia.volta;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

import db.DbException;
import gui.sgomodel.entities.Situacao;
import gui.sgomodel.services.SituacaoService;
import gui.util.Cryptograf;

public class RestauraSituacao implements Serializable {
	private static final long serialVersionUID = 1L;

	public static Integer restauraSituacao(Integer count, String unid, String meioSgo, String file, String ext) {
		@SuppressWarnings("unused")
		String status = "Ok";
		count = 0;
		String path = unid + meioSgo + file + ext;
		Situacao sit = new Situacao();		
		SituacaoService sitService = new SituacaoService();

		FileReader fr;
		Scanner sc = null;
		String str = null;
		String str1 = null;
		String[] regSit = null;
		String campoSit = null;

		try {
			fr = new FileReader(path);
			sc = new Scanner(fr);
			while (sc.hasNextLine()) {
				str = sc.nextLine();		
				str1 = Cryptograf.desCriptografa(str);
				regSit = str1.split(" SITUACAO ");
				for (int i = 1 ; i < regSit.length ; i++) {
					campoSit = regSit[i];
					String[] campo = campoSit.split(" , ");
					sit.setNumeroSit(Integer.parseInt(campo[0].replaceAll("\s", "")));
					sit.setNomeSit(campo[1]);
					sitService.insertBackUp(sit);
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
