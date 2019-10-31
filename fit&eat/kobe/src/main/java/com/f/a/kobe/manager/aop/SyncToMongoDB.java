package com.f.a.kobe.manager.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SyncToMongoDB {
	
	@Autowired
	private MongoTemplate mongoTemplate;

	private final static Logger logger = LoggerFactory.getLogger(SyncToMongoDB.class);

	@After(value = "@annotation(ToMongoDB)")
	public void dofore(JoinPoint joinPoint) throws ClassNotFoundException {
		logger.info("进入后置通知");
		String methodName = joinPoint.getSignature().getName();
		String declareName =  joinPoint.getSignature().getDeclaringType().getSimpleName();
		String clazzName = extractClassName(declareName);
		Object args[] = joinPoint.getArgs();

		switch (methodName) {
		case "insert":
			syncInsertToMongoDB(args,clazzName);
			break;
		case "delete":
			syncDeleteToMongoDB(args,clazzName);
			break;
		case "update":
			syncUpdateToMongoDB(args,clazzName);
			break;
		default:
			break;
		}
		
	}

	private void syncUpdateToMongoDB(Object[] args, String clazzName ) {
		
	}

	@SuppressWarnings("static-access")
	private void syncDeleteToMongoDB(Object[] args,String clazzName) {
		Query query = new Query(new Criteria().where("id").is(Long.parseLong((String)args[0])));
		mongoTemplate.remove(query, clazzName);
	}

	private void syncInsertToMongoDB(Object[] args,String clazzName) throws ClassNotFoundException {
		 mongoTemplate.insert(args[0], clazzName);
	}
	
	public String extractClassName(String declareName) {
		boolean flag = declareName.endsWith("Manager");
		if(flag) {
			return declareName.substring(0,declareName.lastIndexOf("Manager"));
		}
		return null;
	}
	

}
