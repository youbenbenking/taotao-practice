package com.taotao.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;
/**
 * 
 * @author Administrator
 *
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	@Override
	public List<EasyUITreeNode> getContentCategoryList(long parentId) {
		// TODO Auto-generated method stub
		//根据parentId查询子节点
		TbContentCategoryExample example=new TbContentCategoryExample();
		//设置查询条件
		Criteria criteria= example.createCriteria();
		
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbContentCategory> list=contentCategoryMapper.selectByExample(example);
		
		List<EasyUITreeNode> resultList=new ArrayList<EasyUITreeNode>();
		for(TbContentCategory tbContentCategory:list){
			EasyUITreeNode node=new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			//添加到结果列表
			resultList.add(node);
		}
			return resultList;
	}

	@Override
	public TaotaoResult addContentCategory(Long parentId, String name) {
		// TODO Auto-generated method stub
		TbContentCategory contentCategory=new TbContentCategory();
		
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		contentCategory.setStatus(1);
		contentCategory.setSortOrder(1);
		contentCategory.setIsParent(false);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		
		contentCategoryMapper.insert(contentCategory);
		
		TbContentCategory parent=contentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parent.getIsParent()){
			parent.setIsParent(true);
			contentCategoryMapper.updateByPrimaryKey(parent);
		}
		
		return TaotaoResult.ok(contentCategory);
	}

}
