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
import gui.sgomodel.dao.AdiantamentoDao;
import gui.sgomodel.entities.Adiantamento;

public class AdiantamentoDaoJDBC implements AdiantamentoDao {

// tb entra construtor p/ conex�o
	private Connection conn;
	
	public AdiantamentoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	String classe = "Adiantamento JDBC ";
	
	@Override
	public void insert(Adiantamento obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO adiantamento " +
							"(DataAdi, ValeAdi, MesAdi, AnoAdi, ValorAdi, OsAdi, BalcaoAdi, " +
							"ComissaoAdi, TipoAdi, salarioAdi, codigoFun, NomeFun, mesFun, anoFun, " +
							"cargoFun, situacaoFun, salarioFun, cargoId, situacaoId )" +
								"VALUES " + 
							"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
							Statement.RETURN_GENERATED_KEYS);

			st.setDate(1, new java.sql.Date(obj.getDataAdi().getTime()));
			st.setDouble(2, obj.getValeAdi());
			st.setInt(3, obj.getMesAdi());
			st.setInt(4, obj.getAnoAdi());
			st.setDouble(5,  obj.getValorAdi());
			st.setInt(6, obj.getOsAdi());
			st.setInt(7, obj.getBalcaoAdi());
			st.setDouble(8, obj.getComissaoAdi());
			st.setString(9, obj.getTipoAdi());
			st.setDouble(10, obj.getSalarioAdi());
			st.setInt(11, obj.getCodigoFun());
			st.setString(12, obj.getNomeFun());
			st.setInt(13, obj.getMesFun());
			st.setInt(14, obj.getAnoFun());
			st.setString(15, obj.getCargoFun());
			st.setString(16, obj.getSituacaoFun());
			st.setDouble(17, obj.getSalarioFun());
   			st.setInt(18, obj.getCargo().getCodigoCargo());
   			st.setInt(19,  obj.getSituacao().getNumeroSit());


			int rowsaffectad = st.executeUpdate();

