package com.f.a.allan.service.impl;

import com.f.a.allan.dao.GoodsMapper;
import com.f.a.allan.entity.pojo.Goods;
import com.f.a.allan.service.GoodsService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDateTime;
import java.util.Calendar;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author micezhao
 * @since 2019-12-09
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
	
    @Override
    public boolean save(Goods entity) {
    	entity.setCdt(LocalDateTime.now());
        return retBool(baseMapper.insert(entity));
    }
    
    @Override
    public boolean updateById(Goods entity) {
    	entity.setMdt(LocalDateTime.now());
        return retBool(baseMapper.updateById(entity));
    }

    @Override
    public boolean update(Goods entity, Wrapper<Goods> updateWrapper) {
    	entity.setMdt(LocalDateTime.now());
        return retBool(baseMapper.update(entity, updateWrapper));
    }
}
