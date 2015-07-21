package com.example.deti.parse;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2015/7/16.
 */
public class JsonParse {
    public static <T> T getPerson(String jsonString, Class<T> cls) {
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(jsonString, cls);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return t;
    }
}
