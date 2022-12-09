package org.ingomohr.jira.util.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Objects;

import org.ingomohr.jira.JiraAccessConfig;

/**
 * Executes some request on an HTTP connection which is both created and closed
 * automatically.
 * <p>
 * Subclasses implement {@link #execute(HttpURLConnection)} to perform the
 * actual request.
 * </p>
 * 
 * @author ingomohr
 */
public abstract class AutoClosingJiraHttpRequestExecutor {

	public void execute(JiraAccessConfig config, String restUrlSuffix) throws IOException {
		JiraHttpConnector connector = createJiraHttpConnector();

		HttpURLConnection connection = null;
		try {
			connection = connector.connect(config, restUrlSuffix);
			execute(connection);

		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	/**
	 * Called by {@link #execute(JiraAccessConfig, String)}.
	 * <p>
	 * Executes some request on the given connection. The connection has been opened
	 * before calling this method and will be closed afterwards.
	 * </p>
	 * <p>
	 * Subclasses implement this method perform the actual request on the
	 * connection.
	 * </p>
	 * 
	 * @param connection the connection. Never <code>null</code>, is already open.
	 * @throws IOException if there are problems with the connection configuration.
	 * @see #assertResponseIsOK(HttpURLConnection)
	 */
	protected abstract void execute(HttpURLConnection connection) throws IOException;

	protected JiraHttpConnector createJiraHttpConnector() {
		return new JiraHttpConnector();
	}

	/**
	 * Throws a {@link RuntimeException} if the given connection's response is not
	 * 200 (OK).
	 * 
	 * @param conn the connection. Cannot be <code>null</code>.
	 * @throws IOException if connection setup is not valid.
	 */
	protected void assertResponseIsOK(HttpURLConnection conn) throws IOException {
		Objects.requireNonNull(conn);

		int responseCode = conn.getResponseCode();
		if (responseCode != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + responseCode);
		}
	}

}
