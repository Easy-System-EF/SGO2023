package gui.sgcpmodel.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import gui.sgcpmodel.dao.ParcelaDao;
import gui.sgcpmodel.entities.Fornecedor;
import gui.sgcpmodel.entities.Parcela;
import gui.sgcpmodel.entities.TipoConsumo;
import gui.sgcpmodel.entities.consulta.ParPeriodo;

public class ParcelaDaoJDBC implements ParcelaDao {

// aq entra um construtor declarando a conex�o	
	private Connection conn;
	
	public ParcelaDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	String classe = "Parcela JDBC ";

	@Override
	public void insert(Parcela obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
   		try {
			st = conn.prepareStatement(
					"INSERT INTO parcela " + 
					"(CodigoFornecedorPar, NomeFornecedorPar, NnfPar, NumeroPar, DataVencimentoPar, ValorPar, DescontoPar, " 
							+ "JurosPar, TotalPar, PagoPar, DataPagamentoPar, FornecedorIdPar, TipoIdPar, PeriodoIdPar) "   
					+ "VALUES "   
						+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
// retorna o o novo Id inserido 					
									Statement.RETURN_GENERATED_KEYS); 
 			st.setInt(1, obj.getCodigoFornecedorPar());
 			st.setString(2, obj.getNomeFornecedorPar());
 			st.setInt(3, obj.getNnfPar());
 			st.setInt(4, obj.getNumeroPar());
			st.setDate(5, new java.sql.Date(obj.getDataVencimentoPar().getTime()));
  			st.setDouble(6, obj.getValorPar());
			st.setDouble(7, obj.getDescontoPar());
			st.setDouble(8, obj.getJurosPar());
			st.setDouble(9, obj.getTotalPar());
			st.setDouble(10, obj.getPagoPar());
			st.setDate(11, new java.sql.Date(obj.getDataPagamentoPar().getTime()));
  			st.setInt(12, obj.getFornecedor().getCodigo());
  			st.setInt(13, obj.getTipoConsumo().getCodigoTipo());
  			st.setInt(14, obj.getPeriodo().getIdPeriodo());
   			
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0) {
				rs = st.getGeneratedKeys();
				if (rs.next()) {
					int codigo = rs.getInt(1);
					obj.setIdPar(codigo);
//					System.out.println("Novo inserido: " + obj.getCodigo());
				} else {
					throw new DbException("Erro!!! sem inclusão " + classe );
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
	public void insertBackUp(Parcela obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
   		try {
			st = conn.prepareStatement(
					"INSERT INTO parcela " + 
					"(IdPar, CodigoFornecedorPar, NomeFornecedorPar, NnfPar, NumeroPar, DataVencimentoPar, ValorPar, DescontoPar, " 
							+ "JurosPar, TotalPar, PagoPar, DataPagamentoPar, FornecedorIdPar, TipoIdPar, PeriodoIdPar) "   
					+ "VALUES "   
						+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			
 			st.setInt(1, obj.getIdPar());
 			st.setInt(2, obj.getCodigoFornecedorPar());
 			st.setString(3, obj.getNomeFornecedorPar());
 			st.setInt(4, obj.getNnfPar());
 			st.setInt(5, obj.getNumeroPar());
			st.setDate(6, new java.sql.Date(obj.getDataVencimentoPar().getTime()));
  			st.setDouble(7, obj.getValorPar());
			st.setDouble(8, obj.getDescontoPar());
			st.setDouble(9, obj.getJurosPar());
			st.setDouble(10, obj.getTotalPar());
			st.setDouble(11, obj.getPagoPar());
			st.setDate(12, new java.sql.Date(obj.getDataPagamentoPar().getTime()));
  			st.setInt(13, obj.getFornecedor().getCodigo());
  			st.setInt(14, obj.getTipoConsumo().getCodigoTipo());
  			st.setInt(15, obj.getPeriodo().getIdPeriodo());
   			
 			st.executeUpdate();
   		}
 		catch (SQLException e) {
			throw new DbException("Erro!!! sem inclusão " + classe + e.getMessage());
		}
		finally {
 			DB.closeStatement(st);
 			DB.closeResultSet(rs);
		}
	}

	@Override
	public void update(Parcela obj) {
		PreparedStatement st = null;
  		try {
			st = conn.prepareStatement(
					"UPDATE parcela "   
							+ "SET   CodigoFornecedorPar = ?,  NomeFornecedorPar = ? , NnfPar = ?, NumeroPar = ? , " 
							+ "DataVencimentoPar = ? , ValorPar = ? , DescontoPar = ? , JurosPar = ? , TotalPar = ? , " 
							+ "PagoPar = ? , DataPagamentoPar = ? , FornecedorIdPar = ?, TipoIdPar = ? , PeriodoIdPar = ? " 
						+ "WHERE (IdPar = ?)",
				 				Statement.RETURN_GENERATED_KEYS);
			
 			st.setInt(1, obj.getCodigoFornecedorPar());
 			st.setString(2, obj.getNomeFornecedorPar());
 			st.setInt(3, obj.getNnfPar());
 			st.setInt(4, obj.getNumeroPar());
			st.setDate(5, new java.sql.Date(obj.getDataVencimentoPar().getTime()));
  			st.setDouble(6, obj.getValorPar());
			st.setDouble(7, obj.getDescontoPar());
			st.setDouble(8, obj.getJurosPar());
			st.setDouble(9, obj.getTotalPar());
			st.setDouble(10, obj.getPagoPar());
			st.setDate(11, new java.sql.Date(obj.getDataPagamentoPar().getTime()));
  			st.setInt(12, obj.getFornecedor().getCodigo());
  			st.setInt(13, obj.getTipoConsumo().getCodigoTipo());
  			st.setInt(14, obj.getPeriodo().getIdPeriodo());
  			st.setInt(15, obj.getIdPar());
  			
			st.executeUpdate();
 				
	   		} 	
  			catch (SQLException e) {
  				throw new DbException ("Erro compromisso !!! no insert " + classe + " " + e.getMessage());  
			}
			finally {
				DB.closeStatement(st);
			}
		}

	@Override
	public void deleteByNnf(int nnf, int codFor) {
		PreparedStatement st = null;
   		try {
			st = conn.prepareStatement(
					"DELETE FROM parcela WHERE NnfPar = ? AND FornecedorIdPar = ? ", Statement.RETURN_GENERATED_KEYS);
 			st.setInt(1, nnf);
 			st.setInt(2, codFor);
 						
 			st.executeUpdate();
 			
   		}
 		catch (SQLException e) {
			throw new DbException ( "Erro parcela !!! sem exclusão " + classe + " "  + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}
 
	@Override
	public Double findSumAllAberto(int ano, int mes) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					
					"SELECT SUM(PagoPar) AS 'total' FROM parcela "
					+ 	"WHERE (pagoPar = 0.00) AND (year(DataVencimentoPar) <= ?) AND (month(DataVencimentoPar) <= ?) ");

			st.setInt(1, mes);
			st.setInt(2, ano);

			rs = st.executeQuery();
			
			while (rs.next()) {
				Double vlr = rs.getDouble("total");
				return vlr;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException ( "Erro compromisso !!! sem SUM " + classe + " " + e.getMessage()); }
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	@Override
	public Double findSumAllPago(int ano, int mes) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					
					"SELECT SUM(PagoPar) AS 'total' FROM parcela "
					+ 	"WHERE (pagoPar > 0.00) AND (year(DataPagamentoPar) <= ?) AND (month(DataPagamentoPar) <= ?) ");

			st.setInt(1, mes);
			st.setInt(2, ano);

			rs = st.executeQuery();
			
			while (rs.next()) {
				Double vlr = rs.getDouble("total");
				return vlr;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException ( "Erro compromisso !!! sem SUM " + classe + " " + e.getMessage()); }
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	@Override
	public Double findSumAberto(Date dti, Date dtf) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					
					"SELECT SUM(ValorPar) AS 'total' FROM parcela " +
					 	"WHERE pagoPar = 0 AND DataVencimentoPar >= ? AND DataVencimentoPar <= ? ");

			st.setDate(1, new java.sql.Date(dti.getTime()));
			st.setDate(2, new java.sql.Date(dtf.getTime()));

			rs = st.executeQuery();
			
			while (rs.next()) {
				Double vlr = rs.getDouble("total");
				return vlr;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException ( "Erro compromisso !!! sem SUM " + classe + " " + e.getMessage()); }
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	@Override
	public Double findSumPago(Date dti, Date dtf) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					
					"SELECT SUM(PagoPar) AS 'total' FROM parcela " +
					 	"WHERE parcela.pagoPar > 0 AND parcela.DataPagamentoPar >= ? AND parcela.DataPagamentoPar <= ? ");

			st.setDate(1, new java.sql.Date(dti.getTime()));
			st.setDate(2, new java.sql.Date(dtf.getTime()));

			rs = st.executeQuery();
			
			while (rs.next()) {
				Double vlr = rs.getDouble("total");
				return vlr;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException ( "Erro compromisso !!! sem SUM " + classe + " " + e.getMessage()); }
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	private Parcela instantiateParcela(ResultSet rs, Fornecedor forn, TipoConsumo tipo, ParPeriodo per) throws SQLException {
		Parcela obj = new Parcela();
		obj.setIdPar(rs.getInt("IdPar"));
		obj.setCodigoFornecedorPar(rs.getInt("CodigoFornecedorPar"));
		obj.setNomeFornecedorPar(rs.getString("NomeFornecedorPar"));
   		obj.setNnfPar(rs.getInt("NnfPar"));
   		obj.setNumeroPar(rs.getInt("NumeroPar"));
		obj.setDataVencimentoPar(new java.util.Date(rs.getTimestamp("DataVencimentoPar").getTime()));
 		obj.setValorPar(rs.getDouble("ValorPar"));
  		obj.setDescontoPar(rs.getDouble("DescontoPar"));
 		obj.setJurosPar(rs.getDouble("JurosPar"));
		obj.setTotalPar(rs.getDouble("TotalPar"));
		obj.setPagoPar(rs.getDouble("PagoPar"));
		obj.setDataPagamentoPar(new java.util.Date(rs.getTimestamp("DataPagamentoPar").getTime()));
 //objetos montados (sem id e com a associa��o la da classe Fornrecedor fornecedor...				
		obj.setFornecedor(forn);
		obj.setTipoConsumo(tipo);
		obj.setPeriodo(per);
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


	private TipoConsumo instantiateTipo(ResultSet rs) throws SQLException {
		TipoConsumo tipo = new TipoConsumo();
		tipo.setCodigoTipo(rs.getInt("CodigoTipo"));
		tipo.setNomeTipo(rs.getString("NomeTipo"));
 		return tipo;
	}

	private ParPeriodo instantiateParPeriodo(ResultSet rs, Fornecedor forn) throws SQLException {
		ParPeriodo per = new ParPeriodo();
		per.setIdPeriodo(rs.getInt("IdPeriodo"));
		per.setDtiPeriodo(new java.util.Date(rs.getTimestamp("DtiPeriodo").getTime()));
		per.setDtfPeriodo(new java.util.Date(rs.getTimestamp("DtfPeriodo").getTime()));
		per.setFornecedor(forn);
		
//		per.setTipoConsumo(tpForn);
   		return per;
	}
 
 	@Override
	public List<Parcela> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
 					"SELECT * " +  
				    	"FROM parcela "  +
				    		"INNER JOIN fornecedor " +
				    			"ON parcela.FornecedorIdPar = fornecedor.codigo " + 
				    		"INNER JOIN parPeriodo " +
				    			"ON parcela.PeriodoIdPar = parPeriodo.idPeriodo " + 
	  						"INNER JOIN TipoConsumo " +
	  							"ON parcela.TipoIdPar = tipoConsumo.CodigoTipo " +
//	  								"WHERE PagoPar = 0 " +
 									"ORDER BY DataVencimentoPar ");

 			rs = st.executeQuery();
 			 
 			List<Parcela> list = new ArrayList<>();
			Map<Integer, Fornecedor> mapFor = new HashMap<>();
			Map<Integer, TipoConsumo> mapTp = new HashMap<>();
			Map<Integer, ParPeriodo> mapPer = new HashMap<>();
			
			while (rs.next()) {
				Fornecedor objFor = mapFor.get(rs.getInt("FornecedorIdPar"));
				if (objFor == null) {
					objFor = instantiateFornecedor(rs);
					mapFor.put(rs.getInt("FornecedorIdPar"), objFor);
				}	
				TipoConsumo objTp = mapTp.get(rs.getInt("TipoIdPar"));
				if (objTp == null) {
					objTp = instantiateTipo(rs);
					mapTp.put(rs.getInt("TipoIdPar"), objTp);
				}	
				ParPeriodo objPer = mapPer.get(rs.getInt("PeriodoIdPar"));
				if (objPer == null) {
					objPer = instantiateParPeriodo(rs, objFor);
					mapPer.put(rs.getInt("PeriodoIdPar"), objPer);
				}	
				Parcela obj = instantiateParcela(rs, objFor, objTp, objPer);
				list.add(obj);
  			}
 			return list;
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
	public List<Parcela> findAllId() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
 					"SELECT * " +  
				    	"FROM parcela "  +
				    		"INNER JOIN fornecedor " +
				    			"ON parcela.FornecedorIdPar = fornecedor.codigo " + 
				    		"INNER JOIN parPeriodo " +
				    			"ON parcela.PeriodoIdPar = parPeriodo.idPeriodo " + 
	  						"INNER JOIN TipoConsumo " +
	  							"ON parcela.TipoIdPar = tipoConsumo.CodigoTipo " +
 									"ORDER BY IdPar ");

 			rs = st.executeQuery();
 			 
 			List<Parcela> list = new ArrayList<>();
			Map<Integer, Fornecedor> mapFor = new HashMap<>();
			Map<Integer, TipoConsumo> mapTp = new HashMap<>();
			Map<Integer, ParPeriodo> mapPer = new HashMap<>();
			
			while (rs.next()) {
				Fornecedor objFor = mapFor.get(rs.getInt("FornecedorIdPar"));
				if (objFor == null) {
					objFor = instantiateFornecedor(rs);
					mapFor.put(rs.getInt("FornecedorIdPar"), objFor);
				}	
				TipoConsumo objTp = mapTp.get(rs.getInt("TipoIdPar"));
				if (objTp == null) {
					objTp = instantiateTipo(rs);
					mapTp.put(rs.getInt("TipoIdPar"), objTp);
				}	
				ParPeriodo objPer = mapPer.get(rs.getInt("PeriodoIdPar"));
				if (objPer == null) {
					objPer = instantiateParPeriodo(rs, objFor);
					mapPer.put(rs.getInt("PeriodoIdPar"), objPer);
				}	
				Parcela obj = instantiateParcela(rs, objFor, objTp, objPer);
				list.add(obj);
  			}
 			return list;
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
	public List<Parcela> findAllAberto() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
 					"SELECT * " +  
				    	"FROM parcela "  +
				    		"INNER JOIN fornecedor " +
				    			"ON parcela.FornecedorIdPar = fornecedor.codigo " + 
				    		"INNER JOIN parPeriodo " +
				    			"ON parcela.PeriodoIdPar = parPeriodo.idPeriodo " + 
	  						"INNER JOIN TipoConsumo " +
	  							"ON parcela.TipoIdPar = tipoConsumo.CodigoTipo " +
	  								"WHERE PagoPar = 0 " +
 									"ORDER BY DataVencimentoPar ");

