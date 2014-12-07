package org.nutz.rethink4j.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class RqlOrderBuilder {

	protected Map<String, String> map = new LinkedHashMap<String, String>();
	
	public RqlOrderBuilder asc(String name) {
		map.put(name, "asc");
		return this;
	}
	
	public RqlOrderBuilder desc(String name) {
		map.put(name, "desc");
		return this;
	}
	
	public Map<String, String> make() {
		return new LinkedHashMap<String, String>(map);
	}
}
