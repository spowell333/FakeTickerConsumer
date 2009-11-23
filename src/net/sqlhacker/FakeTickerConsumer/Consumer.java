package net.sqlhacker.FakeTickerConsumer;

import java.io.BufferedReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.json.JSONObject;

public class Consumer extends Listener {

    private static final String END = Character.valueOf((char) 4).toString();
    private final Executor slowSaver;

    public Consumer(int consumerPort) {
	super(consumerPort);
	slowSaver = Executors.newFixedThreadPool(1);
    }

    @Override
    public void run() {
	try {
	    ServerSocket server = new ServerSocket(portNumber);
	    while (canRun()) {
		Socket sock = server.accept();
		BufferedReader reader = getBufferedReader(sock);
		System.out.println("Connection accepted from "
			+ sock.getInetAddress());

		boolean consuming = true;
		while (consuming) {
		    String message = reader.readLine();
		    if (END.equals(message)) {
			consuming = false;
		    } else {
			JSONObject json = new JSONObject(message);
			Quote q = new Quote(json);
			slowSaver.execute(new DataBaseSaver(q));
		    }
		}
	    }
	} catch (Exception e) {
	    throw new RuntimeException(
		    "Unable to listen on port " + portNumber, e);
	}

    }
}
