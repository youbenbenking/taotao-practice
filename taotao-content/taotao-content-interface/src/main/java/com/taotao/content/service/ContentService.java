package com.taotao.content.service;

import java.util.List;

import com.taotao.pojo.TbContent;

public interface ContentService {

	List<TbContent> getContentByCid(long cid);
}
