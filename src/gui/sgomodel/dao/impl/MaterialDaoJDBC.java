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
import gui.sgomodel.dao.MaterialDao;
import gui.sgomodel.entities.Grupo;
import gui.sgomodel.entities.Material;
  
public class MaterialDaoJDBC implements MaterialDao {
	
// tb entra construtor p/ conex�o
	private Connection conn;
	
	String classe = "Material JDBC ";
	
	public MaterialDaoJDBC (Connection conn) {
		this.conn = conn;
	}
	
 	@Override
	public void insert(Material obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO material " 
				      + "(GrupoMat, NomeMat, EstMinMat, EntradaMat, SaidaMat, SaldoMat, CmmMat, " 
					  + "PrecoMat, VendaMat, vidaKmMat, vidaMesMat, PercentualMat, LetraMat, "
					  + "DataCadastroMat, GrupoId ) "  
  				      + "VALUES " +
				      "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
 					 Statement.RETURN_GENERATED_KEYS); 

			st.setInt(1, obj.getGrupoMat());
			st.setString(2, obj.getNomeMat());
			st.setDouble(3, obj.getEstMinMat());
			st.setDouble(4, obj.getEntradaMat());
			st.setDouble(5, obj.getSaidaMat());
			st.setDouble(6, obj.getSaldoMat());
			st.setDouble(7, obj.getCmmMat());
			st.setDouble(8, obj.getPrecoMat());
			st.setDouble(9, obj.getVendaMat());
			st.setInt(10, obj.getVidaKmMat());
			st.setInt(11, obj.getVidaMesMat());
			st.setDouble(12, obj.getPercentualMat());
			st.setString(13, String.valueOf(obj.getLetraMat()));
			st.setDate(14, new java.sql.Date(obj.getDataCadastroMat().getTime()));
			st.setInt(15, obj.getGrupo().getCodigoGru());
  
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	int codigo = rs.getInt(1);
					obj.setCodigoMat(codigo);;
 				}
				else
				{	throw new DbException(classe + "Erro!!! sem inclusão" );
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
	public void insertBackUp(Material obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO material " 
						      + "(CodigoMat, GrupoMat, NomeMat, EstMinMat, EntradaMat, SaidaMat, SaldoMat, CmmMat, " 
							  + "PrecoMat, VendaMat, vidaKmMat, vidaMesMat, PercentualMat, LetraMat, "
							  + "DataCadastroMat, GrupoId ) "  
		  				      + "VALUES " +
						      "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
		 					 Statement.RETURN_GENERATED_KEYS); 

					st.setInt(1, obj.getCodigoMat());
					st.setInt(2, obj.getGrupoMat());
					st.setString(3, obj.getNomeMat());
					st.setDouble(4, obj.getEstMinMat());
					st.setDouble(5, obj.getEntradaMat());
					st.setDouble(6, obj.getSaidaMat());
					st.setDouble(7, obj.getSaldoMat());
					st.setDouble(8, obj.getCmmMat());
					st.setDouble(9, obj.getPrecoMat());
					st.setDouble(10, obj.getVendaMat());
					st.setInt(11, obj.getVidaKmMat());
					st.setInt(12, obj.getVidaMesMat());
					st.setDouble(13, obj.getPercentualMat());
					st.setString(14, String.valueOf(obj.getLetraMat()));
					st.setDate(15, new java.sql.Date(obj.getDataCadastroMat().getTime()));
					st.setInt(16, obj.getGrupo().getCodigoGru());
		  
 			st.executeUpdate();
			
  		}
 		catch (SQLException e) {
			throw new DbException("Erro!!! " + classe + " sem inclusão" + e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
 
	@Override
	public void update(Material obj) {
		PreparedStatement st = null;
  		try {
			st = conn.prepareStatement(
  					"UPDATE material " +  
  	  						"SET GrupoMat = ?, NomeMat = ?, EstMinMat = ?, EntradaMat = ?, SaidaMat = ?, SaldoMat = ?, "
  							+ "CmmMat = ?, PrecoMat = ?, VendaMat = ?, "
  							+ "VidaKmMat = ?, VidaMesMat = ?, PercentualMat = ?, LetraMat = ?, "
  							+ "DataCadastroMat = ?, GrupoId = ? "
  							+ "WHERE (CodigoMat = ?)",
        			 Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, obj.getGrupoMat());
			st.setString(2, obj.getNomeMat());
			st.setDouble(3, obj.getEstMinMat());
			st.setDouble(4, obj.getEntradaMat());
			st.setDouble(5, obj.getSaidaMat());
			st.setDouble(6, obj.getSaldoMat());
			st.setDouble(7, obj.getCmmMat());
			st.setDouble(8, obj.getPrecoMat());
			st.setDouble(9, obj.getVendaMat());
			st.setInt(10, obj.getVidaKmMat());
			st.setInt(11, obj.getVidaMesMat());
			st.setDouble(12, obj.getPercentualMat());
			st.setString(13, String.valueOf(obj.getLetraMat()));
			st.setDate(14, new java.sql.Date(obj.getDataCadastroMat().getTime()));
			st.setInt(15, obj.getGrupo().getCodigoGru());
			st.setInt(16, obj.getCodigoMat());
  
 			st.executeUpdate();
   		} 
 		catch (SQLException e) {
 		throw new DbException (classe + "Erro!!! sem atualização " + e.getMessage()); }

  		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer codigo) {
		PreparedStatement st = null;
//		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"DELETE FROM material WHERE CodigoMat = ? ", 
						Statement.RETURN_GENERATED_KEYS);
				  
			st.setInt(1, codigo);
			st.executeUpdate();
   		}
 		catch (SQLException e) {
			throw new DbException (classe + "Erro!!! não excluído " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public Material findById(Integer codigo) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(

					"SELECT *, grupo.CodigoGru " + 
						"FROM material " +
							"INNER JOIN grupo " +
								"ON material.GrupoId = grupo.CodigoGru " +
						"WHERE CodigoMat = ? ");
 					
 			st.setInt(1, codigo);
			rs = st.executeQuery(); 

			List<Material> list = new ArrayList<>();
			Map<Integer, Grupo> mapGru = new HashMap<>();
			if (rs.next())
			{	Grupo gru = mapGru.get(rs.getInt("GrupoId"));
				if (gru == null)
				{	gru = instantiateGrupo(rs);
					mapGru.put(rs.getInt("GrupoId"), gru);
				}	
				Material obj = instantiateMatMat(rs, gru);
				list.add(obj);
				return obj;
  			}
			return null;
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
	public List<Material> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 

					"SELECT *, grupo.* " +  
						"FROM material " +
 							"INNER JOIN grupo "  +
								"ON material.GrupoId = grupo.CodigoGru " + 
 					"ORDER BY NomeMat ");
 			
			rs = st.executeQuery();
			
			List<Material> list = new ArrayList<>();
			Map<Integer, Grupo> mapGru = new HashMap<>();
			
			while (rs.next())
			{	Grupo gru = mapGru.get(rs.getInt("GrupoId"));
				if (gru == null)
				{	gru = instantiateGrupo(rs);
					mapGru.put(rs.getInt("GrupoId"), gru);
				}	
				Material obj = instantiateMatMat(rs, gru);
				list.add(obj);
   			}
			return list;
			
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	} 
	
	@Override
	public List<Material> findAllId() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 

					"SELECT *, grupo.* " +  
						"FROM material " +
 							"INNER JOIN grupo "  +
								"ON material.GrupoId = grupo.CodigoGru " + 
 					"ORDER BY CodigoMat");
 			
			rs = st.executeQuery();
			
			List<Material> list = new ArrayList<>();
			Map<Integer, Grupo> mapGru = new HashMap<>();
			
			while (rs.next())
			{	Grupo gru = mapGru.get(rs.getInt("GrupoId"));
				if (gru == null)
				{	gru = instantiateGrupo(rs);
					mapGru.put(rs.getInt("GrupoId"), gru);
				}	
				Material obj = instantiateMatMat(rs, gru);
				list.add(obj);
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
	public List<Material> findMVR() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 

					"SELECT *, grupo.* " +  
						"FROM material " +
 							"INNER JOIN grupo "  +
								"ON material.GrupoId = grupo.CodigoGru " +
 							"WHERE PercentualMat > 0 " +
 					"ORDER BY - PercentualMat ");
 			
			rs = st.executeQuery();
			
			List<Material> list = new ArrayList<>();
			Map<Integer, Grupo> mapGru = new HashMap<>();
			
			while (rs.next())
			{	Grupo gru = mapGru.get(rs.getInt("GrupoId"));
				if (gru == null)
				{	gru = instantiateGrupo(rs);
					mapGru.put(rs.getInt("GrupoId"), gru);
				}	
				Material obj = instantiateMatMat(rs, gru);
				list.add(obj);
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
	public List<Material> findABC() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 

					"SELECT *, grupo.* " +  
						"FROM material " +
 							"INNER JOIN grupo "  +
								"ON material.GrupoId = grupo.CodigoGru " +
 							"WHERE PrecoMat > 0 " +
 					"ORDER BY PrecoMat ");
 			
			rs = st.executeQuery();
			
			List<Material> list = new ArrayList<>();
			Map<Integer, Grupo> mapGru = new HashMap<>();
			
			while (rs.next())
			{	Grupo gru = mapGru.get(rs.getInt("GrupoId"));
				if (gru == null)
				{	gru = instantiateGrupo(rs);
					mapGru.put(rs.getInt("GrupoId"), gru);
				}	
				Material obj = instantiateMatMat(rs, gru);
				list.add(obj);
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
	public List<Material> findPesquisa(String str) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT *, grupo.* " +  
						"FROM material " +
 							"INNER JOIN grupo "  +
								"ON material.GrupoId = grupo.CodigoGru " +
 						"WHERE nomeMat like ? " +
 					"ORDER BY NomeMat");
 			
			st.setString(1, str + "%");
			rs = st.executeQuery();
			
			List<Material> list = new ArrayList<>();
			Map<Integer, Grupo> mapGru = new HashMap<>();
			
			while (rs.next())
			{	Grupo gru = mapGru.get(rs.getInt("GrupoId"));
				if (gru == null)
				{	gru = instantiateGrupo(rs);
					mapGru.put(rs.getInt("GrupoId"), gru);
				}	
				Material obj = instantiateMatMat(rs, gru);
				list.add(obj);
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
	public Material findPesquisaUnico(String str) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT *, grupo.* " +  
						"FROM material " +
 							"INNER JOIN grupo "  +
								"ON material.GrupoId = grupo.CodigoGru " +
 						"WHERE nomeMat like ? " +
 					"ORDER BY NomeMat");
 			
			st.setString(1, str + "%");
			rs = st.executeQuery();
			
			Material mat = new Material();
			Grupo gru = new Grupo();
			
			while (rs.next()) {
				gru = instantiateGrupo(rs);
				mat = instantiateMatMat(rs, gru);
				return mat;
   			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	} 
	
 	private Material instantiateMatMat(ResultSet rs, Grupo gru) throws SQLException {
 		Material material = new Material();
   		material.setCodigoMat(rs.getInt("CodigoMat"));
  		material.setGrupoMat(rs.getInt("GrupoMat"));
  		material.setNomeMat(rs.getString("NomeMat"));
  		material.setEstMinMat(rs.getDouble("EstMinMat"));
  		material.setEntradaMat(rs.getDouble("EntradaMat"));
  		material.setSaidaMat(rs.getDouble("SaidaMat"));
  		material.setSaldoMat(rs.getDouble("SaldoMat"));
  		material.calculaCmm();
  		material.setPrecoMat(rs.getDouble("PrecoMat"));
  		material.setVendaMat(rs.getDouble("VendaMat"));
  		material.setVidaKmMat(rs.getInt("VidaKmMat"));
  		material.setVidaMesMat(rs.getInt("VidaMesMat"));
  		material.setPercentualMat(rs.getDouble("PercentualMat"));
  		material.setLetraMat(rs.getString("LetraMat").charAt(0));
  		material.setDataCadastroMat(new java.util.Date(rs.getTimestamp("DataCadastroMat").getTime()));
  		material.setGrupo(gru);
    	return material;
	}
 
 	private Grupo instantiateGrupo(ResultSet rs) throws SQLException {
		Grupo gru = new Grupo();
		gru.setCodigoGru(rs.getInt("CodigoGru"));
		gru.setNomeGru(rs.getString("NomeGru"));
  		return gru;
 	}
}
