package com.github.hanavan99.flashcards;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;
import java.util.function.Function;

import javax.swing.JOptionPane;

import com.github.hanavan99.flashcards.io.FileCardHandler;
import com.github.hanavan99.flashcards.io.ICardHandler;
import com.github.hanavan99.flashcards.model.Flashcard;
import com.github.hanavan99.flashcards.model.FlashcardDeck;
import com.github.hanavan99.flashcards.model.Tag;
import com.github.hanavan99.flashcards.ui.CardWindow;

public class Main {

	private static HashMap<String, Function<String[], Boolean>> commandMap = new HashMap<String, Function<String[], Boolean>>();
	private static CardWindow window;
	private static ICardHandler handler;

	public static void main(String[] args) {
		window = new CardWindow();
		window.setFlashcardDeck(new FlashcardDeck(UUID.randomUUID(), "Untitled Deck", "[No Description]", "No Creator", Calendar.getInstance().getTime(), new HashMap<UUID, Tag>(), new HashMap<UUID, Flashcard>()));
		handler = new FileCardHandler();

		// register command handlers
		commandMap.put("exit", Main::exit);
		commandMap.put("quit", Main::exit);
		commandMap.put("help", Main::help);
		commandMap.put("save", Main::save);
		commandMap.put("load", Main::load);
		commandMap.put("addcard", Main::addCard);
		commandMap.put("list", Main::listCards);

		window.show();

		// Scanner s = new Scanner(System.in);
		// System.out.println("Welcome to Flashcard Manager v1.0!");
		// loop: while (true) {
		// System.out.print("> ");
		// String[] cmd = s.nextLine().split(" ");
		// Function<String[], Boolean> commandHandler = commandMap.get(cmd[0]);
		// if (cmd.length >= 1 && commandHandler != null) {
		// if (!commandHandler.apply(Arrays.copyOfRange(cmd, 1, cmd.length))) {
		// break loop;
		// }
		// } else {
		// System.out.println("Invalid command. Type \"help\" for a list of
		// commands.");
		// }
		// }
		//
		// System.out.println("Exiting...");
		// s.close();
		// window.close();
	}

	public static boolean load(String[] args) {
		if (args.length > 0) {
			try {
				window.setFlashcardDeck(handler.readFlashcards(new File(args[0])));
			} catch (IOException e) {
				System.out.println("Failed to load flashcard file: " + e.getMessage());
			}
		} else {
			System.out.println("Usage: load [filename]");
		}
		return true;
	}

	public static boolean save(String[] args) {
		if (args.length >= 1) {
			try {
				handler.writeFlashcards(new File(args[0]), window.getFlashcardDeck());
			} catch (IOException e) {
				System.out.println("Failed to save flashcard file: " + e.getMessage());
			}
		} else {
			System.out.println("Usage: save [filename]");
		}
		return true;
	}

	public static boolean addCard(String[] args) {
		if (args.length >= 0) {
			Date now = Calendar.getInstance().getTime();
			String front = JOptionPane.showInputDialog(window.getFrame(), "Enter the text on the front of the card:");
			String back = JOptionPane.showInputDialog(window.getFrame(), "Enter the text on the back of the card:");
			Flashcard card = new Flashcard(UUID.randomUUID(), front, back, new ArrayList<Tag>(), now, now, 0);
			window.getFlashcardDeck().getCards().put(card.getID(), card);
			System.out.println("Card added");
		} else {
			System.out.println("Usage: addcard");
		}
		return true;
	}

	public static boolean listCards(String[] args) {
		for (Flashcard card : window.getFlashcardDeck().getCards().values()) {
			System.out.printf("%s\t\t\t%s\n", card.getFront(), card.getBack());
		}
		return true;
	}

	public static boolean help(String[] args) {
		System.out.println("Available commands:");
		for (String s : commandMap.keySet()) {
			System.out.println(s);
		}
		return true;
	}

	public static boolean exit(String[] args) {
		return false;
	}

}
