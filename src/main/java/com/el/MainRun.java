package com.el;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


//@ServletComponentScan // 扫描servlet 注解
//@SpringBootApplication // 取消包括了下面两个
@EnableAutoConfiguration
@ComponentScan({"com.el.controller", "com.el.service", "com.el.config"})
public class MainRun {
	
	public static void main(String[] args) {
		SpringApplication.run(MainRun.class, args);
	}
	
	
	/**
	 * add web模板目录到classpath
	 * set CLASSPATH="%BASEDIR%"\conf;"%BASEDIR%"\web;"%REPO%"\*
	 */

}
