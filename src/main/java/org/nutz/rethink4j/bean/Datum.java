package org.nutz.rethink4j.bean;

import org.nutz.json.Json;
import org.nutz.json.JsonFormat;

public class Datum {

	protected DatumType tp;
	protected Object val;
	
	public Datum(Object val) {
		this.val = val;
		if (val == null) {
			tp = DatumType.R_NULL;
		} else if (val instanceof Number) {
			tp = DatumType.R_NUM;
		} else if (val instanceof Boolean) {
			tp = DatumType.R_BOOL;
		} else if (val instanceof String) {
			tp = DatumType.R_STR;
		} else if (val.getClass().isArray()) {
			tp = DatumType.R_ARRAY;
		} else {
			tp = DatumType.R_OBJECT;
		}
	}
	
	public Datum(Object val, DatumType tp) {
		this.val = val;
		this.tp = tp;
	}
	
	public String toJson(JsonFormat jf){
		return Json.toJson(val, jf);
	}
}
