package org.esquivo.downloader;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class URLDownloaderTest.
 */
@RunWith(value = Parameterized.class)
public class URLDownloaderTest {
	private static final Logger LOG = LoggerFactory.getLogger(URLDownloaderTest.class);
	
	private Downloader down;
	private static TestServer server;
		
	public URLDownloaderTest(Downloader down) {
		this.down = down;
	}
	
	@BeforeClass
	public static void startServer() throws Exception {
		server = new TestServer(8080);
		server.getServerJetty().start();
	}
	
	@AfterClass
	public static void stopServer() throws Exception {
		server.getServerJetty().stop();
	}

	@Parameters
	public static Collection<Object[]> data() {
		int connectionTimeout = 1000;
		int readTimeout = 1000;
		int maxThreads = 10;
				
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> emptyParams = new HashMap<String, Object>();
		
		params.put(BufferedHCDownloader.CONNECTION_TIMEOUT, connectionTimeout);
		params.put(BufferedHCDownloader.READ_TIMEOUT, readTimeout);
		params.put(BufferedHCDownloader.MAX_THREADS, maxThreads);
		
		Object[][] data = new Object[][] { { new BufferedURLDownloader() }, {new BufferedHCDownloader()}, { new BufferedURLDownloader(connectionTimeout, readTimeout) }, { new BufferedHCDownloader(params) }, { new BufferedHCDownloader(emptyParams) } };
		
		return Arrays.asList(data);
	 }
	

	public void dispose()  {
		if(this.down instanceof BufferedHCDownloader) {
			((BufferedHCDownloader) this.down).dispose();
		}
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
		File file = down.download(new URL("http://localhost:8080/?size=128"));
		
		// THEN : The file is stored
		assertTrue(file.exists());
	}
		
	/**
	 * Good URL with callback will download file.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void goodURLWithCallBackWillDownloadFile() throws IOException {
		// GIVEN : A download class and a Callback class
		DownloaderCallback callback = new DownloadCallbackTest();

		// WHEN : Download form valid URL
		File file = down.download(new URL("http://localhost:8080/?size=128"), callback);
		
		// THEN : The file is stored
		assertTrue(file.exists());
		assertTrue(((DownloadCallbackTest) callback).getNumCalls() > 0);
	}
	
	/**
	 * Good URL with callback, without content length will download file.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void goodURLWithCallBackWithoutSizeWillDownloadFile() throws IOException {
		// GIVEN : A download class and a Callback class
		DownloaderCallback callback = new DownloadCallbackTest();

		// WHEN : Download form valid URL
		File file = down.download(new URL("http://localhost:8080/?size=262144&reportSize=false"), callback);
		
		// THEN : The file is stored
		assertTrue(file.exists());
		assertTrue(((DownloadCallbackTest) callback).getNumCalls() > 0);
	}
	
	static class DownloadCallbackTest implements DownloaderCallback {
		private int numCalls = 0;

		@Override
        public void progress(File file, long totalSize, long readCount) {
			LOG.debug("File : {}  Size : {}  Read : {}", file.getAbsolutePath(), totalSize, readCount);
	        numCalls++;
        }

		public int getNumCalls() {
        	return numCalls;
        }
	}
}
