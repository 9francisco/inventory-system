package com.inventorysystem.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;

import com.inventorysystem.controller.ProductController;
import com.inventorysystem.controller.WarehouseController;
import com.inventorysystem.model.Product;
import com.inventorysystem.model.Warehouse;

/**
 * Panel for managing products in the inventory system.
 */
public class ProductPanel extends JPanel {
	private ProductController productController;
	private WarehouseController warehouseController;

	private JTable table;
	private JTextField nameField;
	private JFormattedTextField totalQtyField, remainingQtyField;
	private JComboBox<String> warehouseField;
	private JButton addButton, updateButton, deleteButton, resetButton;

	public ProductPanel() {
		productController = new ProductController();
		warehouseController = new WarehouseController();

		initializeUI();
		loadProducts();
	}

	private void initializeUI() {
		setLayout(new BorderLayout());

		// Form Panel
		JPanel formPanel = new JPanel(new GridLayout(0, 2));
		nameField = new JTextField();
		NumberFormat integerFormat = NumberFormat.getIntegerInstance();
		integerFormat.setGroupingUsed(false);
		NumberFormatter formatter = new NumberFormatter(integerFormat);
		formatter.setValueClass(Integer.class);
		totalQtyField = new JFormattedTextField(formatter);
		remainingQtyField = new JFormattedTextField(formatter);
		List<String> warehouseNames = getWarehouseNames();
		warehouseNames.add(0, "");
		warehouseField = new JComboBox<>(warehouseNames.toArray(new String[0]));
		formPanel.add(new JLabel("Name:"));
		formPanel.add(nameField);
		formPanel.add(new JLabel("Total Quantity:"));
		formPanel.add(totalQtyField);
		formPanel.add(new JLabel("Remaining Quantity:"));
		formPanel.add(remainingQtyField);
		formPanel.add(new JLabel("Warehouse:"));
		formPanel.add(warehouseField);

		// Buttons Panel
		JPanel buttonPanel = new JPanel();
		addButton = new JButton("Add");
		updateButton = new JButton("Update");
		deleteButton = new JButton("Delete");
		resetButton = new JButton("Reset");

		buttonPanel.add(addButton);
		buttonPanel.add(updateButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(resetButton);

		updateButton.setEnabled(false); // Initially disabled
		deleteButton.setEnabled(false); // Initially disabled

		// Table Panel
		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[] { "Name", "Total Qty", "Remaining Qty", "Warehouse" }, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});

		JScrollPane scrollPane = new JScrollPane(table);

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Custom ListSelectionListener to handle row selection toggle
		table	.getSelectionModel()
				.addListSelectionListener((ListSelectionEvent e) -> {
					if (!e.getValueIsAdjusting()) {
						if (table.getSelectedRow() != -1) {
							updateNameFieldBasedOnSelection(true);
						} else {
							updateNameFieldBasedOnSelection(false);
						}
					}
				});

		// MouseAdapter to handle click and toggle selection
		table.addMouseListener(new MouseAdapter() {
			private int lastSelectedRow = -1;

			@Override
			public void mouseClicked(MouseEvent e) {
				int currentRow = table.rowAtPoint(e.getPoint());
				if (currentRow == lastSelectedRow) {
					table.clearSelection();
					lastSelectedRow = -1;
				} else {
					lastSelectedRow = currentRow;
				}
			}
		});

		// Add components to main panel
		add(formPanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

		// Add action listeners
		addButton.addActionListener(this::addProduct);
		updateButton.addActionListener(this::updateProduct);
		deleteButton.addActionListener(this::deleteProduct);
		resetButton.addActionListener(e -> {
			clearFields();
			table.clearSelection();
		});
	}

	private void updateNameFieldBasedOnSelection(boolean isSelected) {
		if (isSelected) {
			int selectedRow = table.getSelectedRow();
			if (selectedRow != -1) {
				DefaultTableModel model = (DefaultTableModel) table.getModel();

				String name = (String) model.getValueAt(selectedRow, 0);
				String totalQty = model	.getValueAt(selectedRow, 1)
										.toString();
				String remainingQty = model	.getValueAt(selectedRow, 2)
											.toString();
				String warehouse = (String) model.getValueAt(selectedRow, 3);

				nameField.setText(name);
				totalQtyField.setText(totalQty);
				remainingQtyField.setText(remainingQty);
				warehouseField.setSelectedItem(warehouse);

				nameField.setEditable(false);
				addButton.setEnabled(false);
				updateButton.setEnabled(true);
				deleteButton.setEnabled(true);
			}
		} else {
			clearFields();
			nameField.setEditable(true);
			addButton.setEnabled(true);
			updateButton.setEnabled(false);
			deleteButton.setEnabled(false);
		}
	}

	private void addProduct(ActionEvent e) {
		if (!validateFields()) {
			JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		String name = nameField.getText();
		String warehouse = warehouseField	.getSelectedItem()
											.toString();

		if (isProductExists(name, warehouse)) {
			JOptionPane.showMessageDialog(this, "Product with the same name and warehouse already exists.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		Product product = new Product();
		product.setName(nameField.getText());
		product.setTotalQty(Integer.parseInt(totalQtyField.getText()));
		product.setRemainingQty(Integer.parseInt(remainingQtyField.getText()));
		product.setWarehouse(warehouseField	.getSelectedItem()
											.toString());
		productController.addProduct(product);
		clearFields();
		loadProducts();
	}

	private void updateProduct(ActionEvent e) {
		int selectedRow = table.getSelectedRow();

		if (!validateFields()) {
			JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (selectedRow >= 0) {
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			String name = (String) model.getValueAt(selectedRow, 0);
			Product product = new Product(name, Integer.parseInt(totalQtyField.getText()),
					Integer.parseInt(remainingQtyField.getText()), warehouseField	.getSelectedItem()
																					.toString());
			productController.updateProduct(product);
			loadProducts();
		}
	}

	private void deleteProduct(ActionEvent e) {
		int selectedRow = table.getSelectedRow();

		if (selectedRow >= 0) {
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			String name = (String) model.getValueAt(selectedRow, 0);
			productController.deleteProduct(name);
			loadProducts();
		}
	}

	private void loadProducts() {
		List<Product> products = productController.getAllProducts();
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0); // Clear existing data
		for (Product product : products) {
			model.addRow(new Object[] { product.getName(), product.getTotalQty(), product.getRemainingQty(),
					product.getWarehouse() });
		}
	}

	private boolean validateFields() {
		return !nameField	.getText()
							.isEmpty()
				&& !totalQtyField	.getText()
									.isEmpty()
				&& !remainingQtyField	.getText()
										.isEmpty()
				&& warehouseField.getSelectedIndex() >= 0;
	}

	private void clearFields() {
		nameField.setText("");
		totalQtyField.setText("");
		remainingQtyField.setText("");
		if (warehouseField.getItemCount() > 0) {
			warehouseField.setSelectedIndex(-1);
		}
	}

	private boolean isProductExists(String name, String warehouse) {
		for (int row = 0; row < table.getRowCount(); row++) {
			String productName = (String) table.getValueAt(row, 0);
			String productWarehouse = (String) table.getValueAt(row, 3);
			if (name.equalsIgnoreCase(productName) && warehouse.equalsIgnoreCase(productWarehouse)) {
				return true;
			}
		}
		return false;
	}

	public List<String> getWarehouseNames() {
		List<Warehouse> warehouses = warehouseController.getAllWarehouses();
		List<String> names = new ArrayList<>();
		for (Warehouse warehouse : warehouses) {
			names.add(warehouse.getName());
		}
		return names;
	}
}