			if (rowsaffectad > 0) {
				rs = st.getGeneratedKeys();
				if (rs.next()) {
					int codigo = rs.getInt(1);
					obj.setNumeroAdi(codigo);
				} else {
					throw new DbException(classe + " Erro!!! sem inclusão");
				}
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public void insertBackUp(Adiantamento obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO adiantamento " +
							"(NumeroAdi, DataAdi, ValeAdi, MesAdi, AnoAdi, ValorAdi, OsAdi, BalcaoAdi, " +
							"ComissaoAdi, TipoAdi, salarioAdi, codigoFun, NomeFun, mesFun, anoFun, " +
							"cargoFun, situacaoFun, salarioFun, cargoId, situacaoId )" +
								"VALUES " + 
							"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");

			st.setInt(1, obj.getNumeroAdi());
			st.setDate(2, new java.sql.Date(obj.getDataAdi().getTime()));
			st.setDouble(3, obj.getValeAdi());
			st.setInt(4, obj.getMesAdi());
			st.setInt(5, obj.getAnoAdi());
			st.setDouble(6,  obj.getValorAdi());
			st.setInt(7, obj.getOsAdi());
			st.setInt(8, obj.getBalcaoAdi());
			st.setDouble(9, obj.getComissaoAdi());
			st.setString(10, obj.getTipoAdi());
			st.setDouble(11, obj.getSalarioAdi());
			st.setInt(12, obj.getCodigoFun());
			st.setString(13, obj.getNomeFun());
			st.setInt(14, obj.getMesFun());
			st.setInt(15, obj.getAnoFun());
			st.setString(16, obj.getCargoFun());
			st.setString(17, obj.getSituacaoFun());
			st.setDouble(18, obj.getSalarioFun());
   			st.setInt(19, obj.getCargo().getCodigoCargo());
   			st.setInt(20,  obj.getSituacao().getNumeroSit());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException("Erro!!! sem incluão " + classe + e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public Double findByTotalCom(Integer mes, Integer ano, Integer codFun) {
		Double totCom = null;
		PreparedStatement st = null;
		ResultSet rs = null;
   		try {
			st = conn.prepareStatement(
					
			"select SUM(ComissaoAdi) AS total from adiantamento " +
					"WHERE MesAdi = ? AND AnoAdi = ? AND CodigoFun = ? "); 
	
			st.setInt(1, mes);
			st.setInt(2, ano);
			st.setInt(3, codFun);
			rs = st.executeQuery();
			while (rs.next()) {
				totCom = rs.getDouble("total");
			}	
   		}
 		catch (SQLException e) {
			throw new DbException ( "Erro!!! " + classe + "não totalizado " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		return totCom;
	}

	@Override
	public Double findByTotalAdi(Integer mes, Integer ano, Integer codFun) {
		Double totAdi = null;
		PreparedStatement st = null;
		ResultSet rs = null;
   		try {
			st = conn.prepareStatement(
			
			"select SUM(ValeAdi) AS total from adiantamento " +
				"WHERE MesAdi = ? AND AnoAdi = ? AND CodigoFun = ? "); 

			st.setInt(1, mes);
			st.setInt(2, ano);
			st.setInt(3, codFun);
			
			rs = st.executeQuery();
			while (rs.next()) {
				totAdi = rs.getDouble("total");
			}	
   		}
 		catch (SQLException e) {
			throw new DbException ( "Erro!!! " + classe + "não totalizado " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		return totAdi;
	}

	@Override
	public Double findByTotalComAnual(Integer ano, Integer codFun) {
		Double totCom = null;
		PreparedStatement st = null;
		ResultSet rs = null;
   		try {
			st = conn.prepareStatement(
					
			"select SUM(ComissaoAdi) AS total from adiantamento " +
					"WHERE AnoAdi = ? AND CodigoFun = ? "); 
	
			st.setInt(1, ano);
			st.setInt(2, codFun);
			rs = st.executeQuery();
			while (rs.next()) {
				totCom = rs.getDouble("total");
			}	
   		}
 		catch (SQLException e) {
			throw new DbException ( "Erro!!! " + classe + "não totalizado " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		return totCom;
	}

	@Override
	public Double findByTotalAdiAnual(Integer ano, Integer codFun) {
		Double totAdi = null;
		PreparedStatement st = null;
		ResultSet rs = null;
   		try {
			st = conn.prepareStatement(
			
			"select SUM(ValeAdi) AS total from adiantamento " +
				"WHERE AnoAdi = ? AND CodigoFun = ? "); 

			st.setInt(1, ano);
			st.setInt(2, codFun);
			
			rs = st.executeQuery();
			while (rs.next()) {
				totAdi = rs.getDouble("total");
			}	
   		}
 		catch (SQLException e) {
			throw new DbException ( "Erro!!! " + classe + "não totalizado " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		return totAdi;
	}

	@Override
	public Double findByTotalComOS(Integer os) {
		Double totCom = null;
		PreparedStatement st = null;
		ResultSet rs = null;
   		try {
			st = conn.prepareStatement(
					
			"select SUM(ComissaoAdi) AS total from adiantamento " +
					"WHERE OsAdi = ? "); 
	
			st.setInt(1, os);
			rs = st.executeQuery();
			while (rs.next()) {
				totCom = rs.getDouble("total");
			}	
   		}
 		catch (SQLException e) {
			throw new DbException ( "Erro!!! " + classe + "não totalizado " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		return totCom;
	}

	@Override
	public Double findByTotalComBal(Integer bal) {
		Double totAdi = null;
		PreparedStatement st = null;
		ResultSet rs = null;
   		try {
			st = conn.prepareStatement(
			
			"select SUM(ComissaoAdi) AS total from adiantamento " +
				"WHERE BalcaoAdi = ? "); 

			st.setInt(1, bal);
			
			rs = st.executeQuery();
			while (rs.next()) {
				totAdi = rs.getDouble("total");
			}	
   		}
 		catch (SQLException e) {
			throw new DbException ( "Erro!!! " + classe + "não totalizado " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		return totAdi;
	}

	@Override
	public void deleteById(Integer codigo) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM adiantamento WHERE NumeroAdi = ? ",
					Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, codigo);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(classe + " Erro!!! não excluído " + e.getMessage());
		} finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public List<Adiantamento> findPesquisaFun(String str) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT *, funcionario.* " + 
						"FROM adiantamento " + 
						"WHERE NomeFun like ? ");

			st.setString(1, str);
			rs = st.executeQuery();
			
			List<Adiantamento> adi = new ArrayList<>();

			if (rs.next()) {
				Adiantamento obj = instantiateAdiantamento(rs);
				adi.add(obj);
			}
			return adi;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
 			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Adiantamento> findByOs(Integer idOs) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * " + 
						"FROM adiantamento " + 
						"WHERE OsAdi = ? ");

			st.setInt(1, idOs);
			rs = st.executeQuery();

			List<Adiantamento> adi = new ArrayList<>();

			if (rs.next()) {
				Adiantamento obj = instantiateAdiantamento(rs);
				adi.add(obj);
			}
			return adi;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
 			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Adiantamento> findByBalcao(Integer idBalcao) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * " + 
						"FROM adiantamento " + 
						"WHERE BalcaoAdi = ? ");

			st.setInt(1, idBalcao);
			rs = st.executeQuery();

			List<Adiantamento> adi = new ArrayList<>();

			if (rs.next()) {
				Adiantamento obj = instantiateAdiantamento(rs);
				adi.add(obj);
			}
			return adi;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
 			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

//auxiliar
// tipo => "A" = vale
//         "B" = balcão	
//         "C" = comissão
	@Override
	public List<Adiantamento> findMesTipo(Integer mes, Integer ano, String tipo) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * " + 
						"FROM adiantamento " +
							"WHERE MesAdi = ? AND AnoAdi = ? AND TipoAdi = ? " +
						"ORDER BY - DataAdi ");

			st.setInt(1, mes);
			st.setInt(2, ano);
			st.setString(3, tipo);
			rs = st.executeQuery();

			List<Adiantamento> list = new ArrayList<>();

			while (rs.next()) {
				Adiantamento obj = instantiateAdiantamento(rs);
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
	public Adiantamento findMesIdFun(Integer cod, Integer mes, Integer ano, String tipo) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT *, funcionario.* " + 
						"FROM adiantamento, Funcionario " +
							"WHERE CodigoFun = ? AND MesAdi = ? AND AnoAdi = ? AND TipoAdi = ? " +
						"ORDER BY - DataAdi ");

			st.setInt(1, cod);
			st.setInt(2, mes);
			st.setInt(3, ano);
			st.setString(4, tipo);
			rs = st.executeQuery();

			while (rs.next()) {
				Adiantamento obj = instantiateAdiantamento(rs);
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
	public List<Adiantamento> findMes(Integer mes, Integer ano) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * " + 
						"FROM adiantamento " +
							"WHERE MesAdi = ? AND AnoAdi = ? " +
						"ORDER BY - DataAdi ");

			st.setInt(1, mes);
			st.setInt(2, ano);
			rs = st.executeQuery();

			List<Adiantamento> list = new ArrayList<>();

			while (rs.next()) {
				Adiantamento obj = instantiateAdiantamento(rs);
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
	public List<Adiantamento> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * " + 
						"FROM adiantamento " +
						"ORDER BY - DataAdi ");

			rs = st.executeQuery();

			List<Adiantamento> list = new ArrayList<>();

			while (rs.next()) {
				Adiantamento obj = instantiateAdiantamento(rs);
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
	public List<Adiantamento> findAllId() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * " + 
						"FROM adiantamento " +
						"ORDER BY NumeroAdi ");

			rs = st.executeQuery();

			List<Adiantamento> list = new ArrayList<>();

			while (rs.next()) {
				Adiantamento obj = instantiateAdiantamento(rs);
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
					"TRUNCATE TABLE sgb.Adiantamento " );

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
	

	private Adiantamento instantiateAdiantamento(ResultSet rs) throws SQLException {
		Adiantamento adiantamento = new Adiantamento();
		adiantamento.setNumeroAdi(rs.getInt("NumeroAdi"));
		adiantamento.setDataAdi(new java.util.Date(rs.getTimestamp("DataAdi").getTime()));
		adiantamento.setValeAdi(rs.getDouble("ValeAdi"));
		adiantamento.setMesAdi(rs.getInt("MesAdi"));
		adiantamento.setAnoAdi(rs.getInt("AnoAdi"));
		adiantamento.setValorAdi(rs.getDouble("ValorAdi"));
		adiantamento.setOsAdi(rs.getInt("OsAdi"));
		adiantamento.setBalcaoAdi(rs.getInt("BalcaoAdi"));
		adiantamento.setComissaoAdi(rs.getDouble("ComissaoAdi"));
		adiantamento.setTipoAdi(rs.getString("TipoAdi"));
		adiantamento.setSalarioAdi(rs.getDouble("SalarioAdi"));
		adiantamento.setCodigoFun(rs.getInt("CodigoFun"));
		adiantamento.setNomeFun(rs.getString("NomeFun"));
		adiantamento.setComissaoFun(rs.getDouble("ComissaoFun"));
		adiantamento.setMesFun(rs.getInt("MesFun"));
		adiantamento.setAnoFun(rs.getInt("AnoFun"));
		adiantamento.setCargoFun(rs.getNString("CargoFun"));
		adiantamento.setSituacaoFun(rs.getString("SituacaoFun"));
		adiantamento.setSalarioFun(rs.getDouble("SalarioFun"));		
		return adiantamento;
	}
}
