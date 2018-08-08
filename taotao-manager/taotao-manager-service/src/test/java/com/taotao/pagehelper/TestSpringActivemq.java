package com.taotao.pagehelper;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class TestSpringActivemq {

	/**使用jmsTemplate发送消息
	 */
	@Test
	public void testJmsTemlate() throws Exception{
		//1、初始化Spring容器
		//2、从容器中获取JMSTemplate对象
		//3、从容器中获取Destination对象
		//4、发送消息
		
		ApplicationContext applicationContext=	new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
		Destination destination=(Destination)applicationContext.getBean("test-queue");
		jmsTemplate.send(destination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage("spring activemq test");
				return textMessage;
			}
		});
		
	}
}
