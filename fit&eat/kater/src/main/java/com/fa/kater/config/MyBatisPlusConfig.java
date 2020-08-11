package com.fa.kater.config;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.AbstractSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.core.injector.methods.*;
import com.baomidou.mybatisplus.extension.injector.methods.LogicDeleteByIdWithFill;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Configuration
@EnableTransactionManagement
@MapperScan("com.fa.kater.auth.mapper")
public class MyBatisPlusConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;
    }

    @Bean
    public ISqlInjector sqlInjector() {
    	return new AbstractSqlInjector() {

			@Override
			public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
				  return Stream.of(
				            new Insert(),
				            new Delete(),
				            new DeleteByMap(),
				            new DeleteById(),
				            new DeleteBatchByIds(),
				            new Update(),
				            new UpdateById(),
				            new SelectById(),
				            new SelectBatchByIds(),
				            new SelectByMap(),
				            new SelectOne(),
				            new SelectCount(),
				            new SelectMaps(),
				            new SelectMapsPage(),
				            new SelectObjs(),
				            new SelectList(),
				            new SelectPage(),
				            new LogicDeleteByIdWithFill()
				        ).collect(toList());
				    };
        };
    	//return new LogicSqlInjector();
    }
}
