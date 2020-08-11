package com.fa.kater.auth.service.impl;

import com.fa.kater.auth.pojo.AccessLog;
import com.fa.kater.auth.mapper.AccessLogMapper;
import com.fa.kater.auth.service.AccessLogService;
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
public class AccessLogServiceImpl extends ServiceImpl<AccessLogMapper, AccessLog> implements AccessLogService {

}
