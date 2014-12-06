package org.nutz.rethink4j;

public class RethinkDB {

	public static RqlConnection connect(String host, int port,String authkey, int timeout) {
		return new RqlConnection(host, port, authkey, timeout);
	}
	
	public static RqlQuery db(String db) {
		return new RqlQuery().db(db);
	}
	
	public static RqlQuery table(String table) {
		return new RqlQuery().table(table);
	}
}
