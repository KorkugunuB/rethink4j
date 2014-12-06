package org.nutz.rethink4j;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nutz.lang.util.NutMap;
import org.nutz.rethink4j.bean.Term;

public class RqlConnectionRawTest {
	
	RqlConnection conn;
	
	@Before
	public void init() {
		conn = new RqlConnection("192.168.72.106", 28015, null, 1000);
		conn.connect();
	}
	
	@After
	public void depose() {
		if (conn != null)
			conn.close();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_raw_db_list() {
		Term dbList = Term.mk("db_list");
		NutMap map = conn.startQuery(0, dbList, null);
		assertEquals(1, map.getInt("t"));
		List<Object> r = map.getAs("r", List.class);
		List<String> dbNames = (List<String>) r.get(0);
		
		String dbName = "walnut_test";
		
		// 如果存在,那么删掉数据库
		if (dbNames.contains(dbName)) {
			Term dbDrop = Term.mk("db_drop", Term.mkDatum(dbName));
			map = conn.startQuery(0, dbDrop, null);
			assertEquals(1, map.get("t"));
		}
		
		// 创建数据库
		Term dbCreate = Term.mk("db_create", Term.mkDatum(dbName));
		map = conn.startQuery(0, dbCreate, null);
		assertEquals(1, map.get("t"));
		
		// 再查一次
		map = conn.startQuery(0, dbList, null);
		assertEquals(1, map.get("t"));
		r = map.getAs("r", List.class);
		dbNames = (List<String>) r.get(0);
		assertTrue(dbNames.contains(dbName));
	}

}
