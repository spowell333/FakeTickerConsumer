package net.sqlhacker.FakeTickerConsumer;

import org.json.JSONException;
import org.json.JSONObject;

public class Runner {
    private static int adminPort = 4201;
    private static int consumerPort = 4200;

    public static void main(String[] args) {

	StringBuffer config = new StringBuffer();
	for (int i = 0; i < args.length; i++) {
	    config.append(args[i]);
	}
	System.out.println("Args are " + config.toString());

	int returnStatus = 0;
	try {

	    JSONObject connectionDetails = new JSONObject(config.toString());
	    System.out.println("Connection Details object is "
		    + connectionDetails.toString());

	    Consumer c = new Consumer(consumerPort, connectionDetails);
	    c.canRun(true);
	    System.out.println("Starting tick consumer");
	    c.start();

	    AdminListener al = new AdminListener(adminPort, c);
	    al.canRun(true);
	    System.out.println("Starting admin listener");
	    al.start();

	    try {
		System.out
			.println("Waiting for SHUTDOWN signal from admin listener");
		c.join();
		al.join();
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	} catch (JSONException e) {
	    System.out.println("Exception thrown by JSON" + e.toString());
	    e.printStackTrace();
	    returnStatus = -1;
	} catch (Exception e) {
	    System.out.println(e.toString());
	    e.printStackTrace();
	    returnStatus = -2;
	} finally {
	    System.out.println("About to close the system");
	    System.exit(returnStatus);
	}

    }
}
