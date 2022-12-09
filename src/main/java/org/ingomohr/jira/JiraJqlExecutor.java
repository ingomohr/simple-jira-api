package org.ingomohr.jira;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import org.ingomohr.jira.util.http.AutoClosingJiraHttpRequestExecutor;
import org.ingomohr.jira.util.http.JiraHttpConnector;

/**
 * Executes a JQL query and returns the corresponding result from the server.
 * <p>
 * This automatically connects to Jira and disconnects, again.
 * </p>
 * 
 * @author ingomohr
 */
public class JiraJqlExecutor {

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
	public String execute(JiraAccessConfig config, String jqlQuery) throws IOException {

		Objects.requireNonNull(config);
		Objects.requireNonNull(jqlQuery);

		String jqlEncoded = URLEncoder.encode(jqlQuery, "UTF-8");
		String restSuffix = "/rest/api/2/search?jql=" + jqlEncoded;

		final AtomicReference<String> response = new AtomicReference<>();

		final AutoClosingJiraHttpRequestExecutor executor = new AutoClosingJiraHttpRequestExecutor() {

			@Override
			protected void execute(HttpURLConnection conn) throws IOException {
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");
				conn.setRequestProperty("Content-Type", "application/json");

				assertResponseIsOK(conn);

				List<String> lines = fetchResponseLines(conn);
				response.set(String.join("\n", lines));
			}
		};
		executor.execute(config, restSuffix);

		return response.get();
	}

	protected JiraHttpConnector createJiraConnector() {
		return new JiraHttpConnector();
	}

	private List<String> fetchResponseLines(HttpURLConnection conn) throws IOException {
		List<String> lines = new ArrayList<>();
		try (BufferedReader in = new BufferedReader(new InputStreamReader((conn.getInputStream())))) {
			String output;
			while ((output = in.readLine()) != null) {
				lines.add(output);
			}
		}
		return lines;
	}

}
