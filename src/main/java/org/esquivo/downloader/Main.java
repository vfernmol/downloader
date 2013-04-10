package org.esquivo.downloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {
	static Options options = new Options();

	private Main() {

	}

	private static void usage() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("downloader [-h] | [-u|-t] <URL> [-o] <timeout (milsecs)>", options);
		System.exit(-1);
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		int connectionTimeout = 0;
		int readTimeout = 0;

		// crear grupo de opciones (u | h | t)
		OptionGroup group = new OptionGroup().addOption(new Option("u", "url", false, "download using URLConnection"))
		        .addOption(new Option("t", "http", false, "download using HTTPClient"))
		        .addOption(new Option("h", "help", false, "show usage"));
		group.setRequired(false);

		options.addOptionGroup(group).addOption("o", "connection-timeout", true, "connection timeout (milsecs)")
		        .addOption("r", "timeout", true, "connection timeout (milsecs)");

		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;

		Downloader down = null;

		try {
			// parse the command line arguments
			cmd = parser.parse(options, args);
		} catch (ParseException exp) {
			// oops, something went wrong
			System.err.println("Parsing failed. Reason: " + exp.getMessage());
			usage();
		}

		boolean isHttpClient = false;
		Iterator<Option> iterator = cmd.iterator();
		while (iterator.hasNext()) {
			Option op = iterator.next();

			switch (op.getId()) {
			case 'o':
				connectionTimeout = Integer.parseInt(op.getValue());
				break;
			case 'r':
				readTimeout = Integer.parseInt(op.getValue());
				break;
			case 'u':
				isHttpClient = false;
				break;
			case 't':
				isHttpClient = true;
				break;
			case 'h':
				usage();
				break;

			}
		}

		if (isHttpClient) {
			down = new BufferedHCDownloader(connectionTimeout, readTimeout);
		} else {
			down = new BufferedURLDownloader(connectionTimeout, readTimeout);
		}

		// Check if user a URL
		if (cmd.getArgList().size() > 1) {
			List<Thread> ths = new ArrayList<Thread>();

			for (String arg : cmd.getArgs()) {
				final String url = arg;
				final Downloader fDown = down;
				
				Thread th = new Thread(new Runnable() {

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
				});
				th.start();
				ths.add(th);
			}
			
			for(Thread th : ths) {
				boolean joined = false;

				while(!joined) {
					try {
		                th.join();
		                joined = true;
	                } catch (InterruptedException e) {
	                	System.err.println("Join interrupted, try another time");
	                }
				}
			}
		} else {
			System.out.println("Bad URL. Please type a valid URL");
			usage();
		}

	}
}