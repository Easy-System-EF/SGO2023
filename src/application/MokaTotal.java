package application;

import java.text.ParseException;
import java.util.List;
import java.util.Scanner;

import gui.sgomodel.entities.Grupo;
import gui.sgomodel.entities.Material;
import gui.sgomodel.entities.Produto;
import gui.sgomodel.services.GrupoService;
import gui.sgomodel.services.MaterialService;
import gui.sgomodel.services.ProdutoService;

public class MokaTotal {

	public static void main(String[] args) throws ParseException {
		Material mat = new Material();
		MaterialService matService = new MaterialService();
		Produto prod = new Produto();
		ProdutoService prodService = new ProdutoService();
		Grupo gru = new Grupo();
		GrupoService gruService = new GrupoService();
		int cod = 0;

/*		
		List<Material> listMat = matService.findAll();
		for (Material m : listMat) {
			prod.setCodigoProd(null);
			cod = m.getGrupoMat();
			prod.setGrupoProd(cod);
			gru = gruService.findById(cod);
			prod.setNomeProd(m.getNomeMat());
			prod.setEstMinProd(m.getEstMinMat());
			prod.setSaidaCmmProd(m.getSaidaCmmMat());
			prod.setSaldoProd(m.getSaldoMat());
			prod.setPrecoProd(m.getPrecoMat());
			prod.setVendaProd(m.getVendaMat());
//			LocalDate dt1 = DataStatic.dateParaLocal(m.getDataCadastroMat());
//			Date dt2 = DataStatic.localParaDateFormatada(dt1);
System.out.println(m.getCodigoMat());			
			prod.setVidaKmProd(m.getVidaKmMat());
			prod.setVidaMesProd(m.getVidaMesMat());
			prod.setCmmProd(m.getCmmMat());
//			Date data = m.getDataCadastroMat();
			prod.setDataCadastroProd(m.getDataCadastroMat());
			prod.setGrupo(gru);
			prod.setPercentualProd(0.00);
//			prod.calculaPercentual();
			prod.setLetraProd('A');
			prod.setGrupo(gru);
			prodService.saveOrUpdate(prod);
		}
*/
		
		double totalCst = 0.00;
		List<Produto> listProd0 = prodService.findAll();
		for (Produto p0 : listProd0) {
			p0.setLetraProd(' ');
			if (p0.getPrecoProd() > 0.00 && p0.getVendaProd() > 0.00 && p0.getSaldoProd() > 0) {
				if (p0.getSaldoProd() > 0) {
System.out.println("p0 " + p0.getCodigoProd() + " " + p0.getSaldoProd() + " " + (p0.getPrecoProd() * p0.getSaldoProd()));				
					totalCst += p0.getPrecoProd() * p0.getSaldoProd();
				}	
			}
		}
		List<Produto> listProd = prodService.findAll();
		for (Produto p : listProd) {
			if (p.getSaldoProd() > 0.00) {
				if (p.getPrecoProd() > 0.00) {
					if (p.getVendaProd() > 0.00) {
System.out.println("p " + p.getCodigoProd());						
						p.calculaPercentual(totalCst);
						prodService.saveOrUpdate(p);
					}
				}	
			}
		}
		
		List<Produto> listProd2 = prodService.findMVR();
		for (Produto p2 : listProd2) {
			if (p2.getSaldoProd() > 0.00) {
System.out.println("p2 " + p2.getCodigoProd());				
				if (p2.getPercentualProd() > 0) {
					p2.letraClassCli();
					prodService.saveOrUpdate(p2);
				}	
			}	
		}
		
		int count = 0;
		Scanner sc = new Scanner(System.in);
		List<Produto> listProd3 = prodService.findAll();
		for (Produto p3 : listProd3) {
			mat.setCodigoMat(p3.getCodigoProd());
			mat.setGrupoMat(p3.getGrupoProd());
			cod = p3.getGrupoProd();
			prod.setGrupoProd(cod);
			gru = gruService.findById(cod);
			mat.setNomeMat(p3.getNomeProd());
			mat.setEstMinMat(p3.getEstMinProd());
			mat.setSaldoMat(p3.getSaldoProd());
			mat.setSaidaCmmMat(p3.getSaidaCmmProd());
			mat.setCmmMat(p3.getCmmProd());
			mat.setPrecoMat(p3.getPrecoProd());
			mat.setVendaMat(p3.getVendaProd());
			mat.setVidaKmMat(p3.getVidaKmProd());
			mat.setVidaMesMat(p3.getVidaMesProd());
			mat.setPercentualClass(p3.getPercentualProd());
			if (p3.getPercentualProd() != 0) {
				mat.setLetraClass(' ');
			} else {
				mat.setLetraClass(p3.getLetraProd());
			}	
			mat.setDataCadastroMat(p3.getDataCadastroProd());
			mat.setGrupo(gru);
			if (p3.getCodigoProd() == 5 || p3.getCodigoProd() == 6 || p3.getCodigoProd() == 8 || p3.getCodigoProd() == 9) {
				System.out.println("p3 faltantes " + p3);
//				int nada = sc.nextInt();
			}
			matService.saveOrUpdate(mat);
			Material mat1 = matService.findById(mat.getCodigoMat());
			if (mat1 == null) {
				count  += 1;
				System.out.println("mat1 " + count + " " + mat1); 
			}
		}
		List<Material> mat4 = matService.findAll();
		count = 0;
		for (Material m : mat4) {
			count += 1;
			System.out.println(count + " " + m);
		}
		count = 0;
		System.out.println();
		System.out.println(" ******************** ");
		System.out.println();
		for (Produto p : listProd3) {
			count += 1;
			System.out.println(count + " " + p);			
		}
		System.out.println();
		System.out.println(" ******************** ");
		System.out.println();
		System.out.println("sizes " + listProd3.size() + " " + mat4.size());
		sc.close();
	}
}	
		
//		
//		double acum = 0.00;
//		List<Material> listMat = matService.findAll();
//		for (Material m : listMat) {
//			if (m.getPrecoMat() > 0 && m.getVendaMat() > 0) {
//				Grupo gru = new Grupo();
//				GrupoService gruService = new GrupoService();
//				gru = gruService.findById(m.getGrupoMat());
//				m.setGrupo(gru);
//System.out.println(gru);				
//				m.setPercentualClass(0.00);
//				m.setLetraClass('X');
//System.out.println(m.getCodigoMat() + " " + m.getPercentualClass());
//System.out.println(m);
//				m.calculaPercentual();
//				acum += m.getPercentualClass();
//				m.letraClassCli(acum);
//				matService.saveOrUpdate(mat);
//			}	
//		}
//	}

