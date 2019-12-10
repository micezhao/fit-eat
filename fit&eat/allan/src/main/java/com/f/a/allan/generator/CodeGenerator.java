package com.f.a.allan.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class CodeGenerator {
	
	//生成文件所在项目路径
	private static String baseProjectPath = "/Users/micezhao/fit&eat/fit&eat/allan/src/main/java";
	
	
	private static String basePackage = "com.f.a.allan";
	//数据库配置四要素
	private static String driverName = "com.mysql.cj.jdbc.Driver";
	private static String url = "jdbc:mysql://118.190.53.214:3386/fa_kobe?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8";
	private static String username = "root";
	private static String password = "891122";
	
	
     public static void main(String[] args) {
    	 AutoGenerator gen = new AutoGenerator();
    	 // 数据库连接配置
    	 gen.setDataSource(new DataSourceConfig()
    			 .setDbType(DbType.MYSQL)
    			 .setDriverName(driverName)
    			 .setUrl(url)
    			 .setUsername(username)
    			 .setPassword(password)
    			 );
    	 
    	 gen.setGlobalConfig(new GlobalConfig()
    			 .setAuthor("micezhao")
    			 .setOutputDir(baseProjectPath)
    			 .setActiveRecord(true)// 开启 activeRecord 模式
    	         .setEnableCache(false)// XML 二级缓存
    	         .setBaseResultMap(true)// XML ResultMap
    	         .setBaseColumnList(true)// XML columList
    	         .setOpen(false)//生成后打开文件夹
    	            // 自定义文件命名，注意 %s 会自动填充表实体属性！
    	         .setMapperName("%sMapper")
    	         .setXmlName("%sMapper")
    	         .setServiceName("%sService")
    	         .setServiceImplName("%sServiceImpl")
    	         .setControllerName("%sController")
    			 );
    	 gen.setStrategy(new StrategyConfig()
    			 .setNaming(NamingStrategy.underline_to_camel)// 表名生成策略:下划线转驼峰命名
    			 .setInclude("goods","merchant","order") // 【注意：】需要生成的表 
    			 .setEntityLombokModel(true)
    			 );
    	 gen.setPackageInfo(new PackageConfig()
    	            //.setModuleName("User")
    	            .setParent(basePackage)// 自定义包路径
    	            .setController("ctrl")// 这里是控制器包名，默认 web
    	            .setEntity("pojo")
    	            .setMapper("dao")
    	            .setService("service")
    	            .setServiceImpl("service.impl")
    	            .setXml("mapper")
    	    );
    	 gen.setTemplate(new TemplateConfig());
    	 gen.execute();
    	 
	}
}
