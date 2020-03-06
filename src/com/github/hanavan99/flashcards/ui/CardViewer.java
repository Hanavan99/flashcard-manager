package com.github.hanavan99.flashcards.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import com.github.hanavan99.flashcards.model.Flashcard;

public class CardViewer extends JComponent {

	private static final long serialVersionUID = -50101384715001808L;

	private Flashcard card;
	private boolean showFront = true;
	private Font frontFont = new Font("DengXian", Font.PLAIN, 200);
	private Font backFont = new Font("DengXian", Font.PLAIN, 36);

	public CardViewer() {
		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					showFront = false;
				}
				repaint();
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

		});
		addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					showFront = false;
				}
				repaint();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

		});
		addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				repaint();
			}

		});
	}

	public void setFlashcard(Flashcard card) {
		this.card = card;
		showFront = true;
	}

	@Override
	public void paintComponent(Graphics gfx) {
		super.paintComponent(gfx);
		int width = getWidth();
		int height = getHeight();

		// draw background
		Graphics2D g = (Graphics2D) gfx;
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.BLACK);

		// enable antialiasing
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (card != null) {
			if (showFront) {
				String front = card.getFront();
				g.setFont(frontFont);
				Rectangle2D strBounds = g.getFontMetrics().getStringBounds(front, g);
				g.drawString(front, width / 2 - (int) strBounds.getWidth() / 2, height / 2);
			} else {
				String[] lines = card.getBack().split("\n");
				g.setFont(backFont);
				int y = 50;
				for (String line : lines) {
					g.drawString(line, 50, y);
					y += g.getFontMetrics(backFont).getHeight();
				}
			}
		} else {
			g.drawString("No card selected.", 10, 20);
		}
	}

}
