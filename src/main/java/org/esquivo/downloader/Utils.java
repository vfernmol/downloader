/**
 * 
 */
package org.esquivo.downloader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author woo
 * 
 */
public class Utils {
	private static final Logger LOG = LoggerFactory.getLogger(BufferedHCDownloader.class);
	
	private final static int BUFFER_SIZE = 4096;

	private Utils() {

	}

	public static File writeToTempFile(InputStream in) throws IOException {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {
			bis = new BufferedInputStream(in, BUFFER_SIZE);

			File tempFile = File.createTempFile("urldownloader-", null);
			if (LOG.isDebugEnabled()) {
				LOG.info("Writing to temp file : " + tempFile.getAbsolutePath());
			}
			bos = new BufferedOutputStream(new FileOutputStream(tempFile), BUFFER_SIZE);
            byte[] buffer = new byte[BUFFER_SIZE];
            int n;
			while ((n = bis.read(buffer, 0, BUFFER_SIZE)) != -1) {
				bos.write(buffer, 0, n);
			}

			return tempFile;
		} finally {
			IOUtils.closeQuietly(bis);
			IOUtils.closeQuietly(bos);
		}
	}
	
	public static File writeToTempFile2(InputStream in) throws IOException {
		OutputStream out = null;
		
		try {
			File tempFile = File.createTempFile("urldownloader-", null);
			out = new FileOutputStream(tempFile);
            
			IOUtils.copy(in, out);

			return tempFile;
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
	}

}
