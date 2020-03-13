package com.github.hanavan99.flashcards;

import com.github.hanavan99.flashcards.context.Context;
import com.github.hanavan99.flashcards.context.IContextListener;
import com.github.hanavan99.flashcards.model.Flashcard;
import com.github.hanavan99.flashcards.ui.CardWindow;
import com.github.hanavan99.flashcards.util.CardQueryHelper;

public class Main {

	private static CardWindow window;

	public static void main(String[] args) {
		Context ctx = new Context();
		ctx.setQueryHelper(new CardQueryHelper());
		ctx.setFilterString("");
		ctx.addContextListener(new IContextListener() {

			@Override
			public void deckChanged(Context context) {
				// TODO Auto-generated method stub
				System.out.println("Deck changed");
			}

			@Override
			public void cardUpdated(Context context, Flashcard card) {
				// TODO Auto-generated method stub
				System.out.println("Card updated");
			}

			@Override
			public void cardFilterUpdated(Context context, String filterString) {
				// TODO Auto-generated method stub
				System.out.println("Card filter updated: " + filterString);
			}
			
		});
		
		window = new CardWindow(ctx);
		window.show();
	}

}
