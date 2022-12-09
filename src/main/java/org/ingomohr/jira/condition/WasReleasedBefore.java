package org.ingomohr.jira.condition;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.function.BiPredicate;

import org.ingomohr.jira.model.Version;

/**
 * Predicate that returns <code>true</code> if a given {@link Version} has been
 * released and if the release date was before the given date.
 * <p>
 * The date to test against can be <code>null</code>. In that case, the
 * predicate always returns <code>true</code> (if the version had been released
 * at all and has a release date).
 * </p>
 * 
 * @author ingomohr
 */
public class WasReleasedBefore implements BiPredicate<Version, String> {

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public boolean test(Version version, String date) {
		Objects.requireNonNull(version);

		if (!version.released()) {
			return false;
		}

		final String versionDate = version.releaseDate();
		if (versionDate == null) {
			return false;
		}

		if (date == null) {
			return true;
		}

		try {
			Date before = dateFormat.parse(date);
			Date actual = dateFormat.parse(versionDate);

			return actual.before(before);
		} catch (ParseException e) {
			// no supported date format: ignore.
		}

		return false;
	}

}
