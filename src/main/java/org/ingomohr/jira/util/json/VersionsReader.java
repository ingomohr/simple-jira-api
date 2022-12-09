package org.ingomohr.jira.util.json;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.ingomohr.jira.model.Version;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class VersionsReader {

	public List<Version> readVersions(String json) {
		Objects.requireNonNull(json);

		Type type = new TypeToken<ArrayList<Version>>() {
		}.getType();

		Gson gson = new Gson();

		List<Version> versions = gson.fromJson(json, type);
		return versions;
	}

}
