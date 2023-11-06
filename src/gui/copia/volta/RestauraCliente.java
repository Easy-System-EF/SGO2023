package gui.copia.volta;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

import db.DbException;
import gui.sgomodel.entities.Cliente;
import gui.sgomodel.services.ClienteService;
import gui.util.Cryptograf;

public class RestauraCliente implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Integer restauraCliente(Integer count, String unid, String meioSgo, String file, String ext) {
		@SuppressWarnings("unused")
		String status = "Ok";
		count = 0;
		String path = unid + meioSgo + file + ext;
		Cliente cliente = new Cliente();		
		ClienteService clienteService = new ClienteService();

		FileReader fr;
		Scanner sc = null;
		String str = null;
		String str1 = null;
		String[] regCli = null;
		String campoCli = null;

		try {
			fr = new FileReader(path);
			sc = new Scanner(fr);
			while (sc.hasNextLine()) {
				str = sc.nextLine();		
				str1 = Cryptograf.desCriptografa(str);
				regCli = str1.split(" CLIENTE ");
				for (int i = 1 ; i < regCli.length ; i++) {
					campoCli = regCli[i];
					String[] campo = campoCli.split(" , ");
					cliente.setCodigoCli(Integer.parseInt(campo[0].replace("\s", "")));
					cliente.setNomeCli(campo[1]);
					cliente.setRuaCli(campo[2]);
					cliente.setNumeroCli(Integer.parseInt(campo[3]));
					cliente.setComplementoCli(campo[4]);
					cliente.setBairroCli(campo[5]);
					cliente.setCidadeCli(campo[6]);
					cliente.setUfCli(campo[7]);
					cliente.setCepCli(campo[8]);
					cliente.setDdd01Cli(Integer.parseInt(campo[9]));
					cliente.setTelefone01Cli(Integer.parseInt(campo[10]));
					cliente.setDdd02Cli(Integer.parseInt(campo[11]));
					cliente.setTelefone02Cli(Integer.parseInt(campo[12]));
					cliente.setEmailCli(campo[13]);
					cliente.setPessoaCli(campo[14].charAt(0));
					cliente.setCpfCli(campo[15]);
					cliente.setCnpjCli(campo[16]);
					cliente.setValorClass(Double.parseDouble(campo[17]));
					cliente.setPercentualClass(Double.parseDouble(campo[18]));
					cliente.setLetraClass(campo[19].charAt(0));
					cliente.setVisitaClass(Integer.parseInt(campo[20]));
					clienteService.insertBackUp(cliente);					
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
