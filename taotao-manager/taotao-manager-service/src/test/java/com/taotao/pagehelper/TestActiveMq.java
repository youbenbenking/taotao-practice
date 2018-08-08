package com.taotao.pagehelper;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class TestActiveMq {

	/**queue测试
	 */
	@Test
	public void testQueueProducer() throws Exception{
		//1、创建一个连接工厂对象ConnectionFactory对象，需要指定mq服务的ip和端口
		//2、创建一个连接对象
		//3、开启连接，调用Connection的start方法
		//4、使用Connection对象创建一个Session对象
		//5、使用Session创建一个Destination对象，两种形式queue、topic，此测试创建queue
		//6、使用Session创建一个Producer对象
		//7、创建一个TextMassage对象
		//8、发送消息
		
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.204.128:61616");
		Connection connection = connectionFactory.createConnection();
		connection.start();
		//参数1代表是否开启事务，一般不使用事务。保证数据的最终一致，可以使用消息队列实现；
		//如果参数1位true即开启事务了，第二个参数自动忽略；如果不开启事务，参数2为消息的应答模式。一般为自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue("test-queue");
		MessageProducer producer = session.createProducer(queue);
		TextMessage textMessage = session.createTextMessage("hello activemq");
		producer.send(textMessage);
		
		producer.close();
		session.close();
		connection.close();
	}
	
	@Test
	public void testQueueConsumer() throws Exception{
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.204.128:61616");
		Connection connection = connectionFactory.createConnection();
		connection.start();
		//参数1代表是否开启事务，一般不使用事务。保证数据的最终一致，可以使用消息队列实现；
		//如果参数1位true即开启事务了，第二个参数自动忽略；如果不开启事务，参数2为消息的应答模式。一般为自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue("test-queue");
		MessageConsumer consumer = session.createConsumer(queue);
		consumer.setMessageListener(new MessageListener(){

			@Override
			public void onMessage(Message message) {
				if(message instanceof TextMessage){
					TextMessage textMessage=(TextMessage)message;
					try {
						String text = textMessage.getText();
						System.out.println(text);
						
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			}
		});
		//等待接受消息(键盘输入后停止)
		System.in.read();
		
		consumer.close();
		session.close();
		connection.close();
	}
	
	/**topic测试
	 */
	@Test
	public void testTopicProducer() throws Exception{
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.204.128:61616");
		Connection connection = connectionFactory.createConnection();
		connection.start();
		//参数1代表是否开启事务，一般不使用事务。保证数据的最终一致，可以使用消息队列实现；
		//如果参数1位true即开启事务了，第二个参数自动忽略；如果不开启事务，参数2为消息的应答模式。一般为自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = session.createTopic("test-topic");
		MessageProducer producer = session.createProducer(topic);
		TextMessage textMessage = session.createTextMessage("hello activemq topic");
		producer.send(textMessage);
		
		producer.close();
		session.close();
		connection.close();
	}
	
	@Test
	public void testTopicConsumer() throws Exception{
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.204.128:61616");
		Connection connection = connectionFactory.createConnection();
		connection.start();
		//参数1代表是否开启事务，一般不使用事务。保证数据的最终一致，可以使用消息队列实现；
		//如果参数1位true即开启事务了，第二个参数自动忽略；如果不开启事务，参数2为消息的应答模式。一般为自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = session.createTopic("test-topic");
		MessageConsumer consumer = session.createConsumer(topic);
		consumer.setMessageListener(new MessageListener(){

			@Override
			public void onMessage(Message message) {
				if(message instanceof TextMessage){
					TextMessage textMessage=(TextMessage)message;
					try {
						String text = textMessage.getText();
						System.out.println(text);
						
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			}
		});
		//等待接受消息(键盘输入后停止)
		System.out.println("topic消费者3");
		System.in.read();
		
		consumer.close();
		session.close();
		connection.close();
	}
	
	
}
