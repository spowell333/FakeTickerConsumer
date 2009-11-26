package net.sqlhacker.FakeTickerConsumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.sqlhacker.db.ConnectionFactory;

public class DataBaseSaver implements Runnable {

    private final Quote quote;
    private final ConnectionFactory connectionFactory;

    public DataBaseSaver(Quote quote, ConnectionFactory cf) {
	super();
	this.quote = quote;
	this.connectionFactory = cf;
	System.out.println(quote.toString());

    }

    public void run() {
	Connection conn = connectionFactory.getConnection();
	try {
	    PreparedStatement ins = conn
		    .prepareStatement("insert into PRICES(TICKER, PRICE_TYPE, PRICE) values(?,?,?)");
	    ins.setString(1, quote.getTicker());
	    ins.setString(2, quote.getType().name());
	    ins.setBigDecimal(3, quote.getPrice());

	    conn.setAutoCommit(true);
	    ins.executeUpdate();
	    conn.setAutoCommit(false);
	    connectionFactory.release(conn);

	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }
}
