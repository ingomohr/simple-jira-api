package org.ingomohr.jira.util.http.command;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Asserts that the connection's responsecode is OK (200). Throws a
 * {@link RuntimeException} if response code was not OK.
 * 
 * @author ingomohr
 */
public class AssertResponseIsOkCommand implements HttpUrlConnectionCommand {

	@Override
	public void run(HttpURLConnection connection) throws IOException {
		int responseCode = connection.getResponseCode();
		if (responseCode != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + responseCode);
		}
	}

}
