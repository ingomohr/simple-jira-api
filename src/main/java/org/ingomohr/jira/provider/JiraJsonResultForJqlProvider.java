package org.ingomohr.jira.provider;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import org.ingomohr.jira.JiraAccessConfig;
import org.ingomohr.jira.util.http.AutoClosingJiraHttpRequestExecutor;
import org.ingomohr.jira.util.http.command.AssertResponseIsOkCommand;
import org.ingomohr.jira.util.http.command.FetchResponseLinesCommand;
import org.ingomohr.jira.util.http.command.HttpUrlConnectionCommand;

/**
 * Executes a JQL query and returns the corresponding result from the server.
 * <p>
 * This automatically connects to Jira and disconnects, again.
 * </p>
 * 
 * @author ingomohr
 */
public class JiraJsonResultForJqlProvider {

	/**
	 * Executes the given JQL query on the Jira server specified with the given
	 * config.
	 * 
	 * @param config   the config for accessing the server. Cannot be
	 *                 <code>null</code>.
	 * @param jqlQuery the query to execute. Cannot be <code>null</code>.
	 * @return result from the server - as JSON.
	 * @throws IOException      if there's a problem accessing the server.
	 * @throws RuntimeException if the server doesn't respond with code 200.
	 */
	public String getJsonResult(JiraAccessConfig config, String jqlQuery) throws IOException {

		Objects.requireNonNull(config);
		Objects.requireNonNull(jqlQuery);

		String jqlEncoded = URLEncoder.encode(jqlQuery, "UTF-8");
		String restSuffix = "/rest/api/2/search?jql=" + jqlEncoded;

		final AtomicReference<String> response = new AtomicReference<>();

		AutoClosingJiraHttpRequestExecutor executor = createRequestExecutor();
		HttpUrlConnectionCommand cmdSetupRequest = setupRequest();
		HttpUrlConnectionCommand cmdAssertResponseIsOk = new AssertResponseIsOkCommand();
		HttpUrlConnectionCommand cmdFetchResponse = fetchResponse(response);

		executor.execute(config, restSuffix, Arrays.asList(cmdSetupRequest, cmdAssertResponseIsOk, cmdFetchResponse));

		return response.get();
	}

	private HttpUrlConnectionCommand setupRequest() {
		return new HttpUrlConnectionCommand() {

			@Override
			public void run(HttpURLConnection connection) throws IOException {
				connection.setRequestMethod("GET");
				connection.setRequestProperty("Accept", "application/json");
				connection.setRequestProperty("Content-Type", "application/json");
			}
		};
	}

	private HttpUrlConnectionCommand fetchResponse(final AtomicReference<String> target) {
		return new HttpUrlConnectionCommand() {

			@Override
			public void run(HttpURLConnection connection) throws IOException {
				FetchResponseLinesCommand cmd = new FetchResponseLinesCommand();
				cmd.run(connection);
				List<String> lines = cmd.getLines();
				target.set(String.join("\n", lines));
			}
		};
	}

	protected AutoClosingJiraHttpRequestExecutor createRequestExecutor() {
		return new AutoClosingJiraHttpRequestExecutor();
	}

}
