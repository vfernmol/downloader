package org.esquivo.downloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class URLDownloader implements Downloader {
	public File download(URL url) throws IOException {
		InputStream in = null;
		FileOutputStream out = null;
		try {
			URLConnection conn = url.openConnection();
			in = conn.getInputStream();
			File tempFile = File.createTempFile("urldownloader-", null);
			out = new FileOutputStream(tempFile);
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