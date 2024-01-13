package gui.sgomodel.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import gui.sgomodel.dao.FechamentoMesDao;
import gui.sgomodel.entities.Anos;
import gui.sgomodel.entities.FechamentoMes;
import gui.sgomodel.entities.Meses;
  
public class FechamentoMesDaoJDBC implements FechamentoMesDao {
	
// tb entra construtor p/ conex�o
	private Connection conn;
	
	public FechamentoMesDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	String classe = "Fechamento Mes ";
	
	@Override
	public void insert(FechamentoMes obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO FechamentoMes " +
				      "(OsMensal, BalMensal, DataMensal, ClienteMensal, FuncionarioMensal, " +
                      "ValorOsMensal, ValorMaterialMensal, ValorComissaoMensal, ValorResultadoMensal, " +
				      "ValorAcumuladoMensal, MesesIdMensal, AnosIdMensal ) " +
  				      "VALUES " +
				      "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
 					 Statement.RETURN_GENERATED_KEYS); 

			st.setString(1, obj.getOsMensal());
			st.setString(2, obj.getBalMensal());
			st.setString(3, obj.getDataMensal());
			st.setString(4, obj.getClienteMensal());
			st.setString(5, obj.getFuncionarioMensal());
 			st.setString(6, obj.getValorOsMensal());
 			st.setString(7, obj.getValorMaterialMensal());
 			st.setString(8, obj.getValorComissaoMensal());
 			st.setString(9, obj.getValorResultadoMensal());
 			st.setString(10, obj.getValorAcumuladoMensal());
 			st.setInt(11, obj.getMes().getNumeroMes());
 			st.setInt(12, obj.getAno().getNumeroAnos());
 			
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	int codigo = rs.getInt(1);
					obj.setNumeroMensal(codigo);
				}
				else
				{	throw new DbException(classe + " Erro!!! sem inclusão" );
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
	public List<FechamentoMes> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT *, meses.*, anos.* " +
						"From FechamentoMes " +	
							"INNER JOIN meses " +
								"ON fechamentoMes.MesesIdMensal = meses.NumeroMes " + 
							"INNER JOIN anos " +
								"ON fechamentoMes.AnosIdMensal = anos.NumeroAnos " + 
					"ORDER BY NumeroMensal");
			
			rs = st.executeQuery();
			
			List<FechamentoMes> list = new ArrayList<>();
			Map<Integer, Meses> mapMes = new HashMap<>();
			Map<Integer, Anos> mapAno = new HashMap<>();
			
			while (rs.next()) {
				Meses objMes = mapMes.get(rs.getInt("MesesIdMensal"));
				if (objMes == null) {
					objMes = instantiateMeses(rs);
					mapMes.put(rs.getInt("MesesIdMensal"), objMes);
				}	
				Anos objAno = mapAno.get(rs.getInt("AnosIdMensal"));
				if (objAno == null) {
					objAno = instantiateAnos(rs);
					mapAno.put(rs.getInt("AnosIdMensal"), objAno);
				}	
				FechamentoMes obj = instantiateFechamento(rs, objMes, objAno);
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
					"TRUNCATE TABLE sgo.FechamentoMes " );

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
	
	private FechamentoMes instantiateFechamento(ResultSet rs, Meses mes, Anos ano) throws SQLException {
		FechamentoMes dados = new FechamentoMes();
 		dados.setNumeroMensal(rs.getInt("NumeroMensal"));
 		dados.setOsMensal(rs.getString("OsMensal"));
 		dados.setBalMensal(rs.getString("BalMensal"));
		dados.setDataMensal(rs.getString("DataMensal"));
		dados.setClienteMensal(rs.getString("ClienteMensal"));
		dados.setFuncionarioMensal(rs.getString("FuncionarioMensal"));
 		dados.setValorOsMensal(rs.getString("ValorOsMensal"));
 		dados.setValorMaterialMensal(rs.getString("ValorMaterialMensal"));
 		dados.setValorComissaoMensal(rs.getString("ValorComissaoMensal"));
 		dados.setValorResultadoMensal(rs.getString("ValorResultadoMensal"));
 		dados.setValorAcumuladoMensal(rs.getString("ValorAcumuladoMensal"));
 		dados.setMes(mes);
 		dados.setAno(ano);
        return dados;
	}

	private Meses instantiateMeses(ResultSet rs) throws SQLException {
		Meses meses = new Meses();
		meses.setNumeroMes(rs.getInt("NumeroMes"));
		meses.setNomeMes(rs.getString("NomeMes"));
		return meses;
	}

	
	private Anos instantiateAnos(ResultSet rs) throws SQLException {
		Anos anos = new Anos();
		anos.setNumeroAnos(rs.getInt("NumeroAnos"));
		anos.setAnoAnos(rs.getInt("AnoAnos"));
		return anos;
	} 	
}
