package com.f.a.allan.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.f.a.allan.entity.request.comment.CommentAddRequest;

/**
 * 评价业务服务类
 * @author micezhao
 *
 */
@Service
public class CommentBiz {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public void addComment(CommentAddRequest request) {
		
	}
}
