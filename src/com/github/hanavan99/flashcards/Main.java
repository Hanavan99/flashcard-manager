package com.github.hanavan99.flashcards;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import com.github.hanavan99.flashcards.model.Flashcard;
import com.github.hanavan99.flashcards.model.FlashcardDeck;
import com.github.hanavan99.flashcards.model.Tag;
import com.github.hanavan99.flashcards.ui.CardWindow;

public class Main {

	private static CardWindow window;

	public static void main(String[] args) {
		window = new CardWindow();
		window.setFlashcardDeck(new FlashcardDeck(UUID.randomUUID(), "Untitled Deck", "[No Description]", "No Creator", Calendar.getInstance().getTime(), new HashMap<UUID, Tag>(), new HashMap<UUID, Flashcard>()));
		window.show();
	}

}
