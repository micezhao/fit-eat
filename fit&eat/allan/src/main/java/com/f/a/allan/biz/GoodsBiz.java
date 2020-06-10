package com.f.a.allan.biz;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.f.a.allan.entity.constants.FieldConstants;
import com.f.a.allan.entity.pojo.Commodity;
import com.f.a.allan.entity.pojo.Goods;
import com.f.a.allan.entity.pojo.GoodsItem;
import com.f.a.allan.entity.request.GoodsItemQueryRequest;
import com.f.a.allan.entity.request.GoodsItemRequest;
import com.f.a.allan.enums.GoodsStatusEnum;
import com.f.a.allan.service.GoodsItemService;
import com.f.a.allan.service.SkuConfigService;
import com.f.a.allan.utils.ObjectUtils;
import com.mongodb.client.result.DeleteResult;
import com.netflix.discovery.converters.Auto;

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

	@Autowired
	private CommodityBiz commodityBiz;
	
	// 剩余库存的key标识
	private static final String FOLDER = "remain_stock:";

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private GoodsItemService goodsItemService;
	
	@Autowired
	private SkuConfigService skuConfigService;

	@Autowired
	ThreadPoolTaskExecutor taskExecutor;

	@Autowired
	MongoTemplate mongoTemplate;
	
	/**
	 * @deprecated
	 * @param request
	 * @return
	 */
	public List<GoodsItem> listGoodsItem(GoodsItemQueryRequest request) {
		Query query = new Query();
		Criteria criteria = new Criteria();
		if (StringUtils.isNotBlank(request.getId())) {
			criteria.and(FieldConstants.GOODS_ID).is(request.getId());
		}
		if (StringUtils.isNotBlank(request.getSkuName())) {
			criteria.and(FieldConstants.GOODS_NAME)
					.regex(Pattern.compile("^.*" + request.getSkuName() + ".*$", Pattern.CASE_INSENSITIVE));
		}
		if (StringUtils.isNotBlank(request.getMerchantId())) {
			criteria.and(FieldConstants.MERCHANT_ID).is(request.getMerchantId());
		}

		if (request.getHasDiscount() != null) {
			if (request.getHasDiscount().booleanValue()) {
				criteria.andOperator(Criteria.where(FieldConstants.DISCOUNT_PRICE).exists(true),
						Criteria.where(FieldConstants.DISCOUNT_PRICE).ne(""));
			} else {
				criteria.orOperator(Criteria.where(FieldConstants.DISCOUNT_PRICE).exists(false),
						Criteria.where(FieldConstants.DISCOUNT_PRICE).is(""));
			}
		}

		if (StringUtils.isNotBlank(request.getCategory())) {
			criteria.and(FieldConstants.CATEGORY).is(request.getCategory());
		}
		if (StringUtils.isNotBlank(request.getStatus())) {
			criteria.and(FieldConstants.GOODS_STATUS).is(request.getStatus());
		}
		if (request.getCategoryList() != null && !request.getCategoryList().isEmpty()) {
			criteria.and(FieldConstants.CATEGORY).in(request.getCategoryList());
		}
		if (request.getStatusList() != null && !request.getStatusList().isEmpty()) {
			criteria.and(FieldConstants.CATEGORY).in(request.getStatusList());
		}
		if (StringUtils.isNotBlank(request.getPrice())) {
			criteria.and(FieldConstants.PRICE).is(Integer.parseInt(request.getPrice()));
		}
		if (StringUtils.isNotBlank(request.getPrice_min()) && StringUtils.isBlank(request.getPrice_max())) {
			criteria.and(FieldConstants.PRICE).gte(Integer.parseInt(request.getPrice_min()));
		}
		if (StringUtils.isBlank(request.getPrice_min()) && StringUtils.isNotBlank(request.getPrice_max())) {
			criteria.and(FieldConstants.PRICE).lte(Integer.parseInt(request.getPrice_max()));
		}
		if (StringUtils.isNotBlank(request.getPrice_min()) && StringUtils.isNotBlank(request.getPrice_max())) {
			criteria.andOperator(Criteria.where(FieldConstants.PRICE).gte(Integer.parseInt(request.getPrice_min())),
					Criteria.where(FieldConstants.PRICE).lte(Integer.parseInt(request.getPrice_max())));
		}

		query.addCriteria(criteria);
		return mongoTemplate.find(query, GoodsItem.class);
	}

	/**
	 * 新增商品,此时商品未上架,不能售出
	 * 
	 * @param goodsItem
	 * @return
	 */
	public Goods insertSku(GoodsItemRequest request) {
		Goods temp = new Goods();
		ObjectUtils.copy(temp, request);
		Commodity spu = commodityBiz.findById(temp.getSpuId());
		if (StringUtils.isEmpty(temp.getGoodsName())) {
			temp.setGoodsName(spu.getName());
		}
		if (temp.getStock() == null || temp.getStock() <= 0) {
			throw new RuntimeException("请设置当前商品的初始库存，初始库存 必须大于0");
		}
		temp.setMerchantId(spu.getMerchantId());
		temp.setGoodsStatus(GoodsStatusEnum.UN_SOLD.getCode()); 
		temp.setCdt(LocalDateTime.now());
		Goods sku = mongoTemplate.insert(temp);
		String skuId = sku.getGoodsId();
		commodityBiz.updateGoodsLink(spu.getSpuId(), skuId);
		return sku;
	}
	

	public List<Goods> listGoodsBySpuId(String spuId){
		return mongoTemplate.find(new Query().addCriteria(new Criteria(FieldConstants.GOODS_SPU_ID).is(spuId)), Goods.class);
	}
	
	public boolean removeGoodsById(String id) {
		Query query = new Query().addCriteria(new Criteria(FieldConstants.GOODS_ID).is(id));
		Goods goods = mongoTemplate.findOne(query, Goods.class);
		if(GoodsStatusEnum.getEnumByCode(goods.getGoodsStatus())  == GoodsStatusEnum.ON_SALE) {
			throw new RuntimeException("当前货品处于[在售]状态,无法删除，请先下架或停售该货品");
		}
		DeleteResult result = mongoTemplate.remove(query, Goods.class);
		return result.wasAcknowledged();
	}
	
	public boolean cleanGoodsBySpuId(String spuId) {
		List<Goods> list = listGoodsBySpuId(spuId);
		for (Goods goods : list) {
			removeGoodsById(goods.getGoodsId());
		}
		return true;
	}
	
	
	/**
	 * 上架商品，将库存同步到redis中，商品状态变为可售
	 * 
	 * @param goodsItem
	 * @return
	 */
	public Goods putGoodsOn(String goodsId) {
		GoodsItem item = goodsItemService.findBySkuId(goodsId);
		String merchantId = item.getMerchantId();
		String skuId = item.getGoodsId();
		int currentStock = item.getStock();
		// 同步库存
		redisTemplate.opsForHash().put(FOLDER + merchantId, skuId, currentStock);
		log.debug(">>>>向redis中同步库存,货品编号:{},当前库存:{}", skuId, currentStock);
		// 更新商品状态
		Query query = new Query();
		query.addCriteria(new Criteria(FieldConstants.GOODS_ID).is(goodsId));
		Update update = new Update();
		update.set(FieldConstants.GOODS_STATUS, GoodsStatusEnum.ON_SALE.getCode());
		update.set(FieldConstants.MDT, LocalDateTime.now());
		Goods updatedRecord = mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true),
				Goods.class);
		return updatedRecord;
	}

	/**
	 * 上架商品，将库存同步到redis中，商品状态变为可售
	 * 
	 * @param goodsItem
	 * @return
	 */
	public Goods pullGoodsOff(String skuId) {
		GoodsItem item = goodsItemService.findBySkuId(skuId);
		String merchantId = item.getMerchantId();
		String goodsItemId = item.getGoodsId();
		// 从redis中移除库存
		redisTemplate.opsForHash().delete(FOLDER + merchantId, goodsItemId);
		log.debug(">>>>从redis中移除库存,商品编号:{},当前库存:{}", goodsItemId);
		// 更新商品状态 -> 停售
		Query query = new Query();
		query.addCriteria(new Criteria(FieldConstants.GOODS_ID).is(skuId));
		Update update = new Update();
		update.set(FieldConstants.GOODS_STATUS, GoodsStatusEnum.SUSPENSION.getCode());
		update.set(FieldConstants.MDT, LocalDateTime.now());
		Goods updatedRecord = mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true),
				Goods.class);
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
		log.debug("当前商品：{}已被设置为缺货状态", goodsId);
		return updatedRecord;
	}

	/**
	 * 商品补货
	 * 
	 * @param goodsId
	 * @param replenishment 增补的数量
	 * @return
	 */
	public Goods replenish(String goodsId, int replenishment) {
		Query query = new Query();
		query.addCriteria(new Criteria(FieldConstants.GOODS_ID).is(goodsId));
		Goods item = mongoTemplate.findOne(query, Goods.class);
		if (GoodsStatusEnum.getEnumByCode(item.getGoodsStatus()) == GoodsStatusEnum.UN_SOLD) {
			throw new RuntimeException("当前商品未上架,无需进行补货操作");
		}
		// 先更新 mongodb中的库存
		Update update = new Update();
		update.set(FieldConstants.STOCK, item.getStock() + replenishment);
		if (GoodsStatusEnum.getEnumByCode(item.getGoodsStatus()) == GoodsStatusEnum.LACK) {
			update.set(FieldConstants.GOODS_STATUS, GoodsStatusEnum.ON_SALE.getCode());
		}
		update.set(FieldConstants.MDT, LocalDateTime.now());
		Goods updatedRecord = mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true),
				Goods.class);
		// 再更新 REDIS
		GoodsItem gi = goodsItemService.findBySkuId(goodsId);
		redisTemplate.opsForHash().increment(FOLDER + gi.getMerchantId(), item.getGoodsId(), replenishment);
		return updatedRecord;
	}

	/**
	 * 根据spuId 来更新 spu 以及其下的全部sku
	 * 
	 * @param spuId
	 * @param status
	 */
	@SuppressWarnings("unchecked")
	public void updateSpuStatusBySpuId(String spuId, String status) {
		Query query = new Query().addCriteria(new Criteria(FieldConstants.SPU_ID).is(spuId));
		Commodity commodity = mongoTemplate.findOne(query, Commodity.class);
		mongoTemplate.updateFirst(query, Update.update(FieldConstants.SPU_STATUS, status), Commodity.class); // 更新当前的状态
		String[] skuIdArr = commodity.getGoodsItemLink();
//		UpdateResult result=mongoTemplate.updateMulti(
//				new Query().addCriteria(new Criteria(FieldConstants.GOODS_ID).in(Arrays.asList(skuIdArr))),
//				Update.update(FieldConstants.GOODS_STATUS, status), Goods.class);
		List<Goods> goodsList= mongoTemplate.findAndModify(
									new Query().addCriteria(new Criteria(FieldConstants.GOODS_ID).in(Arrays.asList(skuIdArr))),
									Update.update(FieldConstants.GOODS_STATUS, status),
									FindAndModifyOptions.options().returnNew(true),
									List.class);
		if(goodsList.isEmpty()) {
			return;
		}
		String merchantId = commodity.getMerchantId();
		if(GoodsStatusEnum.getEnumByCode(status) == GoodsStatusEnum.ON_SALE) { // 将剩余库存量批量同步到redis中
			Map<String,Integer> map = new HashMap<String,Integer>(); 
			for (Goods goods : goodsList) {
				map.put(goods.getGoodsId(), goods.getStock());
			}
			redisTemplate.boundHashOps(FOLDER + merchantId).putAll(map);
		}else {
			List<String> keyList = new ArrayList<String>();
			for (Goods goods : goodsList) {
				keyList.add(goods.getGoodsId());
			}
			Object[] keys =  keyList.toArray(); // TODO 这样操作可以吗？
			redisTemplate.boundHashOps(FOLDER + merchantId).delete(keys);
		}
	}

	/**
	 * 根据商户号批量更新商品状态
	 * 
	 * @param merchantId  商户号
	 * @param goodsStatus 商品的目标状态
	 */
	public void updateSpuStatusByMerchant(String merchantId, String goodsStatus) {
		Query query = new Query();
		query.addCriteria(new Criteria(FieldConstants.MERCHANT_ID).is(merchantId));
		List<Commodity> commodityOfMerchant = mongoTemplate.find(query, Commodity.class);
		if (commodityOfMerchant.isEmpty()) {
			return;
		}

		log.debug("准备批量更新商户:{}的商品状态", merchantId);
		mongoTemplate.updateMulti(query,
				new Update().set(FieldConstants.GOODS_STATUS, goodsStatus).set(FieldConstants.MDT, LocalDateTime.now()),
				Commodity.class);

		new Runnable() {
			public void run() {
				log.debug("执行异步任务，更新当前商户{}下所有商品的状态为:{}", merchantId,
						GoodsStatusEnum.getEnumByCode(goodsStatus).getCode());
				for (Commodity commodity : commodityOfMerchant) {
					updateSpuStatusBySpuId(commodity.getSpuId(), goodsStatus); // 在更新商户状态时，同时更新当前商户中的商品状态
				}
			}
		}.run();

	}

	/**
	 * 根据商户编号，删除该商户的全部商品，同时清除库存占用
	 * 
	 * @param merchantId
	 */
	public void removeSpuSkuByMerchant(String merchantId) {
		log.debug("执行删除商户{}的全部SPU:", merchantId);
		List<Commodity> spus = mongoTemplate.findAllAndRemove(
				new Query().addCriteria(new Criteria(FieldConstants.MERCHANT_ID).is(merchantId)), Commodity.class);
		for (Commodity commodity : spus) {
			log.debug("执行删除商户{}中spu:{}包含的sku", merchantId,commodity.getSpuId());
			mongoTemplate.findAllAndRemove(
					new Query().addCriteria(new Criteria(FieldConstants.GOODS_ID).in(Arrays.asList(commodity.getGoodsItemLink())))
					,Goods.class);
		}
		redisTemplate.delete(FOLDER + merchantId); 
	}

	/**
	 * 通过redis占用商品库存
	 * 
	 * @param goodsId
	 * @param occupancy 占用量
	 * @return
	 */
	public boolean occupyByGoodsId(String goodsId, int occupancy) {
		log.info("商品{},占用库存{} ",goodsId,occupancy);
		Query query = new Query();
		query.addCriteria(new Criteria(FieldConstants.GOODS_ID).is(goodsId));
		GoodsItem item = goodsItemService.findBySkuId(goodsId);
		if (GoodsStatusEnum.getEnumByCode(item.getGoodsStatus()) != GoodsStatusEnum.ON_SALE) {
			log.error("商品:{} 未处于可售状态——>当前状态为:{}", goodsId, item.getGoodsStatus());
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
		if (remainStock < occupancy) {
			log.error("商品:{}剩余库存不足,占用库存失败，当前库存{},扣减库存{}", goodsId, remainStock, occupancy);
			return false; 
		}
		redisTemplate.opsForHash().put(FOLDER + item.getMerchantId(), item.getGoodsId(), remainStock-occupancy );
		log.debug("[redis] {}占用库存成功", item.getGoodsId());
		// 暂时 通过另起线程 处理这个业务 -> 后期从线程池中获取线程 TODO 比较在不同的请求量级下，对资源开销的情况
		// 2020-06-03 将扣减动作分离为两个步骤：1、生成订单包时占用商品库存【redis】/ 2、支付回调成功后，扣减商品的实际的库存量【mongodb】
//		new Callable<Boolean>() {
//			@Override
//			public Boolean call() throws Exception {
//				return deductGoodsStockById(goodsId, remainStock - deduction);
//			}
//		};
		// taskExecutor.submit(new DeductGoodsStockByAsync(goodsId,remainStock - deduction,mongoTemplate,redisTemplate));
		return true;
	}

	/**
	 * 偿还到剩余库存
	 * @param merchantId
	 * @param goodsId
	 * @param returnAmount
	 */
	public void returnBack (String merchantId,String goodsId,int returnAmount) {
		redisTemplate.opsForHash().increment(FOLDER + merchantId, goodsId, returnAmount);
	}
	
	/**
	 * 扣减实际库存
	 * @param goodsId
	 * @param remainStock
	 * @return
	 */
	public boolean deductGoodsStockById(String goodsId, int deduction) {
		try {
			Query query = new Query();
			query.addCriteria(new Criteria(FieldConstants.GOODS_ID).is(goodsId));
			Goods record = mongoTemplate.findOne(query, Goods.class);
			int remainStock = record.getStock() - deduction;
			Update update = new Update();
			update.set(FieldConstants.STOCK, remainStock);
			update.set(FieldConstants.MDT, LocalDateTime.now());
			Goods updatedRecord = mongoTemplate.findAndModify(query, update,
					FindAndModifyOptions.options().returnNew(true), Goods.class);
			if (remainStock == 0) {
				log.debug("当前商品:{}库存扣减后为:{},准备进行将当前商品设置为缺货状态的操作", goodsId, remainStock);
				setLack(goodsId);
			}
			log.debug("[mongodb] 商品：{} 库存更新成功,当前库存{}", updatedRecord.getGoodsId(), updatedRecord.getStock());
			return true;
		} catch (Exception ex) {
			// TODO 后期需要改为：如果库存扣减出现异常，已让让订单生效，只是通过其他方式 重试扣减
			return false;
		}
	}

	/**
	 * 扣减商品库存的异步任务
	 * 
	 * @author micezhao
	 *
	 */
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
//				GoodsItem item = mongoTemplate.findOne(query, GoodsItem.class);
				GoodsItem item = goodsItemService.findBySkuId(goodsId);
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
				mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true),
						Goods.class);
				return true;
			} catch (Exception ex) {
				// TODO 后期需要改为：如果库存扣减出现异常，已让让订单生效，只是通过其他方式 重试扣减
				return false;
			}
		}
	}

}
