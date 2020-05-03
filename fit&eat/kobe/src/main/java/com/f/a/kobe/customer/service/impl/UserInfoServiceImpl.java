package com.f.a.kobe.customer.service.impl;

import com.f.a.kobe.customer.pojo.UserInfo;
import com.f.a.kobe.customer.dao.UserInfoMapper;
import com.f.a.kobe.customer.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author micezhao
 * @since 2020-04-30
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

}
