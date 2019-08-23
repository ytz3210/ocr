package com.el.parse;

import com.el.util.OrderPropertyConstants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class APLParser implements TextParser {

	private List<String> list = new ArrayList<>();
	private Map<String, Object> map = new LinkedHashMap<>();

	public APLParser() {
		list.add("工作编号");
		list.add("船公司");
		list.add("船名航次");
		list.add("提单号");
		list.add("码头");
		list.add("MBL截止日" );
		list.add("截关日" );
		list.add("开航日" );
		list.add("中转港代码" );
		list.add("目的港" );
		list.add("配箱信息" );
	}

	@Override
	public void parse(String text) {

		StringTokenizer st  = new StringTokenizer(text, "\r\n");
		boolean f = false;
		int i = 0;
		while (st.hasMoreTokens()) {
			String s = st.nextToken();
			if(s.startsWith("船公司")){
				map.put(OrderPropertyConstants.SHIP_COMPANY,s.substring(s.indexOf("：")+1).trim());
			}else if(s.startsWith("船名")){
				String[] str = s.split(" ");
				StringBuilder sb = new StringBuilder();
				for (int j = 1; j < str.length-3; j++) {
					sb.append(str[j]).append(" ");
				}
				map.put(OrderPropertyConstants.SHIP_NAME,sb.toString().trim());
				sb = new StringBuilder();
				for (int j = str.length-3; j < str.length; j++) {
					sb.append(str[j]).append(" ");
				}
				map.put(OrderPropertyConstants.LINE,sb.toString().trim());
			}else if(s.startsWith("提单号")){
				map.put(OrderPropertyConstants.BOOKING_NO,s.substring(s.indexOf("：")+1));
			}else if(s.startsWith("配箱信息")){
				s = s.substring(s.indexOf("：")+1);
				String[] str = s.split("\\*");
				//str[0],str[1]箱量，箱型
			}else if(s.startsWith("目的港")){
				map.put(OrderPropertyConstants.TO,s.substring(s.indexOf("：")+1));
			}else if(s.startsWith("截关日")){
				map.put(OrderPropertyConstants.PORT_CUT_OFF,s.substring(s.indexOf("：")+1));
			}else if(s.startsWith("开航日")){
				map.put(OrderPropertyConstants.BARGE_ETD,s.substring(s.indexOf("：")+1));
			}
		}
	}

	@Override
	public Map<String, Object> getContent() {
		return map;
	}
	

}
