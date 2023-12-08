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
import gui.sgcpmodel.entities.Fornecedor;
import gui.sgomodel.dao.EntradaDao;
import gui.sgomodel.entities.Entrada;
import gui.sgomodel.entities.Material;
  
public class EntradaDaoJDBC implements EntradaDao {
	
// tb entra construtor p/ conex�o
	private Connection conn;
	
	public EntradaDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	String classe = "Entrada JDBC ";
	
	@Override
	public void insert(Entrada obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO entrada " 
				      + "(NnfEnt, DataEnt, NomeFornEnt, NomeMatEnt, QuantidadeMatEnt, "
				      + "ValorMatEnt, FornecedorIdEnt, MaterialIdEnt)" 
   				      + "VALUES " 
				      + "(?, ?, ?, ?, ?, ?, ?, ? )",
 					 Statement.RETURN_GENERATED_KEYS); 

			st.setInt(1, obj.getNnfEnt());
			st.setDate(2, new java.sql.Date(obj.getDataEnt().getTime()));
			st.setString(3, obj.getForn().getRazaoSocial());
			st.setString(4, obj.getMat().getNomeMat());
 			st.setDouble(5, obj.getQuantidadeMatEnt());
			st.setDouble(6, obj.getValorMatEnt());
			st.setInt(7,  obj.getForn().getCodigo());
			st.setInt(8, obj.getMat().getCodigoMat());
						
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0) {
				rs = st.getGeneratedKeys();
				if (rs.next()) {
					int codigo = rs.getInt(1);
					obj.setNumeroEnt(codigo);
//					System.out.println("Novo inserido: " + classe + " " + obj.getNumeroEnt());
				} else {
					throw new DbException("Erro!!! sem inclusão" + classe);
				}
			}	
  		}
 		catch (SQLException e) {
			throw new DbException (e.getMessage());
		}
		finally {
 			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
 
	@Override
	public void insertBackUp(Entrada obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO entrada " 
				      + "(NumeroEnt, NnfEnt, DataEnt, NomeFornEnt, NomeMatEnt, QuantidadeMatEnt, "
				      + "ValorMatEnt, FornecedorIdEnt, MaterialIdEnt)" 
   				      + "VALUES " 
				      + "(?, ?, ?, ?, ?, ?, ?, ?, ? )",
 					 Statement.RETURN_GENERATED_KEYS); 

			st.setInt(1, obj.getNumeroEnt());
			st.setInt(2, obj.getNnfEnt());
			st.setDate(3, new java.sql.Date(obj.getDataEnt().getTime()));
			st.setString(4, obj.getForn().getRazaoSocial());
			st.setString(5, obj.getMat().getNomeMat());
 			st.setDouble(6, obj.getQuantidadeMatEnt());
			st.setDouble(7, obj.getValorMatEnt());
			st.setInt(8,  obj.getForn().getCodigo());
			st.setInt(9, obj.getMat().getCodigoMat());
						
 			st.executeUpdate();
  		}
 		catch (SQLException e) {
			throw new DbException(classe + "Erro!!! sem inclusão" + e.getMessage());
		}
		finally {
 			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
 
	@Override
	public void update(Entrada obj) {
		PreparedStatement st = null;
  		try {
  			conn.setAutoCommit(false);
			st = conn.prepareStatement(
					"UPDATE entrada " +  
							"SET NnfEnt = ?, "
							+ "DataEnt = ?, "
							+ "NomeFornEnt = ?, "
							+ "NomeMatEnt = ?, "
							+ "QuantidadeMatEnt = ?, "
							+ "ValorMatEnt = ?, "
 							+ "FornecedorIdEnt = ?, "
							+ "MaterialIdEnt = ? "
 						+ "WHERE (NumeroEnt = ?)",
        			 Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, obj.getNnfEnt());
			st.setDate(2, new java.sql.Date(obj.getDataEnt().getTime()));
			st.setString(3, obj.getForn().getRazaoSocial());
			st.setString(4, obj.getMat().getNomeMat());
 			st.setDouble(5, obj.getQuantidadeMatEnt());
			st.setDouble(6, obj.getValorMatEnt());
			st.setInt(7,  obj.getForn().getCodigo());
			st.setInt(8, obj.getMat().getCodigoMat());
			st.setInt(9, obj.getNumeroEnt()); 
 			st.executeUpdate();
 			conn.commit();
   		} 
 		catch (SQLException e) {
 			try {
				conn.rollback();
 				throw new DbException("Erro!!! sem atualização " + classe + " " + e.getMessage());
 				} 
 				catch (SQLException e1) {
 					throw new DbException("Erro!!! sem rollback " + classe + " " + e.getMessage());
 				}
 		}
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(int codigo) {
		PreparedStatement st = null;
   		try {
			st = conn.prepareStatement(
					"DELETE FROM entrada WHERE NumeroEnt = ?", Statement.RETURN_GENERATED_KEYS);
				  
			st.setInt(1, codigo);
			st.executeUpdate();
   		}
 		catch (SQLException e) {
			throw new DbException ( "Erro!!! não excluído " +  classe + " " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public List<Entrada> findByNnf(int nnf) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try 
		{	st = conn.prepareStatement( 
					 "SELECT *, fornecedor.Codigo, material.CodigoMat "
						 + "FROM entrada " 
					 		+ "INNER JOIN fornecedor " 
					 			+ "on entrada.FornecedorIdEnt = fornecedor.Codigo " 
					 		+ "INNER JOIN material " 
					 			+ "on entrada.MaterialIdEnt = Material.CodigoMat "
					 		+ "Where NnfEnt = ? " 
						+ "ORDER BY - NumeroEnt");
  
			st.setInt(1, nnf);
			rs = st.executeQuery();
			
			List<Entrada> list = new ArrayList<>();
			Map<Integer, Fornecedor> mapForn = new HashMap<>();
			Map<Integer, Material> mapMat = new HashMap<>();
   			
			while (rs.next()) {
  				Fornecedor forn = mapForn.get(rs.getInt("FornecedorIdEnt"));
  				if (forn == null)
  				{	forn = instantiateFornecedor(rs);
  					mapForn.put(rs.getInt("FornecedorIdEnt"), forn);  				
  				}
  				Material prod = mapMat.get(rs.getInt("MaterialIdEnt"));
  				if (prod == null)
  				{	prod = instantiateMaterial(rs);
  					mapMat.put(rs.getInt("MaterialIdEnt"), prod);  				
  				}
  			    Entrada obj = instantiateEntrada(rs, forn, prod);
  			    list.add(obj);
  			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException( classe + " " + e.getMessage());
		}
		finally {
 			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	} 
	
	@Override
	public List<Entrada> findByForNnf(int codfor, int nnf) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try 
		{	st = conn.prepareStatement( 
					 "SELECT *, fornecedor.Codigo, material.CodigoMat "
						 + "FROM entrada " 
					 		+ "INNER JOIN fornecedor " 
					 			+ "on entrada.FornecedorIdEnt = fornecedor.Codigo " 
					 		+ "INNER JOIN material " 
					 			+ "on entrada.MaterialIdEnt = Material.CodigoMat "
					 		+ "Where Fornecedor.Codigo = ? AND NnfEnt = ? " 
						+ "ORDER BY - NumeroEnt");
  
			st.setInt(1, codfor);
			st.setInt(2, nnf);
			rs = st.executeQuery();
			
			List<Entrada> list = new ArrayList<>();
			Map<Integer, Fornecedor> mapForn = new HashMap<>();
			Map<Integer, Material> mapMat = new HashMap<>();
   			
			while (rs.next()) {
  				Fornecedor forn = mapForn.get(rs.getInt("FornecedorIdEnt"));
  				if (forn == null)
  				{	forn = instantiateFornecedor(rs);
  					mapForn.put(rs.getInt("FornecedorIdEnt"), forn);  				
  				}
  				Material prod = mapMat.get(rs.getInt("MaterialIdEnt"));
  				if (prod == null)
  				{	prod = instantiateMaterial(rs);
  					mapMat.put(rs.getInt("MaterialIdEnt"), prod);  				
  				}
  			    Entrada obj = instantiateEntrada(rs, forn, prod);
  			    list.add(obj);
  			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException( classe + " " + e.getMessage());
		}
		finally {
 			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	} 
	
	@Override
	public List<Entrada> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try 
		{	st = conn.prepareStatement( 
					 "SELECT *, fornecedor.Codigo, material.CodigoMat "
						 + "FROM entrada " 
					 		+ "INNER JOIN fornecedor " 
					 			+ "on entrada.FornecedorIdEnt = fornecedor.Codigo " 
					 		+ "INNER JOIN material " 
					 			+ "on entrada.MaterialIdEnt = Material.CodigoMat " 
						+ "ORDER BY - DataEnt");
  
			rs = st.executeQuery();
			
			List<Entrada> list = new ArrayList<>();
			Material prod = new Material();
			Map<Integer, Fornecedor> mapForn = new HashMap<>();
			Map<Integer, Material> mapMat = new HashMap<>();
   			
			while (rs.next()) {
  				Fornecedor forn = mapForn.get(rs.getInt("FornecedorIdEnt"));
  				if (forn == null)
  				{	forn = instantiateFornecedor(rs);
  					mapForn.put(rs.getInt("FornecedorIdEnt"), forn);  				
  				}
  				prod = mapMat.get(rs.getInt("MaterialIdEnt"));
  				if (prod == null)
  				{	prod = instantiateMaterial(rs);
  					mapMat.put(rs.getInt("MaterialIdEnt"), prod);  				
  				}
  			    Entrada obj = instantiateEntrada(rs, forn, prod);
  			    list.add(obj);
  			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException( classe + " " + e.getMessage());
		}
		finally {
 			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	} 
	
	@Override
	public List<Entrada> findAllId() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try 
		{	st = conn.prepareStatement( 
					 "SELECT *, fornecedor.Codigo, material.CodigoMat "
						 + "FROM entrada " 
					 		+ "INNER JOIN fornecedor " 
					 			+ "on entrada.FornecedorIdEnt = fornecedor.Codigo " 
					 		+ "INNER JOIN material " 
					 			+ "on entrada.MaterialIdEnt = Material.CodigoMat " 
						+ "ORDER BY NumeroEnt");
  
			rs = st.executeQuery();
			
			List<Entrada> list = new ArrayList<>();
			Material prod = new Material();
			Map<Integer, Fornecedor> mapForn = new HashMap<>();
			Map<Integer, Material> mapMat = new HashMap<>();
   			
			while (rs.next()) {
  				Fornecedor forn = mapForn.get(rs.getInt("FornecedorIdEnt"));
  				if (forn == null)
  				{	forn = instantiateFornecedor(rs);
  					mapForn.put(rs.getInt("FornecedorIdEnt"), forn);  				
  				}
  				prod = mapMat.get(rs.getInt("MaterialIdEnt"));
  				if (prod == null)
  				{	prod = instantiateMaterial(rs);
  					mapMat.put(rs.getInt("MaterialIdEnt"), prod);  				
  				}
  			    Entrada obj = instantiateEntrada(rs, forn, prod);
  			    list.add(obj);
  			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException( classe + " " + e.getMessage());
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
					"TRUNCATE TABLE sgb.Entrada " );

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
	
	private Entrada instantiateEntrada(ResultSet rs, Fornecedor forn, Material prod) throws SQLException {
 		Entrada ent = new Entrada();
 		ent.setNumeroEnt(rs.getInt("NumeroEnt"));
 		ent.setNnfEnt(rs.getInt("NnfEnt"));
  		ent.setDataEnt(new java.util.Date(rs.getTimestamp("DataEnt").getTime()));
 		ent.setNomeFornEnt(rs.getString("NomeFornEnt"));  		
 		ent.setNomeMatEnt(rs.getString("NomeMatEnt"));
 		ent.setQuantidadeMatEnt(rs.getDouble("QuantidadeMatEnt"));
 		ent.setValorMatEnt(rs.getDouble("ValorMatEnt"));
 		ent.setForn(forn);
 		ent.setMat(prod);
        return ent;
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
 		forn.setObservacao(rs.getString("Observacao"));
        return forn;
	}
	
	private Material instantiateMaterial(ResultSet rs) throws SQLException {
		Material material = new Material();
		material.setCodigoMat(rs.getInt("CodigoMat"));
		material.setGrupoMat(rs.getInt("GrupoMat"));
		material.setNomeMat(rs.getString("NomeMat"));
		material.setSaldoMat(rs.getDouble("SaldoMat"));
		material.setEstMinMat(rs.getDouble("EstMinMat"));
		material.setPrecoMat(rs.getDouble("PrecoMat"));
		material.setVendaMat(rs.getDouble("VendaMat"));
		material.setCmmMat(rs.getDouble("CmmMat"));
		material.setDataCadastroMat(new java.util.Date(rs.getTimestamp("DataCadastroMat").getTime()));
        return material;
	}
	
	@Override
	public Entrada findById(int num) {
			PreparedStatement st = null; 
			ResultSet rs = null;
			try 
			{	st = conn.prepareStatement( 
						 "SELECT *, fornecedor.Codigo, material.CodigoMat "
							 + "FROM entrada " 
						 		+ "INNER JOIN fornecedor " 
						 			+ "on entrada.FornecedorIdEnt = fornecedor.Codigo " 
						 		+ "INNER JOIN material " 
						 			+ "on entrada.MaterialIdEnt = Material.CodigoMat "
						 		+ "Where NumeroEnt = ? " 
							+ "ORDER BY - NumeroEnt");

				st.setInt(1, num);
				rs = st.executeQuery();
				
				while (rs.next()) {
	  				Fornecedor forn = instantiateFornecedor(rs);
	  				Material prod = instantiateMaterial(rs);
	  			    Entrada obj = instantiateEntrada(rs, forn, prod);
	  			    return obj;
	  			}
				return null;
			}
			catch (SQLException e) {
				throw new DbException( classe + " " + e.getMessage());
			}
			finally {
	 			DB.closeStatement(st);
				DB.closeResultSet(rs);
			}
		} 
}
