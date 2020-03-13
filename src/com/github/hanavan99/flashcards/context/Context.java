package com.github.hanavan99.flashcards.context;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.github.hanavan99.flashcards.model.Flashcard;
import com.github.hanavan99.flashcards.model.FlashcardDeck;
import com.github.hanavan99.flashcards.util.QueryHelper;
import com.github.hanavan99.flashcards.util.Utils;

public class Context {

	public static final String MSG_NO_DECK_LOADED = "No deck open. Please create a new deck or open one first.";
	public static final String MSG_DECK_ALREADY_OPEN = "A deck is already open. Any unsaved changes will be lost. Are you sure you want to continue?";
	public static final String MSG_UNSAVED_CHANGES = "You have unsaved changes. Are you sure you want to continue?";

	private FlashcardDeck deck;
	private File deckFile;
	private String filterString;
	private QueryHelper<Flashcard> queryHelper;
	private List<Flashcard> filteredCardList;
	private List<Flashcard> practiceCardList;
	private boolean isDirty;
	private ArrayList<IContextListener> listeners = new ArrayList<IContextListener>();

	public Context() {
		Timer t = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if (filteredCardList != null) {
					practiceCardList = Utils.applySpacedRepetitionFilter(filteredCardList);
				}
			}
		};
		t.scheduleAtFixedRate(task, 0, 1000); // used to update the practice card list every second so as to not do to
												// many unnecessary calculations
	}

	public FlashcardDeck getDeck() {
		return deck;
	}

	public void setDeck(FlashcardDeck deck) {
		this.deck = deck;
		filteredCardList = Arrays.asList(deck.getCards().values().toArray(new Flashcard[0]));
		fireDeckChanged();
	}

	public File getDeckFile() {
		return deckFile;
	}

	public void setDeckFile(File deckFile) {
		this.deckFile = deckFile;
	}

	public String getFilterString() {
		return filterString;
	}

	public void setFilterString(String filterString) {
		this.filterString = filterString;
		if (deck != null) {
			filteredCardList = queryHelper.query(deck.getCards().values(), filterString);
		} else {
			filteredCardList = new ArrayList<Flashcard>();
		}
		fireCardFilterUpdated(filterString);
	}

	public QueryHelper<Flashcard> getQueryHelper() {
		return queryHelper;
	}

	public void setQueryHelper(QueryHelper<Flashcard> queryHelper) {
		this.queryHelper = queryHelper;
	}

	public List<Flashcard> getFilteredCardList() {
		return filteredCardList;
	}

	public List<Flashcard> getPracticeCardList() {
		return practiceCardList;
	}

	public void addContextListener(IContextListener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	public void removeContextListener(IContextListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Marks that the current flashcard deck differs from the one saved on disk.
	 */
	public void markDirty() {
		isDirty = true;
	}

	/**
	 * Marks that the current flashcard deck no longer differs from the one saved on
	 * disk.
	 */
	public void markClean() {
		isDirty = false;
	}

	/**
	 * Gets whether or not the current flashcard deck differs from the one on disk.
	 * 
	 * @return {@code true} if the flashcard deck differs from the one on disk,
	 *         {@code false} otherwise
	 */
	public boolean isDirty() {
		return isDirty;
	}

	public void fireDeckChanged() {
		markDirty();
		for (IContextListener listener : listeners) {
			listener.deckChanged(this);
		}
	}

	public void fireCardUpdated(Flashcard card) {
		markDirty();
		for (IContextListener listener : listeners) {
			listener.cardUpdated(this, card);
		}
	}

	public void fireCardFilterUpdated(String filterString) {
		for (IContextListener listener : listeners) {
			listener.cardFilterUpdated(this, filterString);
		}
	}

}
