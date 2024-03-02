package com.inventorysystem.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Utility class for managing database connections.
 */
public class DatabaseUtil {
	private static final String PROPERTIES_FILE = "/config.properties";
	private static Properties properties = new Properties();

	static {
		loadProperties();
		initializeDatabase();
	}

	private static void loadProperties() {
		try (InputStream input = DatabaseUtil.class.getResourceAsStream(PROPERTIES_FILE)) {
			properties.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("Error loading database properties.", ex);
		}
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(properties.getProperty("database.url"),
				properties.getProperty("database.user"), properties.getProperty("database.password"));
	}

	private static void initializeDatabase() {
		ensureTablesExist();
	}

	private static void ensureTablesExist() {
		try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

			// Create warehouses table
			String createWarehousesTable = "CREATE TABLE IF NOT EXISTS warehouses (" + "name VARCHAR(255) PRIMARY KEY,"
					+ "min_product INT," + "max_product INT);";
			stmt.execute(createWarehousesTable);

			// Create products table
			String createProductsTable = "CREATE TABLE IF NOT EXISTS products (" + "name VARCHAR(255),"
					+ "warehouse VARCHAR(255)," + "total_qty INT," + "remaining_qty INT,"
					+ "PRIMARY KEY (name, warehouse)," + "FOREIGN KEY (warehouse) REFERENCES warehouses(name));";
			stmt.execute(createProductsTable);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error initializing database tables.", e);
		}
	}
}
