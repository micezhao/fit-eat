package com.fa.kater.customer.service.impl;

import com.fa.kater.customer.pojo.UserInfo;
import com.fa.kater.customer.mapper.UserInfoMapper;
import com.fa.kater.customer.service.UserInfoService;
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
