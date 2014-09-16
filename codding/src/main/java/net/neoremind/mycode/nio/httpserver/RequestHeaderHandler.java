package net.neoremind.mycode.nio.httpserver;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class RequestHeaderHandler {

	public static enum Verb {
		CONNECT, DELETE, GET, HEAD, OPTIONS, PATCH, POST, PUT, TRACE
	}

	public static enum Version {
		HTTP10, HTTP11
	}

	private static CharsetDecoder decoder = Charset.forName("ISO-8859-1").newDecoder();
	private static final byte[] END = new byte[] { 13, 10, 13, 10 };
	private static final byte[] GET = new byte[] { 71, 69, 84, 32 };
	private static final byte[] HEAD = new byte[] { 72, 69, 65, 68 };

	/**
	 * 
	 * @param data
	 *            to search from
	 * @param tofind
	 *            target
	 * @param start
	 *            start index
	 * @return index of the first find if find, data.length if not find
	 */
	public static int findSub(byte[] data, byte[] tofind, int start) {
		int index = data.length;
		outer: for (int i = start; i < data.length; ++i) {

			for (int j = 0; j < tofind.length;) {
				if (data[i] == tofind[j]) {
					++i;
					++j;
					if (j == tofind.length) {
						index = i - tofind.length;
						break outer;
					}
				} else {
					i = i - j; // step back
					break;
				}
			}
		}
		return index;
	}

	// private static Logger logger = Logger.getLogger(HttpRequestHeader.class);

	/**
	 * same as {@link #findSub(byte[], byte[], int)},except find from end to
	 * start;
	 * 
	 * @param data
	 *            to search from
	 * @param tofind
	 *            target
	 * @param start
	 *            start index
	 * @return index of the first find if find, data.length if not find
	 */
	public static int findSubFromEnd(byte[] data, byte[] tofind, int start) {
		int index = data.length;
		outer: for (int i = data.length - tofind.length; i > 0; --i) {

			for (int j = 0; j < tofind.length;) {
				if (data[i] == tofind[j]) {
					++i;
					++j;
					if (j == tofind.length) {
						index = i - tofind.length;
						break outer;
					}
				} else {
					i = i - j; // step back
					break;
				}
			}
		}
		return index;
	}

	public static void main(String[] args) throws CharacterCodingException {

		CharBuffer s = decoder.decode(ByteBuffer.wrap(new byte[] { 72, 69, 65,
				68 }));

		System.out.println(s);

		byte[] data = { 12, 13, 10, 13, 13, 10, 13, 10, 13, 17 };

		int i = findSubFromEnd(data, END, 0);
		data = new byte[] { 12, 13, 10, 13, 13, 10, 13, 10, 10, 10 };
		i = 0;
		while (i != -1) {
			i = findSub(data, new byte[] { 13, 10 }, i);
			System.out.println(i);
		}

	}

	// private Version version;
	private boolean begin = false;

	private CharBuffer charBuffer = ByteBuffer.allocate(2048).asCharBuffer();

	private Map<String, String> headerMap = new TreeMap<String, String>();

	private String resouce;
	private Verb verb;

	public boolean appendSegment(byte[] segment) {
		int beginIndex = 0;

		if (begin == false) {

			if ((beginIndex = findSub(segment, GET, 0)) != segment.length) {
				begin = true;
				headerMap.clear();
				verb = Verb.GET;

			} else if ((beginIndex = findSub(segment, HEAD, 0)) != segment.length) {
				begin = true;
				headerMap.clear();
				verb = Verb.HEAD;

			} else {
				// not begin yet, and find no begin, just return false;
				return false;

			}
		}

		int endIndex = findSubFromEnd(segment, END, 0);
		ByteBuffer b = ByteBuffer.wrap(segment, beginIndex, endIndex);
		decoder.decode(b, charBuffer, endIndex != segment.length);
		if (endIndex != segment.length) {
			extractValueAndReset();
			return true;
		}
		return false;
	}

	private void extractValueAndReset() {
		charBuffer.flip();
		String head = charBuffer.toString();
		String[] lines = head.split("\r\n");
		String[] split = lines[0].split(" ");

		resouce = split[1];

		String[] temp = null;
		for (int i = 1; i < lines.length; ++i) {
			temp = lines[i].split(":");
			headerMap.put(temp[0], temp[1]);
		}

		charBuffer.clear();
		decoder.reset();
		begin = false;
	}

	public String getHeader(String key) {
		return headerMap.get(key);
	}

	public Set<String> getHeaders() {
		return headerMap.keySet();
	}

	public String getResouce() {
		if (resouce.endsWith("/")) {
			resouce = resouce + "index.html";
		}
		return resouce;
	}

	/**
	 * 
	 * @return currently, only GET [71,69,84,32],and HEAD [72, 69, 65, 68] is
	 *         supported
	 */
	public Verb getVerb() {
		return verb;
	}

	public Version getVersion() {
		throw new RuntimeException("not implement yet");
		// return version;
	}
}
