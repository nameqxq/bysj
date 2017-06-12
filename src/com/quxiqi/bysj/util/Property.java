package com.quxiqi.bysj.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

public class Property {
	private Properties props = new Properties();

	private InputStream in = null;

	public Property(String file) {
		try {
			in = getClass().getClassLoader().getResourceAsStream(file);
			props.load(in);
		} catch (Exception e) {
		}
	}

	public String getValue(String name) {
		return props.getProperty(name);
		//return new String(props.getProperty(name).getBytes("ISO8859-1"), "UTF-8");
	}
	public Properties getProperties(){
		return props;
	}
	public String[] getPropertyNames() {
		@SuppressWarnings("unchecked")
		ArrayList<String> a = (ArrayList<String>) Collections.list(props.propertyNames());
		String[] keys = (String[]) a.toArray(new String[a.size()]);
		return keys;
	}
}
