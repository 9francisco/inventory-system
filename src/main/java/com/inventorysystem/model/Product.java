package com.inventorysystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a product in the inventory system.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
	private String name;
	private int totalQty;
	private int remainingQty;
	private String warehouse;
}
