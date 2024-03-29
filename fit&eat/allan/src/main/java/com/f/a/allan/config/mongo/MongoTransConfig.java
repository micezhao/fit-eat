package com.f.a.allan.config.mongo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import com.alibaba.fastjson.JSONObject;
import com.f.a.allan.entity.pojo.SkuConfig;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

/**
 * mongodb 配置类
 * @author micezhao
 *
 */
@Configuration
public class MongoTransConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(MongoTransConfig.class);
	
	private static final String PREFIX = "mongodb://";
	
	@Autowired
	private MongoConfigProperites mongoConfigProperites;
	
//	@Bean // 配置事务管理：mongodb 4.0以上版本才支持事务，4.0只支持副本集的事务，4.2支持分片场景的事务
//	MongoTransactionManager transactionManager(MongoDbFactory factory) {
//		return new MongoTransactionManager(factory);
//	}
	@Bean //配置连接工厂
	public MongoDbFactory mongoDbFactory() throws Exception {
//		return new SimpleMongoClientDbFactory(PREFIX+mongoConfigProperites.getUsername()+":"+mongoConfigProperites.getPassword()+"@"+mongoConfigProperites.getHost()+":"+mongoConfigProperites.getPort()+"/"+mongoConfigProperites.getDbName());
		return new SimpleMongoClientDbFactory(PREFIX+mongoConfigProperites.getUrl());
	}
	@Bean //将程序配置为一个mongodb的客户端
	public MongoClient mongoClient() {
//		return new MongoClient(mongoConfigProperites.getHost(),mongoConfigProperites.getPort());
		MongoCredential credential =  MongoCredential.createCredential(mongoConfigProperites.getUsername(),
				mongoConfigProperites.getDbName(), mongoConfigProperites.getPassword().toCharArray());
		MongoClientOptions options = MongoClientOptions.builder().sslEnabled(false).build();
		return new MongoClient(new ServerAddress(mongoConfigProperites.getHost(),
				mongoConfigProperites.getPort()), 
				credential, 
				options);
	}
	/**
	 * 重写mongoTemplate中的设置
	 * @return
	 * @throws Exception
	 */
	@Bean
	public MongoTemplate mongoTemplate() throws Exception{
		logger.info(">>>>加载自定义的mongoTemplate");
		DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory());
		MappingMongoConverter mappingMongoConverter = new MappingMongoConverter(dbRefResolver, new MongoMappingContext());
		//实现在向mongodb中插入数据是，不写入 _class : xxx 字段
		mappingMongoConverter.setTypeMapper( new DefaultMongoTypeMapper(null));
		mappingMongoConverter.setCustomConversions(customConversions());
		mappingMongoConverter.afterPropertiesSet();
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory(), mappingMongoConverter);
		return mongoTemplate;
	}
	
	// Direction: Java -> MongoDB
	@WritingConverter 
	public class DateToString implements Converter<LocalDateTime, String> {
	    @Override
	    public String convert(LocalDateTime source) {
	        return source.toString() + 'Z';
	    }
	}

	// Direction: MongoDB -> Java
	@ReadingConverter 
	public class StringToDate implements Converter<String, LocalDateTime> {
		
	    @Override
	    public LocalDateTime convert(String source) {
	        return LocalDateTime.parse(source,DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
	    }
	}
	
    public CustomConversions customConversions() {
        List<Converter<?, ?>> converterList = new ArrayList<Converter<?, ?>>();
        converterList.add(new DateToString());
        converterList.add(new StringToDate());
        return new MongoCustomConversions(converterList);
    }
	 
}
