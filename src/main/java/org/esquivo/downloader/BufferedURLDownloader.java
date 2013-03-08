package org.esquivo.downloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * The Class BufferedURLDownloader.
 */
public class BufferedURLDownloader implements Downloader {
	private int conectionTimeout = 0;
	private int readTimeout = 0;
	
	public BufferedURLDownloader() {
		
	}

	public BufferedURLDownloader(int conectionTimeout, int readTimeout) {
	    this.conectionTimeout = conectionTimeout;
	    this.readTimeout = readTimeout;
    }
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.esquivo.downloader.Downloader#download(java.net.URL, int)
	 */
	@Override
	public File download(URL url) throws IOException {
		int connectionTimeout = this.conectionTimeout;
		int readTimeout = this.readTimeout;
		
		URLConnection conn = url.openConnection();
		conn.setConnectTimeout(connectionTimeout);	
		conn.setReadTimeout(readTimeout);
		
		return Utils.writeToTempFile(conn.getInputStream());
	}
}