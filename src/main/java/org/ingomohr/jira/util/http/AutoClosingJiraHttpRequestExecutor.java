package org.ingomohr.jira.util.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;

import org.ingomohr.jira.JiraAccessConfig;
import org.ingomohr.jira.util.http.command.HttpUrlConnectionCommand;

/**
 * Executes some request on an HTTP connection which is both created and closed
 * automatically.
 * <p>
 * The request can be specified by a number of commands to be executed
 * sequentially.
 * </p>
 * 
 * @author ingomohr
 */
public class AutoClosingJiraHttpRequestExecutor {

	/**
	 * Executes the request specified by the given commands on a connection
	 * established for the given config and
	 * 
	 * @param config        the config to specify the connection. Cannot be
	 *                      <code>null</code>.
	 * @param restUrlSuffix the rest call to be appended to the server url. Can
	 *                      optionally start with a <code>/</code>. Cannot be
	 *                      <code>null</code>.
	 * @param commands      the commands to execute.
	 * @throws IOException if there's a problem executing on the connection.
	 */
	public void execute(JiraAccessConfig config, String restUrlSuffix, List<HttpUrlConnectionCommand> commands)
			throws IOException {
		JiraHttpConnector connector = createJiraHttpConnector();

		HttpURLConnection connection = null;
		try {
			connection = connector.connect(config, restUrlSuffix);

			for (HttpUrlConnectionCommand cmd : commands) {
				cmd.run(connection);
			}

		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

	}

	/**
	 * Executes the request specified by the given commands on a connection
	 * established for the given config and
	 * 
	 * @param config        the config to specify the connection. Cannot be
	 *                      <code>null</code>.
	 * @param restUrlSuffix the rest call to be appended to the server url. Can
	 *                      optionally start with a <code>/</code>. Cannot be
	 *                      <code>null</code>.
	 * @param commands      the commands to execute.
	 * @throws IOException if there's a problem executing on the connection.
	 */
	public void execute(JiraAccessConfig config, String restUrlSuffix, HttpUrlConnectionCommand... commands)
			throws IOException {
		execute(config, restUrlSuffix, Arrays.asList(commands));
	}

	protected JiraHttpConnector createJiraHttpConnector() {
		return new JiraHttpConnector();
	}

}
