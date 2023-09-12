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
import gui.sgomodel.dao.NotaFiscalDao;
import gui.sgomodel.entities.NotaFiscal;
  
public class NotaFiscalJDBC implements NotaFiscalDao {
	
// tb entra construtor p/ conex�o
	private Connection conn;
	
	public NotaFiscalJDBC (Connection conn) {
		this.conn = conn;
	}

	String classe = "NotaFiscal ";
	
	@Override
	public void insert(NotaFiscal obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO notaFiscal " +
				      "(NumeroNF, BalcaoNF, OsNF )" + 
  				      "VALUES " +
				      "(?, ?, ? )",
 					 Statement.RETURN_GENERATED_KEYS); 
 
 			st.setInt(1, obj.getNumeroNF());
 			st.setInt(2, obj.getBalcaoNF());
 			st.setInt(3, obj.getOsNF());
			 
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	int codigo = rs.getInt(1);
					obj.setCodigoNF(codigo);;
				}
				else
				{	throw new DbException(classe + "Erro!!! sem inclus�o" );
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
	public void insertBackUp(NotaFiscal obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO notaFiscal " +
				      "(CodigoNf, NumeroNF, BalcaoNF, OsNF )" + 
  				      "VALUES " +
				      "(?, ?, ?, ? )"); 
 
 			st.setInt(1, obj.getCodigoNF());
 			st.setInt(2, obj.getNumeroNF());
 			st.setInt(3, obj.getBalcaoNF());
 			st.setInt(4, obj.getOsNF());
			 
 			st.executeUpdate();
  		}
 		catch (SQLException e) {
			throw new DbException("Erro!!! " + classe + " sem inclusão" + e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
 
	@Override
	public void deleteById(Integer nf) {
		PreparedStatement st = null;
  		try {
			st = conn.prepareStatement(
					"DELETE FROM notaFiscal WHERE CodigoNF = ? ", Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, nf);
			st.executeUpdate();
   		}
 		catch (SQLException e) {
			throw new DbException (classe + "Erro!!! na� exclu�do " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public void zeraAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"TRUNCATE TABLE sgb.NotaFiscal " );

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
	
	@Override
	public List<NotaFiscal> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT * FROM notaFiscal " +
					"ORDER BY - CodigoNF ");
			
			rs = st.executeQuery();
			
			List<NotaFiscal> list = new ArrayList<>();
			
			while (rs.next())
			{	if (rs != null)
				{	NotaFiscal obj = instantiateNotaFiscals(rs);
 					list.add(obj);
				}	
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
	
	private NotaFiscal instantiateNotaFiscals(ResultSet rs) throws SQLException {
 		NotaFiscal notaFiscal = new NotaFiscal();
 		
 		notaFiscal.setCodigoNF(rs.getInt("CodigoNF"));
 		notaFiscal.setNumeroNF(rs.getInt("NumeroNF"));
 		notaFiscal.setBalcaoNF(rs.getInt("BalcaoNF"));
 		notaFiscal.setOsNF(rs.getInt("OsNF"));
        return notaFiscal;
	}
}
