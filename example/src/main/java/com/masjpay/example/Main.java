package com.masjpay.example;

import com.jpaypp.MasJPay;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by Afon on 16/4/26.
 */
public class Main {

    /**
     * MasJPay 管理平台对应的 API Key，api_key 获取方式：登录 [Dashboard](https://jpay.weidun.biz/agent)->点击管理平台右上角API开发
     */
    private final static String apiKey = "k1qn32lw9ppdfmxslch1b8rh9pvrcdfw";

    /**
     * MasJPay 管理平台对应的应用 ID，app_id 获取方式：登录 [Dashboard](https://jpay.weidun.biz/agent)->点击管理平台右上角API开发
     */
    private final static String clientId = "10002";

    protected static String projectDir;

    public static void main(String[] args) throws Exception {
        projectDir = System.getProperty("user.dir") + "/example/";

        MasJPay.DEBUG = true;

        MasJPay.apiMode = "sandbox";

        // 设置 API Key
        MasJPay.apiKey = apiKey;

        MasJPay.clientId = clientId;


        // Charge 示例
        ChargeExample.runDemos( );
//
//        // Refund 示例
//        RefundExample.runDemos();
//
//        // RedEnvelope 示例
//        RedEnvelopeExample.runDemos(clientId);
//
//        // Transfer 示例
//        TransferExample.runDemos(clientId);
//
//        // Event 示例
//        EventExample.runDemos();
//
//        // Webhooks 验证示例
//        WebhooksVerifyExample.runDemos();
//
//        // 微信公众号 openid 相关示例
//        WxPubOAuthExample.runDemos(clientId);
//
//        // 身份证银行卡信息认证接口
//        // 请使用 live key 调用该接口
//        // IdentificationExample.runDemos(appId);
//
//        // 批量付款示例
//        BatchTransferExample.runDemos(appId);
//
//        // 报关
//        // 请使用 live key 调用该接口
//        CustomsExample.runDemos(appId);
    }

    private static SecureRandom random = new SecureRandom();

    public static String randomString(int length) {
        String str = new BigInteger(130, random).toString(32);
        return str.substring(0, length);
    }

    public static int currentTimeSeconds() {
        return (int)(System.currentTimeMillis() / 1000);
    }
}
