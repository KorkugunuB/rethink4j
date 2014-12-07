package org.nutz.rethink4j.util;

import org.nutz.rethink4j.RqlConnection;

public interface RqlDataSource {

	RqlConnection getConnection();
}
