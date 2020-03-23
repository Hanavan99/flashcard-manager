package com.github.hanavan99.flashcards.io;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import com.github.hanavan99.flashcards.model.Flashcard;
import com.github.hanavan99.flashcards.model.FlashcardDeck;
import com.github.hanavan99.flashcards.model.Tag;

public class DirectoryHandlerV1 extends CardDirectoryHandler {

	public static final String DECK_NAME = "name";
	public static final String DECK_DESCRIPTION = "description";
	public static final String DECK_CREATOR = "creator";
	public static final String DECK_CREATION_DATE = "creation-date";
	public static final String DECK_ID = "id";

	public static final String TAG_NAME = "name";
	public static final String TAG_DESCRIPTION = "description";

	public static final String CARD_FRONT = "front";
	public static final String CARD_BACK = "back";
	public static final String CARD_VIEW_COUNT = "view-count";
	public static final String CARD_LAST_VIEW_DATE = "last-view-date";
	public static final String CARD_NEXT_VIEW_DATE = "next-view-date";
	public static final String CARD_TAG_LIST = "tags";

	@Override
	public int getVersion() {
		return 1;
	}

	@Override
	public Tag readTag(Properties props, UUID id) throws Exception {
		return new Tag(id, props.getProperty(TAG_NAME), props.getProperty(TAG_DESCRIPTION));
	}

	@Override
	public Properties writeTag(Tag tag) throws Exception {
		Properties props = new Properties();
		props.put(TAG_NAME, tag.getName());
		props.put(TAG_DESCRIPTION, tag.getDescription());
		return props;
	}

	@Override
	public FlashcardDeck readDeck(Properties props) throws Exception {
		return new FlashcardDeck(UUID.fromString(props.getProperty(DECK_ID)), props.getProperty(DECK_NAME),
				props.getProperty(DECK_DESCRIPTION), props.getProperty(DECK_CREATOR),
				new Date(Long.valueOf(props.getProperty(DECK_CREATION_DATE))), new HashMap<UUID, Tag>(),
				new HashMap<UUID, Flashcard>());
	}

	@Override
	public Properties writeDeck(FlashcardDeck deck) throws Exception {
		Properties props = new Properties();
		props.put(DECK_NAME, deck.getName());
		props.put(DECK_DESCRIPTION, deck.getDescription());
		props.put(DECK_CREATOR, deck.getCreator());
		props.put(DECK_CREATION_DATE, String.valueOf(deck.getCreationDate().getTime()));
		props.put(DECK_ID, deck.getID().toString());
		return props;
	}

	@Override
	public Flashcard readFlashcard(Properties props, Map<UUID, Tag> deckTags, UUID id) throws Exception {
		ArrayList<Tag> tags = new ArrayList<Tag>();
		String[] tagids = props.getProperty(CARD_TAG_LIST).split(",");
		for (String tagid : tagids) {
			if (tagid.length() > 0) {
				tags.add(deckTags.get(UUID.fromString(tagid)));
			}
		}
		return new Flashcard(id, props.getProperty(CARD_FRONT), props.getProperty(CARD_BACK), tags,
				new Date(Long.valueOf(props.getProperty(CARD_LAST_VIEW_DATE))),
				new Date(Long.valueOf(props.getProperty(CARD_NEXT_VIEW_DATE))),
				Integer.valueOf(props.getProperty(CARD_VIEW_COUNT)));
	}

	@Override
	public Properties writeFlashcard(Flashcard card) throws Exception {
		Properties props = new Properties();
		props.put(CARD_FRONT, card.getFront());
		props.put(CARD_BACK, card.getBack());
		props.put(CARD_VIEW_COUNT, String.valueOf(card.getViewCount()));
		props.put(CARD_LAST_VIEW_DATE, String.valueOf(card.getLastViewDate().getTime()));
		props.put(CARD_NEXT_VIEW_DATE, String.valueOf(card.getNewViewDate().getTime()));
		StringBuilder sb = new StringBuilder();
		for (Tag t : card.getTags()) {
			sb.append(t.getID().toString());
			sb.append(',');
		}
		if (sb.length() > 0) {
			sb.setLength(sb.length() - 1); // remove last comma
		}
		props.put(CARD_TAG_LIST, sb.toString());
		return props;
	}

}
