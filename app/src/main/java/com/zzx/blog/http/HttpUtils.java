package com.zzx.blog.http;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.zzx.blog.AppContext;
import com.zzx.blog.myinterface.ResultListener;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import utils.StringUtils;


/**
 * Created by Administrator on 2017/6/22.
 */

public class HttpUtils {

//    public static <T> void requestPosts(Context context, String url, Map<String, Object> map, Callback<T> callback) {
//        HttpParams params = new HttpParams();
//        if (map != null) {
//            Log.d("TAG", "上传的参数:" + map.toString() + "请求地址:" + url);
//            for (String key : map.keySet()) {
//                params.put(key, map.get(key) == null ? "" : map.get(key).toString());
//            }
//        }
//
//        OkGo.<T>post(url)
//                .isMultipart(true)
//                .tag(context)
//                .params(params)
////                .headers(headers)
//                .execute(callback);
//    }

    public static <T> void requestPosts(Context context, String url, Map<String, Object> mapFile, Callback<T> callback) {
        HttpParams params = new HttpParams();
        if (mapFile != null) {
            for (String key : mapFile.keySet()) {
                if (mapFile.get(key) instanceof File) {
                    MediaType MEDIA_TYPE_PLAIN = MediaType.parse("image/jpeg;charset=utf-8");
                    HttpParams.FileWrapper fileWrapper = new HttpParams.FileWrapper((File) mapFile.get(key), ((File) mapFile.get(key)).getName(), MEDIA_TYPE_PLAIN);
                    params.put(key, fileWrapper);
                } else {
                    params.put(key, mapFile.get(key) == null ? "" : mapFile.get(key).toString());
                }
            }
        }
        if (AppContext.getInstance().checkUser()) {//登录
            String token = AppContext.getInstance().getUserInfo().getToken();
            if (!StringUtils.isEmpty(token)) {
                params.put("token", token);
            }
        }
//        Map<String, Object> objectMap = new HashMap<>();
//        for (String key : objectMap.keySet()) {
//            params.put(key, objectMap.get(key) == null ? "" : objectMap.get(key).toString());
//        }
        OkGo.<T>post(url)//
                .isMultipart(true)
                .tag(context)//
                .params(params)//
                .execute(callback);
    }

    public static <T> void requestGet(Context context, String url, Map<String, Object> map, Callback<T> callback) {
        HttpParams params = new HttpParams();
        if (map != null) {
            Log.d("TAG", "上传的参数:" + map.toString() + "请求地址:" + url);
            for (String key : map.keySet()) {
                params.put(key, map.get(key) == null ? "" : map.get(key).toString());
            }
        }
        OkGo.<T>get(url)//
                .tag(context)//
                .params(params)//
                .execute(callback);
    }

    public static <T> void requestPosts(Context context, String url, Map<String, Object> map, final ResultListener resultListener) {
        HttpParams params = new HttpParams();
        if (map != null) {
            Log.d("TAG", "上传的参数:" + map.toString() + "请求地址:" + url);
            for (String key : map.keySet()) {
                params.put(key, map.get(key) == null ? "" : map.get(key).toString());
            }
        }
        OkGo.<T>post(url)
                .isMultipart(true)
                .tag(context)
                .params(params)
//                .headers(headers)
                .execute((Callback<T>) new JsonCallback<String>(context) {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String body = response.body();
                        Log.d("TAG", "回调数据:" + body);
                        ServerData serverData = JSON.parseObject(body, ServerData.class);
                        if (serverData != null) {
                            resultListener.onSuccess(serverData.getData(), serverData.getMessage());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        resultListener.onFailure(response.body());
                    }
                });
    }

}
