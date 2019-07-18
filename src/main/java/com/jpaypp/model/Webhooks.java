package com.jpaypp.model;

import com.jpaypp.net.APIResource;


/**
 * Created by on 19/5/11.
 */
public class Webhooks {
    /**
     * 解析 event 中的 object
     *
     * @param eventStr
     * @return MasJPayObject
     */
    public static MasJPayObject getObject(String eventStr) {
        return eventParse(eventStr).getData().getObject();
    }

    /**
     * 解析event，返回Event对象
     *
     * @param eventStr
     * @return Event
     */
    public static com.jpaypp.model.Event eventParse(String eventStr) {
        return APIResource.getGson().fromJson(eventStr, Event.class);
    }
}
