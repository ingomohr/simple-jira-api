package org.ingomohr.jira.util.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Objects;

import org.ingomohr.jira.JiraAccessConfig;

/**
 * Connects to a Jira server via HTTP.
 * 
 * @author ingomohr
 */
public class JiraHttpConnector {

	/**
	 * Connects to the Jira server specified by the given config.
	 * 
	 * @param config        the config. Cannot be <code>null</code>.
	 * @param restUrlSuffix the rest call to be appended to the server url. Can
	 *                      optionally start with a <code>/</code>. Cannot be
	 *                      <code>null</code>.
	 * @return connection (already connected). Never <code>null</code>. The
	 *         connection needs to be closed by the client.
	 * @throws IOException if connection config isn't valid.
	 */
	public HttpURLConnection connect(JiraAccessConfig config, String restUrlSuffix) throws IOException {

		/*
		 * Q: Why not use HTTPClient API from Java 11?
		 * 
		 * A: I like to be able to explicitly close the connection; and for now the old
		 * API is all that I need.
		 */

		Objects.requireNonNull(config);
		Objects.requireNonNull(restUrlSuffix);

		String urlSuffix = restUrlSuffix.startsWith("/") ? restUrlSuffix : ("/" + restUrlSuffix);
		String fullUrl = config.serverUrl() + urlSuffix;

		String user = config.user();
		String pwd = config.password();
		String auth = new String(Base64.getEncoder().encode((user + ":" + pwd).getBytes()));

		URL url = new URL(fullUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Authorization", "Basic " + auth);
		return conn;
	}

}
