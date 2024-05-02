package gui.sgomodel.dao;

import java.util.List;

import gui.sgcpmodel.entities.consulta.ParPeriodo;
import gui.sgomodel.entities.Material;
import gui.sgomodel.entities.NotaFiscal;
import gui.sgomodel.entities.Orcamento;
import gui.sgomodel.entities.OrdemServico;
import gui.sgomodel.entities.Veiculo;

public interface OSCommitDao {

	void gravaOS(OrdemServico objOs, Orcamento objOrc, ParPeriodo objPer, NotaFiscal objNf, Veiculo objVei, 
			double maoObra, List<Material> listMat);
}
