package org.ingomohr.jira.condition;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.function.BiPredicate;

import org.ingomohr.jira.model.Version;

/**
 * Predicate that returns <code>true</code> if a given version was released and
 * has a release date and that release date was <i>not</i> before the given date
 * (or if the given since-date was <code>null</code>).
 * 
 * @author ingomohr
 */
public class WasReleasedSince implements BiPredicate<Version, String> {

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public boolean test(Version version, String sinceDate) {
		Objects.requireNonNull(version);

		final String versionDate = version.releaseDate();
		if (!version.released() || versionDate == null) {
			return false;
		}

		if (sinceDate == null) {
			return true;
		}

		try {
			Date since = dateFormat.parse(sinceDate);
			Date actual = dateFormat.parse(versionDate);

			return actual.compareTo(since) >= 0;
		} catch (ParseException e) {
			// no supported date format: ignore.
		}

		return false;
	}

}
