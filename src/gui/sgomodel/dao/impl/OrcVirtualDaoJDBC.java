package gui.sgomodel.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import gui.sgomodel.dao.OrcVirtualDao;
import gui.sgomodel.entities.Grupo;
import gui.sgomodel.entities.Material;
import gui.sgomodel.entities.OrcVirtual;
import gui.sgomodel.services.GrupoService;
 
public class OrcVirtualDaoJDBC implements OrcVirtualDao {
	
// tb entra construtor p/ conex�o
	private Connection conn;
	
	public OrcVirtualDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	String classe = "Virtual JDBC ";
	
	@Override
	public void insert(OrcVirtual obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO orcVirtual " +
				      "(NomeMatVir, QuantidadeMatVir, PrecoMatVir, TotalMatVir, NumeroOrcVir, "
				      + "NumeroBalVir, MaterialId, custoMatVir )"
  				      + "VALUES " +
				      "(?, ?, ?, ?, ?, ?, ?, ? )",
 					 Statement.RETURN_GENERATED_KEYS); 

			st.setString(1, obj.getMaterial().getNomeMat());
  			st.setDouble(2, obj.getQuantidadeMatVir()); 
   			st.setDouble(3, obj.getPrecoMatVir());
 			st.setDouble(4, obj.getTotalMatVir());
 			st.setInt(5, obj.getNumeroOrcVir());
 			st.setInt(6, obj.getNumeroBalVir());
 			st.setInt(7, obj.getMaterial().getCodigoMat());
 			st.setDouble(8, obj.getCustoMatVir());
  			
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	int codigo = rs.getInt(1);
					obj.setNumeroVir(codigo);
//					System.out.println("Novo inserido: " + obj.getNumeroVir());
				}
				else
				{	throw new DbException("Erro!!! " + classe + "sem inclusão" );
				}
			}	
  		}
 		catch (SQLException e) {
			throw new DbException (e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
 
	@Override
	public void update(OrcVirtual obj) {
		PreparedStatement st = null;
  		try {
			st = conn.prepareStatement(
					"UPDATE orcVirtual " +  
							"SET NomeMatVir = ?, "
							+ "QuantidadeMatVir = ?, "
							+ "PrecoMatVir = ?, "
							+ "TotalMatVir = ?, "
							+ "NumeroOrcVir = ?, "
							+ "NumeroBalVir = ?, "
							+ "MaterialId = ?, "
							+ "CustoMatVir = ? "
  					+ "WHERE (NumeroVir = ?)",
        			 Statement.RETURN_GENERATED_KEYS);

			st.setString(1,  obj.getMaterial().getNomeMat());
  			st.setDouble(2, obj.getQuantidadeMatVir()); 
   			st.setDouble(3, obj.getPrecoMatVir());
 			st.setDouble(4, obj.getTotalMatVir());
 			st.setInt(5, obj.getNumeroOrcVir());
 			st.setInt(6, obj.getNumeroBalVir());
 			st.setInt(7, obj.getMaterial().getCodigoMat());
 			st.setDouble(8, obj.getCustoMatVir());
			st.setInt(9,  obj.getNumeroVir());
			
 			st.executeUpdate();
   		} 
 		catch (SQLException e) {
 				throw new DbException ( "Erro!!! " + classe + "sem atualização " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteByIdOrc(int numOrc) {
		PreparedStatement st = null;
   		try {
			st = conn.prepareStatement(
					"DELETE FROM orcVirtual WHERE NumeroOrcVir = ? ", Statement.RETURN_GENERATED_KEYS);
				  
			st.setInt(1, numOrc);
			st.executeUpdate();
   		}
 		catch (SQLException e) {
			throw new DbException ( "Erro!!! " + classe + "não excluído " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public Double findByTotalOrc(Integer numOrc) {
		Double totOrc = null;
		PreparedStatement st = null;
		ResultSet rs = null;
   		try {
			st = conn.prepareStatement(
					"select orcVirtual.TotalMatVir, orcVirtual.NumeroOrcVir, " +
					  "sum(TotalMatVir) AS total from orcVirtual WHERE NumeroOrcVir = ? "); 
			
			st.setInt(1, numOrc);
			rs = st.executeQuery();
			while (rs.next()) {
				totOrc = rs.getDouble("total");
			}	
   		}
 		catch (SQLException e) {
			throw new DbException ( "Erro!!! " + classe + "não totalizado " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
		return totOrc;
	}

	@Override
	public void deleteByIdBal(int numBal) {
		PreparedStatement st = null;
   		try {
			st = conn.prepareStatement(
					"DELETE FROM orcVirtual WHERE NumeroBalVir = ? ", Statement.RETURN_GENERATED_KEYS);
				  
			st.setInt(1, numBal);
			st.executeUpdate();
   		}
 		catch (SQLException e) {
			throw new DbException ( "Erro!!! " + classe + "não excluído " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public Double findByTotalBal(Integer numBal) {
		Double totBal = null;
		PreparedStatement st = null;
		ResultSet rs = null;
   		try {
			st = conn.prepareStatement(
					"select orcVirtual.TotalMatVir, orcVirtual.NumeroBalVir, " +
					  "sum(TotalMatVir) AS total from orcVirtual WHERE NumeroBalVir = ? "); 
			
			st.setInt(1, numBal);
			rs = st.executeQuery();
			while (rs.next()) {
				totBal = rs.getDouble("total");
			}	
   		}
 		catch (SQLException e) {
			throw new DbException ( "Erro!!! " + classe + "não totalizado " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
 			DB.closeResultSet(rs);
		}
		return totBal;
	}

	@Override
	public void deleteByIdVir(int numVir) {
		PreparedStatement st = null;
   		try {
			st = conn.prepareStatement(
					"DELETE FROM orcVirtual WHERE NumeroVir = ? ", Statement.RETURN_GENERATED_KEYS);
				  
			st.setInt(1, numVir);
			st.executeUpdate();
   		}
 		catch (SQLException e) {
			throw new DbException ( "Erro!!! " + classe + "não excluído " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public List<OrcVirtual> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT *, material.* " 
						+ "FROM orcVirtual " 
							+ "INNER JOIN material "
								+ "ON orcVirtual.MaterialId = material.CodigoMat " 
					+ "ORDER BY - NumeroOrcVir");
			
			rs = st.executeQuery();
			
			List<OrcVirtual> list = new ArrayList<>();
			Map<Integer, Material> mapMat = new HashMap<>();
			
			while (rs.next())
			{	@SuppressWarnings("unlikely-arg-type")
			Material mat = mapMat.get("MaterialId");
				if (mat == null)
				{	mat = instantiateMaterial(rs);
					mapMat.put(rs.getInt("MaterialId"), mat);
				}
				OrcVirtual orcVir = instantiateOrcVirtual(rs, mat);
				list.add(orcVir);
 			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	} 
	
	@Override
	public List<OrcVirtual> findByOrcto(Integer orc) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT *, material.* " +
						 "FROM orcVirtual " +
							 "INNER JOIN material " +
								 "ON orcVirtual.MaterialId = material.CodigoMat " +
					"WHERE NumeroOrcVir = ? " +		
					   "ORDER BY NumeroOrcVir");
			
			st.setInt(1, orc);
			rs = st.executeQuery();
			
			List<OrcVirtual> list = new ArrayList<>();
			Map<Integer, Material> mapMat = new HashMap<>();
			
			while (rs.next())
			{	@SuppressWarnings("unlikely-arg-type")
			Material mat = mapMat.get("MaterialId");
				if (mat == null)
				{	mat = instantiateMaterial(rs);
					mapMat.put(rs.getInt("MaterialId"), mat);
				}
				OrcVirtual orcVir = instantiateOrcVirtual(rs, mat);
				list.add(orcVir);
 			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	} 
	
	@Override
	public List<OrcVirtual> findByBalcao(Integer bal) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT *, material.* " +
						 "FROM orcVirtual " +
							 "INNER JOIN material " +
								 "ON orcVirtual.MaterialId = material.CodigoMat " +
					"WHERE NumeroBalVir = ? " +		
					   "ORDER BY NumeroBalVir");
			
			st.setInt(1, bal);
			rs = st.executeQuery();
			
			List<OrcVirtual> list = new ArrayList<>();
			Map<Integer, Material> mapMat = new HashMap<>();
			
			while (rs.next())
			{	@SuppressWarnings("unlikely-arg-type")
			Material mat = mapMat.get("MaterialId");
				if (mat == null)
				{	mat = instantiateMaterial(rs);
					mapMat.put(rs.getInt("MaterialId"), mat);
				}
				OrcVirtual orcVir = instantiateOrcVirtual(rs, mat);
				list.add(orcVir);
 			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	} 
	
	@Override
	public List<OrcVirtual> findPesquisa(String str) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT *, material.* " +
						 "FROM orcVirtual " +
							 "INNER JOIN material " +
								 "ON orcVirtual.MaterialId = material.CodigoMat " +
					"WHERE NomeMatVir like ? " +		
					   "ORDER BY NumeroBalVir");
			
			st.setString(1, str);
			rs = st.executeQuery();
			
			List<OrcVirtual> list = new ArrayList<>();
			Map<Integer, Material> mapMat = new HashMap<>();
			
			while (rs.next())
			{	@SuppressWarnings("unlikely-arg-type")
			Material mat = mapMat.get("MaterialId");
				if (mat == null)
				{	mat = instantiateMaterial(rs);
					mapMat.put(rs.getInt("MaterialId"), mat);
				}
				OrcVirtual orcVir = instantiateOrcVirtual(rs, mat);
				list.add(orcVir);
 			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	} 
	
	@Override
	public void zeroAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"TRUNCATE TABLE sgo.dadosFechamento " );

			st.executeUpdate();

		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	} 
	
	private OrcVirtual instantiateOrcVirtual(ResultSet rs, Material mat) throws SQLException {
 		OrcVirtual vir = new OrcVirtual();
 		vir.setNumeroVir(rs.getInt("NumeroVir"));
 		vir.setNomeMatVir(rs.getString("NomeMatVir"));
 		vir.setQuantidadeMatVir(rs.getDouble("QuantidadeMatVir"));
 		vir.setPrecoMatVir(rs.getDouble("PrecoMatVir"));
 		vir.setTotalMatVir(rs.getDouble("TotalMatVir"));
 		vir.setNumeroOrcVir(rs.getInt("NumeroOrcVir"));
 		vir.setNumeroBalVir(rs.getInt("NumeroBalVir"));
 		vir.setMaterial(mat);
 		vir.setCustoMatVir(rs.getDouble("CustoMatVir"));
        return vir;
	}

	private Material instantiateMaterial(ResultSet rs) throws SQLException {
		Grupo grupo = new Grupo();
		GrupoService gruService = new GrupoService();
 		Material material = new Material();
   		material.setCodigoMat(rs.getInt("CodigoMat"));
  		material.setGrupoMat(rs.getInt("GrupoMat"));
  		grupo = gruService.findById(material.getGrupoMat());
  		material.setNomeMat(rs.getString("NomeMat"));
  		material.setPrecoMat(rs.getDouble("PrecoMat"));
  		material.setVendaMat(rs.getDouble("VendaMat"));
  		material.setVidaKmMat(rs.getInt("VidaKmMat"));
  		material.setVidaMesMat(rs.getInt("VidaMesMat"));
  		material.setCmmMat(rs.getDouble("CmmMat"));
  		material.setDataCadastroMat(new java.util.Date(rs.getTimestamp("DataCadastroMat").getTime()));
  		material.setGrupo(grupo);
    	return material;
	}
}
