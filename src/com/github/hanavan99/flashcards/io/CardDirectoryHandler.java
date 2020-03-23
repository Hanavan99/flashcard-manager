package com.github.hanavan99.flashcards.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;

import javax.swing.JFileChooser;

import com.github.hanavan99.flashcards.model.Flashcard;
import com.github.hanavan99.flashcards.model.FlashcardDeck;
import com.github.hanavan99.flashcards.model.Tag;

public abstract class CardDirectoryHandler implements IFileHandler {

	/**
	 * This is the name of the file that contains information about the deck.
	 */
	public static final String DECK_FILE_NAME = "deck.txt";
	public static final String DECK_VERSION = "reader-version";
	public static final String TAG_FILE_PREFIX = "tag";
	public static final String CARD_FILE_PREFIX = "card";

	@Override
	public String getHandlerName() {
		return "Deck Directory (v" + getVersion() + ")";
	}

	@Override
	public JFileChooser getFileChooser(File file) {
		JFileChooser chooser = new JFileChooser(file);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		return chooser;
	}

	@Override
	public final boolean isValidFile(File file) throws IOException {
		File deckFile = new File(file, DECK_FILE_NAME);
		try {
			Properties props = readProperties(deckFile);
			return props.containsKey(DECK_VERSION) && Integer.valueOf(props.getProperty(DECK_VERSION)) == getVersion();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public final FlashcardDeck readFlashcards(File file, IMessageCallback messageCallback) throws IOException {
		int cardErrorCount = 0;
		int tagErrorCount = 0;

		// read deck file
		try {
			Properties props = readProperties(new File(file, DECK_FILE_NAME));
			FlashcardDeck deck = readDeck(props);

			// read tag files
			for (File f : file.listFiles()) {
				String[] name = f.getName().split("-", 2);
				if (name.length >= 2 && name[0].equals(TAG_FILE_PREFIX)) {
					try {
						UUID id = UUID.fromString(name[1].split("\\.")[0]);
						Properties tagProps = readProperties(f);
						Tag tag = readTag(tagProps, id);
						deck.getTags().put(id, tag);
					} catch (Exception e) {
						if (tagErrorCount == 0) {
							e.printStackTrace();
							messageCallback.message("Failed to read tag file: " + e.getMessage());
						}
						tagErrorCount++;
					}
				}
			}

			// read card files
			for (File f : file.listFiles()) {
				String[] name = f.getName().split("-", 2);
				if (name.length >= 2 && name[0].equals(CARD_FILE_PREFIX)) {
					try {
						UUID id = UUID.fromString(name[1].split("\\.")[0]);
						Properties tagProps = readProperties(f);
						Flashcard card = readFlashcard(tagProps, deck.getTags(), id);
						deck.getCards().put(id, card);
					} catch (Exception e) {
						if (cardErrorCount == 0) {
							e.printStackTrace();
							messageCallback.message("Failed to read card file: " + e.getMessage());
						}
						cardErrorCount++;
					}
				}
			}

			// print errors if necessary
			if (cardErrorCount > 0 || tagErrorCount > 0) {
				messageCallback.message("Opened deck but failed to read " + cardErrorCount + " card files and "
						+ tagErrorCount + " tag files.");
			}

			return deck;
		} catch (Exception e) {
			e.printStackTrace();
			messageCallback.message("Failed to read deck file: " + e.getMessage());
			return null;
		}
	}

	@Override
	public final boolean writeFlashcards(File file, FlashcardDeck deck, IMessageCallback messageCallback)
			throws IOException {
		int cardErrorCount = 0;
		int tagErrorCount = 0;

		// build deck file
		try {
			Properties props = writeDeck(deck);
			props.put(DECK_VERSION, String.valueOf(getVersion()));
			writeProperties(props, "This file stores information about the entire flashcard deck.",
					new File(file, DECK_FILE_NAME));
		} catch (Exception e) {
			e.printStackTrace();
			messageCallback.message("Failed to write deck file: " + e.getMessage());
			return false;
		}

		// build tag files
		for (Entry<UUID, Tag> entry : deck.getTags().entrySet()) {
			try {
				Properties props = writeTag(entry.getValue());
				writeProperties(props, "This file stores information about a card tag.",
						new File(file, TAG_FILE_PREFIX + "-" + entry.getKey().toString() + ".txt"));
			} catch (Exception e) {
				if (tagErrorCount == 0) {
					e.printStackTrace();
					messageCallback.message("Failed to write tag file: " + e.getMessage());
				}
				tagErrorCount++;
			}
		}

		// build card files
		for (Entry<UUID, Flashcard> entry : deck.getCards().entrySet()) {
			try {
				Properties props = writeFlashcard(entry.getValue());
				writeProperties(props, "This file stores information about a flashcard.",
						new File(file, CARD_FILE_PREFIX + "-" + entry.getKey().toString() + ".txt"));
			} catch (Exception e) {
				if (cardErrorCount == 0) {
					e.printStackTrace();
					messageCallback.message("Failed to write flashcard file: " + e.getMessage());
				}
				cardErrorCount++;
			}
		}

		// print errors if necessary
		if (cardErrorCount > 0 || tagErrorCount > 0) {
			messageCallback.message("Saved deck but failed to write " + cardErrorCount + " card files and "
					+ tagErrorCount + " tag files.");
		}

		return true;
	}

	private Properties readProperties(File file) throws IOException {
		InputStream in = new FileInputStream(file);
		Properties props = new Properties();
		props.load(in);
		in.close();

		return props;
	}

	private void writeProperties(Properties props, String comments, File file) throws IOException {
		OutputStream out = new FileOutputStream(file);
		props.store(out, comments);
		out.close();
	}

	public abstract int getVersion();

	public abstract Tag readTag(Properties props, UUID id) throws Exception;

	public abstract Properties writeTag(Tag tag) throws Exception;

	public abstract FlashcardDeck readDeck(Properties props) throws Exception;

	public abstract Properties writeDeck(FlashcardDeck deck) throws Exception;

	public abstract Flashcard readFlashcard(Properties props, Map<UUID, Tag> deckTags, UUID id) throws Exception;

	public abstract Properties writeFlashcard(Flashcard card) throws Exception;

}
