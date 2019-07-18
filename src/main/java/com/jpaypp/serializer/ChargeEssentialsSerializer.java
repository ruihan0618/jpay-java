package com.jpaypp.serializer;

import com.google.gson.*;
import com.jpaypp.model.ChargeEssentials;

import java.lang.reflect.Type;

/**
 * Created by  on 16/11/06.
 */
public class ChargeEssentialsSerializer implements JsonSerializer<ChargeEssentials> {

    public JsonElement serialize(ChargeEssentials chargeEssentials, Type type, JsonSerializationContext jsonSerializationContext) {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Double.class, new DoubleTypeSerializer())
                .disableHtmlEscaping();

        if (chargeEssentials.getChannel() != null) {
            gsonBuilder.serializeNulls();
        }

        return gsonBuilder.create().toJsonTree(chargeEssentials, type);
    }
}
