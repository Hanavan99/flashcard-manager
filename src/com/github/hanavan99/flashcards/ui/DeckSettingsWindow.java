package com.github.hanavan99.flashcards.ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.github.hanavan99.flashcards.model.FlashcardDeck;

public class DeckSettingsWindow extends Window {

	private JTextField idText;
	private JTextField nameText;
	private JTextField creatorText;
	private JTextArea descriptionText;

	public DeckSettingsWindow(FlashcardDeck deck, Window parent) {
		super("Deck Settings: " + deck.getName(), 420, 500, parent);
		frame.setResizable(false);

		createLabel("Deck ID:", 10, 10);
		idText = new JTextField(deck.getID().toString());
		idText.setBounds(100, 10, 300, 20);
		idText.setEditable(false);
		frame.add(idText);

		createLabel("Name:", 10, 40);
		nameText = new JTextField(deck.getName());
		nameText.setBounds(100, 40, 300, 20);
		frame.add(nameText);

		createLabel("Creator:", 10, 70);
		creatorText = new JTextField(deck.getCreator());
		creatorText.setBounds(100, 70, 300, 20);
		frame.add(creatorText);

		createLabel("Description:", 10, 100);
		descriptionText = new JTextArea(deck.getDescription());
		JScrollPane dsc = new JScrollPane(descriptionText);
		dsc.setBounds(100, 100, 300, 200);
		frame.add(dsc);

		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				deck.setName(nameText.getText());
				deck.setCreator(creatorText.getText());
				deck.setDescription(descriptionText.getText());
			}

		});
	}

}
