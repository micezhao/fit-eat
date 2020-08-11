package com.fa.kater.auth.service.impl;

import com.fa.kater.auth.pojo.UserInfo;
import com.fa.kater.auth.mapper.UserInfoMapper;
import com.fa.kater.auth.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author micezhao
 * @since 2020-08-10
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

}
