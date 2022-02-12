package edu.nmsu.cs.webserver;

/**
 * Web worker: an object of this class executes in its own new thread to receive and respond to a
 * single HTTP request. After the constructor the object executes on its "run" method, and leaves
 * when it is done.
 *
 * One WebWorker object is only responsible for one client connection. This code uses Java threads
 * to parallelize the handling of clients: each WebWorker runs in its own thread. This means that
 * you can essentially just think about what is happening on one client at a time, ignoring the fact
 * that the entirety of the webserver execution might be handling other clients, too.
 *
 * This WebWorker class (i.e., an object of this class) is where all the client interaction is done.
 * The "run()" method is the beginning -- think of it as the "main()" for a client interaction. It
 * does three things in a row, invoking three methods in this class: it reads the incoming HTTP
 * request; it writes out an HTTP header to begin its response, and then it writes out some HTML
 * content for the response content. HTTP requests and responses are just lines of text (in a very
 * particular format).
 * 
 * @author Jon Cook, Ph.D.
 *
 **/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.TimeZone;

public class WebWorker implements Runnable
{

	private Socket socket;

	/**
	 * Constructor: must have a valid open socket
	 **/
	public WebWorker(Socket s)
	{
		socket = s;
	}

	/**
	 * Worker thread starting point. Each worker handles just one HTTP request and then returns, which
	 * destroys the thread. This method assumes that whoever created the worker created it with a
	 * valid open socket object.
	 **/
	public void run() // This is what we need to edit
	{
		System.err.println("Handling connection...");
		try
		{
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			readHTTPRequest(is);     
		    String file = writeHTTPHeader(os, "text/html"); 
			writeContent(os, file);  					 
			os.flush();
			socket.close();
		}
		catch (Exception e)
		{
			System.err.println("Output error: " + e);
		}
		System.err.println("Done handling connection.");
		return;
	}

	/**
	 * Read the HTTP request header.
	 **/
	private void readHTTPRequest(InputStream is) throws Exception
	{
		
		File requestFile = new File("request.txt");
		requestFile.delete();
		
		FileWriter writer = new FileWriter("request.txt");  
	    BufferedWriter buffer = new BufferedWriter(writer);	
		
		String line;
		BufferedReader r = new BufferedReader(new InputStreamReader(is));
		while (true)
		{
			try
			{
				while (!r.ready())
					Thread.sleep(1);
				line = r.readLine();
				System.err.println("Request line: (" + line + ")");
				
				buffer.write(line + "\n");
				
				if (line.length() == 0)
					break;
			}
			catch (Exception e)
			{
				System.err.println("Request error: " + e);
				break;
			}
		}
		buffer.close();
		return;
	}

	/**
	 * Write the HTTP header lines to the client network connection.
	 * 
	 * @param os
	 *          is the OutputStream object to write to
	 * @param contentType
	 *          is the string MIME content type (e.g. "text/html")
	 **/
	private String writeHTTPHeader(OutputStream os, String contentType) throws Exception
	{
		
		boolean isProblem = false;
		
		// Reading the Request
		BufferedReader reader = new BufferedReader(new FileReader("request.txt"));
		
		String GETline  = reader.readLine();
		
		// Get the file specified in address bar
		int indexOfSlash = GETline.indexOf("/");
		GETline = GETline.substring(indexOfSlash);
		
		int indexOfNextSpace = GETline.indexOf(" ");
		GETline = GETline.substring(1, indexOfNextSpace);
		
		File requestedFile = new File(GETline);
		
		
		Date d = new Date();
		DateFormat df = DateFormat.getDateTimeInstance();
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		// If files exist, 200 is returned
		if (GETline.equals("") || requestedFile.exists()) {
			os.write("HTTP/1.1 200 OK\n".getBytes());
			isProblem = false;
		}
		// If file doesn't exist, 404 is returned
		else {
			os.write("HTTP/1.1 404 NOT FOUND\n".getBytes());
			isProblem = true;
		}
		
		
		os.write("Date: ".getBytes());
		os.write((df.format(d)).getBytes());
		os.write("\n".getBytes());
		os.write("Server: Jacob's own server\n".getBytes());
		
		os.write("Connection: close\n".getBytes());
		os.write("Content-Type: ".getBytes());
		os.write(contentType.getBytes());
		os.write("\n\n".getBytes()); // HTTP header ends with 2 newlines
		
		// Return "ERROR" if the file doesn't exists
		if (isProblem) {
			return "ERROR";
		}
		// Return the filename if the files exists
		else {
			return GETline;
		}
	}

	/**
	 * Write the data content to the client network connection. This MUST be done after the HTTP
	 * header has been written out.
	 * 
	 * @param os
	 *          is the OutputStream object to write to
	 **/
	private void writeContent(OutputStream os, String file) throws Exception
	{
		
		LocalDate date = LocalDate.now();
		int dayOfMonth = date.getDayOfMonth();
		
		// 404 NOT FOUND Page output
		if (file.equals("ERROR")) {
			os.write("<html><head></head><body>\n".getBytes());
			os.write("<center><h1>404 Not Found</h1></center>\n".getBytes());
			os.write("</body></html>\n".getBytes());
		}
		// Default page
		else if (file.equals("")) {
			os.write("<html><head></head><body>\n".getBytes());
			os.write("<h3>My web server works!</h3>\n".getBytes());
			os.write("</body></html>\n".getBytes());
		}
		else {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line, lineReplace;
			int index, index2;
		
			// replace the tags specified in assignment
			while ((line = reader.readLine()) != null) {
				index = line.indexOf("<cs371date>");
				index2 = line.indexOf("<cs371server>");
				if (index != -1) {
					// Look for tag (date)
					line = line.replaceAll("<cs371date>", LocalDate.now().toString());
					System.out.println(line);
				}
				
				// Look for tag (server)
				if (index2 != -1) {
					line = line.replaceAll("<cs371server>", "Jacob Rydecki's Server.");
				}
				
				// Write out the html line by line
				os.write(line.getBytes());
			}
		
		}
		
	}

} // end class
