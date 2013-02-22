package org.esquivo.downloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;


/**
 * The Class BufferedURLDownloader.
 */
public class BufferedURLDownloader implements Downloader {
	
	/* (non-Javadoc)
	 * @see org.esquivo.downloader.Downloader#download(java.net.URL)
	 */
	@Override
	public File download(URL url) throws IOException {
		URLConnection conn = url.openConnection();
		return Utils.writeToTempFile(conn.getInputStream());
	}
}