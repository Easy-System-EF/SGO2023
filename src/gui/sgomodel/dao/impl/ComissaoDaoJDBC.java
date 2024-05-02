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
import gui.sgomodel.dao.ComissaoDao;
import gui.sgomodel.entities.Comissao;

public class ComissaoDaoJDBC implements ComissaoDao {

// tb entra construtor p/ conex�o
	private Connection conn;
	
	public ComissaoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	String classe = "Comissao JDBC ";
	
	@Override
	public void insert(Comissao obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO comissao " +
							"(DataCom, FunCom, NomefunCom, CargoCom, SituacaoCom, OSCom, BalcaoCom, MesCom, AnoCom, "
							+ "PercentualCom, ProdutoCom, ComissaoCom )" +
								"VALUES " + 
							"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?  )",
							Statement.RETURN_GENERATED_KEYS);
			
			st.setDate(1, new java.sql.Date(obj.getDataCom().getTime()));
			st.setInt(2, obj.getFunCom());
			st.setString(3, obj.getNomeFunCom());
			st.setString(4, obj.getCargoCom());
			st.setString(5, obj.getSituacaoCom());
			st.setInt(6, obj.getOSCom());
			st.setInt(7, obj.getBalcaoCom());
			st.setInt(8, obj.getMesCom());
			st.setInt(9, obj.getAnoCom());
			st.setDouble(10, obj.getPercentualCom());
			st.setDouble(11, obj.getProdutoCom());
			st.setDouble(12, obj.getComissaoCom());

			int rowsaffectad = st.executeUpdate();

			if (rowsaffectad > 0) {
				rs = st.getGeneratedKeys();
				if (rs.next()) {
					int codigo = rs.getInt(1);
					obj.setNumeroCom(codigo);
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
	public void insertBackUp(Comissao obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO Comissao " +
						"(NumeroCom, DataCom, FunCom, NomeFunCom, CargoCom, SituacaoCom, OSCom, BalcaoCom, " +
								"MesCom, AnoCom, PercentualCom, ProdutoCom, ComissaoCom )" +
						"VALUES " +
							"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
								Statement.RETURN_GENERATED_KEYS);
			
			st.setInt(1, obj.getNumeroCom());
			st.setDate(2, new java.sql.Date(obj.getDataCom().getTime()));
			st.setInt(3, obj.getFunCom());
			st.setString(4, obj.getNomeFunCom());
			st.setString(5, obj.getCargoCom());
			st.setString(6, obj.getSituacaoCom());
			st.setInt(7, obj.getOSCom());
			st.setInt(8, obj.getBalcaoCom());
			st.setInt(9, obj.getMesCom());
			st.setInt(10, obj.getAnoCom());
			st.setDouble(11, obj.getPercentualCom());
			st.setDouble(12, obj.getProdutoCom());
			st.setDouble(13, obj.getComissaoCom());

			st.executeUpdate();
		}	
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
	
	@Override
	public void deleteById(Integer codigo) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM comissao WHERE NumeroCom = ? ",
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
	public Double comSumTotalFun(int mes, int ano, int codFun) {
		Double totCom = null;
		PreparedStatement st = null;
		ResultSet rs = null;
   		try {
			st = conn.prepareStatement(
					
			"select SUM(ComissaoCom) AS total from comissao " +
					"WHERE MesCom = ? AND AnoCom = ? AND FunCom = ? "); 
	
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
	public Double comSumTotalGeral(int mes, int ano) {
		Double totCom = null;
		PreparedStatement st = null;
		ResultSet rs = null;
   		try {
			st = conn.prepareStatement(
					
			"select SUM(ComissaoCom) AS total from comissao " +
					"WHERE MesCom = ? AND AnoCom = ? "); 
	
			st.setInt(1, mes);
			st.setInt(2, ano);
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
	public Double oSSumComissao(int mm, int aa, int os) {
		Double totCom = null;
		PreparedStatement st = null;
		ResultSet rs = null;
   		try {
			st = conn.prepareStatement(
			
			"select SUM(ComissaoCom) AS total from comissao " +
				"WHERE MesCom = ? AND AnoCom = ? AND OSCom = ? "); 

			st.setInt(1, mm);
			st.setInt(2, aa);
			st.setInt(3, os);
			
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
	public Double balcaoSumComissao(int mm, int aa, int bal) {
		Double totCom = null;
		PreparedStatement st = null;
		ResultSet rs = null;
   		try {
			st = conn.prepareStatement(
			
			"select SUM(ComissaoCom) AS total from comissao " +
				"WHERE MesCom = ? AND AnoCom = ? AND BalcaoCom = ? "); 

			st.setInt(1, mm);
			st.setInt(2, aa);
			st.setInt(3, bal);
			
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
	public List<Comissao> findByOS(Integer os) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM comissao " + 
						"WHERE OSCom = ? ");

			st.setInt(1, os);
			rs = st.executeQuery();

			List<Comissao> adi = new ArrayList<>();

			if (rs.next()) {
				Comissao obj = instantiateComissao(rs);
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
	public List<Comissao> findByBalcao(Integer bal) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM comissao " + 
						"WHERE BalcaoCom = ? ");

			st.setInt(1, bal);
			rs = st.executeQuery();

			List<Comissao> adi = new ArrayList<>();

			if (rs.next()) {
				Comissao obj = instantiateComissao(rs);
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
	public List<Comissao> findMesAno(int mm, int aa) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * " + 
						"FROM comissao " +
							"WHERE MesCom = ? AND AnoCom = ? " +
						"ORDER BY - DataCom ");

			st.setInt(1, mm);
			st.setInt(2, aa);
			rs = st.executeQuery();

			List<Comissao> list = new ArrayList<>();

			while (rs.next()) {
				Comissao obj = instantiateComissao(rs);
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
	public List<Comissao> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * " + 
						"FROM comissao " +
						"ORDER BY - NumeroCom ");

			rs = st.executeQuery();

			List<Comissao> list = new ArrayList<>();

			while (rs.next()) {
				Comissao obj = instantiateComissao(rs);
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
					"TRUNCATE TABLE Comissao " );

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
	
	private Comissao instantiateComissao(ResultSet rs) throws SQLException {
		Comissao comissao = new Comissao();
		comissao.setNumeroCom(rs.getInt("NumeroCom"));
		comissao.setDataCom(new java.sql.Date(rs.getTimestamp("DataCom").getTime()));
		comissao.setFunCom(rs.getInt("FunCom"));
		comissao.setNomeFunCom(rs.getString("NomeFunCom"));
		comissao.setCargoCom(rs.getString("CargoCom"));
		comissao.setSituacaoCom(rs.getString("SituacaoCom"));
		comissao.setOSCom(rs.getInt("OSCom"));
		comissao.setBalcaoCom(rs.getInt("BalcaoCom"));
		comissao.setMesCom(rs.getInt("MesCom"));
		comissao.setAnoCom(rs.getInt("AnoCom"));
		comissao.setPercentualCom(rs.getDouble("PercentualCom"));
		comissao.setProdutoCom(rs.getDouble("ProdutoCom"));
		comissao.setComissaoCom();
		return comissao;
	}
}
