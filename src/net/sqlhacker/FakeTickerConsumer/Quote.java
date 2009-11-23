package net.sqlhacker.FakeTickerConsumer;

import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;

public class Quote {

    private final String ticker;
    private final PriceType type;
    private final BigDecimal price;

    public Quote(JSONObject json) throws JSONException {
	this.ticker = json.getString("TICKER");
	this.type = PriceType.valueOf(json.getString("TYPE"));
	this.price = new BigDecimal(json.getString("PRICE"));
    }

    public Quote(String ticker, PriceType type, BigDecimal price) {
	super();
	this.ticker = ticker;
	this.type = type;
	this.price = price;
    }

    public String getTicker() {
	return ticker;
    }

    public PriceType getType() {
	return type;
    }

    public BigDecimal getPrice() {
	return price;
    }

    @Override
    public String toString() {
	return "{" + "'TICKER' => '" + ticker + "', " + "'TYPE' => '"
		+ type.name() + "', " + "'PRICE' => " + price.toString() + "};";
    }

}
