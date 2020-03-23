package com.github.hanavan99.flashcards.model;

import java.util.UUID;

public class Tag {

	private UUID id;
	private String name;
	private String description;

	public Tag(UUID id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
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

	@Override
	public String toString() {
		return name;
	}

}
