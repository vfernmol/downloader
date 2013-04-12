/**
 * 
 */
package org.esquivo.downloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author woo
 * 
 */
public class Url2File implements Runnable {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */

	private final String url;
	private final Downloader fDown;

	public Url2File(String url, Downloader fDown) {
		this.url = url;
		this.fDown = fDown;
	}

	@Override
	public void run() {
		try {
			File file = fDown.download(new URL(url));
			System.out.println("Path URL downloaded: " + file.getAbsolutePath());
		} catch (IOException e) {
			System.out.println("Troubles downloading URL : " + e.getMessage());
			e.printStackTrace();
		}
	}
}
