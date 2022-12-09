package org.ingomohr.jira.model;

public record IssueType(String self, String id, String description, String name, boolean subtask) {

}
