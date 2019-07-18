/**
 * Ping++ Server SDK
 * 说明：
 * 以下代码只是为了方便商户测试而提供的样例代码，商户可根据自己网站需求按照技术文档编写, 并非一定要使用该代码。
 * 接入支付流程参考开发者中心：https://www.pingxx.com/docs/server/charge ，文档可筛选后端语言和接入渠道。
 * 该代码仅供学习和研究 Ping++ SDK 使用，仅供参考。
 */
package com.masjpay.example;

import java.util.*;

import com.jpaypp.exception.APIConnectionException;
import com.jpaypp.exception.APIException;
import com.jpaypp.exception.AuthenticationException;
import com.jpaypp.exception.ChannelException;
import com.jpaypp.exception.InvalidRequestException;
import com.jpaypp.exception.RateLimitException;
import com.jpaypp.exception.MasJPayException;
import com.jpaypp.model.Charge;

/**
 * Charge 对象相关示例
 *
 * 该实例程序演示了如何从 Ping++ 服务器获得 charge ，查询 charge。
 *
 * 开发者需要填写 apiKey 和 appId ，
 *
 * apiKey 有 TestKey 和 LiveKey 两种。
 *
 * TestKey 模式下不会产生真实的交易。
 */
public class ChargeExample {

    ChargeExample(){

    }

    public static void runDemos() {

        ChargeExample chargeExample = new ChargeExample();
        System.out.println("------- 创建 charge -------");
        Charge charge = chargeExample.create();
//         System.out.println("------- 查询 charge -------");
//          chargeExample.retrieve("ch_4b7e112fa0ca7c942392ee61");
//        System.out.println("------- 撤销 charge -------");
//        chargeExample.reverse("ch_4b7e112fa0ca7c942392ee61");
    }

    /**
     * 创建 Charge
     *
     * 创建 Charge 用户需要组装一个 map 对象作为参数传递给 Charge.create();
     * @return Charge
     */
    public Charge create() {
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


        try {
            //发起交易请求
            charge = Charge.create(chargeMap);
            // 传到客户端请先转成字符串 .toString(), 调该方法，会自动转成正确的 JSON 字符串
            String chargeString = charge.toString();
            System.out.println(chargeString);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (ChannelException e) {
            e.printStackTrace();
        } catch (RateLimitException e) {
            e.printStackTrace();
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        }
        return charge;
    }

    /**
     * 查询 Charge
     * 该接口根据 charge Id 查询对应的 charge 。
     * @param id
     */
    public Charge retrieve(String id) {
        Charge charge = null;
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            charge = Charge.retrieve(id, params);
            System.out.println(charge.getStatus());
        } catch (MasJPayException e) {
            e.printStackTrace();
        }

        return charge;
    }

    /**
     * 撤销 Charge
     *
     * @param id
     */
    public Charge reverse(String id) {
        Charge charge = null;
        try {
            charge = Charge.reverse(id);
            System.out.println(charge);
        } catch (MasJPayException e) {
            e.printStackTrace();
        }

        return charge;
    }
}
