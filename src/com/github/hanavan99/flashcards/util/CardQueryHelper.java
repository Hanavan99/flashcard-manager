package com.github.hanavan99.flashcards.util;

import com.github.hanavan99.flashcards.model.Flashcard;
import com.github.hanavan99.flashcards.model.Tag;

public class CardQueryHelper extends QueryHelper<Flashcard> {

	public CardQueryHelper() {
		super();

		addFilterFunction("front", (card, arg) -> {
			return card.getFront().toLowerCase().contains(arg.toLowerCase());
		});
		addFilterFunction("frontexact", (card, arg) -> {
			return card.getFront().toLowerCase().equals(arg.toLowerCase());
		});
		addFilterFunction("back", (card, arg) -> {
			return card.getBack().toLowerCase().contains(arg.toLowerCase());
		});
		addFilterFunction("backexact", (card, arg) -> {
			return card.getBack().toLowerCase().equals(arg.toLowerCase());
		});
		addFilterFunction("tag", (card, arg) -> {
			for (Tag t : card.getTags()) {
				if (t.getName().toLowerCase().contains(arg)) {
					return true;
				}
			}
			return false;
		});

		addSortFunction("front", (a, b) -> {
			return a.getFront().compareTo(b.getFront());
		});
		addSortFunction("back", (a, b) -> {
			return a.getBack().compareTo(b.getBack());
		});
		addSortFunction("tagcount", (a, b) -> {
			return -((Integer) a.getTags().size()).compareTo(b.getTags().size()); // sort most at top
		});
		addSortFunction("lastviewed", (a, b) -> {
			return -a.getLastViewDate().compareTo(b.getLastViewDate()); // sort latest at the top
		});
		addSortFunction("viewcount", (a, b) -> {
			return -((Integer) a.getViewCount()).compareTo(b.getViewCount()); // sort most at top
		});
	}

}
