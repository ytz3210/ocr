package com.el.parse;

import com.el.util.HttpUtils;
import com.el.util.OrderPropertyConstants;

import java.util.*;

public class COSCO_SParser implements TextParser {

    Map<String, Object> map = new HashMap<>();

    @Override
    public void parse(String text) {
        map.clear();
        List<Map<String,String>> mulType_Quality = new LinkedList<>();
        StringTokenizer st = new StringTokenizer(text, "\r\n");
        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            String[] str = s.split(" ");
            if (s.startsWith("COSCO")) {
                map.put(OrderPropertyConstants.SHIP_COMPANY, s);
            } else if (s.startsWith("订舱单号")) {
                str = s.split(" ");
                map.put(OrderPropertyConstants.BOOKING_NO, str[1]);
            } else if (s.contains("头程船名航次")) {
                Map<String,Object> sm = splitBargeName(s);
                String[] arr = (String[]) sm.get("arr");
                map.put(OrderPropertyConstants.BARGE_NAME, sm.get("shipName").toString().trim());
                map.put(OrderPropertyConstants.BARGE_LINE, arr[arr.length - 1].substring(0, arr[arr.length - 1].indexOf("(")).trim());
            } else if (s.contains("二程船名航次")) {
                Map<String,Object> sm = splitBargeName(s);
                String[] arr = (String[]) sm.get("arr");
                map.put(OrderPropertyConstants.SHIP_NAME, sm.get("shipName").toString().trim());
                map.put(OrderPropertyConstants.LINE, arr[arr.length - 1].substring(0, arr[arr.length - 1].indexOf("(")).trim());
            } else if (s.startsWith("收货地")) {
                str = s.split(" ");
                map.put(OrderPropertyConstants.BARGE_FROM, str[str.length - 1]);
            } else if (s.startsWith("卸港")) {
                str = s.split(" ");
                map.put(OrderPropertyConstants.FROM, str[1]);
                if (str.length == 3) {
                    map.put(OrderPropertyConstants.TO, str[2]);
                } else {
                    for (int i = 3; i < str.length; i++) {
                        str[i] = str[i - 1] + " " + str[i];
                    }
                    map.put(OrderPropertyConstants.TO, str[str.length - 1]);
                }
            } else if (s.startsWith("箱量")) {
                Map<String,String> tqMap = new HashMap<>();
                String[] strs = str[1].split("×");
                tqMap.put("quality",strs[0]);
                tqMap.put("type",strs[1]);
                mulType_Quality.add(tqMap);
                map.put(OrderPropertyConstants.TYPE_QUALITY, mulType_Quality);
            } else if (s.contains("截放行条时间")) {
                map.put(OrderPropertyConstants.PORT_CUT_OFF, HttpUtils.formDate(s.substring(s.indexOf("2"))));
            } else if (s.contains("截文件时间")) {
                map.put(OrderPropertyConstants.ORDER_CUT_OFF, HttpUtils.formDate(s.substring(s.indexOf("2"))));
            } else if (s.contains("开航时间")) {
                map.put(OrderPropertyConstants.BARGE_ETD, HttpUtils.formDate(s.substring(s.indexOf("2"))));
            } else if (s.contains("到目的港时间")) {
                map.put(OrderPropertyConstants.ETA, HttpUtils.formDate(s.substring(s.indexOf("2"))));
            }
        }
    }

    @Override
    public Map<String, Object> getContent() {
        return map;
    }

    public Map<String,Object> splitBargeName(String s){
        StringBuilder sb = new StringBuilder(s);
        String[] str = s.split(" ");
        if (str[1].toCharArray().length == 1) {
            sb = new StringBuilder();
            str[1] = str[1] + str[2];
            List<String> strs = Arrays.asList(str);
            List<String> lists = new ArrayList<>();
            for (String str1 : strs) {
                lists.add(str1);
            }
            lists.remove(2);
            for (String list : lists) {
                sb.append(list).append(" ");
            }
        }
        String sb1 = sb.substring(sb.indexOf(":"), sb.indexOf(")"));
        String[] arr = sb1.split(" ");
        StringBuilder shipName = new StringBuilder();
        for (int i = 1; i < arr.length - 1; i++) {
            shipName.append(arr[i]).append(" ");
        }
        Map<String,Object> map = new HashMap<>();
        map.put("shipName",shipName);
        map.put("arr",arr);
        return map;
    }
}
