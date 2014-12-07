package org.nutz.rethink4j.simple;

import org.nutz.rethink4j.RqlConnection;
import org.nutz.rethink4j.util.RqlDataSource;

public class SimpleRqlDataSource implements RqlDataSource {

	protected String host;
	protected int port;
	protected String authkey;
	protected int timeout;
	
	public SimpleRqlDataSource() {
		this(null, 0, null, -1);
	}
	
	public SimpleRqlDataSource(String host, int port, String authkey, int timeout) {
		super();
		this.host = host == null ? "localhost" : host;
		this.port = port == 0 ? 28015 : port;
		this.authkey = authkey;
		this.timeout = timeout < 1 ? 2000 : timeout;
	}
	
	public RqlConnection getConnection() {
		return new RqlConnection(host, port, authkey, timeout);
	}
}
