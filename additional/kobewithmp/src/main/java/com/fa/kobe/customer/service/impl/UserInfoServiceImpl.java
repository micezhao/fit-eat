package com.fa.kobe.customer.service.impl;

import com.fa.kobe.customer.pojo.UserInfo;
import com.fa.kobe.customer.mapper.UserInfoMapper;
import com.fa.kobe.customer.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jxh
 * @since 2020-04-27
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

}
