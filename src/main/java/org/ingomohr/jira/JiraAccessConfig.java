package org.ingomohr.jira;

/**
 * Config to access a Jira server.
 * 
 * @author ingomohr
 */
public record JiraAccessConfig(String serverUrl, String user, String password) {

}
