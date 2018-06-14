package com.zsx.fwmp.web.others.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamGobbler extends Thread {

	static final Logger logger=LoggerFactory.getLogger(StreamGobbler.class);
	
	InputStream is;
	String type;
	OutputStream os;
	String name;

	public StreamGobbler(InputStream is, String type,String name) {
		this(is, type, null,name);
	}

	public StreamGobbler(InputStream is, String type, OutputStream redirect,String name) {
		this.is = is;
		this.type = type;
		this.os = redirect;
		this.name=name;
	}

	@Override
	public void run() {
		try {
			PrintWriter pw = null;
			if (os != null)
				pw = new PrintWriter(os);

			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				if (pw != null)
					pw.println(line);
					logger.debug(type + ">" + "["+name+"]"+line);
			}
			if (pw != null)
				pw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
