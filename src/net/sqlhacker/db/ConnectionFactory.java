package net.sqlhacker.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class ConnectionFactory {

    private static Map<String, List<Connection>> connections = Collections
	    .synchronizedMap(new HashMap<String, List<Connection>>());

    private final JSONObject connectionDetails;

    public ConnectionFactory(JSONObject connectionDetails) {
	this.connectionDetails = connectionDetails;
	try {
	    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    public Connection getConnection() {
	String key = getConnectionKey();
	Connection conn = getConnection(key);
	return conn;

    }

    private Connection getConnection(String key) {
	List<Connection> interestingConnections = connections.get(key);
	if (interestingConnections == null) {
	    interestingConnections = new ArrayList<Connection>();
	    connections.put(key, interestingConnections);
	}
	Connection c;
	if (interestingConnections.isEmpty()) {
	    c = buildNewConnection();
	} else {
	    c = interestingConnections.remove(0);
	}
	return c;
    }

    private Connection buildNewConnection() {
	Connection conn;
	try {
	    conn = DriverManager.getConnection(connectionDetails
		    .getString("JDBC_URL"), connectionDetails
		    .getString("DB_USER"), connectionDetails
		    .getString("DB_PASS"));
	} catch (SQLException e) {
	    throw new RuntimeException("Unable to connect ", e);
	} catch (JSONException e) {
	    throw new RuntimeException("Unable to load db connection details",
		    e);
	}
	return conn;
    }

    public void release(Connection conn) {
	String key = getConnectionKey();

	List<Connection> interestingConnections = connections.get(key);
	interestingConnections.add(conn);

    }

    private String getConnectionKey() {
	String key = connectionDetails.toString();
	return key;
    }
}
