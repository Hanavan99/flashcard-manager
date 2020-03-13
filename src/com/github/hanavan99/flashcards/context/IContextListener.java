package com.github.hanavan99.flashcards.context;

import com.github.hanavan99.flashcards.model.Flashcard;

public interface IContextListener {

	/**
	 * Called when the deck has been changed, whether by the user loading a new
	 * deck, or the properties of the deck have changed.
	 * 
	 * @param context the context
	 */
	public void deckChanged(Context context);

	/**
	 * Called when a card has been updated.
	 * 
	 * @param context the context
	 * @param card    the card that was updated
	 */
	public void cardUpdated(Context context, Flashcard card);

	/**
	 * Called when the card filter string has changed.
	 * 
	 * @param context      the context
	 * @param filterString the new filter string
	 */
	public void cardFilterUpdated(Context context, String filterString);

}
