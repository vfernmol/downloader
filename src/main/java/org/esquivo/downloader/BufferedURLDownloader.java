package org.esquivo.downloader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class BufferedURLDownloader implements Downloader {
	public File download(URL url) throws IOException {
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		try {
			URLConnection conn = url.openConnection();
			in = new BufferedInputStream(conn.getInputStream(), 4096);
			File tempFile = File.createTempFile("urldownloader-", null);
			out = new BufferedOutputStream(new FileOutputStream(tempFile), 4096);
			int c;
			while ((c = in.read()) != -1) {
				out.write(c);
			}

			return tempFile;
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException localIOException2) {
				}
			if (out != null)
				try {
					out.close();
				} catch (IOException localIOException3) {
				}
		}
	}
}