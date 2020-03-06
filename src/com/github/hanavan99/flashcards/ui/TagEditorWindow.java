package com.github.hanavan99.flashcards.ui;

import java.awt.GridLayout;
import java.util.UUID;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import com.github.hanavan99.flashcards.model.FlashcardDeck;
import com.github.hanavan99.flashcards.model.Tag;

public class TagEditorWindow extends Window {

	private final FlashcardDeck deck;
	private JList<Tag> tagList;
	private JMenuBar menu;
	private JMenu editMenu;

	public TagEditorWindow(FlashcardDeck deck, Window parent) {
		super("Tag Editor", 300, 500, parent);
		this.deck = deck;
		frame.setLayout(new GridLayout());

		menu = new JMenuBar();
		frame.setJMenuBar(menu);

		editMenu = new JMenu("Edit");
		menu.add(editMenu);

		JMenuItem addTagMenuItem = new JMenuItem("Add Tag");
		addTagMenuItem.addActionListener((_e) -> {
			String value = JOptionPane.showInputDialog(frame, "Enter the name of the tag:");
			Tag tag = new Tag(UUID.randomUUID(), value, "[No Description]");
			deck.getTags().put(tag.getID(), tag);
			updateTagList();
		});
		editMenu.add(addTagMenuItem);

		JMenuItem editTagMenuItem = new JMenuItem("Edit Tag");
		editTagMenuItem.addActionListener((_e) -> {
			Tag selected = tagList.getSelectedValue();
			if (selected != null) {
				selected.setName(JOptionPane.showInputDialog(frame, "Enter the name of this tag:", selected.getName()));
			} else {
				JOptionPane.showMessageDialog(frame, "Please select a tag to edit.");
			}
			updateTagList();
		});
		editMenu.add(editTagMenuItem);

		tagList = new JList<Tag>(new DefaultListModel<Tag>());
		frame.add(new JScrollPane(tagList));
		updateTagList();
	}

	private void updateTagList() {
		DefaultListModel<Tag> model = (DefaultListModel<Tag>) tagList.getModel();
		model.clear();
		for (Tag t : deck.getTags().values()) {
			model.addElement(t);
		}
		frame.repaint();
	}

}
