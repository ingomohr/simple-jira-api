package org.ingomohr.jira.util.http.command;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * A command to be executed on an {@link HttpURLConnection}.
 * 
 * @author ingomohr
 */
public interface HttpUrlConnectionCommand {

	/**
	 * Executes some request on the given connection. The connection has to be been
	 * established before calling this method.
	 * 
	 * @param connection the connection to run the command on. Cannot be
	 *                   <code>null</code>.
	 * @throws IOException if the command cannot be executed on the connection.
	 */
	void run(HttpURLConnection connection) throws IOException;

}
