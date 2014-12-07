package org.nutz.rethink4j.bean;

import static org.junit.Assert.*;

import org.junit.Test;

public class TermTest {

	@Test
	public void testFromString() {
		Term dbList = Term.mk("db_list");
		String re = dbList.toJson(null);
		Term r = Term.from(re);
		String re2 = r.toJson(null);
		assertEquals(re, re2);
	}

}
