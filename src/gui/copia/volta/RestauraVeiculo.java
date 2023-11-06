package gui.copia.volta;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

import db.DbException;
import gui.sgomodel.entities.Veiculo;
import gui.sgomodel.services.VeiculoService;
import gui.util.Cryptograf;

public class RestauraVeiculo implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Integer restauraVeiculo(Integer count, String unid, String meioSgo, String file, String ext) {
		@SuppressWarnings("unused")
		String status = "Ok";
		count = 0;
		String path = unid + meioSgo + file + ext;
		Veiculo vei = new Veiculo();		
		VeiculoService veiService = new VeiculoService();

		FileReader fr;
		Scanner sc = null;
		String str = null;
		String str1 = null;
		String[] regVei = null;
		String campoVei = null;

		try {
			fr = new FileReader(path);
			sc = new Scanner(fr);
			while (sc.hasNextLine()) {
				str = sc.nextLine();		
				str1 = Cryptograf.desCriptografa(str);
				regVei = str1.split(" VEICULO ");
				for (int i = 1 ; i < regVei.length ; i++) {
					campoVei = regVei[i];
					String[] campo = campoVei.split(" , ");
					vei.setNumeroVei(Integer.parseInt(campo[0]));
					vei.setPlacaVei(campo[1]);
					vei.setKmInicialVei(Integer.parseInt(campo[2]));
					vei.setKmFinalVei(Integer.parseInt(campo[3]));
					vei.setModeloVei(campo[4]);
					vei.setAnoVei(Integer.parseInt(campo[5]));
					veiService.insertBackUp(vei);
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
