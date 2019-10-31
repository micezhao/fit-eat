package com.f.a.kobe.manager.aop;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SyncToMongoDB {

	@Autowired
	private MongoTemplate mongoTemplate;

	private final static Logger logger = LoggerFactory.getLogger(SyncToMongoDB.class);

	private final static String POJO_PATH = "com.f.a.kobe.pojo.";

	@After(value = "@annotation(ToMongoDB)")
	public void dofore(JoinPoint joinPoint) throws ClassNotFoundException {
		logger.info("进入后置通知");
		String methodName = joinPoint.getSignature().getName();
		String declareName = joinPoint.getSignature().getDeclaringType().getSimpleName();
		String clazzName = extractClassName(declareName);
		Object args[] = joinPoint.getArgs();

		switch (methodName) {
		case "insert":
			syncInsertToMongoDB(args, clazzName);
			break;
		case "delete":
			syncDeleteToMongoDB(args, clazzName);
			break;
		case "update":
			syncUpdateToMongoDB(args, clazzName);
			break;
		default:
			break;
		}

	}

	@SuppressWarnings("static-access")
	private void syncUpdateToMongoDB(Object[] args, String clazzName) {
		String fullName = POJO_PATH + clazzName;
		Field fields[];
		try {
			// 通过反射进行一波骚操作
			fields = Class.forName(fullName).getClass().getDeclaredFields();
			Long idValue = null;
			List<String> excludeList = new ArrayList<String>(20); // 初始化容积为20个元素
			for (Field field : fields) {
				field.setAccessible(true);
				if(StringUtils.equals("id", field.getName()) ) {
					idValue = (Long) field.get(Class.forName(fullName).getClass());
				}
				excludeList.add(field.getName());
			}
			
			String[] exclude = null;
			if (!excludeList.isEmpty()) {
				exclude = (String[]) excludeList.toArray();
			}
			
			Query query = new Query(new Criteria().where("id").is(idValue));  // 封装查询条件，以找到需要被更新的对象
			Update update = Update.fromDocument((Document) args[0], exclude); // 封装更新的对象
			Long modifiedCount = mongoTemplate.updateFirst(query, update, clazzName).getModifiedCount();
			if(modifiedCount != 0) {
				logger.info("同步从mongodb中更新集合：{} ,id = {} 的数据,更新的内容为:{} ", clazzName, (String) args[0]);
			}
		} catch (Exception e) {
			// TODO 如果失败了，就需要记录一条数据同步操作失败的日志，定时任务定时轮训操作日志表中的数据，进行补偿
		} 
		
	}

	@SuppressWarnings("static-access")
	private void syncDeleteToMongoDB(Object[] args, String clazzName) {
		Query query = new Query(new Criteria().where("id").is(Long.parseLong((String) args[0])));
		boolean isAck = mongoTemplate.remove(query, clazzName).wasAcknowledged();
		if (isAck) {
			logger.info("同步从mongodb中删除集合：{} ,id = {} 的数据 ", clazzName, (String) args[0]);
		} else {
			// TODO 如果失败了，就需要记录一条数据同步操作失败的日志，定时任务定时轮训操作日志表中的数据，进行补偿
		}
	}

	private void syncInsertToMongoDB(Object[] args, String clazzName) throws ClassNotFoundException {
		try {
			mongoTemplate.insert(args[0], clazzName);
		} catch (Exception e) {
			// TODO 如果失败了，就需要记录一条数据同步操作失败的日志，定时任务定时轮训操作日志表中的数据，进行补偿
		}
	}

	public String extractClassName(String declareName) {
		boolean flag = declareName.endsWith("Manager");
		if (flag) {
			return declareName.substring(0, declareName.lastIndexOf("Manager"));
		}
		return null;
	}

}
