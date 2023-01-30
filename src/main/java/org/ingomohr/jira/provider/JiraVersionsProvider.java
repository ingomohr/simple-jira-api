package org.ingomohr.jira.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import org.ingomohr.jira.JiraAccessConfig;
import org.ingomohr.jira.model.Version;
import org.ingomohr.jira.util.http.AutoClosingJiraHttpRequestExecutor;
import org.ingomohr.jira.util.json.VersionsReader;

/**
 * Returns all versions for a given project key.
 * <p>
 * Automatically connects and disconnects to/from Jira.
 * </p>
 * 
 * @author ingomohr
 */
public class JiraVersionsProvider {

	/**
	 * Returns all versions from the project with the given key.
	 * 
	 * @param config     the Jira access config. Cannot be <code>null</code>.
	 * @param projectKey the project key to use. Cannot be <code>null</code>.
	 * @return all versions. Never <code>null</code>, possibly empty.
	 * @throws IOException if there are problems with the connection configuration.
	 */
	public List<Version> getVersions(JiraAccessConfig config, String projectKey) throws IOException {
		Objects.requireNonNull(config);
		Objects.requireNonNull(projectKey);

		final String restUrlSuffix = "rest/api/latest/project/" + projectKey + "/versions";

		final AtomicReference<String> response = new AtomicReference<>();
		AutoClosingJiraHttpRequestExecutor executor = new AutoClosingJiraHttpRequestExecutor() {

			@Override
			protected void execute(HttpURLConnection conn) throws IOException {
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");

				assertResponseIsOK(conn);

				List<String> lines = fetchResponseLines(conn);
				response.set(String.join("\n", lines));
			}
		};
		executor.execute(config, restUrlSuffix);

		String json = response.get();
		List<Version> versions = createJSonReader().readVersions(json);

		return versions;
	}

	protected VersionsReader createJSonReader() {
		return new VersionsReader();
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
