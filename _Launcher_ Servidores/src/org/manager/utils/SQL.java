package org.manager.utils;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQL {

	// strings de conexao
	private final String driverName = "com.mysql.jdbc.Driver";
	// ATENCAO. ESTA DIRECIONADO JA PARA O BANCO NOVO!

	// log
	private Logger log = Logger.getLogger(SQL.class.getName());

	// conexao
	private Connection con = null;

	// private Statement stmt = null;
	private ResultSet resultSet = null;
	@SuppressWarnings("unused")
	private int results = 0;

	// construtor
	public SQL() {
		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException ex) {
			log.log(Level.SEVERE, "Erro ao encontrar o driver", ex.getMessage());
		}
	}

	/********************************/
	/***** CONNECTIONS METHODS ******/
	/********************************/
	/**
	 * Metodo que cria a conexao
	 * 
	 * @return Objeto Connection
	 */
	public Connection createConnection() {
		// try {
		// this.con = DriverManager.getConnection(getThing("db"), getThing("username"),
		// getThing("password"));
		// } catch (SQLException e) {
		// log.log(Level.SEVERE, "Erro criar conexao", e.getMessage());
		// }
		// return this.con;
		try {
			return this.con = ConnectionFactory.newConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.con = null;

	}

	/**
	 * Metodo que fecha a conexao
	 */
	public void closeConnetion() {
		try {
			this.con.close();
		} catch (SQLException e) {
			log.log(Level.SEVERE, "Erro fechar conexao", e.getMessage());
		}
	}

	/**
	 * Metodo que fecha o result set e zera o contador de retorno
	 */
	public void closeResultSet() {
		try {
			if (this.resultSet != null) {
				this.resultSet.close();
				this.results = 0;
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, "Erro fechar result set", e.getMessage());
		}
	}

	/**
	 * Metodo que prepara o statement setando os parametros na query
	 * 
	 * @param String
	 *            query
	 * @param String[]
	 *            parametross
	 * @return o PreparedStatement
	 */
	public PreparedStatement prepare(String query, String params[]) {
		try {
			PreparedStatement stat = createConnection().prepareStatement(query);

			for (int x = 0; x < params.length; x++)
				stat.setString((x + 1), params[x]);

			return stat;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/********************************/
	/******* GENERIC QUERYS *********/
	/********************************/
	/*
	 * Metodo que verifica se uma query tem resultado ou nao.
	 * 
	 * @param String query
	 * 
	 * @param String[] parametros
	 * 
	 * @return boolean true se tiver resultado
	 */
	public boolean check(String query, String... params) {
		try {
			this.resultSet = this.prepare(query, params).executeQuery();

			if (this.resultSet.next())
				return true;
			else
				return false;
		} catch (SQLException e) {
			log.log(Level.SEVERE, "Erro ao tentar criar statement: " + e.getClass() + "\n Cause: " + e.getCause(),
					e.getMessage());
			return false;
		} finally {
			closeConnetion();
			closeResultSet();
		}
	}

	/**
	 * Metodo que verifica se uma query tem resultado ou nao.
	 * 
	 * @param String
	 *            query
	 * @return boolean true se tiver resultado
	 */
	public boolean check(String query) {
		try {
			PreparedStatement stat = createConnection().prepareStatement(query);
			ResultSet result = stat.executeQuery(query);
			if (result.next())
				return true;
			else
				return false;
		} catch (SQLException e) {
			log.log(Level.SEVERE, "Erro ao tentar criar statement: " + e.getClass(), e.getMessage());
			return false;
		} finally {
			closeConnetion();
			closeResultSet();
		}
	}

	/**
	 * Metodo que faz query de insert
	 * 
	 * @param String
	 *            query
	 * @param String[]
	 *            params
	 * @return boolean true se tudo ocorrer corretamente
	 */
	public boolean insert(String query, String[] params) {
		try {
			this.prepare(query, params).execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			closeConnetion();
		}
	}

	public String selectSingleResultFromColumn(String query, String[] params, String column) {
		try {
			this.resultSet = this.prepare(query, params).executeQuery();
			if (this.resultSet.next())
				return this.resultSet.getString(column);
			else
				return "";

		} catch (SQLException e) {
			log.log(Level.SEVERE, "Erro ao fazer select de string: " + e.getClass() + "\n " + e.getCause(),
					e.getMessage());
			return "";
		} finally {
			closeConnetion();
			closeResultSet();
		}
	}

	/**
	 * Metodo que faz uma busca passando parametros de busca retorna uma lista de
	 * resultados
	 * 
	 * @param String
	 *            query
	 * @param String[]
	 *            parametros
	 * @param String
	 *            column
	 * @return List<String> results
	 */
	public ResultSet selecList(String query, String[] params) {
		try {

			this.resultSet = this.prepare(query, params).executeQuery();

			if (this.resultSet.getFetchSize() > 0) {

				return this.resultSet;
			} else
				return null;

		} catch (SQLException e) {
			log.log(Level.SEVERE, "Erro ao fazer select de string: " + e.getClass() + "\n " + e.getCause(),
					e.getMessage());
			return null;
		} finally {
			closeConnetion();
			closeResultSet();
		}
	}

	/**
	 * Metodo que faz uma busca passando parametros de busca retorna uma lista de
	 * resultados
	 * 
	 * @param String
	 *            query
	 * @param String[]
	 *            parametros
	 * @param String
	 *            column
	 * @return List<String> results
	 */
	public List<String> selectListFromColumn(String query, String[] params, String column) {
		try {
			List<String> results = new ArrayList<>();
			this.resultSet = this.prepare(query, params).executeQuery();

			if (this.resultSet.getFetchSize() > 0) {
				while (this.resultSet.next())
					results.add(this.resultSet.getString(column));

				return results;
			} else
				return null;

		} catch (SQLException e) {
			log.log(Level.SEVERE, "Erro ao fazer select de string: " + e.getClass() + "\n " + e.getCause(),
					e.getMessage());
			return null;
		} finally {
			closeConnetion();
			closeResultSet();
		}
	}

	/**
	 * Metodo que faz uma busca do tipo FIND ALL
	 * 
	 * @param String
	 *            query
	 * @param String
	 *            coluna que vai retornar o resultado
	 * @return List<String>
	 */
	public List<String> selectListFromColumn(String query, String column) {
		try {
			List<String> results = new ArrayList<>();
			PreparedStatement stat = createConnection().prepareStatement(query);

			this.resultSet = stat.executeQuery();

			if (this.resultSet != null) {
				while (this.resultSet.next())
					results.add(this.resultSet.getString(column));

				return results;
			} else
				return null;

		} catch (SQLException e) {
			log.log(Level.SEVERE, "Erro ao fazer select de string: " + e.getClass() + "\n " + e.getCause(),
					e.getMessage());
			return null;
		} finally {
			closeConnetion();
			closeResultSet();
		}
	}

	/**
	 * Metodo que faz uma busca do tipo FIND ALL
	 * 
	 * @param String
	 *            query
	 * @param String
	 *            coluna que vai retornar o resultado
	 * @return List<Integer>
	 */
	public List<Integer> selectListIntFromColumn(String query, String column) {
		try {
			List<Integer> results = new ArrayList<>();
			PreparedStatement stat = createConnection().prepareStatement(query);

			this.resultSet = stat.executeQuery();

			if (this.resultSet != null) {
				while (this.resultSet.next())
					results.add(this.resultSet.getInt(column));

				return results;
			} else
				return null;

		} catch (SQLException e) {
			log.log(Level.SEVERE, "Erro ao fazer select de string: " + e.getClass() + "\n " + e.getCause(),
					e.getMessage());
			return null;
		} finally {
			closeConnetion();
			closeResultSet();
		}
	}

	/**
	 * Metodo que faz uma busca passando parametros de busca retorna uma lista de
	 * resultados
	 * 
	 * @param String
	 *            query
	 * @param String[]
	 *            parametros
	 * @param String
	 *            column
	 * @return List<Integer> results
	 */
	public List<Integer> selectIntListFromColumn(String query, String[] params, String column) {
		try {
			List<Integer> results = new ArrayList<>();
			this.resultSet = this.prepare(query, params).executeQuery();

			if (this.resultSet.getFetchSize() > 0) {
				while (this.resultSet.next())
					results.add(this.resultSet.getInt(column));

				return results;
			} else
				return null;

		} catch (SQLException e) {
			log.log(Level.SEVERE, "Erro ao fazer select de integer: " + e.getClass() + "\n " + e.getCause(),
					e.getMessage());
			return null;
		} finally {
			closeConnetion();
			closeResultSet();
		}
	}

	/**
	 * Metodo que faz update em qualquer tabela
	 * 
	 * @param String
	 *            tabela
	 * @param String[]
	 *            campos
	 * @param String[]
	 *            valores dos campos
	 * @param String
	 *            campoCondicao
	 * @param String
	 *            condicaoValue
	 * @return boolean true se tudo ocorrer corretamente
	 */
	public boolean updateDB(String tabela, String[] campos, String[] values, String campoCondicao,
			String condicaoValue) {
		String query = "UPDATE " + tabela + " SET ";

		for (int x = 0; x < campos.length; x++) {
			if (x > 0)
				query += " , ";

			query += campos[x] + " = '" + values[x] + "'";
		}

		query += " WHERE " + campoCondicao + " = '" + condicaoValue + "'";

		try {
			PreparedStatement stat = createConnection().prepareStatement(query);
			stat.execute();
			return true;
		} catch (SQLException e) {
			log.log(Level.SEVERE, "Erro ao fazer update de tabela: " + e.getClass() + "\n " + e.getCause(),
					e.getMessage());
			return false;
		} finally {
			closeConnetion();
		}
	}
}
