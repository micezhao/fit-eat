# kater-api doc


## 通用约定
 * 请求协议：http
 * 请求风格：restfulApi
 * 响应结果: httpstatuscode = 200 
 * 响应失败: httpstatuscode = 500 => {"errMsg":"错误原因"}
---

## 用户登录
* 使用第三方账号登录
> 接口说明：用于用户通过业务系统客户端请求登录，如果用户是首次访问系统，系统会自动完成注册<br>
> uri： /login/logout<br>
> method: get<br>
> header: 无<br>

 **request**
  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |机构编号|agentId|String|Y|客户端在系统平台中所属的编号|
  |登录方式|loginType|String|Y|可选项：ali_lite,wx_lite,app|
  |第三方授权码|thirdAuthId|String|Y||
  |认证方式|authType|String|Y|可选项:wx,alipay|
  
**response**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |客户端访问凭证|X-AUTH-TOKEN|String|Y|客户端访问凭证，其他请求需要其放入header中,格式为：X-AUTH-TOKEN:398e1b55-aa8d-4c98-bbbc-81991043db90|
  |用户是否绑定标识|hasBinded|boolean|Y|客户端通过该字段引导跳转到绑定手机号页面|
---
* 使用手机号和短信验证码登录
---
* 使用账号密码登录
---

* 用户登出
> 接口说明：用于用户通过业务系统客户端请求登录，如果用户是首次访问系统，系统会自动完成注册<br>
> uri： /login/thirdPart/{agentId}/{loginType}/{thirdAuthId}/{authType}<br>
> method: get<br>
> header: X-AUTH-TOKEN
  
**response**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |||||httpstatuscode = 200 |

---

## 用户信息相关接口

* 获取验证码
> 接口说明：通过手机号对用户信息进行绑定，绑定成功后，用户信息将会合并<br>
> uri： /user/smscodeAttain/{mobile}<br>
> method: get<br>
> header: X-AUTH-TOKEN

**request**
  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |接收短信的验证码|mobile|String|Y||
  
**response**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |验证码|validateCode|String|Y|用于校验接口|
  |过期时间|expire|date|Y|用于页面倒数计时|
---

* 校验验证码
> 接口说明：通过手机号对用户信息进行绑定，绑定成功后，用户信息将会合并<br>
> uri： /user/smscodeVaildate/{mobile}/{validateCode}<br>
> method: get<br>
> header: X-AUTH-TOKEN<br>

**request**
  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |接收短信的手机号|mobile|String|Y|接收短信的手机号|
  |验证码|validateCode|String|Y|
  
**response**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |验证结果|validateResult|boolean|Y||
---

* 用户通过手机号绑定
> 接口说明：通过手机号对用户信息进行绑定，绑定成功后，用户信息将会合并<br>
> uri： /user/mobileBind/{mobile}<br>
> method: put<br>
> header: X-AUTH-TOKEN

 **request**
  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |手机号|mobile|String|Y|绑定手机号|
  
**response**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |||||httpstatuscode = 200 |
---

* 用户(更新)完善个人信息
> 接口说明：用户完善个人信息<br>
> uri： /user/fillup<br>
> method: put<br>
> header: X-AUTH-TOKEN

 **request**
  |字段|参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |性别|gender|String|N|可选项：male(男),female(女)|
  |真实姓名|realname|String|N||
  |出生年月|birthday|String|N|填写后，系统会自动计算用户年龄|
  |昵称|nickname|String|N||
  |头像|headimgUrl|String|N||
  
**response**
  | 字段 |参数名|参数类型|是否必填|备注|
  |:----|:----|:----|:----|:----|
  |用户编号|id|number|Y||
  |用户账号|userAccount|String|Y||
  |所属机构号|agentId|String|Y|当前用户所在机构号|
  |真实姓名|realname|String|N||
  |出生年月|birthday|String|N||
  |年龄|age|number|N|
  |昵称|nickname|String|N||
  |头像|headimgUrl|String|N||
  |手机号|mobile|String|N|
  |用户积分|score|number|N|
---




