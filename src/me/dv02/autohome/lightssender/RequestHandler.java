package me.dv02.autohome.lightssender;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class RequestHandler {
	private Socket socket;

	RequestHandler(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			System.out.println("Received a connection from " + this.socket.getInetAddress().toString());

			// Get input and output streams
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			final String line = in.readLine();
			System.out.println("Got " + line);
			
			// Build the process
			ProcessBuilder ps = new ProcessBuilder(line.split(" "));

			// From the DOC: Initially, this property is false, meaning that the
			// standard output and error output of a subprocess are sent to two
			// separate streams
			ps.redirectErrorStream(true);
			
			Process pr = ps.start();

			BufferedReader processIn = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String gotin;
			while ((gotin = processIn.readLine()) != null) {
				System.out.println(gotin);
			}
			pr.waitFor();

			processIn.close();

			// Close our connection
			in.close();
			socket.close();

			System.out.println("Connection closed");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}