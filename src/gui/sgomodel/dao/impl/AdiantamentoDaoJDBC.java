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
							"(DataAdi, FunAdi, NomeFunAdi, cargoAdi, situacaoAdi, adiantamentoAdi, MesAdi, AnoAdi )" +
								"VALUES " + 
							"(?, ?, ?, ?, ?, ?, ?, ? )",
							Statement.RETURN_GENERATED_KEYS);

			st.setDate(1, new java.sql.Date(obj.getDataAdi().getTime()));
			st.setInt(2, obj.getFunAdi());
			st.setString(3, obj.getNomeFunAdi());
			st.setString(4, obj.getCargoAdi());
			st.setString(5, obj.getSituacaoAdi());
			st.setDouble(6, obj.getAdiantamentoAdi());
			st.setInt(7, obj.getMesAdi());
			st.setInt(8, obj.getAnoAdi());

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
							"(NumeroAdi, DataAdi, FunAdi, NomeFunAdi, CargoAdi, SituacaoAdi, AdiantamentoAdi, MesAdi, AnoAdi )" +
								"VALUES " + 
							"(?, ?, ?, ?, ?, ?, ?, ?, ? )");

			st.setInt(1, obj.getNumeroAdi());
			st.setDate(2, new java.sql.Date(obj.getDataAdi().getTime()));
			st.setInt(3, obj.getFunAdi());
			st.setString(4, obj.getNomeFunAdi());
			st.setString(5, obj.getCargoAdi());
			st.setString(6, obj.getSituacaoAdi());
			st.setDouble(7, obj.getAdiantamentoAdi());
			st.setInt(8, obj.getMesAdi());
			st.setInt(9, obj.getAnoAdi());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException("Erro!!! sem incluão " + classe + e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public Double findByTotalAdi(Integer mes, Integer ano, Integer codFun) {
		Double totAdi = null;
		PreparedStatement st = null;
		ResultSet rs = null;
   		try {
			st = conn.prepareStatement(
			
			"select SUM(AdiantamentoAdi) AS total from adiantamento " +
				"WHERE MesAdi = ? AND AnoAdi = ? AND FunAdi = ? "); 

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
	public Double findByTotalAdiAnual(Integer ano, Integer codFun) {
		Double totAdi = null;
		PreparedStatement st = null;
		ResultSet rs = null;
   		try {
			st = conn.prepareStatement(
			
			"select SUM(AdiantamentoAdi) AS total from adiantamento " +
				"WHERE AnoAdi = ? AND FunAdi = ? "); 

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
						"WHERE NomeFunAdi like ? ");

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
	public void zeraAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"TRUNCATE TABLE Adiantamento " );

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
		adiantamento.setFunAdi(rs.getInt("FunAdi"));
		adiantamento.setNomeFunAdi(rs.getString("NomeFunAdi"));
		adiantamento.setCargoAdi(rs.getNString("CargoAdi"));
		adiantamento.setSituacaoAdi(rs.getString("SituacaoAdi"));
		adiantamento.setAdiantamentoAdi(rs.getDouble("AdiantamentoAdi"));
		adiantamento.setMesAdi(rs.getInt("MesAdi"));
		adiantamento.setAnoAdi(rs.getInt("AnoAdi"));
		return adiantamento;
	}
}
