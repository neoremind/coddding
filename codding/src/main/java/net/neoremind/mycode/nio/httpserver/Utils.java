package net.neoremind.mycode.nio.httpserver;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPOutputStream;

public class Utils {

	public static void closeQuietly(Closeable is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 
	 * @param file
	 *            the absolute file path
	 * @param zip
	 *            gzip or not
	 * @return byte array of the file
	 * 
	 * @throws IOException
	 */
	public static byte[] file2ByteArray(File file, boolean zip) throws IOException {
		InputStream is = null;
		GZIPOutputStream gzip = null;
		byte[] buffer = new byte[8912];
		ByteArrayOutputStream baos = new ByteArrayOutputStream(8912);
		try {
			if (zip) {
				gzip = new GZIPOutputStream(baos);
			}

			is = new BufferedInputStream(new FileInputStream(file));
			int read = 0;
			while ((read = is.read(buffer)) != -1) {
				if (zip) {
					gzip.write(buffer, 0, read);
				} else {
					baos.write(buffer, 0, read);
				}
			}
		} catch (IOException e) {
			throw e;
		} finally {
			closeQuietly(is);
			closeQuietly(gzip);
		}
		return baos.toByteArray();

	}
}
