package com.taotao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 首页展示
 * @author Administrator
 *
 */
@Controller
public class PageController {

	@RequestMapping("/")
	public String showIndex(){
		return "index";
	}
	
	
	@RequestMapping("/{page}")
	public String showpage(@PathVariable String page){
		return page;
	}
}
