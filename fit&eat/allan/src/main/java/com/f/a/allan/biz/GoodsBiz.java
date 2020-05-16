package com.f.a.allan.biz;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.f.a.allan.entity.constants.FieldConstants;
import com.f.a.allan.entity.pojo.GoodsItem;
import com.f.a.allan.enums.GoodsItemCategoryEnum;
import com.f.a.allan.enums.GoodsStatusEnum;

import lombok.extern.slf4j.Slf4j;

/**
 * 商品业务服务类
 * 
 * @author micezhao
 *
 */
@Service
@Slf4j
public class GoodsBiz {

	private static final String FOLDER = "stock:";

	@Autowired
	private RedisTemplate<String,Object> redisTemplate; 
	
	@Autowired
	ThreadPoolTaskExecutor taskExecutor;

	@Autowired
	MongoTemplate mongoTemplate;

	/**
	 * 新增商品,此时商品未上架,不能售出
	 * 
	 * @param goodsItem
	 * @return
	 */
	public GoodsItem insert(GoodsItem goodsItem) {
		if (goodsItem.getStock() == null || goodsItem.getStock() <= 0) {
			throw new RuntimeException("请设置当前商品的初始库存，初始库存 必须大于0");
		}
		goodsItem.setGoodsStatus(GoodsStatusEnum.UN_SOLD.getCode());
		goodsItem.setCdt(LocalDateTime.now());
		return mongoTemplate.insert(goodsItem);
	}

	/**
	 * 上架商品，将库存同步到redis中，商品状态变为可售
	 * 
	 * @param goodsItem
	 * @return
	 */
	public GoodsItem putGoodsItemOn(String goodsId) {
		Query query = new Query();
		query.addCriteria(new Criteria(FieldConstants.GOODS_ID).is(goodsId));
		GoodsItem item = mongoTemplate.findOne(query, GoodsItem.class);
		String merchantId = item.getMerchantId();
		String goodsItemId = item.getGoodsId();
		int currentStock = item.getStock();
		// 同步库存
		redisTemplate.opsForHash().put(FOLDER + merchantId, goodsItemId, currentStock);
		log.debug(">>>>向redis中同步库存,商品编号:{},当前库存:{}", goodsItemId, currentStock);
		// 更新商品状态
		Update update = new Update();
		update.set(FieldConstants.GOODS_STATUS, GoodsStatusEnum.ON_SALE.getCode());
		update.set(FieldConstants.MDT, LocalDateTime.now());
		GoodsItem updatedRecord = mongoTemplate.findAndModify(query, update,
				FindAndModifyOptions.options().returnNew(true), GoodsItem.class);
		return updatedRecord;
	}

	/**
	 * 上架商品，将库存同步到redis中，商品状态变为可售
	 * 
	 * @param goodsItem
	 * @return
	 */
	public GoodsItem pullGoodsItemOff(String goodsId) {
		Query query = new Query();
		query.addCriteria(new Criteria(FieldConstants.GOODS_ID).is(goodsId));
		GoodsItem item = mongoTemplate.findOne(query, GoodsItem.class);
		String merchantId = item.getMerchantId();
		String goodsItemId = item.getGoodsId();
		// 从redis中移除库存
		redisTemplate.opsForHash().delete(FOLDER + merchantId, goodsItemId);
		log.debug(">>>>从redis中移除库存,商品编号:{},当前库存:{}", goodsItemId);
		// 更新商品状态 -> 停售
		Update update = new Update();
		update.set(FieldConstants.GOODS_STATUS, GoodsStatusEnum.SUSPENSION.getCode());
		update.set(FieldConstants.MDT, LocalDateTime.now());
		GoodsItem updatedRecord = mongoTemplate.findAndModify(query, update,
				FindAndModifyOptions.options().returnNew(true), GoodsItem.class);
		return updatedRecord;
	}

