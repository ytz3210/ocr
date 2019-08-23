package com.el.parse;

import com.el.util.OrderPropertyConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class HapagParser implements TextParser {

	Map<String, Object> map = new HashMap<>();
	
	@Override
	public void parse(String text) {
		StringTokenizer st  = new StringTokenizer(text, "\r\n");
		map.put(OrderPropertyConstants.SHIP_COMPANY, "HAPAG-LLOYD (CHINA) SHIPPING LTD.");
		while(st.hasMoreTokens()){
			String s = st.nextToken();
			if (s.contains("提单号")) {
				map.put(OrderPropertyConstants.BOOKING_NO, s.substring(5));
			} else if (s.startsWith("Vessel")) {
				map.put("Vessel Voy. No", st.nextToken()+ st.nextToken());
			} else if (s.startsWith("起点")) {
				map.put(OrderPropertyConstants.FROM, st.nextToken()+ st.nextToken()+ st.nextToken());
				map.put(OrderPropertyConstants.TO, st.nextToken()+ st.nextToken()+ st.nextToken());
			} else if (s.contains("摘要")) {
				map.put("摘要", s.substring(s.indexOf(":")+1));
			} else if (s.startsWith("VGM cut-off")) {
				st.nextToken();
				map.put(OrderPropertyConstants.PORT_CUT_OFF, st.nextToken() + st.nextToken());
			} else if (s.startsWith("FCL delivery")) {
				st.nextToken();
				map.put(OrderPropertyConstants.ORDER_CUT_OFF, st.nextToken() + st.nextToken());
			}
		}
		int i = text.indexOf("目的港码头提箱地址");
		String date = text.substring(i-42, i);
		String date0[] = date.split("\r\n");
		map.put(OrderPropertyConstants.BARGE_ETD, date0[0] + date0[1]);
		map.put(OrderPropertyConstants.ETA, date0[2] + date0[3]);
	}

	@Override
	public Map<String, Object> getContent() {
		return map;
	}

}
