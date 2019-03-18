package com.football.net.http.request;

import com.google.gson.Gson;

import java.util.HashMap;


/**
 * Created by Administrator on 2015/3/25.
 */
public class   RequestParam {

//    HashMap<String,String> header = new HashMap<String,String>();
    HashMap<String,Object> param = new HashMap<String,Object>();
    public RequestParam() {
//        header.put("token", NaKeApplication.getInstance().getUser().getToken()==null? null: CallXApplication.getInstance().getUser().getToken());
//        header.put("userid", CallXApplication.getInstance().getUser().getUserid()==null?null: CallXApplication.getInstance().getUser().getUserid());
//        header.put("udid", JPushInterface.getRegistrationID(CallXApplication.getInstance()));
//        header.put("version", PackageUtils.getVersionName());
//        header.put("versioncode", PackageUtils.getVersionCode() + "");
//        header.put("devicetype","2");
    }

    public void put(String key, Object value){
        if(value != null){
            param.put(key,value);
        }
    }


    @Override
    public String toString() {
        Gson g = new Gson();
        return g.toJson(param);
    }

    public HashMap<String, Object> getParam() {
        return param;
    }

    public void setParam(HashMap<String, Object> param) {
        this.param = param;
    }

//    public HashMap<String, String> getHeader() {
//        return header;
//    }
//
//    public void setHeader(HashMap<String, String> header) {
//        this.header = header;
//    }
}
