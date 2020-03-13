package com.github.hanavan99.flashcards.ui;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.github.hanavan99.flashcards.context.Context;
import com.github.hanavan99.flashcards.context.IContextListener;
import com.github.hanavan99.flashcards.io.FileCardHandler;
import com.github.hanavan99.flashcards.io.ICardHandler;
import com.github.hanavan99.flashcards.model.Flashcard;
import com.github.hanavan99.flashcards.util.Utils;

public class CardWindow extends Window {

	private JMenuBar menu;
	private JMenu fileMenu;
	private JMenu deckMenu;
	private CardViewer cardViewer;
	private Random random = new Random();
	private JFileChooser fileChooser;
	private ICardHandler cardHandler;

	public CardWindow(Context context) {
		super("Card Viewer", 500, 500, null);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLayout(new GridLayout());

		fileChooser = new JFileChooser(new File("."));
		cardHandler = new FileCardHandler();

		menu = new JMenuBar();
		frame.setJMenuBar(menu);

		fileMenu = new JMenu("File");
		menu.add(fileMenu);

		JMenuItem newFileMenuItem = new JMenuItem("New");
		newFileMenuItem.addActionListener((_e) -> {
			if (context.getDeck() == null || !context.isDirty()) {
				context.setDeck(Utils.createNewDeck());
				context.markDirty();
			} else if (JOptionPane.showConfirmDialog(frame, Context.MSG_DECK_ALREADY_OPEN, "Confirm Overwrite",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				context.setDeck(Utils.createNewDeck());
				context.markDirty();
			}
		});
		fileMenu.add(newFileMenuItem);

		JMenuItem openFileMenuItem = new JMenuItem("Open...");
		openFileMenuItem.addActionListener((_e) -> {
			if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
				try {
					File f = fileChooser.getSelectedFile();
					context.setDeck(cardHandler.readFlashcards(f));
					context.setDeckFile(f);
					context.markClean();
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(frame, "Failed to open file: " + e.getMessage());
				}
			}
		});
		fileMenu.add(openFileMenuItem);

		JMenuItem saveFileMenuItem = new JMenuItem("Save");
		saveFileMenuItem.addActionListener((_e) -> {
			if (context.getDeckFile() != null) {
				try {
					cardHandler.writeFlashcards(context.getDeckFile(), context.getDeck());
					context.markClean();
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(frame, "Failed to save file: " + e.getMessage());
				}
			}
		});
		fileMenu.add(saveFileMenuItem);

		JMenuItem saveAsFileMenuItem = new JMenuItem("Save As...");
		saveAsFileMenuItem.addActionListener((_e) -> {
			if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
				try {
					File f = fileChooser.getSelectedFile();
					cardHandler.writeFlashcards(f, context.getDeck());
					context.setDeckFile(f);
					context.markClean();
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(frame, "Failed to save file: " + e.getMessage());
				}
			}
		});
		fileMenu.add(saveAsFileMenuItem);

		deckMenu = new JMenu("Deck");
		menu.add(deckMenu);

		JMenuItem viewCardsMenuItem = new JMenuItem("View Card List");
		viewCardsMenuItem.addActionListener((_e) -> {
			DeckEditorWindow editor = new DeckEditorWindow(context, this);
			editor.show();
		});
		deckMenu.add(viewCardsMenuItem);

		JMenuItem viewTagsMenuItem = new JMenuItem("View Tag List");
		viewTagsMenuItem.addActionListener((_e) -> {
			TagEditorWindow editor = new TagEditorWindow(context, this);
			editor.show();
		});
		deckMenu.add(viewTagsMenuItem);

		cardViewer = new CardViewer(context);
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
				switch (e.getKeyCode()) {
				case KeyEvent.VK_R: // random card
					List<Flashcard> cardList = context.getFilteredCardList();
					if (cardList.size() > 0) {
						Flashcard card = cardList.get(random.nextInt(cardList.size()));
						cardViewer.setFlashcard(card);
					}
					break;
				}
				frame.repaint();
			}
		});

		context.addContextListener(new IContextListener() {

			@Override
			public void deckChanged(Context context) {
				frame.setTitle("Card Viewer (" + context.getDeck().getName() + ")");
			}

			@Override
			public void cardUpdated(Context context, Flashcard card) {
				// TODO Auto-generated method stub

			}

			@Override
			public void cardFilterUpdated(Context context, String filterString) {
				frame.repaint();
			}

		});

		// handle closing the window
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (!context.isDirty()
						|| (context.isDirty() && JOptionPane.showConfirmDialog(frame, Context.MSG_UNSAVED_CHANGES,
								"Confirm Quit", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)) {
					frame.dispose();
					System.exit(0);  // not sure why I have to do this
				}
			}
		});

		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	public void setFlashcard(Flashcard card) {
		cardViewer.setFlashcard(card);
		frame.repaint();
	}

}
