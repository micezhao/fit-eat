# Recipe-client-api doc


## 通用约定
 * 请求协议：http
 * 请求风格：restfulApi
 * 响应结果:【success：200】 其他均为失败 
---

## 通用接口
* 查看业务列表
> request uri： /bizType
> request method: post
> request header: x-auth-token

 **request**
  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  
**response param：list**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |业务类型编码|bizTypeId|String|Y||
  |业务类型|typeCode|String|Y||
  |业务类型名|typeName|String|Y||
---
* 文件上传接口
* 获取多媒体信息接口

## 用户登录
- - -
#### 获取用户授权码 
> request uri：前端调用地址
> request method: get
> requset header: null

  **request params**
  |  字段  | 参数名 | 参数类型 | 是否必填 | 备注 |
  |:----|:----|:----|:----|:----|
  |登录渠道|loginType|String|Y|wx,alipay|
  |应用id|appId|String|Y||
  |应用秘钥|appSecretKey|String|Y||
 **response params**
  |  字段  | 参数名 | 参数类型 | 是否必填 | 备注 |
  |:----|:----|:----|:----|:----|
  |用户唯一标识|openId|String|Y||
---

#### 用户通过第三方id登录
> request uri： /loginByThird/
> request method: post
> requset header: null 

 **request params**
  |  字段  | 参数名 | 参数类型 | 是否必填 | 备注 |
  |:----|:----|:----|:----|:----|
  |登录渠道|loginType|String|Y|wx,alipay|
  |用户授权码|authCode|String|Y||

 **response params**
  |  字段  | 参数名 | 参数类型 | 是否必填 | 备注 |
  |:----|:----|:----|:----|:----|
  |回话凭证|x-auth-token|String|Y|访问其他请求时，需要将该字段存入在 请求的header中|
---

#### 获取验证码
> request uri： 外部地址
> request method: get
> requset header: x-auth-token 

 **request params**
  |  字段  | 参数名 | 参数类型 | 是否必填 | 备注 |
  |:----|:----|:----|:----|:----|
  |手机号|mobile|String|Y||

 **response params**
  |  字段  | 参数名 | 参数类型 | 是否必填 | 备注 |
  |:----|:----|:----|:----|:----|
  |验证码|validCode|String|Y|外部返回|
  |过期时间|expireTime|datetime|Y||
---

#### 校验验证码
> request uri： 外部地址
> request method: get
> requset header: x-auth-token 

 **request params**
  |  字段  | 参数名 | 参数类型 | 是否必填 | 备注 |
  |:----|:----|:----|:----|:----|
  |手机号|mobile|String|Y||
  |验证码|validCode|String|Y||

 **response params**
  |  字段  | 参数名 | 参数类型 | 是否必填 | 备注 |
  |:----|:----|:----|:----|:----|
  |校验结果|result|boolean|Y|如果校验不通过，不准提交表单|
---


#### 用户完善个人信息 
> request uri： user/fill
> request method: post  
> request header: x-auth-token

 **request params**

  |  字段  | 参数名 | 参数类型 | 是否必填 | 备注 |
  |:----|:----|:----|:----|:----|
  |手机号|mobile|String|Y|
  |性别|gender|String|Y|m:男 / f:女|
  |生日|birthday|String|N|yyyy-MM-dd|

 **response params**
  |  字段  | 参数名 | 参数类型 | 是否必填 | 备注 |
  |:----|:----|:----|:----|:----|
  |操作结果|result|boolean|Y|httpstatus.code = 200|
---

## 菜谱功能

#### 查看菜谱分类
> request uri： /category/recipe
> request method: get  
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
 **response params**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |分类编号|categoryId|String|Y|
  |分类名称|categoryName|String|Y|
  |排序|sort|String|
---
#### 根据菜谱分类获得列表
> request uri： /recipe/categoryId
> request method: get  
> request header: x-auth-token

 **request params**
  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |当前页码|pageNum|String|N|默认10条|
  |每页条数|pageSize|String|N|默认第一页|
  |总条数|pageTotal|String|
  |排序|sort|String|N|排序条件,默认创建时间|
  |排序方式|orderBy|String|desc / asc|

 **response params**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |菜谱信息|items|List|Y|{recipeId:菜谱编号/recipeName:菜谱名称 / summary: 概述/hot: 热度/imageUrl:展示图 }|
  |当前页码|pageNum|String|N|默认第一页|
  |每页条数|pageSize|String|N|默认显示10条|
  |总条数|pageTotal|String|
