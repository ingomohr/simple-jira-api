package org.ingomohr.jira.provider;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import org.ingomohr.jira.JiraAccessConfig;
import org.ingomohr.jira.model.Version;
import org.ingomohr.jira.util.http.AutoClosingJiraHttpRequestExecutor;
import org.ingomohr.jira.util.http.command.AssertResponseIsOkCommand;
import org.ingomohr.jira.util.http.command.FetchResponseLinesCommand;
import org.ingomohr.jira.util.http.command.HttpUrlConnectionCommand;
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

		AutoClosingJiraHttpRequestExecutor executor = new AutoClosingJiraHttpRequestExecutor();

		List<HttpUrlConnectionCommand> commands = new ArrayList<>();

		commands.add(new HttpUrlConnectionCommand() {

			@Override
			public void run(HttpURLConnection connection) throws IOException {
				connection.setRequestMethod("GET");
				connection.setRequestProperty("Accept", "application/json");
			}
		});
		commands.add(new AssertResponseIsOkCommand());
		commands.add(new HttpUrlConnectionCommand() {

			@Override
			public void run(HttpURLConnection connection) throws IOException {
				FetchResponseLinesCommand cmd = new FetchResponseLinesCommand();
				cmd.run(connection);
				List<String> lines = cmd.getLines();
				response.set(String.join("\n", lines));
			}
		});

		executor.execute(config, restUrlSuffix, commands);

		String json = response.get();
		List<Version> versions = createJSonReader().readVersions(json);

		return versions;
	}

	protected VersionsReader createJSonReader() {
		return new VersionsReader();
	}

}
