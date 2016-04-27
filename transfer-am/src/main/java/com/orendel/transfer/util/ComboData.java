package com.orendel.transfer.util;

import java.util.Map;
import java.util.TreeMap;

public class ComboData<T> {
	
	private Map<String, T> data = new TreeMap<String, T>();

	public ComboData(Map<String, T> data) {
		this.data = new TreeMap<String, T>(data);
	}
	
	public String[] getEntriesAsStringArray() {
		String[] result = new String[data.size()];
		data.keySet().toArray(result);
		return result;
	}
	
	public T getEntry(String key) {
		return data.get(key);
	}
	
}
