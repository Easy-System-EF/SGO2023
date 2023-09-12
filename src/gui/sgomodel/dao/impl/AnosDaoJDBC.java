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
import gui.sgomodel.dao.AnosDao;
import gui.sgomodel.entities.Anos;
  
public class AnosDaoJDBC implements AnosDao {
	
// tb entra construtor p/ conex�o
	private Connection conn;
	
	public AnosDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Anos obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"Insert INTO anos " + 
						"AnoAnos, AnoStrAnos " +
							"VALUES " +
					 	"(?, ? ) ",

					Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, obj.getAnoAnos());
			st.setString(2, obj.getAnoStrAnos());
			st.executeUpdate();
			rs = st.getGeneratedKeys();

			while (rs.next()) {
				int cod = rs.getInt(1);
				obj.setNumeroAnos(cod);
			}			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
 			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public void update(Anos obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"UPDATE anos " + 
						"SET AnoAnos = ?, AnoStrAnos = ? " +
					 " WHERE (NumeroAnos = ? )",

					Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, obj.getAnoAnos());
			st.setString(2, obj.getAnoStrAnos());
			st.setInt(3, obj.getNumeroAnos());
			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
 			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Anos> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT * FROM anos " +
					"ORDER BY AnoAnos");
			
			rs = st.executeQuery();
			
			List<Anos> list = new ArrayList<>();
			
			while (rs.next())
			{	if (rs != null)
				{	Anos obj = instantiateAnos(rs);
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
	
	@Override
	public List<Anos> findAllId() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT * FROM anos " +
					"ORDER BY NumeroAnos");
			
			rs = st.executeQuery();
			
			List<Anos> list = new ArrayList<>();
			
			while (rs.next())
			{	if (rs != null)
				{	Anos obj = instantiateAnos(rs);
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
	
	@Override
	public Anos findAno(Integer ano) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT * " +
						"From Anos " +	
					"Where AnoAnos = ? " );
			
			st.setInt(1, ano);
			
			rs = st.executeQuery();
						
			if (rs.next()) {
				Anos obj = instantiateAnos(rs);
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
	
	private Anos instantiateAnos(ResultSet rs) throws SQLException {
 		Anos ano = new Anos();
 		ano.setNumeroAnos(rs.getInt("NumeroAnos"));
 		ano.setAnoAnos(rs.getInt("AnoAnos"));
 		ano.setAnoStrAnos(rs.getString("AnoStrAnos"));
        return ano;
	}
}
