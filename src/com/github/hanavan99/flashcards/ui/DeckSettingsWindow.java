package com.github.hanavan99.flashcards.ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.github.hanavan99.flashcards.context.Context;

public class DeckSettingsWindow extends Window {

	public static final String WINDOW_TITLE = "Deck Settings (%s)";

	private JTextField idText;
	private JTextField nameText;
	private JTextField creatorText;
	private JTextArea descriptionText;

	public DeckSettingsWindow(Context context, Window parent) {
		super(String.format(WINDOW_TITLE, context.getDeck().getName()), 420, 500, parent);
		frame.setResizable(false);

		createLabel("Deck ID:", 10, 10);
		idText = new JTextField(context.getDeck().getID().toString());
		idText.setBounds(100, 10, 300, 20);
		idText.setEditable(false);
		frame.add(idText);

		createLabel("Name:", 10, 40);
		nameText = new JTextField(context.getDeck().getName());
		nameText.setBounds(100, 40, 300, 20);
		frame.add(nameText);

		createLabel("Creator:", 10, 70);
		creatorText = new JTextField(context.getDeck().getCreator());
		creatorText.setBounds(100, 70, 300, 20);
		frame.add(creatorText);

		createLabel("Description:", 10, 100);
		descriptionText = new JTextArea(context.getDeck().getDescription());
		JScrollPane dsc = new JScrollPane(descriptionText);
		dsc.setBounds(100, 100, 300, 200);
		frame.add(dsc);

		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				context.getDeck().setName(nameText.getText());
				context.getDeck().setCreator(creatorText.getText());
				context.getDeck().setDescription(descriptionText.getText());
				context.fireDeckChanged();
			}

		});
	}

}
