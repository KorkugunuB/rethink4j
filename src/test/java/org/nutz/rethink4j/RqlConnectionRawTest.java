package org.nutz.rethink4j;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nutz.json.JsonFormat;
import org.nutz.lang.util.NutMap;
import org.nutz.rethink4j.bean.Term;
import org.nutz.rethink4j.simple.SimpleRqlDataSource;
import org.nutz.rethink4j.util.RqlDataSource;

public class RqlConnectionRawTest {
	
	RqlConnection conn;
	
	@Before
	public void init() {
		conn = new SimpleRqlDataSource().getConnection();
	}
	
	@After
	public void depose() {
		if (conn != null)
			conn.close();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void test_raw_db_list() {
		// 带上profile
		Map<String, Term> optargs = new HashMap<String, Term>();
		//optargs.put("profile", Term.mkDatum(true));
		
		Term dbList = Term.mk("db_list"); // java. r.dbList().run(conn);
		NutMap map = conn.startQuery(0, dbList, null);
		checkSuccessAtom(map);
		List<Object> r = map.getAs("r", List.class);
		List<String> dbNames = (List<String>) r.get(0);
		
		String dbName = "walnut_test";
		
		// 如果存在,那么删掉数据库
		if (dbNames.contains(dbName)) {
			Term dbDrop = Term.mk("db_drop", Term.mkDatum(dbName)); // java: r.dbDrop(dnName).run(conn);
			map = conn.startQuery(0, dbDrop, null);
			checkSuccessAtom(map);
		}
		
		// 创建数据库
		Term dbCreate = Term.mk("db_create", Term.mkDatum(dbName)); // java: r.dbCreate(dnName).run(conn);
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
		Term tableList = Term.mk("table_list", Term.mk("db", Term.mkDatum(dbName)));// java: r.db(dnName).tableList().run(conn);
		map = conn.startQuery(0, tableList, Term.mkOptargs("db", dbName));
		checkSuccessAtom(map);
		r = map.getAs("r", List.class);
		List<String> tableNames = (List<String>) r.get(0);
		Term tableDrop =  Term.mk("table_drop", Term.mk("db", Term.mkDatum(dbName)), Term.mkDatum(tableName));// java: r.db(dnName).tableDrop(tableName).run(conn);
		if (tableNames.contains(tableName)) { // 存在就先删除
			map = conn.startQuery(0, tableDrop, null);
			checkSuccessAtom(map);
		}
		// 建表
		Term tableCreate = Term.mk("table_create", Term.mk("db", Term.mkDatum(dbName)), Term.mkDatum(tableName)); // java r.db(dnName).tableCreate(tableName).run(conn);
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
		
		
		// 试试一些无参数的方法
		Term uuid = Term.mk("uuid");
		map = conn.startQuery(0, uuid, optargs);
		checkSuccessAtom(map);
		r = map.getAs("r", List.class);
		assertEquals(1, r.size());
		assertNotNull(r.get(0));
		UUID _id = UUID.fromString(r.get(0).toString());
		System.out.println(_id);
		
		// 把表建回来
		map = conn.startQuery(0, tableCreate, optargs);
		checkSuccessAtom(map);
		
		// 插入一条数据
		NutMap user_rp = new NutMap();
		user_rp.put("id", _id.toString());
		user_rp.put("name", "mary");
		user_rp.put("age", 29);
		user_rp.put("city", "gz");
		Term insert = Term.mk("insert", Term.mk("table", Term.mk("db", Term.mkDatum(dbName)), Term.mkDatum(tableName)), Term.mkDatum(user_rp));
		map = conn.startQuery(0, insert, optargs);
		checkSuccessAtom(map);
		
		// 再插入一条
		user_rp = new NutMap();
		user_rp.put("id", UUID.randomUUID().toString());
		user_rp.put("name", "wendal");
		user_rp.put("age", 90);
		user_rp.put("city", "richmond");
		insert = Term.mk("insert", Term.mk("table", Term.mk("db", Term.mkDatum(dbName)), Term.mkDatum(tableName)), Term.mkDatum(user_rp));
		map = conn.startQuery(0, insert, optargs);
		checkSuccessAtom(map);
		
		// 插入第3条
		user_rp = new NutMap();
		user_rp.put("id", UUID.randomUUID().toString());
		user_rp.put("name", "zozoh");
		user_rp.put("age", 35);
		user_rp.put("city", "beijing");
		insert = Term.mk("insert", Term.mk("table", Term.mk("db", Term.mkDatum(dbName)), Term.mkDatum(tableName)), Term.mkDatum(user_rp));
		map = conn.startQuery(0, insert, optargs);
		checkSuccessAtom(map);
		
		// 看看总数
		
		Term user_count = Term.mk("count", Term.mk("table", Term.mk("db", Term.mkDatum(dbName)), Term.mkDatum(tableName)));
		map = conn.startQuery(0, user_count, optargs);
		checkSuccessAtom(map);
		r = map.getAs("r", List.class);
		assertEquals(3, r.get(0));
		
		// 取出第一个user (已知id)
		Term user_get = Term.mk("get", Term.mk("table", Term.mk("db", Term.mkDatum(dbName)), Term.mkDatum(tableName)), Term.mkDatum(_id.toString()));
		map = conn.startQuery(0, user_get, optargs);
		checkSuccessAtom(map);
		r = map.getAs("r", List.class);
		Map<String, Object> user = (Map<String, Object>) r.get(0);
		assertEquals(29, user.get("age"));
		assertEquals("gz", user.get("city"));
		
		// 取出age=90的user, 直接匹配方法, 传Map
		Term user_age_90 = Term.mk("filter", Term.mk("table", Term.mk("db", Term.mkDatum(dbName)), Term.mkDatum(tableName)), Term.mkDatum(new NutMap().setv("age", 90)));
		map = conn.startQuery(0, user_age_90, optargs);
		checkSuccessSequence(map);
		r = map.getAs("r", List.class);
		user = (Map<String, Object>) r.get(0);
		assertEquals(90, user.get("age"));
		
		// 取出age=90的user, 直接匹配方法, 传(eq "age" 90)
		Term eg = Term.mk("eq", Term.mk("bracket", Term.mk("implicit_var"), Term.mkDatum("age")), Term.mkDatum(90));
		Term func = Term.mk("func", Term.mk("make_array", Term.mkDatum(2)), eg);
		user_age_90 = Term.mk("filter", Term.mk("table", Term.mk("db", Term.mkDatum(dbName)), Term.mkDatum(tableName)), func);
		map = conn.startQuery(0, user_age_90, optargs);
		checkSuccessSequence(map);
		r = map.getAs("r", List.class);
		user = (Map<String, Object>) r.get(0);
		assertEquals(90, user.get("age"));
		System.out.println(user_age_90.toJson(JsonFormat.compact()));
		
		// 取出city是gz和richmond的用户
		Term row_city = Term.mk("bracket", Term.mk("implicit_var"), Term.mkDatum("city"));
		Term eg_city_gz = Term.mk("eq", row_city, Term.mkDatum("gz"));
		Term eg_city_richmond = Term.mk("eq", row_city, Term.mkDatum("richmond"));
		Term any_city = Term.mk("any", eg_city_gz, eg_city_richmond);
		func = Term.mk("func", Term.mk("make_array", Term.mkDatum(4)), any_city);
		Term user_any_city = Term.mk("filter", Term.mk("table", Term.mk("db", Term.mkDatum(dbName)), Term.mkDatum(tableName)), func);
		map = conn.startQuery(0, user_any_city, optargs);
		checkSuccessSequence(map);
		r = map.getAs("r", List.class);
		assertEquals(2, r.size());
		
		// 在上一个测试的基础上加入年龄排序, age降序
		Term age_desc = Term.mk("orderby", user_any_city, Term.mk("desc", Term.mkDatum("age")));
		map = conn.startQuery(0, age_desc, null);
		checkSuccessAtom(map); // 返回的又变成一个结果
		r = (List<Object>) map.getAs("r", List.class).get(0);
		assertEquals(2, r.size());
		assertEquals(90, ((Map)r.get(0)).get("age"));
		assertEquals(29, ((Map)r.get(1)).get("age"));
		
		// 将对city的查询改成js形式
		
		Term city_js = Term.mk("javascript", Term.mkDatum("(function (r) { return r.city == 'gz' || r.city == 'richmond';})"));
		user_any_city = Term.mk("filter", Term.mk("table", Term.mk("db", Term.mkDatum(dbName)), Term.mkDatum(tableName)), city_js);
		map = conn.startQuery(0, user_any_city, optargs);
		checkSuccessSequence(map);
		r = map.getAs("r", List.class);
		assertEquals(2, r.size());
		
		// 单单返回city为gz的记录
		city_js = Term.mk("javascript", Term.mkDatum("(function (r) { return r.city == 'gz';})"));
		user_any_city = Term.mk("filter", Term.mk("table", Term.mk("db", Term.mkDatum(dbName)), Term.mkDatum(tableName)), city_js);
		map = conn.startQuery(0, user_any_city, optargs);
		checkSuccessSequence(map);
		r = map.getAs("r", List.class);
		assertEquals(1, r.size());
		
		// 年龄小于40的记录
		Term age_js = Term.mk("javascript", Term.mkDatum("(function (r) { return r.age < 40;})"));
		Term user_age = Term.mk("filter", Term.mk("table", Term.mk("db", Term.mkDatum(dbName)), Term.mkDatum(tableName)), age_js);
		map = conn.startQuery(0, user_age, optargs);
		checkSuccessSequence(map);
		r = map.getAs("r", List.class);
		assertEquals(2, r.size());
		System.out.println(map.get("p"));
	}

	public void checkSuccessAtom(NutMap map) {
		assertEquals(map.getAs("r", List.class).get(0).toString(), 1, map.getInt("t"));
	}
	
	public void checkSuccessSequence (NutMap map) {
		assertEquals(map.getAs("r", List.class).get(0).toString(), 2, map.getInt("t"));
	}
}
