package org.ingomohr.jira.model;

/**
 * A version in Jira.
 * 
 * @author ingomohr
 */
public record Version(String self, String id, String projectId, String name, String description, boolean released,
		boolean archived, String releaseDate) {

}
