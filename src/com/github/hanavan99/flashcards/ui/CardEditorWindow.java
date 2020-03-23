package com.github.hanavan99.flashcards.ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.github.hanavan99.flashcards.context.Context;
import com.github.hanavan99.flashcards.model.Flashcard;
import com.github.hanavan99.flashcards.model.Tag;

public class CardEditorWindow extends Window {

	private final Context context;
	private final Flashcard card;
	private JTextField idText;
	private JTextField frontText;
	private JTextArea backText;
	private JList<Tag> deckTagList;
	private JList<Tag> cardTagList;
	private JButton addTagToCardButton;
	private JButton removeTagFromCardButton;

	public CardEditorWindow(Context context, Flashcard card, Window parent) {
		super("Edit Card", 420, 670, parent);
		this.context = context;
		this.card = card;
		frame.setResizable(false);

		createLabel("Card ID:", 10, 10);
		idText = new JTextField(card.getID().toString());
		idText.setBounds(100, 10, 300, 20);
		idText.setEditable(false);
		frame.add(idText);

		createLabel("Front text:", 10, 40);
		frontText = new JTextField(card.getFront());
		frontText.setBounds(100, 40, 300, 20);
		frame.add(frontText);

		createLabel("Back text:", 10, 70);
		backText = new JTextArea(card.getBack());
		JScrollPane pane = new JScrollPane(backText);
		pane.setBounds(100, 70, 300, 200);
		frame.add(pane);

		createLabel("Tags:", 10, 275);
		deckTagList = new JList<Tag>(new DefaultListModel<Tag>());
		JScrollPane pane1 = new JScrollPane(deckTagList);
		pane1.setBounds(10, 300, 190, 300);
		frame.add(pane1);

		createLabel("Card Tags:", 210, 275);
		cardTagList = new JList<Tag>(new DefaultListModel<Tag>());
		JScrollPane pane2 = new JScrollPane(cardTagList);
		pane2.setBounds(210, 300, 190, 300);
		frame.add(pane2);

		addTagToCardButton = new JButton("Add");
		addTagToCardButton.setBounds(10, 610, 190, 20);
		addTagToCardButton.addActionListener((_e) -> {
			Tag selected = deckTagList.getSelectedValue();
			if (selected != null) {
				card.getTags().add(selected);
				updateTagLists();
			} else {
				JOptionPane.showMessageDialog(frame, "Please select a tag to add.");
			}
		});
		frame.add(addTagToCardButton);

		removeTagFromCardButton = new JButton("Remove");
		removeTagFromCardButton.setBounds(210, 610, 190, 20);
		removeTagFromCardButton.addActionListener((_e) -> {
			Tag selected = cardTagList.getSelectedValue();
			if (selected != null) {
				card.getTags().remove(selected);
				updateTagLists();
			} else {
				JOptionPane.showMessageDialog(frame, "Please select a tag to remove.");
			}
		});
		frame.add(removeTagFromCardButton);

		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				card.setFront(frontText.getText());
				card.setBack(backText.getText());
				context.fireDeckChanged();
				context.fireCardUpdated(card);
			}

		}); 

		updateTagLists();
	}

	private void updateTagLists() {
		DefaultListModel<Tag> deckTagModel = (DefaultListModel<Tag>) deckTagList.getModel();
		deckTagModel.clear();
		List<Tag> deckTags = sortCollection(context.getDeck().getTags().values(), (a, b) -> a.getName().compareTo(b.getName()), new Tag[0]);
		for (Tag t : deckTags) {
			if (!card.getTags().contains(t)) {
				deckTagModel.addElement(t);
			}
		}

		DefaultListModel<Tag> cardTagModel = (DefaultListModel<Tag>) cardTagList.getModel();
		cardTagModel.clear();
		List<Tag> cardTags = sortCollection(card.getTags(), (a, b) -> a.getName().compareTo(b.getName()), new Tag[0]);
		for (Tag t : cardTags) {
			cardTagModel.addElement(t);
		}
	}

	private <T> List<T> sortCollection(Collection<T> collection, Comparator<T> comp, T[] array) {
		List<T> list = Arrays.asList(collection.toArray(array));
		list.sort(comp);
		return list;
	}

}
