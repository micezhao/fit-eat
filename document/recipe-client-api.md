# Recipe-client-api doc

## 通用约定
 * 请求协议：http
 * 请求风格：restfulApi
 * 响应结果:【success：200】 其他均为失败 

## 用户登录
- - -
#### 获取用户授权码 
> request uri：
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
> request uri： 
> request method: get
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
> request uri： 
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
> request uri： 
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
> request uri： 
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
  |完善结果|result|boolean|Y||
---

## 菜谱功能

#### 查看菜谱分类
> request uri： 
> request method: get  
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |业务类型|bizType|String|Y|业务类型|

 **response params**

  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |分类编号|categoryId|String|Y|
  |分类名称|categoryName|String|Y|
  |排序|sort|String|
---
#### 根据菜谱分类获得列表
> request uri： 
> request method: get  
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |分类编号|categoryId|String|Y||
  |当前页码|pageNum|String|N|默认10条|
  |每页条数|pageSize|String|N|默认第一页|
  |排序|sort|String|N|排序条件,默认创建时间|
  |排序方式|orderBy|String|desc / asc|

 **response params**

  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |菜谱信息|info|List|Y|{recipeId:菜谱编号/recipeName:菜谱名称/ categoryId: 分类编号 / summary: 概述/hot: 热度/imageUrl:展示图   }|
  |当前页码|pageNum|String|N|默认第一页|
  |每页条数|pageSize|String|N|默认显示10条|
  |总条数|pageTotal|String|
--- 
### 查看菜谱详情
> request uri： 
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

#### 新建菜谱
> request uri： 
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
---
## 商品功能
#### 查看商品详情
> request uri： 
> request method: get
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |商品编号|spuId|String|Y|

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
> request uri： 
> request method: get
> request header: x-auth-token

 **request params**
  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |商品编号|spuId|String|Y|

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
> request uri： 
> request method: get
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |相关物编号|relativeId|String|Y|recipeId/spuId |
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
> request uri： 
> request method: get
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
  |操作结果|result|boolean|Y|
---


## 商品功能
#### 查看商品分类
> request uri： 
> request method: get  
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |业务类型|bizType|String|Y|业务类型|

 **response params**

  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |分类编号|categoryId|String|Y|
  |分类名称|categoryName|String|Y|
  |排序|sort|String|
---
#### 根据获取商品列表
> request uri： 
> request method: get  
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |分类编号|categoryId|String|Y||
  |当前页码|pageNum|String|N|默认10条|
  |每页条数|pageSize|String|N|默认第一页|
  |排序|sort|String|N|排序条件,默认创建时间|
  |排序方式|orderBy|String|desc / asc|

 **response params**

  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |商品信息|info|List|Y|{spuId:商品编号/spuName:商品名称/ categoryId: 分类编号 / summary: 概述/hot: 热度/imageUrl:展示图}|
  |当前页码|pageNum|String|N|默认第一页|
  |每页条数|pageSize|String|N|默认10条|
  |总条数|pageTotal|String|
---


## 购买功能
#### 查看购物车
> request uri： 
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
> request uri： 
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
> request uri： 
> request method: put
> request header: x-auth-token

 **request params**
  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |商品Id|skuId|String|Y||
  |当前数量|num|String|Y||
 **response params**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |当前分项总价|amount|String|Y||
---
#### 从购物车中移除项目
> request uri： 
> request method: delete
> request header: x-auth-token

 **request params**
  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |商品Id|skuId|String|Y||
 **response params**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |操作结果|result|boolean|Y|httpstatus.code = 200|
--- 
#### 清空购物车
> request uri： 
> request method: delete
> request header: x-auth-token

 **request params**
  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |购物车编号|cartId|String|Y||
 **response params**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |操作结果|result|boolean|Y|httpstatus.code = 200|

#### 查询收货地址信息
> request uri： 
> request method: get
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
 **response params**

  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |地址列表|userAddressList|List|Y|userAddress|
---
#### 查看配送方式
> request uri： 
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
> request uri： 
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
> request uri： 
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
> request uri： 
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
#### 提交配送信息
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
> request uri： 
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
> request uri： 
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
#### 取消订单
> request uri： 
> request method: put
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
   |订单编号|orderId|String|Y||
  
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
  |订单状态|state|String|Y|state:cancel|
  |订单过期时间|expire|datetime|y|
---
#### 支付订单
> request uri： 
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
* 