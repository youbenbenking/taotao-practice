package com.taotao.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;

public class LoginInterceptor implements HandlerInterceptor {

	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	
	@Value("${SSO_URL}")
	private String SSO_URL;
	
	@Autowired
	private UserService userService;
	
	//执行handler之前执行此方法
	//返回值true:放行，false:拦截
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object object) throws Exception {
		//1.从cookie中取token信息
		//2.如果取不到token，跳转sso登录页面，需要把当前请求url作为参数传递给sso，登录成功后跳回请求的页面
		//3.取到token，调用sso系统服务判断用户是否登录
		//4.如果用户没登录，即没取到用户信息，跳转登录页面
		//5.如果取到用户信息，放行
		String token = CookieUtils.getCookieValue(request, TOKEN_KEY);
		if(StringUtils.isBlank(token)){
			//跳转登录页面
			String requestURL = request.getRequestURL().toString();
			response.sendRedirect(SSO_URL+"/page/login?url="+requestURL);
			//拦截
			return false;
		}
		TaotaoResult result = userService.getUserByToken(token);
		if(result.getStatus()!=200){
			//跳转登录页面
			String requestURL = request.getRequestURL().toString();
			response.sendRedirect(SSO_URL+"/page/login?url="+requestURL);
			//拦截
			return false;
		}
		
		//把用户信息放到request中,方便放行后执行的方法通过用户信息取收货地址
		TbUser user = (TbUser) result.getData();
		request.setAttribute("user", user);
		
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object object, ModelAndView modelAndView) throws Exception {
		// handler执行之后,ModelAndView返回之前

	}
	
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// ModelAndView返回之后,通常异常处理

	}

	

	

}