	/**
	 * 设置为缺货
	 * 
	 * @param goodsId
	 * @return
	 */
	private GoodsItem setLack(String goodsId) {
		Query query = new Query();
		query.addCriteria(new Criteria(FieldConstants.GOODS_ID).is(goodsId));
		Update update = new Update();
		update.set(FieldConstants.STOCK, 0);
		update.set(FieldConstants.GOODS_STATUS, GoodsStatusEnum.LACK.getCode());
		update.set(FieldConstants.MDT, LocalDateTime.now());
		GoodsItem updatedRecord = mongoTemplate.findAndModify(query, update,
				FindAndModifyOptions.options().returnNew(true), GoodsItem.class);
		// 如果redis中也存在key 同步将其删除掉
		boolean redisHasKey = redisTemplate.opsForHash().hasKey(FOLDER + updatedRecord.getMerchantId(),
				updatedRecord.getGoodsId());
		if (redisHasKey) {
			redisTemplate.opsForHash().delete(FOLDER + updatedRecord.getMerchantId(), updatedRecord.getGoodsId());
		}
		log.debug("当前商品：{}已被设置为缺货状态",goodsId);
		return updatedRecord;
	}

	/**
	 * 商品补货
	 * 
	 * @param goodsId
	 * @param replenishment 增补的数量
	 * @return
	 */
	public GoodsItem replenish(String goodsId, int replenishment) {
		Query query = new Query();
		query.addCriteria(new Criteria(FieldConstants.GOODS_ID).is(goodsId));
		GoodsItem item = mongoTemplate.findOne(query, GoodsItem.class);
		if (GoodsStatusEnum.getEnumByCode(item.getGoodsStatus()) == GoodsStatusEnum.UN_SOLD) {
			throw new RuntimeException("当前商品未上架,无需进行补货操作");
		}
		// 先更新 mongodb中的库存
		Update update = new Update();
		update.set(FieldConstants.STOCK, item.getStock() + replenishment);
		update.set(FieldConstants.MDT, LocalDateTime.now());
		GoodsItem updatedRecord = mongoTemplate.findAndModify(query, update,
				FindAndModifyOptions.options().returnNew(true), GoodsItem.class);
		// 再更新 REDIS
		redisTemplate.opsForHash().increment(FOLDER + item.getMerchantId(), item.getGoodsId(), replenishment);
		return updatedRecord;
	}

