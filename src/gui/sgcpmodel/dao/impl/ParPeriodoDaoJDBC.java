package gui.sgcpmodel.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import gui.sgcpmodel.dao.ParPeriodoDao;
import gui.sgcpmodel.entities.Fornecedor;
import gui.sgcpmodel.entities.TipoConsumo;
import gui.sgcpmodel.entities.consulta.ParPeriodo;

public class ParPeriodoDaoJDBC implements ParPeriodoDao {

// aq entra um construtor declarando a conex�o	
	private Connection conn;
	
	public ParPeriodoDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	String classe = "ParPeriodo ";

	@Override
	public void update(ParPeriodo obj) {
		PreparedStatement st = null;
  		try {
			st = conn.prepareStatement(
					"UPDATE parPeriodo " +
  							"SET DtiPeriodo = ?, " +
  								"DtfPeriodo = ?, " +
  								"FornecedorIdPer = ?, " +
  								"TipoIdPer = ? " +
  									"WHERE (IdPeriodo = ?)",
  										Statement.RETURN_GENERATED_KEYS);

  			st.setDate(1, new java.sql.Date(obj.getDtiPeriodo().getTime()));
 			st.setDate(2, new java.sql.Date(obj.getDtfPeriodo().getTime()));
 			st.setInt(3, obj.getFornecedor().getCodigo());
 			st.setInt(4, obj.getTipoConsumo().getCodigoTipo());
 			st.setInt(5,  obj.getIdPeriodo());
     			
			st.executeUpdate();
   		} 
 		catch (SQLException e) {
 		throw new DbException ( "Erro!!! sem atualiza��o " + classe  + e.getMessage()); }

  		finally {
 			DB.closeStatement(st);
		}
	}
 	
	@Override
	public void insert(ParPeriodo obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO parPeriodo " +
  							"(DtiPeriodo, DtfPeriodo, FornecedorIdPer, TipoIdPer ) " +
  								"VALUE " +
  									"(?, ?, ?, ?) ", +
  										Statement.RETURN_GENERATED_KEYS);

