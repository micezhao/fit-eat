package com.f.a.kobe.service.impl.wx;

import java.security.AlgorithmParameters;
import java.security.Security;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.f.a.kobe.config.credential.WeChatConfigProperties;
import com.f.a.kobe.exceptions.ErrEnum;
import com.f.a.kobe.manager.previous.CustomerBaseInfoManager;
import com.f.a.kobe.pojo.bo.AuthBo;
import com.f.a.kobe.pojo.enums.DrEnum;
import com.f.a.kobe.pojo.enums.LoginTypeEnum;
import com.f.a.kobe.pojo.previous.CustomerCredential;
import com.f.a.kobe.pojo.request.ParamRequest;
import com.f.a.kobe.service.previous.CustomerCredentialService;

@Component(value="wxCustomerCredentialService")
public class WxCustomerCredentialService extends CustomerCredentialService {

	private static final long KEY_EXPIRE = 1000L*60*60*24;

	@Autowired
	private WeChatConfigProperties weChatConfigProperties;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private CustomerBaseInfoManager customerBaseInfoManager;
	
	private static final String hashKey = "customerAuth";

	private static final String AES_CBC = "AES/CBC/PKCS5Padding";

	private static final String AES = "AES";

	public void updateCustomerCredential(CustomerCredential customerCredential) {
		manager.update(customerCredential);
	}


	public List<CustomerCredential> listCustomerCredential(CustomerCredential conditional) {
		return manager.listByConditional(conditional);
	}

	// 根据openid 判断用户是否存在于授权表
	private CustomerCredential getCustomerCredentialByOpenid(String openid) {
		CustomerCredential conditional = new CustomerCredential();
		conditional.setWxOpenid(openid);
		List<CustomerCredential> customerCredentialList = manager.listByConditional(conditional);
		if (customerCredentialList.size() > 0) {
			return customerCredentialList.get(0);
		}
		return null;
	}

	// 将加密内容根据sessionkey解析出手机号
	public Object getPhoneNumber(String encryptedData, String session_key, String iv) {
		// 被加密的数据
		byte[] dataByte = Base64.decodeBase64(encryptedData);
		// 加密秘钥
		byte[] keyByte = Base64.decodeBase64(session_key);
		// 偏移量
		byte[] ivByte = Base64.decodeBase64(iv);
		try {
			// 如果密钥不足16位，那么就补足. 这个if 中的内容很重要
			int base = 16;
			if (keyByte.length % base != 0) {
				int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
				byte[] temp = new byte[groups * base];
				Arrays.fill(temp, (byte) 0);
				System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
				keyByte = temp;
			}
			// 初始化
			Security.addProvider(new BouncyCastleProvider());
			Cipher cipher = Cipher.getInstance(AES_CBC);
			SecretKeySpec spec = new SecretKeySpec(keyByte, AES);
			AlgorithmParameters parameters = AlgorithmParameters.getInstance(AES);
			parameters.init(new IvParameterSpec(ivByte));
			cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
			byte[] resultByte = cipher.doFinal(dataByte);
			if (null != resultByte && resultByte.length > 0) {
				String result = new String(resultByte, "UTF-8");
				return JSONObject.parseObject(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected CustomerCredential queryCustomerCredential(String authCode) {
		CustomerCredential record = manager.queryByAutCode(authCode, LoginTypeEnum.WECHAT.getLoginTypeCode());
		return record;
	}
	
	
	// 与微信交互
	@SuppressWarnings("unchecked")
	@Override
	public AuthBo getAuthInfoByLoginRequest(ParamRequest requestAuth) {
		String code = requestAuth.getCode();
		String result = requestWxAuthInfoByCode1(code);
		WxLoginSuccess wxLoginSuccess = JSONObject.parseObject(result, WxLoginSuccess.class);
		String session_key = wxLoginSuccess.getSession_key();
		String md5Hex = DigestUtils.md5Hex(session_key);
		redisTemplate.opsForValue().set(md5Hex, session_key);
		redisTemplate.expire(md5Hex, KEY_EXPIRE, TimeUnit.SECONDS);
		AuthBo wxAuthResult = new AuthBo();
		wxAuthResult.setOpenid(wxLoginSuccess.getOpenid());
		wxAuthResult.setAuthToken(md5Hex);
		wxAuthResult.setAuthType(LoginTypeEnum.WECHAT.getLoginTypeCode());
		return wxAuthResult;
	}

	private String requestWxAuthInfoByCode(String code) {
		String request2WxAuthUrl = MessageFormat.format(weChatConfigProperties.getUrlPattern(),
				weChatConfigProperties.getAppId(), weChatConfigProperties.getAppSecret(), code);
		String result = restTemplate.getForObject(request2WxAuthUrl, String.class);
		if (StringUtils.contains(result, weChatConfigProperties.getErrtag())) {
			throw new RuntimeException(ErrEnum.WX_AUTH_INVAILD.getErrMsg());
		}
		return result;
	}
	
	private String requestWxAuthInfoByCode1(String code) {
		
		Random random = new Random();
		int nextInt = random.nextInt();
		
		String result = "{\"session_key\":\"KdAdSaaNNDAS"+nextInt+"8877JSADN+1==\",\"openid\":\"o"+nextInt+"ndaJdsaJ3omdasmn-LU\"}";
		return result;
	}

	@Override
	public boolean existsed(AuthBo authInfoByLoginRequest) {
		CustomerCredential customerCredential = new CustomerCredential();
		customerCredential.setWxOpenid(authInfoByLoginRequest.getOpenid());
		 List<CustomerCredential> list= manager.listByConditional(customerCredential);
		 if(list.isEmpty()) {
			 return false;
		 }
		 return true;
	}
	

	@Override
	public CustomerCredential insertCustomerCredential(AuthBo authBo) {
		CustomerCredential customerCredential = new CustomerCredential();
		customerCredential.setDr(DrEnum.AVAILABLE.getCode());
		customerCredential.setCustomerId(authBo.getCustomerId());
		customerCredential.setWxOpenid(authBo.getOpenid());
		manager.insert(customerCredential);
		return customerCredential;
	}


	@Override
	public void registerCustomer(ParamRequest request) {
		CustomerCredential customerCredential = manager.queryByBiz(request.getCustomerId());
		customerCredential.setMobile(request.getMobile());
		manager.update(customerCredential);
	}

}
