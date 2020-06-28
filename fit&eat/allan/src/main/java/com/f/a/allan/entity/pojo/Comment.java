package com.f.a.allan.entity.pojo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.f.a.allan.entity.bo.MediaAttachment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("Comment")
/**
 * 评价
 * @author micezhao
 *
 */
public class Comment {

	@Id
	private String commentId;
	
	private String goodsId;
	
	private String stars;
	
	private String content;
	
	private String states;
	
	private List<MediaAttachment> attachments; 
	
	private String reply;
	
	private LocalDateTime cdt;
	
	private LocalDateTime mdt;
	
	
}
