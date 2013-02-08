package org.esquivo.downloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class HCDownloader implements Downloader {
	public File download(URL url) throws IOException {
		HttpClient httpclient = new DefaultHttpClient();
		InputStream in = null;
		FileOutputStream out = null;
		try {
			HttpGet httpget = new HttpGet(url.toString());

			System.out.println("executing request " + httpget.getURI());

			HttpResponse response = httpclient.execute(httpget);

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				in = entity.getContent();

				File tempFile = File.createTempFile("urldownloader-", null);
				out = new FileOutputStream(tempFile);
				int c;
				while ((c = in.read()) != -1) {
					out.write(c);
				}

				return tempFile;
			}
			return null;
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException localIOException4) {
				}
			if (out != null) {
				try {
					out.close();
				} catch (IOException localIOException5) {
				}
			}
			httpclient.getConnectionManager().shutdown();
		}
	}
}