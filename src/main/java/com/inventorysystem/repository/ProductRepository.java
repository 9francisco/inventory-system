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

import com.inventorysystem.model.Product;
import com.inventorysystem.util.DatabaseUtil;

/**
 * Repository for accessing product data from the database.
 */
public class ProductRepository {

	private static final Logger LOGGER = Logger.getLogger(ProductRepository.class.getName());

	/**
	 * Adds a new product to the database.
	 * 
	 * @param product
	 *            The product to add.
	 */
	public void addProduct(Product product) {
		String sql = "INSERT INTO products (name, total_qty, remaining_qty, warehouse) VALUES (?, ?, ?, ?)";
		try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, product.getName());
			stmt.setInt(2, product.getTotalQty());
			stmt.setInt(3, product.getRemainingQty());
			stmt.setString(4, product.getWarehouse());
			stmt.executeUpdate();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Exception occurred while adding a product", e);
		}
	}

	/**
	 * Updates an existing product in the database.
	 * 
	 * @param product
	 *            The product to update.
	 */
	public void updateProduct(Product product) {
		String sql = "UPDATE products SET total_qty = ?, remaining_qty = ?, warehouse = ? WHERE name = ?";
		try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, product.getTotalQty());
			stmt.setInt(2, product.getRemainingQty());
			stmt.setString(3, product.getWarehouse());
			stmt.setString(4, product.getName());
			stmt.executeUpdate();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Exception occurred while updating a product", e);
		}
	}

	/**
	 * Deletes a product by its name.
	 * 
	 * @param name
	 *            The name of the product to delete.
	 */
	public void deleteProduct(String name) {
		String sql = "DELETE FROM products WHERE name = ?";
		try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, name);
			stmt.executeUpdate();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Exception occurred while deleting a product", e);
		}
	}

	/**
	 * Retrieves all products from the database.
	 * 
	 * @return A list of products.
	 */
	public List<Product> getAllProducts() {
		List<Product> products = new ArrayList<>();
		String sql = "SELECT * FROM products";
		try (Connection conn = DatabaseUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				products.add(new Product(rs.getString("name"), rs.getInt("total_qty"), rs.getInt("remaining_qty"),
						rs.getString("warehouse")));
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Exception occurred while retrieving all products", e);
		}
		return products;
	}
}
