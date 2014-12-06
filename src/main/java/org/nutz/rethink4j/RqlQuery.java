package org.nutz.rethink4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.lang.util.NutMap;
import org.nutz.rethink4j.bean.Term;
import org.nutz.rethink4j.bean.TermType;
import org.nutz.rethink4j.drop.RqlExec;
import org.nutz.rethink4j.drop.RqlFunction;

public class RqlQuery extends NutMap {

	private static final long serialVersionUID = 5361885125460551228L;
	//--------------------------------------------------------------------
	// 特殊参数相关的设置
	protected Map<String, Object> optargs;
	protected synchronized void setOptargs(String key, Object value) {
		if (optargs == null)
			optargs = new HashMap<String, Object>();
		optargs.put(key, value);
	}
	public RqlQuery db(String db) {
		setOptargs("db", db);
		return this;
	}
	public RqlQuery use_outdated(boolean flag) {
		setOptargs("use_outdated", false);
		return this;
	}
	public RqlQuery noreply(boolean flag) {
		setOptargs("noreply", flag);
		return this;
	}
	public RqlQuery durability(boolean hard) {
		setOptargs("durability", hard ? "hard" : "soft");
		return this;
	}
	public RqlQuery profile(boolean flag) {
		setOptargs("profile", flag);
		return this;
	}
	//--------------------------------------------------------------------
	
	public void CMD(TermType tt, List<Term> args, List<Map<String, Term>> optargs) {
		
	}

	public RqlQuery table(String table) {
		put("table", table);
		return this;
	}
	//----------------------------------
	// 数据库操作相关
	public RqlExec<Map<String,Object>> dbCreate(String name) {
		return null;
	}
	
	public RqlExec<Map<String, Object>> dbDrop(String name) {
		return null;
	}
	public RqlExec<List<String>> dbList() {
		return null;
	}
	// 表操作相关
	public RqlExec<Map<String, Object>> tableCreate(String name) {
		return tableCreate(name, null);
	}
	public RqlExec<Map<String, Object>> tableCreate(String name, Map<String, Object> options) {
		return null;
	}
	public RqlExec<Map<String, Object>> tableDrop(String name) {
		return null;
	}
	public RqlExec<List<String>> tableList() {
		return null;
	}
	// 索引操作相关
	public RqlExec<Map<String, Object>> indexCreate(String name, RqlFunction func, boolean multi) {
		return null;
	}
	public RqlExec<Map<String, Object>> indexDrop(String name) {
		return null;
	}
	public RqlExec<List<String>> indexList() {
		return null;
	}
	public RqlExec<Map<String, Object>> indexRename(String oldIndexName, String newIndexName, boolean overwrite) {
		return null;
	}
	public RqlExec<List<Map<String, Object>>> indexStatus() {
		return null;
	}
	public RqlExec<List<Map<String, Object>>> indexWait() {
		return null;
	}
//	public RqlExec<List<Map<String, Object>>> changes() {
//		return null;
//	}
	
	//----------------------------------------------------------
	// 写入数据相关
	public RqlExec<Map<String, Object>> insert(Object obj) {
		return null;
	}
	public RqlExec<Map<String, Object>> insert(Object obj, Map<String, Object> options) {
		return null;
	}
	public RqlExec<Map<String, Object>> update(Map<String, Object> map, Map<String, Object> options) {
		return null;
	}
	public RqlExec<Map<String, Object>> replace(Map<String, Object> map, Map<String, Object> options) {
		return null;
	}
	public RqlExec<Map<String, Object>> delete(Map<String, Object> options) {
		return null;
	}
	public RqlExec<Map<String, Object>> sync() {
		return null;
	}
	//---------------------------------------------------------
	// 更新数据相关
	// db(options);
	// table(options);
	public RqlQuery get(String id) {
		return this;
	}
	public RqlQuery getAll(String... ids) {
		return this;
	}
	public RqlQuery getAll(List<String> ids, String indexName) {
		return this;
	}
	public RqlQuery between(String lowerKey, String upperKey, String indexName) {
		return this;
	}
}