--- 
### 查看菜谱详情
> request uri：/recipe/${recipeId}/detail
> request method: get
> request header: x-auth-token

 **request params**
  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |菜谱编号|recipeId|String|Y|

 **response params**

  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |菜谱编号|recipeId|String|Y|
  |菜谱名称|recipeName|String|Y|
  |分类编号|categoryId|String|Y||
  |分类名称|categoryName|String|Y||
  |概述|summary|String|N|
  |热度|hot|String|N|
  |关联多媒体列表|media|List|N||
  |配料|ingredient|String|N|json|
  |难度等级|level|String|
  |准备时间|prepareTime|String|
  |制作时间|cookTime|String|
  |详情介绍|introduction|String|
  |营养成分|nutritional|String||json|
--- 
#### 搜索食材

#### 分享菜谱 
> request uri： /recipe/public
> request method: post
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |菜谱名称|recipeName|String|Y|
  |分类编号|categoryId|String|Y||
  |概述|desc|String|N|
  |关联多媒体列表|media|List|N||
  |配料|ingredient|List|N|ingredient|
  |难度等级|level|String|
  |准备时间|prepareTime|String|
  |制作时间|cookTime|String|
  |详情介绍|introduction|String|

 **response params**

  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |菜谱编号|recipeId|String|Y|
  |发布状态|state|String|Y|verifying:审核中 \ verified \ unverified|
---

## 商品功能
#### 查看商品分类
> request uri： /category/goods
> request method: get  
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
 **response params**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |分类编号|categoryId|String|Y|
  |分类名称|categoryName|String|Y|
  |排序|sort|String|
---
#### 根据商品分类获得列表
> request uri： /goods/categoryId
> request method: get  
> request header: x-auth-token

 **request params**
  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |当前页码|pageNum|String|N|默认10条|
  |每页条数|pageSize|String|N|默认第一页|
  |总条数|pageTotal|String|
  |排序|sort|String|N|排序条件,默认创建时间|
  |排序方式|orderBy|String|desc / asc|

 **response params**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |商品信息|items|List|Y||
  |当前页码|pageNum|String|N|默认第一页|
  |每页条数|pageSize|String|N|默认显示10条|
  |总条数|pageTotal|String|
--- 
#### 查看商品详情
> request uri： /goods/${spuId}/detail
> request method: get
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|

 **response params**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |商品编号|spuId|String|Y|
  |商品编号|spuName|String|Y|
  |商品分类|categoryId|String|Y||
  |分类名称|categoryName|String|Y||
  |热度|hot|String|N|
  |关联多媒体列表|media|List|N||
  |详情介绍|introduction|String|
  |储存方式说明|storage|String|N|
  |产地来源|origin|String|Y|
  |使用注意事项|preparation|String|Y|
  |营养成分|nutritional|String||json|

#### 查看商品对应的sku
> request uri： /spu/${spuId}/sku
> request method: get
> request header: x-auth-token

 **request params**
  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|

 **response params**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |商品编号|spuId|String|Y|
  |货品编号|skuId|String|Y|
  |规格|spec|String|Y|
  |价格|price|String|Y|
---

## 评价功能
### 查看评价
> request uri： /comment/${relativeId}
> request method: get
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |评价类型|search|Array|Y|

 **response params**

  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |评价编号|commentId|String|Y|
  |相关物编号|relativeId|String|Y|
  |相关物业务类型|relativeType|String|Y|
  |发布人编号|publisherId|String|Y|
  |发布人昵称|publisherNickName|String|Y|
  |发布人头像|publisherHeaderImage|String|Y|
  |发布时间|publishTime|String|Y||
  |评价内容|content|String|N|
  |评价等级|stars|String|N|
  |多媒体列表|media|List|N||
--- 

#### 评价
> request uri： /comment/
> request method: post
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |相关物编号|relativeId|String|Y|
  |相关物业务类型|relativeType|String|Y|
  |评价等级|stars|String|Y|
  |评价内容|content|String|Y|
  |多媒体列表|media|List|Y|

 **response params**

  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |操作结果|result|boolean|Y|httpstatuscode = 200|
---

## 购买功能
#### 查看购物车
> request uri： /cart
> request method: get
> request header: x-auth-token

 **request params**
  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
 **response params**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |购物车编码|cartId|String|Y|
  |购物车内项目数|cartItemCount|String|Y|
  |商品内容|cartItem|List|Y|{spuId: \ skuId: \ name: \ num: \ amount: } |
  |当前页码|pageNum|String|Y|
  |显示条数|pageSize|String|Y|
  |总页数|pageTotal|String|Y|
