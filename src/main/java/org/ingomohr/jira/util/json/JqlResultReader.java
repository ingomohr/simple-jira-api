package org.ingomohr.jira.util.json;

import java.util.Collections;
import java.util.Objects;

import org.ingomohr.jira.model.JqlResult;

import com.google.gson.Gson;

/**
 * Reads a {@link JqlResult} out of the given JSON.
 * 
 * @author ingomohr
 */
public class JqlResultReader {

	/**
	 * Reads the {@link JqlResult} represented by the given JSON.
	 * 
	 * @param json the json text. Cannot be <code>null</code>.
	 * @return {@link JqlResult}. Never <code>null</code>, possibly empty.
	 */
	public JqlResult readJqlResult(String json) {
		Objects.requireNonNull(json);

		Gson gson = new Gson();

		JqlResult result = gson.fromJson(json, JqlResult.class);

		if (result == null) {
			result = new JqlResult(null, 0, 0, 0, Collections.emptyList());
		}
		return result;
	}

}
