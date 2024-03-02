package com.inventorysystem.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.inventorysystem.model.Warehouse;
import com.inventorysystem.util.DatabaseUtil;

/**
 * Repository for accessing warehouse data.
 */
public class WarehouseRepository {

	private static final Logger LOGGER = Logger.getLogger(ProductRepository.class.getName());

	/**
	 * Adds a new warehouse to the database.
	 * 
	 * @param warehouse
	 *            The warehouse to add.
	 */
	public void addWarehouse(Warehouse warehouse) {
		try (Connection conn = DatabaseUtil.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"INSERT INTO warehouses (name, min_product, max_product) VALUES (?, ?, ?)")) {
			stmt.setString(1, warehouse.getName());
			stmt.setInt(2, warehouse.getMinProduct());
			stmt.setInt(3, warehouse.getMaxProduct());
			stmt.executeUpdate();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Exception occurred while adding a warehouse", e);
		}
	}

	/**
	 * Updates an existing warehouse in the database.
	 * 
	 * @param warehouse
	 *            The warehouse to update.
	 */
	public void updateWarehouse(Warehouse warehouse) {
		try (Connection conn = DatabaseUtil.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"UPDATE warehouses SET min_product = ?, max_product = ? WHERE name = ?")) {
			stmt.setInt(1, warehouse.getMinProduct());
			stmt.setInt(2, warehouse.getMaxProduct());
			stmt.setString(3, warehouse.getName());
			stmt.executeUpdate();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Exception occurred while updating a warehouse", e);
		}
	}

	/**
	 * Deletes a warehouse by its name.
	 * 
	 * @param name
	 *            The name of the warehouse to delete.
	 */
	public void deleteWarehouse(String name) {
		String sql = "DELETE FROM warehouses WHERE name = ?;";

		try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, name);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Exception occurred while deleting a warehouse", e);
		}
	}

	/**
	 * Retrieves all warehouses from the database.
	 * 
	 * @return A list of warehouses.
	 */
	public List<Warehouse> getAllWarehouses() {
		List<Warehouse> warehouses = new ArrayList<>();
		try (Connection conn = DatabaseUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM warehouses")) {
			while (rs.next()) {
				warehouses.add(new Warehouse(rs.getString("name"), rs.getInt("min_product"), rs.getInt("max_product")));
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Exception occurred while retrieving all warehouses", e);
		}
		return warehouses;
	}

	/**
	 * Retrieves a warehouse by its name.
	 * 
	 * @param name
	 *            The name of the warehouse to retrieve.
	 * @return The Warehouse object if found, null otherwise.
	 */
	public Warehouse getWarehouseByName(String name) {
		String sql = "SELECT * FROM warehouses WHERE name = ?";
		try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, name);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return new Warehouse(rs.getString("name"), rs.getInt("min_product"), rs.getInt("max_product"));
				}
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Exception occurred while retrieving warehouse by name", e);
		}
		return null;
	}

}
