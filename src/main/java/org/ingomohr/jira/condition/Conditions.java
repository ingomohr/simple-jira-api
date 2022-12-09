package org.ingomohr.jira.condition;

import org.ingomohr.jira.model.Version;

/**
 * Provides static condition methods.
 * 
 * @author ingomohr
 */
public final class Conditions {

	/**
	 * Returns <code>true</code> if a given {@link Version} has been released and if
	 * the release date was before the given date.
	 * <p>
	 * The date to test against can be <code>null</code>. In that case, the
	 * predicate always returns <code>true</code> (if the version had been released
	 * at all and has a release date).
	 * </p>
	 * 
	 * @param version     the version to test. Cannot be <code>null</code>.
	 * @param exludedDate the date to exclude. Might be <code>null</code>.
	 * @return <code>true</code> if version has been released and has a release date
	 *         and if version's release date is before (excl.) the given date (or
	 *         given date is <code>null</code>).
	 */
	public static boolean wasReleasedBefore(Version version, String exludedDate) {
		return new WasReleasedBefore().test(version, exludedDate);
	}

	/**
	 * Returns <code>true</code> if the given {@link Version} has been released and
	 * has a release date and if that date is <i>not</i> before the given date (or
	 * if that date is <code>null</code>).
	 * 
	 * @param version      the version to test. Cannot be <code>null</code>.
	 * @param includedDate the since-date. Might be <code>null</code>.
	 * @return <code>true</code> if version was released and has a release date and
	 *         (either since-date is <code>null</code> or since-date is equal or
	 *         older than version's release date.)
	 */
	public static boolean wasReleasedSince(Version version, String includedDate) {
		return new WasReleasedSince().test(version, includedDate);
	}

	private Conditions() {
	}

}
