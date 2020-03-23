package com.github.hanavan99.flashcards;

import com.github.hanavan99.flashcards.context.Context;
import com.github.hanavan99.flashcards.io.DirectoryHandlerV1;
import com.github.hanavan99.flashcards.io.FileCardHandler;
import com.github.hanavan99.flashcards.ui.CardWindow;
import com.github.hanavan99.flashcards.util.CardQueryHelper;

public class Main {

	private static CardWindow window;

	public static void main(String[] args) {
		Context ctx = new Context();
		ctx.setQueryHelper(new CardQueryHelper());
		ctx.setFilterString("");
		ctx.registerFileHandler(new FileCardHandler());
		ctx.registerFileHandler(new DirectoryHandlerV1());

		window = new CardWindow(ctx);
		window.show();
	}

}
