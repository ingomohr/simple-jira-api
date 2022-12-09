package org.ingomohr.jira.util.http;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.ingomohr.jira.JiraAccessConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestAutoClosingJiraHttpRequestExecutor {

	/*
	 * Note: Requires file
	 * 
	 * src/test/resources/mockito-extensions/org.mockito.plugins.
	 * 
	 * MockMaker to enable mocking final classes & methods.
	 */

	private JiraHttpConnector connector;
	private HttpURLConnection connection;
	private JiraAccessConfig config;

	@BeforeEach
	void prep() throws Exception {
		connector = mock(JiraHttpConnector.class);
		connection = mock(HttpURLConnection.class);

		config = mock(JiraAccessConfig.class);

		when(connector.connect(config, "mySuffix")).thenReturn(connection);
	}

	@Test
	public void execute_exutionThrowsCheckedException_DisconnectIsStillCalled() {
		execute_executionThrowsException_DisconnectIsStillCalled(ExceptionType.Checked);
	}

	@Test
	public void execute_exutionThrowsUncheckedException_DisconnectIsStillCalled() {
		execute_executionThrowsException_DisconnectIsStillCalled(ExceptionType.Unchecked);
	}

	private void execute_executionThrowsException_DisconnectIsStillCalled(ExceptionType type) {
		AutoClosingJiraHttpRequestExecutor objUT = new AutoClosingJiraHttpRequestExecutor() {

			@Override
			protected void execute(HttpURLConnection connection) throws IOException {
				if (type == ExceptionType.Unchecked) {
					throw new RuntimeException("foo");
				} else {
					throw new IOException("bar");
				}
			}

			@Override
			protected JiraHttpConnector createJiraHttpConnector() {
				return connector;
			}
		};

		try {
			objUT.execute(config, "mySuffix");
		} catch (Exception ex) {
			// ignore. We don't care for the exception. We only care for the disconnect to
			// happen.
		}

		verify(connection, times(1)).disconnect();
	}

	private enum ExceptionType {
		Checked, Unchecked
	}

}
