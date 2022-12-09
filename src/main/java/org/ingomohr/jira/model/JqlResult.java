package org.ingomohr.jira.model;

import java.util.List;

public record JqlResult(String expand, int startAt, int maxResults, int total, List<Issue> issues) {

}
