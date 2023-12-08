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
import gui.sgomodel.dao.FechamentoAnualDao;
import gui.sgomodel.entities.FechamentoAnual;
  
public class FechamentoAnualDaoJDBC implements FechamentoAnualDao {
	
// tb entra construtor p/ conex�o
	private Connection conn;
	
	public FechamentoAnualDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	String classe = "Fechamento Ano ";
	
	@Override
	public void insert(FechamentoAnual obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO FechamentoAnual " +
				      "(MesAnual, DoctoAnual, ValorAnual, ValorCustoAnual, ValorComissaoAnual, ValorResultadoAnual, " +
				      "ValorAcumuladoAnual ) " +
  				      "VALUES " +
				      "(?, ?, ?, ?, ?, ?, ? )",
 					 Statement.RETURN_GENERATED_KEYS); 

			st.setString(1, obj.getMesAnual());
			st.setString(2, obj.getDoctoAnual());
 			st.setString(3, obj.getValorAnual());
 			st.setString(4, obj.getValorCustoAnual());
 			st.setString(5, obj.getValorComissaoAnual());
 			st.setString(6, obj.getValorResultadoAnual());
 			st.setString(7, obj.getValorAcumuladoAnual());
 			
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
	public List<FechamentoAnual> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT * " +
						"From FechamentoAnual " +	
					"ORDER BY NumeroAnual");
			
			rs = st.executeQuery();
			
			List<FechamentoAnual> list = new ArrayList<>();
			
			while (rs.next()) {
				FechamentoAnual obj = instantiateFechamento(rs);
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
					"TRUNCATE TABLE FechamentoAnual " );

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
	
	private FechamentoAnual instantiateFechamento(ResultSet rs) throws SQLException {
		FechamentoAnual dados = new FechamentoAnual();
 		dados.setNumeroAnual(rs.getInt("NumeroAnual"));
 		dados.setMesAnual(rs.getString("MesAnual"));
 		dados.setDoctoAnual(rs.getString("DoctoAnual"));
 		dados.setValorAnual(rs.getString("ValorAnual"));
 		dados.setValorCustoAnual(rs.getString("ValorCustoAnual"));
 		dados.setValorComissaoAnual(rs.getString("ValorComissaoAnual"));
 		dados.setValorResultadoAnual(rs.getString("ValorResultadoAnual"));
 		dados.setValorAcumuladoAnual(rs.getString("ValorAcumuladoAnual"));
        return dados;
	}
}
