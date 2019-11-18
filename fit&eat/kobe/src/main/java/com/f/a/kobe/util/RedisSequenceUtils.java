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
 * @author micezhao
 *
 */
@Component
public class RedisSequenceUtils {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private RedisAtomicLong redisAtomicLong;

	private final static Long INIT_VALUE = 0L;

	private final static int DEFALUT_LENGHT = 8;

	private String key = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());

	private final String prefix = this.key;

	@Bean
	public RedisAtomicLong getRedisAtomicLong() {
		RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory(), INIT_VALUE);
		return counter;
	}

	public String getRedisSequence() {
		Long sequence = redisAtomicLong.incrementAndGet();
		String seq = String.valueOf(sequence);
		StringBuffer buffer = new StringBuffer(prefix);
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
