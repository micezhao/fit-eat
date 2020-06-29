package com.f.a.allan.biz;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.f.a.allan.entity.bo.CommentReply;
import com.f.a.allan.entity.bo.CommentReply.CommentReplyBuilder;
import com.f.a.allan.entity.constants.FieldConstants;
import com.f.a.allan.entity.pojo.Comment;
import com.f.a.allan.entity.request.comment.CommentAddRequest;
import com.f.a.allan.entity.request.comment.CommentQueryRequest;
import com.f.a.allan.entity.request.comment.CommentUpdateRequest;
import com.f.a.allan.enums.CommentVerifyEnum;

/**
 * 评价业务服务类
 * 
 * @author micezhao
 *
 */
@Service
public class CommentBiz {

	@Autowired
	private MongoTemplate mongoTemplate;

	/**
	 * 用户发表对商品的评价
	 * 
	 * @param request
	 * @return
	 */
	public Comment addComment(CommentAddRequest request, String userAccount) {
		String stars = request.getStars();
		CommentVerifyEnum verifyEnum = CommentVerifyEnum.VERIFIED;
		if (Integer.parseInt(stars) <= 3) {
			verifyEnum = CommentVerifyEnum.WAIT_VERIFY;
		}
		Comment comment = Comment.builder().userAccount(userAccount).spuId(request.getSpuId())
				.goodsId(request.getGoodsId()).content(request.getContent()).stars(request.getStars())
				.states(verifyEnum.getCode()).attachments(request.getAttachments()).cdt(LocalDateTime.now()).build();
		return mongoTemplate.insert(comment);
	}

	/**
	 * 商户回复评价
	 * 
	 * @return
	 */
	public Comment replyComment(String merchantId, CommentUpdateRequest request) {
		CommentReplyBuilder builder = CommentReply.builder();
		builder.merchantId(merchantId).commentId(request.getCommentId());
		if (StringUtils.isNotBlank(request.getStates())) {
			builder.states(request.getStates());
		}
		if (StringUtils.isNotBlank(request.getReplyContnet())) {
			builder.content(request.getReplyContnet());
		}
		CommentReply reply = builder.build();
		Query query = new Query().addCriteria(new Criteria(FieldConstants.COMMENT_ID).is(request.getCommentId()));
		Update update = new Update();
		update.set(FieldConstants.COMMENT_REPLY, reply);
		update.set(FieldConstants.MDT, LocalDateTime.now());
		return  mongoTemplate.findAndModify(query, update,FindAndModifyOptions.options().returnNew(true), Comment.class);
	}
	
	/**
	 * 用户删除评论【删除操作只有用户可以进行】
	 * @param userAccount
	 * @param commentId
	 * @return
	 */
	public boolean deleteComment(String userAccount,String commentId) {
		Query query = new Query().addCriteria(new Criteria(FieldConstants.COMMENT_ID).is(commentId))
									.addCriteria(new Criteria(FieldConstants.USER_ACCOUNT).is(userAccount));
		mongoTemplate.findAndRemove(query, Comment.class);
		return true;
	}
	
	public List<Comment> listComment(CommentQueryRequest request){
		
		return null;
	}

}