	/**
	 * 商品扣减
	 * 
	 * @param goodsId
	 * @param deduction 扣减量
	 * @return
	 */
	public boolean deduct(String goodsId, int deduction) {
		Query query = new Query();
		query.addCriteria(new Criteria(FieldConstants.GOODS_ID).is(goodsId));
		GoodsItem item = mongoTemplate.findOne(query, GoodsItem.class);
		if(GoodsStatusEnum.getEnumByCode(item.getGoodsStatus()) != GoodsStatusEnum.ON_SALE ) {
			log.error("商品:{} 未处于可售状态——>当前状态为:{}", goodsId,item.getGoodsStatus());
			return false; // 如果库存小于扣减量，就直接返回扣减失败
		}
		int remainStock;
		// 从redis中获取 当前商品的库存 与 扣减量进行比较
		boolean redisHasKey = redisTemplate.opsForHash().hasKey(FOLDER + item.getMerchantId(), item.getGoodsId());
		if (redisHasKey) {
			remainStock = (int) redisTemplate.opsForHash().get(FOLDER + item.getMerchantId(), item.getGoodsId());
		} else { // 如果redis中不存在当前的商品的库存信息 就从 mongo 中获取
			remainStock = item.getStock().intValue();
		}
		if (remainStock < deduction) {
			log.error("商品:{}库存不足,库存扣减失败，当前库存{},扣减库存{}", goodsId,remainStock,deduction);
			return false; // 如果库存小于扣减量，就直接返回扣减失败
		}
		// 暂时 通过另起线程 处理这个业务  -> 后期从线程池中获取线程 TODO 比较在不同的请求量级下，对资源开销的情况
		Callable<Boolean> executeResule = new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return deductGoodsStockById(goodsId, remainStock - deduction);
			}
		};
		try {
			return executeResule.call().booleanValue();
		} catch (Exception e) {
			return false;
		}
		// taskExecutor.submit(new DeductGoodsStockByAsync(goodsId,remainStock - deduction,mongoTemplate,redisTemplate));
	}

	private boolean deductGoodsStockById(String goodsId,int remainStock) {
		try {
			Query query = new Query();
			query.addCriteria(new Criteria(FieldConstants.GOODS_ID).is(goodsId));
			GoodsItem item = mongoTemplate.findOne(query, GoodsItem.class);
			// 如果扣减后的剩余量是0 那么就像 当前商品设置为缺货
			if (remainStock == 0) {
				log.debug("当前商品:{}库存扣减后为:{},准备进行将当前商品设置为缺货状态的操作",goodsId,remainStock);
				setLack(goodsId);
				return true;
			}
			redisTemplate.opsForHash().put(FOLDER + item.getMerchantId(), item.getGoodsId(), remainStock);
			log.debug("[redis] 商品：{} 库存更新成功,当前库存{}",item.getGoodsId(), remainStock);
			Update update = new Update();
			update.set(FieldConstants.STOCK, remainStock);
			update.set(FieldConstants.MDT, LocalDateTime.now());
			GoodsItem updatedRecord=mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), GoodsItem.class);
			log.debug("[mongodb] 商品：{} 库存更新成功,当前库存{}",updatedRecord.getGoodsId(), updatedRecord.getStock());
			return true;
		} catch (Exception ex) {
			// TODO 后期需要改为：如果库存扣减出现异常，已让让订单生效，只是通过其他方式 重试扣减
			return false;
		}
	}
	
	public class DeductGoodsStockByAsync implements Callable<Boolean> {
		
		private String goodsId;
		
		private int remainStock;
		
		private MongoTemplate mongoTemplate;
		
		private RedisTemplate<String, Integer> redisTemplate;
		public String getGoodsId() {
			return goodsId;
		}
		public void setGoodsId(String goodsId) {
			this.goodsId = goodsId;
		}
		public int getRemainStock() {
			return remainStock;
		}
		public void setRemainStock(int remainStock) {
			this.remainStock = remainStock;
		}
		public MongoTemplate getMongoTemplate() {
			return mongoTemplate;
		}
		public void setMongoTemplate(MongoTemplate mongoTemplate) {
			this.mongoTemplate = mongoTemplate;
		}
		public RedisTemplate<String, Integer> getRedisTemplate() {
			return redisTemplate;
		}
		public void setRedisTemplate(RedisTemplate<String, Integer> redisTemplate) {
			this.redisTemplate = redisTemplate;
		}
		public DeductGoodsStockByAsync(String goodsId, int remainStock, MongoTemplate mongoTemplate,
				RedisTemplate<String, Integer> redisTemplate) {
			super();
			this.goodsId = goodsId;
			this.remainStock = remainStock;
			this.mongoTemplate = mongoTemplate;
			this.redisTemplate = redisTemplate;
		}
		@Override
		public Boolean call() throws Exception {
			try {
				Query query = new Query();
				query.addCriteria(new Criteria(FieldConstants.GOODS_ID).is(goodsId));
				GoodsItem item = mongoTemplate.findOne(query, GoodsItem.class);
				// 判断剩余量 是不是为 0
				if (remainStock == 0) { // 如果扣减后的剩余量是0 那么就像 当前商品设置为缺货
					setLack(goodsId);
					return true;
				}
				// 先更新 redis 同时 异步更新 mongodb
				redisTemplate.opsForHash().put(FOLDER + item.getMerchantId(), item.getGoodsId(), remainStock);
				Update update = new Update();
				update.set(FieldConstants.STOCK, remainStock);
				update.set(FieldConstants.MDT, LocalDateTime.now());
				mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), GoodsItem.class);
				return true;
			} catch (Exception ex) {
				// TODO 后期需要改为：如果库存扣减出现异常，已让让订单生效，只是通过其他方式 重试扣减
				return false;
			}
		}
	} 

}
