package com.el.parse;

import com.el.exception.MyException;

import java.util.Map;

public interface TextParser {
	
	void parse(String text) throws MyException;
	
	Map<String, Object> getContent();
}
