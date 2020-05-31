package com.f.a.allan.ctrl.admin.goods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.f.a.allan.biz.CommodityBiz;
import com.f.a.allan.biz.GoodsBiz;
import com.f.a.allan.ctrl.admin.BaseAdminCtrl;
import com.f.a.allan.entity.pojo.Commodity;
import com.f.a.allan.entity.pojo.GoodsItem;
import com.f.a.allan.entity.pojo.SkuConfig;
import com.f.a.allan.entity.pojo.SkuConfigItem;
import com.f.a.allan.entity.request.CommodityRequest;
import com.f.a.allan.entity.request.GoodsItemQueryRequest;
import com.f.a.allan.entity.request.GoodsItemQueryRequest.GoodsItemQueryRequestBuilder;
import com.f.a.allan.entity.request.GoodsItemRequest;
import com.f.a.kobe.view.UserAgent;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = "admin-商品管理")
@RestController
@RequestMapping("/admin/goods")
public class AdminGoodsCtrl extends BaseAdminCtrl {

	@Autowired
	private GoodsBiz goodBiz;
	
	@Autowired
	private CommodityBiz commodityBiz;
	
	@GetMapping
	@ApiOperation("商品查询")
	@ApiImplicitParams({
		@ApiImplicitParam(name="merchantId",value = "商户id",required = false),
		@ApiImplicitParam(name="goodsId",value = "商品id",required = false),
		@ApiImplicitParam(name="goodsName",value = "商品名称",required = false),
		@ApiImplicitParam(name="categories",value = "商品分类列表",required = false,allowableValues = "substantial,virtual"),
		@ApiImplicitParam(name="goodsStatuses",value = "商品状态列表",required = false),
		@ApiImplicitParam(name="price_min",value = "商品价格最低值",required = false),
		@ApiImplicitParam(name="price_max",value = "商品价格最高值",required = false),
		@ApiImplicitParam(name="hasDiscount",value = "是否有优惠",required = false,dataType = "boolean" )
	})
	public ResponseEntity<Object> listGoodsItem(
			@RequestParam(name = "merchantId",required = false)String merchantId,
			@RequestParam(name = "goodsId",required = false)String goodsId,
			@RequestParam(name = "goodsName",required = false)String goodsName,
			@RequestParam(name = "categories",required = false)String categories,
			@RequestParam(name = "goodsStatuses",required = false)String goodsStatuses,
			@RequestParam(name = "price_min",required = false)String price_min,
			@RequestParam(name = "price_max",required = false)String price_max,
			@RequestParam(name = "hasDiscount",required = false)Boolean hasDiscount
			,UserAgent userAgent) {
		GoodsItemQueryRequestBuilder builder = GoodsItemQueryRequest.builder();
		if(StringUtils.isNotBlank(merchantId)) {
			builder.merchantId(merchantId);
		}
		if(StringUtils.isNotBlank(goodsId)) {
			builder.goodsId(goodsId);
		}
		if(StringUtils.isNotBlank(goodsName)) {
			builder.goodsName(goodsName);
		}
		if(StringUtils.isNotBlank(price_max)) {
			builder.price_max(price_max);
		}
		if(StringUtils.isNotBlank(price_min)) {
			builder.price_min(price_min);
		}
		if(hasDiscount != null) {
			builder.hasDiscount(hasDiscount.booleanValue());
		}
		if(StringUtils.isNotBlank(categories)) {
			builder.categoryList(parse2List(categories));
		}
		if(StringUtils.isNotBlank(goodsStatuses)) {
			builder.categoryList(parse2List(goodsStatuses));
		}
		List<GoodsItem> list = goodBiz.listGoodsItem(builder.build());
		return new ResponseEntity<Object>(list, HttpStatus.OK);
	}
	
	@PostMapping("/commodity")
	@ApiOperation("新增商品")
	public ResponseEntity<Object> addCommodity(@RequestBody CommodityRequest request,UserAgent userAgent){
		Commodity record= commodityBiz.insertOne(request);
		return new ResponseEntity<Object>(record, HttpStatus.OK);
	}
	
	
	@PutMapping("/commodity/skuConfig/{spuId}/{index}/{value}")
	@ApiOperation("修改商品指定配置项")
	@ApiImplicitParams({
		@ApiImplicitParam(name="spuId",value = "商品spuId",required = true),
//		@ApiImplicitParam(name="name",value = "配置项",required = true),
		@ApiImplicitParam(name="index",value = "配置的下标",required = true),
		@ApiImplicitParam(name="value",value = "配置的值",required = true)
	})
	public ResponseEntity<Object> updateSkuConfigByIndex(@PathVariable("spuId") String spuId,
										@PathVariable("index") String index,
//										@PathVariable("name") String name,
										@PathVariable("value") String configValue,
										UserAgent userAgent){
 		Commodity record= commodityBiz.updateSkuConfigByIndex(spuId, index, configValue);
		return new ResponseEntity<Object>(record, HttpStatus.OK);
	}
	
