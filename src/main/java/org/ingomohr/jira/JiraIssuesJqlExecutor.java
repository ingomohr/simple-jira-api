package org.ingomohr.jira;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.ingomohr.jira.model.Issue;
import org.ingomohr.jira.model.JqlResult;
import org.ingomohr.jira.util.json.JqlResultReader;

/**
 * Connects to Jira and returns all issues found for a given JQL query.
 * Disconnects automatically from Jira.
 * 
 * @author ingomohr
 */
public class JiraIssuesJqlExecutor {

	/**
	 * Executes the given JQL query on the Jira server specified with the given
	 * config.
	 * <p>
	 * Automatically connects and disconnects to/from Jira.
	 * </p>
	 * 
	 * @param config   the config for accessing the Jira server. Cannot be
	 *                 <code>null</code>.
	 * @param jqlQuery the query to run. Cannot be <code>null</code>.
	 * @return all issues found for the given query. Never <code>null</code>,
	 *         possibly empty.
	 * @throws IOException      if there's a problem accessing the server.
	 * @throws RuntimeException if the server doesn't respond with code 200.
	 */
	public List<Issue> execute(JiraAccessConfig config, String jqlQuery) throws IOException {
		Objects.requireNonNull(config);
		Objects.requireNonNull(jqlQuery);

		JiraJqlExecutor executor = createExecutor();
		String response = executor.execute(config, jqlQuery);

		JqlResultReader reader = createJqlResultReader();
		JqlResult result = reader.readJqlResult(response);
		List<Issue> issues = result.issues();

		return issues;
	}

	protected JiraJqlExecutor createExecutor() {
		return new JiraJqlExecutor();
	}

	protected JqlResultReader createJqlResultReader() {
		return new JqlResultReader();
	}

}
