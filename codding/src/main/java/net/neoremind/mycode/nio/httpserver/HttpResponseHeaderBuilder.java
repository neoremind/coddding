package net.neoremind.mycode.nio.httpserver;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class HttpResponseHeaderBuilder {
	
	public static final String OK_200 = "HTTP/1.1 200 OK";
	public static final String NEWLINE = "\r\n";
	public static final String NOT_FOUND_404 = "HTTP/1.1 404 Not Find";
	public static final String SERVER_ERROR_500 = "HTTP/1.1 500 Internal Server Error";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String CONNECTION = "Connection";
	public static final String CONTENT_LENGTH = "Content-Length";
	public static final String KEEP_ALIVE = "keep-alive";
	public static final String CONTENT_ENCODING = "Content-Encoding";
	public static final String ACCEPT_ENCODING = "Accept-Encoding";
	public static final String LAST_MODIFIED = "Last-Modified";
	public static final String GZIP = "gzip";

	private String status;
	private Map<String, Object> header = new TreeMap<String, Object>();

	/**
	 * status default to 200
	 */
	public HttpResponseHeaderBuilder() {
		status = OK_200;
	}

	public HttpResponseHeaderBuilder addHeader(String key, Object value) {
		header.put(key, value);
		return this;
	}

	public void clear() {
		status = OK_200;
		header.clear();
	}

	public byte[] getHeader() {
		return toString().getBytes();
	}

	public HttpResponseHeaderBuilder setStatus(String status) {
		this.status = status;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(120);
		sb.append(status).append(NEWLINE);
		Set<String> keySet = header.keySet();
		for (String key : keySet) {
			sb.append(key).append(": ").append(header.get(key)).append(NEWLINE);
		}
		sb.append(NEWLINE); // empty line;
		return sb.toString();
	}

}
