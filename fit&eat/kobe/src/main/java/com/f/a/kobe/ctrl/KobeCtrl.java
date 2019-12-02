package com.f.a.kobe.ctrl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.f.a.kobe.pojo.request.ParamRequest;
import com.f.a.kobe.pojo.view.UserAgent;
import com.f.a.kobe.service.aop.ParamCheck;
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

	@GetMapping("kobe/user")
	public ResponseEntity<? extends Object> getUser(HttpSession session) {
		UserAgent useAgent = (UserAgent) session.getAttribute("userDetail");
		if (useAgent == null) {
			return new ResponseEntity<String>("请先访问localhost:8763/login 登陆本系统：",HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		}
		return  new ResponseEntity<UserAgent>(useAgent,HttpStatus.OK);
	}
	
	
	@PostMapping("kobe/buy")
	//public String buy(HttpSession session) {
	public String buy(UserAgent userAgent) {
		if (userAgent == null) {
			return "请先访问localhost:8763/login 登陆本系统：";
		}
		return userAgent.getNickname() + "恭喜您！兑换成功";
	}
	
	@GetMapping("kobe/getSequence")
	public String getSequence() {
		 return sequenceUtils.getRedisSequence("test");
	}
	
}
