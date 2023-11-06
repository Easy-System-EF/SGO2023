package gui.copia.volta;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

import db.DbException;
import gui.sgomodel.entities.NotaFiscal;
import gui.sgomodel.services.NotaFiscalService;
import gui.util.Cryptograf;

public class RestauraNotaFiscal implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Integer restauraNotaFiscal(Integer count, String unid, String meioSgo, String file, String ext) {
		@SuppressWarnings("unused")
		String status = "Ok";
		count = 0;
		String path = unid + meioSgo + file + ext;
		NotaFiscal nf = new NotaFiscal();		
		NotaFiscalService nfService = new NotaFiscalService();

		FileReader fr;
		Scanner sc = null;
		String str = null;
		String str1 = null;
		String[] regNf = null;
		String campoNf = null;

		try {
			fr = new FileReader(path);
			sc = new Scanner(fr);
			while (sc.hasNextLine()) {
				str = sc.nextLine();		
				str1 = Cryptograf.desCriptografa(str);
				regNf = str1.split(" NF ");
				for (int i = 1 ; i < regNf.length ; i++) {
					campoNf = regNf[i];
					String[] campo = campoNf.split(" , ");
					nf.setCodigoNF(Integer.parseInt(campo[0].replaceAll("\s", "")));
					nf.setNumeroNF(Integer.parseInt(campo[1]));
					nf.setBalcaoNF(Integer.parseInt(campo[2]));
					nf.setOsNF(Integer.parseInt(campo[3]));
					nfService.insertBackUp(nf);
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
