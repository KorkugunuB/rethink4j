package org.nutz.rethink4j.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.nutz.http.Http;
import org.nutz.json.Json;
import org.nutz.lang.Files;
import org.nutz.lang.Strings;

public class ApiBuilder {

	public static void main(String[] args) {
		File tt = new File("term_type");
		if (!tt.exists()) {
			File f = new File("ql2.proto");
			String URL = "https://raw.githubusercontent.com/rethinkdb/rethinkdb/v1.15.x/src/rdb_protocol/ql2.proto";
			String content = Http.get(URL).getContent();
			Files.write(f, Http.get(URL).getContent());
			int start = content.indexOf("    enum TermType {")
					+ "    enum TermType {".length();
			int end = content.indexOf("\n    }", start);
			Files.write(tt, content.substring(start, end));
		}
		List<String> list = new ArrayList<String>();
		List<String> lines = Files.readLines(tt);
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i).trim();
			if (line.length() == 0)
				continue;
			if (line.startsWith("//"))
				continue;
			String TT = line;
			if (TT.endsWith("|")) {
				i++;
				for (; i < lines.size(); i++) {
					line = lines.get(i).trim();
					if (line.startsWith("//")) {
						TT += line.substring(2);
						if (!line.endsWith("|"))
							break;
					} else {
//						System.out.println(line);
						i--;
						break;
					}
				}
			}
			if (TT.contains("=>")) {
//				System.out.println(TT);
				TT = TT.replace("=>", "->");
			}
			if (TT.endsWith("|")) {
//				System.out.println(TT);
				TT = TT.substring(0, TT.length() - 1);
			}
			list.add(TT.trim());
		}
//		System.out.println("--------------------------");
		for (String TT : list) {
			p(TT);
		}
	}
	
	public static void p(String TT) {
//		System.out.println(TT);
		String[] T = Strings.splitIgnoreBlank(TT.substring(0, TT.indexOf(";")).trim(), " ");
		
		if (T.length > 0) {
			// uncommet blower for create TermType items
			// System.out.printf("\t%-20s(%3s),%s\n", T[0].trim(), T[2], TT.contains("//") ? TT.substring(TT.indexOf("//")) : "");
			//return; 
		}
		System.out.println("  -- " + Json.toJson(T));
		if (TT.contains("//")) {
			String D = TT.substring(TT.indexOf("//") + 2).trim();
			System.out.println("  -- " + D);
			String[] cons = Strings.splitIgnoreBlank(D, "\\|");
			System.out.println("    -- " + Json.toJson(cons));
			for (String con : cons) {
				String[] sp = Strings.splitIgnoreBlank(con, "->");
				System.out.println("       -- " + Json.toJson(sp));
				if (sp.length == 2) {
					String opts = null;
					String ARGS = sp[0].trim();
					if (ARGS.endsWith("}")) {
						ARGS = ARGS.substring(0, ARGS.indexOf("{")).trim();
						opts = sp[0].substring(sp[0].indexOf("{")).trim();
					}
					String[] zp = Strings.splitIgnoreBlank(ARGS, ",");
					System.out.println("         -- " + Json.toJson(zp) + (opts == null ? "" : " , " + opts));
				}
			}
		} else {
			System.out.println("  ---");
		}
	}
}
