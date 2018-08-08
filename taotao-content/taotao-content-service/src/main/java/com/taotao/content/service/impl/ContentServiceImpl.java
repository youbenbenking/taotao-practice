package com.taotao.content.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.utils.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${INDEX_CONTENT}")
	private String INDEX_CONTENT;
	
	
	@Override
	public List<TbContent> getContentByCid(long cid) {

		//添加缓存不能影响正常业务逻辑
		//先查询缓存************************
		try {
			String json=jedisClient.hget(INDEX_CONTENT, cid+"");
			if(StringUtils.isNotBlank(json)){
				List<TbContent> list=JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//缓存中没有命中，则去查询数据库
		TbContentExample example=new TbContentExample();
		example.createCriteria().andCategoryIdEqualTo(cid);
		
		List<TbContent> list=contentMapper.selectByExample(example);
		
		
		//把结果添加到缓存************************
		try {
			jedisClient.hset(INDEX_CONTENT, cid+"", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

}
