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
import gui.sgomodel.dao.ProdutoDao;
import gui.sgomodel.entities.Grupo;
import gui.sgomodel.entities.Produto;
  
public class ProdutoDaoJDBC implements ProdutoDao {
	
// tb entra construtor p/ conex�o
	private Connection conn;
	
	String classe = "Produto JDBC";
	
	public ProdutoDaoJDBC (Connection conn) {
		this.conn = conn;
	}
	
 	@Override
	public void insert(Produto obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO produto " 
				      + "(GrupoProd, NomeProd, EstMinProd, SaldoProd, SaidaCmmProd, CmmProd, " 
					  + "PrecoProd, VendaProd, vidaKmProd, vidaMesProd, PercentualProd, LetraProd, "
					  + "DataCadastroProd, GrupoIdProd ) "  
  				      + "VALUES " +
				      "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
 					 Statement.RETURN_GENERATED_KEYS); 

  			st.setInt(1, obj.getGrupoProd());
			st.setString(2, obj.getNomeProd());
			st.setDouble(3, obj.getEstMinProd());
			st.setDouble(4, obj.getSaldoProd());
			st.setDouble(5, obj.getSaidaCmmProd());
			st.setDouble(6, obj.getCmmProd());
			st.setDouble(7, obj.getPrecoProd());
			st.setDouble(8, obj.getVendaProd());
			st.setInt(9, obj.getVidaKmProd());
			st.setInt(10, obj.getVidaMesProd());
			st.setDouble(11, obj.getPercentualProd());
			st.setString(12, String.valueOf(obj.getLetraProd()));
			st.setDate(13, new java.sql.Date(obj.getDataCadastroProd().getTime()));
			st.setInt(14, obj.getGrupo().getCodigoGru());

 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	int codigo = rs.getInt(1);
					obj.setCodigoProd(codigo);;
 				}
				else
				{	throw new DbException(classe + " Erro!!! sem inclusão" );
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
	public void update(Produto obj) {
		PreparedStatement st = null;
  		try {
			st = conn.prepareStatement(
  					"UPDATE produto " +  
  						"SET GrupoProd = ?, NomeProd = ?, EstMinProd = ?, SaldoProd = ?, "
  							+ "SaidaCmmProd = ?, CmmProd = ?, PrecoProd = ?, VendaProd = ?, "
  							+ "VidaKmProd = ?, VidaMesProd = ?, PercentualProd = ?, LetraProd = ?, "
  							+ "DataCadastroProd = ?, GrupoIdProd = ? "
  							+ "WHERE (CodigoProd = ?)",
        			 Statement.RETURN_GENERATED_KEYS);
			
  			st.setInt(1, obj.getGrupoProd());
			st.setString(2, obj.getNomeProd());
			st.setDouble(3, obj.getEstMinProd());
			st.setDouble(4, obj.getSaldoProd());
			st.setDouble(5, obj.getSaidaCmmProd());
			st.setDouble(6, obj.getCmmProd());
			st.setDouble(7, obj.getPrecoProd());
			st.setDouble(8, obj.getVendaProd());
			st.setInt(9, obj.getVidaKmProd());
			st.setInt(10, obj.getVidaMesProd());
			st.setDouble(11, obj.getPercentualProd());
			st.setString(12, String.valueOf(obj.getLetraProd()));
			st.setDate(13, new java.sql.Date(obj.getDataCadastroProd().getTime()));
			st.setInt(14, obj.getGrupo().getCodigoGru());
  			st.setInt(15, obj.getCodigoProd());
  			
 			st.executeUpdate();
   		} 
 		catch (SQLException e) {
 		throw new DbException (classe + " Erro!!! sem atualizaçâo " + e.getMessage()); }

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
					"DELETE FROM produto WHERE CodigoProd = ? ", 
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
	public Produto findById(Integer codigo) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(

					"SELECT *, grupo.CodigoGru " + 
						"FROM produto " +
							"INNER JOIN grupo " +
								"ON produto.GrupoIdProd = grupo.CodigoGru " +
						"WHERE CodigoProd = ? ");
 					
 			st.setInt(1, codigo);
			rs = st.executeQuery(); 

			List<Produto> list = new ArrayList<>();
			Map<Integer, Grupo> mapGru = new HashMap<>();
			if (rs.next())
			{	Grupo gru = mapGru.get(rs.getInt("GrupoIdProd"));
				if (gru == null)
				{	gru = instantiateGrupo(rs);
					mapGru.put(rs.getInt("GrupoIdProd"), gru);
				}	
				Produto obj = instantiateProduto(rs, gru);
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
	public List<Produto> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 

					"SELECT *, grupo.* " +  
						"FROM produto " +
 							"INNER JOIN grupo "  +
								"ON produto.GrupoIdProd = grupo.CodigoGru " + 
 					"ORDER BY NomeProd");
 			
			rs = st.executeQuery();
			
			List<Produto> list = new ArrayList<>();
			Map<Integer, Grupo> mapGru = new HashMap<>();
			
			while (rs.next())
			{	Grupo gru = mapGru.get(rs.getInt("GrupoIdProd"));
				if (gru == null)
				{	gru = instantiateGrupo(rs);
					mapGru.put(rs.getInt("GrupoIdProd"), gru);
				}	
				Produto obj = instantiateProduto(rs, gru);
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
	public List<Produto> findAllId() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 

					"SELECT *, grupo.* " +  
						"FROM produto " +
 							"INNER JOIN grupo "  +
								"ON produto.GrupoIdProd = grupo.CodigoGru " + 
 					"ORDER BY CodigoProd");
 			
			rs = st.executeQuery();
			
			List<Produto> list = new ArrayList<>();
			Map<Integer, Grupo> mapGru = new HashMap<>();
			
			while (rs.next())
			{	Grupo gru = mapGru.get(rs.getInt("GrupoIdProd"));
				if (gru == null)
				{	gru = instantiateGrupo(rs);
					mapGru.put(rs.getInt("GrupoIdProd"), gru);
				}	
				Produto obj = instantiateProduto(rs, gru);
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
	public List<Produto> findMVR() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 

					"SELECT *, grupo.* " +  
						"FROM produto " +
 							"INNER JOIN grupo "  +
								"ON produto.GrupoIdProd = grupo.CodigoGru " +
 							"WHERE percentualProd > 0 " +
 					"ORDER BY - PercentualProd");
 			
			rs = st.executeQuery();
			
			List<Produto> list = new ArrayList<>();
			Map<Integer, Grupo> mapGru = new HashMap<>();
			
			while (rs.next())
			{	Grupo gru = mapGru.get(rs.getInt("GrupoIdProd"));
				if (gru == null)
				{	gru = instantiateGrupo(rs);
					mapGru.put(rs.getInt("GrupoIdProd"), gru);
				}	
				Produto obj = instantiateProduto(rs, gru);
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
	public List<Produto> findPesquisa(String str) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 

					"SELECT *, grupo.* " +  
						"FROM produto " +
 							"INNER JOIN grupo "  +
								"ON produto.GrupoIdProd = grupo.CodigoGru " +
 						"WHERE nomeProd like ? " +
 					"ORDER BY NomeProd");
 			
			st.setString(1, str + "%");
			rs = st.executeQuery();
			
			List<Produto> list = new ArrayList<>();
			Map<Integer, Grupo> mapGru = new HashMap<>();
			
			while (rs.next())
			{	Grupo gru = mapGru.get(rs.getInt("GrupoIdProd"));
				if (gru == null)
				{	gru = instantiateGrupo(rs);
					mapGru.put(rs.getInt("GrupoIdProd"), gru);
				}	
				Produto obj = instantiateProduto(rs, gru);
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
	public void zeraAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"TRUNCATE TABLE sgb.Produto " );

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
	
 	private Produto instantiateProduto(ResultSet rs, Grupo gru) throws SQLException {
 		Produto produto = new Produto();
   		produto.setCodigoProd(rs.getInt("CodigoProd"));
  		produto.setGrupoProd(rs.getInt("GrupoProd"));
  		produto.setNomeProd(rs.getString("NomeProd"));
  		produto.setEstMinProd(rs.getDouble("EstMinProd"));
  		produto.setSaldoProd(rs.getDouble("SaldoProd"));
  		produto.setSaidaCmmProd(rs.getDouble("SaidaCmmProd"));
  		produto.setCmmProd(rs.getDouble("CmmProd"));
  		produto.setPrecoProd(rs.getDouble("PrecoProd"));
  		produto.setVendaProd(rs.getDouble("VendaProd"));
  		produto.setVidaKmProd(rs.getInt("VidaKmProd"));
  		produto.setVidaMesProd(rs.getInt("VidaMesProd"));
  		produto.setPercentualProd(rs.getDouble("PercentualProd"));
  		produto.setLetraProd(rs.getString("LetraProd").charAt(0));
  		produto.setDataCadastroProd(new java.util.Date(rs.getTimestamp("DataCadastroProd").getTime()));
  		produto.setGrupo(gru);
    	return produto;
	}
 
 	private Grupo instantiateGrupo(ResultSet rs) throws SQLException {
		Grupo gru = new Grupo();
		gru.setCodigoGru(rs.getInt("CodigoGru"));
		gru.setNomeGru(rs.getString("NomeGru"));
  		return gru;
 	}
}
