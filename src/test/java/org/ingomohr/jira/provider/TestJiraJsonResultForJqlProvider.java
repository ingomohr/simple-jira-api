package org.ingomohr.jira.provider;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.ingomohr.jira.JiraAccessConfig;
import org.ingomohr.jira.util.http.AutoClosingJiraHttpRequestExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestJiraJsonResultForJqlProvider {

	private static final String JQL = "jql";

	private JiraJsonResultForJqlProvider objUT;

	private AutoClosingJiraHttpRequestExecutor executor;
	private JiraAccessConfig config;

	@BeforeEach
	void prep() throws Exception {
		config = mock(JiraAccessConfig.class);

		executor = mock(AutoClosingJiraHttpRequestExecutor.class);

		objUT = new JiraJsonResultForJqlProvider() {

			@Override
			protected AutoClosingJiraHttpRequestExecutor createRequestExecutor() {
				return executor;
			}
		};
	}

	@Test
	void getJsonResult_ExecutorWasCalled() throws Exception {
		objUT.getJsonResult(config, JQL);
		verify(executor, times(1)).execute(same(config), any(String.class), anyList());
	}

}
