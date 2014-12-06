package org.nutz.rethink4j.bean;

public enum QueryType {
	START(1), CONTINUE(2), STOP(3), NOREPLY_WAIT(4);
	
	public byte[] data;
	
	QueryType(int i) {
		data = Integer.toString(i).getBytes();
	}
}
