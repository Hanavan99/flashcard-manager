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

import com.github.hanavan99.flashcards.context.Context;
import com.github.hanavan99.flashcards.model.Tag;

public class TagEditorWindow extends Window {

	public static final String WINDOW_TITLE = "Tag Editor (%s)";

	private final Context context;
	private JList<Tag> tagList;
	private JMenuBar menu;
	private JMenu editMenu;

	public TagEditorWindow(Context context, Window parent) {
		super(String.format(WINDOW_TITLE, context.getDeck().getName()), 300, 500, parent);
		this.context = context;
		frame.setLayout(new GridLayout());

		menu = new JMenuBar();
		frame.setJMenuBar(menu);

		editMenu = new JMenu("Edit");
		menu.add(editMenu);

		JMenuItem addTagMenuItem = new JMenuItem("Add Tag");
		addTagMenuItem.addActionListener((_e) -> {
			if (context.getDeck() != null) {
				String value = JOptionPane.showInputDialog(frame, "Enter the name of the tag:");
				Tag tag = new Tag(UUID.randomUUID(), value, "[No Description]");
				context.getDeck().getTags().put(tag.getID(), tag);
				context.fireDeckChanged();
				updateTagList();
			} else {
				JOptionPane.showMessageDialog(frame, "No deck is loaded.");
			}
		});
		editMenu.add(addTagMenuItem);

		JMenuItem editTagMenuItem = new JMenuItem("Edit Tag");
		editTagMenuItem.addActionListener((_e) -> {
			Tag selected = tagList.getSelectedValue();
			if (selected != null) {
				selected.setName(JOptionPane.showInputDialog(frame, "Edit the name of this tag:", selected.getName()));
				context.fireDeckChanged();
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
		for (Tag t : context.getDeck().getTags().values()) {
			model.addElement(t);
		}
		frame.repaint();
	}

}
