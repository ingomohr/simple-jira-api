package org.ingomohr.jira.util.http.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Command to fetch all lines from the connection's {@link InputStream}.
 * <p>
 * Lines can be retrieved via {@link #getLines()}.
 * </p>
 * <p>
 * The command is synchronized so that only 1 thread can run the method at a
 * time to write the lines to be retrieved via {@link #getLines()}.
 * </p>
 * 
 * @author ingomohr
 */
public class FetchResponseLinesCommand implements HttpUrlConnectionCommand {

	private List<String> lines;

	@Override
	public void run(HttpURLConnection connection) throws IOException {
		synchronized (this) {
			lines = new ArrayList<>();
			try (BufferedReader in = new BufferedReader(new InputStreamReader((connection.getInputStream())))) {
				String output;
				while ((output = in.readLine()) != null) {
					lines.add(output);
				}
			}

		}
	}

	public List<String> getLines() {
		return lines;
	}

}
