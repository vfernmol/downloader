package org.esquivo.downloader;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * The Class URLDownloaderTest.
 */
@RunWith(value = Parameterized.class)
public class URLDownloaderTest {
	
	private Downloader down;
	
	public URLDownloaderTest(Downloader down) {
		this.down = down;
	}
	
	@Parameters
	public static Collection<Object[]> data() {
	   Object[][] data = new Object[][] { { new BufferedURLDownloader() }, {new BufferedHCDownloader()} };
	   
	   return Arrays.asList(data);
	 }

	/**
	 * Malformed URL will fail.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test(expected = IOException.class)
	public void malformedURLWillFail() throws IOException {
		// GIVEN : A download class
		
		// WHEN : Download form invalid URL
		down.download(new URL("http://mierd"));
		
		// THEN : Fails
		fail("Exception not throw");
	}
	
	/**
	 * Good URL will download file.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void goodURLWillDownloadFile() throws IOException {
		// GIVEN : A download class
		
		// WHEN : Download form valid URL
		File file = down.download(new URL("http://www.google.es"));
		
		// THEN : The file is stored
		assertTrue(file.exists());
	}
}
