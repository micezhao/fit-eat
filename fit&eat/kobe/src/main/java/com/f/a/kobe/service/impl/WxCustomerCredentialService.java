package com.f.a.kobe.service.impl;

import java.security.AlgorithmParameters;
import java.security.Security;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.f.a.kobe.config.credential.WeChatConfigProperties;
import com.f.a.kobe.exceptions.ErrCodeEnum;
import com.f.a.kobe.manager.CustomerBaseInfoManager;
import com.f.a.kobe.manager.CustomerCredentialManager;
import com.f.a.kobe.pojo.CustomerBaseInfo;
import com.f.a.kobe.pojo.CustomerCredential;
import com.f.a.kobe.pojo.DrEnum;
import com.f.a.kobe.service.CustomerCredentialService;
import com.f.a.kobe.util.IdWorker;

@Component
public class WxCustomerCredentialService extends CustomerCredentialService {

	@Autowired
	private WeChatConfigProperties weChatConfigProperties;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private CustomerCredentialManager customerCredentialManager;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private CustomerBaseInfoManager customerBaseInfoManager;

	@Autowired
	private IdWorker idworker;

	private static final String hashKey = "customerAuth";

	private static final String AES_CBC = "AES/CBC/PKCS5Padding";

	private static final String AES = "AES";

	@Override
	public String getAuthStringByCode(String code) {
		String request2WxAuthUrl = MessageFormat.format(weChatConfigProperties.getUrlPattern(),
				weChatConfigProperties.getAppId(), weChatConfigProperties.getAppSecret(), code);
		String result = restTemplate.getForObject(request2WxAuthUrl, String.class);
		if (StringUtils.contains(result, weChatConfigProperties.getErrtag())) {
			throw new RuntimeException(ErrCodeEnum.WX_AUTH_INVAILD.getErrMsg());
		}
		String openid = "";
		if (getCustomerCredentialByOpenid(openid) == null) {
			// 添加用户到授权信息表
		}
		String returnStr = "";

		return returnStr;
	}

	@Override
	public void insertCustomerCredential(CustomerCredential customerCredential) {
		customerCredential.setCustomerId(idworker.nextId());
		customerCredential.setDr(DrEnum.AVAILABLE.getCode());
		customerCredential.setCdt(Calendar.getInstance().getTime());
		int effectRows = customerCredentialManager.insert(customerCredential);
		if (effectRows == 0) {
			throw new RuntimeException(ErrCodeEnum.WX_AUTH_INSERTFAil_INVAILD.getErrMsg());
		}

	}

	@Override
	public void insertCustomerBaseInfoWithCustomerCredential(CustomerBaseInfo customerBaseInfo,
			CustomerCredential conditional) {
		CustomerCredential customerCredential = customerCredentialManager.queryByBiz(conditional.getCustomerId());
		customerBaseInfo.setMdt(Calendar.getInstance().getTime());
		customerBaseInfo.setCustomerId(customerCredential.getCustomerId());
		customerBaseInfoManager.insert(customerBaseInfo);
	}

	public void updateCustomerCredential(CustomerCredential customerCredential) {
		customerCredentialManager.update(customerCredential);
	}

	public void registerCustomerBaseInfo(CustomerBaseInfo customerBaseInfo, CustomerCredential customerCredential,
			Object object) {
		WxAuthRegistryBean wxAuthRegistryBean = (WxAuthRegistryBean) object;
		
		String sessionId = wxAuthRegistryBean.getSessionId();
		WxAuthInfoBean wxAuthInfoBean = getSessionKeyAndOpenidByEncryptedStr(sessionId);
		
		getPhoneNumber(wxAuthRegistryBean.getEncryptedData(),wxAuthInfoBean.getSessionKey(),wxAuthRegistryBean.getIv());
		
		///未完成的手机号解析
		String phonenum = "";
		customerBaseInfo.setMdt(Calendar.getInstance().getTime());
		customerBaseInfo.setMobile(phonenum);
		customerCredential.setMdt(Calendar.getInstance().getTime());
		customerBaseInfo.setMobile(phonenum);
		customerCredentialManager.update(customerCredential);
		customerBaseInfoManager.update(customerBaseInfo);
	}

	public List<CustomerCredential> listCustomerCredential(CustomerCredential conditional) {
		return customerCredentialManager.listByConditional(conditional);
	}

	// 根据openid 判断用户是否存在于授权表
	private CustomerCredential getCustomerCredentialByOpenid(String openid) {
		CustomerCredential conditional = new CustomerCredential();
		conditional.setWxOpenid(openid);
		List<CustomerCredential> customerCredentialList = customerCredentialManager.listByConditional(conditional);
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

	// 根据加密字符串解析出openid和sessionkey
	public WxAuthInfoBean getSessionKeyAndOpenidByEncryptedStr(String sessionId) {
		WxAuthInfoBean wxAuthInfoBean = (WxAuthInfoBean) redisTemplate.opsForHash().get(hashKey, sessionId);
		return wxAuthInfoBean;
	}

	@Override
	protected CustomerCredential queryCustomerCredential(String authCode) {
		// TODO Auto-generated method stub
		return null;
	}

}
