package org.esquivo.downloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class BufferedHCDownloader.
 * 
 * @author woo
 */
@ThreadSafe
public class BufferedHCDownloader implements Downloader {
	private static final Logger LOG = LoggerFactory.getLogger(BufferedHCDownloader.class);
	
	public static final String CONNECTION_TIMEOUT = "connectionTimeout";
	public static final String READ_TIMEOUT = "readTimeout";
	public static final String MAX_THREADS = "maxThreads";
	
	protected HttpClient httpclient;

	private int connectionTimeout = 0;
	private int readTimeout = 0;
	private int maxThreads = -1;
	
	public BufferedHCDownloader() {
		initHttpClient();
	}

	public BufferedHCDownloader(Map<String,Object> params) {
		if (params.containsKey(CONNECTION_TIMEOUT)) {
			connectionTimeout = ((Integer) params.get(CONNECTION_TIMEOUT)).intValue();
		}
		
		if (params.containsKey(READ_TIMEOUT)) {
			readTimeout = ((Integer) params.get(READ_TIMEOUT)).intValue();
		}
		
		if (params.containsKey(MAX_THREADS)) {
			maxThreads = ((Integer) params.get(MAX_THREADS)).intValue();
		}
		
		initHttpClient();
    }
	
	private void initHttpClient() {
		PoolingClientConnectionManager cm = new PoolingClientConnectionManager();
		if(maxThreads != -1) {
			cm.setMaxTotal(maxThreads);
			cm.setDefaultMaxPerRoute(maxThreads);
		}
		
		this.httpclient = new DefaultHttpClient(cm);
	}

	/* (non-Javadoc)
	 * @see org.esquivo.downloader.Downloader#download(java.net.URL)
	 */
	@Override
	public File download(URL url) throws IOException {
		HttpGet httpget = new HttpGet(url.toString());
		setTimeouts(httpget.getParams());

		if (LOG.isInfoEnabled()) {
			LOG.info("executing request " + httpget.getURI());
		}

		HttpResponse response = httpclient.execute(httpget);

		HttpEntity entity = response.getEntity();
		
		if (entity != null) {
			return Utils.writeToTempFile(entity.getContent());
		}
		
		return null;
	}
	
	public void dispose() {
		this.httpclient.getConnectionManager().shutdown();
	}
	
	private void setTimeouts(HttpParams params) {
		HttpConnectionParams.setConnectionTimeout(params, connectionTimeout);
		HttpConnectionParams.setSoTimeout(params, readTimeout);
	}

}