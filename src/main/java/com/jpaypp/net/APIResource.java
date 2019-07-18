package com.jpaypp.net;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.jpaypp.MasJPay;
import com.jpaypp.exception.RateLimitException;
import com.jpaypp.model.MasJPayObject;
import com.jpaypp.model.MasJPayRawJsonObject;
import com.jpaypp.exception.InvalidRequestException;
import com.jpaypp.model.*;
import com.jpaypp.serializer.*;
import com.jpaypp.util.Md5;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

import javax.net.ssl.HttpsURLConnection;

/**
 * extends the abstract class when you need requset anything from MasJPay
 */
public abstract class APIResource extends MasJPayObject {
    /**
     * URLEncoder charset
     */
    public static final String CHARSET = "UTF-8";

    private static final String REQUEST_TIME_KEY = "jpaypp-Request-Timestamp";

    public static int CONNECT_TIMEOUT = 30;
    public static int READ_TIMEOUT = 80;
    public static int RETRY_MAX = 1;

    /**
     * Http requset method
     */
    protected enum RequestMethod {
        GET, POST, DELETE, PUT
    }

    /**
     * Gson object use to transform json string to resource object
     */
    public static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(EventData.class, new EventDataDeserializer())
            .registerTypeAdapter(MasJPayRawJsonObject.class, new MasJPayRawJsonObjectDeserializer())
            .create();

