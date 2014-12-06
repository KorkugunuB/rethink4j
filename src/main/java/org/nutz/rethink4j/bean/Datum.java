package org.nutz.rethink4j.bean;

import org.nutz.json.Json;
import org.nutz.json.JsonFormat;

public class Datum {

	protected Object val;
	
	public Datum(Object val) {
		this.val = val;
	}
	
	public String toJson(JsonFormat jf){
		return Json.toJson(val, jf);
	}
}
