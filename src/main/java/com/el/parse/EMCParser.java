package com.el.parse;

import com.el.util.OrderPropertyConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class EMCParser implements TextParser {

	Map<String, Object> map = new HashMap<>();

	@Override
	public void parse(String text) {
		StringTokenizer st = new StringTokenizer(text, "\r\n");
		while (st.hasMoreTokens()) {
			String s = st.nextToken();
			if (s.contains("BOOKING NUMBER")) {
				map.put(OrderPropertyConstants.BOOKING_NO, s.substring(s.indexOf(":") + 1, s.indexOf("APPLICATION")));
			} else if (s.contains("VESSEL")) {
				map.put("VESSEL/VOYAGE", s.substring(s.indexOf(":") + 1));
			} else if (s.contains("CARRIER")) {
				map.put("CARRIER", s.substring(s.indexOf(":") + 1));
			} else if (s.contains("PORT OF LOADING")) {
				map.put(OrderPropertyConstants.FROM, s.substring(s.indexOf(":") + 1));
			} else if (s.contains("PLACE OF DELIVERY")) {
				map.put(OrderPropertyConstants.BARGE_FROM, s.substring(s.indexOf(":") + 1));
			} else if (s.contains("截关时间")) {
				map.put(OrderPropertyConstants.PORT_CUT_OFF, s.substring(s.indexOf(":") + 1));
			} else if (s.contains("VGM CUT OFF")) {
				map.put("VGM CUT OFF", s.substring(s.indexOf(":") + 1));
			} else if (s.contains("ETA DATE")) {
				map.put(OrderPropertyConstants.ETA, s.substring(s.indexOf(":") + 1));
			} else if (s.contains("ETD DATE")) {
				map.put(OrderPropertyConstants.BARGE_ETD, s.substring(s.indexOf(":") + 1));
			} else if (s.contains("重箱返还处")) {
				map.put("重箱返还处", s.substring(s.indexOf(":") + 1));
			} else if (s.contains("QTY_TYPE")) {
				st.nextToken();
				s = st.nextToken();
				map.put("QTY_TYPE(箱数箱型)", s.split("   ")[0]);
			}
		}
	}

	@Override
	public Map<String, Object> getContent() {
		return map;
	}

}
