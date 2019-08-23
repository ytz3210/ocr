package com.el.parse;

import com.el.util.OrderPropertyConstants;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class MSCParser implements TextParser {
	
	private Map<String, Object> map = new LinkedHashMap<>();
	
	@Override
	public void parse(String text) {
		StringTokenizer st  = new StringTokenizer(text, "\r\n");
		while(st.hasMoreTokens()){
			String s = st.nextToken();
			if(s.contains("预计开航日")){
				map.put(OrderPropertyConstants.ETD, s.substring(5, s.indexOf("船公司")));
				map.put(OrderPropertyConstants.SHIP_COMPANY, s.substring(s.indexOf("船公司") + 3));
			} else if(s.contains("起运港")){
				map.put(OrderPropertyConstants.FROM, s.substring(4, s.indexOf("目的港")));
				map.put(OrderPropertyConstants.TO, s.substring(s.indexOf("目的港") + 3, s.indexOf("中转代码")));
				map.put("中转代码", s.substring(s.indexOf("中转代码") +4));
			}  else if(s.contains("CTNR")){
				s = s.substring(7);
				String[] str = s.split("X");
				//str[0],str[1]箱量箱型
			} else if(s.contains("MB/L NO")){
				map.put("MB/L NO", s.substring(7, s.indexOf("船名航次")));
				s = s.substring(s.indexOf("次")+1);
				String[] str = s.split(" ");
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < str.length-2; i++) {
					sb.append(str[i]).append(" ");
				}
				map.put(OrderPropertyConstants.SHIP_NAME, sb.toString().trim());
				map.put(OrderPropertyConstants.LINE, str[str.length-1]);
			} else if(s.contains("截关日期")){
				map.put(OrderPropertyConstants.PORT_CUT_OFF, s.substring(6));
			} else if(s.contains("提单样本截止时间")){
				map.put(OrderPropertyConstants.ORDER_CUT_OFF, s.substring(9));
			} 
		}
	}

	@Override
	public Map<String, Object> getContent() {
		return map;
	}

	
}
