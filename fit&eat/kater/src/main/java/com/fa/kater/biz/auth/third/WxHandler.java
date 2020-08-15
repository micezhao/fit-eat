package com.fa.kater.biz.auth.third;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fa.kater.exceptions.ErrRtn;
import com.fa.kater.pojo.AgentThirdConfig;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WxHandler implements ThirdAuthHandlerInterface{
	
	@Autowired
	private RestTemplate restTemplate;
	
	private static final String AUTH_URL = "https://api.weixin.qq.com/sns/jscode2session";
	
	private static final String SUCCESS_CODE = "0";
	
	@Override
	public String getOpenId(AgentThirdConfig agentConfig, String thirdAuthId) {
		Map<String,String> requestMap = new HashMap<String,String>();
		requestMap.put("appid", agentConfig.getAppId());
		requestMap.put("secret",  agentConfig.getAppSecretkey());
		requestMap.put("js_code",  thirdAuthId);
		requestMap.put("grant_type",  "authorization_code");
		String openId = null;
		String result=restTemplate.getForObject(AUTH_URL+"?appid={appid}&secret={secret}&js_code={js_code}&grant_type={grant_type}", String.class, requestMap);
		JSONObject json = JSONObject.parseObject(result);
		if(StringUtils.equals(json.getString("errcode"), SUCCESS_CODE) ) {
			openId = json.getString("openid");
			String sessionKey = json.getString("session_key");
			log.info("获取微信openId成功,openId:{}|session_key:{}",openId,sessionKey);
		}else {
			log.error("获取微信openId失败,错误码:{}|错误原因:{}", json.getString("errcode"),json.getString("errmsg"));
			throw new RuntimeException( new ErrRtn(json.getString("errcode"), json.getString("errmsg")));
		}
		return openId;
	}
	
}
