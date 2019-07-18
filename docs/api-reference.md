# MasPay Java 使用文档


#### clientId 和 apiKey
SDK 需要 MasPay 提供的 clientId 和 apiKey 创建支付、查询数据。  
具体数值可以在 MasPay 商户平台的【API开发】中得到。  

#### 依赖
- gons-2.6.2
- commons-codec-1.10

#### 设置 支付环境、支付模式、 clientId、apiKey
``` java
MasJPay.DEBUG = true;  //

MasJPay.apiMode = "sandbox"; //沙盒环境 live 正式环境

MasJPay.clientId = clientId;

MasPay.apiKey = "YOUR-KEY";
```


#### API 使用
在创建 Charge 前，请设置 apiKey。 MasPay Java SDK 部分常用类的使用方法，详细使用方法开发者可阅读源码以及示例程序
- Charge
- Webhooks

#### Charge
##### 创建 Charge
``` java
create(Map<String, Object> params)
```
方法名：create  
类型：静态方法  
参数：Map  
返回：Charge  
示例：
``` java
Charge charge = null;
String channel = "901";
String orderNo = new Date().getTime() + Main.randomString(7);

//请求体
Map<String, Object> chargeMap = new HashMap<String, Object>();

//商品信息
Map<String, Object> product = new HashMap<String, Object>();
product.put("subject", "商品测试"); //商品名称
product.put("body", "商品测试"); //商品描述
product.put("amount", 0.01); //订单总金额, 人民币单位：元（如订单总金额为 1 元，此处请填 1
product.put("quantity", 1); //商品数量

chargeMap.put("product", product);

//wechat ///微信渠道901 试用
Map<String, Object> extra = new HashMap<String, Object>();
extra.put("mode", "link");  //微信渠道901 ，支付模式，jsapi 微信公众号、native 扫码支付、mweb H5 支付 ,link 返回支付链接跳转
extra.put("format", "json");
chargeMap.put("extra", extra);

chargeMap.put("out_order_no", orderNo);// 推荐使用 8-20 位，要求数字或字母，不允许其他字符
chargeMap.put("channel", channel);// 支付使用的第三方支付渠道取值，请参考 商户后台
chargeMap.put("client_ip", "127.0.0.1"); // 发起支付请求客户端的 IP 地址，格式为 IPV4，如: 127.0.0.1
chargeMap.put("metadata", "商品测试");
chargeMap.put("description", "description");
chargeMap.put("notify", "http://localhost/notify.html");
chargeMap.put("return", "http://localhost/callback.html");
```

##### 查询 Charge
``` java
retrieve(String id)
```
方法名：retrieve  
类型：静态方法  
参数：String 类型的 Charge ID  
返回：Charge  
示例：  
``` java
Charge charge = Charge.retrieve(CHARGE_ID);
```

##### 撤销 Charge
``` java
reverse(String id)
```
方法名：reverse
类型：静态方法
参数：String 类型的 Charge ID
返回：Charge
示例：
``` java
Charge charge = Charge.reverse(CHARGE_ID);
```