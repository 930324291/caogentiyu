package com.football.net.http.request;

import com.google.gson.Gson;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/3/25.
 */
public class RequestInnerStr {

    HashMap<String,Object> map = new HashMap<String,Object>();

    public RequestInnerStr() {

    }

    public void put(String key, Object value){
        if(value != null){
            map.put(key,value);
        }
    }

    public String getString(){
        Gson g = new Gson();
        return g.toJson(map,HashMap.class);
    }


}
