package com.github.hanavan99.flashcards.model;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class FlashcardDeck {

	private UUID id;
	private String name;
	private String description;
	private String creator;
	private Date creationDate;
	private Map<UUID, Tag> tags;
	private Map<UUID, Flashcard> cards;

	public FlashcardDeck(UUID id, String name, String description, String creator, Date creationDate, Map<UUID, Tag> tags, Map<UUID, Flashcard> cards) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.creator = creator;
		this.creationDate = creationDate;
		this.tags = tags;
		this.cards = cards;
	}

	public UUID getID() {
		return id;
	}

	public void setID(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Map<UUID, Tag> getTags() {
		return tags;
	}

	public void setTags(Map<UUID, Tag> tags) {
		this.tags = tags;
	}

	public Map<UUID, Flashcard> getCards() {
		return cards;
	}

	public void setCards(Map<UUID, Flashcard> cards) {
		this.cards = cards;
	}

}
