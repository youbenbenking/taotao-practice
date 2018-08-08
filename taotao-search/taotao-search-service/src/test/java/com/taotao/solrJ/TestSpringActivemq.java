package com.taotao.solrJ;


import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
public class TestSpringActivemq {

	/**接受消息
	 */
	@Test
	public void testJmsTemlate() throws Exception{
		ApplicationContext applicationContext=	new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		//等待
		System.in.read();
	}
}
