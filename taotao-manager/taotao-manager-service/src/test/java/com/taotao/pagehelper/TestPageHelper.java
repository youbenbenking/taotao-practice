package com.taotao.pagehelper;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemExample.Criteria;

public class TestPageHelper {

	@Test
	public void testPageHelper(){
		/**1、在mybatis的配置文件中配置分页插件
		 * 2、在执行查询之前配置分页条件。使用PageHelper的静态方法
		 * 3、执行查询
		 * 4、取分页信息，使用PageInfo对象取
		 */
		
		PageHelper.startPage(1, 10);
		
		ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		TbItemMapper tbItemMapper=applicationContext.getBean(TbItemMapper.class);
		//创建Example对象
		TbItemExample example=new TbItemExample();
		//Criteria criteria=example.createCriteria();
		List<TbItem> list=tbItemMapper.selectByExample(example);
		
		PageInfo<TbItem> pageInfo=new PageInfo<TbItem>(list);
		
		System.out.println("总记录数："+pageInfo.getTotal());
		System.out.println("总记页数："+pageInfo.getPages());
		System.out.println("返回的记录数："+list.size());
		
		
	}
}
