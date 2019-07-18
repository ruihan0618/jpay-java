package com.jpaypp.model;

/**
 * Created by Afon on 15/12/30.
 */
public class EventData extends MasJPayObject {
    MasJPayObject object;

    public MasJPayObject getObject() {
        return object;
    }

    public void setObject(MasJPayObject object) {
        this.object = object;
    }
}
