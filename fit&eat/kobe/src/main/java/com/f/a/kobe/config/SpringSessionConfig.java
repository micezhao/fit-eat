 package com.f.a.kobe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * description:TODO
 *
 * @version 1.0
 * @author: xianbang.yang
 * @date: 2019/10/26 17:07
 */
@EnableRedisHttpSession
public class SpringSessionConfig {

//    @Bean
//    JedisConnectionFactory connectionFactory(){
//        return new JedisConnectionFactory();
//    }
}
