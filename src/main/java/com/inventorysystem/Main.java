package com.inventorysystem;

import javax.swing.SwingUtilities;

import com.inventorysystem.view.MainFrame;

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainFrame().setVisible(true);
			}
		});
	}
}
