package com.hl.book.util.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

/**
 * 描述:网络帮助类
 * 作者:Leon
 * 时间:2016/12/22 0022
 */

public class JsonUtil {
    public static int getIntByJson(String json, String key){
        return (Integer) getDataByJson(json,key,true);
    }
    public static String getStringByJson(String json, String key){
        return (String) getDataByJson(json,key,false);
    }
    private static Object getDataByJson(String json, String key, boolean isInt){
        try{
            json = json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1) ;//删掉有可能出现在bom头
        }catch (Exception e){
            e.printStackTrace();
        }
        String des = "" ;
        int result = 0 ;
        try{
            JSONObject jsonObject = new JSONObject(json);
            if (isInt){
                result = jsonObject.optInt(key) ;
            }else {
                des = jsonObject.optString(key) ;
            }
        }catch (Exception e){
            des = "" ;
            result = 0 ;
        }
        if (isInt){
            return result ;
        }else {
            return des ;
        }
    }

    private static Gson gson = null;
    public static <T> T fromJson(String json, Class<T> classOfT){
        try {
            if (gson ==null){
                gson = new GsonBuilder().serializeNulls().create();
            }
            return gson.fromJson(json,classOfT);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public static String toJson(Object o){
        try {
            if (gson ==null){
                gson = new GsonBuilder().create();
            }
            return gson.toJson(o);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public static Gson getGson(){
        if (gson ==null){
            gson = new GsonBuilder().serializeNulls().create();
        }
        return gson;
    }
}