  			st.setDate(1, new java.sql.Date(obj.getDtiPeriodo().getTime()));
 			st.setDate(2, new java.sql.Date(obj.getDtfPeriodo().getTime()));
 			st.setInt(3, obj.getFornecedor().getCodigo());
 			st.setInt(4, obj.getTipoConsumo().getCodigoTipo());
     			
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0) {
				rs = st.getGeneratedKeys();
				if (rs.next()) {
					int codigo = rs.getInt(1);
					obj.setIdPeriodo(codigo);
//					System.out.println("Novo inserido: " + obj.getCodigo());
				} else {
					throw new DbException("Erro!!! sem inclusão " + classe + classe );
				}	
	  		}
  		} 
 		catch (SQLException e) {
 		throw new DbException ( "Erro!!! sem atualização " + e.getMessage()); }

  		finally {
 			DB.closeStatement(st);
		}
	}
 	
 	@Override
	public List<ParPeriodo> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
  			
			"SELECT *, fornecedor.Codigo, tipoConsumo.codigoTipo " + 
				"FROM ParPeriodo " +
			 		"INNER JOIN fornecedor " +
			 			"on parPeriodo.FornecedorIdPer = fornecedor.codigo " +
			 		"INNER JOIN TipoConsumo " +
			 			"on parPeriodo.TipoIdPer = tipoConsumo.codigoTipo " +
				"ORDER BY DtiPeriodo ");
 			
			rs = st.executeQuery();
			 
			List<ParPeriodo> list = new ArrayList<>();
			
			while (rs.next()) 
 			{ 	Fornecedor forn = instantiateFornecedor(rs);
 				TipoConsumo tpForn = instantiateTipoConsumo(rs);
  			    ParPeriodo obj = instantiatePeriodo(rs, forn, tpForn);
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
	public List<ParPeriodo> findAllId() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
  			
			"SELECT *, fornecedor.Codigo, tipoConsumo.codigoTipo " + 
				"FROM ParPeriodo " +
			 		"INNER JOIN fornecedor " +
			 			"on parPeriodo.FornecedorIdPer = fornecedor.codigo " +
			 		"INNER JOIN TipoConsumo " +
			 			"on parPeriodo.TipoIdPer = tipoConsumo.codigoTipo " +
				"ORDER BY IdPeriodo ");
 			
			rs = st.executeQuery();
			 
			List<ParPeriodo> list = new ArrayList<>();
			
			while (rs.next()) 
 			{ 	Fornecedor forn = instantiateFornecedor(rs);
 				TipoConsumo tpForn = instantiateTipoConsumo(rs);
  			    ParPeriodo obj = instantiatePeriodo(rs, forn, tpForn);
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
	public ParPeriodo findById(int cod) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
 			st = conn.prepareStatement(
  					"SELECT *, fornecedor.Codigo, tipoConsumo.codigoTipo " +  
  						"FROM parPeriodo "  +
   							"INNER JOIN fornecedor " +
  								"ON parPeriodo.FornecedorIdPer = fornecedor.Codigo " +
	  						"INNER JOIN TipoConsumo " +
	  							"ON parPeriodo.TipoIdPer = tipoConsumo.CodigoTipo " +
  									"WHERE  IdPeriodo = ?");
  				
			st.setInt(1, cod);
			rs = st.executeQuery();
			
			if (rs.next())
			{	Fornecedor forn = instantiateFornecedor(rs);
				TipoConsumo tipo = instantiateTipoConsumo(rs);
				ParPeriodo obj = instantiatePeriodo(rs, forn, tipo);
				return obj;
			}
			return null;
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
	public void deleteByAll() {
		PreparedStatement st = null;
   		try {
			st = conn.prepareStatement(
					"DELETE FROM consulta WHERE Dti > 0 ", Statement.RETURN_GENERATED_KEYS);

   			st.executeUpdate();
 			
   		}
 		catch (SQLException e) {
			throw new DbException ( "Erro consulta !!! sem exclusão " + classe + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}
  	
	private ParPeriodo instantiatePeriodo(ResultSet rs, Fornecedor forn, TipoConsumo tpForn) throws SQLException {
		ParPeriodo obj = new ParPeriodo();
		obj.setIdPeriodo(rs.getInt("IdPeriodo"));
		obj.setDtiPeriodo(new java.util.Date(rs.getTimestamp("DtiPeriodo").getTime()));
		obj.setDtfPeriodo(new java.util.Date(rs.getTimestamp("DtfPeriodo").getTime()));
		obj.setFornecedor(forn);
		obj.setTipoConsumo(tpForn);
   		return obj;
	}

 	private Fornecedor instantiateFornecedor(ResultSet rs) throws SQLException {
		Fornecedor forn = new Fornecedor();
		forn.setCodigo(rs.getInt("Codigo"));
		forn.setRazaoSocial(rs.getString("RazaoSocial"));
 		forn.setRua(rs.getString("Rua"));
		forn.setNumero(rs.getInt("Numero"));
		forn.setComplemento(rs.getString("Complemento"));
		forn.setBairro(rs.getString("Bairro"));
		forn.setCidade(rs.getString("Cidade"));
		forn.setUf(rs.getString("UF"));
		forn.setCep(rs.getString("Cep"));
		forn.setDdd01(rs.getInt("Ddd01"));
		forn.setTelefone01(rs.getInt("Telefone01"));
		forn.setDdd02(rs.getInt("Ddd02"));
		forn.setTelefone02(rs.getInt("Telefone02"));
		forn.setContato(rs.getString("Contato"));
		forn.setDddContato(rs.getInt("DddContato"));
		forn.setTelefoneContato(rs.getInt("TelefoneContato"));
		forn.setEmail(rs.getString("Email"));
		forn.setPix(rs.getString("Pix"));
 		return forn;
 	}
  	
	private TipoConsumo instantiateTipoConsumo(ResultSet rs) throws SQLException {
		TipoConsumo tpForn = new TipoConsumo();
		tpForn.setCodigoTipo(rs.getInt("CodigoTipo"));
		tpForn.setNomeTipo(rs.getString("NomeTipo"));
 		return tpForn;
	}
  }
