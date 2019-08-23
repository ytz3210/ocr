package com.el.parse;

import com.el.util.HttpUtils;
import com.el.util.OrderPropertyConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class JSZCParser implements TextParser {
    Logger log = LoggerFactory.getLogger(JSZCParser.class);
    Map<String, Object> map = new HashMap<>();

    @Override
    public void parse(String text) {
        map.clear();
        List<Map<String,String>> mulType_Quality = new LinkedList<>();
        StringTokenizer st = new StringTokenizer(text, "\r\n");
        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            if (s.contains("提单号")) {
                String[] arr = s.substring(0, s.indexOf("运费") - 1).split(" ");
                map.put(OrderPropertyConstants.BOOKING_NO, arr[1]);
                String[] arr1 = arr[3].split("×");
                if (arr.length > 4) {
                    Map<String,String> tqMap = new HashMap<>();
                    String[] arr2 = arr[4].split("×");
                    tqMap.put("quality",arr2[0]);
                    tqMap.put("type",arr2[1]);
                    mulType_Quality.add(tqMap);
                    map.put(OrderPropertyConstants.TYPE_QUALITY, mulType_Quality);
                }
                Map<String,String> tqMap = new HashMap<>();
                tqMap.put("quality",arr1[0]);
                tqMap.put("type",arr1[1]);
                mulType_Quality.add(tqMap);
                map.put(OrderPropertyConstants.TYPE_QUALITY, mulType_Quality);
                s = st.nextToken();
                String s1 = st.nextToken();
                if (s1.contains("驳船船名")) {
                    map.put(OrderPropertyConstants.BARGE_NAME, s);
                    map.put(OrderPropertyConstants.BARGE_LINE, st.nextToken());
                    map.put(OrderPropertyConstants.BARGE_ETD, HttpUtils.formDate(s1.substring(12)));
                    s = st.nextToken();
                    s1 = st.nextToken();
                    if (s1.contains("大船船名")) {
                        map.put(OrderPropertyConstants.SHIP_NAME, s);
                        map.put(OrderPropertyConstants.LINE, st.nextToken());
                        map.put(OrderPropertyConstants.ETD, HttpUtils.formDate(s1.substring(12, 22)));
                        s1 = st.nextToken();
                    }
                    String[] str = s1.split(" ");
                    String s2 = st.nextToken();
                    String[] str1 = s2.split(" ");
                    for (int i = 0; i < str.length; i++) {
                        if (str[i].contains("起运港")) {
                            if (!str[i + 1].contains("卸货港")) {
                                map.put(OrderPropertyConstants.BARGE_FROM, str[i + 1]);
                            }
                        }
                        if (str[i].contains("卸货港")) {
                            if (!str[i + 1].contains("目的港")) {
                                String to = s1.substring(s1.indexOf("卸货") + 4, s1.indexOf("目的")).trim();
                                if (!s2.contains("件数") && str1.length > 1) {
                                    map.put(OrderPropertyConstants.TO, to + " " + str1[0]);
                                } else {
                                    map.put(OrderPropertyConstants.TO, to);
                                }
                            }
                        }
                        if (str[i].contains("目的港")) {
                            if ((i + 1) < str.length) {
                                String delivery = s1.substring(s1.indexOf("目的") + 4).trim();
                                if (!s2.contains("件数") && str1.length > 1) {
                                    map.put(OrderPropertyConstants.DELIVERY_PLACE, delivery + " " + str1[1]);
                                } else if (str1.length == 1) {
                                    map.put(OrderPropertyConstants.DELIVERY_PLACE, delivery + " " + s2);
                                } else {
                                    map.put(OrderPropertyConstants.DELIVERY_PLACE, delivery);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public Map<String, Object> getContent() {
        return map;
    }
}
