![Build](https://github.com/ingomohr/simple-jira-api/actions/workflows/mvn-build-main.yml/badge.svg?branch=main)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
## What is This?
Simple API to read information from Jira

### Features
- ✅ Read Versions
- ✅ Read released versions in a certain time window
- ✅ Read issues for a given JQL query
- ✅ Read issues for a given fix version

### How does it Work?
* It connects to a Jira instance via HTTPS (uses username/password authorization for now).
* Calls REST end points on the Jira instance
* Automatically closes the connection afterwards


### How to Build
:bulb: _This also builds the JAR-with-dependencies_

(Requires Maven installed)

On your terminal run:
```
mvn clean install
```
