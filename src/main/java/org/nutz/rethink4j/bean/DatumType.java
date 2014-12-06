package org.nutz.rethink4j.bean;

public enum DatumType {
	R_NULL(1),
	R_BOOL(2),
	R_NUM(3), // a double
	R_STR(4),
	R_ARRAY(5),
	R_OBJECT(6),
	R_JSON(7), // uses r_str
	;
	
	public int index;
	
	DatumType(int index) {
		this.index = index;
	}
}
