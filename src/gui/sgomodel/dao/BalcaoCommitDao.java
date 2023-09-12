package gui.sgomodel.dao;

import java.util.List;

import gui.sgcpmodel.entities.consulta.ParPeriodo;
import gui.sgomodel.entities.Adiantamento;
import gui.sgomodel.entities.Balcao;
import gui.sgomodel.entities.Material;
import gui.sgomodel.entities.NotaFiscal;

public interface BalcaoCommitDao {

	void gravaBalcao(Balcao objBal, ParPeriodo objPer, NotaFiscal objNf, Adiantamento objAdi, List<Material> listMat);
}