 			rs = st.executeQuery();
 			List<Parcela> list = new ArrayList<>();			 
			Map<Integer, Fornecedor> mapFor = new HashMap<>();
			Map<Integer, TipoConsumo> mapTp = new HashMap<>();
			Map<Integer, ParPeriodo> mapPer = new HashMap<>();
			
			while (rs.next()) {
				Fornecedor objFor = mapFor.get(rs.getInt("FornecedorIdPar"));
				if (objFor == null) {
					objFor = instantiateFornecedor(rs);
					mapFor.put(rs.getInt("FornecedorIdPar"), objFor);
				}	
				TipoConsumo objTp = mapTp.get(rs.getInt("TipoIdPar"));
				if (objTp == null) {
					objTp = instantiateTipo(rs);
					mapTp.put(rs.getInt("TipoIdPar"), objTp);
				}	
				ParPeriodo objPer = mapPer.get(rs.getInt("PeriodoIdPar"));
				if (objPer == null) {
					objPer = instantiateParPeriodo(rs, objFor);
					mapPer.put(rs.getInt("PeriodoIdPar"), objPer);
				}	
				Parcela obj = instantiateParcela(rs, objFor, objTp, objPer);
				list.add(obj);
 			}
 			return list;
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
	public List<Parcela> findAllPago() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
 					"SELECT * " +  
				    	"FROM parcela "  +
				    		"INNER JOIN fornecedor " +
				    			"ON parcela.FornecedorIdPar = fornecedor.codigo " + 
				    		"INNER JOIN parPeriodo " +
				    			"ON parcela.PeriodoIdPar = parPeriodo.idPeriodo " + 
	  						"INNER JOIN TipoConsumo " +
	  							"ON parcela.TipoIdPar = tipoConsumo.CodigoTipo " +
	  								"WHERE PagoPar > 0 " +
 									"ORDER BY - DataPagamentoPar ");

 			rs = st.executeQuery();
 			 
			 
			List<Parcela> list = new ArrayList<>();
			Map<Integer, Fornecedor> mapFor = new HashMap<>();
			Map<Integer, TipoConsumo> mapTp = new HashMap<>();
			Map<Integer, ParPeriodo> mapPer = new HashMap<>();
			
			while (rs.next()) {
				Fornecedor objFor = mapFor.get(rs.getInt("FornecedorIdPar"));
				if (objFor == null) {
					objFor = instantiateFornecedor(rs);
					mapFor.put(rs.getInt("FornecedorIdPar"), objFor);
				}	
				TipoConsumo objTp = mapTp.get(rs.getInt("TipoIdPar"));
				if (objTp == null) {
					objTp = instantiateTipo(rs);
					mapTp.put(rs.getInt("TipoIdPar"), objTp);
				}	
				ParPeriodo objPer = mapPer.get(rs.getInt("PeriodoIdPar"));
				if (objPer == null) {
					objPer = instantiateParPeriodo(rs, objFor);
					mapPer.put(rs.getInt("PeriodoIdPar"), objPer);
				}	
				Parcela obj = instantiateParcela(rs, objFor, objTp, objPer);
				list.add(obj);
 			}
 			return list;
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
	public List<Parcela> findPeriodoAberto() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
 			st = conn.prepareStatement(
  					"SELECT * " +  
  						"FROM parcela "  +
  							"INNER JOIN parPeriodo " +
  								"ON parcela.PeriodoIdPar = parPeriodo.IdPeriodo " +
  							"INNER JOIN fornecedor " +
  								"ON parcela.FornecedorIdPar = fornecedor.Codigo " +
	  						"INNER JOIN TipoConsumo " +
	  							"ON parcela.TipoIdPar = tipoConsumo.CodigoTipo " +
  									"WHERE  PagoPar = 0 AND " +
  									"parcela.DataVencimentoPar >= parPeriodo.DtiPeriodo AND " +
  									"parcela.DataVencimentoPar <= parPeriodo.DtfPeriodo " +
 										"ORDER BY DataVencimentoPar, NumeroPar ");
 				
 			rs = st.executeQuery();
			
			 
			List<Parcela> list = new ArrayList<>();
			Map<Integer, Fornecedor> mapFor = new HashMap<>();
			Map<Integer, TipoConsumo> mapTp = new HashMap<>();
			Map<Integer, ParPeriodo> mapPer = new HashMap<>();
			
			while (rs.next()) {
				Fornecedor objFor = mapFor.get(rs.getInt("FornecedorIdPar"));
				if (objFor == null) {
					objFor = instantiateFornecedor(rs);
					mapFor.put(rs.getInt("FornecedorIdPar"), objFor);
				}	
				TipoConsumo objTp = mapTp.get(rs.getInt("TipoIdPar"));
				if (objTp == null) {
					objTp = instantiateTipo(rs);
					mapTp.put(rs.getInt("TipoIdPar"), objTp);
				}	
				ParPeriodo objPer = mapPer.get(rs.getInt("PeriodoIdPar"));
				if (objPer == null) {
					objPer = instantiateParPeriodo(rs, objFor);
					mapPer.put(rs.getInt("PeriodoIdPar"), objPer);
				}	
				Parcela obj = instantiateParcela(rs, objFor, objTp, objPer);
				list.add(obj);
 			}
  			return list;
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
	public List<Parcela> findPeriodoPago() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
 			st = conn.prepareStatement(

 					"SELECT * " +  
 		  				"FROM parcela "  +
 		  					"INNER JOIN parPeriodo " +
 		  						"ON parcela.PeriodoIdPar = parPeriodo.IdPeriodo " +
 		  					"INNER JOIN fornecedor " +
 		  						"ON parcela.FornecedorIdPar = fornecedor.Codigo " +
	  						"INNER JOIN TipoConsumo " +
	  							"ON parcela.TipoIdPar = tipoConsumo.CodigoTipo " +
 		  							"WHERE  PagoPar > 0 AND " +
 		  							"parcela.DataVencimentoPar >= parPeriodo.DtiPeriodo AND " +
 		  							"parcela.DataVencimentoPar <= parPeriodo.DtfPeriodo " +
 										"ORDER BY - DataPagamentoPar, NumeroPar ");
    
 			rs = st.executeQuery();
			
			 
			List<Parcela> list = new ArrayList<>();
			Map<Integer, Fornecedor> mapFor = new HashMap<>();
			Map<Integer, TipoConsumo> mapTp = new HashMap<>();
			Map<Integer, ParPeriodo> mapPer = new HashMap<>();
			
			while (rs.next()) {
				Fornecedor objFor = mapFor.get(rs.getInt("FornecedorIdPar"));
				if (objFor == null) {
					objFor = instantiateFornecedor(rs);
					mapFor.put(rs.getInt("FornecedorIdPar"), objFor);
				}	
				TipoConsumo objTp = mapTp.get(rs.getInt("TipoIdPar"));
				if (objTp == null) {
					objTp = instantiateTipo(rs);
					mapTp.put(rs.getInt("TipoIdPar"), objTp);
				}	
				ParPeriodo objPer = mapPer.get(rs.getInt("PeriodoIdPar"));
				if (objPer == null) {
					objPer = instantiateParPeriodo(rs, objFor);
					mapPer.put(rs.getInt("PeriodoIdPar"), objPer);
				}	
				Parcela obj = instantiateParcela(rs, objFor, objTp, objPer);
				list.add(obj);
 			}
 			return list;
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
	public Parcela findByIdForn(int cod) {
		PreparedStatement st = null;
		ResultSet rs = null;
 		try {
			st = conn.prepareStatement(
 
					"SELECT * " +  
		  				"FROM parcela "  +
		  					"INNER JOIN parPeriodo " +
		  						"ON parcela.PeriodoIdPar = parPeriodo.IdPeriodo " +
		  					"INNER JOIN fornecedor " +
		  						"ON parcela.FornecedorIdPar = fornecedor.Codigo " +
	  						"INNER JOIN TipoConsumo " +
	  							"ON parcela.TipoIdPar = tipoConsumo.CodigoTipo " +
		  							"WHERE CodigoFornecedorPar = ? " +
										"ORDER BY DataVencimentoPar, Numeropar");

  			st.setInt(1, cod);
			rs = st.executeQuery();
			
  			while (rs.next()) 
 			{ 	Fornecedor forn = instantiateFornecedor(rs);
				TipoConsumo tipo = instantiateTipo(rs);
				ParPeriodo per = instantiateParPeriodo(rs, forn);
				Parcela obj = instantiateParcela(rs, forn, tipo, per);
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
	public List<Parcela> findByIdFornecedorAberto(int cod) {
		PreparedStatement st = null;
		ResultSet rs = null;
 		try {
			st = conn.prepareStatement(
 
					"SELECT * " +  
		  				"FROM parcela "  +
		  					"INNER JOIN parPeriodo " +
		  						"ON parcela.PeriodoIdPar = parPeriodo.IdPeriodo " +
		  					"INNER JOIN fornecedor " +
		  						"ON parcela.FornecedorIdPar = fornecedor.Codigo " +
	  						"INNER JOIN TipoConsumo " +
	  							"ON parcela.TipoIdPar = tipoConsumo.CodigoTipo " +
		  							"WHERE CodigoFornecedorPar = ? AND PagoPar = 0 " +
										"ORDER BY DataVencimentoPar, Numeropar");

  			st.setInt(1, cod);
			rs = st.executeQuery();
			
			 
			List<Parcela> list = new ArrayList<>();
			Map<Integer, Fornecedor> mapFor = new HashMap<>();
			Map<Integer, TipoConsumo> mapTp = new HashMap<>();
			Map<Integer, ParPeriodo> mapPer = new HashMap<>();
			
			while (rs.next()) {
				Fornecedor objFor = mapFor.get(rs.getInt("FornecedorIdPar"));
				if (objFor == null) {
					objFor = instantiateFornecedor(rs);
					mapFor.put(rs.getInt("FornecedorIdPar"), objFor);
				}	
				TipoConsumo objTp = mapTp.get(rs.getInt("TipoIdPar"));
				if (objTp == null) {
					objTp = instantiateTipo(rs);
					mapTp.put(rs.getInt("TipoIdPar"), objTp);
				}	
				ParPeriodo objPer = mapPer.get(rs.getInt("PeriodoIdPar"));
				if (objPer == null) {
					objPer = instantiateParPeriodo(rs, objFor);
					mapPer.put(rs.getInt("PeriodoIdPar"), objPer);
				}	
				Parcela obj = instantiateParcela(rs, objFor, objTp, objPer);
				list.add(obj);
 			}
  			return list;
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
	public List<Parcela> findByIdFornecedorPago(int cod) {
		PreparedStatement st = null;
		ResultSet rs = null;
 		try {
			st = conn.prepareStatement(
 
					"SELECT * " +  
		  				"FROM parcela "  +
		  					"INNER JOIN parPeriodo " +
		  						"ON parcela.PeriodoIdPar = parPeriodo.IdPeriodo " +
		  					"INNER JOIN fornecedor " +
		  						"ON parcela.FornecedorIdPar = fornecedor.Codigo " +
	  						"INNER JOIN TipoConsumo " +
	  							"ON parcela.TipoIdPar = tipoConsumo.CodigoTipo " +
		  							"WHERE CodigoFornecedorPar = ? AND PagoPar > 0 " +
										"ORDER BY - DataPagamentoPar, Numeropar");
 			
  			st.setInt(1, cod);
			rs = st.executeQuery();
			
			List<Parcela> list = new ArrayList<>();
			Map<Integer, Fornecedor> mapFor = new HashMap<>();
			Map<Integer, TipoConsumo> mapTp = new HashMap<>();
			Map<Integer, ParPeriodo> mapPer = new HashMap<>();
			
			while (rs.next()) {
				Fornecedor objFor = mapFor.get(rs.getInt("FornecedorIdPar"));
				if (objFor == null) {
					objFor = instantiateFornecedor(rs);
					mapFor.put(rs.getInt("FornecedorIdPar"), objFor);
				}	
				TipoConsumo objTp = mapTp.get(rs.getInt("TipoIdPar"));
				if (objTp == null) {
					objTp = instantiateTipo(rs);
					mapTp.put(rs.getInt("TipoIdPar"), objTp);
				}	
				ParPeriodo objPer = mapPer.get(rs.getInt("PeriodoIdPar"));
				if (objPer == null) {
					objPer = instantiateParPeriodo(rs, objFor);
					mapPer.put(rs.getInt("PeriodoIdPar"), objPer);
				}	
				Parcela obj = instantiateParcela(rs, objFor, objTp, objPer);
				list.add(obj);
 			}
  			return list;
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
	public List<Parcela> findByIdFornecedorNnf(Integer cod, Integer nnf) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
 
					"SELECT * " +  
		  				"FROM parcela "  +
		  					"INNER JOIN parPeriodo " +
		  						"ON parcela.PeriodoIdPar = parPeriodo.IdPeriodo " +
		  					"INNER JOIN fornecedor " +
		  						"ON parcela.FornecedorIdPar = fornecedor.Codigo " +
	  						"INNER JOIN TipoConsumo " +
	  							"ON parcela.TipoIdPar = tipoConsumo.CodigoTipo " +
		  				"WHERE CodigoFornecedorPar = ? AND NnfPar = ? " +
					"ORDER BY DataVencimentoPar, Numeropar");
 			
  			st.setInt(1, cod);
  			st.setInt(2, nnf);
			rs = st.executeQuery();
			
			 
			List<Parcela> list = new ArrayList<>();
			Map<Integer, Fornecedor> mapFor = new HashMap<>();
			Map<Integer, TipoConsumo> mapTp = new HashMap<>();
			Map<Integer, ParPeriodo> mapPer = new HashMap<>();
			
			while (rs.next()) {
				Fornecedor objFor = mapFor.get(rs.getInt("FornecedorIdPar"));
				if (objFor == null) {
					objFor = instantiateFornecedor(rs);
					mapFor.put(rs.getInt("FornecedorIdPar"), objFor);
				}	
				TipoConsumo objTp = mapTp.get(rs.getInt("TipoIdPar"));
				if (objTp == null) {
					objTp = instantiateTipo(rs);
					mapTp.put(rs.getInt("TipoIdPar"), objTp);
				}	
				ParPeriodo objPer = mapPer.get(rs.getInt("PeriodoIdPar"));
				if (objPer == null) {
					objPer = instantiateParPeriodo(rs, objFor);
					mapPer.put(rs.getInt("PeriodoIdPar"), objPer);
				}	
				Parcela obj = instantiateParcela(rs, objFor, objTp, objPer);
				list.add(obj);
 			}
  			return list;
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
	public List<Parcela> findByIdTipoAberto(int cod) {
		PreparedStatement st = null;
		ResultSet rs = null;
 		try {
			st = conn.prepareStatement(
 
					"SELECT * " +  
		  				"FROM parcela "  +
		  					"INNER JOIN parPeriodo " +
		  						"ON parcela.PeriodoIdPar = parPeriodo.IdPeriodo " +
			  						"INNER JOIN fornecedor " +
		  						"ON parcela.FornecedorIdPar = fornecedor.Codigo " +
			  						"INNER JOIN TipoConsumo " +
		  						"ON parcela.TipoIdPar = tipoConsumo.CodigoTipo " +
		  							"WHERE TipoIdPar = ? AND " +
		  							"PagoPar = 0 " +
										"ORDER BY DataVencimentoPar, Numeropar");
 			
  			st.setInt(1, cod);
			rs = st.executeQuery();
			
			 
			List<Parcela> list = new ArrayList<>();
			Map<Integer, Fornecedor> mapFor = new HashMap<>();
			Map<Integer, TipoConsumo> mapTp = new HashMap<>();
			Map<Integer, ParPeriodo> mapPer = new HashMap<>();
			
			while (rs.next()) {
				Fornecedor objFor = mapFor.get(rs.getInt("FornecedorIdPar"));
				if (objFor == null) {
					objFor = instantiateFornecedor(rs);
					mapFor.put(rs.getInt("FornecedorIdPar"), objFor);
				}	
				TipoConsumo objTp = mapTp.get(rs.getInt("TipoIdPar"));
				if (objTp == null) {
					objTp = instantiateTipo(rs);
					mapTp.put(rs.getInt("TipoIdPar"), objTp);
				}	
				ParPeriodo objPer = mapPer.get(rs.getInt("PeriodoIdPar"));
				if (objPer == null) {
					objPer = instantiateParPeriodo(rs, objFor);
					mapPer.put(rs.getInt("PeriodoIdPar"), objPer);
				}	
				Parcela obj = instantiateParcela(rs, objFor, objTp, objPer);
				list.add(obj);
 			}
  			return list;
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
	public List<Parcela> findByIdTipoPago(int cod) {
		PreparedStatement st = null;
		ResultSet rs = null;
 		try {
			st = conn.prepareStatement(
 
					"SELECT * " +  
		  				"FROM parcela "  +
		  					"INNER JOIN parPeriodo " +
		  						"ON parcela.PeriodoIdPar = parPeriodo.IdPeriodo " +
			  						"INNER JOIN fornecedor " +
		  						"ON parcela.FornecedorIdPar = fornecedor.Codigo " +
			  						"INNER JOIN TipoConsumo " +
		  						"ON parcela.TipoIdPar = tipoConsumo.CodigoTipo " +
		  							"WHERE TipoIdPar = ? AND " +
		  							"PagoPar > 0 " +
										"ORDER BY - DataPagamentoPar, Numeropar");
 			
  			st.setInt(1, cod);
			rs = st.executeQuery();
			
			 
			List<Parcela> list = new ArrayList<>();
			Map<Integer, Fornecedor> mapFor = new HashMap<>();
			Map<Integer, TipoConsumo> mapTp = new HashMap<>();
			Map<Integer, ParPeriodo> mapPer = new HashMap<>();
			
			while (rs.next()) {
				Fornecedor objFor = mapFor.get(rs.getInt("FornecedorIdPar"));
				if (objFor == null) {
					objFor = instantiateFornecedor(rs);
					mapFor.put(rs.getInt("FornecedorIdPar"), objFor);
				}	
				TipoConsumo objTp = mapTp.get(rs.getInt("TipoIdPar"));
				if (objTp == null) {
					objTp = instantiateTipo(rs);
					mapTp.put(rs.getInt("TipoIdPar"), objTp);
				}	
				ParPeriodo objPer = mapPer.get(rs.getInt("PeriodoIdPar"));
				if (objPer == null) {
					objPer = instantiateParPeriodo(rs, objFor);
					mapPer.put(rs.getInt("PeriodoIdPar"), objPer);
				}	
				Parcela obj = instantiateParcela(rs, objFor, objTp, objPer);
				list.add(obj);
 			}
  			return list;
 		}
		catch (SQLException e) {
			throw new DbException (e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
 	}
 }
