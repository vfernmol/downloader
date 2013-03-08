package org.esquivo.downloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class BufferedHCDownloader.
 * 
 * @author woo
 */
public class BufferedHCDownloader implements Downloader {
	private static final Logger LOG = LoggerFactory.getLogger(BufferedHCDownloader.class);
	
	private int conectionTimeout = 0;
	private int readTimeout = 0;
	
	public BufferedHCDownloader() {
		
	}

	public BufferedHCDownloader(int conectionTimeout, int readTimeout) {
	    this.conectionTimeout = conectionTimeout;
	    this.readTimeout = readTimeout;
    }

	/* (non-Javadoc)
	 * @see org.esquivo.downloader.Downloader#download(java.net.URL)
	 */
	@Override
	public File download(URL url) throws IOException {
		int connectionTimeout = this.conectionTimeout;
		int readTimeout = this.readTimeout;

		HttpParams httpParameters = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(httpParameters, connectionTimeout);
		HttpConnectionParams.setSoTimeout(httpParameters, readTimeout);
		
		HttpClient httpclient = new DefaultHttpClient(httpParameters);

		try {
			HttpGet httpget = new HttpGet(url.toString());

			if(LOG.isInfoEnabled()) {
				LOG.info("executing request " + httpget.getURI());
			}

			HttpResponse response = httpclient.execute(httpget);

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				return Utils.writeToTempFile(entity.getContent());
			}
			return null;
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
	}

}