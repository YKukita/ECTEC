package jp.ac.osaka_u.ist.sdl.ectec.db.data.retriever;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import jp.ac.osaka_u.ist.sdl.ectec.db.DBConnectionManager;
import jp.ac.osaka_u.ist.sdl.ectec.db.data.AbstractDBElement;

/**
 * An abstract class to retrieve elements from db
 * 
 * @author k-hotta
 * 
 * @param <T>
 */
public abstract class AbstractElementRetriever<T extends AbstractDBElement> {

	/**
	 * the manager of the connection between db
	 */
	protected final DBConnectionManager dbManager;

	public AbstractElementRetriever(final DBConnectionManager dbManager) {
		this.dbManager = dbManager;
	}

	/**
	 * retrieve elements with the given query
	 * 
	 * @param query
	 * @return
	 * @throws SQLException
	 */
	public synchronized SortedMap<Long, T> retrieve(final String query)
			throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		SortedMap<Long, T> result = null;

		try {
			stmt = dbManager.createStatement();
			rs = stmt.executeQuery(query);

			result = instantiate(rs);
		} finally {
			if (stmt != null) {
				stmt.close();
			}

			if (rs != null) {
				rs.close();
			}
		}

		if (result != null) {
			return Collections.unmodifiableSortedMap(result);
		} else {
			return null;
		}
	}

	/**
	 * retrieve all elements stored into the db
	 * 
	 * @return
	 * @throws SQLException
	 */
	public synchronized SortedMap<Long, T> retrieveAll() throws SQLException {
		final String query = "select * from " + getTableName();
		return retrieve(query);
	}

	/**
	 * retrieve elements having one of the given id
	 * 
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	public synchronized SortedMap<Long, T> retrieveWithIds(
			final Collection<Long> ids) throws SQLException {
		if (ids.isEmpty()) {
			return new TreeMap<Long, T>();
		}
		final StringBuilder builder = new StringBuilder();
		builder.append("select * from " + getTableName() + " where "
				+ getIdColumnName() + " in (");

		for (final long id : ids) {
			builder.append(id + ",");
		}

		builder.deleteCharAt(builder.length() - 1);
		builder.append(")");

		return retrieve(builder.toString());
	}

	/**
	 * retrieve elements having one of the given id
	 * 
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	public synchronized SortedMap<Long, T> retrieveWithIds(long... ids)
			throws SQLException {
		final Set<Long> idSet = new HashSet<Long>();
		for (final long id : ids) {
			idSet.add(id);
		}

		return retrieveWithIds(idSet);
	}

	/**
	 * retrieve elements NOT having one of the given id
	 * 
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	public synchronized SortedMap<Long, T> retrieveWithoutIds(
			final Collection<Long> ids) throws SQLException {
		if (ids.isEmpty()) {
			return new TreeMap<Long, T>();
		}
		final StringBuilder builder = new StringBuilder();
		builder.append("select * from " + getTableName() + " where "
				+ getIdColumnName() + " not in (");

		for (final long id : ids) {
			builder.append(id + ",");
		}

		builder.deleteCharAt(builder.length() - 1);
		builder.append(")");

		return retrieve(builder.toString());
	}

	/**
	 * retrieve elements NOT having one of the given id
	 * 
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	public synchronized SortedMap<Long, T> retrieveWithoutIds(long... ids)
			throws SQLException {
		final Set<Long> idSet = new HashSet<Long>();
		for (final long id : ids) {
			idSet.add(id);
		}

		return retrieveWithoutIds(idSet);
	}

	/**
	 * get the maximum number of ID
	 * 
	 * @return
	 * @throws SQLException
	 */
	public synchronized long getMaximumId() throws SQLException {
		final String query = "select MAX(" + getIdColumnName() + ") from "
				+ getTableName();
		long result = 0;

		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = dbManager.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				result = rs.getLong(1);
				break;
			}
		} finally {
			stmt.close();
			rs.close();
		}

		return result;
	}

	/**
	 * instantiate the given result set
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public abstract SortedMap<Long, T> instantiate(final ResultSet rs)
			throws SQLException;

	/**
	 * get the name of the table
	 * 
	 * @return
	 */
	protected abstract String getTableName();

	/**
	 * get the name of the column having ids
	 * 
	 * @return
	 */
	protected abstract String getIdColumnName();

}
