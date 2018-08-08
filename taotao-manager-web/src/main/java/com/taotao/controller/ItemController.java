package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;

/**
 * 商品管理
 * @author Administrator
 *
 */

@Controller
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/{itemId}")
	@ResponseBody	//一般依赖json相关包，将对象转化成json字符串
	public TbItem getItemById(@PathVariable long itemId){
		
		 TbItem tbItem=itemService.getItemById(itemId);
		 return tbItem;
	}
	
	
	@RequestMapping("/item/list")
	@ResponseBody	
	public EasyUIDataGridResult getItemList(Integer page,Integer rows){
		
		EasyUIDataGridResult result=itemService.getItemList(page, rows);
		return result;
	}
	
	@RequestMapping(value="/item/save",method=RequestMethod.POST)
	@ResponseBody	
	public TaotaoResult addItem(TbItem item,String desc){

		TaotaoResult taotaoResult = itemService.addItem(item, desc);
		return taotaoResult;
	}

}
