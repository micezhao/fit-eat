package com.fa.kater.biz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.f.a.kobe.view.UserAgent;
import com.f.a.kobe.view.UserAgent.UserAgentBuilder;
import com.fa.kater.entity.requset.UserInfoRequest;
import com.fa.kater.enums.AuthTypeEnum;
import com.fa.kater.enums.LoginTypeEnum;
import com.fa.kater.pojo.ThirdCredential;
import com.fa.kater.pojo.UserInfo;
import com.fa.kater.service.impl.ThirdCredentialServiceImpl;
import com.fa.kater.service.impl.UserInfoServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserInfoBiz {

	@Autowired
	private UserInfoServiceImpl userInfoService;
	
	@Autowired
	private ThirdCredentialServiceImpl thirdCredentialService;
	
	/**
	 * 用户代理对象生成器，返回登录用户视图
	 * @param userAccount
	 * @return
	 */
	public  UserAgent generator(String userAccount,String loginType) {
		UserInfo userInfo = new UserInfo();
		userInfo.setUserAccount(userAccount);
		userInfo.setDeleted(0);
		userInfo = userInfo.selectOne(new QueryWrapper<UserInfo>(userInfo));
		
		UserAgentBuilder builder = UserAgent.builder();
		builder.hasbinded(false);
		if(StringUtils.isNotBlank(userInfo.getAgentId())) {
			builder.agentId(userInfo.getAgentId());
		}
		if(StringUtils.isNotBlank(userInfo.getUserAccount())) {
			builder.userAccount(userInfo.getUserAccount());
		}
		if(StringUtils.isNotBlank(userInfo.getRealname())) {
			builder.realname(userInfo.getRealname());
		}
		if(StringUtils.isNotBlank(userInfo.getBirthday())) {
			builder.birthday(userInfo.getBirthday());
		}
		if(userInfo.getAge()!=null && userInfo.getAge() != 0) {
			builder.age(userInfo.getAge());
		}
		if(StringUtils.isNotBlank(userInfo.getGender())) {
			builder.gender(userInfo.getGender());
		}
		if(StringUtils.isNotBlank(userInfo.getNickname())) {
			builder.nickname(userInfo.getNickname());
		}
		if(userInfo.getScore()!=null) {
			builder.score(userInfo.getScore());
		}
		if(StringUtils.isNotBlank( userInfo.getMobile())) {
			builder.mobile(userInfo.getMobile());
			builder.hasbinded(true);
		}
		builder.loginType(loginType);
		ThirdCredential credential = new ThirdCredential();
		credential.setUserAccount(userAccount);
		if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.WECHAT) {
			credential.setAuthType(AuthTypeEnum.AUTH_WX.type);
			credential = credential.selectOne(new QueryWrapper<ThirdCredential>(credential));
			builder.wxopenid(credential.getOpenId());
		}else if(LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.ALI_PAY) {
			credential.setAuthType(AuthTypeEnum.AUTH_ALI.type);
			builder.aliopenid(credential.getOpenId());
		}else {
			List<ThirdCredential> credentialList = credential.selectList(new QueryWrapper<ThirdCredential>(credential));
			for (ThirdCredential thirdCredential : credentialList) {
				if(thirdCredential.getAuthType() == AuthTypeEnum.AUTH_WX.type) {
					builder.aliopenid(thirdCredential.getOpenId());
				}else if(thirdCredential.getAuthType() == AuthTypeEnum.AUTH_ALI.type) {
					builder.wxopenid(thirdCredential.getOpenId());
				}
			}
		}
		return builder.build();
	}
	
	/**
	 * 完善个人信息,并执行通过手机号绑定的操作
	 * @return
	 */
	public UserAgent fillUserInfo(UserInfoRequest request,String loginType) {
		String agentId = request.getAgentId();
		String userAccount = request.getUserAccout();
		String mobile = request.getMobile();
		UserInfo userInfo = new UserInfo();
//		userInfo.setAgentId(request.getAgentId());
		userInfo.setGender(request.getGender());
//		userInfo.setUserAccount(request.getUserAccout());
		userInfo.setBirthday(request.getBirthday());
		int age = getAgeByBirth(request.getBirthday());
		userInfo.setAge(age);
		userInfo.setMobile(mobile);
		boolean need = needBind(request.getAgentId(), request.getMobile());
		String finalUserAccount = "";
		if(need) {
			// TODO 查询手机号对应的账户信息，根据创建时间排序，删除排在后面的用户信息，并更新用户凭证
			UserInfo queryWapper = new UserInfo();
			queryWapper.setAgentId(agentId).setMobile(mobile);
			List<UserInfo> list = userInfo.selectList(new QueryWrapper<UserInfo>(queryWapper).orderByAsc("gmt_create"));
			UserInfo targetBindItem = list.get(0);
			UserInfo prepareCopeItem = list.get(list.size());
			prepareCopeItem.setDeleted(1);
			userInfoService.update(prepareCopeItem, new UpdateWrapper<UserInfo>().set("agent_id", agentId).set("user_account", userAccount));
			ThirdCredential thirdCredential = new ThirdCredential();
			thirdCredential.setUserAccount(targetBindItem.getUserAccount()).setAgentId(agentId);
			thirdCredentialService.update(thirdCredential,  new UpdateWrapper<ThirdCredential>().set("agent_id", agentId).set("user_account", userAccount));
			finalUserAccount = targetBindItem.getUserAccount();
		}else {
			// TODO 更新当前用户账号对应的用户个人信息
			UserInfo updateWapper = new UserInfo();
			updateWapper.setAgentId(agentId).setUserAccount(userAccount);
			userInfoService.update(userInfo, new UpdateWrapper<UserInfo>(updateWapper));
			finalUserAccount = userAccount;
		}
		return this.generator(finalUserAccount, loginType);
	}
	
	
	
	/**
	 * 通过手机号检查是否需要绑定手机号
	 * @param mobile
	 * @return
	 */
	public boolean needBind(String agentId,String mobile) {
		UserInfo userInfo = new UserInfo();
		userInfo.setAgentId(agentId);
		userInfo.setMobile(mobile);
		userInfo.setDeleted(0);
		boolean needBind = userInfo.selectOne(new QueryWrapper<UserInfo>().setEntity(userInfo)) == null ? false:true;
		return needBind;
	}
	
	/**
	 * 手机号短信验证码校验
	 * @param modile
	 * @param smscode
	 * @return
	 */
	public boolean smscodeValidate(String modile,String smscode) {
		return true;
	}
	
	
	
	public static Integer getAgeByBirth(String birthDay)  {
		try {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
			
			Date birthDaydate = simpleDateFormat.parse(birthDay);
        int age = 0;
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) { //出生日期晚于当前时间，无法计算
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);  //当前年份
        int monthNow = cal.get(Calendar.MONTH);  //当前月份
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); //当前日期
        cal.setTime(birthDaydate);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        age = yearNow - yearBirth;   //计算整岁数
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;//当前日期在生日之前，年龄减一
            } else {
                age--;//当前月份在生日之前，年龄减一
            }
            return age;
        }
		}catch (ParseException e) {
			log.error("用户年龄计算出现异常:{}",e.getMessage());
		}
		return null;
    }
 
}
