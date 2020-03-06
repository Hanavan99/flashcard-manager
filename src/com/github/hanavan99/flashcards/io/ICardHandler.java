package com.github.hanavan99.flashcards.io;

import java.io.File;
import java.io.IOException;

import com.github.hanavan99.flashcards.model.FlashcardDeck;

public interface ICardHandler {

	public FlashcardDeck readFlashcards(File file) throws IOException;

	public void writeFlashcards(File file, FlashcardDeck deck) throws IOException;

}
