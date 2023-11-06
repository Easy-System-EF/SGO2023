package gui.copia.volta;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

import db.DbException;
import gui.sgomodel.entities.Grupo;
import gui.sgomodel.services.GrupoService;
import gui.util.Cryptograf;

public class RestauraGrupo implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Integer restauraGrupo(Integer count, String unid, String meioSgo, String file, String ext) {
		@SuppressWarnings("unused")
		String status = "Ok";
		count = 0;
		String path = unid + meioSgo + file + ext;
		Grupo gru = new Grupo();		
		GrupoService gruService = new GrupoService();

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
				regSit = str1.split("GRUPO");
				for (int i = 1 ; i < regSit.length ; i++) {
					campoSit = regSit[i];
					String[] campo = campoSit.split(" , ");
					gru.setCodigoGru(Integer.parseInt(campo[0].replaceAll("\s", "")));
					gru.setNomeGru(campo[1].replace(";", ""));
					gruService.insertBackUp(gru);
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
