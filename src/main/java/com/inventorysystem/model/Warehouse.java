package com.inventorysystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a warehouse in the inventory system.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Warehouse {
	private String name;
	private int minProduct;
	private int maxProduct;
}
