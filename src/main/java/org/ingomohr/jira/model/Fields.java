package org.ingomohr.jira.model;

import java.util.List;

public record Fields(Resolution resolution, String description, String summary, String dueDate, Status status,
		List<Component> components, User creator, User assignee, User reporter, IssueType issuetype, Project project,
		String updated, List<Version> versions) {

}