---
#### 加入购物车
> request uri： cart/add
> request method: post
> request header: x-auth-token

 **request params**
  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |商品内容|item|Array|Y|{skuId: \ num: } |
 **response params**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |操作结果|result|boolean|Y|httpstatus.code = 200|
---
#### 修改购物车中项目的数量
> request uri： /cart/${itemId}/modify
> request method: put
> request header: x-auth-token

 **request params**
  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |当前数量|num|String|Y||
 **response params**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |当前分项总价|amount|String|Y||
---
#### 从购物车中移除项目
> request uri： /cart/${itemId}/remove
> request method: delete
> request header: x-auth-token

 **request params**
  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
 **response params**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |操作结果|result|boolean|Y|httpstatus.code = 200|
--- 
#### 清空购物车
> request uri： /cart/${cartId}/clean
> request method: delete
> request header: x-auth-token

 **request params**
  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
 **response params**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |操作结果|result|boolean|Y|httpstatus.code = 200|

#### 查询收货地址信息[同用户收货地址接口]

#### 查看配送方式
> request uri： /delivery/type
> request method: get
> request header: x-auth-token

 **request params**
  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
 **response params**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |配送方式|deliveryType|String|Y||
  |配送费|deliveryFee|String|N||
  |排序码|sort|String|Y||
---

#### 查看可配送日期【deliveryType = standar 】
> request uri：/delivery/date
> request method: get
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |配送方式|deliveryType|String||standar|
 **response params**

  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |可选配送日期|option|List|Y|String|
---
#### 查看可配送时间【deliveryType = sonic 】
> request uri： /delivery/time
> request method: get
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |配送方式|deliveryType|String||sonic|
 **response params**

  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |可选配送时间|option|List|Y|String|
---
#### 查看可自提地点【deliveryType = self 】
> request uri： /delivery/pickUpAddress
> request method: get
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |配送方式|deliveryType|String||self|
 **response params**

  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |可选配送时间|option|List|Y|json|
---
~~#### 提交配送信息~~
> request uri： 
> request method: post
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |收货地址编码|userAddressId|String|Y||
  |配送方式|deliveryType|String|Y|standard/sonic/self|
  |送货时间|deliveryTime|String|N|deliveryType=self 时不填写|
---
#### 获取优惠券信息
> request uri：/order/coupon/
> request method: get
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  
**response params >> list**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |可用优惠券编码|couponId|String|Y||
  |可用优惠券类型|couponType|String|Y||
  |优惠金额|discount|String|Y|单位:分|
---
#### 提交购物车并生成订单
> request uri： /order/
> request method: post
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |结算内容|packItemArr|jsonArr|Y|[{"goodsId":"12345"，"num"："4"}]|
  |配送方式|deliveryType|String|Y|standard/sonic/self|
  |收货地址方式|userAddressId|String|N|deliveryType ！= self|
  |提/收货人姓名|consignee|String|Y|
  |提/收货人联系方式|consigneeContact|Y|
  |提/收货人地址|consigneeAddress|Y|
  |优惠券编码|couponId|String|Y|
  |配送费|deliveryFee|Y|
  |提/收货时间|deliveryTime|String|N|deliveryType=self 时不填写|
  
**response**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |订单编号|orderId|String|Y||
  |订单概况|summary|String|Y|订单内容简介|
  |提/收货人姓名|consignee|String|Y|
  |提/收货人联系方式|consigneeContact|Y|
  |提/收货人地址|consigneeAddress|Y|
  |优惠金额|discount|String|Y|
  |运费|deliveryFee|String|Y|
  |订单金额|amount|String|Y|单位:分|
  |订单状态|state|String|Y|state:unpay|
  |订单过期时间|expire|datetime|y|
---
#### 取消订单：已支付未发货时可使用
> request uri： /order/${orderId}/cancel
> request method: put
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  
**response**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |操作结果|result|String|Y|httpstatuscode = 200|

#### 支付订单
> request uri：/order/${orderId}/pay
> request method: put
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |订单编号|orderId|String|Y||
  
**response**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |支付参数|param|String|Y|字段待定|
---

## 个人中心

