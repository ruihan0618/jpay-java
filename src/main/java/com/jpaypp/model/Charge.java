package com.jpaypp.model;

import com.jpaypp.exception.RateLimitException;
import com.jpaypp.net.APIResource;

import java.util.HashMap;
import java.util.Map;

public class Charge extends APIResource {

    String status;
    String message;
    Object data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 创建 charge
     *
     * @param params
     * @return Charge
     * @throws com.jpaypp.exception.AuthenticationException
     * @throws com.jpaypp.exception.InvalidRequestException
     * @throws com.jpaypp.exception.APIConnectionException
     * @throws com.jpaypp.exception.APIException
     * @throws com.jpaypp.exception.ChannelException
     * @throws com.jpaypp.exception.RateLimitException
     */
    public static Charge create(Map<String, Object> params) throws com.jpaypp.exception.AuthenticationException, com.jpaypp.exception.InvalidRequestException, com.jpaypp.exception.APIConnectionException, com.jpaypp.exception.APIException, com.jpaypp.exception.ChannelException, com.jpaypp.exception.RateLimitException {
        return create(null, params);
    }

    /**
     * 创建 charge
     *
     * @param params
     * @param apiKey MasJPay ApiKey
     * @return Charge
     * @throws com.jpaypp.exception.AuthenticationException
     * @throws com.jpaypp.exception.InvalidRequestException
     * @throws com.jpaypp.exception.APIConnectionException
     * @throws com.jpaypp.exception.APIException
     * @throws com.jpaypp.exception.ChannelException
     * @throws com.jpaypp.exception.RateLimitException
     */
    public static Charge create(String apiKey, Map<String, Object> params) throws com.jpaypp.exception.AuthenticationException, com.jpaypp.exception.InvalidRequestException, com.jpaypp.exception.APIConnectionException, com.jpaypp.exception.APIException, com.jpaypp.exception.ChannelException, com.jpaypp.exception.RateLimitException {
        return request(RequestMethod.POST, classURL(Charge.class), apiKey, params, Charge.class);
    }

    /**
     * 查询 charge
     *
     * @param id
     * @return Charge
     * @throws com.jpaypp.exception.AuthenticationException
     * @throws com.jpaypp.exception.InvalidRequestException
     * @throws com.jpaypp.exception.APIConnectionException
     * @throws com.jpaypp.exception.APIException
     * @throws com.jpaypp.exception.ChannelException
     * @throws com.jpaypp.exception.RateLimitException
     */
    public static Charge retrieve(String id) throws com.jpaypp.exception.AuthenticationException, com.jpaypp.exception.InvalidRequestException, com.jpaypp.exception.APIConnectionException, com.jpaypp.exception.APIException, com.jpaypp.exception.ChannelException, com.jpaypp.exception.RateLimitException {
        return retrieve(id, null, null);
    }

    /**
     * 查询 charge
     *
     * @param id
     * @return Charge
     * @throws com.jpaypp.exception.AuthenticationException
     * @throws com.jpaypp.exception.InvalidRequestException
     * @throws com.jpaypp.exception.APIConnectionException
     * @throws com.jpaypp.exception.APIException
     * @throws com.jpaypp.exception.ChannelException
     * @throws com.jpaypp.exception.RateLimitException
     */
    public static Charge retrieve(String id, String apiKey) throws com.jpaypp.exception.AuthenticationException, com.jpaypp.exception.InvalidRequestException, com.jpaypp.exception.APIConnectionException, com.jpaypp.exception.APIException, com.jpaypp.exception.ChannelException, com.jpaypp.exception.RateLimitException {
        return retrieve(id, apiKey, null);
    }

    /**
     * 查询 charge
     *
     * @param id
     * @param params
     * @return Charge
     * @throws com.jpaypp.exception.AuthenticationException
     * @throws com.jpaypp.exception.InvalidRequestException
     * @throws com.jpaypp.exception.APIConnectionException
     * @throws com.jpaypp.exception.APIException
     * @throws com.jpaypp.exception.ChannelException
     * @throws com.jpaypp.exception.RateLimitException
     */
    public static Charge retrieve(String id, Map<String, Object> params) throws com.jpaypp.exception.AuthenticationException, com.jpaypp.exception.InvalidRequestException, com.jpaypp.exception.APIConnectionException, com.jpaypp.exception.APIException, com.jpaypp.exception.ChannelException, com.jpaypp.exception.RateLimitException {
        return retrieve(id, null, params);
    }

