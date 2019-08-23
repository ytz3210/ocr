package com.el.parse;

import com.el.util.HttpUtils;
import com.el.util.OrderPropertyConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class COSUParser implements TextParser {
    Logger log = LoggerFactory.getLogger(COSUParser.class);

    Map<String, Object> map = new HashMap<>();

    @Override
    public void parse(String text){
        map.clear();
        List<Map<String,String>> mulType_Quality = new LinkedList<>();
        StringTokenizer st = new StringTokenizer(text, "\r\n");
            while (st.hasMoreTokens()) {
                String s = st.nextToken();
                if (s.startsWith("订舱号")) {
                    if(s.contains("COAU")){
                        map.put(OrderPropertyConstants.SHIP_COMPANY,"NEW GOLDEN SEA SHIPPING PTE, LTD.");
                    }else if(s.contains("COSU")){
                        map.put(OrderPropertyConstants.SHIP_COMPANY,"COSCO SHIPPING LINES CO., LTD.");
                    }
                    map.put(OrderPropertyConstants.BOOKING_NO, s.substring(s.indexOf("：") + 1));
                } else if (s.startsWith("船名")) {
                    map.put(OrderPropertyConstants.SHIP_NAME, s.substring(s.indexOf(":") + 1, s.lastIndexOf(" ")).trim());
                    map.put(OrderPropertyConstants.LINE, s.substring(s.lastIndexOf(" ")).trim());
                } else if (s.startsWith("接货地")) {
                    map.put(OrderPropertyConstants.FROM, s.substring(s.indexOf(":") + 1, 25).trim());
                    map.put(OrderPropertyConstants.TO, s.substring(s.lastIndexOf(":") + 1,s.indexOf(",")).trim());
                } else if (s.startsWith("Quantity")) {
                    String s1 = st.nextToken();
                    Map<String,String> tqMap = new HashMap<>();
                    tqMap.put("quality",s1.substring(0, s1.indexOf(" ")).trim());
                    tqMap.put("type",s1.substring(s1.indexOf(" "), s1.indexOf(" ") + 5).trim());
                    mulType_Quality.add(tqMap);
                    map.put(OrderPropertyConstants.TYPE_QUALITY, mulType_Quality);
                } else if (s.contains("ETD")) {
                    s = st.nextToken();
                    String s1 = st.nextToken();
                    String[] str = s.split(" ");
                    for (int i = 0; i < str.length - 1; i++) {
                        if (str[i].contains("-")) {
                            log.info(s1);
                            map.put(OrderPropertyConstants.ETD, HttpUtils.formDate(str[i] + " " + s1.substring(0, 5)));
                            break;
                        }
                    }
                    map.put(OrderPropertyConstants.ETA, HttpUtils.formDate(str[str.length - 1] + " " + s1.substring(s1.indexOf(" ") + 1)));
                }
            }
    }

    @Override
    public Map<String, Object> getContent() {
        return map;
    }
}
