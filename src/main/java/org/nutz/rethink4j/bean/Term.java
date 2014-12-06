package org.nutz.rethink4j.bean;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.util.NutMap;

public class Term {

	TermType tt;
	Datum datum;
	List<Object> fz;
	
	public Term(Datum datum) {
		super();
		this.tt = TermType.DATUM;
		this.datum = datum;
	}

	public Term(TermType tt, List<Term> args, Map<String, Term> optargs) {
		this.tt = tt;
		fz = new ArrayList<Object>();
		fz.add(tt.index);
		if(args == null && optargs == null) {
			return;
		} else {
			fz.add(args == null ? Collections.EMPTY_LIST : args);
			if (optargs != null)
				fz.add(optargs);
		}
	}
	
	public String toJson(JsonFormat jf) {
		if (tt == TermType.DATUM) {
			return datum.toJson(jf);
		} else {
			return Json.toJson(fz, jf);
		}
	}
	
	public String toString() {
		return toJson(JsonFormat.full());
	}
	
	public static Term mkDatum(Object val) {
		if (val != null && val instanceof Term) {
			return (Term)val;
		}
		return new Term(new Datum(val));
	}

	public static Term mk(TermType tt, List<Term> args, Map<String, Term> optargs) {
		return new Term(tt, args, optargs);
	}
	public static Term mk(TermType tt, List<Term> args) {
		return mk(tt, args, null);
	}
	public static Term mk(TermType tt) {
		return mk(tt, null, null);
	}
	public static Term mk(String name, List<Term> args, Map<String, Term> optargs) {
		return mk(TermType.valueOf(name.toUpperCase()), args, optargs);
	}
	public static Term mk(String name, List<Term> args) {
		return mk(name, args, null);
	}
	public static Term mk(String name) {
		return mk(name, null, null);
	}
	public static Term mk(String name, Term...args) {
		return mk(name, Arrays.asList(args), null);
	}
	public static Term mk(TermType tt, Term...args) {
		return mk(tt, Arrays.asList(args), null);
	}
//	public static Term mk(String name, Object arg) {
//		return mk(name, Arrays.asList(mkDatum(arg)), null);
//	}
//	public static Term mk(TermType tt, Object arg) {
//		return mk(tt, Arrays.asList(mkDatum(arg)), null);
//	}
	public static Map<String, Term> mkOptargs(String key, Object val) {
		Map<String, Term> optargs = new HashMap<String, Term>();
		optargs.put(key, mkDatum(val));
		return optargs;
	}
	
	public static byte[] toBytes(Term t) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Json.toJson(new OutputStreamWriter(out), t, JsonFormat.compact());
		return out.toByteArray();
	}
	
	public static void main(String[] _args) {
		Term t = mk("insert", Arrays.asList(
								mk("table", mkDatum("walnut")), 
								mk("make_array", mkDatum(new NutMap()), mkDatum(new NutMap())
							  )
				), mkOptargs("durability", "hard"));
		System.out.println(t.toJson(JsonFormat.compact()));
	}
}
