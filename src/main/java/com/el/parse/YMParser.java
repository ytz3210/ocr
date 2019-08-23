package com.el.parse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class YMParser implements TextParser {

	Map<String, Object> map = new HashMap<>();

	@Override
	public void parse(String text) {
		HashMap<String, Object> map_text = JSON.parseObject(text, HashMap.class);
		List<Map<String, Object>> list = (List<Map<String, Object>>) map_text.get("words_result");
		int i = 0;
		for (Map<String, Object> m : list) {
			String s = (String) m.get("words");
			if (s.startsWith("关单号")) {
				map.put("关单号", s.substring(3));
			} else if (s.startsWith("船名航次")) {
				map.put("船名航次", s.substring(5));
			} else if (s.startsWith("靠区")) {
				map.put("靠区", s.substring(2));
			} else if (s.startsWith("开航时间")) {
				map.put("开航时间", s.substring(4));
			} else if (s.startsWith("目的地")) {
				map.put("目的地", s.substring(3));
			} else if (s.startsWith("卸货港代码")) {
				map.put("卸货港代码", s.substring(6));
			} else if (s.contains("Group")) {
				i = 1;
			}else if (i ==1){
				map.put("箱型箱量", s);
				break;
			}
		}
	}

	@Override
	public Map<String, Object> getContent() {
		return map;
	}

}
