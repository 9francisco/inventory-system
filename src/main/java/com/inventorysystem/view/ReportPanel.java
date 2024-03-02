package com.inventorysystem.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.inventorysystem.controller.ProductController;
import com.inventorysystem.controller.WarehouseController;
import com.inventorysystem.model.Product;
import com.inventorysystem.model.Warehouse;

/**
 * Panel for displaying the inventory report with color-coded stock levels.
 */
public class ReportPanel extends JPanel {
	private final ProductController productController = new ProductController();
	private final WarehouseController warehouseController = new WarehouseController();
	private JTable reportTable;
	private DefaultTableModel reportTableModel;

	public ReportPanel() {
		setLayout(new BorderLayout());
		initializeUI();
	}

	private void initializeUI() {
		// Legend Panel
		JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		legendPanel.add(new JLabel("Legend:"));
		legendPanel.add(createColorLabel("Not enough product", Color.RED));
		legendPanel.add(createColorLabel("More than necessary", Color.YELLOW));
		legendPanel.add(createColorLabel("Full capacity", Color.GREEN));

		// View Report Button
		JButton viewReportButton = new JButton("View Report");
		viewReportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadReportData(); // Load the data when the button is clicked
			}
		});

		// Report Table
		reportTableModel = new DefaultTableModel(
				new Object[] { "Product Name", "On Stock", "Sold", "Total", "Warehouse" }, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		reportTable = new JTable(reportTableModel);
		reportTable.setDefaultRenderer(Object.class, new ReportTableCellRenderer());

		JScrollPane scrollPane = new JScrollPane(reportTable);

		// Assembling the Panel
		add(legendPanel, BorderLayout.NORTH);
		add(viewReportButton, BorderLayout.SOUTH);
		add(scrollPane, BorderLayout.CENTER);
	}

	private JLabel createColorLabel(String text, Color color) {
		JLabel label = new JLabel(text);
		label.setOpaque(true);
		label.setBackground(color);
		return label;
	}

	private void loadReportData() {
		List<Product> products = productController.getAllProducts();
		Map<String, Warehouse> warehouseMap = new HashMap<>();
		for (Warehouse warehouse : warehouseController.getAllWarehouses()) {
			warehouseMap.put(warehouse.getName(), warehouse);
		}

		reportTableModel.setRowCount(0); // Clear existing data
		for (Product product : products) {
			Warehouse warehouse = warehouseMap.get(product.getWarehouse());
			int sold = product.getTotalQty() - product.getRemainingQty();
			reportTableModel.addRow(new Object[] { product.getName(), product.getRemainingQty(), sold,
					product.getTotalQty(), product.getWarehouse() });
		}
	}

	/**
	 * Custom cell renderer to color-code the table cells based on stock level.
	 */
	class ReportTableCellRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			c.setBackground(Color.WHITE); // Default background
			if (column == 1) {
				String warehouseName = (String) table.getValueAt(row, 4);
				Warehouse warehouse = warehouseController.getWarehouseByName(warehouseName);
				int onStock = (Integer) table.getValueAt(row, 1);

				if (onStock < warehouse.getMinProduct()) {
					c.setBackground(Color.RED); // Not enough product
				} else if (onStock > warehouse.getMaxProduct()) {
					c.setBackground(Color.YELLOW); // More than necessary
				} else if (onStock == warehouse.getMaxProduct()) {
					c.setBackground(Color.GREEN); // Full capacity
				}
			}
			return c;
		}
	}
}
