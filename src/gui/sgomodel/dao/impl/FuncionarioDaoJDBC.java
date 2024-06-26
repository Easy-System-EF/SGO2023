package gui.sgomodel.dao.impl;

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
import gui.sgomodel.dao.FuncionarioDao;
import gui.sgomodel.entities.Cargo;
import gui.sgomodel.entities.Funcionario;
import gui.sgomodel.entities.Situacao;
 
public class FuncionarioDaoJDBC implements FuncionarioDao {
	
// tb entra construtor p/ conex�o
	private Connection conn;

	public FuncionarioDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	String classe = "Funcionario JDBC";
		
	@Override
	public void insert(Funcionario obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO funcionario " +
				      "(NomeFun, EnderecoFun, BairroFun, CidadeFun, " +
				       "UfFun, CepFun, DddFun, TelefoneFun, " +
				       "CpfFun, PixFun, comissaoFun, adiantamentoFun, MesFun, AnoFun, CargoFun, SituacaoFun, "
				       + "SalarioFun, DataCadastroFun, CargoId, SituacaoId )" + 
  				    "VALUES " +
				      "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
 					 Statement.RETURN_GENERATED_KEYS); 
 
  			st.setString(1, obj.getNomeFun());
 			st.setString(2, obj.getEnderecoFun());
  			st.setString(3, obj.getBairroFun());
 			st.setString(4, obj.getCidadeFun());
 			st.setString(5, obj.getUfFun());
  			st.setString(6,  obj.getCepFun());
  			st.setInt(7, obj.getDddFun());
   			st.setInt(8,  obj.getTelefoneFun());
   			st.setString(9, obj.getCpfFun());
   			st.setString(10, obj.getPixFun());
   			st.setDouble(11, obj.getComissaoFun());
   			st.setDouble(12, obj.getAdiantamentoFun());
   			st.setInt(13, obj.getMesFun());
   			st.setInt(14, obj.getAnoFun());
   			st.setString(15, obj.getCargoFun());
   			st.setString(16, obj.getSituacaoFun());
   			st.setDouble(17, obj.getSalarioFun());
   			st.setDate(18, new java.sql.Date(obj.getDataCadastroFun().getTime()));
   			st.setInt(19, obj.getCargo().getCodigoCargo());
   			st.setInt(20,  obj.getSituacao().getNumeroSit());
   			
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	int codigo = rs.getInt(1);
					obj.setCodigoFun(codigo);
//					System.out.println("Novo inserido: " + obj.getCodigoFun());
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
	public void insertBackUp(Funcionario obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO funcionario " +
				      "(CodigoFun, NomeFun, EnderecoFun, BairroFun, CidadeFun, " +
				       "UfFun, CepFun, DddFun, TelefoneFun, " +
				       "CpfFun, PixFun, comissaoFun, adiantamentoFun, MesFun, AnoFun, CargoFun, SituacaoFun, SalarioFun, "
				       + "DataCadastroFun, CargoId, SituacaoId )" + 
  				    "VALUES " +
				      "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
 					 Statement.RETURN_GENERATED_KEYS); 

  			st.setInt(1, obj.getCodigoFun());
  			st.setString(2, obj.getNomeFun());
 			st.setString(3, obj.getEnderecoFun());
  			st.setString(4, obj.getBairroFun());
 			st.setString(5, obj.getCidadeFun());
 			st.setString(6, obj.getUfFun());
  			st.setString(7,  obj.getCepFun());
  			st.setInt(8, obj.getDddFun());
   			st.setInt(9,  obj.getTelefoneFun());
   			st.setString(10, obj.getCpfFun());
   			st.setString(11, obj.getPixFun());
   			st.setDouble(12, obj.getComissaoFun());
   			st.setDouble(13, obj.getAdiantamentoFun());
   			st.setInt(14, obj.getMesFun());
   			st.setInt(15, obj.getAnoFun());
   			st.setString(16, obj.getCargoFun());
   			st.setString(17, obj.getSituacaoFun());
   			st.setDouble(18, obj.getSalarioFun());
   			st.setDate(19, new java.sql.Date(obj.getDataCadastroFun().getTime()));
   			st.setInt(20, obj.getCargo().getCodigoCargo());
   			st.setInt(21,  obj.getSituacao().getNumeroSit());
   			
