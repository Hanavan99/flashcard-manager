package com.github.hanavan99.flashcards.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.github.hanavan99.flashcards.model.Flashcard;
import com.github.hanavan99.flashcards.model.FlashcardDeck;
import com.github.hanavan99.flashcards.model.Tag;

public class Utils {

	public static FlashcardDeck createNewDeck() {
		Date now = Calendar.getInstance().getTime();
		return new FlashcardDeck(UUID.randomUUID(), "Untitled Deck", "[No Description]", "[No Author]", now,
				new HashMap<UUID, Tag>(), new HashMap<UUID, Flashcard>());
	}

	public static List<Flashcard> applySpacedRepetitionFilter(Collection<Flashcard> cards) {
		ArrayList<Flashcard> cardList = new ArrayList<Flashcard>();

		// add cards to be viewed to list
		Date now = Calendar.getInstance().getTime();
		for (Flashcard card : cards) {
			if (card.getNewViewDate().before(now)) {
				cardList.add(card);
			}
		}

		// sort cards by least viewed
		cardList.sort((a, b) -> {
			return ((Integer) a.getViewCount()).compareTo(b.getViewCount());
		});
		return cardList;
	}

	public static Date computeNewCardDate(Date old, int viewCount, double scaleFactor) {
		long time = old.getTime();

		time += (long) (1000 * Math.pow(1.7, 3 + 4 * viewCount) * scaleFactor);

		return new Date(time);
	}

	public static String dateDiffToString(Date base, Date now) {
		long diff = Math.abs(now.getTime() - base.getTime());
		if (diff < 1000) { // under 1 second
			return diff + "ms";
		}

		diff /= 1000;
		if (diff < 60) {
			return diff + "s";
		}

		diff /= 60;
		if (diff < 60) {
			return diff + "m";
		}

		diff /= 60;
		if (diff < 24) {
			return diff + "hr";
		}

		diff /= 24;
		if (diff < 7) {
			return diff + "d";
		}

		diff /= 7;
		if (diff < 52) {
			return diff + "w";
		}

		diff /= 52;
		return diff + "y";
	}

}