#### 优惠券
> request uri： /coupon
> request method: get
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  
**response param：list**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |优惠券编码|couponId|String|Y||
  |优惠券使用金额|discount|String|Y|
  |限定使用范围|limitScope|String|Y|
  |优惠券状态|state|String|Y|available | forbidden|
  |使用时间|period|String|Y|
  |使用说明|introduction|String|Y|
---

#### 我的订单
* 查看列表
> request uri：/order/${orderType}
> request method: get
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  
**response param：list**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |订单概况|items|List|Y|{orderId:/state:/xxx:}|
  |当前页码|pageNum|String|Y|默认10条|
  |每页条数|pageSize|String|Y|默认第一页|
  |总条数|pageTotal|String|Y||
---
* 查看订单详情
> request uri：/order/detail/${orderId}
> request method: get
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  
**response**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |订单编号|orderId|String|Y|
  |订单状态|orderState|String|Y|
  |商品列表|items|List|Y|{id: /spuId：/ skuId: / spuName: /imageUrl:/ price:/num:/ amount: / isRefund: 是否有退款}|
  |配送状态|deliveryState|String|Y|
  |配送信息|deliveryInfo|String|N|
  |配送费|deliveryFee|String|N||
  |优惠金额|discount|String|N|
  |订单费用总计|totalAmount|String|Y|
---
* 申请退款：用户收货后，对有质量问题的商品进行退款申请操作【退款需要后台审核】
> request uri： /order/${orderId}/${itemId}/refund
> request method: put
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  
**response param：list**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |操作结果|result|String|Y|httpstatuscode = 200|
---

#### 常用地址 
* 查询常用地址
> request uri：/address 
> request method: get
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  
**response param：list：最多5条记录**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |地址编码|addressId|String|Y||
  |联系人|contactName|String|Y|
  |联系电话|contactPhone|String|Y|
  |国家|nation|String|Y|默认中国|
  |省|province|String|Y||
  |城|city|String|Y||
  |具体地址|detail|String|Y||
  |默认|default|Boolean|Y|
---
* 删除地址
> request uri：/address/${addressId}
> request method: delete
> request header: x-auth-token

 **request params**
  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |地址编码|addressId|String|Y||
  
**response param：list：最多5条记录**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |操作结果|result|String|Y|httpstatuscode = 200|
---
* 修改地址
> request uri： /address/${addressId}
> request method: put
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |联系人|contactName|String|N|
  |联系电话|contactPhone|String|N|
  |国家|nation|String|N|默认中国|
  |省|province|String|N||
  |城|city|String|N||
  |具体地址|detail|String|N||
  |默认|default|Boolean|Y|
  
**response**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |操作结果|result|String|Y|httpstatuscode = 200|
---
* 新增地址
> request uri： /address
> request method: put
> request header: x-auth-token

 **request params**
  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |联系人|contactName|String|Y|
  |联系电话|contactPhone|String|Y|
  |国家|nation|String|Y|默认中国|
  |省|province|String|Y||
  |城|city|String|Y||
  |区县|district|String|Y|页面上字段分开，如果外部接口提供数据就从接口获取，反之就让用户填写|
  |街道|street|String|Y|页面上字段分开，如果外部接口提供数据就从接口获取，反之就让用户填写|
  |具体地址|detail|String|Y|页面上字段分开，用户填写|
  |默认|default|Boolean|Y|
  
**response param：list：最多5条记录**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |操作结果|result|String|Y|httpstatuscode = 200|
---

#### 我的收藏
* 加入到收藏
> request uri： /favorite
> request method: post
> request header: x-auth-token

 **request params**
  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |业务类型|bizType|String|Y|
  |相关物编号|relativeId|String|Y|
  
**response param**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |操作结果|result|String|Y|httpstatuscode = 200|
---
* 从收藏夹中移除
> request uri： /favorite/${favoriteId}
> request method: post
> request header: x-auth-token

 **request**
  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |业务类型|bizType|String|Y|
  |收藏内容编号|favoriteId|String|Y|
  
**response**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |操作结果|result|String|Y|httpstatuscode = 200|
---
* 根据业务类型查询收藏记录
> request uri： /favorite/${typeCode}
> request method: get
> request header: x-auth-token

 **request param:list**
  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  
**response param：list**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |收藏的内容|items|List|Y|{favoriteId: / typeCode: / xxx:}|
  |当前页码|pageNum|String|Y|默认10条|
  |每页条数|pageSize|String|Y|默认第一页|
  |总条数|pageTotal|String|Y|
---

#### 我的分享
