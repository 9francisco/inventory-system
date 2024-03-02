package com.inventorysystem.view;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame {
	private JTabbedPane tabbedPane;

	public MainFrame() {
		initialize();
	}

	private void initialize() {
		setTitle("Inventory System");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		tabbedPane = new JTabbedPane();

		tabbedPane.addTab("Warehouse", new WarehousePanel());
		tabbedPane.addTab("Product", new ProductPanel());
		tabbedPane.addTab("Report", new ReportPanel());

		add(tabbedPane);
	}
}
