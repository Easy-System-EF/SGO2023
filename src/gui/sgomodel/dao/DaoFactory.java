package gui.sgomodel.dao;

import db.DB;
import gui.sgomodel.dao.impl.AdiantamentoDaoJDBC;
import gui.sgomodel.dao.impl.AnosDaoJDBC;
import gui.sgomodel.dao.impl.BalcaoCommitDaoJDBC;
import gui.sgomodel.dao.impl.BalcaoDaoJDBC;
import gui.sgomodel.dao.impl.CargoDaoJDBC;
import gui.sgomodel.dao.impl.ClienteDaoJDBC;
import gui.sgomodel.dao.impl.EmpresaDaoJDBC;
import gui.sgomodel.dao.impl.EntradaDaoJDBC;
import gui.sgomodel.dao.impl.FechamentoAnualDaoJDBC;
import gui.sgomodel.dao.impl.FechamentoMesDaoJDBC;
import gui.sgomodel.dao.impl.FolhaMesDaoJDBC;
import gui.sgomodel.dao.impl.FuncionarioDaoJDBC;
import gui.sgomodel.dao.impl.GrupoDaoJDBC;
import gui.sgomodel.dao.impl.LoginDaoJDBC;
import gui.sgomodel.dao.impl.MaterialDaoJDBC;
import gui.sgomodel.dao.impl.MesesDaoJDBC;
import gui.sgomodel.dao.impl.NotaFiscalJDBC;
import gui.sgomodel.dao.impl.OSCommitDaoJDBC;
import gui.sgomodel.dao.impl.OrcVirtualDaoJDBC;
import gui.sgomodel.dao.impl.OrcamentoDaoJDBC;
import gui.sgomodel.dao.impl.OrdemServicoDaoJDBC;
import gui.sgomodel.dao.impl.ProdutoDaoJDBC;
import gui.sgomodel.dao.impl.ReceberDaoJDBC;
import gui.sgomodel.dao.impl.ReposicaoVeiculoDaoJDBC;
import gui.sgomodel.dao.impl.SituacaoDaoJDBC;
import gui.sgomodel.dao.impl.VeiculoDaoJDBC;

public class DaoFactory {

/*
 * � obrigat�rio passar uma conex�o como argumento (db.getCon...)
 */
 	public static ClienteDao createClienteDao() {
		return new ClienteDaoJDBC(DB.getConnection());
	}
	
 	public static GrupoDao createGrupoDao() {
		return new GrupoDaoJDBC(DB.getConnection());
	}
	
  	public static MaterialDao createMaterialDao() {
		return new MaterialDaoJDBC(DB.getConnection());
	}
	
  	public static ProdutoDao createProdutoDao() {
		return new ProdutoDaoJDBC(DB.getConnection());
	}
	
  	public static CargoDao createCargoDao() {
		return new CargoDaoJDBC(DB.getConnection());
	}
	
  	public static SituacaoDao createSituacaoDao() {
		return new SituacaoDaoJDBC(DB.getConnection());
	}
	
  	public static FuncionarioDao createFuncionarioDao() {
		return new FuncionarioDaoJDBC(DB.getConnection());
	}
	
  	public static OrcVirtualDao createOrcVirtualDao() {
		return new OrcVirtualDaoJDBC(DB.getConnection());
	}
	
  	public static OrcamentoDao createOrcamentoDao() {
		return new OrcamentoDaoJDBC(DB.getConnection());
	}
	
  	public static OrdemServicoDao createOrdemServicoDao() {
		return new OrdemServicoDaoJDBC(DB.getConnection());
	}	

  	public static OSCommitDao createOSCommitDao() {
		return new OSCommitDaoJDBC(DB.getConnection());
	}	

  	public static ReceberDao createReceberDao() {
		return new ReceberDaoJDBC(DB.getConnection());
	}
  	
  	public static ReposicaoVeiculoDao createReposicaoVeiculoDao() {
		return new ReposicaoVeiculoDaoJDBC(DB.getConnection());
	}
  	public static VeiculoDao createVeiculoDao() {
		return new VeiculoDaoJDBC(DB.getConnection());
	}
  	public static EntradaDao createEntradaDao() {
		return new EntradaDaoJDBC(DB.getConnection());
	}  	
  	public static AdiantamentoDao createAdiantamentoDao() {
		return new AdiantamentoDaoJDBC(DB.getConnection());
	}
  	public static MesesDao createMesesDao() {
		return new MesesDaoJDBC(DB.getConnection());
	}
  	public static FolhaMesDao createFolhaMesDao() {
		return new FolhaMesDaoJDBC(DB.getConnection());
	}
  	public static FechamentoMesDao createFechamentoMesDao() {
		return new FechamentoMesDaoJDBC(DB.getConnection());
	}
  	
  	public static FechamentoAnualDao createFechamentoAnualDao() {
		return new FechamentoAnualDaoJDBC(DB.getConnection());
	}
  	public static AnosDao createAnosDao() {
		return new AnosDaoJDBC(DB.getConnection());
	}
  	public static LoginDao createLoginDao() {
		return new LoginDaoJDBC(DB.getConnection());
	}
  	public static EmpresaDao createEmpresaDao() {
		return new EmpresaDaoJDBC(DB.getConnection());
	}
  	public static BalcaoDao createBalcaoDao() {
		return new BalcaoDaoJDBC(DB.getConnection());
	}
  	
  	public static BalcaoCommitDao createBalcaoCommitDao() {
		return new BalcaoCommitDaoJDBC(DB.getConnection());
	}	

  	public static NotaFiscalDao createNotaFiscalDao() {
		return new NotaFiscalJDBC(DB.getConnection());
	}
}
