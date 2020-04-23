package com.f.a.allan.config.mybatis;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;

import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.extension.parsers.DynamicTableNameParser;
import com.baomidou.mybatisplus.extension.parsers.ITableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;

/**
 * mybatis动态表名配置，暂时不开启
 * @author micezhao
 *
 */
//@Configuration
@MapperScan("com.f.a.allan.dao.*")
public class MybatisConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(MybatisConfig.class);
	
	private static final String TABLE_ORDER = "order";
	
	// 动态表名解析器
//	@Bean
    public PaginationInterceptor paginationInterceptor() {
		logger.info(">>>>>加载动态订单表表名的策略: order_yyyyMMdd");
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        DynamicTableNameParser orderdynamicParser = new DynamicTableNameParser();
        //设置动态表名的策略,注意表名如果是sql关键字要加上``
        orderdynamicParser.setTableNameHandlerMap(new HashMap<String, ITableNameHandler>(2) {{
            put("`order`", new ITableNameHandler() {
				@Override
				public String dynamicTableName(MetaObject metaObject, String sql, String tableName) {
					String dynamicDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	                return TABLE_ORDER+"_"+ dynamicDate;
				}
            });
        }});
        paginationInterceptor.setSqlParserList(Collections.singletonList(orderdynamicParser));
        return paginationInterceptor;
    }
}