    /**
     * 查询 charge
     *
     * @param id
     * @param params
     * @return Charge
     * @throws com.jpaypp.exception.AuthenticationException
     * @throws com.jpaypp.exception.InvalidRequestException
     * @throws com.jpaypp.exception.APIConnectionException
     * @throws com.jpaypp.exception.APIException
     * @throws com.jpaypp.exception.ChannelException
     * @throws com.jpaypp.exception.RateLimitException
     */
    public static Charge retrieve(String id, String apiKey, Map<String, Object> params) throws com.jpaypp.exception.AuthenticationException, com.jpaypp.exception.InvalidRequestException, com.jpaypp.exception.APIConnectionException, com.jpaypp.exception.APIException, com.jpaypp.exception.ChannelException, com.jpaypp.exception.RateLimitException {
        String retrieveUrl = String.format("%s/retrieve/" + id, classURL(Charge.class));
        System.out.println("retrieveUrl:" + retrieveUrl);
        params.put("ch",id);
        return request(RequestMethod.POST, retrieveUrl, apiKey, params, Charge.class);
    }

    /**
     * 撤销 charge
     *
     * @param id
     * @param apiKey  MasJPay ApiKey
     * @param params
     * @return Charge
     * @throws com.jpaypp.exception.AuthenticationException
     * @throws com.jpaypp.exception.InvalidRequestException
     * @throws com.jpaypp.exception.APIConnectionException
     * @throws com.jpaypp.exception.APIException
     * @throws com.jpaypp.exception.ChannelException
     * @throws com.jpaypp.exception.RateLimitException
     */
    public static Charge reverse(String id, String apiKey, Map<String, Object> params) throws com.jpaypp.exception.AuthenticationException, com.jpaypp.exception.InvalidRequestException, com.jpaypp.exception.APIConnectionException, com.jpaypp.exception.APIException, com.jpaypp.exception.ChannelException, com.jpaypp.exception.RateLimitException {
        String reverseUrl = String.format("%s/reverse/" + id, classURL(Charge.class));
        if (params == null) {
            params = new HashMap<String, Object>();
        }

        params.put("ch",id);

        return request(RequestMethod.POST, reverseUrl, apiKey, params, Charge.class);
    }

    /**
     * 撤销 charge
     *
     * @param id
     * @param params
     * @return Charge
     * @throws com.jpaypp.exception.AuthenticationException
     * @throws com.jpaypp.exception.InvalidRequestException
     * @throws com.jpaypp.exception.APIConnectionException
     * @throws com.jpaypp.exception.APIException
     * @throws com.jpaypp.exception.ChannelException
     * @throws com.jpaypp.exception.RateLimitException
     */
    public static Charge reverse(String id, Map<String, Object> params) throws com.jpaypp.exception.AuthenticationException, com.jpaypp.exception.InvalidRequestException, com.jpaypp.exception.APIConnectionException, com.jpaypp.exception.APIException, com.jpaypp.exception.ChannelException, com.jpaypp.exception.RateLimitException {
        return reverse(id, null, params);
    }

    /**
     * 撤销 charge
     *
     * @param id
     * @return Charge
     * @throws com.jpaypp.exception.AuthenticationException
     * @throws com.jpaypp.exception.InvalidRequestException
     * @throws com.jpaypp.exception.APIConnectionException
     * @throws com.jpaypp.exception.APIException
     * @throws com.jpaypp.exception.ChannelException
     * @throws com.jpaypp.exception.RateLimitException
     */
    public static Charge reverse(String id) throws com.jpaypp.exception.AuthenticationException, com.jpaypp.exception.InvalidRequestException, com.jpaypp.exception.APIConnectionException, com.jpaypp.exception.APIException, com.jpaypp.exception.ChannelException, com.jpaypp.exception.RateLimitException {
        return reverse(id, null, null);
    }

    /**
     * 撤销 charge
     *
     * @param id
     * @param apiKey  MasJPay ApiKey
     * @return Charge
     * @throws com.jpaypp.exception.AuthenticationException
     * @throws com.jpaypp.exception.InvalidRequestException
     * @throws com.jpaypp.exception.APIConnectionException
     * @throws com.jpaypp.exception.APIException
     * @throws com.jpaypp.exception.ChannelException
     * @throws com.jpaypp.exception.RateLimitException
     */
    public static Charge reverse(String id, String apiKey) throws com.jpaypp.exception.AuthenticationException, com.jpaypp.exception.InvalidRequestException, com.jpaypp.exception.APIConnectionException, com.jpaypp.exception.APIException, com.jpaypp.exception.ChannelException, RateLimitException {
        return reverse(id, apiKey, null);
    }
}
