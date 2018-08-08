package com.taotao.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;

/**
 * 购物车管理
 * @author Administrator
 *
 */
@Controller
public class CartController {
	
	@Value("${CART_KEY}")
	private String CART_KEY;
	
	@Value("${CART_EXPIER}")
	private Integer CART_EXPIER;
	
	@Autowired
	private ItemService itemService;
	
	//添加购物车处理
	@RequestMapping("/cart/add/{itemId}")
	public String addItemCart(@PathVariable Long itemId,@RequestParam(defaultValue="1") Integer num,
				HttpServletRequest request,HttpServletResponse response){
		
		//取购物车商品列表
		List<TbItem> cartItemList = getCartItemList(request);
		//判断商品在购物车是否存在(存在数量相加;)
		boolean flag=false;
		for (TbItem tbItem : cartItemList) {
			if(tbItem.getId()==itemId.longValue()){
				tbItem.setNum(tbItem.getNum()+num);
				flag=true;
				break;
			}
		}
		//如果不存在,调用服务取商品信息,设置购买数量,添加一个新商品
		if(!flag){
			//调用服务取商品信息
			TbItem item = itemService.getItemById(itemId);
			item.setNum(num);	//设置商品购买数量
			//取图片地址字符串中一张图片即可
			String image = item.getImage();
			if(StringUtils.isNotBlank(image)){
				String[] images = image.split(",");
				item.setImage(images[0]);
			}
			//把商品添加到购物车
			cartItemList.add(item);
		}
		//把购物车列表写入cookie
		CookieUtils.setCookie(request, response, CART_KEY, JsonUtils.objectToJson(cartItemList), CART_EXPIER, true);
		//返回添加成功页面
		return "cartSuccess";
	}
	
	
	private List<TbItem> getCartItemList(HttpServletRequest request){
		//从购物车中取购物车商品列表
		String json = CookieUtils.getCookieValue(request, CART_KEY, true);
		if(StringUtils.isBlank(json)){
			return new ArrayList<>();
		}
		List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}
	
	//查看购物车处理
	@RequestMapping("/cart/cart")
	public String showCartList(HttpServletRequest request){
		//取购物车商品列表
		List<TbItem> cartItemList = getCartItemList(request);
		request.setAttribute("cartList", cartItemList);
		return "cart";
	}
	
	
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public TaotaoResult updateItemNum(@PathVariable Long itemId,@PathVariable Integer num,
			HttpServletRequest request,HttpServletResponse response){
		//从cookie中取购物车列表
		//查询到对应的商品
		//更新商品数量
		//将列表写入cookie
		//返回成功
		List<TbItem> cartItemList = getCartItemList(request);
		
		for (TbItem tbItem : cartItemList) {
			if(tbItem.getId()==itemId.longValue()){
				tbItem.setNum(num);
				break;	//退出循环
			}
		}
		
		CookieUtils.setCookie(request, response, CART_KEY, JsonUtils.objectToJson(cartItemList), CART_EXPIER, true);
		return TaotaoResult.ok();
	}
	
	
	@RequestMapping("/cart/delete/{itemId}")
	@ResponseBody
	public String deleteItem(@PathVariable Long itemId,@PathVariable Integer num,
			HttpServletRequest request,HttpServletResponse response){
		//从cookie中取购物车列表
		//查询到对应的商品后移除
		//将移除后的列表写入cookie
		//重定向到购物车页面
		List<TbItem> cartItemList = getCartItemList(request);
		
		for (TbItem tbItem : cartItemList) {
			if(tbItem.getId()==itemId.longValue()){
				cartItemList.remove(tbItem);
				break;	//退出循环
			}
		}
		
		CookieUtils.setCookie(request, response, CART_KEY, JsonUtils.objectToJson(cartItemList), CART_EXPIER, true);
		return "redirect:/cart/cart.html";
	}
}
