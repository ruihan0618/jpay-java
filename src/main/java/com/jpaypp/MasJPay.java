package com.jpaypp;

/**
 * MasJPay Base class
 */
public abstract class MasJPay {
    /**
     * MasJPay API BASE URL
     */
    public static final String LIVE_API_BASE = "https://api.jpay.live.weidun.biz";
    /**
     * MasJPay API BASE URL
     */
    public static final String SANDBOX_API_BASE = "https://api.jpay.sandbox.weidun.biz";
    /**
     * version
     */
    public static final String VERSION = "2.3.11";

    public static String AcceptLanguage = "zh-CN";

    public static volatile String apiMode = "live";
    /**
     * api key
     */
    public static volatile String apiKey;

    public static volatile String apiVersion;

    public static volatile String clientId;

    private static volatile String apiBase = LIVE_API_BASE;

    public static Boolean DEBUG = false;

    /**
     * get api url
     *
     * @return String  api url
     */
    public static String getApiBase() {

        return apiMode.equals("live") ? LIVE_API_BASE : SANDBOX_API_BASE;
    }
}
