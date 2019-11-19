package com.f.a.kobe.ctrl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.f.a.kobe.pojo.request.LoginRequest;
import com.f.a.kobe.pojo.view.UserAgent;
import com.f.a.kobe.util.RedisSequenceUtils;

@RestController
public class KobeCtrl {
	
	private final static  Logger logger = LoggerFactory.getLogger(KobeCtrl.class);
	
	@Autowired
	@Qualifier("regionRedisTemplate")
	private RedisTemplate<String, Object> regionRedisTemplate;
	
	@Autowired
	private RedisSequenceUtils sequenceUtils;
	
	@Value("${appname}")
	private String appname;
	
	@GetMapping("/kobe/redis")
	public String region() {
		Map<String,String> map = new HashMap<String,String>();
		map.put("city", "beijin");
		regionRedisTemplate.opsForHash().put("region", "1234", map);
		return "aaa";
	}
	
	@GetMapping("/kobe/syncChina")
	public String region_sync() {
		
		return "aaa";
	}
	
	@GetMapping("/kobe/getapp")
	public String getAppname(HttpSession session) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(appname);
		UUID uuid =(UUID)session.getAttribute("uuid");
		if(uuid != null) {
			buffer.append("|session:").append(uuid.toString());
		}
		return buffer.toString();
	}

	@GetMapping("/user")
	public String getUser(HttpSession session) {
		UserAgent useAgent = (UserAgent) session.getAttribute("userDetail");
		if (useAgent == null) {
			return "请先访问localhost:8763/login 登陆本系统：";
		}
		return JSON.toJSONString(useAgent);
	}
	
	@PostMapping("/login")
	public String login(HttpSession session,@RequestBody LoginRequest loginRequest,UserAgent userAgent) {
		
		if(StringUtils.equals("micezhao", loginRequest.getWxOpenid()) && StringUtils.equals("wx", loginRequest.getLoginType())) {
			userAgent = new UserAgent();
			userAgent.setAge(29);
			userAgent.setWxOpenid("micezhao");
			userAgent.setCustomerId(123456L);
			userAgent.setNickname("肿眼的熊");
			session.setAttribute("userDetail", userAgent);
		}
		return JSON.toJSONString(userAgent);
	}
	
	
	@PostMapping("/buy")
	public String buy(HttpSession session) {
		Object object = session.getAttribute("userDetail");
		UserAgent userAgent = JSON.parseObject(JSON.toJSONString(object), UserAgent.class);
		if (userAgent == null) {
			return "请先访问localhost:8763/login 登陆本系统：";
		}
		return userAgent.getNickname() + "恭喜您！兑换成功";
	}
	
	@GetMapping("/getSequence")
	public String getSequence() {
		 return sequenceUtils.getRedisSequence("test");
	}
}
