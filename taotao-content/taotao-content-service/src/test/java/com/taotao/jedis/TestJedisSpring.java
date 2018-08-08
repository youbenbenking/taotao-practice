package com.taotao.jedis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestJedisSpring {

	@Test
	public void testJedisClientPool() throws Exception{
		
		//初始化Spring容器
		ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		//从容器中获取jedisClient对象
		JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
		//操作redis
		jedisClient.set("jedisClient", "mytest");
		String result=jedisClient.get("jedisClient");
		
		System.out.println(result);
	}
}
