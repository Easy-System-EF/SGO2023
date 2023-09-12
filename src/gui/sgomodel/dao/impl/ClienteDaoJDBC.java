package gui.sgomodel.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import gui.sgomodel.dao.ClienteDao;
import gui.sgomodel.entities.Cliente;
 
public class ClienteDaoJDBC implements ClienteDao {
	
// tb entra construtor p/ conex�o
	private Connection conn;
	
	public ClienteDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	String classe = "Cliente ";
	
	@Override
	public void insert(Cliente obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO cliente " +
				      "(NomeCli, RuaCli, NumeroCli, ComplementoCli, BairroCli, CidadeCli, "
				      + "UFCli, CepCli, Ddd01Cli, Telefone01Cli, Ddd02Cli, Telefone02Cli, "
				      + "EmailCli, PessoaCli, CpfCli, CnpjCli, ValorClass, PercentualClass, LetraClass, VisitaClass )" 
  				      + "VALUES " +
				      "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
 					 Statement.RETURN_GENERATED_KEYS); 

			st.setString(1, obj.getNomeCli());
 			st.setString(2, obj.getRuaCli());
   			st.setInt(3, obj.getNumeroCli());
 			st.setString(4, obj.getComplementoCli());
 			st.setString(5, obj.getBairroCli());
 			st.setString(6, obj.getCidadeCli());
 			st.setString(7, obj.getUfCli());
  			st.setString(8,  obj.getCepCli());
  			st.setInt(9, obj.getDdd01Cli());
   			st.setInt(10,  obj.getTelefone01Cli());
  			st.setInt(11, obj.getDdd02Cli());
   			st.setInt(12, obj.getTelefone02Cli());
  			st.setString(13, obj.getEmailCli());
   			st.setString(14, String.valueOf(obj.getPessoaCli()));
  			st.setString(15, obj.getCpfCli());
   			st.setString(16, obj.getCnpjCli());
   			st.setDouble(17, obj.getValorClass());
   			st.setDouble(18, obj.getPercentualClass());
   			st.setString(19, String.valueOf(obj.getLetraClass()));
   			st.setInt(20, obj.getVisitaClass());
   			
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	int codigo = rs.getInt(1);
					obj.setCodigoCli(codigo);
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
	public void insertBackUp(Cliente obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO cliente " +
				      "(CodigoCli, NomeCli, RuaCli, NumeroCli, ComplementoCli, BairroCli, CidadeCli, "
				      + "UFCli, CepCli, Ddd01Cli, Telefone01Cli, Ddd02Cli, Telefone02Cli, "
				      + "EmailCli, PessoaCli, CpfCli, CnpjCli, ValorClass, PercentualClass, LetraClass, VisitaClass )" 
  				      + "VALUES " +
				      "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"); 

			st.setInt(1, obj.getCodigoCli());
			st.setString(2, obj.getNomeCli());
 			st.setString(3, obj.getRuaCli());
   			st.setInt(4, obj.getNumeroCli());
 			st.setString(5, obj.getComplementoCli());
 			st.setString(6, obj.getBairroCli());
 			st.setString(7, obj.getCidadeCli());
 			st.setString(8, obj.getUfCli());
  			st.setString(9,  obj.getCepCli());
  			st.setInt(10, obj.getDdd01Cli());
   			st.setInt(11,  obj.getTelefone01Cli());
  			st.setInt(12, obj.getDdd02Cli());
   			st.setInt(13, obj.getTelefone02Cli());
  			st.setString(14, obj.getEmailCli());
   			st.setString(15, String.valueOf(obj.getPessoaCli()));
  			st.setString(16, obj.getCpfCli());
   			st.setString(17, obj.getCnpjCli());
   			st.setDouble(18, obj.getValorClass());
   			st.setDouble(19, obj.getPercentualClass());
   			st.setString(20, String.valueOf(obj.getLetraClass()));
   			st.setInt(21, obj.getVisitaClass());
   			
