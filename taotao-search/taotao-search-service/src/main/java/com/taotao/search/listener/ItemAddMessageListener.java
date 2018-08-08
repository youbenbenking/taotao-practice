package com.taotao.search.listener;


import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.common.pojo.SearchItem;
import com.taotao.search.mapper.SearchItemMapper;
/**
 * 接受商品添加后同步索引库
 * @author Administrator
 */
public class ItemAddMessageListener implements MessageListener {

	@Autowired
	private SearchItemMapper searchItemMapper;
	@Autowired
	private SolrServer solrServer;
	
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
			SearchItem searchItem = searchItemMapper.getItemById(itemId);
			//创建文档对象
			//向文档对象中添加域
			//把文件对象写入索引库
			//创建文档对象
			SolrInputDocument document = new SolrInputDocument();
			//向文档中添加域
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			document.addField("item_desc", searchItem.getItem_desc());
			//把文档写入索引库
			solrServer.add(document);
			//提交
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
