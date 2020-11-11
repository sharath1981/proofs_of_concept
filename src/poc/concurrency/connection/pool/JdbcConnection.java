/**
 * 
 */
package poc.concurrency.connection.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Sharath Kumar B <sharath1981@gmail.com>
 *
 */
public enum JdbcConnection {
	POOL();

	private static final String POOL_SIZE = "POOL_SIZE";
	private static final String DRIVER_CLASS = "DRIVER_CLASS";
	private static final String DB_URL = "DB_URL";
	private static final String DB_USER = "DB_USER";
	private static final String DB_PASSWORD = "DB_PASSWORD";

	private final List<Connection> poolOfAvailableConnections;
	private final List<Connection> poolOfBusyConnections;
	private final int poolSize;

	private JdbcConnection() {
		poolSize = Integer.parseInt(System.getenv(POOL_SIZE));
		poolOfAvailableConnections = new ArrayList<Connection>(poolSize);
		poolOfBusyConnections = new ArrayList<Connection>(poolSize);
		registerDriver();
		initializePool();
	}

	private void initializePool() {
		IntStream.range(0, poolSize).boxed().map(e -> createConnection()).forEach(poolOfAvailableConnections::add);
	}

	private Connection createConnection() {
		try {
			return DriverManager.getConnection(System.getenv(DB_URL), System.getenv(DB_USER),
					System.getenv(DB_PASSWORD));
		} catch (SQLException e) {
			return null;
		}
	}

	private void registerDriver() {
		try {
			Class.forName(System.getenv(DRIVER_CLASS)).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public synchronized Connection get() {
		if (poolOfAvailableConnections.isEmpty()) {
			throw new RuntimeException("Connection Pool Underflow...");
		}
		final Connection connection = poolOfAvailableConnections.remove(0);
		poolOfBusyConnections.add(connection);
		return connection;
	}

	public synchronized void release(final Connection connection) {
		if (poolOfAvailableConnections.size() == poolSize) {
			throw new RuntimeException("Connection Pool Overflow...");
		}
		if (poolOfBusyConnections.remove(connection)) {
			poolOfAvailableConnections.add(connection);
		}
	}

	public synchronized void shutdown() {
		poolOfBusyConnections.forEach(this::closeConnection);
		poolOfAvailableConnections.forEach(this::closeConnection);
		poolOfBusyConnections.clear();
		poolOfAvailableConnections.clear();
	}

	private void closeConnection(final Connection connection) {
		try {
			if (!connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public synchronized int size() {
		return poolOfAvailableConnections.size();
	}

}
