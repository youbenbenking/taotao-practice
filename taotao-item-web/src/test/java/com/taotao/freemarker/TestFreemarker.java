package com.taotao.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class TestFreemarker {

	@Test
	public void testFreemarker() throws Exception{
		//1、创建一个模板对象
		//2、创建一个Configuration对象
		//3、设置模板所在路径
		//4、设置模板的字符集
		//5、使用Configuration对象加载一个模板文件，需要指定模板文件的文件名
		//6、创建一个数据集，可以使pojo，也可以是Map，推荐使用map
		//7、创建一个Writer对象，指定输出文件的 路径及文件名
		//8、使用模板对象的process方法输出文件
		//9、关闭流
		
		Configuration configuration = new Configuration(Configuration.getVersion());
		configuration.setDirectoryForTemplateLoading(new File("D:/lunaspace/taotao-item-web/src/main/webapp/WEB-INF/ftl"));
		configuration.setDefaultEncoding("utf-8");
		Template template = configuration.getTemplate("student.ftl");
		
		Map data=new HashMap<>();
		data.put("hello", "hello freemarker");
		
		Student student = new Student(23, "发条", 18, "召唤师峡谷");
		data.put("student", student);
		
		List<Student> stuList=new ArrayList<Student>();
		stuList.add(new Student(1, "发条1", 11, "召唤师峡谷"));
		stuList.add(new Student(2, "发条2", 12, "召唤师峡谷"));
		stuList.add(new Student(3, "发条3", 13, "召唤师峡谷"));
		stuList.add(new Student(4, "发条4", 14, "召唤师峡谷"));
		stuList.add(new Student(5, "发条5", 15, "召唤师峡谷"));
		stuList.add(new Student(6, "发条6", 16, "召唤师峡谷"));
		
		data.put("stuList", stuList);
		//日期类型
		data.put("date", new Date());
		data.put("val", "123456");
		
		Writer out=new FileWriter(new File("C:/Users/Administrator/Desktop/taotao_note/student.txt"));
		template.process(data, out);
		
		out.close();
	}
}
