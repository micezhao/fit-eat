package com.f.a.allan.ctrl.admin.goods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.f.a.allan.biz.CommodityBiz;
import com.f.a.allan.biz.GoodsBiz;
import com.f.a.allan.ctrl.admin.BaseAdminCtrl;
import com.f.a.allan.entity.pojo.Commodity;
import com.f.a.allan.entity.pojo.Goods;
import com.f.a.allan.entity.pojo.GoodsItem;
import com.f.a.allan.entity.pojo.SkuConfig;
import com.f.a.allan.entity.pojo.SkuConfigItem;
import com.f.a.allan.entity.request.CommodityRequest;
import com.f.a.allan.entity.request.GoodsItemQueryRequest;
import com.f.a.allan.entity.request.GoodsItemQueryRequest.GoodsItemQueryRequestBuilder;
import com.f.a.allan.entity.request.GoodsItemRequest;
import com.f.a.allan.entity.response.ConfigView;
import com.f.a.allan.service.SkuConfigService;
import com.f.a.kobe.view.UserAgent;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = "admin-商品管理")
@RestController
@RequestMapping("/admin")
public class AdminGoodsCtrl extends BaseAdminCtrl {

	@Autowired
	private GoodsBiz goodBiz;
	
	@Autowired
	private CommodityBiz commodityBiz;
	
	@Autowired
	private SkuConfigService skuConfigService;
	
	@GetMapping
	@ApiOperation("商品查询")
	@ApiImplicitParams({
		@ApiImplicitParam(name="merchantId",value = "商户id",required = false),
		@ApiImplicitParam(name="spuId",value = "商品id",required = false),
		@ApiImplicitParam(name="name",value = "商品名称",required = false),
		@ApiImplicitParam(name="categories",value = "商品分类列表",required = false,allowableValues = "substantial,virtual",dataTypeClass = String[].class),
		@ApiImplicitParam(name="statuses",value = "商品状态列表",required = false,dataTypeClass = String[].class),
		@ApiImplicitParam(name="pageNum",value = "当前页",required = false),
		@ApiImplicitParam(name="pageSize",value = "显示条数",required = false),
	})
	public ResponseEntity<Object> listGoodsItem(
			@RequestParam(name = "merchantId",required = false)String merchantId,
			@RequestParam(name = "spuId",required = false)String spuId,
			@RequestParam(name = "name",required = false)String name,
			@RequestParam(name = "categories",required = false)String[] categories,
			@RequestParam(name = "statuses",required = false)String[] statuses,
			@RequestParam(name = "pageNum",required = false) int pageNum,
			@RequestParam(name = "pageSize",required = false) int pageSize
			,UserAgent userAgent) {
		GoodsItemQueryRequestBuilder builder = GoodsItemQueryRequest.builder();
		if(StringUtils.isNotBlank(merchantId)) {
			builder.merchantId(merchantId);
		}
		if(StringUtils.isNotBlank(spuId)) {
			builder.spuId(spuId);
		}
		if(StringUtils.isNotBlank(name)) {
			builder.spuName(name);
		}

		if(categories != null && categories.length > 0) {
			builder.categoryList(Arrays.asList(categories));
		}
		if(statuses != null && statuses.length > 0) {
			builder.statusList(Arrays.asList(statuses));
		}
		GoodsItemQueryRequest request=builder.build();
		if(pageNum > 0) {
			request.setPageNum(pageNum);
		}
		if(pageSize > 0) {
			request.setPageSize(pageSize);
		}
		Page<Commodity> page = commodityBiz.pageListCommodity(builder.build());
		return new ResponseEntity<Object>(page, HttpStatus.OK);
	}
	
	@PostMapping("/commodity")
	@ApiOperation("新增商品")
	public ResponseEntity<Object> addCommodity(@RequestBody CommodityRequest request,UserAgent userAgent){
		Commodity record= commodityBiz.insertOne(request);
		return new ResponseEntity<Object>(record, HttpStatus.OK);
	}
	
	
	@PutMapping("/skuConfig/{spuId}/{id}/{value}")
	@ApiOperation("修改商品指定配置项")
	@ApiImplicitParams({
		@ApiImplicitParam(name="spuId",value = "商品spuId",required = true),
		@ApiImplicitParam(name="id",value = "配置id",required = true),
		@ApiImplicitParam(name="value",value = "配置的值",required = true)
	})
	public ResponseEntity<Object> updateSkuConfigByIndex(@PathVariable("spuId") String spuId,
										@PathVariable("id") String id,
										@PathVariable("value") String configValue,
										UserAgent userAgent){
		// TODO
		return new ResponseEntity<Object>(null, HttpStatus.OK);
	}
	
	@DeleteMapping("/spu/skuConfig/{id}")
	@ApiOperation("删除商品指定配置项")
	@ApiImplicitParams({
		@ApiImplicitParam(name="id",value = "配置id",required = true)
	})
	public ResponseEntity<Object> deleteSkuConfigByIndex(@PathVariable("id") String id,
										UserAgent userAgent){
		skuConfigService.deleteById(id);
 		return new ResponseEntity<Object>(null, HttpStatus.OK);
	}
	
