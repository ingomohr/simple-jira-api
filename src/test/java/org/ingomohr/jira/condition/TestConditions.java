package org.ingomohr.jira.condition;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.ingomohr.jira.model.Version;
import org.junit.jupiter.api.Test;

class TestConditions {

	@Test
	void wasReleasedBefore() {
		assertAll(() -> {
			assertEquals(false, Conditions.wasReleasedBefore(mkVersion(false, null), null));
			assertEquals(false, Conditions.wasReleasedBefore(mkVersion(true, null), null));
			assertEquals(false, Conditions.wasReleasedBefore(mkVersion(false, "2022-12-20"), null));
			assertEquals(true, Conditions.wasReleasedBefore(mkVersion(true, "2022-12-20"), null));
			assertEquals(false, Conditions.wasReleasedBefore(mkVersion(false, null), "2022-12-21"));
			assertEquals(false, Conditions.wasReleasedBefore(mkVersion(true, null), "2022-12-21"));
			assertEquals(false, Conditions.wasReleasedBefore(mkVersion(false, "2022-12-20"), "2022-12-21"));
			assertEquals(true, Conditions.wasReleasedBefore(mkVersion(true, "2022-12-20"), "2022-12-21"));
			assertEquals(false, Conditions.wasReleasedBefore(mkVersion(true, "2022-12-20"), "2022-12-20"));
			assertEquals(false, Conditions.wasReleasedBefore(mkVersion(true, "2022-12-20"), "2022-02-21"));
			assertEquals(true, Conditions.wasReleasedBefore(mkVersion(true, "2021-12-20"), "2022-12-20"));
		});
	}

	@Test
	void wasReleasedSince() {
		assertAll(() -> {
			assertEquals(false, Conditions.wasReleasedSince(mkVersion(false, null), null));
			assertEquals(false, Conditions.wasReleasedSince(mkVersion(true, null), null));
			assertEquals(false, Conditions.wasReleasedSince(mkVersion(false, "2022-12-20"), null));
			assertEquals(true, Conditions.wasReleasedSince(mkVersion(true, "2022-12-20"), null));
			assertEquals(false, Conditions.wasReleasedSince(mkVersion(false, "2022-12-20"), "2022-12-20"));
			assertEquals(true, Conditions.wasReleasedSince(mkVersion(true, "2022-12-20"), "2022-12-20"));
			assertEquals(false, Conditions.wasReleasedSince(mkVersion(false, "2022-12-20"), "2022-12-19"));
			assertEquals(true, Conditions.wasReleasedSince(mkVersion(true, "2022-12-20"), "2022-12-19"));
			assertEquals(false, Conditions.wasReleasedSince(mkVersion(false, "2022-12-20"), "2022-12-21"));
			assertEquals(false, Conditions.wasReleasedSince(mkVersion(true, "2022-12-20"), "2022-12-21"));
		});
	}

	private Version mkVersion(boolean released, String releasDate) {
		Version version = mock(Version.class);
		when(version.released()).thenReturn(released);
		when(version.releaseDate()).thenReturn(releasDate);
		return version;
	}

}
