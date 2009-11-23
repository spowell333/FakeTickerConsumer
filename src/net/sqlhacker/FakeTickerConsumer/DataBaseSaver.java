package net.sqlhacker.FakeTickerConsumer;

public class DataBaseSaver implements Runnable {

    private final Quote quote;

    public DataBaseSaver(Quote quote) {
	super();
	this.quote = quote;
    }

    public void run() {

    }

}