    public static Gson getGson() {
        try {
            Class<?> klass = Class.forName("com.jpaypp.net.AppBasedResource");
            Field field = klass.getField("GSON");
            return (Gson) field.get(klass);
        } catch (ClassNotFoundException e) {
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return GSON;
    }

    public static Class<?> getSelfClass() {
        return APIResource.class;
    }

    /**
     * @param clazz
     * @return className
     */
    protected static String className(Class<?> clazz) {
        String className = clazz.getSimpleName().toLowerCase().replace("$", " ");
        return className;
    }

    /**
     * @param clazz
     * @return singleClassURL
     */
    protected static String singleClassURL(Class<?> clazz) throws InvalidRequestException {
        String className = null;
        Class<?> klass = getSelfClass();
        if (!klass.getSimpleName().equalsIgnoreCase("APIResource")) {
            try {
                Method method = klass.getMethod("className", Class.class);
                className = (String)method.invoke(klass, clazz);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        if (className == null) {
            className = className(clazz);
        }

        return String.format("%s/v1/%s", MasJPay.getApiBase(), className);
    }

    /**
     * @param clazz
     * @return classURL
     */
    protected static String classURL(Class<?> clazz) throws InvalidRequestException {
        return String.format("%ss", singleClassURL(clazz));
    }

    /**
     * @param clazz
     * @param id
     * @return instanceURL
     * @throws InvalidRequestException
     */
    protected static String instanceURL(Class<?> clazz, String id) throws InvalidRequestException {
        try {
            return String.format("%s/%s", classURL(clazz), urlEncode(id));
        } catch (UnsupportedEncodingException e) {
            throw new InvalidRequestException("Unable to encode parameters to " + CHARSET, null, e);
        }
    }

    protected static String apiBasePrefixedURL(String url) {
        return String.format("%s%s", MasJPay.getApiBase(), url);
    }

    /**
     * @param str
     * @return urlEncodedString
     * @throws UnsupportedEncodingException
     */
    protected static String urlEncode(String str) throws UnsupportedEncodingException {
        if (str == null) {
            return null;
        } else {
            return URLEncoder.encode(str, CHARSET);
        }
    }

    /**
     * @param k
     * @param v
     * @return urlEncodedString
     * @throws UnsupportedEncodingException
     */
    private static String urlEncodePair(String k, String v)
            throws UnsupportedEncodingException {
        return String.format("%s=%s", urlEncode(k), urlEncode(v));
    }

    /**
     * @param apiKey
     * @return headers
     */
    static Map<String, String> getHeaders(String apiKey) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Accept-Charset", CHARSET);
        headers.put("User-Agent", String.format("MasJPay/v1 JavaBindings/%s", MasJPay.VERSION));

        if (apiKey == null) {
            apiKey = MasJPay.apiKey;
        }

        headers.put("Authorization", String.format("Bearer %s", apiKey));
        headers.put("Accept-Language", MasJPay.AcceptLanguage);

        // debug headers
        String[] propertyNames = {"os.name", "os.version", "os.arch", "java.version", "java.vendor", "java.vm.version", "java.vm.vendor"};
        Map<String, String> propertyMap = new HashMap<String, String>();
        for (String propertyName : propertyNames) {
            propertyMap.put(propertyName, System.getProperty(propertyName));
        }
        propertyMap.put("bindings.version", MasJPay.VERSION);
        propertyMap.put("lang", "Java");
        propertyMap.put("publisher", "MasJPay");
        headers.put("X-MasJPay-Client-User-Agent", getGson().toJson(propertyMap));
        if (MasJPay.apiVersion != null) {
            headers.put("jpaypp-Version", MasJPay.apiVersion);
        }
        return headers;
    }

    /**
     * @param url
     * @param apiKey
     * @return HttpURLConnection
     * @throws IOException
     */
    private static java.net.HttpURLConnection createPingppConnection(
            String url, String apiKey) throws IOException {
        URL pingppURL = new URL(url);
        HttpURLConnection conn;
        if (pingppURL.getProtocol().equals("https")) {
            conn = (HttpsURLConnection) pingppURL.openConnection();
        } else {
            conn = (HttpURLConnection) pingppURL.openConnection();
        }

        conn.setConnectTimeout(CONNECT_TIMEOUT * 1000);
        conn.setReadTimeout(READ_TIMEOUT * 1000);
        conn.setUseCaches(false);
        for (Map.Entry<String, String> header : getHeaders(apiKey).entrySet()) {
            conn.setRequestProperty(header.getKey(), header.getValue());
        }

        return conn;
    }

    /**
     * @throws com.jpaypp.exception.APIConnectionException
     */
    private static void throwInvalidCertificateException() throws com.jpaypp.exception.APIConnectionException {
        throw new com.jpaypp.exception.APIConnectionException("Invalid server certificate. You tried to connect to a server that has a revoked SSL certificate, which means we cannot securely send data to that server. ");
    }

    /**
     * @param url
     * @param query
     * @return formatedURL
     */
    private static String formatURL(String url, String query) {
        if (query == null || query.equals("")) {
            return url;
        } else {
            // In some cases, URL can already contain a question mark (eg, upcoming invoice lines)
            String separator = url.contains("?") ? "&" : "?";
            return String.format("%s%s%s", url, separator, query);
        }
    }

    /**
     * @param url
     * @param query
     * @param apiKey
     * @return HttpURLConnection
     * @throws IOException
     * @throws com.jpaypp.exception.APIConnectionException
     */
    private static java.net.HttpURLConnection createGetConnection(String url, String query, String apiKey) throws IOException, com.jpaypp.exception.APIConnectionException { String getURL = formatURL(url, query);java.net.HttpURLConnection conn = createPingppConnection(getURL, apiKey);
        conn.setRequestMethod("GET");

        String requestTime = currentTimeString();
        String stringToBeSigned = getRequestURIFromURL(conn.getURL()) + requestTime;
        conn.setRequestProperty(REQUEST_TIME_KEY, requestTime);
        String signature = "";

        return conn;
    }

    private static java.net.HttpURLConnection createDeleteConnection(String url, String query, String apiKey) throws IOException, com.jpaypp.exception.APIConnectionException { String getURL = formatURL(url, query);java.net.HttpURLConnection conn = createPingppConnection(getURL, apiKey);
        conn.setRequestMethod("DELETE");

        String requestTime = currentTimeString();
        String stringToBeSigned = getRequestURIFromURL(conn.getURL()) + requestTime;
        conn.setRequestProperty(REQUEST_TIME_KEY, requestTime);
        String signature = "";

        return conn;
    }

    /**
     * @param url
     * @param query
     * @param apiKey
     * @return HttpURLConnection
     * @throws IOException
     * @throws com.jpaypp.exception.APIConnectionException
     */
    private static java.net.HttpURLConnection createPostConnection(String url, String query, String apiKey) throws IOException, com.jpaypp.exception.APIConnectionException { java.net.HttpURLConnection conn = createPingppConnection(url, apiKey);

        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", String.format(
                "application/json;charset=%s", CHARSET));

        String stringToBeSigned = query;
        stringToBeSigned += getRequestURIFromURL(conn.getURL());
        String requestTime = currentTimeString();
        stringToBeSigned += requestTime;

        String signature = "";
        conn.setRequestProperty(REQUEST_TIME_KEY, requestTime);

        OutputStream output = null;
        try {
            output = conn.getOutputStream();
            output.write(query.getBytes(CHARSET));
        } finally {
            if (output != null) {
                output.close();
            }
        }
        return conn;
    }

