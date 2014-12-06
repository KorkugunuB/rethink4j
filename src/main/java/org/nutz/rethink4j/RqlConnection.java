package org.nutz.rethink4j;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Streams;
import org.nutz.lang.util.NutMap;
import org.nutz.rethink4j.bean.QueryType;
import org.nutz.rethink4j.bean.Term;

public class RqlConnection implements Closeable {
	
	private static final byte[] Version = new byte[]{0x3e, (byte)0xe8, 0x75, 0x5f}; // V_3
	private static final byte[] Protocol = new byte[]{(byte)0xc7, 0x70,0x69, 0x7e}; // JSON
	private static final byte[] EMTRY = new byte[0];
	public static AtomicLong ID = new AtomicLong(System.currentTimeMillis()); 

	String host;
	int port;
	byte[] authkey;
	int timeout;
	Socket socket = new Socket();
	DataInputStream in;
	OutputStream out;
	
	public RqlConnection(String host, int port,String authkey, int timeout) {
		super();
		if (authkey != null) {
			throw Lang.noImplement();
		}
		this.host = host;
		this.port = port;
		this.timeout = timeout;
		this.authkey = authkey == null ? EMTRY : authkey.getBytes();
	}
	
	public RqlConnection db(String db) {
		// setdb();
		return this;
	}
	
	public void close() {
		if (socket.isConnected()) {
			Streams.safeClose(socket);
		}
	}
	
	public RqlConnection connect() {
		if (!socket.isConnected()) {
			try {
				socket.connect(new InetSocketAddress(host, port), timeout);
				in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
				out = new BufferedOutputStream(socket.getOutputStream());
				out.write(Version);
				out.write(new byte[]{(byte)authkey.length, 0, 0, 0});
				if (authkey.length > 0)
					out.write(authkey);
				out.write(Protocol);
				out.flush();
				byte[] buf = new byte[8];
				int len = in.read(buf); // TODO 如果没fill完就挂了
				if (len == 8 && "SUCCESS".equals(new String(buf).trim())) {
					// ...
				} else {
					throw new RethinkRuntimeException("server resp err");
				}
			} catch (IOException e) {
				close();
				throw new RethinkRuntimeException(e);
			}
		}
		return this;
	}
	
	public RqlConnection reconnect() {
		close();
		return connect();
	}
	
	public void sendRaw(QueryType qt, long id, byte[] t, byte[] optargs) {
		try {
			if (id  == 0)
				id = ID.getAndIncrement();
			int sz = "[2]".length();
			if (qt == QueryType.START) {
				System.out.println("Term = "+new String(t));
				System.out.println("Opts = "+new String(optargs));
				sz += 1/*,*/ + t.length /*Lenght of Term*/ + 1 /*,*/ + optargs.length;
			}
			out.write(long2byte(id, sz));
			out.write('[');
			out.write(qt.data);
			if (qt == QueryType.START) {
				out.write(',');
				out.write(t);
				out.write(',');
				out.write(optargs);
			}
			out.write(']');
			out.flush();
		} catch (IOException e) {
			throw new RethinkRuntimeException(e);
		}
	}
	
	public byte[] readRaw() {
		try {
			byte[] buf = new byte[8];
			in.readFully(buf); // id, handle it in futher.
			System.out.println(Arrays.toString(buf));
			in.readFully(buf, 0, 4);
			System.out.println(Arrays.toString(buf));
			final ByteBuffer bb = ByteBuffer.wrap(buf, 0, 4);
		    bb.order(ByteOrder.LITTLE_ENDIAN);
		    int sz = bb.getInt();
		    System.out.println("sz="+sz);
		    buf = new byte[sz]; // TODO do it in a better way
		    in.readFully(buf);
			String re = new String(buf);
			System.out.println("Resp = " + re);
		    return buf;
		} catch (IOException e) {
			throw new RethinkRuntimeException(e);
		}
	}
	
	public Reader readAsReader() {
		return new InputStreamReader(new ByteArrayInputStream(readRaw()));
	}
	
	public NutMap startQuery(long id, Term t, Map<String, Term> optargs) {
		sendRaw(QueryType.START, id, Term.toBytes(t), optargs == null ? "{}".getBytes() : Json.toJson(optargs).getBytes());
		return Json.fromJson(NutMap.class, readAsReader());
	}
	
	public NutMap stopQuery(long id) {
		sendRaw(QueryType.STOP, id, null, null);
		return Json.fromJson(NutMap.class, readAsReader());
	}
	
	public NutMap conntiueQuery(long id) {
		sendRaw(QueryType.CONTINUE, id, null, null);
		return Json.fromJson(NutMap.class, readAsReader());
	}
	
	protected static byte[] long2byte(long id, int size) {
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES + Integer.BYTES);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
	    buffer.putLong(id);
	    buffer.putInt(size);
	    return buffer.array();
	}
	
	public static void main(String[] args) {
		RqlConnection conn = new RqlConnection("192.168.72.106", 28015, null, 1000);
		conn.connect();
		conn.close();
	}
}
