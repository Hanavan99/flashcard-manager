package com.github.hanavan99.flashcards.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import com.github.hanavan99.flashcards.model.Flashcard;
import com.github.hanavan99.flashcards.model.FlashcardDeck;
import com.github.hanavan99.flashcards.model.Tag;

public class FileCardHandler implements ICardHandler {

	@Override
	public FlashcardDeck readFlashcards(File file) throws IOException {
		DataInputStream in = new DataInputStream(new FileInputStream(file));

		// read flashcard deck information
		HashMap<UUID, Tag> tags = new HashMap<UUID, Tag>();
		HashMap<UUID, Flashcard> cards = new HashMap<UUID, Flashcard>();
		FlashcardDeck deck = new FlashcardDeck(readUUID(in), in.readUTF(), in.readUTF(), in.readUTF(), readDate(in), tags, cards);

		// read in tags
		int tagCount = in.readInt();
		for (int i = 0; i < tagCount; i++) {
			Tag tag = new Tag(readUUID(in), in.readUTF(), in.readUTF());
			tags.put(tag.getID(), tag);
		}

		// read in flashcards
		int cardCount = in.readInt();
		for (int i = 0; i < cardCount; i++) {

			// read in tags for flashcard
			ArrayList<Tag> cardTags = new ArrayList<Tag>();
			int cardTagCount = in.readInt();
			for (int j = 0; j < cardTagCount; j++) {
				cardTags.add(tags.get(readUUID(in)));
			}
			Flashcard card = new Flashcard(readUUID(in), in.readUTF(), in.readUTF(), cardTags, readDate(in), readDate(in), in.readInt());
			cards.put(card.getID(), card);
		}

		in.close();
		return deck;
	}

	@Override
	public void writeFlashcards(File file, FlashcardDeck deck) throws IOException {
		DataOutputStream out = new DataOutputStream(new FileOutputStream(file));

		// write flashcard deck information
		writeUUID(out, deck.getID());
		out.writeUTF(deck.getName());
		out.writeUTF(deck.getDescription());
		out.writeUTF(deck.getCreator());
		writeDate(out, deck.getCreationDate());

		// write tags
		out.writeInt(deck.getTags().size());
		for (Tag t : deck.getTags().values()) {
			writeUUID(out, t.getID());
			out.writeUTF(t.getName());
			out.writeUTF(t.getDescription());
		}

		// write cards
		out.writeInt(deck.getCards().size());
		for (Flashcard card : deck.getCards().values()) {
			out.writeInt(card.getTags().size());
			for (Tag t : card.getTags()) {
				writeUUID(out, t.getID());
			}
			writeUUID(out, card.getID());
			out.writeUTF(card.getFront());
			out.writeUTF(card.getBack());
			writeDate(out, card.getLastViewed());
			writeDate(out, card.getNewViewDate());
			out.writeInt(card.getViewCount());
		}
		out.close();
	}

	private UUID readUUID(DataInputStream in) throws IOException {
		return new UUID(in.readLong(), in.readLong());
	}

	private void writeUUID(DataOutputStream out, UUID id) throws IOException {
		out.writeLong(id.getMostSignificantBits());
		out.writeLong(id.getLeastSignificantBits());
	}

	private Date readDate(DataInputStream in) throws IOException {
		return new Date(in.readLong());
	}

	private void writeDate(DataOutputStream out, Date date) throws IOException {
		out.writeLong(date.getTime());
	}

}
