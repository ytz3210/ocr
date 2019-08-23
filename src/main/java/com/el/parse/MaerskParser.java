package com.el.parse;

import com.el.util.HttpUtils;
import com.el.util.OrderPropertyConstants;

import java.util.*;

public class MaerskParser implements TextParser {

    Map<String, Object> map = new HashMap<>();

    @Override
    public void parse(String text){
        map.clear();
        List<Map<String,String>> mulType_Quality = new LinkedList<>();
        StringTokenizer st = new StringTokenizer(text, "\r\n");
            while (st.hasMoreTokens()) {
                String s = st.nextToken();
                if (s.contains("订舱分公司")) {
                    map.put(OrderPropertyConstants.SHIP_COMPANY, s.substring(s.lastIndexOf(":") + 1, s.lastIndexOf("(")).trim());
                } else if (s.startsWith("Booking")) {
                    map.put(OrderPropertyConstants.BOOKING_NO, s.substring(s.indexOf(":") + 1, s.indexOf(":") + 11).trim());
                } else if (s.contains("收货地")) {
                    if (s.contains("-")) {
                        map.put(OrderPropertyConstants.FROM, s.substring(s.lastIndexOf(":") + 1, s.lastIndexOf("-")).trim());
                    } else {
                        map.put(OrderPropertyConstants.FROM, s.substring(s.lastIndexOf(":") + 1, s.indexOf(",")).trim());
                    }
                } else if (s.contains("交货地")) {
                    map.put(OrderPropertyConstants.TO, s.substring(s.lastIndexOf(":") + 1, s.indexOf(",")).trim());
                } else if (s.contains("数量")) {
                    s = st.nextToken();
                    String[] str = s.split(" ");
                    String[] Type_Quality = new String[2];
                    Map<String,String> tqMap = new HashMap<>();
                    tqMap.put("quality",str[0]);
                    String type = str[3] + str[4];
                    if ("86".equals(type)) {
                        s = str[1] + "GP";
                    } else if ("96".equals(type)) {
                        s = str[1] + "HC";
                    }
                    tqMap.put("type",s);
                    mulType_Quality.add(tqMap);
                    map.put(OrderPropertyConstants.TYPE_QUALITY, mulType_Quality);
                } else if (s.contains("船名")) {
                    String bargeEta = st.nextToken();
                    String[] str = splitNameAndLine(bargeEta);
                    st.nextToken();
                    String eta = st.nextToken();
                    String s1 = st.nextToken();
                    if (s1.toLowerCase().contains("terminal")) {
                        map.put(OrderPropertyConstants.BARGE_NAME, str[0]);
                        map.put(OrderPropertyConstants.BARGE_LINE, str[1]);
                        map.put(OrderPropertyConstants.BARGE_ETD, HttpUtils.formDate(str[2]));
                        str = splitNameAndLine(eta);
                        map.put(OrderPropertyConstants.SHIP_NAME, str[0]);
                        map.put(OrderPropertyConstants.LINE, str[1]);
                        map.put(OrderPropertyConstants.ETD, HttpUtils.formDate(str[2]));
                        String s2 = st.nextToken();
                        if (st.nextToken().contains("II")) {
                            str = splitNameAndLine(s2);
                        }
                        map.put(OrderPropertyConstants.ETA, HttpUtils.formDate(str[3]));
                    } else {
                        map.put(OrderPropertyConstants.SHIP_NAME, str[0]);
                        map.put(OrderPropertyConstants.LINE, str[1]);
                        map.put(OrderPropertyConstants.ETD, HttpUtils.formDate(str[2]));
                        map.put(OrderPropertyConstants.ETA, HttpUtils.formDate(str[3]));
                    }
                }
            }
    }

    @Override
    public Map<String, Object> getContent() {
        return map;
    }

    /**
     * 分割出船名、航次、预计出发日期、预计到达日期
     *
     * @param s
     * @return arr[]
     * @author Yangtz
     * @date 2019/7/30
     */
    public String[] splitNameAndLine(String s) {
        String[] arr = new String[4];
        String[] str = s.split(" ");
        for (int i = 0; i < str.length - 1; i++) {
            if ("FEF".equals(str[i]) || "MVS".equals(str[i]) || "FEO".equals(str[i])) {
                StringBuilder sb = new StringBuilder();
                for (int j = i + 1; j < str.length - 3; j++) {
                    sb.append(str[j]).append(" ");
                }
                arr[0] = sb.toString().trim();
                break;
            }
        }
        arr[1] = str[str.length - 3];
        arr[2] = str[str.length - 2];
        arr[3] = str[str.length - 1];
        return arr;
    }
}
