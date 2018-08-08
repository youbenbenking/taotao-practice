package com.taotao.portal.controller;

import java.util.ArrayList;

import com.taotao.common.utils.JsonUtils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import com.taotao.portal.pojo.AD1Node;
/**
 * 首页展示
 * @author Administrator
 *
 */
@Controller
public class IndexController {
	
	//读取属性配置文件里面广告位的相关参数大广告位id，及适应的宽高(宽屏和窄屏)
	@Value("${AD1_CATEGORY}")
	private Long AD1_CATEGORY;
	
	@Value("${AD1_WIDTH}")
	private Integer AD1_WIDTH;
	
	@Value("${AD1_WIDTH_B}")
	private Integer AD1_WIDTH_B;
	
	@Value("${AD1_HEIGHT}")
	private Integer AD1_HEIGHT;
	
	@Value("${AD1_HEIGHT_B}")
	private Integer AD1_HEIGHT_B;
	
	@Autowired
	private ContentService contentService;
	
	
	@RequestMapping("/index")
	public String showIndex(Model model){
		//根据cid查询轮播图内容列表
		List<TbContent> conteList=contentService.getContentByCid(AD1_CATEGORY);
		//把列表转化成AD1Node列表
		List<AD1Node> ad1Nodes=new ArrayList<AD1Node>();
		for (TbContent tbContent : conteList) {
			AD1Node node=new AD1Node();
			node.setAlt(tbContent.getTitle());
			node.setWidth(AD1_WIDTH);
			node.setWidthB(AD1_WIDTH_B);
			node.setHeight(AD1_HEIGHT);
			node.setHeightB(AD1_HEIGHT_B);
			node.setHref(tbContent.getUrl());
			node.setSrc(tbContent.getPic());
			node.setSrcB(tbContent.getPic2());
			//添加到节点集合
			ad1Nodes.add(node);
		}
		//列表转化成json数据
		String ad1Json=JsonUtils.objectToJson(ad1Nodes);
		//把json数据传递给页面
		model.addAttribute("ad1", ad1Json);
		return "index";
	}

}
