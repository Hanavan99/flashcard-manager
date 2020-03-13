package com.github.hanavan99.flashcards.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.github.hanavan99.flashcards.context.Context;
import com.github.hanavan99.flashcards.context.IContextListener;
import com.github.hanavan99.flashcards.model.Flashcard;
import com.github.hanavan99.flashcards.model.Tag;
import com.github.hanavan99.flashcards.util.Utils;

public class FlashcardDeckTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 426992611847361692L;
	private static final String[] COLS = new String[] { "Front", "Back", "Tags", "Last Viewed", "Next View",
			"View Count" };
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MMM d, YYYY hh:mm:ss aa");

	private final Context context;

	public FlashcardDeckTableModel(Context context) {
		this.context = context;

		context.addContextListener(new IContextListener() {

			@Override
			public void deckChanged(Context context) {
				// TODO Auto-generated method stub

			}

			@Override
			public void cardUpdated(Context context, Flashcard card) {
				fireTableDataChanged();
			}

			@Override
			public void cardFilterUpdated(Context context, String filterString) {
				fireTableDataChanged();
			}

		});
	}

	@Override
	public int getRowCount() {
		return context.getFilteredCardList().size();
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
		return context.getFilteredCardList().get(rowIndex);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Flashcard card = context.getFilteredCardList().get(rowIndex);
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
		case 4:
			return DATE_FORMAT.format(card.getNewViewDate()) + " ("
					+ Utils.dateDiffToString(Calendar.getInstance().getTime(), card.getNewViewDate()) + ")";
		case 5:
			return card.getViewCount();
		}
		return null;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

	}
}
