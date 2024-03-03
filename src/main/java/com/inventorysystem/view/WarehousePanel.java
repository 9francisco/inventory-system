package com.inventorysystem.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;

import com.inventorysystem.controller.WarehouseController;
import com.inventorysystem.model.Warehouse;

/**
 * Panel for managing warehouses in the inventory system.
 */
public class WarehousePanel extends JPanel {
	private WarehouseController warehouseController;
	private JTable table;
	private JTextField nameField;
    private JFormattedTextField minProductField, maxProductField;

	private JButton addButton, updateButton, deleteButton, resetButton;

	public WarehousePanel() {
		warehouseController = new WarehouseController();

		initializeUI();
		loadWarehouses();
	}

	private void initializeUI() {
		setLayout(new BorderLayout(5, 5));

		// Form Panel
		JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
		nameField = new JTextField();	
		NumberFormat integerFormat = NumberFormat.getIntegerInstance();
		integerFormat.setGroupingUsed(false);
		NumberFormatter formatter = new NumberFormatter(integerFormat);
		formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        minProductField = new JFormattedTextField(formatter);
        maxProductField = new JFormattedTextField(formatter);
        
		formPanel.add(new JLabel("Name:"));
		formPanel.add(nameField);
		formPanel.add(new JLabel("Min Product:"));
		formPanel.add(minProductField);
		formPanel.add(new JLabel("Max Product:"));
		formPanel.add(maxProductField);

		// Buttons Panel
		JPanel buttonsPanel = new JPanel();
		addButton = new JButton("Add");
		updateButton = new JButton("Update");
		deleteButton = new JButton("Delete");
		resetButton = new JButton("Reset");
		buttonsPanel.add(addButton);
		buttonsPanel.add(updateButton);
		buttonsPanel.add(deleteButton);
		buttonsPanel.add(resetButton);

		updateButton.setEnabled(false); // Initially disabled
		deleteButton.setEnabled(false); // Initially disabled

		// Table Panel
		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[] { "Name", "Min Product", "Max Product" }, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});

		JScrollPane scrollPane = new JScrollPane(table);

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Custom ListSelectionListener to handle row selection toggle
		table	.getSelectionModel()
				.addListSelectionListener(new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						if (!e.getValueIsAdjusting()) {
							if (table.getSelectedRow() != -1) {
								updateNameFieldBasedOnSelection(true);
							} else {
								updateNameFieldBasedOnSelection(false);
							}
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
		add(buttonsPanel, BorderLayout.SOUTH);

		// Add action listeners
		addButton.addActionListener(e -> addWarehouse());
		updateButton.addActionListener(e -> updateWarehouse());
		deleteButton.addActionListener(e -> deleteWarehouse());
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
				String minProduct = model	.getValueAt(selectedRow, 1)
											.toString();
				String maxProduct = model	.getValueAt(selectedRow, 2)
											.toString();

				nameField.setText(name);
				minProductField.setText(minProduct);
				maxProductField.setText(maxProduct);

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

	private void addWarehouse() {
		if (!validateFields()) {
			JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		String name = nameField.getText();
		if (isWarehouseExists(name)) {
			JOptionPane.showMessageDialog(this, "Warehouse with the same name already exists.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		int minProduct = Integer.parseInt(minProductField.getText());
		int maxProduct = Integer.parseInt(maxProductField.getText());
		Warehouse warehouse = new Warehouse(name, minProduct, maxProduct);
		warehouseController.addWarehouse(warehouse);
		clearFields();
		loadWarehouses();
	}

	private void updateWarehouse() {
		int selectedRow = table.getSelectedRow();

		if (!validateFields()) {
			JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (selectedRow >= 0) {
			DefaultTableModel model = (DefaultTableModel) table.getModel();

			String name = (String) model.getValueAt(selectedRow, 0);
			int minProduct = Integer.parseInt(minProductField.getText());
			int maxProduct = Integer.parseInt(maxProductField.getText());
			Warehouse warehouse = new Warehouse(name, minProduct, maxProduct);
			warehouseController.updateWarehouse(warehouse);
			loadWarehouses();
		}
	}

	private void deleteWarehouse() {
		int selectedRow = table.getSelectedRow();

		if (selectedRow >= 0) {
			DefaultTableModel model = (DefaultTableModel) table.getModel();

			String name = (String) model.getValueAt(selectedRow, 0);
			warehouseController.deleteWarehouse(name);
			loadWarehouses();
		}
	}

	private void loadWarehouses() {
		List<Warehouse> warehouses = warehouseController.getAllWarehouses();

		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0); // Clear existing data
		for (Warehouse warehouse : warehouses) {
			model.addRow(new Object[] { warehouse.getName(), warehouse.getMinProduct(), warehouse.getMaxProduct() });
		}
	}

	private boolean validateFields() {
		return !nameField	.getText()
							.isEmpty()
				&& !minProductField	.getText()
									.isEmpty()
				&& !maxProductField	.getText()
									.isEmpty();
	}

	private void clearFields() {
		nameField.setText("");
		minProductField.setText("");
		maxProductField.setText("");
	}

	private boolean isWarehouseExists(String name) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();

		for (int row = 0; row < table.getRowCount(); row++) {
			if (name.equalsIgnoreCase((String) model.getValueAt(row, 0))) {
				return true;
			}
		}
		return false;
	}

}