 			st.executeUpdate();
  		}
 		catch (SQLException e) {
			throw new DbException(classe + "Erro!!! sem inclusão " + e.getMessage() );
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
 
	@Override
	public void update(Cliente obj) {
		PreparedStatement st = null;
  		try {
			st = conn.prepareStatement(
					"UPDATE cliente " +  
							"SET NomeCli = ?, "
							+ "RuaCli = ?, "
							+ "NumeroCli = ?, "
							+ "ComplementoCli = ?, "
							+ "BairroCli = ?, "
							+ "CidadeCli = ?, "
							+ "UFCli = ?, "
							+ "CepCli = ?, "
							+ "Ddd01Cli = ?, "
 							+ "Telefone01Cli = ?, "
							+ "Ddd02Cli = ?, "
 							+ "Telefone02Cli = ?, "
 							+ "EmailCli = ?, "
 							+ "PessoaCli = ?, " 
							+ "CpfCli = ?, " 
 							+ "CnpjCli = ?, " 
							+ "ValorClass = ?, "
 							+ "PercentualClass = ?, "
							+ "LetraClass = ?, "
 							+ "VisitaClass = ? " +
 					"WHERE (CodigoCli = ?)",
        			 Statement.RETURN_GENERATED_KEYS);

 			st.setString(1, obj.getNomeCli());
			st.setString(2,  obj.getRuaCli());
			st.setInt(3, obj.getNumeroCli());
			st.setString(4,  obj.getComplementoCli());
			st.setString(5,  obj.getBairroCli());
			st.setString(6,  obj.getCidadeCli());
			st.setString(7,  obj.getUfCli());
			st.setString(8, obj.getCepCli());
  			st.setInt(9, obj.getDdd01Cli());
   			st.setInt(10,  obj.getTelefone01Cli());
  			st.setInt(11, obj.getDdd02Cli());
   			st.setInt(12, obj.getTelefone02Cli());
  			st.setString(13, obj.getEmailCli());
   			st.setString(14, String.valueOf(obj.getPessoaCli()));
   			st.setString(15, obj.getCpfCli());
   			st.setString(16, obj.getCnpjCli());
   			st.setDouble(17, obj.getValorClass());
   			st.setDouble(18, obj.getPercentualClass());
   			st.setString(19, String.valueOf(obj.getLetraClass()));
   			st.setInt(20, obj.getVisitaClass());
  			st.setInt(21, obj.getCodigoCli());
			
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
   		try {
			st = conn.prepareStatement(
					"DELETE FROM cliente WHERE CodigoCli = ?", Statement.RETURN_GENERATED_KEYS);
				  
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
	public Cliente findById(Integer codigo) {
 		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				 "SELECT * FROM cliente " +
 				 "WHERE CodigoCli = ?");
 			
			st.setInt(1, codigo);
			rs = st.executeQuery();
			if (rs.next())
			{	Cliente obj = instantiateCliente(rs);
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
	public List<Cliente> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT * FROM cliente " +
					"ORDER BY NomeCli");
			
			rs = st.executeQuery();
			
			List<Cliente> list = new ArrayList<>();
			
			while (rs.next())
			{	Cliente obj = instantiateCliente(rs);
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
	public List<Cliente> findAllId() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT * FROM cliente " +
					"ORDER BY CodigoCli");
			
			rs = st.executeQuery();
			
			List<Cliente> list = new ArrayList<>();
			
			while (rs.next())
			{	Cliente obj = instantiateCliente(rs);
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
	public List<Cliente> findMVR() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT * FROM cliente " +
						"WHERE percentualClass > 0 " +
					"ORDER BY - PercentualClass ");
			
			rs = st.executeQuery();
			
			List<Cliente> list = new ArrayList<>();
			
			while (rs.next())
			{	Cliente obj = instantiateCliente(rs);
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
	public List<Cliente> findABC() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT * FROM cliente " +
						"WHERE ValorClass > 0 " +
					"ORDER BY - ValorClass ");
			
			rs = st.executeQuery();
			
			List<Cliente> list = new ArrayList<>();
			
			while (rs.next())
			{	Cliente obj = instantiateCliente(rs);
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
	public List<Cliente> findPesquisa(String str) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT * FROM cliente " +
						"WHERE NomeCli like ? " +	
					"ORDER BY NomeCli");
			st.setString(1, str + "%");
			rs = st.executeQuery();
			
			List<Cliente> list = new ArrayList<>();
			
			while (rs.next())
			{	Cliente obj = instantiateCliente(rs);
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
	
	private Cliente instantiateCliente(ResultSet rs) throws SQLException {
 		Cliente cli = new Cliente();
 		
 		cli.setCodigoCli(rs.getInt("CodigoCli"));
 		cli.setNomeCli(rs.getString("NomeCli"));	
 		cli.setRuaCli(rs.getString("RuaCli"));
 		cli.setNumeroCli(rs.getInt("NumeroCli"));
 		cli.setComplementoCli(rs.getString("ComplementoCli"));
 		cli.setBairroCli(rs.getString("BairroCli"));
 		cli.setCidadeCli(rs.getString("CidadeCli")); 
 		cli.setUfCli(rs.getString("UFCli"));
 		cli.setCepCli(rs.getString("CepCli"));
 		cli.setDdd01Cli(rs.getInt("Ddd01Cli"));
  		cli.setTelefone01Cli(rs.getInt("Telefone01Cli"));
 		cli.setDdd02Cli(rs.getInt("Ddd02Cli"));
  		cli.setTelefone02Cli(rs.getInt("Telefone02Cli"));
  		cli.setEmailCli(rs.getString("EmailCli"));
   		cli.setPessoaCli(rs.getString("PessoaCli").charAt(0));
   		cli.setCpfCli(rs.getString("CpfCli"));
   		cli.setCnpjCli(rs.getString("CnpjCli"));
   		cli.setValorClass(rs.getDouble("ValorClass"));
   		cli.setPercentualClass(rs.getDouble("PercentualClass"));
   		cli.setLetraClass(rs.getString("LetraClass").charAt(0));
   		cli.setVisitaClass(rs.getInt("VisitaClass"));
        return cli;
	}
}
