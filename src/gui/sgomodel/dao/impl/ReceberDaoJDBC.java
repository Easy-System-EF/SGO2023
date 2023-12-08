package gui.sgomodel.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import db.DB;
import db.DbException;
import gui.sgcpmodel.entities.consulta.ParPeriodo;
import gui.sgomodel.dao.ReceberDao;
import gui.sgomodel.entities.Receber;

public class ReceberDaoJDBC implements ReceberDao {

// aq entra um construtor declarando a conex�o	
	private Connection conn;

	public ReceberDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	Locale ptBR = new Locale("pt", "BR");
	String classe = "Receber JDBC ";

	@Override
	public void insert(Receber obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("INSERT INTO receber " + "(FuncionarioRec, ClienteRec, NomeClienteRec, "
					+ "OsRec, DataOsRec, PlacaRec, ParcelaRec, FormaPagamentoRec, "
					+ "ValorRec, DataVencimentoRec, DataPagamentoRec, PeriodoIdRec, jurosRec, descontoRec, "
					+ "totalRec, valorPagoRec ) " + "VALUES " + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
// retorna o o novo Id inserido 					
					Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, obj.getFuncionarioRec());
			st.setInt(2, obj.getClienteRec());
			st.setString(3, obj.getNomeClienteRec());
			st.setInt(4, obj.getOsRec());
			st.setDate(5, new java.sql.Date(obj.getDataOsRec().getTime()));
			st.setString(6, obj.getPlacaRec());
			st.setInt(7, obj.getParcelaRec());
			st.setString(8, obj.getFormaPagamentoRec());
			st.setDouble(9, obj.getValorRec());
			st.setDate(10, new java.sql.Date(obj.getDataVencimentoRec().getTime()));
			st.setDate(11, new java.sql.Date(obj.getDataPagamentoRec().getTime()));
			st.setInt(12, obj.getPeriodo().getIdPeriodo());
			st.setDouble(13, obj.getJurosRec());
			st.setDouble(14, obj.getDescontoRec());
			st.setDouble(15, obj.getTotalRec());
			st.setDouble(16, obj.getValorPagoRec());

			int rowsaffectad = st.executeUpdate();

			if (rowsaffectad > 0) {
				rs = st.getGeneratedKeys();
				if (rs.next()) {
					int codigo = rs.getInt(1);
					obj.setNumeroRec(codigo);
				} else {
					throw new DbException(classe + "Erro!!! sem inclusão");
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
	public void insertBackUp(Receber obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("INSERT INTO receber " 
					+ "(NumeroRec, FuncionarioRec, ClienteRec, NomeClienteRec, "
						+ "OsRec, DataOsRec, PlacaRec, ParcelaRec, FormaPagamentoRec, "
						+ "ValorRec, DataVencimentoRec, DataPagamentoRec, PeriodoIdRec, jurosRec, descontoRec, "
						+ "totalRec, valorPagoRec ) " 
					+ "VALUES " 
						+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");

			st.setInt(1, obj.getNumeroRec());
			st.setInt(2, obj.getFuncionarioRec());
			st.setInt(3, obj.getClienteRec());
			st.setString(4, obj.getNomeClienteRec());
			st.setInt(5, obj.getOsRec());
			st.setDate(6, new java.sql.Date(obj.getDataOsRec().getTime()));
			st.setString(7, obj.getPlacaRec());
			st.setInt(8, obj.getParcelaRec());
			st.setString(9, obj.getFormaPagamentoRec());
			st.setDouble(10, obj.getValorRec());
			st.setDate(11, new java.sql.Date(obj.getDataVencimentoRec().getTime()));
			st.setDate(12, new java.sql.Date(obj.getDataPagamentoRec().getTime()));
			st.setInt(13, obj.getPeriodo().getIdPeriodo());
			st.setDouble(14, obj.getJurosRec());
			st.setDouble(15, obj.getDescontoRec());
			st.setDouble(16, obj.getTotalRec());
			st.setDouble(17, obj.getValorPagoRec());

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(classe + "Erro!!! sem inclusão " + e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Receber obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(

					"UPDATE receber " 
							+ "SET FuncionarioRec = ?, ClienteRec = ?, NomeClienteRec = ?, "
							+ "OsRec = ?, DataOsRec = ?, PlacaRec = ?, ParcelaRec = ?, "
							+ "FormaPagamentoRec = ?, ValorRec = ?, DataVencimentoRec = ?, "
							+ "DataPagamentoRec = ?, PeriodoIdRec = ?, JurosRec = ?, DescontoRec = ?, "
							+ "TotalRec = ?, ValorPagoRec = ? "
							+ "WHERE (NumeroRec = ? ) ",
					Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, obj.getFuncionarioRec());
			st.setInt(2, obj.getClienteRec());
			st.setString(3, obj.getNomeClienteRec());
			st.setInt(4, obj.getOsRec());
			st.setDate(5, new java.sql.Date(obj.getDataOsRec().getTime()));
			st.setString(6, obj.getPlacaRec());
			st.setInt(7, obj.getParcelaRec());
			st.setString(8, obj.getFormaPagamentoRec());
			st.setDouble(9, obj.getValorRec());
			st.setDate(10, new java.sql.Date(obj.getDataVencimentoRec().getTime()));
			st.setDate(11, new java.sql.Date(obj.getDataPagamentoRec().getTime()));
			st.setInt(12, obj.getPeriodo().getIdPeriodo());
			st.setDouble(13, obj.getJurosRec());
			st.setDouble(14, obj.getDescontoRec());
			st.setDouble(15, obj.getTotalRec());
			st.setDouble(16, obj.getValorPagoRec());
			st.setInt(17, obj.getNumeroRec());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void removeOS(int numOs) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM receber WHERE OsRec = ? ", Statement.RETURN_GENERATED_KEYS);
			st.setInt(1, numOs);

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(classe + "Erro receber !!! sem exclusão " + e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	private Receber instantiateReceber(ResultSet rs, ParPeriodo per) throws SQLException {
		Receber obj = new Receber();
		obj.setNumeroRec(rs.getInt("NumeroRec"));
		obj.setFuncionarioRec(rs.getInt("FuncionarioRec"));
		obj.setClienteRec(rs.getInt("ClienteRec"));
		obj.setNomeClienteRec(rs.getString("NomeClienteRec"));
		obj.setOsRec(rs.getInt("OsRec"));
		obj.setDataOsRec(new java.util.Date(rs.getTimestamp("DataOsRec").getTime()));
		obj.setPlacaRec(rs.getString("PlacaRec"));
		obj.setParcelaRec(rs.getInt("ParcelaRec"));
		obj.setFormaPagamentoRec(rs.getString("FormaPagamentoRec"));
		obj.setValorRec(rs.getDouble("ValorRec"));
		obj.setDataVencimentoRec(new java.util.Date(rs.getTimestamp("DataVencimentoRec").getTime()));
		obj.setDataPagamentoRec(new java.util.Date(rs.getTimestamp("DataPagamentoRec").getTime()));
//objetos montados (sem id e com a associa��o la da classe Fornrecedor fornecedor...				
		obj.setPeriodo(per);
		obj.setJurosRec(rs.getDouble("JurosRec"));
		obj.setDescontoRec(rs.getDouble("DescontoRec"));
		obj.setTotalRec(rs.getDouble("TotalRec"));
		obj.setValorPagoRec(rs.getDouble("ValorPagoRec"));
		return obj;
	}

	private ParPeriodo instantiateParPeriodo(ResultSet rs) throws SQLException {
		ParPeriodo per = new ParPeriodo();
		per.setIdPeriodo(rs.getInt("IdPeriodo"));
		per.setDtiPeriodo(new java.util.Date(rs.getTimestamp("DtiPeriodo").getTime()));
		per.setDtfPeriodo(new java.util.Date(rs.getTimestamp("DtfPeriodo").getTime()));
		return per;
	}

	@Override
	public List<Receber> findByAllOs(Integer os) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT *, parPeriodo.* "  
						+ "FROM receber " 
							+ "INNER JOIN parPeriodo "
								+ "ON receber.PeriodoIdRec = parPeriodo.IdPeriodo " 
					+ "Where OsRec = ? "
						+ "ORDER BY DataVencimentoRec, ParcelaRec ");

			st.setInt(1, os);
			rs = st.executeQuery();

			List<Receber> list = new ArrayList<>();

			while (rs.next()) {
				ParPeriodo per = instantiateParPeriodo(rs);
				Receber obj = instantiateReceber(rs, per);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Receber> findAllAberto() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT *, parPeriodo.* " + 
						"FROM receber " + 
							"INNER JOIN parPeriodo "
								+ "ON receber.PeriodoIdRec = parPeriodo.IdPeriodo " + 
							"WHERE ValorPagoRec = 0.00 "
					+ "ORDER BY DataVencimentoRec ");

			rs = st.executeQuery();

			List<Receber> list = new ArrayList<>();

			while (rs.next()) {
				ParPeriodo per = instantiateParPeriodo(rs);
				Receber obj = instantiateReceber(rs, per);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Receber> findAllPago(Date dti, Date dtf) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * " + 
						"FROM receber " + 
							"INNER JOIN parPeriodo " +
								"ON receber.PeriodoIdRec = parPeriodo.IdPeriodo " + 
							"WHERE (ValorPagoREc > 0.00 ) AND (DataVencimentoRec >= ? ) AND (DataVencimentoRec <= ? ) " +
					 	"ORDER BY - DataVencimentoRec, ParcelaRec ");

			st.setDate(1, new java.sql.Date(dti.getTime()));
			st.setDate(2, new java.sql.Date(dtf.getTime()));
			
			rs = st.executeQuery();

			List<Receber> list = new ArrayList<>();

			while (rs.next()) {
				ParPeriodo per = instantiateParPeriodo(rs);
				Receber obj = instantiateReceber(rs, per);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public Double findPagoOsMes(Date dti, Date dtf) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					
				"SELECT SUM(ValorPagoRec) AS 'totMes' FROM receber " +
					"WHERE (ValorPagoRec > 0) AND (DataVencimentoRec >= ?) AND (DataPagamentoRec <= ?) " + 
					 "AND (PlacaRec != 'Balcão' OR 'Balcao' ) ");

					st.setDate(1, new java.sql.Date(dti.getTime()));
					st.setDate(2, new java.sql.Date(dtf.getTime()));

					rs = st.executeQuery();

					while (rs.next()) {
						Double vlrRec = rs.getDouble("totMes");
						return vlrRec;
					}
					return null;
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
	public Double findAbertoOsMes(Date dti, Date dtf) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(

					"SELECT SUM(ValorRec) AS 'totMes' FROM receber " +
					"WHERE (ValorPagoRec = 0) AND (PlacaRec != ('Balcão' OR 'Balcao' )) AND " +
						"(DataVencimentoRec >= ?) AND (DataPagamentoRec <= ?) ");

					st.setDate(1, new java.sql.Date(dti.getTime()));
					st.setDate(2, new java.sql.Date(dtf.getTime()));

					rs = st.executeQuery();

					while (rs.next()) {
						Double vlrRec = rs.getDouble("totMes");
						return vlrRec;
					}
					return null;
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
	public Double findPagoBalMes(Date dtiRP, Date dtfRP) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(

					"SELECT SUM(ValorPagoRec) AS 'totMes' FROM receber " +
					"WHERE (ValorPagoRec > 0) AND (PlacaRec = ('Balcão' OR 'Balcao' )) AND " +
						"(DataVencimentoRec >= ?) AND (DataPagamentoRec <= ?) ");

					st.setDate(1, new java.sql.Date(dtiRP.getTime()));
					st.setDate(2, new java.sql.Date(dtfRP.getTime()));

					rs = st.executeQuery();

					while (rs.next()) {
						Double vlrRec = rs.getDouble("totMes");
						return vlrRec;
					}
					return null;
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
	public Double findAbertoBalMes(Date dti, Date dtf) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(

				"SELECT SUM(ValorRec) AS 'totMes' FROM receber " +
					"WHERE (ValorPagoRec = 0) AND (PlacaRec = ('Balcão' OR 'Balcao' )) AND " +
						"(DataVencimentoRec >= ?) AND (DataVencimentoRec <= ?) ");

					st.setDate(1, new java.sql.Date(dti.getTime()));
					st.setDate(2, new java.sql.Date(dtf.getTime()));

					rs = st.executeQuery();

					while (rs.next()) {
						Double vlrRec = rs.getDouble("totMes");
						return vlrRec;
					}
					return null;
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
	public Double sumPagoCli(int cod) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(

				"SELECT SUM(ValorPagoRec) AS 'totCli' FROM receber " + 
					"WHERE ValorPagoRec > 0.00 AND ClienteRec = ? ");

					st.setInt(1, cod);

					rs = st.executeQuery();

					while (rs.next()) {
						Double vlrCli = rs.getDouble("totCli");
						return vlrCli;
					}
					return null;
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
	public List<Receber> findPeriodoAberto() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * " + "FROM receber " + "INNER JOIN parPeriodo "
					+ "ON receber.PeriodoIdRec = parPeriodo.IdPeriodo " + "WHERE DataOsRec >= DataPagamentoRec AND "
					+ "receber.DataVencimentoRec >= parPeriodo.DtiPeriodo AND "
					+ "receber.DataVencimentoRec <= parPeriodo.DtfPeriodo AND " + "ValorPagoRec = 0.00 "
					+ "ORDER BY DataVencimentoRec, ParcelaRec ");

			rs = st.executeQuery();

			List<Receber> list = new ArrayList<>();

			while (rs.next()) {
				ParPeriodo per = instantiateParPeriodo(rs);
				Receber obj = instantiateReceber(rs, per);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Receber> findPeriodoPago() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(

					"SELECT * " + 
						"FROM receber " + 
							"INNER JOIN parPeriodo "
								+ "ON receber.PeriodoIdRec = parPeriodo.IdPeriodo "
									+ "WHERE  DataPagamentoRec >= DataOsRec AND "
										+ "receber.DataVencimentoRec >= parPeriodo.DtiPeriodo AND "
										+ "receber.DataVencimentoRec <= parPeriodo.DtfPeriodo AND " 
										+ "ValorPagoRec > 0.00 "
							+ "ORDER BY DataVencimentoRec, ParcelaRec ");

			rs = st.executeQuery();

			List<Receber> list = new ArrayList<>();

			while (rs.next()) {
				ParPeriodo per = instantiateParPeriodo(rs);
				Receber obj = instantiateReceber(rs, per);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Receber> findByIdClienteAberto(Integer cod) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT *, parPeriodo.* " + "FROM receber " + "INNER JOIN parPeriodo "
					+ "ON receber.PeriodoIdRec = parPeriodo.IdPeriodo "
					+ "WHERE ClienteRec = ? AND ValorPagoRec = 0.00 " + "ORDER BY DataVencimentoRec, ParcelaRec");

			st.setInt(1, cod);
			rs = st.executeQuery();

			List<Receber> list = new ArrayList<>();

			while (rs.next()) {
				ParPeriodo per = instantiateParPeriodo(rs);
				Receber obj = instantiateReceber(rs, per);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Receber> findByIdClientePago(Integer cod) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(

					"SELECT *, parPeriodo.* " 
						+ "FROM receber " 
							+ "INNER JOIN parPeriodo "
								+ "ON receber.PeriodoIdRec = parPeriodo.IdPeriodo "
									+ "WHERE ClienteRec = ? AND ValorPagoRec > 0.00 "
							+ "ORDER BY DataVencimentoRec, ParcelaRec");

			st.setInt(1, cod);
			rs = st.executeQuery();

			List<Receber> list = new ArrayList<>();

			while (rs.next()) {
				ParPeriodo per = instantiateParPeriodo(rs);
				Receber obj = instantiateReceber(rs, per);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
	
	@Override
	public List<Receber> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(

					"SELECT *, parPeriodo.* " + 
							"FROM receber " + 
								"INNER JOIN parPeriodo " +
									"ON receber.PeriodoIdRec = parPeriodo.IdPeriodo " +
								"ORDER BY DataVencimentoRec, ParcelaRec");

			rs = st.executeQuery();

			List<Receber> list = new ArrayList<>();

			while (rs.next()) {
				ParPeriodo per = instantiateParPeriodo(rs);
				Receber obj = instantiateReceber(rs, per);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Receber> findAllId() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(

					"SELECT *, parPeriodo.* " + 
							"FROM receber " + 
								"INNER JOIN parPeriodo " +
									"ON receber.PeriodoIdRec = parPeriodo.IdPeriodo " +
								"ORDER BY NumeroRec");

			rs = st.executeQuery();

			List<Receber> list = new ArrayList<>();

			while (rs.next()) {
				ParPeriodo per = instantiateParPeriodo(rs);
				Receber obj = instantiateReceber(rs, per);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
}
