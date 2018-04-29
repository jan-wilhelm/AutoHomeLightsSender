package me.dv02.autohome.lightssender;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer extends Thread {
	private ServerSocket serverSocket;
	private int port;
	private boolean running = false;

	public SocketServer(int port) {
		this.port = port;
	}

	public void startServer() {
		try {
			serverSocket = new ServerSocket(port);
			this.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stopServer() {
		running = false;
		this.interrupt();
	}

	@Override
	public void run() {
		running = true;
		while (running) {
			try {
				System.out.println("Listening for a connection");

				// Call accept() to receive the next connection
				Socket socket = serverSocket.accept();
				System.out.println(socket.getInetAddress().toString());
				if (!socket.getInetAddress().toString().contains("192.168.178")) {
					socket.close();
					continue;
				}

				RequestHandler requestHandler = new RequestHandler(socket);
				requestHandler.run();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		args = new String[] { "8080" };
		if (args.length == 0) {
			System.out.println("Usage: SimpleSocketServer <port>");
			System.exit(0);
		}
		int port = Integer.parseInt(args[0]);
		System.out.println("Start server on port: " + port);

		SocketServer server = new SocketServer(port);
		server.startServer();
	}
}
