package com.github.hanavan99.flashcards.ui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.github.hanavan99.flashcards.model.Flashcard;
import com.github.hanavan99.flashcards.model.FlashcardDeck;
import com.github.hanavan99.flashcards.model.Tag;

public class DeckEditorWindow extends Window {

	private FlashcardDeck deck;
	private JMenuBar menu;
	private JMenu editMenu;
	private JTextField searchText;
	private JTable cardTable;
	private FlashcardDeckTableModel cardTableModel;
	private JScrollPane tableScrollPane;

	public DeckEditorWindow(FlashcardDeck deck, Window parent) {
		super("Edit Deck", 600, 800, parent);
		this.deck = deck;
		frame.setLayout(new BorderLayout());

		menu = new JMenuBar();
		frame.setJMenuBar(menu);

		editMenu = new JMenu("Edit");
		menu.add(editMenu);

		JMenuItem editDeckSettingsMenuItem = new JMenuItem("Edit Deck Settings");
		editDeckSettingsMenuItem.addActionListener((_e) -> {
			DeckSettingsWindow settings = new DeckSettingsWindow(deck, this);
			settings.show();
		});
		editMenu.add(editDeckSettingsMenuItem);

		JMenuItem addCardMenuItem = new JMenuItem("Add Card");
		addCardMenuItem.addActionListener((_e) -> {
			Date now = Calendar.getInstance().getTime();
			Flashcard card = new Flashcard(UUID.randomUUID(), "", "", new ArrayList<Tag>(), now, now, 0);
			CardEditorWindow editor = new CardEditorWindow(deck, card, this);
			editor.show();
			deck.getCards().put(card.getID(), card);
		});
		editMenu.add(addCardMenuItem);

		JMenuItem editCardMenuItem = new JMenuItem("Edit Card");
		editCardMenuItem.addActionListener((_e) -> {
			if (cardTable.getSelectedRow() != -1) {
				CardEditorWindow editor = new CardEditorWindow(deck, cardTableModel.getCardAt(cardTable.getSelectedRow()), this);
				editor.show();
			} else {
				JOptionPane.showMessageDialog(frame, "Please select a card to edit.");
			}
		});
		editMenu.add(editCardMenuItem);

		JMenuItem deleteCardMenuItem = new JMenuItem("Delete Card");
		deleteCardMenuItem.addActionListener((_e) -> {
			if (cardTable.getSelectedRow() != -1) {
				if (JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this card?", "Confirm Delete", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					deck.getCards().remove(cardTableModel.getCardAt(cardTable.getSelectedRow()).getID());
					cardTableModel.fireTableDataChanged();
				}
			} else {
				JOptionPane.showMessageDialog(frame, "Please select a card to delete.");
			}
		});
		editMenu.add(deleteCardMenuItem);

		searchText = new JTextField();
		searchText.addActionListener((_e) -> {
			cardTableModel.setSearchText(searchText.getText());
			tableScrollPane.invalidate();
			frame.repaint();
		});

		frame.add(searchText, BorderLayout.PAGE_START);

		cardTable = new JTable(cardTableModel = new FlashcardDeckTableModel(deck));
		frame.add(tableScrollPane = new JScrollPane(cardTable), BorderLayout.CENTER);
	}

}
