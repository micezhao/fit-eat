# Recipe-client-api doc

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
### 根据菜谱分类获得列表
> request uri： 
> request method: get  
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |分类编号|categoryId|String|Y||
  |当前页码|pageNum|String|N|默认10条|
  |当前页码|pageNum|String|N|默认第一页|
  |排序|sort|String|N|排序条件,默认创建时间|
  |排序方式|orderBy|String|desc / asc|

 **response params**

  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |菜谱编号|recipeId|String|Y|
  |菜谱名称|recipeName|String|Y|
  |分类编号|categoryId|String|Y||
  |概述|desc|String|N|
  |热度|hot|String|N|
  |展示图|imageUrl|String|N|相对路径|
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
  |概述|desc|String|N|
  |热度|hot|String|N|
  |关联多媒体列表|media|List|N||
  |配料|ingredient|String|N|json|
  |难度等级|level|String|
  |准备时间|prepareTime|String|
  |制作时间|cookTime|String|
  |详情介绍|introduction|String|
  |营养成分|nutritional|String||json|
--- 

### 查看菜谱评价
> request uri： 
> request method: get
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |菜谱编号|recipeId|String|Y|
  |评价类型|search|Array|Y|

 **response params**

  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |评价编号|commentId|String|Y|
  |发布人编号|publisherId|String|Y|
  |发布人昵称|publisherNickName|String|Y|
  |发布时间|publishTime|String|Y||
  |评价内容|content|String|N|
  |评价等级|stars|String|N|
  |多媒体列表|media|List|N||
  |评价点赞|like|String|N|json|
--- 

#### 评价菜谱
> request uri： 
> request method: get
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |菜谱编号|recipeId|String|Y|
  |评价等级|stars|String|Y|
  |评价内容|content|String|Y|
  |多媒体列表|media|List|Y|

 **response params**

  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |操作结果|result|boolean|Y|

***

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

***

## 商品功能
* 查看商品分类
* 根据获取商品列表
* 查看商品详情
* 评价商品
* 查看商品评价

## 购买功能
#### 查看购物车
#### 加入购物车
#### 修改购物车中项目的数量
#### 从购物车中移除项目
#### 清空购物车

#### 查询收货信息
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

#### 提交配送信息
> request uri： 
> request method: post
> request header: x-auth-token

 **request params**

  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |收货地址编码|userAddressId|String|||
  |配送标准|standard|String||standard/supersonic|
  |配送时间|
 **response params**

  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |地址列表|userAddressList|List|Y|userAddress|
#### 选择优惠券
#### 取消订单
#### 提交订单
#### 支付订单

## 个人中心
* 