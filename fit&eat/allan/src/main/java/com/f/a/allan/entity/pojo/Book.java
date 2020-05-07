package com.f.a.allan.entity.pojo;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection="book")
public class Book {
	@Id
    private Long id;
    //价格
    private Integer price;
    //书名
    private String name;
    //简介
    private String info;
    //出版社
    private String publish;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime; 
}
