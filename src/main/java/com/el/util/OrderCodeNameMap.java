package com.el.util;

import java.util.HashMap;
import java.util.Map;

public abstract class OrderCodeNameMap {
    public static final Map<String, String> map = new HashMap<>();

    static {
        map.put("CHENNAI", "INMAA");
        map.put("ANTWERP", "BEANR");
        map.put("ZHANGJIAGANG", "CNZJG");
        map.put("LAEM CHABANG", "THLCH");
        map.put("NINGBO", "CNNGB");
        map.put("BILBAO", "ESBIO");
        map.put("KEELUNG", "TWKEE");
        map.put("GRANGEMOUTH", "GBGRG");
        map.put("KOTKA", "FIKTK");
        map.put("LONG BEACH,CA", "USLGB");
        map.put("HAIPHONG", "SBHHG");
        map.put("DETROIT,MI", "USDET");
        map.put("HO CHI MINH (CAT LAI)", "VNCLI");
        map.put("JIANGYING", "CNJIA");
        map.put("JAWAHARLAL NEHRU", "JAWAH");
        map.put("HAKATA", "FPHKA");
        map.put("SHANGHAI", "CNSHA");
        map.put("TAICHUNG", "TWTXG,TWTCH");
        map.put("NORFOLK,VA", "USORF");
        map.put("NHAVA SHEVA", "INNSA");
        map.put("IZMIT", "TRIZT");
        map.put("CHARLESTON,SC", "USCHS");
        map.put("ST. PETERSBURG", "RULED,RUPET");
        map.put("PORT ELIZABETH", "ZAPEL,USPEB");
    }
}
