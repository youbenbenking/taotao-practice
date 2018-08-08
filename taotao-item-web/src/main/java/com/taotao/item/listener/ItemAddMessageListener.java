package com.taotao.item.listener;


import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;
/**
 * 接受商品添加后生成静态页面用
 * @author Administrator
 */
public class ItemAddMessageListener implements MessageListener {

	@Autowired
	private ItemService itemService;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Value("${HTML_OUT_PATH}")
	private String HTML_OUT_PATH;
	
	@Override
	public void onMessage(Message message) {
		try {
			//从消息中获取商品id
			TextMessage	textMessage=(TextMessage) message;
			String text = textMessage.getText();
			long itemId = Long.parseLong(text);
			
			//根据商品id查询数据，取商品信息
			//等待商品添加事务提交(如果商品添加事务还没提交，mq消息已经发出，此时拿到itemId去查数据库查不出添加的对象，会报空指针)
			Thread.sleep(3000);
			TbItem tbItem = itemService.getItemById(itemId);
			Item item = new Item(tbItem);
			TbItemDesc itemDesc = itemService.getItemDescById(itemId);
			
			//根据freemarker生成静态页面
			//1、创建模板
			//2、加载模板对象
			//3、准备模板需要的数据
			//4、指定输出的目录及文件名
			//3、生成静态页面
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			Map data=new HashMap<>();
			data.put("item", item);
			data.put("itemDesc", itemDesc);
			
			Writer out=new FileWriter(new File(HTML_OUT_PATH+text+".html"));
			template.process(data, out);
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
