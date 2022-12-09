package org.ingomohr.jira.provider;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.ingomohr.jira.JiraAccessConfig;
import org.ingomohr.jira.condition.Conditions;
import org.ingomohr.jira.model.Version;

/**
 * Returns all released versions in a given time window that belong to given
 * projects.
 * 
 * @author ingomohr
 */
public class JiraFilteredReleasedVersionsProvider {

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * Returns all versions released in a given time window from the projects with
	 * the given keys.
	 * 
	 * @param config       the config to use. Cannot be <code>null</code>.
	 * @param fromIncluded the start date (includes versions released on that date).
	 *                     <code>null</code> to not specify a start date. Format is
	 *                     <code>YYYY-MM-DD</code>.
	 * @param toExcluded   the end date (excludes versions released on that date).
	 *                     <code>null</code> to not specify an end date. Format is
	 *                     <code>YYYY-MM-DD</code>.
	 * @param projectKeys  the keys of the projects to include. Cannot be
	 *                     <code>null</code>.
	 * @return all released versions that fit the time window from given projects.
	 *         Never <code>null</code>, possibly empty.
	 * @throws IOException
	 */
	public List<Version> getReleasedVersions(JiraAccessConfig config, String fromIncluded, String toExcluded,
			String... projectKeys) throws IOException {

		Objects.requireNonNull(config);
		assertIsDateOrNull(fromIncluded);
		assertIsDateOrNull(toExcluded);

		List<Version> versions = new ArrayList<>();

		for (String projectKey : projectKeys) {
			JiraVersionsProvider provider = createJiraVersionsProvider();
			List<Version> versionsFromProject = provider.getVersions(config, projectKey);
			versions.addAll(versionsFromProject);
		}

		versions = versions.stream().filter(v -> v.released() && Conditions.wasReleasedSince(v, fromIncluded)
				&& Conditions.wasReleasedBefore(v, toExcluded)).collect(Collectors.toList());

		return versions;
	}

	private void assertIsDateOrNull(String date) {
		if (date != null) {
			try {
				dateFormat.parse(date);
			} catch (ParseException e) {
				throw new IllegalArgumentException("Not a date of format yyyy-MM-dd: " + date);
			}
		}
	}

	protected JiraVersionsProvider createJiraVersionsProvider() {
		return new JiraVersionsProvider();
	}

}
