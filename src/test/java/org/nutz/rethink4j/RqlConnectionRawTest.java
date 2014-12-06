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
		checkSuccessAtom(map);
		List<Object> r = map.getAs("r", List.class);
		List<String> dbNames = (List<String>) r.get(0);
		
		String dbName = "walnut_test";
		
		// 如果存在,那么删掉数据库
		if (dbNames.contains(dbName)) {
			Term dbDrop = Term.mk("db_drop", Term.mkDatum(dbName));
			map = conn.startQuery(0, dbDrop, null);
			checkSuccessAtom(map);
		}
		
		// 创建数据库
		Term dbCreate = Term.mk("db_create", Term.mkDatum(dbName));
		map = conn.startQuery(0, dbCreate, null);
		checkSuccessAtom(map);
		
		// 再查一次
		map = conn.startQuery(0, dbList, null);
		checkSuccessAtom(map);
		r = map.getAs("r", List.class);
		dbNames = (List<String>) r.get(0);
		assertTrue(dbNames.contains(dbName));
		
		String tableName = "user";
		// 查一下表
		Term tableList = Term.mk("table_list", Term.mk("db", Term.mkDatum(dbName)));
		map = conn.startQuery(0, tableList, Term.mkOptargs("db", dbName));
		checkSuccessAtom(map);
		r = map.getAs("r", List.class);
		List<String> tableNames = (List<String>) r.get(0);
		Term tableDrop =  Term.mk("table_drop", Term.mk("db", Term.mkDatum(dbName)), Term.mkDatum(tableName));
		if (tableNames.contains(tableName)) { // 存在就先删除
			map = conn.startQuery(0, tableDrop, null);
			checkSuccessAtom(map);
		}
		// 建表
		Term tableCreate = Term.mk("table_create", Term.mk("db", Term.mkDatum(dbName)), Term.mkDatum(tableName));
		map = conn.startQuery(0, tableCreate, null);
		checkSuccessAtom(map);
		
		// 查一下是不是真的有
		map = conn.startQuery(0, tableList, Term.mkOptargs("db", dbName));
		checkSuccessAtom(map);
		r = map.getAs("r", List.class);
		tableNames = (List<String>) r.get(0);
		assertTrue(tableNames.toString(), tableNames.contains(tableName));
		
		// 删表
		map = conn.startQuery(0, tableDrop, null);
		checkSuccessAtom(map);
		
		// 再查, 应该是没有的
		map = conn.startQuery(0, tableList, Term.mkOptargs("db", dbName));
		checkSuccessAtom(map);
		r = map.getAs("r", List.class);
		tableNames = (List<String>) r.get(0);
		assertFalse(tableNames.contains(tableName));
		
	}

	public void checkSuccessAtom(NutMap map) {
		assertEquals(map.getAs("r", List.class).get(0).toString(), 1, map.getInt("t"));
	}
}
