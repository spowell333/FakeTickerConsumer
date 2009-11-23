package net.sqlhacker.FakeTickerConsumer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Socket;

public abstract class Listener extends Thread {
    protected final int portNumber;
    private boolean canRun;

    public Listener(int consumerPort) {
	this.portNumber = consumerPort;
    }

    protected boolean canRun() {
	return this.canRun;
    }

    public synchronized void canRun(boolean b) {
	this.canRun = b;
    }

    @Override
    public abstract void run();

    protected BufferedReader getBufferedReader(Socket s) throws IOException {

	InputStream is = s.getInputStream();
	Reader r = new InputStreamReader(is);
	BufferedReader reader = new BufferedReader(r);
	return reader;
    }
}
