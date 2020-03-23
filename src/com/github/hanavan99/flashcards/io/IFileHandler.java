package com.github.hanavan99.flashcards.io;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import com.github.hanavan99.flashcards.model.FlashcardDeck;

/**
 * This interface provides a skeleton for saving and loading flashcard decks to
 * and from the file system.
 * 
 * @author Hanavan Kuhn
 *
 */
public interface IFileHandler {

//	/**
//	 * Gets the {@code FileFilter} or {@code FileNameExtensionFilter} that is used
//	 * to filter files that may be able to be opened by this file handler. This
//	 * filter should not open files and inspect them, it should only examine the
//	 * file name or whether or not it is a directory.
//	 * 
//	 * @return the filter
//	 */
//	public FileFilter getFileNameFilter();

	/**
	 * Gets the friendly name of this handler, which will be displayed to the user.
	 * 
	 * @return the name
	 */
	public String getHandlerName();

	/**
	 * Gets a {@code JFileChooser} that can be used to query the user when opening
	 * and saving flashcard decks.
	 * 
	 * @param file the file to select as the default in the chooser
	 * @return the file chooser
	 */
	public JFileChooser getFileChooser(File file);

	/**
	 * Determines if the file or directory selected by the user can be read by this
	 * file handler. This function may only inspect the file extension, but it is
	 * perfectly acceptable to open a file to check for a version number or other
	 * indicators that show the file is of the correct type. This function does not
	 * need to verify that the file is completely valid, but should just look for
	 * indicators that show that it is highly likely that the file can be opened
	 * with this file handler.
	 * 
	 * @param file the file or directory to check
	 * @return {@code true} if the file can be opened, {@code false} otherwise
	 * @throws IOException if there was an error reading the file
	 */
	public boolean isValidFile(File file) throws IOException;

	/**
	 * Reads the flashcards from the given file or directory.
	 * 
	 * @param file            the file or directory to read from
	 * @param messageCallback a callback that is used to communicate with the caller
	 *                        or the user
	 * @return the flashcard deck, or {@code null} if there was an error
	 * @throws IOException if there was a problem reading the deck file
	 */
	public FlashcardDeck readFlashcards(File file, IMessageCallback messageCallback) throws IOException;

	/**
	 * Writes the flashcards to the given file or directory.
	 * 
	 * @param file            the file or directory to write to
	 * @param deck            the flashcard deck
	 * @param messageCallback a callback that is used to communicate with the caller
	 *                        or the user
	 * @return {@code true} if writing was successful, {@code false} otherwise
	 * @throws IOException if there was a problem writing the deck file
	 */
	public boolean writeFlashcards(File file, FlashcardDeck deck, IMessageCallback messageCallback) throws IOException;

}