	@DeleteMapping("/commodity/skuConfig/{spuId}/{index}")
	@ApiOperation("删除商品指定配置项")
	@ApiImplicitParams({
		@ApiImplicitParam(name="spuId",value = "商品spuId",required = true),
		@ApiImplicitParam(name="index",value = "配置的下标",required = true)
	})
	public ResponseEntity<Object> deleteSkuConfigByIndex(@PathVariable("spuId") String spuId,
										@PathVariable("index") String index,
										UserAgent userAgent){
 		Commodity record= commodityBiz.deleteSkuConfigByIndex(spuId, index);
		return new ResponseEntity<Object>(record, HttpStatus.OK);
	}
	
	@PostMapping("/commodity/skuConfig")
	@ApiOperation("向指定商品新增配置项")
	public ResponseEntity<Object> addSkuConfig(@RequestBody CommodityRequest request,UserAgent userAgent){
		 String spuId = request.getSpuId();
		 List<Map<String,String[]>> list = request.getSkus();
		 List<SkuConfig> skus = new ArrayList<SkuConfig>();
		 // 前端确保不会提交重复的配置项，后端只负责受理并处理数据即可
		 for (int i = 0; i < list.size(); i++) { // 循环前台提交集合
			Map<String,String[]> map= list.get(i);
			for(Entry<String, String[]> element : map.entrySet()){	 // 遍历元素中的key
//				List<SkuConfigItem> configItemList = new ArrayList<SkuConfigItem>();
				List<Map<String,String>> itemMap = new ArrayList<Map<String,String>>();
				String skuConfigName= element.getKey();
				// 生成 下标前缀
				String index = spuId.subSequence(spuId.length()-4,spuId.length())+"_"+Math.round((Math.random()+1) * 1000);
				String[] skuConfigValues = element.getValue();
				int innerIndex = 0;
				for (String value : skuConfigValues) { // 根据 key 来逐一生成 config
					innerIndex ++;
					Map<String,String> item = new HashMap<String, String>();
					item.put(index+innerIndex, value);
//					SkuConfigItem item = SkuConfigItem.builder().index(index+innerIndex).configValue(value).build();
//					configItemList.add(item);
					itemMap.add(item);
				}
//				SkuConfig sku = SkuConfig.builder().name(skuConfigName).value(configItemList).build();
				SkuConfig sku = SkuConfig.builder().name(skuConfigName).value(itemMap).build();
				skus.add(sku);
			  }
		 }
		 Commodity record= commodityBiz.addSkuConfig(spuId, skus);
		 return new ResponseEntity<Object>(record, HttpStatus.OK);
	}
	
	
	@PostMapping("/goodsItem")
	@ApiOperation("新增货品")
	public ResponseEntity<Object> addGoodsItem(@RequestBody GoodsItemRequest request,UserAgent userAgent) {
		GoodsItem item = goodBiz.insert(request);
		return new ResponseEntity<Object>(item, HttpStatus.OK);
	}

	@PutMapping("{id}/puton")
	@ApiOperation("商品上架")
	@ApiImplicitParam(name = "id", value = "商品id", required = true)
	public ResponseEntity<Object> puton(@PathVariable("id") String goodsId,UserAgent userAgent) {
		GoodsItem item = goodBiz.putGoodsItemOn(goodsId);
		return new ResponseEntity<Object>(item, HttpStatus.OK);
	}

	@PutMapping("{id}/pulloff")
	@ApiImplicitParam(name = "id", value = "商品id", required = true)
	@ApiOperation("商品下架")
	public ResponseEntity<Object> pulloff(@PathVariable("id") String goodsId,UserAgent userAgent) {
		GoodsItem item = goodBiz.pullGoodsItemOff(goodsId);
		return new ResponseEntity<Object>(item, HttpStatus.OK);
	}

	@PutMapping("{id}/{replenishment}/replenish")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "商品id", required = true),
		@ApiImplicitParam(name = "replenishment", value = "增补量", required = true)
	})
	@ApiOperation("商品补货")
	public ResponseEntity<Object> replenish(@PathVariable("id") String goodsId,@PathVariable("replenishment") int replenishment,UserAgent userAgent) {
		GoodsItem item = goodBiz.replenish(goodsId, replenishment);
		return new ResponseEntity<Object>(item, HttpStatus.OK);
	}


}
