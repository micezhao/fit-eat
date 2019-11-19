package com.f.a.kobe.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

/**
 * 通过redis的自增机制实现的生成有序的id
 * 
 * @author micezhao
 *
 */
@Component
public class RedisSequenceUtils {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private RedisAtomicLong redisAtomicLong;

	private final static int DEFALUT_LENGHT = 8;

	private String key = "scorce_squence";

	private final String prefix = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());

	@Bean
	public RedisAtomicLong getRedisAtomicLong() {
		// 不需要设置带有默认值的构造方法，因为如果设定了默认值，项目再次启动式都将重设当前计数器
		RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
		Calendar todayEnd = Calendar.getInstance();
		todayEnd.set(Calendar.HOUR_OF_DAY, 23);
		todayEnd.set(Calendar.MINUTE, 59);
		todayEnd.set(Calendar.SECOND, 59);
		todayEnd.set(Calendar.MILLISECOND, 999);
		counter.expireAt(todayEnd.getTime()); //设置当前的键在当日的23:59:59:999 时失效
		return counter;
	}
	
	
	/**
	 * 根据业务类型生成全局有序id
	 * @param bizPrefix
	 * @return
	 */
	public String getRedisSequence(String bizPrefix) { 
		Long sequence = redisAtomicLong.incrementAndGet();
		String seq = String.valueOf(sequence);
		StringBuffer buffer = new StringBuffer(bizPrefix+prefix);
		int rest = DEFALUT_LENGHT - String.valueOf(sequence).length();
		if (rest > 0) {
			for (int i = 0; i < rest; i++) {
				buffer.append('0');
			}
		}
		buffer.append(seq);
		return buffer.toString();
	}

}
