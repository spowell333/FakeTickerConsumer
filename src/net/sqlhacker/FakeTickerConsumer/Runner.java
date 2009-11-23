package net.sqlhacker.FakeTickerConsumer;

public class Runner {
    private static int adminPort = 4201;
    private static int consumerPort = 4200;

    public static void main(String[] args) {
	Consumer c = new Consumer(consumerPort);
	c.canRun(true);
	c.start();

	AdminListener al = new AdminListener(adminPort, c);
	al.canRun(true);
	al.start();

    }
}
