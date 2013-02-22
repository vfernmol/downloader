package org.esquivo.downloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Main {
	private Main() {
		
	}

	private static void usage(String name, int argsLength) {
		System.out.println("Numero de argunemtos: " + argsLength);
		System.out.println("Usage: " + name + " <options> <URL>");
		System.out.println("Options: ");
		System.out.println("-bu | descargar usando URLConnection con buffer");
		System.out.println("-bh | descargar usando HTTPClient con buffer");
	}

	public static void main(String[] args) {
		if ((args.length != 1) && (args.length != 2)) {
			usage("downloader", args.length);
			System.exit(1);
		}
		Downloader down = null;
		try {
			if ("-bu".equals(args[0])) {
				down = new BufferedURLDownloader();
			} else if (args[0].equals("-bh")) {
				down = new BufferedHCDownloader();
			}

			File file = down.download(new URL(args[1]));
			System.out.println("Path URL downloaded: " + file.getAbsolutePath());
		} catch (IOException e) {
			System.out.println("Troubles downloading URL : " + e.getMessage());
		}
	}
}