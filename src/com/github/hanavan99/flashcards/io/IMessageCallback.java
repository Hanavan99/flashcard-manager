package com.github.hanavan99.flashcards.io;

/**
 * Provides an interface between a file handler and the calling application to
 * display messages and request input from the user.
 * 
 * @author Hanavan Kuhn
 *
 */
public interface IMessageCallback {

	/**
	 * Prompts the user to answer a yes/no question with the provided message.
	 * 
	 * @param message the message to show to the user
	 * @return {@code true} if the user selected the "yes" option, {@code false}
	 *         otherwise (if the user clicked any other button or closed the window)
	 */
	public boolean confirm(String message);

	/**
	 * Shows a message to the user.
	 * 
	 * @param message the message to show
	 */
	public void message(String message);

}
