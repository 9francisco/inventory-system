package com.inventorysystem.controller;

import java.util.List;

import com.inventorysystem.model.Warehouse;
import com.inventorysystem.repository.WarehouseRepository;

/**
 * Controller for managing warehouse operations.
 */
public class WarehouseController {
	private WarehouseRepository repository;

	public WarehouseController() {
		this.repository = new WarehouseRepository();
	}

	/**
	 * Adds a new warehouse.
	 * 
	 * @param warehouse
	 *            Warehouse to be added.
	 */
	public void addWarehouse(Warehouse warehouse) {
		repository.addWarehouse(warehouse);
	}

	/**
	 * Updates an existing warehouse.
	 * 
	 * @param warehouse
	 *            Warehouse to be updated.
	 */
	public void updateWarehouse(Warehouse warehouse) {
		repository.updateWarehouse(warehouse);
	}

	/**
	 * Deletes a warehouse by name.
	 * 
	 * @param name
	 *            name of the warehouse to be deleted.
	 */
	public void deleteWarehouse(String name) {
		repository.deleteWarehouse(name);
	}

	/**
	 * Retrieves all warehouses.
	 * 
	 * @return A list of warehouses.
	 */
	public List<Warehouse> getAllWarehouses() {
		return repository.getAllWarehouses();
	}

	/**
	 * Retrieves a warehouse by its name.
	 * 
	 * @param name
	 *            The name of the warehouse to retrieve.
	 * @return The Warehouse object if found, null otherwise.
	 */
	public Warehouse getWarehouseByName(String name) {
		return repository.getWarehouseByName(name);
	}
}
