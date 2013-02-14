package org.esquivo.downloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * The Interface Downloader.
 */
public abstract interface Downloader {
	
	/**
	 * Download a file.
	 *
	 * @param paramURL the param url
	 * @return the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public abstract File download(URL paramURL) throws IOException;
}