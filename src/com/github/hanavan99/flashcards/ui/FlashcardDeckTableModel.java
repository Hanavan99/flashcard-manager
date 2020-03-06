package com.github.hanavan99.flashcards.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import com.github.hanavan99.flashcards.model.Flashcard;
import com.github.hanavan99.flashcards.model.FlashcardDeck;
import com.github.hanavan99.flashcards.model.Tag;

public class FlashcardDeckTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 426992611847361692L;
	private static final String[] COLS = new String[] { "Front", "Back", "Tags", "Last Viewed" };
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MMM d, YYYY hh:mm:ss aa");
	private String searchText = "";

	private FlashcardDeck deck;
	private ArrayList<Flashcard> displayList = new ArrayList<Flashcard>();

	public FlashcardDeckTableModel(FlashcardDeck deck) {
		this.deck = deck;
		updateCardList();
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
		updateCardList();
		fireTableDataChanged();
	}

	@Override
	public int getRowCount() {
		return displayList.size();
	}

	@Override
	public int getColumnCount() {
		return COLS.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return COLS[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public Flashcard getCardAt(int rowIndex) {
		return displayList.get(rowIndex);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Flashcard card = displayList.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return card.getFront();
		case 1:
			return card.getBack().replace('\n', ' ');
		case 2:
			StringBuilder sb = new StringBuilder();
			for (Tag t : card.getTags()) {
				sb.append(t.getName());
				sb.append(", ");
			}
			if (sb.length() >= 2) {
				sb.setLength(sb.length() - 2);
				return sb.toString();
			} else {
				return "[none]";
			}
		case 3:
			return DATE_FORMAT.format(card.getLastViewed());
		}
		return null;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

	}

	private void updateCardList() {
		displayList.clear();
		String[] search = searchText.toLowerCase().split(":", 2);
		for (Flashcard card : deck.getCards().values()) {
			if (search[0].equals("")) {
				displayList.add(card);
			} else {
				switch (search[0]) {
				case "notags":
				case "notag":
					if (card.getTags().size() == 0) {
						displayList.add(card);
					}
					break;
				case "tag":
					for (Tag t : card.getTags()) {
						if (t.getName().toLowerCase().contains(search[1])) {
							displayList.add(card);
							break;
						}
					}
					break;
				case "tagexact":
					for (Tag t : card.getTags()) {
						if (t.getName().toLowerCase().equals(search[1])) {
							displayList.add(card);
							break;
						}
					}
					break;
				case "front":
					if (card.getFront().toLowerCase().contains(search[1])) {
						displayList.add(card);
					}
					break;
				case "frontexact":
					if (card.getFront().toLowerCase().equals(search[1])) {
						displayList.add(card);
					}
					break;
				case "back":
					if (card.getBack().toLowerCase().contains(search[1])) {
						displayList.add(card);
					}
					break;
				case "backexact":
					if (card.getBack().toLowerCase().equals(search[1])) {
						displayList.add(card);
					}
					break;
				default:
					JOptionPane.showMessageDialog(null, "Invalid search terms.");
					return;
				}
			}
		}
		System.out.println(displayList.size());
	}
}
