package com.el.parse;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class ONEParser implements TextParser {

	private Map<String, Object> map = new LinkedHashMap<>();
	
	@Override
	public void parse(String text) {
		StringTokenizer st  = new StringTokenizer(text, "\r\n");
		while(st.hasMoreTokens()){
			String s = st.nextToken();
		}
	}

	@Override
	public Map<String, Object> getContent() {
		return map;
	}

	
}
