package com.jpaypp.serializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.jpaypp.model.MasJPayRawJsonObject;

import java.lang.reflect.Type;

/**
 * Created by on 15/12/30.
 */
public class MasJPayRawJsonObjectDeserializer implements JsonDeserializer<MasJPayRawJsonObject> {
    public MasJPayRawJsonObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        MasJPayRawJsonObject object = new MasJPayRawJsonObject();
        object.json = json.getAsJsonObject();
        return object;
    }
}
