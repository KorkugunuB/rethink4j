package org.nutz.rethink4j.simple;

import java.util.List;
import java.util.Map;

import org.nutz.dao.pager.Pager;

public interface RqlDao {
	
	
	// table
	void tableCreate(String tableName, Map<String, Object> opts);
	void tableDrop(String tableName);
	List<String> tableList();
	boolean tableExist(String tableName);
	
	// db
	List<String> dbList();
	void dbCreate(String dbName, Map<String, Object> opts);
	void dbDrop(String dbName);
	boolean dbExist(String dbName);
	
	// index
	void indexCreate(String tableName, String indexName, Map<String, Object> optargs);
	void indexDrop(String tableName, String indexName);
	List<String> indexList(String tableName);
	boolean indexExist(String tableName, String indexName);
	//void indexRename();
	//void indexStatus();
	//void indexWait();
	
	void insert(String tableName, Object...objs);
	Map<String, Object> fetch(String tableName, Object key);
	void delete(String tableName, Object key);
	void updateOne(String tableName, Object key, Map<String, Object> up);
	void update(String tableName, String js, Map<String, Object> up);
	//List<Map<String, Object>> query(String tableName, Map<String, Object> cnd, Pager pager);
	List<Map<String, Object>> query(String tableName, String js, Map<String, String> order,  Pager pager);
	long count(String tableName, String js);
}
