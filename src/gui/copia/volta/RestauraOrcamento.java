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
import gui.sgomodel.entities.Orcamento;
import gui.sgomodel.services.ClienteService;
import gui.sgomodel.services.FuncionarioService;
import gui.sgomodel.services.OrcamentoService;
import gui.util.Cryptograf;

public class RestauraOrcamento implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Integer restauraOrcamento(Integer count, String unid, String meioSgo, String file, String ext) {
		@SuppressWarnings("unused")
		String status = "Ok";
		count = 0;
		String path = unid + meioSgo + file + ext;
		Orcamento orc = new Orcamento();		
		OrcamentoService orcService = new OrcamentoService();
		ClienteService cliService = new ClienteService();
		FuncionarioService funService = new FuncionarioService(); 
	 	SimpleDateFormat sdfAno = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	 	Calendar cal = Calendar.getInstance();

		FileReader fr;
		Scanner sc = null;
		String str = null;
		String str1 = null;
		String[] regOrc = null;
		String campoOrc = null;

		try {
			fr = new FileReader(path);
			sc = new Scanner(fr);
			while (sc.hasNextLine()) {
				str = sc.nextLine();		
				str1 = Cryptograf.desCriptografa(str);
				regOrc = str1.split(" ORCTO ");
				for (int i = 1 ; i < regOrc.length ; i++) {
					campoOrc = regOrc[i];
					String[] campo = campoOrc.split(" , ");
					orc.setNumeroOrc(Integer.parseInt(campo[0].replace("\s", "")));
					Date dataOrc = null;
					try {
						dataOrc = sdfAno.parse(campo[1]);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					cal.setTime(dataOrc);
					orc.setDataOrc(cal.getTime());
					orc.setClienteOrc(campo[2]);
					orc.setFuncionarioOrc(campo[3]);
					orc.setPlacaOrc(campo[4]);
					orc.setKmInicialOrc(Integer.parseInt(campo[5]));
					orc.setKmFinalOrc(Integer.parseInt(campo[6]));
					orc.setDescontoOrc(Double.parseDouble(campo[7]));
					orc.setTotalOrc(Double.parseDouble(campo[8]));
					orc.setOsOrc(Integer.parseInt(campo[9]));
					orc.setMesOrc(Integer.parseInt(campo[10]));
					orc.setAnoOrc(Integer.parseInt(campo[11]));
					orc.setCliente(cliService.findById(Integer.parseInt(campo[12])));
					orc.setFuncionario(funService.findById(Integer.parseInt(campo[13])));
					orcService.insertBackUp(orc);
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
