package com.el.parse;

import com.el.util.HttpUtils;
import com.el.util.OrderPropertyConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class COSCO_WParser implements TextParser {

	Map<String, Object> map = new HashMap<>();
	
	@Override
	public void parse(String text) {
		map.clear();
		StringTokenizer st  = new StringTokenizer(text, "\r\n");
		while(st.hasMoreTokens()){
			String s = st.nextToken();
			if(s.contains("订舱单号")){
				s = st.nextToken();
				String s1[] = s.split(" ");
				map.put(OrderPropertyConstants.BOOKING_NO,s1[0]);
			}else if(s.contains("预配二程船名航次")){
				s = s.substring(1,s.indexOf("("));
				String s1[] = s.split(" ");
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < s1.length-1; i++) {
					sb.append(s1[i]).append(" ");
				}
				map.put(OrderPropertyConstants.SHIP_NAME,sb.toString().trim());
				map.put(OrderPropertyConstants.LINE,s1[s1.length-1]);
			}else if(s.contains("收货地")){
				String s1[] = s.split(" ");
				map.put(OrderPropertyConstants.BARGE_FROM,s1[1]);
			}else if(s.contains("最终卸港")){
				s = st.nextToken();
				String[] s1 = s.split(" ");
				String[] s2 = s1[1].split("×");
				if(s.contains("箱量")){
					//s2[0],s2[1]箱量箱型
				}
				map.put(OrderPropertyConstants.TO,s1[s1.length-1]);
			}else if(s.startsWith("预计截文件时间")){
				String s1[] = s.split(" ");
				map.put(OrderPropertyConstants.ORDER_CUT_OFF, HttpUtils.formDate(s1[1]+" "+s1[2]));
			}else if(s.startsWith("预计截放行条时间")){
				String s1[] = s.split(" ");
				map.put(OrderPropertyConstants.PORT_CUT_OFF, HttpUtils.formDate(s1[1]+" "+s1[2]));
			}else if(s.startsWith("预计开舱时间")){
				String s1[] = s.split(" ");
				map.put(OrderPropertyConstants.BARGE_ETD, HttpUtils.formDate(s1[1]+" "+s1[2]));
			}else if(s.contains("预计到目的港时间")){
				String s1[] = s.split(" ");
				map.put(OrderPropertyConstants.ETA, HttpUtils.formDate(s1[1]+" "+s1[2]));
			}
		}

	}

	@Override
	public Map<String, Object> getContent() {
		return map;
	}

	
}
