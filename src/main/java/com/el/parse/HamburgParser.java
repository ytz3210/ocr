package com.el.parse;

import com.el.util.OrderPropertyConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class HamburgParser implements TextParser {

	Map<String, Object> map = new HashMap<>();

	@Override
	public void parse(String text) {
		StringTokenizer st = new StringTokenizer(text, "\r\n");
		while (st.hasMoreTokens()) {
			String s = st.nextToken();
			if (s.startsWith("Booking Number")) {
				map.put(OrderPropertyConstants.BOOKING_NO, s.substring(15));
			} else if (s.startsWith("Ves/Voy/Dir")) {
				map.put("Ves/Voy/Dir", s.substring(12));
			} else if (s.startsWith("Destination")) {
				map.put(OrderPropertyConstants.TO, s.substring(11, s.indexOf("Estimated")));
			} else if (s.contains("Estimated Arrival")) {
				s = st.nextToken();
				String s0[] = s.split(" ");
				map.put(OrderPropertyConstants.FROM,s0[0]+s0[1]+s0[2]+s0[3]);
				map.put(OrderPropertyConstants.BARGE_ETD, s0[s0.length-4] + " " + s0[s0.length-3]);
				map.put(OrderPropertyConstants.ETA, s0[s0.length-2] + " " + s0[s0.length-1]);
			} else if (s.contains("Cargo Cut-Off")) {
				map.put(OrderPropertyConstants.PORT_CUT_OFF, s.substring(s.lastIndexOf("Cut-Off")+9));
				map.put("Full Equipment Delivery", st.nextToken());
			} else if(s.contains("Qty")){
				st.nextToken();
				s = st.nextToken();
				String s1[] = s.split(" ");
				//s1[7],s1[8]箱量箱型
			}
			/*
			if (s.startsWith("Booking Number")) {
				map.put("Booking Number", s.substring(15));
			} else if (s.startsWith("Ves/Voy/Dir")) {
				map.put("Ves/Voy/Dir", s.substring(12));
			} else if (s.startsWith("Origin")) {
				map.put("From", s.substring(7, s.indexOf("Cargo")));
			} else if (s.startsWith("Destination")) {
				map.put("Destination", s.substring(11, s.indexOf("Estimated")));
			} else if (s.startsWith("Full Equipment")) {
				map.put("Full Equipment Delivery", st.nextToken());
			} else if (s.contains("Equipment Condition")) {
				s = st.nextToken();
				String s0[] = s.split(" ");
				map.put("Qty Type", s0[7] + " " + s0[8]);
			} else if (s.startsWith("Cargo Cut-Off")) {
				map.put("Cargo Cut-Off", s.substring(s.indexOf(":") + 1));
			} else if (s.contains("Estimated Arrival")) {
				st.nextToken();
				st.nextToken();
				s = st.nextToken();
				String s0[] = s.split(" ");
				map.put("Estimated Departure", s0[0] + " " + s0[1]);
				map.put("Estimated Arrival", s0[1] + " " + s0[2]);
			}
			*/
		}
	}

	@Override
	public Map<String, Object> getContent() {
		return map;
	}

}
