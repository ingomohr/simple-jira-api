package org.ingomohr.jira.util.json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.ingomohr.jira.model.Issue;
import org.ingomohr.jira.model.JqlResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

class TestJqlResultReader {

	private JqlResultReader objUT;

	@BeforeEach
	void prep() {
		objUT = new JqlResultReader();
	}

	@Test
	void read_JsonIsEmpty_ReturnsEmptyResult() {
		assertIsEmpty(objUT.readJqlResult(""));
	}

	@Test
	void read_JsonIsNotEmpty_ReturnsResultWithAllIssuesFromJson() throws Exception {
		Path path = Paths
				.get("src/test/resources/org/ingomohr/csrelease/internal/jira/util/json/JqlResultReader/input.json");
		String json = Files.readString(path);

		JqlResult actual = objUT.readJqlResult(json);
		assertEquals(17, actual.total());
		assertEquals(1, actual.issues().size());
		Issue actualIssue = actual.issues().get(0);

		System.out.println(new Gson().toJson(actualIssue));

		assertEquals("This is the summary of the issue", actualIssue.fields().summary());
		assertEquals("This is the description of the issue", actualIssue.fields().description());
		assertEquals("username-2", actualIssue.fields().assignee().name());
		assertEquals("Closed", actualIssue.fields().status().name());
		assertEquals("Done", actualIssue.fields().status().statusCategory().name());
		assertEquals("Fixed", actualIssue.fields().resolution().name());
	}

	private void assertIsEmpty(JqlResult result) {
		assertNotNull(result);
		assertThat(result.issues(), is(empty()));
		assertEquals(0, result.total());
	}

}