	@PostMapping("/spu/skuConfig")
	@ApiOperation("向指定商品新增配置项")
	public ResponseEntity<Object> addSkuConfig(@RequestBody CommodityRequest request,UserAgent userAgent){
		 String spuId = request.getSpuId();
		 List<Map<String,String[]>> list = request.getSkuConfigs();
		 List<SkuConfig> skus = new ArrayList<SkuConfig>();
		 // 前端确保不会提交重复的配置项，后端只负责受理并处理数据即可
		 for (int i = 0; i < list.size(); i++) { // 循环前台提交集合
			Map<String,String[]> map= list.get(i);
			for(Entry<String, String[]> element : map.entrySet()){	 // 遍历元素中的key
				String skuConfigName= element.getKey();
				String suffix =String.valueOf((int)((Math.random()*9+1)*1000));
				String code = spuId.substring(spuId.length() - 4, spuId.length())+"_"+suffix;
				String[] skuConfigValues = element.getValue();
				for (String value : skuConfigValues) { // 根据 key 来逐一生成 config
					SkuConfig sku = SkuConfig.builder().code(code).spuId(spuId).name(skuConfigName).value(value).build();
					skus.add(sku);
				}
			  }
		 }
		 List<SkuConfig> record= skuConfigService.addSkuConfig(skus);
		 return new ResponseEntity<Object>(record, HttpStatus.OK);
	}
	
	@GetMapping("/spu/skuConfig/{spuId}")
	@ApiOperation("获取商品配置项")
	@ApiImplicitParam(name="spuId",value = "spuId编号",required = true)
	public ResponseEntity<Object> getConfigNameByCode(@PathVariable("spuId")String spuId){
		List<SkuConfig> list= skuConfigService.listBySpuId(spuId);
		List<ConfigView> resultList = new ArrayList<ConfigView>();
		if(list.isEmpty()) {
			return new ResponseEntity<Object>(resultList, HttpStatus.OK);
		}
		Set<String> set = new HashSet<String>();
		for (SkuConfig skuConfig : list) {
			set.add(skuConfig.getCode());
		}
		ConfigView configView = null;
		for (String code : set) {
			List<SkuConfig> ls = skuConfigService.listByCode(code);
			configView = new ConfigView();
			configView.setId(code);
			List<String> valueList = new ArrayList<String>();
			for (SkuConfig i : ls) {
				i.getValue();
				valueList.add(i.getValue());
			}
			String[] values = valueList.toArray(new String[valueList.size()]);
			configView.setValues(values);
			resultList.add(configView);
		}
		return new ResponseEntity<Object>(resultList, HttpStatus.OK);
	}
	
	@GetMapping("/spu/skuConfig/{spuId}/name/{code}")
	@ApiOperation("获取商品配置项的值")
	@ApiImplicitParams({
		@ApiImplicitParam(name="spuId",value = "spuId编号",required = true),
		@ApiImplicitParam(name="code",value = "配置项编码",required = true)
	})
	public ResponseEntity<Object> getConfigValueByCode(@PathVariable("spuId")String spuId,@PathVariable("code")String code){
		List<SkuConfig> ls = skuConfigService.listByCode(code);
		List<ConfigView> resultList = new ArrayList<ConfigView>();
		if(ls.isEmpty()) {
			new ResponseEntity<Object>(resultList, HttpStatus.OK);
		}
		for (SkuConfig i : ls) {
			ConfigView configView = new ConfigView();
			configView.setId(i.getConfigId());
			String [] values = new String[]{i.getValue()};
			configView.setValues(values);
			resultList.add(configView);
		}
		return new ResponseEntity<Object>(resultList, HttpStatus.OK);
	}
	
	
	
	@GetMapping("/skuConfig/{spuId}")
	@ApiOperation("获取商品配置")
	public ResponseEntity<Object> getConfigBySpuId(@PathVariable("spuId")String spuId){
		List<SkuConfig> list= skuConfigService.listBySpuId(spuId);
		return new ResponseEntity<Object>(list, HttpStatus.OK);
	}
	
	
	@PostMapping("/sku")
	@ApiOperation("新增货品")
	public ResponseEntity<Object> addGoodsItem(@RequestBody GoodsItemRequest request,UserAgent userAgent) {
		Goods item = goodBiz.insertSku(request);
		return new ResponseEntity<Object>(item, HttpStatus.OK);
	}

	@PutMapping("/sku/{id}/puton")
	@ApiOperation("sku上架")
	@ApiImplicitParam(name = "id", value = "skuId", required = true)
	public ResponseEntity<Object> puton(@PathVariable("id") String goodsId,UserAgent userAgent) {
		Goods item = goodBiz.putGoodsOn(goodsId);
		return new ResponseEntity<Object>(item, HttpStatus.OK);
	}

	@PutMapping("/sku/{id}/pulloff")
	@ApiImplicitParam(name = "id", value = "skuId", required = true)
	@ApiOperation("sku下架")
	public ResponseEntity<Object> pulloff(@PathVariable("id") String goodsId,UserAgent userAgent) {
		Goods item = goodBiz.pullGoodsOff(goodsId);
		return new ResponseEntity<Object>(item, HttpStatus.OK);
	}

	@PutMapping("/sku/{id}/{replenishment}/replenish")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "商品id", required = true),
		@ApiImplicitParam(name = "replenishment", value = "增补量", required = true)
	})
	@ApiOperation("sku补货")
	public ResponseEntity<Object> replenish(@PathVariable("id") String goodsId,@PathVariable("replenishment") int replenishment,UserAgent userAgent) {
		Goods item = goodBiz.replenish(goodsId, replenishment);
		return new ResponseEntity<Object>(item, HttpStatus.OK);
	}

	public static void main(String[] args) {
		int a= (int)((Math.random()*9+1)*1000);
		System.out.println(a);
	}
	
}
