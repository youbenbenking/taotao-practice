package com.taotao.fastdfs;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import com.taotao.utils.FastDFSClient;

public class TestFastDFS {

	@Test
	public void uploadFile() throws Exception{
		//1、向工程中添加jajar包
		//2、创建一个配置文件。配置tracker服务器地址
		//3、加载配置文件
		ClientGlobal.init("D:/lunaspace/taotao-manager-web/src/main/resources/resource/client.conf");
		//4、创建一个TrackerClient对象
		TrackerClient trackerClient=new TrackerClient();
		//5、使用trackerClient对象获取trackerServer对象
		TrackerServer trackerServer=trackerClient.getConnection();
		//6、创建一个StorageServer的引用null就可以
		StorageServer storageServer=null;
		//7、创建一个storageClient。trackerServer，storageServer两个参数
		StorageClient storageClient=new StorageClient(trackerServer, storageServer);
		//8、使用storageClient对象上传文件
		String[] strings=storageClient.upload_file("C:/Users/Administrator/Desktop/images/aaa.png", "png", null);
		for (String string : strings) {
			System.out.println(string);
		}
	}
	
	public void testFastDfsClient() throws Exception{
		FastDFSClient fastDFSClient = new FastDFSClient("D:/lunaspace/taotao-manager-web/src/main/resources/resource/client.conf");
		String string = fastDFSClient.uploadFile("C:/Users/Administrator/Desktop/images/aaa.png");
		System.out.println(string);
	}
}
