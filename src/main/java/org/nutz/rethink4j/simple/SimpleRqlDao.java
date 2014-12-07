package org.nutz.rethink4j.simple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.nutz.dao.pager.Pager;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Logs;
import org.nutz.log.Log;
import org.nutz.rethink4j.RethinkRuntimeException;
import org.nutz.rethink4j.RqlConnection;
import org.nutz.rethink4j.bean.Term;
import org.nutz.rethink4j.util.RqlDataSource;

@SuppressWarnings("unchecked")
public class SimpleRqlDao implements RqlDao {
	
	protected RqlDataSource ds;
	
	protected Term db;
	
	protected static Log log = Logs.get();

	public SimpleRqlDao(RqlDataSource ds, String db) {
		this.ds = ds;
		this.db = Term.mk("db", Term.mkDatum(db));
	}
	
	public void createTable(String table, Map<String, Term> optargs) {
		Term tableCreate = Term.mk("table_create", db, Term.mkDatum(table)); // java r.db(dnName).tableCreate(tableName).run(conn);
		_run(tableCreate, optargs);
	}
	
	public void insert(String tableName, Object ... objs) {
		List<Term> list = new ArrayList<Term>();
		for (Object obj : objs) {
			if (obj == null)
				continue;
			Map<String, Object> map = null;
			if (obj instanceof Map) {
				map = (Map<String, Object>) obj;
			} else {
				map = Lang.obj2map(obj);
			}
			if (map.isEmpty())
				continue;
			list.add(Term.mkDatum(map));
		}
		RqlConnection conn = ds.getConnection();
		try {
			Term insert = Term.mk("insert", table(tableName), Term.mk("make_array", list));
			NutMap re = conn.startQuery(0, insert, null);
			Map<String, Object> r = (Map<String, Object>) re.getAs("r", List.class).get(0);
			log.debug("insert result: " + r);
			if (r.get("errors") != null && (Integer)r.get("errors") != 0)
				throw new RethinkRuntimeException("insert fail: " + r);
		} finally {
			conn.close();
		}
	}

	public void tableCreate(String tableName, Map<String, Object> opts) {
		Term tc = Term.mk("table_create", Arrays.asList(db, Term.mkDatum(tableName)), to_optargs(opts));
		_run(tc, null);
	}

	public void tableDrop(String tableName) {
		_run(Term.mk("table_drop", db, Term.mkDatum(tableName)));
	}

	public List<String> tableList() {
		return (List<String>) _run(Term.mk("table_list", db)).getAs("r", List.class).get(0);
	}

	public boolean tableExist(String tableName) {
		return tableList().contains(tableName);
	}

	public List<String> dbList() {
		return (List<String>) _run(Term.mk("db_list")).getAs("r", List.class).get(0);
	}

	public void dbCreate(String dbName, Map<String, Object> opts) {
		_run(Term.mk("db_create", Arrays.asList(Term.mkDatum(dbName))), to_optargs(opts));
	}

	public void dbDrop(String dbName) {
		_run(Term.mk("db_drop", Term.mkDatum(dbName)));
	}

	public boolean dbExist(String dbName) {
		return dbList().contains(dbName);
	}
	
	public void indexCreate(String tableName, String indexName, Map<String, Object> optargs) {
		_run(Term.mk("index_create", Arrays.asList(table(tableName), Term.mkDatum(indexName)), to_optargs(optargs)));
	}
	
	public void indexDrop(String tableName, String indexName) {
		_run(Term.mk("index_drop", Arrays.asList(table(tableName), Term.mkDatum(indexName))));
	}
	
	public boolean indexExist(String tableName, String indexName) {
		return indexList(tableName).contains(indexName);
	}
	
	public List<String> indexList(String tableName) {
		return (List<String>) _run(Term.mk("index_list", table(tableName))).getAs("r", List.class).get(0);
	}

	public Map<String, Object> fetch(String tableName, Object key) {
		Term get = Term.mk("get", table(tableName), Term.mkDatum(key));
		return (Map<String, Object>) _run(get).getAs("r", List.class).get(0);
	}

	public void delete(String tableName, Object key) {
		_run(Term.mk("delete", Term.mk("get", table(tableName), Term.mkDatum(key))));
	}

	public void updateOne(String tableName, Object key, Map<String, Object> up) {
		_run(Term.mk("update", Term.mk("get", table(tableName), Term.mkDatum(key)), Term.mkDatum(up)));
	}
	
	public void update(String tableName, String js, Map<String, Object> up) {
		if (js == null)
			_run(Term.mk("update", table(tableName), Term.mkDatum(up)));
		else
			_run(Term.mk("update", filter_js(table(tableName), js), Term.mkDatum(up)));
	}

	public List<Map<String, Object>> query(String tableName, String js, Map<String, String> order, Pager pager) {
		Term t = table(tableName);
		if (!Strings.isBlank(js)) {
			t = filter_js(t, js);
		}
		if (order != null && order.size() > 0) {
			for (Entry<String, String> en : order.entrySet()) {
				String key = en.getKey();
				if (!key.startsWith("index:"))
					t = Term.mk("orderby", t, Term.mk(en.getValue(), Term.mkDatum(en.getKey())));
				else {
					t= Term.mk("orderby", Arrays.asList(t), Term.mkOptargs("index", Term.mk(en.getValue(), Term.mkDatum(key.substring(6)))));
				}
			}
		}
		if (pager != null) {
			if (pager.getOffset() > 0){
				t = Term.mk("skip", t, Term.mkDatum(pager.getOffset()));
			}
			if (pager.getPageSize() > 0) {
				t = Term.mk("limit", t, Term.mkDatum(pager.getPageSize()));
			}
		}
		System.out.println(Json.toJson(t, JsonFormat.compact()));
		return (List<Map<String, Object>>) _run(t).get("r");
	}
	
	protected Map<String, Term> to_optargs(Map<String, Object> _args) {
		Map<String, Term> optargs = new HashMap<String, Term>();
		if (_args == null)
			return optargs;
		for (Entry<String, Object> en : _args.entrySet()) {
			if (en.getValue() == null)
				continue;
			if (en.getValue() instanceof Term) {
				optargs.put(en.getKey(), (Term)en.getValue());
			} else {
				optargs.put(en.getKey(), Term.mkDatum(en.getValue()));
			}
		}
		return optargs;
	}
	
	public long count(String tableName, String js) {
		if (js != null)
			return ((Number) _run(Term.mk("count", filter_js(table(tableName), js))).getAs("r", List.class).get(0)).longValue();
		return ((Number) _run(Term.mk("count", table(tableName))).getAs("r", List.class).get(0)).longValue();
	}
	
	protected NutMap _run(Term t) {
		return _run(t, null);
	}
	
	protected Term table(String tableName) {
		return Term.mk("table", db, Term.mkDatum(tableName));
	}
	
	protected Term js(String js) {
		if (!js.startsWith("(function"))
			js = "(function(row){ return " + js +";})";
		log.debug("javascript: " + js);
		return Term.mk("javascript", Term.mkDatum(js));
	}
	
	protected Term filter_js(Term t, String js_str) {
		return Term.mk("filter", t, js(js_str));
	}
	
	protected NutMap _run(Term t, Map<String, Term> optargs) {
		RqlConnection conn = ds.getConnection();
		try {
			NutMap re = conn.startQuery(0, t, optargs);
			log.debug("_run result: " + re);
			if (re.getInt("t") > 4)
				throw new RethinkRuntimeException(re.toString());
			return re;
		} finally {
			conn.close();
		}
	}
}
