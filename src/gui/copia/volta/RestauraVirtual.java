package gui.copia.volta;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

import db.DbException;
import gui.sgomodel.entities.OrcVirtual;
import gui.sgomodel.services.MaterialService;
import gui.sgomodel.services.OrcVirtualService;
import gui.util.Cryptograf;

public class RestauraVirtual implements Serializable {
	private static final long serialVersionUID = 1L;

	public static Integer restauraVirtual(Integer count, String unid, String meioSgo, String file, String ext) {
		@SuppressWarnings("unused")
		String status = "Ok";
		count = 0;
		String path = unid + meioSgo + file + ext;
		OrcVirtual vir = new OrcVirtual();		
		OrcVirtualService virService = new OrcVirtualService();
		MaterialService matService = new MaterialService();

		FileReader fr;
		Scanner sc = null;
		String str = null;
		String str1 = null;
		String[] regVir = null;
		String campoVir = null;

		try {
			fr = new FileReader(path);
			sc = new Scanner(fr);
			while (sc.hasNextLine()) {
				str = sc.nextLine();		
				str1 = Cryptograf.desCriptografa(str);
				regVir = str1.split(" VIRTUAL ");
				for (int i = 1 ; i < regVir.length ; i++) {
					campoVir = regVir[i];
					String[] campo = campoVir.split(" , ");
					vir.setNumeroVir(Integer.parseInt(campo[0].replaceAll("\s", "")));
					vir.setNomeMatVir(campo[1]);
					vir.setQuantidadeMatVir(Double.parseDouble(campo[2]));
					vir.setPrecoMatVir(Double.parseDouble(campo[3]));
					vir.setTotalMatVir(Double.parseDouble(campo[4]));
					vir.setNumeroOrcVir(Integer.parseInt(campo[5]));
					vir.setNumeroBalVir(Integer.parseInt(campo[6]));
					vir.setMaterial(matService.findById(Integer.parseInt(campo[7])));					
					vir.setCustoMatVir(Double.parseDouble(campo[8]));
					virService.insertBackUp(vir);
					count += 1;
				}
			}	
			return count;
		}	
		catch(IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {
			sc.close();
		}
	}
}	
