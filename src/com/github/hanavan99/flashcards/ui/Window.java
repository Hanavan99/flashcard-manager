package com.github.hanavan99.flashcards.ui;

import javax.swing.JFrame;
import javax.swing.JLabel;

public abstract class Window {

	private final Window parent;
	protected JFrame frame;

	public Window(String name, int width, int height, Window parent) {
		this.parent = parent;

		frame = new JFrame(name);
		frame.setBounds(50, 50, width, height);
		frame.setDefaultCloseOperation(parent != null ? JFrame.HIDE_ON_CLOSE : JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
	}
	
	public JFrame getFrame() {
		return frame;
	}

	protected void createLabel(String text, int x, int y) {
		JLabel label = new JLabel(text);
		int length = label.getFontMetrics(label.getFont()).stringWidth(text);
		label.setBounds(x, y, length + 10, 20);
		frame.add(label);
	}

	public void show() {
		frame.setVisible(true);
	}

	public void hide() {
		frame.setVisible(true);
	}
	
	public void close() {
		hide();
		frame.dispose();
	}

}
