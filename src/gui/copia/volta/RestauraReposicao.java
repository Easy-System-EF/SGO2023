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
import gui.sgomodel.entities.ReposicaoVeiculo;
import gui.sgomodel.services.ReposicaoVeiculoService;
import gui.util.Cryptograf;

public class RestauraReposicao implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Integer restauraReposicao(Integer count, String unid, String meioSgo, String file, String ext) {
		@SuppressWarnings("unused")
		String status = "Ok";
		count = 0;
		String path = unid + meioSgo + file + ext;
		ReposicaoVeiculo rep = new ReposicaoVeiculo();		
		ReposicaoVeiculoService repService = new ReposicaoVeiculoService();
	 	SimpleDateFormat sdfAno = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	 	Calendar cal = Calendar.getInstance();

		FileReader fr;
		Scanner sc = null;
		String str = null;
		String str1 = null;
		String[] regRep = null;
		String campoRep = null;

		try {
			fr = new FileReader(path);
			sc = new Scanner(fr);
			while (sc.hasNextLine()) {
				str = sc.nextLine();		
				str1 = Cryptograf.desCriptografa(str);
				regRep = str1.split(" REPOSICAO ");
				for (int i = 1 ; i < regRep.length ; i++) {
					campoRep = regRep[i];
					String[] campo = campoRep.split(" , ");
					rep.setNumeroRep(Integer.parseInt(campo[0].replace("\s", "")));
					rep.setNumeroRep(null);
					rep.setOsRep(Integer.parseInt(campo[1]));
					Date dt1 = null;
					try {
						dt1 = sdfAno.parse(campo[2]);
					} 
					catch (ParseException e1) {
						e1.printStackTrace();
					}
					cal.setTime(dt1);
					rep.setDataRep(cal.getTime());
					rep.setPlacaRep(campo[3]);
					rep.setClienteRep(campo[4]);
					rep.setDddClienteRep(Integer.parseInt(campo[5]));
					rep.setTelefoneClienteRep(Integer.parseInt(campo[6]));
					rep.setCodigoMaterialRep(Integer.parseInt(campo[7]));
					rep.setMaterialRep(campo[8]);
					rep.setKmTrocaRep(Integer.parseInt(campo[9]));
					rep.setProximaKmRep(Integer.parseInt(campo[10]));
					try {
						dt1 = sdfAno.parse(campo[11]);
					} 
					catch (ParseException e) {
						e.printStackTrace();
					}
					cal.setTime(dt1);
					rep.setProximaDataRep(cal.getTime());
					repService.insert(rep);
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
