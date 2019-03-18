/**
 * ID: SmartClient.java
 * Copyright (c) 2002-2014 Luther Inc.
 * http://zuv.cc
 * All rights reserved.
 */
package com.football.net.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.football.net.common.constant.IConfig;
import com.football.net.common.fileIo.StorageUtil;
import com.football.net.common.util.LogUtils;
import com.football.net.http.reponse.Result;
import com.football.net.manager.FootBallApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;


/**
 * http client
 */
public class SmartClient implements IConfig {

    private static final Logger logger = LoggerFactory.getLogger(SmartClient.class);

    //---------------------------------------------------------------

    public static final int HTTP_OK = 200;

    //---------------------------------------------------------------

    private FootBallApplication application;
    private AsyncHttpClient client;
    private Gson gson;

    //接口缓存管理
//    private SmartCache smartcache;

    private Context context;
    private boolean debug;


    public SmartClient(Context context) {
        application = (FootBallApplication) context.getApplicationContext();
        debug = application.isAppLog();
        this.context = context;
        client = new AsyncHttpClient();
        client.setConnectTimeout(5 * 1000);
        client.setTimeout(5 * 1000);
        client.setResponseTimeout(60 * 1000);
        client.setUserAgent("ChromeOS");
        client.addHeader("Accept", "application/json");
        client.addHeader("Content-Type", "application/json");
//        client.addHeader("token", FootBallApplication.getInstance().getUser().getToken() == null ? "" : CallXApplication.getInstance().getUser().getToken());
//        client.addHeader("userid", CallXApplication.getInstance().getUser().getUserid() == null ? "" : CallXApplication.getInstance().getUser().getUserid());
        client.addHeader("clientType", "1");
        gson = new Gson();

    }

    public SmartClient(Context context, boolean uploadFile) {
        application = (FootBallApplication) context.getApplicationContext();
        debug = application.isAppLog();
        this.context = context;
        client = new AsyncHttpClient();
        client.setConnectTimeout(60 * 1000);
        client.setTimeout(60 * 1000);
        client.setResponseTimeout(60 * 1000);
//        client.addHeader("token", FootBallApplication.getInstance().getUser().getToken() == null ? "" : CallXApplication.getInstance().getUser().getToken());
//        client.addHeader("userid", CallXApplication.getInstance().getUser().getUserid() == null ? "" : CallXApplication.getInstance().getUser().getUserid());
        gson = new Gson();

    }

    //---------------------------------------------------------------

