package org.ingomohr.jira.model;

public record Status(String self, String name, String id, StatusCategory statusCategory) {

}
