package org.ingomohr.jira.provider;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.ingomohr.jira.JiraAccessConfig;
import org.ingomohr.jira.JiraJqlExecutor;
import org.ingomohr.jira.model.Issue;
import org.ingomohr.jira.model.JqlResult;
import org.ingomohr.jira.util.json.JqlResultReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestJiraIssuesForJqlProvider {

	private static final String RESULT = "result";
	private static final String JQL = "jql";

	private JiraIssuesForJqlProvider objUT;

	private JqlResultReader resultReader;
	private JiraJqlExecutor jqlExecutor;

	@BeforeEach
	void prep() {
		jqlExecutor = mock(JiraJqlExecutor.class);
		resultReader = mock(JqlResultReader.class);

		objUT = new JiraIssuesForJqlProvider() {

			@Override
			protected JiraJqlExecutor createJiraJqlExecutor() {
				return jqlExecutor;
			}

			@Override
			protected JqlResultReader createJqlResultReader() {
				return resultReader;
			}
		};

	}

	@Test
	void execute() throws Exception {
		JiraAccessConfig config = mock(JiraAccessConfig.class);

		@SuppressWarnings("unchecked")
		List<Issue> issues = mock(List.class);
		JqlResult result = mock(JqlResult.class);
		when(result.issues()).thenReturn(issues);

		when(jqlExecutor.execute(config, JQL)).thenReturn(RESULT);
		when(resultReader.readJqlResult(RESULT)).thenReturn(result);

		assertSame(issues, objUT.execute(config, JQL));
	}

}
