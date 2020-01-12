/**
 * 
 */
package poc.concurrency;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author Sharath Kumar B <sharath1981@gmail.com>
 *
 */
public class ConnectionPoolManager {
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

interface ConnectionPool<T> {
	T getConnection();
	void freeConnection(T t);
	void shutdown();
}

final class JDBCConnectionPool implements ConnectionPool<Connection> {

	private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
	private final BlockingQueue<Connection> connectionPool;
	private final List<Connection> busyConnections;
	private final String url;
	private final String user;
	private final String password;
	private final int poolSize;

	public JDBCConnectionPool(final String url, final String user, final String password, final int poolSize) {
		connectionPool = new ArrayBlockingQueue<>(poolSize);
		busyConnections = new ArrayList<>(poolSize);
		this.url = url;
		this.user = user;
		this.password = password;
		this.poolSize = poolSize;
		registerDriver();
		populateConnectionPool();
	}

	private void registerDriver() {
		try {
			Class.forName(DRIVER_CLASS);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	private void populateConnectionPool() {
		IntStream.range(0, poolSize)
				 .boxed()
				 .map(i->createConnection())
				 .forEach(connectionPool::add);
	}

	private Connection createConnection() {
		try {
			return DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public synchronized Connection getConnection() {
		try {
			final Connection connection = connectionPool.poll(10, TimeUnit.SECONDS);
			busyConnections.add(connection);
			return connection;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public synchronized void freeConnection(final Connection connection) {
		try {
			busyConnections.remove(connection);
			connectionPool.put(connection);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void shutdown() {
		busyConnections.forEach(this::freeConnection);
		connectionPool.forEach(connection -> {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		busyConnections.clear();
		connectionPool.clear();
	}

}