 			st.executeUpdate();
			
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
	public void update(Funcionario obj) {
		PreparedStatement st = null;
  		try {
			st = conn.prepareStatement(
					"UPDATE funcionario " +
						    "SET NomeFun = ?, EnderecoFun = ?, BairroFun = ?, CidadeFun = ?, " +
						       "UfFun = ?, CepFun = ?, DddFun = ?, TelefoneFun = ?, " +
						       "CpfFun = ?, PixFun = ?, comissaoFun = ?, adiantamentoFun = ?, mesFun = ?, anoFun = ?, " +
						       "CargoFun = ?, SituacaoFun = ?, SalarioFun = ?, DataCadastroFun = ?, CargoId = ?, SituacaoId = ? " + 
							"WHERE (CodigoFun = ?) ",
								Statement.RETURN_GENERATED_KEYS);
  			st.setString(1, obj.getNomeFun());
 			st.setString(2, obj.getEnderecoFun());
  			st.setString(3, obj.getBairroFun());
 			st.setString(4, obj.getCidadeFun());
 			st.setString(5, obj.getUfFun());
  			st.setString(6,  obj.getCepFun());
  			st.setInt(7, obj.getDddFun());
   			st.setInt(8,  obj.getTelefoneFun());
   			st.setString(9, obj.getCpfFun());
   			st.setString(10, obj.getPixFun());
   			st.setDouble(11, obj.getComissaoFun());
   			st.setDouble(12, obj.getAdiantamentoFun());
   			st.setInt(13, obj.getMesFun());
   			st.setInt(14, obj.getAnoFun());
   			st.setString(15, obj.getCargoFun());
   			st.setString(16, obj.getSituacaoFun());
   			st.setDouble(17, obj.getSalarioFun());
   			st.setDate(18, new java.sql.Date(obj.getDataCadastroFun().getTime()));
   			st.setInt(19, obj.getCargo().getCodigoCargo());
   			st.setInt(20,  obj.getSituacao().getNumeroSit());
   			st.setInt(21, obj.getCodigoFun());
			
 			st.executeUpdate();
   		} 
 		catch (SQLException e) {
 				throw new DbException (classe +  "Erro!!! sem atualização " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer codigo) {
		PreparedStatement st = null;
   		try {
			st = conn.prepareStatement(
					"DELETE FROM funcionario WHERE CodigoFun = ? ", 
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
	public Funcionario findById(Integer codigo) {
 		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				 "SELECT *, cargo.*, situacao.* "
					+ "FROM funcionario " 
					+ "INNER JOIN cargo "
						+ " ON funcionario.CargoId = cargo.CodigoCargo "
					+ "INNER JOIN situacao "
						+ " ON funcionario.SituacaoId = situacao.NumeroSit "
				+ "WHERE CodigoFun = ?");
 			
			st.setInt(1, codigo);
			rs = st.executeQuery();
			
			if (rs.next()) {
				Cargo cargo = instantiateCargo(rs);
				Situacao sit = instantiateSit(rs);
				Funcionario obj = instantiateFuncionario(rs, cargo, sit);
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
	public List<Funcionario> findAll(Date data) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT *, cargo.*, situacao.* " 
						+ "FROM funcionario " 
							+ "INNER JOIN cargo " 
								+ "ON funcionario.CargoId = cargo.CodigoCargo "
							+ "INNER JOIN situacao "
								+ " ON funcionario.SituacaoId = situacao.NumeroSit "
								+ "WHERE DataCadastroFun <= ? "
						+ "ORDER BY NomeFun ");
			
			st.setDate(1, new java.sql.Date(data.getTime()));
			rs = st.executeQuery();
			
			List<Funcionario> list = new ArrayList<>();
			Map<Integer, Cargo> mapCargo = new HashMap<>();
			Map<Integer, Situacao> mapSit = new HashMap<>();
			
			while (rs.next())
			{	Cargo objCargo = mapCargo.get(rs.getInt("CargoId"));
				if (objCargo == null) {
					objCargo = instantiateCargo(rs);
					mapCargo.put(rs.getInt("CargoId"), objCargo);
				}
				Situacao objSit = mapSit.get(rs.getInt("NumeroSit"));
				if (objSit == null) {
					objSit = instantiateSit(rs);
					mapSit.put(rs.getInt("NumeroSit"), objSit);
				}
				Funcionario obj = instantiateFuncionario(rs, objCargo, objSit);
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
	public List<Funcionario> findPesquisa(String str, Date data) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT *, cargo.*, situacao.* " 
						+ "FROM funcionario " 
							+ "INNER JOIN cargo " 
								+ "ON funcionario.CargoId = cargo.CodigoCargo "
							+ "INNER JOIN situacao "
								+ " ON funcionario.SituacaoId = situacao.NumeroSit "
								+ "WHERE NomeFun like ? AND DataCadastroFun <= ?   "
						+ "ORDER BY NomeFun ");
			st.setString(1, str + "%");
			st.setDate(2, new java.sql.Date(data.getTime()));			
			rs = st.executeQuery();
			
			List<Funcionario> list = new ArrayList<>();
			Map<Integer, Cargo> mapCargo = new HashMap<>();
			Map<Integer, Situacao> mapSit = new HashMap<>();
			
			while (rs.next())
			{	Cargo objCargo = mapCargo.get(rs.getInt("CargoId"));
				if (objCargo == null) {
					objCargo = instantiateCargo(rs);
					mapCargo.put(rs.getInt("CargoId"), objCargo);
				}
				Situacao objSit = mapSit.get(rs.getInt("NumeroSit"));
				if (objSit == null) {
					objSit = instantiateSit(rs);
					mapSit.put(rs.getInt("NumeroSit"), objSit);
				}
				Funcionario obj = instantiateFuncionario(rs, objCargo, objSit);
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
	public List<Funcionario> findByAtivo(String situacao, Date data) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT *, cargo.*, situacao.* " + 
						 "FROM funcionario " +
							 "INNER JOIN cargo " +
								 "ON funcionario.CargoId = cargo.CodigoCargo " +
							  "INNER JOIN situacao " +
								 " ON funcionario.SituacaoId = situacao.NumeroSit " +
									"WHERE SituacaoFun = ? AND DataCadastroFun <= ? " +
						 "ORDER BY NomeFun ");
			st.setString(1, situacao);
			st.setDate(2, new java.sql.Date(data.getTime()));
			rs = st.executeQuery();
			
			List<Funcionario> fun = new ArrayList<>();
			Map<Integer, Cargo> mapCargo = new HashMap<>();
			Map<Integer, Situacao> mapSit = new HashMap<>();
			
			while (rs.next())
			{	Cargo objCargo = mapCargo.get(rs.getInt("CargoId"));
				if (objCargo == null) {
					objCargo = instantiateCargo(rs);
					mapCargo.put(rs.getInt("CargoId"), objCargo);
				}
				Situacao objSit = mapSit.get(rs.getInt("NumeroSit"));
				if (objSit == null) {
					objSit = instantiateSit(rs);
					mapSit.put(rs.getInt("NumeroSit"), objSit);
				}
				Funcionario obj = instantiateFuncionario(rs, objCargo, objSit);
 				fun.add(obj);
 			}
			return fun;
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
					"TRUNCATE TABLE sgb.Funcionario " );

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
	
	private Funcionario instantiateFuncionario(ResultSet rs, Cargo objCargo,
							Situacao objSit) throws SQLException {
 		Funcionario fun = new Funcionario();
 		
 		fun.setCodigoFun(rs.getInt("CodigoFun"));
 		fun.setNomeFun(rs.getString("NomeFun"));	
 		fun.setEnderecoFun(rs.getString("EnderecoFun"));
  		fun.setBairroFun(rs.getString("BairroFun"));
 		fun.setCidadeFun(rs.getString("CidadeFun")); 
 		fun.setUfFun(rs.getString("UfFun"));
 		fun.setCepFun(rs.getString("CepFun"));
 		fun.setDddFun(rs.getInt("DddFun"));
  		fun.setTelefoneFun(rs.getInt("TelefoneFun"));
   		fun.setCpfFun(rs.getString("CpfFun"));
   		fun.setPixFun(rs.getString("PixFun"));
   		fun.totalComissao(rs.getDouble("ComissaoFun"));
   		fun.totalAdiantamentoFun(rs.getDouble("AdiantamentoFun"));
   		fun.setMesFun(rs.getInt("MesFun"));
   		fun.setAnoFun(rs.getInt("AnoFun"));
   		fun.setCargoFun(rs.getString("CargoFun"));
   		fun.setSituacaoFun(rs.getString("SituacaoFun"));
   		fun.setSalarioFun(rs.getDouble("SalarioFun"));
   		fun.setDataCadastroFun(new java.util.Date(rs.getTimestamp("DataCadastroFun").getTime()));
   		fun.setCargo(objCargo);
   		fun.setSituacao(objSit);
        return fun;
	}

	private Cargo instantiateCargo(ResultSet rs) throws SQLException {
		Cargo cargo = new Cargo();
		cargo.setCodigoCargo(rs.getInt("CodigoCargo"));
		cargo.setNomeCargo(rs.getString("NomeCargo"));
		cargo.setSalarioCargo(rs.getDouble("SalarioCargo"));
		cargo.setComissaoCargo(rs.getDouble("ComissaoCargo"));
		return cargo;
	}	

	private Situacao instantiateSit(ResultSet rs) throws SQLException {
		Situacao sit = new Situacao();
		sit.setNumeroSit(rs.getInt("NumeroSit"));
		sit.setNomeSit(rs.getString("NomeSit"));
		return sit;
	}	
}