    /**
     * @param url
     * @param query
     * @param apiKey
     * @return HttpURLConnection
     * @throws IOException
     * @throws com.jpaypp.exception.APIConnectionException
     */
    private static java.net.HttpURLConnection createPutConnection(String url, String query, String apiKey) throws IOException, com.jpaypp.exception.APIConnectionException { java.net.HttpURLConnection conn = createPingppConnection(url, apiKey);

        conn.setDoOutput(true);
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", String.format("application/json;charset=%s", CHARSET));

        String stringToBeSigned = query;
        stringToBeSigned += getRequestURIFromURL(conn.getURL());
        String requestTime = currentTimeString();
        stringToBeSigned += requestTime;

        String signature = "";
        conn.setRequestProperty(REQUEST_TIME_KEY, requestTime);

        OutputStream output = null;
        try {
            output = conn.getOutputStream();
            output.write(query.getBytes(CHARSET));
        } finally {
            if (output != null) {
                output.close();
            }
        }
        return conn;
    }

    /**
     * @param  params
     * @return signString
     */
    private static String createQuerySign(Map<String, Object> params) {
        try {
            return createParams(params);
        }catch (UnsupportedEncodingException e){
            return "";
        }catch (InvalidRequestException e){
            return "";
        }
    }


    /**
     * @param params
     * @return queryString
     * @throws UnsupportedEncodingException
     * @throws InvalidRequestException
     */
    private static String createQuery(Map<String, Object> params) throws UnsupportedEncodingException, InvalidRequestException {
        Map<String, String> flatParams = flattenParams(params);
        StringBuilder queryStringBuffer = new StringBuilder();
        for (Map.Entry<String, String> entry : flatParams.entrySet()) {
            if (queryStringBuffer.length() > 0) {
                queryStringBuffer.append("&");
            }
            queryStringBuffer.append(urlEncodePair(entry.getKey(), entry.getValue()));
        }
        return queryStringBuffer.toString();
    }

