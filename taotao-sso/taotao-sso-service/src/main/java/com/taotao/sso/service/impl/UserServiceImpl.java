package com.taotao.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private TbUserMapper userMapper;
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${USER_SESSION}")
	private String USER_SESSION;
	
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	@Override
	public TaotaoResult checkData(String data, int type) {
		
		TbUserExample example=new TbUserExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		//1、判断用户名是否可用
		if(type == 1){
			criteria.andUsernameEqualTo(data);
		//2、判断手机号是否可用
		}else if(type == 2){
			criteria.andPhoneEqualTo(data);
		//3、判断邮箱是否可用
		}else if(type == 3){
			criteria.andEmailEqualTo(data);
		}else{
			return TaotaoResult.build(400, "参数中包含非法数据");
		}
		
		List<TbUser> list = userMapper.selectByExample(example);
		if(list!=null && list.size()>0){
			//查询到数据，返回false
			return TaotaoResult.ok(false);
		}
		//数据可用
		return TaotaoResult.ok(true);
	}
	
	
	
	

	@Override
	public TaotaoResult register(TbUser user) {
		//检验数据
		//判断用户名是否为空
		if(StringUtils.isBlank(user.getUsername())){
			return TaotaoResult.build(400, "用户名不能为空");
		}
		//判断用户名是否重复
		TaotaoResult result = checkData(user.getUsername(), 1);
		if(!(boolean)result.getData()){
			return TaotaoResult.build(400, "用户名重复");
		}
		//判断密码是否为空(密码可以重复)
		if(StringUtils.isBlank(user.getPassword())){
			return TaotaoResult.build(400, "密码不能为空");
		}
		//如果电话号码不为空的话进行重复校验
		if(StringUtils.isNotBlank(user.getPhone())){
			//检验是否重复
			result = checkData(user.getPhone(), 2);
			if(!(boolean)result.getData()){
				return TaotaoResult.build(400, "电话号码重复");
			}
		}
		//如果email不为空的话进行重复校验
		if(StringUtils.isNotBlank(user.getEmail())){
			//检验是否重复
			result = checkData(user.getEmail(), 3);
			if(!(boolean)result.getData()){
				return TaotaoResult.build(400, "email重复");
			}
		}
		//补全pojo属性
		user.setCreated(new Date());
		user.setUpdated(new Date());
		//密码进行md5加密
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass);
		//插入数据
		userMapper.insert(user);
		return TaotaoResult.ok();
	}


	@Override
	public TaotaoResult login(String username, String password) {
		// 判断用户名和密码是否正确
		// 密码进行md5加密后在校验
		// 生成token，使用uuid
		// 把用户信息保存到Redis，key就是token，value是用户信息
		// 设置key的过期时间
		// 返回登录成功，其中把token返回
		
		TbUserExample example=new TbUserExample();
		Criteria criteria = example.createCriteria();
		
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = userMapper.selectByExample(example);
		
		if(list==null || list.size()==0){
			return TaotaoResult.build(400, "用户名或密码不正确");
		}
		
		TbUser currentUser=list.get(0);
		if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(currentUser.getPassword())){
			return TaotaoResult.build(400, "用户名或密码不正确");
		}
		
		String token = UUID.randomUUID().toString();
		currentUser.setPassword(null);//保存到Redis中的session用户不应含密码
		jedisClient.set(USER_SESSION+":"+token, JsonUtils.objectToJson(currentUser));
		jedisClient.expire(USER_SESSION+":"+token, SESSION_EXPIRE);
		
		return TaotaoResult.ok(token);
	}





	@Override
	public TaotaoResult getUserByToken(String token) {
		String json = jedisClient.get(USER_SESSION+":"+token);
		if(StringUtils.isBlank(json)){
			TaotaoResult.build(400, "用户登录已过期");
		}
		//重置session的过期时间
		jedisClient.expire(USER_SESSION+":"+token, SESSION_EXPIRE);
		//将json转化成user对象
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		return TaotaoResult.ok(user);
	}
}
