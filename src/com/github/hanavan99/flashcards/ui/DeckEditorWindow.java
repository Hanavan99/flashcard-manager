package com.github.hanavan99.flashcards.ui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

import com.github.hanavan99.flashcards.context.Context;
import com.github.hanavan99.flashcards.model.Flashcard;
import com.github.hanavan99.flashcards.model.Tag;

public class DeckEditorWindow extends Window {

	private Context context;
	private JMenuBar menu;
	private JMenu editMenu;
	private JTextField searchText;
	private JTable cardTable;
	private FlashcardDeckTableModel cardTableModel;
	private JScrollPane tableScrollPane;

	public DeckEditorWindow(Context context, Window parent) {
		super("Edit Deck", 600, 800, parent);
		this.context = context;
		frame.setLayout(new BorderLayout());

		menu = new JMenuBar();
		frame.setJMenuBar(menu);

		editMenu = new JMenu("Edit");
		menu.add(editMenu);

		JMenuItem editDeckSettingsMenuItem = new JMenuItem("Edit Deck Settings");
		editDeckSettingsMenuItem.addActionListener((_e) -> {
			if (context.getDeck() != null) {
				DeckSettingsWindow settings = new DeckSettingsWindow(context, this);
				settings.show();
			} else {
				JOptionPane.showMessageDialog(frame, Context.MSG_NO_DECK_LOADED);
			}
		});
		editMenu.add(editDeckSettingsMenuItem);

		JMenuItem addCardMenuItem = new JMenuItem("Add Card");
		addCardMenuItem.addActionListener((_e) -> {
			if (context.getDeck() != null) {
				Date now = Calendar.getInstance().getTime();
				Flashcard card = new Flashcard(UUID.randomUUID(), "", "", new ArrayList<Tag>(), now, now, 0);
				CardEditorWindow editor = new CardEditorWindow(context, card, this);
				editor.show();
				context.getDeck().getCards().put(card.getID(), card);
				context.fireCardUpdated(card);
			} else {
				JOptionPane.showMessageDialog(frame, Context.MSG_NO_DECK_LOADED);
			}
		});
		editMenu.add(addCardMenuItem);

		JMenuItem editCardMenuItem = new JMenuItem("Edit Card");
		editCardMenuItem.addActionListener((_e) -> {
			if (context.getDeck() != null && cardTable.getSelectedRow() != -1) {
				CardEditorWindow editor = new CardEditorWindow(context,
						cardTableModel.getCardAt(cardTable.getSelectedRow()), this);
				editor.show();
			} else {
				JOptionPane.showMessageDialog(frame, "Please select a card to edit.");
			}
		});
		editMenu.add(editCardMenuItem);

		JMenuItem deleteCardMenuItem = new JMenuItem("Delete Card");
		deleteCardMenuItem.addActionListener((_e) -> {
			if (cardTable.getSelectedRow() != -1) {
				if (JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this card?", "Confirm Delete",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					context.getDeck().getCards().remove(cardTableModel.getCardAt(cardTable.getSelectedRow()).getID());
					cardTableModel.fireTableDataChanged();
				}
			} else {
				JOptionPane.showMessageDialog(frame, "Please select a card to delete.");
			}
		});
		editMenu.add(deleteCardMenuItem);
		editMenu.addSeparator();

		JMenuItem applySearchMenuItem = new JMenuItem("Apply Search to Card Viewer");
		applySearchMenuItem.addActionListener((_e) -> {
			context.setFilterString(searchText.getText());
		});
		//editMenu.add(applySearchMenuItem);

		JMenuItem showCardMenuItem = new JMenuItem("Show Card in Viewer");
		showCardMenuItem.addActionListener((_e) -> {
			if (parent instanceof CardWindow && cardTable.getSelectedRow() >= 0) {
				CardWindow cw = (CardWindow) parent;
				cw.setFlashcard(cardTableModel.getCardAt(cardTable.getSelectedRow()));
			} else {
				JOptionPane.showMessageDialog(frame, "The parent of this frame is not a CardWindow or no card is selected");
			}
		});
		editMenu.add(showCardMenuItem);

		searchText = new JTextField(context.getFilterString());
		searchText.addActionListener((_e) -> {
			context.setFilterString(searchText.getText());
			frame.repaint();
		});

		frame.add(searchText, BorderLayout.PAGE_START);

		cardTable = new JTable(cardTableModel = new FlashcardDeckTableModel(context));
		cardTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (parent instanceof CardWindow && cardTable.getSelectedRow() >= 0) {
					CardWindow cw = (CardWindow) parent;
					cw.setFlashcard(cardTableModel.getCardAt(cardTable.getSelectedRow()));
				}
			}

		});
		frame.add(tableScrollPane = new JScrollPane(cardTable), BorderLayout.CENTER);
	}

}
