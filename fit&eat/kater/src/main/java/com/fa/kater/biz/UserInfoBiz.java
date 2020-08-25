package com.fa.kater.biz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.f.a.kobe.view.UserAgent;
import com.f.a.kobe.view.UserAgent.UserAgentBuilder;
import com.fa.kater.entity.requset.UserInfoRequest;
import com.fa.kater.enums.AuthTypeEnum;
import com.fa.kater.enums.LoginTypeEnum;
import com.fa.kater.pojo.MerchantInfo;
import com.fa.kater.pojo.ThirdCredential;
import com.fa.kater.pojo.UserInfo;
import com.fa.kater.service.MerchantInfoService;
import com.fa.kater.service.impl.ThirdCredentialServiceImpl;
import com.fa.kater.service.impl.UserInfoServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserInfoBiz {
	
	private final static String DEFAULT_AVATAR_URI = "/headimgs/avatar.png";
	
	@Autowired
	private UserInfoServiceImpl userInfoService;

	@Autowired
	private ThirdCredentialServiceImpl thirdCredentialService;
	
	@Autowired
	private MerchantBiz merchantBiz;
	
	
	public UserInfo initializeUserInfo(String merchantId) {
		Date date = new Date();
		Long timeStamp = date.getTime();
		String timeStampStr = String.valueOf(timeStamp);
		String nameSuffix = timeStampStr.substring(timeStampStr.length() - 8, timeStampStr.length());
		UserInfo userInfo = new UserInfo();
		String userAccount = UUID.randomUUID().toString();
		MerchantInfo merchantInfo = merchantBiz.getMerchantInfoByMerId(merchantId);
		userInfo.setAgentId(merchantInfo.getAgentId());
		userInfo.setMerchantId(merchantId);
		userInfo.setHeadimgUrl(DEFAULT_AVATAR_URI);
		userInfo.setNickname("anonymous"+nameSuffix);
		userInfo.setUserAccount(userAccount);
		userInfoService.save(userInfo); 
		return userInfo;
	}
	
	/**
	 * 用户代理对象生成器，返回登录用户视图
	 * 
	 * @param userAccount
	 * @return
	 */
	public UserAgent generator(String userAccount, String loginType, String authType) {
		UserInfo userInfo = new UserInfo();
		userInfo.setUserAccount(userAccount);
		userInfo.setDeleted(0);
		userInfo = userInfo.selectOne(new QueryWrapper<UserInfo>(userInfo));

		UserAgentBuilder builder = UserAgent.builder();
		builder.hasbinded(false);
		if (StringUtils.isNotBlank(userInfo.getAgentId())) {
			builder.agentId(userInfo.getAgentId());
		}
		if (StringUtils.isNotBlank(userInfo.getUserAccount())) {
			builder.userAccount(userInfo.getUserAccount());
		}
		if (StringUtils.isNotBlank(userInfo.getRealname())) {
			builder.realname(userInfo.getRealname());
		}
		if (StringUtils.isNotBlank(userInfo.getBirthday())) {
			builder.birthday(userInfo.getBirthday());
		}
		if (userInfo.getAge() != null && userInfo.getAge() != 0) {
			builder.age(userInfo.getAge());
		}
		if (StringUtils.isNotBlank(userInfo.getGender())) {
			builder.gender(userInfo.getGender());
		}
		if (StringUtils.isNotBlank(userInfo.getNickname())) {
			builder.nickname(userInfo.getNickname());
		}
		if (userInfo.getScore() != null) {
			builder.score(userInfo.getScore());
		}
		if (StringUtils.isNotBlank(userInfo.getMobile())) {
			builder.mobile(userInfo.getMobile());
			builder.hasbinded(true);
		}
		if (StringUtils.isNotBlank(userInfo.getMerchantId())) {
			builder.merchantId(userInfo.getMerchantId());
		}
		if (StringUtils.isNotBlank(userInfo.getAgentId())) {
			builder.agentId(userInfo.getAgentId());
		}
		builder.loginType(loginType);
		ThirdCredential credential = new ThirdCredential();
		credential.setUserAccount(userAccount);
		if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.WECHAT) {
			credential.setAuthType(AuthTypeEnum.AUTH_WX.type);
			credential = credential.selectOne(new QueryWrapper<ThirdCredential>(credential));
			builder.wxopenid(credential.getOpenId());
		} else if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.ALI_PAY) {
			credential.setAuthType(AuthTypeEnum.AUTH_ALI.type);
			builder.aliopenid(credential.getOpenId());
		} else {
			List<ThirdCredential> credentialList = credential.selectList(new QueryWrapper<ThirdCredential>(credential));
			for (ThirdCredential thirdCredential : credentialList) {
				if (thirdCredential.getAuthType() == AuthTypeEnum.AUTH_WX.type) {
					builder.aliopenid(thirdCredential.getOpenId());
				} else if (thirdCredential.getAuthType() == AuthTypeEnum.AUTH_ALI.type) {
					builder.wxopenid(thirdCredential.getOpenId());
				}
			}
		}
		builder.authType(authType);
		return builder.build();
	}
	
	/**
	 * TODO 查询方法，后期改为通过联表查询进行
	 * 更新用户视图对象
	 * @param userAgent
	 * @return
	 */
	private UserAgent updateUserAgent(UserAgent userAgent) {
		UserInfo queryEntity = new UserInfo();
		queryEntity.setUserAccount(userAgent.getUserAccount());
		queryEntity.setAgentId(userAgent.getAgentId());
		queryEntity.setDeleted(0);
	
		UserInfo userInfo = userInfoService.getOne(new QueryWrapper<UserInfo>(queryEntity));
		
		if (StringUtils.isNotBlank(userInfo.getRealname())) {
			userAgent.setRealname(userInfo.getRealname());
		}
		if (StringUtils.isNotBlank(userInfo.getBirthday())) {
			userAgent.setBirthday(userInfo.getBirthday());
		}
		if (userInfo.getAge() != null && userInfo.getAge() != 0) {
			userAgent.setAge(userInfo.getAge());
		}
		if (StringUtils.isNotBlank(userInfo.getGender())) {
			userAgent.setGender(userInfo.getGender());
		}
		if (StringUtils.isNotBlank(userInfo.getNickname())) {
			userAgent.setNickname(userInfo.getNickname());
		}
		if (userInfo.getScore() != null) {
			userAgent.setScore(userInfo.getScore());
		}
		if (StringUtils.isNotBlank(userInfo.getMobile())) {
			userAgent.setMobile(userInfo.getMobile());
			userAgent.setHasbinded(true);
		}
		
		// TODO 后期需要封装成方法，将当前用户的全部凭证通过联表查询的方法一次性返回
		List<ThirdCredential> thirdCredentialList = thirdCredentialService.list(new QueryWrapper<ThirdCredential>(
				new ThirdCredential().setUserAccount(userAgent.getUserAccount())
				)
			);
		if(!thirdCredentialList.isEmpty()) {
			for (ThirdCredential thirdCredential : thirdCredentialList) {
				if(AuthTypeEnum.getEnumByType(thirdCredential.getAuthType()) == AuthTypeEnum.AUTH_ALI ) {
					userAgent.setAliopenid( thirdCredential.getOpenId());
				}else if(AuthTypeEnum.getEnumByType(thirdCredential.getAuthType()) == AuthTypeEnum.AUTH_WX ) {
					userAgent.setWxopenid(thirdCredential.getOpenId());
				}
			}
		}
		return userAgent;
	}
	
	/**
	 * 完善个人信息,并执行通过手机号绑定的操作
	 * 
	 * @return
	 */
	public UserAgent fillUserInfo(UserInfoRequest request,UserAgent userAgent) {
		UserInfo userInfo = new UserInfo();
		if(StringUtils.isNotBlank(request.getBirthday())) {
			userInfo.setBirthday(request.getBirthday());
			getAgeByBirth(request.getBirthday());
		}
		if(StringUtils.isNotBlank(request.getGender())) {
			userInfo.setGender(request.getGender());
		}
		if(StringUtils.isNotBlank(request.getHeadimgUrl())) {
			userInfo.setHeadimgUrl(request.getHeadimgUrl());
		}
		if(StringUtils.isNotBlank(request.getNickname())) {
			userInfo.setNickname(request.getNickname());
		}
		if(StringUtils.isNotBlank(request.getRealname())) {
			userInfo.setRealname(request.getRealname());
		}
		if(StringUtils.isNotBlank(request.getScore())) {
			userInfo.setScore(Integer.valueOf(request.getScore()));
		}
		boolean result = userInfoService.update(userInfo, new UpdateWrapper<UserInfo>(new UserInfo().setUserAccount(userAgent.getUserAccount())));
		if(!result) {
			throw new RuntimeException("用户信息更新失败");
		}
		
		return this.updateUserAgent(userAgent);
	}

	/**
	 * 通过手机号绑定用户账号
	 * 
	 * @param mobile
	 * @param userAgent
	 * @return
	 */
	@Transactional
	public UserAgent bindAccountByMobile(String mobile, UserAgent userAgent) {
//		String agentId = userAgent.getAgentId();
		String merchantId = userAgent.getMerchantId();
		UserInfo userInfo = new UserInfo();
		String authType = userAgent.getAuthType();
		UpdateWrapper<UserInfo> updateWrapper = new UpdateWrapper<UserInfo>();
		String userAccount = userAgent.getUserAccount();
		boolean result = false;
		if (!isBinded(merchantId, mobile)) { // 没有绑定的处理逻辑：更新手机号
			userInfo.setMobile(mobile);
			updateWrapper.setEntity(new UserInfo().setUserAccount(userAccount).setMerchantId(merchantId));
			result = userInfoService.update(userInfo, updateWrapper);
		} else { // 已经绑定了的处理逻辑：合并账户信息，更新凭证信息
			UserInfo sourceUserInfo = userInfoService
					.getOne(new QueryWrapper<UserInfo>(new UserInfo().setMerchantId(merchantId).setUserAccount(userAccount)), true);
			UserInfo targetUserInfo = userInfoService
					.getOne(new QueryWrapper<UserInfo>(new UserInfo().setMerchantId(merchantId).setMobile(mobile)), true);
			
			combineCredential(sourceUserInfo.getUserAccount(), targetUserInfo.getUserAccount(), authType, merchantId);
			// 将源信息设置为不可用
			sourceUserInfo.deleteById();
			userAgent.setUserAccount(targetUserInfo.getUserAccount());
			result = true;
		}
		if (!result) {
			throw new RuntimeException("通过手机号绑定账号执行失败");
		}
		// 更新userAgent
		return this.updateUserAgent(userAgent);
	}

	/**
	 * 判断是否已经存在并且绑定
	 * 
	 * @param agentId
	 * @param mobile
	 * @return
	 */
	private boolean isBinded(String merchantId, String mobile) {
		QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<UserInfo>();
		queryWrapper.setEntity(new UserInfo().setMerchantId(merchantId).setMobile(mobile).setDeleted(0));
		UserInfo userInfo = userInfoService.getOne(queryWrapper, true);
		return userInfo == null ? false : true;
	}

	public static Integer getAgeByBirth(String birthDay) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// 注意月份是MM

			Date birthDaydate = simpleDateFormat.parse(birthDay);
			int age = 0;
			Calendar cal = Calendar.getInstance();
			if (cal.before(birthDay)) { // 出生日期晚于当前时间，无法计算
				throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
			}
			int yearNow = cal.get(Calendar.YEAR); // 当前年份
			int monthNow = cal.get(Calendar.MONTH); // 当前月份
			int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); // 当前日期
			cal.setTime(birthDaydate);
			int yearBirth = cal.get(Calendar.YEAR);
			int monthBirth = cal.get(Calendar.MONTH);
			int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
			age = yearNow - yearBirth; // 计算整岁数
			if (monthNow <= monthBirth) {
				if (monthNow == monthBirth) {
					if (dayOfMonthNow < dayOfMonthBirth)
						age--;// 当前日期在生日之前，年龄减一
				} else {
					age--;// 当前月份在生日之前，年龄减一
				}
				return age;
			}
		} catch (ParseException e) {
			log.error("用户年龄计算出现异常:{}", e.getMessage());
		}
		return null;
	}

	private void combineCredential(String sourceAccount, String targetAccount, String authType,String merchantId) {
		boolean updated = false;
		if (AuthTypeEnum.getEnumByType(authType) == AuthTypeEnum.AUTH_ALI
				|| AuthTypeEnum.getEnumByType(authType) == AuthTypeEnum.AUTH_WX) {
			
			updated = thirdCredentialService.update(new ThirdCredential().setUserAccount(targetAccount),
					new UpdateWrapper<ThirdCredential>(
							new ThirdCredential().setAuthType(authType).setMerchantId(merchantId).setUserAccount(sourceAccount)
							)
					);
			
		} else if (AuthTypeEnum.getEnumByType(authType) == AuthTypeEnum.AUTH_PASSWORD) {

		}
		
		if(!updated) {
			throw new RuntimeException("合并用户凭证信息失败");
		}
		
	}

}
