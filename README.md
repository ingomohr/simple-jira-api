![Build](https://github.com/ingomohr/simple-jira-api/actions/workflows/mvn-build-main.yml/badge.svg?branch=main)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
## What is This?
Simple API to read information from Jira

### Features
- ✅ Read versions
- ✅ Read released versions in a certain time window
- ✅ Read issues for a given JQL query
- ✅ Read issues for a given fix version

### How does it Work?
* Connects to a Jira instance via HTTPS (uses simple authorization).
* Calls REST end points on the Jira instance
* Automatically closes the connection afterwards

### Requirements
- Java 17 JRE installed (or newer)
- Access to some Jira instance
  - (tested on Jira Server 8.22.6 and Jira Cloud)

### How to Build
- You need Maven installed (e.g. 3.8.6)

On your terminal run:
```
mvn clean install
```
💡 _This also builds the JAR-with-dependencies_

### Examples
#### Read all Issues from Project
_Please note that the example requires an API token to be generated at the given user's account. For Jira Server instances, the user password can be used, too._

```Java
String user = "myName@sampleMail.com";
String apiToken = "123Fly";
String jiraInstance = "https://my-name.atlassian.net/";

JiraAccessConfig config = new JiraAccessConfig(jiraInstance, user, apiToken);

// Get issues for JQL
String jql = "project = EXWM ORDER BY Rank ASC";
JiraIssuesForJqlProvider issuesProvider = new JiraIssuesForJqlProvider();
List<Issue> issues = issuesProvider.getIssues(config, jql);

// Get all versions in project MYPROJ
JiraVersionsProvider versionsProvider = new JiraVersionsProvider();
List<Version> allVersionsInProject = versionsProvider.getVersions(config, "MYPROJ");

// Get released versions in time window from 20th Jan (included) to 1st Feb (exluded)
JiraFilteredReleasedVersionsProvider versionsProvider2 = new JiraFilteredReleasedVersionsProvider();
List<Version> versions = versionsProvider2.getReleasedVersions(config, "2023-01-20", "2023-02-01", "MYPROJ");

// Get all issues with fix-version "1.0.0"
JiraIssuesForVersionProvider issuesForVersionsProvider = new JiraIssuesForVersionProvider();
List<Issue> issuesForVersion = issuesForVersionsProvider.getIssuesForVersion(config, "1.0.0");
```
