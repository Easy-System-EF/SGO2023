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
import gui.sgomodel.dao.MesesDao;
import gui.sgomodel.entities.Meses;
  
public class MesesDaoJDBC implements MesesDao {
	
// tb entra construtor p/ conex�o
	private Connection conn;
	
	public MesesDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Meses obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"Insert INTO meses " + 
						"(NomeMes ) " +
							"VALUES " +
					 	"(? ) ",
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getNomeMes());
			st.executeUpdate();
			rs = st.getGeneratedKeys();

			while (rs.next()) {
				int cod = rs.getInt(1);
				obj.setNumeroMes(cod);
			}			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Meses> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT * FROM meses " +
					"ORDER BY NumeroMes");
			
			rs = st.executeQuery();
			
			List<Meses> list = new ArrayList<>();
			
			while (rs.next())
			{	if (rs != null)
				{	Meses obj = instantiateMeses(rs);
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
	public Meses findId(Integer mes) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT * " +
						"From Meses " +	
					"Where NumeroMes = ? " );
			
			st.setInt(1, mes);
			rs = st.executeQuery();
						
			if (rs.next()) {
				Meses obj = instantiateMeses(rs);
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
	
	private Meses instantiateMeses(ResultSet rs) throws SQLException {
 		Meses mes = new Meses();
 		mes.setNumeroMes(rs.getInt("NumeroMes"));
 		mes.setNomeMes(rs.getString("NomeMes"));
        return mes;
	}
}
