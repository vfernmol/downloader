package org.esquivo.downloader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class BufferedHCDownloader.
 * 
 * @author woo
 */
public class BufferedHCDownloader implements Downloader {
	
	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(BufferedHCDownloader.class);
	
	/* (non-Javadoc)
	 * @see org.esquivo.downloader.Downloader#download(java.net.URL)
	 */
	public File download(URL url) throws IOException {
		HttpClient httpclient = new DefaultHttpClient();
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		try {
			HttpGet httpget = new HttpGet(url.toString());

			if(LOG.isInfoEnabled()) {
				LOG.info("executing request " + httpget.getURI());
			}

			HttpResponse response = httpclient.execute(httpget);

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				in = new BufferedInputStream(entity.getContent(), 4096);

				File tempFile = File.createTempFile("urldownloader-", null);
				out = new BufferedOutputStream(new FileOutputStream(tempFile),
						4096);
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