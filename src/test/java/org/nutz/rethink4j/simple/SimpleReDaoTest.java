package org.nutz.rethink4j.simple;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.nutz.dao.pager.Pager;
import org.nutz.lang.random.R;
import org.nutz.lang.util.NutMap;
import org.nutz.rethink4j.RethinkRuntimeException;
import org.nutz.rethink4j.util.RqlDataSource;
import org.nutz.rethink4j.util.RqlOrderBuilder;

public class SimpleReDaoTest {
	
	SimpleRqlDao dao;
	
	@Before
	public void init() {
		RqlDataSource ds = new SimpleRqlDataSource();
		dao = new SimpleRqlDao(ds, "walnut_test");
	}

	@Test
	public void testInsert() {
		String id = UUID.randomUUID().toString();
		NutMap user_40 = new NutMap().setv("id", id).setv("age", 40);
		NutMap user_50 = new NutMap().setv("id", UUID.randomUUID().toString()).setv("age", 50);
		dao.insert("user", user_40, user_50);
		System.out.println(dao.dbList());
		assertNotNull(dao.fetch("user", id));
		System.out.println(dao.fetch("user", id));
		System.out.println(dao.count("user", null));
		System.out.println(dao.count("user", "row.age > 50"));
		System.out.println(dao.query("user", null, null, null));
		Pager pager = new Pager();
		pager.setPageNumber(2);
		pager.setPageSize(9);
		System.out.println(dao.query("user", "row.age < 70", null, pager).size());
		if (!dao.indexExist("user", "age"))
			dao.indexCreate("user", "age", null);
		dao.query("user", null, new RqlOrderBuilder().asc("index:age").make(), null);
		dao.query("user", null, new RqlOrderBuilder().asc("age").desc("city").make(), null);
	}

	@Test
	public void test_update_one() {
		dao.tableDrop("user");
		dao.tableCreate("user", null);
		dao.indexCreate("user", "age", null);
		
		assertTrue(dao.indexExist("user", "age"));
		
		String id = R.UU16();
		NutMap user_40 = new NutMap().setv("id", id).setv("age", 40);
		dao.insert("user", user_40);
		dao.updateOne("user", id, new NutMap().setv("age", 30));
		NutMap user_new = new NutMap(dao.fetch("user", id));
		assertEquals(30, user_new.getInt("age"));
		
		try {
			dao.insert("user", user_40);
			fail("insert duplicate primary key success");
		} catch (RethinkRuntimeException e) {
		}
		user_40.setv("id", R.UU16());
		dao.insert("user", user_40);
		
		assertEquals(2, dao.count("user", null));
		assertEquals(1, dao.count("user", "row.age == 40"));
		dao.update("user", "row.age==40", new NutMap().setv("city", "richmond"));
		assertEquals("richmond", dao.query("user", "row.age==40", null, null).get(0).get("city"));
	}
}