    public void setImageListScrollListener(AbsListView listView, boolean pauseOnScroll, boolean pauseOnFling) {
        listView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), pauseOnScroll, pauseOnFling));
    }

    public void stopImageLoader() {
        ImageLoader.getInstance().stop();
    }

    public void clearImageMemoryCache() {
        ImageLoader.getInstance().clearMemoryCache();
    }

    public void clearImageDiskCache() {
        ImageLoader.getInstance().clearDiskCache();
    }

    public DisplayImageOptions optionImage(int loading, int failure, int empty) {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(loading)
                .showImageForEmptyUri(empty)
                .showImageOnFail(failure)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    public DisplayImageOptions optionImage(int loading, int failure, int empty, BitmapDisplayer displayer) {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(loading)
                .showImageForEmptyUri(empty)
                .showImageOnFail(failure)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(displayer)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    public void displayImage(String uri, ImageView imageView, DisplayImageOptions options) {
        ImageLoader.getInstance().displayImage(uri, imageView, options);
    }

    public void displayImage(String uri, ImageView imageView, DisplayImageOptions options, ImageLoadingListener listener) {
        ImageLoader.getInstance().displayImage(uri, imageView, options, listener);
    }

    //---------------------------------------------------------------

    //	public <T extends Result> RequestHandle get(final String url,
//			final SmartCallback<T> callback,
//			final Class<T> clazz)
//	{
//		return get(url, null, callback, clazz, application.getToken(), false, true);
//	}
//
//	public <T extends Result> RequestHandle get(final String url,
//			final SmartCallback<T> callback,
//			final Class<T> clazz,
//			final boolean usecache)
//	{
//		return get(url, null, callback, clazz, application.getToken(), usecache, true);
//	}
//
//	public <T extends Result> RequestHandle get(final String url,
//			final SmartParams params,
//			final SmartCallback<T> callback,
//			final Class<T> clazz)
//	{
//		return get(url, params, callback, clazz, application.getToken(), false, true);
//	}
//
//	public <T extends Result> RequestHandle get(final String url,
//			final SmartParams params,
//			final SmartCallback<T> callback,
//			final Class<T> clazz,
//			final boolean usecache)
//	{
//		return get(url, params, callback, clazz, application.getToken(), usecache, true);
//	}
//
    public <T> RequestHandle getList(final String url,
                                     final SmartParams params,
                                     final SmartListCallback<T> callback,
                                     final Class<T> cls) {
        //判断网络是否可用, 不可用则从缓存查找 缓存不存在则报错
        if (!application.isConnected()) {
            callback.onFailure(ERRCODE_NETWORK_UNAVALLABLE, ERRMSG_NETWORK_UNAVALLABLE);
            return null;
        }


        return client.get(url, params, new SmartResponseHandler<T>() {
            @Override
            public void onStart() {
                debugreq(url, params, false);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (TextUtils.isEmpty(responseString)) {
                    callback.onFailure(ERRCODE_REQUEST_FAILURE, ERRMSG_REQUEST_FAILURE);
                    return;
                }
                ArrayList<T> t = new ArrayList<T>();
                try {
                    JsonParser parser = new JsonParser();
                    //将JSON的String 转成一个JsonArray对象
                    JsonArray jsonArray = parser.parse(responseString).getAsJsonArray();
                    //加强for循环遍历JsonArray
                    for (JsonElement user : jsonArray) {
                        //使用GSON，直接转成Bean对象
                        T userBean = gson.fromJson(user, cls);
                        t.add(userBean);
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    callback.onFailure(ERRCODE_FORMAT_INVALID, ERRMSG_FORMAT_INVALID);
                    return;
                }

                //        if(t.isSuccess()){
                callback.onSuccess(statusCode, t);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callback.onFailure(statusCode, responseString);
            }
        });
    }

    public <T extends Result> RequestHandle get(final String url,
                                                final SmartParams params,
                                                final SmartCallback<T> callback,
                                                final Class<T> clazz) {
        return get(url, params, callback, clazz, null, true);
    }

    public <T extends Result> RequestHandle get(final String url,
                                                final SmartParams params,
                                                final SmartCallback<T> callback,
                                                final Class<T> clazz,
                                                final String token,
                                                final boolean autologin) {
        //判断网络是否可用, 不可用则从缓存查找 缓存不存在则报错
        if (!application.isConnected()) {
//            CacheEntry entry = useCache(url, params);
//            if (entry != null) {
//                T t = gson.fromJson(entry.getData(), clazz);
//                callback.onSuccess(HTTP_OK, t);
//            } else {
            callback.onFailure(ERRCODE_NETWORK_UNAVALLABLE, ERRMSG_NETWORK_UNAVALLABLE);
//            }
            return null;
        }

        //使用缓存, 且缓存存在则执行成功并返回
//        if (usecache) {
//            CacheEntry entry = useCache(url, params);
//            if (entry != null) {
//                T t = gson.fromJson(entry.getData(), clazz);
//                callback.onSuccess(HTTP_OK, t);
//                return null;
//            }
//        }

        //否则,则进入网络请求
        if (!TextUtils.isEmpty(token)) {
            client.addHeader("Authorization", "Token " + token);
        }
        return client.get(url, params, new SmartResponseHandler<T>() {
            @Override
            public void onStart() {
                debugreq(url, params, false);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                success(callback, clazz, statusCode, responseString, autologin);
                debugres(responseString);
//                setCache(url, params, responseString, 0);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failure(callback, clazz, statusCode, responseString, throwable, autologin);
            }
        });
    }


    //---------------------------------------------------------------

    public void getheader(final String url,
                          final SmartParams params,
                          final SmartCallback<Result> callback) {
        //判断网络是否可用, 不可用则从缓存查找 缓存不存在则报错
        if (!application.isConnected()) {
            callback.onFailure(ERRCODE_NETWORK_UNAVALLABLE, ERRMSG_NETWORK_UNAVALLABLE);
        }

        //否则,则进入网络请求
//		if(!TextUtils.isEmpty(application.getToken()))
//		{
//			client.addHeader("Authorization", "Token "+application.getToken());
//		}
        client.setEnableRedirects(false);
        client.get(url, params, new SmartResponseHandler<Result>() {
            @Override
            public void onStart() {
                debugreq(url, params, false);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (headers != null &&
                        (statusCode == HttpStatus.SC_MULTIPLE_CHOICES ||
                                statusCode == HttpStatus.SC_MOVED_PERMANENTLY ||
                                statusCode == HttpStatus.SC_MOVED_TEMPORARILY ||
                                statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)
                        ) {
                    for (Header header : headers) {
                        if (header.getName().equalsIgnoreCase("Location")) {
                            responseString = header.getValue();
                            break;
                        }
                    }
                }

                callback.onSuccess(HTTP_OK, new Result(IERRCode.ERRCODE_SUCCESS, responseString));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (headers != null &&
                        (statusCode == HttpStatus.SC_MULTIPLE_CHOICES ||
                                statusCode == HttpStatus.SC_MOVED_PERMANENTLY ||
                                statusCode == HttpStatus.SC_MOVED_TEMPORARILY ||
                                statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)
                        ) {
                    for (Header header : headers) {
                        if (header.getName().equalsIgnoreCase("Location")) {
                            responseString = header.getValue();
                            callback.onSuccess(HTTP_OK, new Result(IERRCode.ERRCODE_SUCCESS, responseString));
                            break;
                        }
                    }
                } else {
                    callback.onFailure(statusCode, responseString);
                }
            }
        });
    }

    //---------------------------------------------------------------


    public <T extends Result> RequestHandle post(final String url,
                                                 final String params,
                                                 final SmartCallback<T> callback,
                                                 final Class<T> clazz) {
        //判断网络是否可用
        if (!application.isConnected()) {
            callback.onFailure(ERRCODE_NETWORK_UNAVALLABLE, ERRMSG_NETWORK_UNAVALLABLE);
            return null;
        }

        //否则,则进入网络请求
        ByteArrayEntity entity = null;
        if (params != null) {
            //Post请求直接传入json字符串
            try {
                entity = new ByteArrayEntity(params.getBytes("UTF-8"));
                entity.setContentType("application/json");
            } catch (UnsupportedEncodingException e) {
                callback.onFailure(500, "字符转换错误");
                return null;
            }
        }

//		if(!TextUtils.isEmpty(token))
//		{
//			client.addHeader("token", token);
//			client.addHeader("loginsign", "");
//		}
        return client.post(context, url, entity, "application/json", new SmartResponseHandler<T>() {
            @Override
            public void onStart() {
                debugreq(url, params);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                debugres(responseString);
                Log.d("huang", responseString + "-=-=-=-=-=");
                success(callback, clazz, statusCode, responseString, false);
//                if(url.equals(HttpUrlConstant.getCommonData())) {
//                    String path = FileUtils.getCacheDir() + "/CommonData.txt";
//                    LogUtils.d("存储信息路径,path:" + path);
//                    FileUtils.writeFile(responseString, path, true);
//                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failure(callback, clazz, statusCode, responseString, throwable, false);
            }
        });
    }

    //---------------------------------------------------------------
//
//	public <T extends Result> RequestHandle put(final String url,
//			final SmartCallback<T> callback,
//			final Class<T> clazz)
//	{
//		return put(url, null, callback, clazz, application.getToken(), true);
//	}
//
//	public <T extends Result> RequestHandle put(final String url,
//			final SmartParams params,
//			final SmartCallback<T> callback,
//			final Class<T> clazz)
//	{
//		return put(url, null, callback, clazz, application.getToken(), true);
//	}

    public <T extends Result> RequestHandle put(final String url,
                                                final SmartParams params,
                                                final SmartCallback<T> callback,
                                                final Class<T> clazz,
                                                final String token,
                                                final boolean autologin) {
        //判断网络是否可用, 不可用则从缓存查找 缓存不存在则报错
        if (!application.isConnected()) {
            callback.onFailure(ERRCODE_NETWORK_UNAVALLABLE, ERRMSG_NETWORK_UNAVALLABLE);
            return null;
        }

        //否则,则进入网络请求
        if (!TextUtils.isEmpty(token)) {
            client.addHeader("Authorization", "Token " + token);
        }
        return client.put(url, params, new SmartResponseHandler<T>() {
            @Override
            public void onStart() {
                debugreq(url, params, false);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                success(callback, clazz, statusCode, responseString, autologin);
                debugres(responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failure(callback, clazz, statusCode, responseString, throwable, autologin);
            }
        });
    }

    //---------------------------------------------------------------

    //	public <T extends Result> RequestHandle delete(final String url,
//			final SmartCallback<T> callback,
//			final Class<T> clazz)
//	{
//		return delete(url, callback, clazz, application.getToken(), true);
//	}
    public <T extends Result> RequestHandle delete(final String url,
                                                   final SmartCallback<T> callback,
                                                   final Class<T> clazz,
                                                   final String token,
                                                   final boolean autologin) {
        //判断网络是否可用
        if (!application.isConnected()) {
            callback.onFailure(ERRCODE_NETWORK_UNAVALLABLE, ERRMSG_NETWORK_UNAVALLABLE);
            return null;
        }

        //网络请求
        if (!TextUtils.isEmpty(token)) {
            client.addHeader("Authorization", "Token " + token);
        }
        return client.delete(url, new SmartResponseHandler<T>() {
            @Override
            public void onStart() {
                debugreq(url, null, false);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                success(callback, clazz, statusCode, responseString, autologin);
                debugres(responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failure(callback, clazz, statusCode, responseString, throwable, autologin);
            }
        });
    }

    //---------------------------------------------------------------

    //	public RequestHandle head(final String url, final SmartHeadCallback callback)
//	{
//		return head(url, callback, application.getToken());
//	}
    public RequestHandle head(final String url, final SmartHeadCallback callback, final String token) {
        //判断网络是否可用
        if (!application.isConnected()) {
            callback.onFailure(ERRCODE_NETWORK_UNAVALLABLE, null, ERRMSG_NETWORK_UNAVALLABLE);
            return null;
        }

        //网络请求
        if (!TextUtils.isEmpty(token)) {
            client.addHeader("Authorization", "Token " + token);
        }
        client.setEnableRedirects(false);
        return client.head(url, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
                callback.onFailure(statusCode, headers, throwable.getMessage());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String responseString) {
                callback.onSuccess(statusCode, headers);
            }

        });
    }

    //---------------------------------------------------------------

    //同步head
//	public String headc(String url, SmartParams params)
//	{
//		return headc(url, params, application.getToken());
//	}
    public String headc(String url, SmartParams params, String token) {
        //判断网络是否可用
        if (!application.isConnected()) {
            return null;
        }

        //解析参数
        if (params != null) {
            if (url.indexOf('?') > -1) {
                url += "&" + params.value();
            } else {
                url += "?" + params.value();
            }
        }

        //
        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10 * 1000); // 10�? 连接�?��url的连接等待时�?
        HttpConnectionParams.setSoTimeout(client.getParams(), 30 * 1000);// 30�? 连接上一个url，获取response的返回等待时�?

        HttpHead head = new HttpHead(url);
        head.addHeader("Content-Type", "application/json");
        if (!TextUtils.isEmpty(token)) {
            head.addHeader("Authorization", "Token " + token);
        }

        try {
            //发送请求
            HttpResponse response = client.execute(head);
            int status = response.getStatusLine().getStatusCode();

            if ((status == HttpStatus.SC_MOVED_TEMPORARILY)
                    || (status == HttpStatus.SC_MOVED_PERMANENTLY)
                    || (status == HttpStatus.SC_SEE_OTHER)
                    || (status == HttpStatus.SC_TEMPORARY_REDIRECT)) {
                Header head_location = head.getFirstHeader("location");
                if (head_location != null) {
                    String location = head_location.getValue();
                    logger.info("location : " + location);
                    return location;
                }
                return null;
            } else {
                String responseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                logger.info("headc : " + responseString);
                return responseString;
            }
        } catch (java.net.SocketTimeoutException e) {
            logger.error("请求超时", e);
            return null;
        } catch (IOException e) {
            logger.error("读写错误", e);
            return null;
        }
    }

    //---------------------------------------------------------------

    //同步get
//	public <T extends Result> T getc(String url, SmartParams params, Class<T> clazz)
//	{
//		return getc(url, params, application.getToken(), clazz);
//	}
    public <T extends Result> T getc(String url, SmartParams params, String token, Class<T> clazz) {
        //判断网络是否可用
        if (!application.isConnected()) {
            return null;
        }

        //解析参数
        if (params != null) {
            if (url.indexOf('?') > -1) {
                url += "&" + params.value();
            } else {
                url += "?" + params.value();
            }
        }

        //
        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10 * 1000); // 10�? 连接�?��url的连接等待时�?
        HttpConnectionParams.setSoTimeout(client.getParams(), 30 * 1000);// 30�? 连接上一个url，获取response的返回等待时�?

        HttpGet get = new HttpGet(url);
        get.addHeader("Content-Type", "application/json");
        if (!TextUtils.isEmpty(token)) {
            get.addHeader("Authorization", "Token " + token);
        }

        try {
            //发送请求
            HttpResponse response = client.execute(get);
            int status = response.getStatusLine().getStatusCode();
            String msg = response.getStatusLine().getReasonPhrase();
            if (status == HttpStatus.SC_OK) {
                String responseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                logger.info("getc : " + responseString);

                T t = null;
                try {
                    t = gson.fromJson(responseString, clazz);
                } catch (JsonSyntaxException e) {
                    t.setResult(ERRCODE_FORMAT_INVALID);
                    t.setError(ERRMSG_FORMAT_INVALID);
                    return t;
                }

//				if(t.getResult() == API_ERRCODE_TOKEN_INVALID)
//				{
//					//自动重新登录
//					UserBean user = application.getUser();
//					if(user != null)
//					{
//						String account = user.getUserid();
//						String encpass = user.getUserEncpass();
//						boolean simulate = user.isSimulate();
//
//						//请求后台自动登录
////						EventBus.getDefault().post(new EventUpdateDispatchReq(account, encpass, true, simulate));
//					}
//
//					t.setResult(API_ERRCODE_TOKEN_INVALID);
//					t.setDescription(API_ERRMSG_TOKEN_INVALID);
//					return t;
//				}

                return t;
            } else {
                logger.error("status=" + status + ",message=" + msg);
                return null;
            }
        } catch (java.net.SocketTimeoutException e) {
            logger.error("请求超时", e);
            return null;
        } catch (IOException e) {
            logger.error("读写错误", e);
            return null;
        }
    }

    //---------------------------------------------------------------

    //同步post
//	public Result postc(String url, SmartParams params)
//	{
//		return postc(url, params, application.getToken());
//	}
    public <T extends Result> Result postc(final String url,
                                           final String params,
                                           final Class<T> clazz) {
        //判断网络是否可用
        if (!application.isConnected()) {
            return new Result(ERRCODE_NETWORK_UNAVALLABLE, ERRMSG_NETWORK_UNAVALLABLE);
        }

        //否则,则进入网络请求
        StringEntity entity = null;
        if (params != null) {
            //Post请求直接传入json字符串
            try {
                entity = new StringEntity(params, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return new Result(ERRCODE_ENCODE_INVALID, ERRMSG_ENCODE_INVALID);
            }
        }

        //
        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10 * 1000); // 10s 连接时间url的连接等待时间
        HttpConnectionParams.setSoTimeout(client.getParams(), 30 * 1000);// 30s 连接上一个url，获取response的返回等待时间

        HttpPost post = new HttpPost(url);
        post.addHeader("Content-Type", "application/json");
//        if (!TextUtils.isEmpty(token)) {
//            post.addHeader("Authorization", "Token " + token);
//        }
//        post.addHeader("token", CallXApplication.getInstance().getUser().getToken() == null ? "" : CallXApplication.getInstance().getUser().getToken());
//        post.addHeader("userid", CallXApplication.getInstance().getUser().getUserid() == null ? "" : CallXApplication.getInstance().getUser().getUserid());
        post.setEntity(entity);

        try {
            //发送请求
            HttpResponse response = client.execute(post);
            int status = response.getStatusLine().getStatusCode();
            String msg = response.getStatusLine().getReasonPhrase();
            if (status == HttpStatus.SC_OK) {
                String responseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                logger.info("postc : " + responseString);

                T t = null;
                try {
                    t = gson.fromJson(responseString, clazz);
                } catch (JsonSyntaxException e) {
                    return new Result(ERRCODE_FORMAT_INVALID, ERRMSG_FORMAT_INVALID);
                }

//				if(t.getResult() == API_ERRCODE_TOKEN_INVALID)
//				{
//					//自动重新登录
//					UserBean user = application.getUser();
//					if(user != null)
//					{
//						String account = user.getUserid();
//						String encpass = user.get();
//						boolean simulate = user.isSimulate();
//
//						//请求后台自动登录
////						EventBus.getDefault().post(new EventUpdateDispatchReq(account, encpass, true, simulate));
//					}
//
//					return new Result(API_ERRCODE_TOKEN_INVALID, API_ERRMSG_TOKEN_INVALID);
//				}

                return t;
            } else {
                return new Result(status, msg);
            }
        } catch (java.net.SocketTimeoutException e) {
            logger.error("请求超时", e);
            return new Result(ERRCODE_REQUEST_FAILURE, ERRMSG_REQUEST_FAILURE);
        } catch (IOException e) {
            logger.error("读写错误", e);
            return new Result(ERRCODE_REQUEST_FAILURE, ERRMSG_REQUEST_FAILURE);
        }
    }

    //---------------------------------------------------------------


//    public  void uploadFile(String path, String url,String param) throws Exception {
//        File file = new File(path);
//        if (file.exists() && file.length() > 0) {
////            AsyncHttpClient client = new AsyncHttpClient();
//            RequestParams params = new RequestParams();
//            params.put("jsonparam",param);
//            params.put("files", file);
//
//            // 上传文件
//            client.post(url, params, new AsyncHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers,
//                                      byte[] responseBody) {
//                    // 上传成功后要做的工作
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers,
//                                      byte[] responseBody, Throwable error) {
//                    // 上传失败后要做到工作
//                }
//
//                @Override
//                public void onProgress(int bytesWritten, int totalSize) {
//                    // TODO Auto-generated method stub
//                    super.onProgress(bytesWritten, totalSize);
//                    int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);
//                    // 上传进度显示
//                }
//
//                @Override
//                public void onRetry(int retryNo) {
//                    // TODO Auto-generated method stub
//                    super.onRetry(retryNo);
//                    // 返回重试次数
//                }
//
//            });
//        }
//
//    }


    /**
     * @param path 要上传的文件路径
     * @param url  服务端接收URL
     * @throws Exception
     */
    public <T extends Result> void uploadFile(String url, String path, final SmartCallback<T> callback,
                                              final Class<T> clazz) {

        logger.info("========================upload========================");
        //判断网络是否可用
        if (!application.isConnected()) {
            callback.onFailure(ERRCODE_NETWORK_UNAVALLABLE, ERRMSG_NETWORK_UNAVALLABLE);
            return;
        }

        File file = new File(path);
        if (file.exists() && file.length() > 0) {
            RequestParams params = new RequestParams();
            try {
                params.put("file", file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                callback.onFailure(ERRCODE_REQUEST_FILE_Error, ERRMSG_REQUEST_FILE_rror);
                return;
            }
//            client.addHeader("Content-Type", "multipart/form-data");
            // 上传文件
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      byte[] responseBody) {
                    // 上传成功后要做的工作
//                    progress.setProgress(0);
                    try {
                        String responseString = new String(responseBody, "utf-8");
                        debugres(responseString);
                        success(callback, clazz, statusCode, responseString, false);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      byte[] responseBody, Throwable error) {
//                    try {
                    String responseString = new String(responseBody);
//                        debugres(responseString);
                    failure(callback, clazz, statusCode, responseString, error, false);
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
                }

                @Override
                public void onProgress(int bytesWritten, int totalSize) {
                    // TODO Auto-generated method stub
                    super.onProgress(bytesWritten, totalSize);
//                    int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);
                    // 上传进度显示
//                    LogUtils.e("上传 Progress>>>>>"+bytesWritten + " / " + totalSize);
                }

                @Override
                public void onRetry(int retryNo) {
                    // TODO Auto-generated method stub
                    super.onRetry(retryNo);
                    // 返回重试次数
                }

            });
        } else {
//            Result result = new Result();
//            result.setSuccess(false);
//            result.setError("文件不存在");
            callback.onFailure(ERRCODE_REQUEST_FILE_NOTEXIT, ERRMSG_REQUEST_FILE_NOTEXIT);
        }

    }


    /**
     * 普通文件上传
     *
     * @param url
     * @param files
     * @param paramStr
     * @param callback
     * @param clazz
     * @param <T>
     */
    public <T extends Result> void upload(String url, ArrayList<File> files, String paramStr, final SmartCallback<T> callback,
                                          final Class<T> clazz) {
        // TODO
//        if(files == null || files.size() == 0){
//            return;
//        }
        logger.info("========================upload========================");
        logger.info("paramStr : " + paramStr);
        //判断网络是否可用
        if (!application.isConnected()) {
            callback.onFailure(ERRCODE_NETWORK_UNAVALLABLE, ERRMSG_NETWORK_UNAVALLABLE);
            return;
        }
//        client.addHeader("Content-Type", "multipart/form-data");
        client.removeHeader("Content-Type");
        final HttpPost post = new HttpPost(url);
//        post.addHeader("charset", HTTP.UTF_8);
        MultipartEntity reqEntity = new MultipartEntity();
        try {
            reqEntity.addPart("jsonparam", new StringBody(paramStr, Charset.forName(HTTP.UTF_8)));
            for (int i = 0; i < files.size(); i++) {
                FileBody bin1 = new FileBody(files.get(i));
                reqEntity.addPart("files", bin1);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Result result = new Result();
            result.setResult(0);
            result.setError("文件格式有误，或者文件不存在");
            callback.onSuccess(-1, (T) result);
            return;
        }
        post.setEntity(reqEntity);
        debugreq(url, paramStr);
        new AsyncTask<String, Integer, T>() {

            @Override
            protected void onPostExecute(T response) {
                super.onPostExecute(response);
                if (response.getResult() == 1) {
                    callback.onSuccess(200, response);
                } else {
//                    callback.onSuccess( -1,  response);
                    callback.onFailure(response.getResult(), response.getError());
                }
            }

            @Override
            protected T doInBackground(String... params) {
                {
                    HttpResponse response = null;
                    try {
                        response = client.getHttpClient().execute(post);
                        LogUtils.d("response:" + response.getStatusLine().getStatusCode());

                        if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                            HttpEntity entity = response.getEntity();
                            //显示内容
                            if (entity != null) {
                                String s = EntityUtils.toString(response.getEntity());
                                debugres(s);
                                T res = new Gson().fromJson(s, clazz);
                                entity.consumeContent();
                                return res;
                            }
                        } else {

                            Result result = new Result();
                            result.setResult(0);
                            result.setError("文件上传失败");
                            return (T) result;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Result result = new Result();
                        result.setResult(0);
                        result.setError("文件上传失败");
                        return (T) result;
                    }
                }
                return null;
            }
        }.execute("");


    }

    /**
     * 私教课程发布
     *
     * @param url      接口地址
     * @param file1    封面图片
     * @param file2    课程图片
     * @param paramStr 参数
     * @param callback 回调
     * @param clazz
     * @param <T>
     */
    public <T extends Result> void upload(String url, ArrayList<File> file1, ArrayList<File> file2, String paramStr, final SmartCallback<T> callback,
                                          final Class<T> clazz) {
        // TODO
//        if(files == null || files.size() == 0){
//            return;
//        }
        logger.info("========================upload========================");
        logger.info("paramStr : " + paramStr);
        //判断网络是否可用
        if (!application.isConnected()) {
            callback.onFailure(ERRCODE_NETWORK_UNAVALLABLE, ERRMSG_NETWORK_UNAVALLABLE);
            return;
        }
//        client.addHeader("Content-Type", "multipart/form-data");
        client.removeHeader("Content-Type");
        final HttpPost post = new HttpPost(url);
//        post.addHeader("charset", HTTP.UTF_8);
        MultipartEntity reqEntity = new MultipartEntity();
        try {
            reqEntity.addPart("jsonparam", new StringBody(paramStr, Charset.forName(HTTP.UTF_8)));
            if (file1 != null) {
                for (int i = 0; i < file1.size(); i++) {
                    FileBody bin1 = new FileBody(file1.get(i));
                    reqEntity.addPart("file1", bin1);
                }
            }
            if (file2 != null) {
                for (int i = 0; i < file2.size(); i++) {
                    FileBody bin1 = new FileBody(file2.get(i));
                    reqEntity.addPart("file2", bin1);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Result result = new Result();
            result.setResult(0);
            result.setError("文件格式有误，或者文件不存在");
            callback.onSuccess(-1, (T) result);
            return;
        }
        post.setEntity(reqEntity);
        debugreq(url, paramStr);
        new AsyncTask<String, Integer, T>() {

            @Override
            protected void onPostExecute(T response) {
                super.onPostExecute(response);
                logger.info("response : " + response.getResult() + "," + response.toString());
                if (response.getResult() == 1) {
                    callback.onSuccess(200, response);
                } else {
//                    callback.onSuccess( -1,  response);
                    callback.onFailure(response.getResult(), response.getError());
                }
            }

            @Override
            protected T doInBackground(String... params) {
                {
                    HttpResponse response = null;
                    try {
                        response = client.getHttpClient().execute(post);
                        logger.info("response:" + response.getStatusLine().getStatusCode());

                        if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                            HttpEntity entity = response.getEntity();
                            //显示内容
                            if (entity != null) {
                                String s = EntityUtils.toString(response.getEntity());
                                debugres(s);
                                T res = new Gson().fromJson(s, clazz);
                                entity.consumeContent();
                                return res;
                            }
                        } else {

                            Result result = new Result();
                            result.setResult(0);
                            result.setError("文件上传失败");
                            return (T) result;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Result result = new Result();
                        result.setResult(0);
                        result.setError("文件上传失败");
                        return (T) result;
                    }
                }
                return null;
            }
        }.execute("");


    }


    /**
     * 技能发布文字、图片、语音
     *
     * @param url        接口地址
     * @param files      图片集合
     * @param vociceFile 语音文件
     * @param paramStr   参数
     * @param callback   回调
     * @param clazz
     * @param <T>
     */
    public <T extends Result> void upload(String url, ArrayList<File> files, File vociceFile, String paramStr, final SmartCallback<T> callback,
                                          final Class<T> clazz) {
        // TODO
//        if(files == null || files.size() == 0){
//            return;
//        }
        //判断网络是否可用
        if (!application.isConnected()) {
            callback.onFailure(ERRCODE_NETWORK_UNAVALLABLE, ERRMSG_NETWORK_UNAVALLABLE);
            return;
        }
//        client.addHeader("Content-Type", "multipart/form-data");
        client.removeHeader("Content-Type");
        final HttpPost post = new HttpPost(url);
//        post.addHeader("charset", HTTP.UTF_8);
        MultipartEntity reqEntity = new MultipartEntity();
        try {
            reqEntity.addPart("jsonparam", new StringBody(paramStr, Charset.forName(HTTP.UTF_8)));
            for (int i = 0; i < files.size(); i++) {
                FileBody bin1 = new FileBody(files.get(i));
                String fileStr = "file" + (i + 1);
                reqEntity.addPart(fileStr, bin1);
            }
            if (vociceFile != null) {
                FileBody fileBody = new FileBody(vociceFile);
                reqEntity.addPart("voice", fileBody);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Result result = new Result();
            result.setResult(0);
            result.setError("文件格式有误，或者文件不存在");
            callback.onSuccess(-1, (T) result);
            return;
        }
        post.setEntity(reqEntity);
        debugreq(url, paramStr);
        new AsyncTask<String, Integer, T>() {

            @Override
            protected void onPostExecute(T response) {
                super.onPostExecute(response);
                if (response.getResult() == 1) {
                    callback.onSuccess(200, response);
                } else {
//                    callback.onSuccess( -1,  response);
                    callback.onFailure(response.getResult(), response.getError());
                }
            }

            @Override
            protected T doInBackground(String... params) {
                {
                    HttpResponse response = null;
                    try {
                        response = client.getHttpClient().execute(post);
                        LogUtils.d("response:" + response.getStatusLine().getStatusCode());

                        if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                            HttpEntity entity = response.getEntity();
                            //显示内容
                            if (entity != null) {
                                String s = EntityUtils.toString(response.getEntity());
                                debugres(s);
                                T res = new Gson().fromJson(s, clazz);
                                entity.consumeContent();
                                return res;
                            }
                        } else {

                            Result result = new Result();
                            result.setResult(0);
                            result.setError("文件上传失败");
                            return (T) result;
                        }
                    } catch (Exception e) {

                        LogUtils.d("抛异常了:" + e.getMessage() + ",\nString:" + e.toString());
                        e.printStackTrace();
                        Result result = new Result();
                        result.setResult(0);
                        result.setError("文件上传失败");
                        return (T) result;
                    }
                }
                return null;
            }
        }.execute("");


    }


    public static String uploadc(String url, File file, final String params) {
        logger.info("upload");
        if (url == null || file == null) return null;

        //判断网络是否可用
        if (!FootBallApplication.getInstance().isConnected()) {
            logger.info("network unavailable");
            return null;
        }

        //返回结果
        String result = null;

        //
        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10 * 1000); // 10s 连接url的连接等待时间
        HttpConnectionParams.setSoTimeout(client.getParams(), 60 * 1000);// 60s 连接上一个url，获取response的返回等待时间

        HttpPost httpPost = new HttpPost(url);
//        httpPost.addHeader("token", CallXApplication.getInstance().getUser().getToken() == null ? "" : CallXApplication.getInstance().getUser().getToken());
//        httpPost.addHeader("userid", CallXApplication.getInstance().getUser().getUserid() == null ? "" : CallXApplication.getInstance().getUser().getUserid());
        try {
            MultipartEntity reqEntity = new MultipartEntity();
            reqEntity.addPart("jsonparam", new StringBody(params, Charset.forName(HTTP.UTF_8)));
//            reqEntity.addPart("filename", new StringBody(name, Charset.forName(HTTP.UTF_8)));
//            reqEntity.addPart(name, new FileBody(file, name, "application/octet-stream", "UTF-8"));
            FileBody bin1 = new FileBody(file);
            reqEntity.addPart("files", bin1);
            httpPost.setEntity(reqEntity);

            HttpResponse response = client.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK) {
                String str = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                logger.info("upload : " + str);

                Gson gson = new Gson();
//                try {
//                    FilePut t = gson.fromJson(str, FilePut.class);
//                    if (t.getResult() == API_ERRCODE_SUCCESS) {
//                        result = t.getData().getFilename();
//                    }
//                } catch (JsonSyntaxException e) {
//                }
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("字符转换错误", e);
        } catch (ClientProtocolException e) {
            logger.error("协议错误", e);
        } catch (java.net.SocketTimeoutException e) {
            logger.error("请求超时", e);
        } catch (IOException e) {
            logger.error("读写错误", e);
        }

        return result;
    }

    //---------------------------------------------------------------

    //同步文件下载
//	public void file(final String url, final File dstfile, final SmartFileCallback callback, boolean usecache)
//	{
//		file(url, dstfile, callback, application.getToken(), usecache);
//	}

    public void file(final String url, final File dstfile, final SmartFileCallback callback, final String token, boolean usecache) {
        //判断网络是否可用
        if (!application.isConnected()) {
            callback.onFailure(ERRMSG_NETWORK_UNAVALLABLE);
            return;
        }

        if (!TextUtils.isEmpty(token)) {
            client.addHeader("Authorization", "Token " + token);
        }

        File file = dstfile;
        if (dstfile == null) {
            String key = new Md5FileNameGenerator().generate(url);
            file = new File(StorageUtil.getAvaRoot() + CACHE_PATH, key);
        }
        if (usecache && file.exists()) {
            callback.onSuccess(file);
        } else {
            final String filepath = file.getPath();
            new Thread() {

                @Override
                public void run() {
                    HttpClient client = new DefaultHttpClient();
                    HttpGet get = new HttpGet(url);
                    FileOutputStream fos = null;
                    try {
                        HttpResponse response = client.execute(get);
                        HttpEntity entity = response.getEntity();
                        long length = entity.getContentLength();
                        callback.onLength(length);

                        File file = new File(filepath);
                        if (file.exists()) file.delete();

                        InputStream is = entity.getContent();
                        if (is != null) {
                            fos = new FileOutputStream(file);
                            byte[] b = new byte[1024];
                            int len = 0;
                            long count = 0;
                            while ((len = is.read(b)) != -1) {
                                fos.write(b, 0, len);
                                count += len;
                                callback.onProgress(count);
                            }
                            fos.flush();
                            fos.close();

                            callback.onSuccess(file);
                        } else {
                            callback.onFailure("输入流为空");
                        }
                    } catch (ClientProtocolException e) {
                        logger.error(e.getMessage());
                        callback.onFailure("协议错误:" + e.getMessage());
                    } catch (IOException e) {
                        logger.error(e.getMessage());
                        callback.onFailure("文件读写错误:" + e.getMessage());
                    } finally {
                        try {
                            if (fos != null) fos.close();
                        } catch (IOException e) {
                            logger.error("close writer error", e);
                        }
                    }
                }

            }.start();
        }
    }

    //---------------------------------------------------------------

    //异步文件下载
//	public void filec(final String url, final File dstfile, final SmartFileCallback callback, boolean usecache)
//	{
//		filec(url, dstfile, callback, application.getToken(), usecache);
//	}
    public void filec(final String url, final File dstfile, final SmartFileCallback callback, final String token, boolean usecache) {
        //�?��网络是否可用
        if (!application.isConnected()) {
            callback.onFailure(ERRMSG_NETWORK_UNAVALLABLE);
            return;
        }

        if (TextUtils.isEmpty(token)) {
            client.addHeader("Authorization", "Token " + token);
        }

        File file = dstfile;
        if (dstfile == null) {
            String key = new Md5FileNameGenerator().generate(url);
            file = new File(StorageUtil.getAvaRoot() + CACHE_PATH, key);
        }
        if (usecache && file.exists()) {
            callback.onSuccess(file);
        } else {
            client.get(url, new FileAsyncHttpResponseHandler(file) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, File response) {
                    callback.onSuccess(response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                    callback.onFailure(throwable.getMessage());
                }

                @Override
                public void onProgress(int bytesWritten, int totalSize) {
                    callback.onLength(totalSize);
                    callback.onProgress(bytesWritten);
                }
            });
        }
    }

    //---------------------------------------------------------------

    private boolean cache_all = false;

    //查询缓存
//    private CacheEntry useCache(String url, SmartParams params) {
//        if (params != null) {
//            url += params.value();
//        }
//        url = DigestCoder.getHexString(DigestCoder.Digest(DigestCoder.MD5, url));
//
//        if (!cache_all && params != null && params.contain(API_PAGENUM_TAG)) {
//            try {
//                int curpage = Integer.parseInt(params.get(API_PAGENUM_TAG));
//                if (curpage <= MAX_PAGENUM_4_CACHE) {
//                    return smartcache.get(url);
//                }
//                return null;
//            } catch (NumberFormatException e) {
//                return null;
//            }
//        } else {
//            return smartcache.get(url);
//        }
//    }

    //保存缓存
//    private void setCache(String url, SmartParams params, String data, long timestamp) {
//        if (params != null) {
//            url += params.value();
//        }
//        url = DigestCoder.getHexString(DigestCoder.Digest(DigestCoder.MD5, url));
//
//        if (!cache_all && (params != null && params.contain(API_PAGENUM_TAG))) {
//            try {
//                int curpage = Integer.parseInt(params.get(API_PAGENUM_TAG));
//                if (curpage <= MAX_PAGENUM_4_CACHE) {
//                    CacheEntry entry = new CacheEntry();
//                    entry.setKey(url);
//                    entry.setData(data);
//                    entry.setServerDate(timestamp);
//                    entry.setUpdateDate(System.currentTimeMillis());
//                    smartcache.put(entry);
//                }
//            } catch (NumberFormatException e) {
//            }
//        } else {
//            CacheEntry entry = new CacheEntry();
//            entry.setKey(url);
//            entry.setData(data);
//            entry.setServerDate(timestamp);
//            entry.setUpdateDate(System.currentTimeMillis());
//            smartcache.put(entry);
//        }
//    }

    //---------------------------------------------------------------

    private void debugreq(String url, SmartParams params, boolean isjson) {
        if (debug) {
            logger.info("========================Request========================");
            logger.info("URL : " + url);
            logger.info("PARA : " + ((params != null) & isjson ? params.json() : params));
        }
    }

    private void debugreq(String url, String params) {
        if (debug) {
            logger.info("========================Request========================");
            logger.info("URL : " + url);
            logger.info("PARA : " + params);
        }
    }

    private void debugres(String data) {
        if (!TextUtils.isEmpty(data) && debug) {
            Gson formatter = new GsonBuilder().setPrettyPrinting().create();
            JsonElement je = new JsonParser().parse(data);
            logger.info("========================Response: Success========================");
            int size = 2500;
            if (data.length() > size) {
                for (int j = 0; j < (data.length() / size) + 1; j++) {
                    if (j == data.length() / size) {
                        logger.info("Result: " + data.substring(j * size));
                    } else {
                        logger.info("Result: " + data.substring(j * size, (j + 1) * size));
                    }
                }
            } else {
                logger.info("Result: " + data);
            }
            logger.info("DATA : " + formatter.toJson(je));
        }
    }

    //---------------------------------------------------------------

    private <T extends Result> void success(final SmartCallback<T> callback, final Class<T> cls,
                                            int statusCode, String responseString, boolean autologin) {
        if (TextUtils.isEmpty(responseString)) {
            callback.onFailure(ERRCODE_REQUEST_FAILURE, ERRMSG_REQUEST_FAILURE);
            return;
        }

        T t = null;
        try {
            t = gson.fromJson(responseString, cls);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            callback.onFailure(ERRCODE_FORMAT_INVALID, ERRMSG_FORMAT_INVALID);
            return;
        }

//        if(t.isSuccess()){
        callback.onSuccess(statusCode, t);
//        }else{
//            callback.onFailure(statusCode, t.getError());
//        }
//        if (t.getResult() == API_ERRCODE_SUCCESS) {
//            callback.onSuccess(statusCode, t);
//        }
//		else if(t.getResult() == API_ERRCODE_TOKEN_INVALID)
//		{
//			callback.onFailure(API_ERRCODE_TOKEN_INVALID, API_ERRMSG_TOKEN_INVALID);
//
//			if(!autologin) return;
//
//			//自动重新登录
//			User user = application.getUser();
//			if(user != null)
//			{
//				String account = user.getUserAccount();
//				String encpass = user.getUserEncpass();
//				boolean simulate = user.isSimulate();
//
//				//请求后台自动登录
////				EventBus.getDefault().post(new EventUpdateDispatchReq(account, encpass, true, simulate));
//			}
//		}
//        else if(t.getResult() == 2){    //token过期,需要重新登录
//            try {
//                if(((BaseActivity)context).dlg != null) {
//                    return;
//                }
//                ((BaseActivity)context).showDialog(UIUtils.getString(R.string.relogin),new MaterialDialog.ButtonCallback() {
//                    @Override
//                    public void onPositive(MaterialDialog dialog) {
//                        super.onPositive(dialog);
//                        ((BaseActivity)context).dissProgress();
//                        CommonUtils.setShortcutBadger(2, 0);
//                        TIMManager.getInstance().logout();
//                        UserInfoManagerNew.getInstance().ClearData();
//                        CommonUtils.cacheBooleanData(Constant.LOGIN_SUCCESS, false);
//                        Intent intent = new Intent(context,UserLoginActivity2_0.class);
//                        ((BaseActivity)context).startActivity(intent);
//                        CallXApplication.finishAll();
//
//                    }
//                });
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
//        }else {
//            callback.onFailure(statusCode, t.getError());
//        }
    }

    private <T extends Result> void failure(final SmartCallback<T> callback, final Class<T> cls,
                                            int statusCode, String responseString, Throwable throwable, boolean autologin) {
        logger.info("========================Response: Failure========================");
        logger.error("status : " + statusCode);
        logger.error("message : " + responseString);

        String msg = throwable.getMessage();

        if (throwable instanceof java.net.SocketTimeoutException) {
            callback.onFailure(ERRCODE_REQUEST_TIMEOUT, ERRMSG_REQUEST_TIMEOUT);
            return;
        }

        if (TextUtils.isEmpty(responseString)) {
            callback.onFailure(ERRCODE_REQUEST_FAILURE, ERRMSG_REQUEST_FAILURE);
            return;
        }

        T t = null;
        try {
            t = gson.fromJson(responseString, cls);
            msg = (t != null) ? t.getError() : msg;
        } catch (JsonSyntaxException e) {
        }

//		if(t!=null && t.getResult() == API_ERRCODE_TOKEN_INVALID)
//		{
//			callback.onFailure(API_ERRCODE_TOKEN_INVALID, API_ERRMSG_TOKEN_INVALID);
//
//			if(!autologin) return;
//
//			//自动重新登录
//			User user = application.getUser();
//			if(user != null)
//			{
//				String account = user.getUserAccount();
//				String encpass = user.getUserEncpass();
//				boolean simulate = user.isSimulate();
//
//				//请求后台自动登录
////				EventBus.getDefault().post(new EventUpdateDispatchReq(account, encpass, true, simulate));
//			}
//		}
        callback.onFailure(statusCode, msg);
    }

    //---------------------------------------------------------------

    public void stopPageRequest() {
        client.cancelRequests(context, true);
        context = null;
    }

}
