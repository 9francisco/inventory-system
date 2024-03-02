package com.inventorysystem.controller;

import java.util.List;

import com.inventorysystem.model.Product;
import com.inventorysystem.repository.ProductRepository;

/**
 * Controller for managing product operations.
 */
public class ProductController {
	private ProductRepository repository;

	public ProductController() {
		this.repository = new ProductRepository();
	}

	/**
	 * Adds a new product.
	 * 
	 * @param product
	 *            Product to be added.
	 */
	public void addProduct(Product product) {
		repository.addProduct(product);
	}

	/**
	 * Updates an existing product.
	 * 
	 * @param product
	 *            Product to be updated.
	 */
	public void updateProduct(Product product) {
		repository.updateProduct(product);
	}

	/**
	 * Deletes a product by name.
	 * 
	 * @param name
	 *            name of the product to be deleted.
	 */
	public void deleteProduct(String name) {
		repository.deleteProduct(name);
	}

	/**
	 * Retrieves a product by its name.
	 * 
	 * @return The Product object if found, null otherwise.
	 */
	public List<Product> getAllProducts() {
		return repository.getAllProducts();
	}
}
