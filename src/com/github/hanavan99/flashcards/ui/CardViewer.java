package com.github.hanavan99.flashcards.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JComponent;

import com.github.hanavan99.flashcards.context.Context;
import com.github.hanavan99.flashcards.model.Flashcard;
import com.github.hanavan99.flashcards.util.Utils;

public class CardViewer extends JComponent {

	private static final long serialVersionUID = -50101384715001808L;

	private final Context context;
	private Flashcard card;
	private boolean showFront = true;
	private Font defaultFont = new Font("Arial", Font.PLAIN, 12);
	private Font frontFont = new Font("DengXian", Font.PLAIN, 200);
	private Font backFont = new Font("DengXian", Font.PLAIN, 36);
	private Button[] buttons;

	public CardViewer(Context context) {
		this.context = context;
		Button againButton = new Button("Again", Color.RED, () -> {
			card.setNewViewDate(new Date(card.getLastViewed().getTime() + 1000 * 60 * 10)); // 10 minutes
			chooseNewCard();
			repaint();
		}, -310, 10, 300, 50);
		Button hardButton = new Button("Hard", Color.BLUE, () -> {
			card.setNewViewDate(Utils.computeNewCardDate(card.getLastViewed(), card.getViewCount(), 0.25));
			chooseNewCard();
			repaint();
		}, -310, 70, 300, 50);
		Button easyButton = new Button("Easy", Color.GREEN, () -> {
			card.setNewViewDate(Utils.computeNewCardDate(card.getLastViewed(), card.getViewCount(), 1.0));
			chooseNewCard();
			repaint();
		}, -310, 130, 300, 50);
		buttons = new Button[] { againButton, hardButton, easyButton };

		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					Point mouse = getMousePosition();
					mouse.translate(-getWidth(), 0);
					for (Button b : buttons) {
						if (mouse != null && b.isPointInside(mouse)) {
							b.getClickAction().run();
							return;
						}
					}

					if (card != null) {
						showFront = false;
					}
				}
				repaint();
			}

		});
		addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseMoved(MouseEvent e) {
				repaint();
			}

		});
	}

	public Flashcard getFlashcard() {
		return card;
	}

	public void setFlashcard(Flashcard card) {
		this.card = card;
		showFront = true;
	}

	private void chooseNewCard() {
		List<Flashcard> cardList = context.getPracticeCardList();
		if (cardList.size() > 0) {
			setFlashcard(cardList.get(0));
			card.setLastViewed(Calendar.getInstance().getTime());
			card.setViewCount(card.getViewCount() + 1);
		}
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
				int y = 100;
				for (String line : lines) {
					g.drawString(line, 100, y);
					y += g.getFontMetrics(backFont).getHeight();
				}
			}
			g.setFont(defaultFont);
			g.drawString("Current filter: " + context.getFilterString(), 10, 20);
			g.drawString("Card count: " + context.getFilteredCardList().size(), 10, 35);
			g.drawString("Cards to practice: " + context.getPracticeCardList().size(), 10, 50);
		} else {
			g.drawString("No card selected.", 10, 20);
		}

		if (context.getDeck() == null) {
			g.drawString("No deck loaded.", 10, 40);
		}

		if (!showFront) {
			Point mouse = getMousePosition();
			if (mouse != null) {
				mouse.translate(-width, 0);
			}
			g.setFont(backFont);
			for (Button b : buttons) {
				if (mouse != null && b.isPointInside(mouse)) {
					g.setColor(b.getColor());
				} else {
					g.setColor(b.getColor().darker());
				}
				g.fillRect(width + b.getBounds().x, b.getBounds().y, b.getBounds().width, b.getBounds().height);
				g.setColor(Color.BLACK);
				g.drawRect(width + b.getBounds().x, b.getBounds().y, b.getBounds().width, b.getBounds().height);
				int strWidth = g.getFontMetrics().stringWidth(b.getText());
				g.setColor(Color.WHITE);
				g.drawString(b.getText(), width + b.getBounds().x + b.getBounds().width / 2 - strWidth / 2,
						b.getBounds().y + b.getBounds().height / 2 + 10);
			}
		}
	}

	public static final class Button {

		private String text;
		private Color color;
		private Runnable clickAction;
		private Rectangle bounds;

		public Button(String text, Color color, Runnable clickAction, int x, int y, int width, int height) {
			this.text = text;
			this.color = color;
			this.clickAction = clickAction;
			this.bounds = new Rectangle(x, y, width, height);
		}

		public boolean isPointInside(Point p) {
			return bounds.contains(p);
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public Color getColor() {
			return color;
		}

		public void setColor(Color color) {
			this.color = color;
		}

		public Runnable getClickAction() {
			return clickAction;
		}

		public void setClickAction(Runnable clickAction) {
			this.clickAction = clickAction;
		}

		public Rectangle getBounds() {
			return bounds;
		}

		public void setBounds(Rectangle bounds) {
			this.bounds = bounds;
		}

	}

}
