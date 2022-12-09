package org.ingomohr.jira.model;

public record User(String self, String name, String key, String emailAddress, String displayName, String active,
		String timeZone) {

}
