package org.esquivo.downloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public abstract interface Downloader {
	public abstract File download(URL paramURL) throws IOException;
}