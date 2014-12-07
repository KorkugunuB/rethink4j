package org.nutz.rethink4j.simple;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.nutz.dao.pager.Pager;
import org.nutz.lang.util.NutMap;
import org.nutz.rethink4j.util.RqlDataSource;

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
	}

}
