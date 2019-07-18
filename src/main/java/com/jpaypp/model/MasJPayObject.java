package com.jpaypp.model;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;

public abstract class MasJPayObject {

    public static final Gson PRETTY_PRINT_GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .disableHtmlEscaping()
            .registerTypeAdapter(Double.class, new com.jpaypp.serializer.DoubleTypeSerializer())
            .registerTypeAdapter(ChargeEssentials.class, new com.jpaypp.serializer.ChargeEssentialsSerializer())
            .create();

//    public static Gson getPrettyPrintGson() {
//        try {
//            Class<?> klass = Class.forName("com.jpaypp.net.AppBasedResource");
//            Field field = klass.getField("PRETTY_PRINT_GSON");
//            return (Gson) field.get(klass);
//        } catch (ClassNotFoundException e) {
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
//
//        return PRETTY_PRINT_GSON;
//    }

   // @Override
   // public String toString() {
     //   return getPrettyPrintGson().toJson(this);
    //}
}
