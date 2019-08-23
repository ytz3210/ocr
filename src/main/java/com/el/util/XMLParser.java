package com.el.util;

import org.apache.catalina.LifecycleState;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.List;

public class XMLParser{

    public static void parse(String path) {
        try {
            //创建XML解析器
            SAXReader reader = new SAXReader();
            //加载文件，读取到document中
            Document document = reader.read(path+".xml");
            //获取根元素
            Element rootEle = document.getRootElement();
            //通过根节点获取所有直接子节点
            List<Element> child = rootEle.elements();
            // 遍历根元素下所有直接子元素
            for (Element e : child) {
                // 获取子元素名称
                System.out.print(e.getName() + " ");

                // 获取子元素的属性
//                Attribute attr = e.attribute(0);
//                System.out.println(attr.getName() + "=" + attr.getValue());

                // 获取下一级子元素
                List<Element> echilds = e.elements();

                for (Element e2 : echilds) {
                    // 获取子元素名称和值
                    System.out.println("\t" + e2.getName() + "=" + e2.getTextTrim());
                }
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

}
