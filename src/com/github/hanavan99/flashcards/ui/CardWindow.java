package com.github.hanavan99.flashcards.ui;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.github.hanavan99.flashcards.io.FileCardHandler;
import com.github.hanavan99.flashcards.io.ICardHandler;
import com.github.hanavan99.flashcards.model.Flashcard;
import com.github.hanavan99.flashcards.model.FlashcardDeck;

public class CardWindow extends Window {

	private JMenuBar menu;
	private JMenu fileMenu;
	private JMenu deckMenu;
	private CardViewer cardViewer;
	private FlashcardDeck deck;
	private Random random = new Random();
	private JFileChooser fileChooser;
	private ICardHandler cardHandler;

	public CardWindow() {
		super("Flashcard Manager", 500, 500, null);
		frame.setLayout(new GridLayout());

		fileChooser = new JFileChooser(new File("."));
		cardHandler = new FileCardHandler();

		menu = new JMenuBar();
		frame.setJMenuBar(menu);

		fileMenu = new JMenu("File");
		menu.add(fileMenu);

		JMenuItem openFileMenuItem = new JMenuItem("Open...");
		openFileMenuItem.addActionListener((_e) -> {
			if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
				try {
					deck = cardHandler.readFlashcards(fileChooser.getSelectedFile());
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(frame, "Failed to open file: " + e.getMessage());
				}
			}
		});
		fileMenu.add(openFileMenuItem);

		JMenuItem saveFileMenuItem = new JMenuItem("Save As...");
		saveFileMenuItem.addActionListener((_e) -> {
			if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
				try { 
					cardHandler.writeFlashcards(fileChooser.getSelectedFile(), deck);
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(frame, "Failed to save file: " + e.getMessage());
				}
			}
		});
		fileMenu.add(saveFileMenuItem);

		deckMenu = new JMenu("Deck");
		menu.add(deckMenu);

		JMenuItem viewCardsMenuItem = new JMenuItem("View Card List");
		viewCardsMenuItem.addActionListener((_e) -> {
			DeckEditorWindow editor = new DeckEditorWindow(deck, this);
			editor.show();
		});
		deckMenu.add(viewCardsMenuItem);

		JMenuItem viewTagsMenuItem = new JMenuItem("View Tag List");
		viewTagsMenuItem.addActionListener((_e) -> {
			TagEditorWindow editor = new TagEditorWindow(deck, this);
			editor.show();
		});
		deckMenu.add(viewTagsMenuItem);

		cardViewer = new CardViewer();
		frame.add(cardViewer);

		frame.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				switch (e.getKeyCode()) {
				case KeyEvent.VK_R: // random card
					Flashcard[] cards = deck.getCards().values().toArray(new Flashcard[0]);
					if (cards.length > 0) {
						cardViewer.setFlashcard(cards[random.nextInt(cards.length)]);
					}
					break;
				}
				frame.repaint();
			}
		});

		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	public FlashcardDeck getFlashcardDeck() {
		return deck;
	}

	public void setFlashcardDeck(FlashcardDeck deck) {
		this.deck = deck;
	}

}
