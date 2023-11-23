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
import gui.sgomodel.dao.FechamentoAnoDao;
import gui.sgomodel.entities.FechamentoAno;
  
public class FechamentoAnoDaoJDBC implements FechamentoAnoDao {
	
// tb entra construtor p/ conex�o
	private Connection conn;
	
	public FechamentoAnoDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	String classe = "Fechamento Ano ";
	
	@Override
	public void insert(FechamentoAno obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO FechamentoAno " +
				      "(OsAnual, BalAnual, DataAnual, ClienteAnual, FuncionarioAnual, " +
                      "ValorOsAnual, ValorMaterialAnual, ValorComissaoAnual, ValorResultadoAnual, " +
				      "ValorAcumuladoAnual ) " +
  				      "VALUES " +
				      "(?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
 					 Statement.RETURN_GENERATED_KEYS); 

			st.setString(1, obj.getOsAnual());
			st.setString(2, obj.getBalAnual());
			st.setString(3, obj.getDataAnual());
			st.setString(4, obj.getClienteAnual());
			st.setString(5, obj.getFuncionarioAnual());
 			st.setString(6, obj.getValorOsAnual());
 			st.setString(7, obj.getValorMaterialAnual());
 			st.setString(8, obj.getValorComissaoAnual());
 			st.setString(9, obj.getValorResultadoAnual());
 			st.setString(10, obj.getValorAcumuladoAnual());
 			
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	int codigo = rs.getInt(1);
					obj.setNumeroAnual(codigo);
				}
				else
				{	throw new DbException("Erro!!! sem inclusão" );
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
	public List<FechamentoAno> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT * " +
						"From FechamentoAno " +	
					"ORDER BY NumeroAnual");
			
			rs = st.executeQuery();
			
			List<FechamentoAno> list = new ArrayList<>();
			
			while (rs.next()) {
				FechamentoAno obj = instantiateFechamento(rs);
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
					"TRUNCATE TABLE sgo.FechamentoAno " );

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
	
	private FechamentoAno instantiateFechamento(ResultSet rs) throws SQLException {
		FechamentoAno dados = new FechamentoAno();
 		dados.setNumeroAnual(rs.getInt("NumeroAnual"));
 		dados.setOsAnual(rs.getString("OsAnual"));
 		dados.setBalAnual(rs.getString("BalAnual"));
		dados.setDataAnual(rs.getString("DataAnual"));
		dados.setClienteAnual(rs.getString("ClienteAnual"));
		dados.setFuncionarioAnual(rs.getString("FuncionarioAnual"));
 		dados.setValorOsAnual(rs.getString("ValorOsAnual"));
 		dados.setValorMaterialAnual(rs.getString("ValorMaterialAnual"));
 		dados.setValorComissaoAnual(rs.getString("ValorComissaoAnual"));
 		dados.setValorResultadoAnual(rs.getString("ValorResultadoAnual"));
 		dados.setValorAcumuladoAnual(rs.getString("ValorAcumuladoAnual"));
        return dados;
	}
}
