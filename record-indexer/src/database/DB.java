package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class DB {
	public static Connection connection = null;
	
	public static Connection getConnection() {
		if (connection == null) {
			try {
		        Class.forName("org.sqlite.JDBC");
				connection = DriverManager.getConnection("jdbc:sqlite:indexer_server.sqlite");
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		return connection;
	}
	
	public static int run(String sql, String[] values) {
		getConnection();
		try {
			PreparedStatement s = connection.prepareStatement(sql);
			for (int i = 0; i < values.length; i++) {
				s.setString(i+1, values[i]);
			}

			return s.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static int run(String sql) {
		String[] values = {};
		return run(sql, values);
	}
	
	public static List<Map<String, String>> get(String sql, String[] values) {
		getConnection();
		try {
			PreparedStatement s = connection.prepareStatement(sql);
			for (int i = 0; i < values.length; i++) {
				s.setString(i+1, values[i]);
			}
			
			ResultSet rs = s.executeQuery();
			ResultSetMetaData md = rs.getMetaData();
			int columns = md.getColumnCount();
			ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
			while (rs.next()){
				Map<String, String> row = new HashMap<String, String>(columns);
				for (int i = 1; i <= columns; ++i) {
					row.put(md.getColumnName(i), rs.getString(i));
				}
				list.add(row);
			}

			rs.close();
			return list;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}	
	
	public static List<Map<String, String>> get(String sql) {
		String[] values = {};
		return get(sql, values);
	}
	
	public static List<Map<String, String>> get(String sql, String param) {
		String[] values = {param};
		return get(sql, values);
	}
	
	public static List<Map<String, String>> get(String sql, Integer param) {
		return get(sql, param.toString());
	}
	
	public static int insert(String table, Map<String, String> properties) {
		String columns = "";
		String placeHolders = "";
		String[] values = new String[properties.size()];
		
		int i = 0;
		for (Entry<String, String> entry : properties.entrySet()) {
			if (!columns.equals("")) columns += ", ";
			if (!placeHolders.equals("")) placeHolders += ", ";
			columns += entry.getKey();
			placeHolders += "?";
			values[i++] = entry.getValue();
		}
		
		String sql = "INSERT INTO `" + table + "` (" + columns + ") VALUES (" + placeHolders + ")";
		
		run(sql, values);
		return Integer.parseInt(get("SELECT last_insert_rowid() as id").get(0).get("id"));
	}
	
	public static int update(String table, Map<String, String> properties) {
		String updates = "";
		String[] values = new String[properties.size()];
		
		int i = 0;
		for (Entry<String, String> entry : properties.entrySet()) {
			if (entry.getKey().equals("rowid")) continue;
			if (!updates.equals("")) updates += ", ";
			updates += entry.getKey() + " = ?";
			values[i++] = entry.getValue();
		}
		values[i++] = properties.get("rowid");
		
		String sql = "UPDATE " + table + " SET " + updates + " WHERE rowid = ?";
		return run(sql, values);
	}
	
	public static int insertOrReplace(String table, Map<String, String> properties) {
		
		if (properties.containsKey("rowid")) {
			return update(table, properties);
		} else {
			int id = insert(table, properties);
			properties.put("rowid", String.valueOf(id));
			return id;
		}
		
	}
	
}