    /**
     * @param params
     * @return JSONString
     */
    private static String createJSONString(Map<String, Object> params) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        return gson.toJson(params);
    }

    /**
     * @param params
     * @return flattenParams
     * @throws InvalidRequestException
     */
    private static Map<String, String> flattenParams(Map<String, Object> params)
            throws InvalidRequestException {
        if (params == null) {
            return new HashMap<String, String>();
        }
        Map<String, String> flatParams = new HashMap<String, String>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map<?, ?>) {
                Map<String, Object> flatNestedMap = new HashMap<String, Object>();
                Map<?, ?> nestedMap = (Map<?, ?>) value;
                for (Map.Entry<?, ?> nestedEntry : nestedMap.entrySet()) {
                    flatNestedMap.put(
                            String.format("%s[%s]", key, nestedEntry.getKey()),
                            nestedEntry.getValue());
                }
                flatParams.putAll(flattenParams(flatNestedMap));
            } else if (value instanceof ArrayList<?>) {
                ArrayList<?> ar = (ArrayList<?>) value;
                Map<String, Object> flatNestedMap = new HashMap<String, Object>();
                int size = ar.size();
                for (int i = 0; i < size; i++) {
                    flatNestedMap.put(String.format("%s[%d]", key, i), ar.get(i));
                }
                flatParams.putAll(flattenParams(flatNestedMap));
            } else if ("".equals(value)) {
                throw new InvalidRequestException("You cannot set '" + key + "' to an empty string. " +
                        "We interpret empty strings as null in requests. " +
                        "You may set '" + key + "' to null to delete the property.",
                        key, null);
            } else if (value == null) {
                flatParams.put(key, "");
            } else {
                flatParams.put(key, value.toString());
            }
        }
        return flatParams;
    }

    /**
     * create params
     * @param params
     * @return queryString
     */
    private static String createParams(Map<String, Object> params) throws UnsupportedEncodingException, InvalidRequestException{

        Map<String, Object> _params = new HashMap<String, Object>();
        StringBuilder queryStringBuffer = new StringBuilder();

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();  Object value = entry.getValue();
            if (value instanceof Map<?, ?>) {
                Map<String, String> ctx = (Map<String, String>) value;
                for (Map.Entry<String,String> cte: ctx.entrySet()){
                    String ctk = cte.getKey();  Object ctv = cte.getValue();
                    _params.put(ctk,ctv.toString());
                }

            }else{
                _params.put(key,value.toString());
            }
        }
        //sort
        Set set  =   _params.keySet();  Object[] arr =   set.toArray();
        Arrays.sort(arr);
        for(Object key:arr){
            if (queryStringBuffer.length() > 0) {
                queryStringBuffer.append("&");
            }
            queryStringBuffer.append(urlEncodePair(key.toString(), _params.get(key).toString()));
        }

        return  queryStringBuffer.toString();
    }

    // represents Errors returned as JSON
    private static class ErrorContainer {
        private APIResource.Error error;
    }

    /**
     *
     */
    private static class Error {
        @SuppressWarnings("unused")
        String type;

        String message;

        String code;

        String param;

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            if (null != type ) {
                sb.append("Error type: " + type + "\n");
            }
            if (null != message) {
                sb.append("\t Error message: " + message + "\n");
            }
            if (null != code) {
                sb.append("\t Error code: " + code + "\n");
            }

            return sb.toString();
        }
    }

    /**
     * @param responseStream
     * @return responseString
     * @throws IOException
     */
    private static String getResponseBody(InputStream responseStream)  throws IOException {
        String rBody = new Scanner(responseStream, CHARSET) .useDelimiter("\\A").next(); //
        responseStream.close();
        return rBody;
    }

    /**
     * @param method
     * @param url
     * @param query
     * @param apiKey
     * @return MasJPayResponse
     * @throws com.jpaypp.exception.APIConnectionException
     */
    private static MasJPayResponse makeURLConnectionRequest(
            APIResource.RequestMethod method, String url, String query,
            String apiKey) throws com.jpaypp.exception.APIConnectionException {
        java.net.HttpURLConnection conn = null;
        try {
            switch (method) {
                case GET:
                    conn = createGetConnection(url, query, apiKey);
                    break;
                case POST:
                    conn = createPostConnection(url, query, apiKey);
                    break;
                case DELETE:
                    conn = createDeleteConnection(url, query, apiKey);
                    break;
                case PUT:
                    conn = createPutConnection(url, query, apiKey);
                    break;
                default:
                    throw new com.jpaypp.exception.APIConnectionException(String.format("Unrecognized HTTP method %s. ", method));
            }
            // trigger the request
            int rCode = conn.getResponseCode();
            String rBody = null;
            Map<String, List<String>> headers;

            if (rCode >= 200 && rCode < 300) {
                rBody = getResponseBody(conn.getInputStream());
            } else {
                rBody = getResponseBody(conn.getErrorStream());
            }
            headers = conn.getHeaderFields();
            return new MasJPayResponse(rCode, rBody, headers);

        } catch (IOException e) {
            throw new com.jpaypp.exception.APIConnectionException(
                    String.format("IOException during API request to MasJPay (%s): %s " + "Please check your internet connection and try again. If this problem persists," + "you should check MasJPay's service status at https://pingxx.com/status.", MasJPay.getApiBase(), e.getMessage()), e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * @param method
     * @param url
     * @param params
     * @param clazz
     * @param <T>
     * @return MasJPayObject
     * @throws com.jpaypp.exception.AuthenticationException
     * @throws InvalidRequestException
     * @throws com.jpaypp.exception.APIConnectionException
     * @throws com.jpaypp.exception.APIException
     */
    protected static <T> T request(APIResource.RequestMethod method, String url, Map<String, Object> params, Class<T> clazz) throws com.jpaypp.exception.AuthenticationException, InvalidRequestException, com.jpaypp.exception.APIConnectionException, com.jpaypp.exception.APIException, com.jpaypp.exception.ChannelException, com.jpaypp.exception.RateLimitException {
        return request(method, url, null, params, clazz);
    }

    /**
     * @param method
     * @param url
     * @param apiKey
     * @param params
     * @param clazz
     * @param <T>
     * @return MasJPayObject
     * @throws com.jpaypp.exception.AuthenticationException
     * @throws InvalidRequestException
     * @throws com.jpaypp.exception.APIConnectionException
     * @throws com.jpaypp.exception.APIException
     * @throws com.jpaypp.exception.ChannelException
     * @throws com.jpaypp.exception.RateLimitException
     *
     */
    protected static <T> T request(APIResource.RequestMethod method, String url, String apiKey, Map<String, Object> params, Class<T> clazz) throws com.jpaypp.exception.AuthenticationException, InvalidRequestException, com.jpaypp.exception.APIConnectionException, com.jpaypp.exception.APIException, com.jpaypp.exception.ChannelException, com.jpaypp.exception.RateLimitException {

        String clientId =  MasJPay.clientId;
        apiKey = apiKey != null ? apiKey : MasJPay.apiKey;

        if ((clientId == null || clientId.length() == 0)) {
            throw new com.jpaypp.exception.AuthenticationException(
                    "No clientId provided. (HINT: set your API key using 'MasJPay.clientId = <clientId>'. "
                            + "You can generate API keys from the MasJPay web interface. "
                            + "See https://jpay.weidun.biz/agent for details.");
        }
        if ((apiKey == null || apiKey.length() == 0)) {
            throw new com.jpaypp.exception.AuthenticationException(
                    "No API key provided. (HINT: set your API key using 'MasJPay.apiKey = <API-KEY>'. "
                            + "You can generate API keys from the MasJPay web interface. "
                            + "See https://jpay.weidun.biz/agent for details.");
        }

        params.put("client_id",clientId);
        //create sign
        try {
            params.put("sign", Md5.md5(createQuerySign(params) + "&key=" + MasJPay.apiKey).toUpperCase());
        }catch (Exception e){
            params.put("sign","");
        }
        String query = null;
        switch (method) {
            case GET:
            case DELETE:
                try {
                    query = createQuery(params);
                } catch (UnsupportedEncodingException e) {
                    throw new InvalidRequestException("Unable to encode parameters to " + CHARSET, null, e);
                }
                break;
            case POST:
            case PUT:
                query = createJSONString(params);
                break;
        }

        MasJPayResponse response;
        int retryCount = 0;
        while(true) {
            try {
                // HTTPSURLConnection verifies SSL cert by default
                response = makeURLConnectionRequest(method, url, query, apiKey);
                if (MasJPay.DEBUG) {
                    System.out.println(getGson().toJson(response));
                }

                int rCode = response.getResponseCode();
                String rBody = response.getResponseBody();
                if (rCode < 200 || rCode >= 300) {
                    handleAPIError(rBody, rCode);
                }
                return getGson().fromJson(rBody, clazz);
            } catch (ClassCastException ce) {
                throw ce;
            } catch (ConnectException e) {
                if(retryCount < RETRY_MAX) {
                    retryCount++;
                } else {
                    throw new com.jpaypp.exception.APIConnectionException(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 错误处理
     *
     * @param rBody
     * @param rCode
     * @throws InvalidRequestException
     * @throws com.jpaypp.exception.AuthenticationException
     * @throws com.jpaypp.exception.APIException
     */
    private static void handleAPIError(String rBody, int rCode)
            throws InvalidRequestException, com.jpaypp.exception.AuthenticationException,
            com.jpaypp.exception.APIException, com.jpaypp.exception.ChannelException, com.jpaypp.exception.RateLimitException, ConnectException {
        Error error = null;
        try {
            error = getGson().fromJson(rBody,
                    ErrorContainer.class).error;
        } catch (JsonSyntaxException e) {
            error = new Error();
            error.message = rBody;
            error.code = String.valueOf(rCode);
        }

        switch (rCode) {
            case 400:
                throw new InvalidRequestException(error.toString(), error.param, null);
            case 404:
                throw new InvalidRequestException(error.toString(), error.param, null);
            case 403:
            case 429:
                throw new RateLimitException(error.toString(), null);
            case 402:
                throw new com.jpaypp.exception.ChannelException(error.toString(), error.param, null);
            case 401:
                throw new com.jpaypp.exception.AuthenticationException(error.toString());
            case 502:
                throw new ConnectException(error.toString());
            default:
                throw new com.jpaypp.exception.APIException(error.toString(), null);
        }
    }

    private static String currentTimeString() {
        Integer requestTime = (int) (System.currentTimeMillis() / 1000);
        return requestTime.toString();
    }

    private static String getRequestURIFromURL(URL url) {
        String path = url.getPath();
        String query = url.getQuery();
        if (query == null) {
            return path;
        }
        return path + "?" + query;
    }
}
