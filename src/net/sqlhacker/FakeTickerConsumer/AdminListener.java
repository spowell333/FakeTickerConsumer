package net.sqlhacker.FakeTickerConsumer;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;

public class AdminListener extends Listener {
    private final Consumer consumer;

    public AdminListener(int portNumber, Consumer c) {
	super(portNumber);
	this.consumer = c;
    }

    @Override
    public void run() {
	try {
	    ServerSocket server = new ServerSocket(portNumber);
	    while (canRun()) {
		BufferedReader reader = getBufferedReader(server.accept());

		String command = reader.readLine();

		boolean canRun = command == null
			|| !command.matches("SHUTDOWN");
		System.out.println("Command received as '" + command
			+ "' Consumer can run == " + canRun);
		if (consumer != null)
		    consumer.canRun(canRun);
		this.canRun(canRun);
		reader.close();

	    }
	} catch (IOException e) {
	    throw new RuntimeException("Failed to open port " + portNumber, e);
	}
    }

}
