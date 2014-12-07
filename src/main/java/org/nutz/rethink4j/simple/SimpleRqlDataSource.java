package org.nutz.rethink4j.simple;

import org.nutz.rethink4j.RqlConnection;
import org.nutz.rethink4j.util.RqlDataSource;

public class SimpleRqlDataSource implements RqlDataSource {

	protected String host;
	protected int port;
	protected String authkey;
	protected int timeout;
	public SimpleRqlDataSource(String host, int port, String authkey, int timeout) {
		super();
		this.host = host;
		this.port = port;
		this.authkey = authkey;
		this.timeout = timeout;
	}
	
	public RqlConnection getConnection() {
		return new RqlConnection(host, port, authkey, timeout);
	}
}
