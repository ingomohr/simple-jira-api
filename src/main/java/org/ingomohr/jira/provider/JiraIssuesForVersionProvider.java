package org.ingomohr.jira.provider;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.ingomohr.jira.JiraAccessConfig;
import org.ingomohr.jira.JiraIssuesJqlExecutor;
import org.ingomohr.jira.model.Issue;
import org.ingomohr.jira.model.Version;

/**
 * Returns all issues for a given version.
 * <p>
 * Automatically connects and disconnected to/from Jira.
 * </p>
 * 
 * @author ingomohr
 */
public class JiraIssuesForVersionProvider {

	/**
	 * Returns all issues assigned to the given version as "fix version".
	 * 
	 * @param config  the config to access the Jira server. Cannot be
	 *                <code>null</code>.
	 * @param version the version for which to find the issues. Cannot be
	 *                <code>null</code>. Must have a name.
	 * @return all issues for given version as "fix version". Never
	 *         <code>null</code>, possibly empty.
	 * @throws IOException if there are problems with the connection configuration.
	 */
	public List<Issue> getIssuesForVersion(JiraAccessConfig config, Version version) throws IOException {
		Objects.requireNonNull(version);
		Objects.requireNonNull(version.name());

		return getIssuesForVersion(config, version.name());
	}

	/**
	 * Returns all issues assigned to the given version as "fix version".
	 * 
	 * @param config  the config to access the Jira server. Cannot be
	 *                <code>null</code>.
	 * @param version the version for which to find the issues. Cannot be
	 *                <code>null</code>.
	 * @return all issues for given version as "fix version". Never
	 *         <code>null</code>, possibly empty.
	 * @throws IOException if there are problems with the connection configuration.
	 */
	public List<Issue> getIssuesForVersion(JiraAccessConfig config, String versionName) throws IOException {
		Objects.requireNonNull(config);
		Objects.requireNonNull(versionName);

		JiraIssuesJqlExecutor executor = createExecutor();
		List<Issue> issues = executor.execute(config, "fixVersion in (" + versionName + ")");
		return issues;
	}

	protected JiraIssuesJqlExecutor createExecutor() {
		return new JiraIssuesJqlExecutor();
	}

}
