package com.github.hanavan99.flashcards.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableModel;

import com.github.hanavan99.flashcards.context.Context;
import com.github.hanavan99.flashcards.context.IContextListener;
import com.github.hanavan99.flashcards.io.IFileHandler;
import com.github.hanavan99.flashcards.io.IMessageCallback;
import com.github.hanavan99.flashcards.model.Flashcard;
import com.github.hanavan99.flashcards.util.Utils;

public class CardWindow extends Window {

	private JMenuBar menu;
	private JMenu fileMenu;
	private JMenu deckMenu;
	private JTabbedPane tabbedPane;
	private JPanel viewerPane;
	private JPanel cardListPane;
	private JPanel tagListPane;
	private CardViewer cardViewer;
	private Random random = new Random();
	private JFileChooser fileChooser;
	private IMessageCallback messageCallback;

	public CardWindow(Context context) {
		super("Card Viewer", 500, 500, null);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLayout(new GridLayout());

		menu = new JMenuBar();
		frame.setJMenuBar(menu);

		fileMenu = new JMenu("File");
		menu.add(fileMenu);

		messageCallback = new IMessageCallback() {

			@Override
			public boolean confirm(String message) {
				return JOptionPane.showConfirmDialog(frame, message, "Confirm",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
			}

			@Override
			public void message(String message) {
				JOptionPane.showMessageDialog(frame, message);
			}

		};

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

		JMenu openFileMenu = new JMenu("Open...");
		for (IFileHandler handler : context.getFileHandlers()) {
			JMenuItem openItem = new JMenuItem(handler.getHandlerName());
			openItem.addActionListener((_e) -> {
				JFileChooser chooser = handler.getFileChooser(new File("."));
				if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
					try {
						File f = chooser.getSelectedFile();
						context.setDeck(handler.readFlashcards(f, messageCallback));
						context.setDeckFile(f);
						context.setCurrentFileHandler(handler);
						context.markClean();
					} catch (IOException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(frame, "Failed to open deck: " + e.getMessage());
					}
				}
			});
			openFileMenu.add(openItem);
		}
		fileMenu.add(openFileMenu);

		JMenuItem saveFileMenuItem = new JMenuItem("Save");
		saveFileMenuItem.addActionListener((_e) -> {
			if (context.getDeckFile() != null && context.getCurrentFileHandler() != null) {
				try {
					context.getCurrentFileHandler().writeFlashcards(context.getDeckFile(), context.getDeck(),
							messageCallback);
					context.markClean();
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(frame, "Failed to save deck: " + e.getMessage());
				}
			} else {
				JOptionPane.showMessageDialog(frame,
						"No deck is open, or the deck hasn't been saved. Please use 'Save As'.");
			}
		});
		fileMenu.add(saveFileMenuItem);

		JMenu saveAsFileMenu = new JMenu("Save As...");
		for (IFileHandler handler : context.getFileHandlers()) {
			JMenuItem saveAsItem = new JMenuItem(handler.getHandlerName());
			saveAsItem.addActionListener((_e) -> {
				JFileChooser chooser = handler
						.getFileChooser(context.getDeckFile() != null ? context.getDeckFile() : new File("."));
				if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
					try {
						File f = chooser.getSelectedFile();
						handler.writeFlashcards(f, context.getDeck(), messageCallback);
						context.setDeckFile(f);
						context.setCurrentFileHandler(handler);
						context.markClean();
					} catch (IOException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(frame, "Failed to open deck: " + e.getMessage());
					}
				}
			});
			saveAsFileMenu.add(saveAsItem);
		}
		fileMenu.add(saveAsFileMenu);

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

		tabbedPane = new JTabbedPane();
		frame.add(tabbedPane);

		setupViewerPane(context);
		setupCardListPane(context);

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
					System.exit(0); // not sure why I have to do this
				}
			}
		});

		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	public void setupViewerPane(Context context) {
		viewerPane = new JPanel();
		viewerPane.setLayout(new GridLayout());
		tabbedPane.add("Viewer", viewerPane);

		cardViewer = new CardViewer(context);
		viewerPane.add(cardViewer);
	}

	public void setupCardListPane(Context context) {
		cardListPane = new JPanel();
		cardListPane.setLayout(new BorderLayout());
		tabbedPane.add("Card List", cardListPane);

		JTextField searchText = new JTextField(context.getFilterString());
		searchText.addActionListener((_e) -> {
			context.setFilterString(searchText.getText());
			frame.repaint();
		});

		cardListPane.add(searchText, BorderLayout.PAGE_START);

		FlashcardDeckTableModel cardTableModel;
		JTable cardTable = new JTable(cardTableModel = new FlashcardDeckTableModel(context));
		cardTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				setFlashcard(cardTableModel.getCardAt(cardTable.getSelectedRow()));
			}

		});
		JScrollPane tableScrollPane;
		cardListPane.add(tableScrollPane = new JScrollPane(cardTable), BorderLayout.CENTER);
	}

	public void setFlashcard(Flashcard card) {
		cardViewer.setFlashcard(card);
		frame.repaint();
	}

}
