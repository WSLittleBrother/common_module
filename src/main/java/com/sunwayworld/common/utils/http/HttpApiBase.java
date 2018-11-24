package com.sunwayworld.common.utils.http;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.sunwayworld.common.dao.model.AppModel;
import com.sunwayworld.common.utils.InitAppUtils;
import com.sunwayworld.utils.util.EncryptUtils;
import com.sunwayworld.utils.util.ZipUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author yanghan ${2016/5/13}
 * @version V1.0
 * @Description 网络请求工具类
 */
public class HttpApiBase implements Progress.ProgressListener {
    private final static int SUCCESS = 99;
    private final static int FAILURE = 100;
    private final static int PROGRESS_DOWNLOAD = 101;
    private final static int PROGRESS_UPLOAD = 102;
    private OkHttpClient sOkHttpClient;
    private JsonHttpResponseListener mListener;
    private Handler handler;
    private Call call;

    public HttpApiBase() {
        handler = new MyHandler();
    }

    /**
     * GET请求
     *
     * @param url 请求地址
     */
    public void get(String url) {
        Request request = new Request.Builder().url(url).build();
        enqueueCall(request);
    }

    /**
     * GET请求
     *
     * @param url      请求地址
     * @param listener 监听
     */
    public void get(String url, JsonHttpResponseListener listener) {
        setOnJsonHttpResponseListener(listener);
        Request request = new Request.Builder().url(url).build();
        enqueueCall(request);
    }

    /**
     * POST请求
     *
     * @param url     请求地址
     * @param builder 参数集合
     */
    public void post(String url, FormBody.Builder builder) {
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        enqueueCall(request);
    }

    /**
     * POST请求
     *
     * @param url      请求地址
     * @param listener 监听
     */
    public void post(String url, JsonHttpResponseListener listener) {
        setOnJsonHttpResponseListener(listener);
        Request request = new Request.Builder().url(url).build();
        enqueueCall(request);
    }

    /**
     * POST请求
     *
     * @param url      请求地址
     * @param builder  FormBody.Builder参数集合
     * @param listener 监听
     */
    public void post(String url, FormBody.Builder builder, JsonHttpResponseListener listener) {
        setOnJsonHttpResponseListener(listener);
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        enqueueCall(request);
    }

    /**
     * POST请求
     *
     * @param url      请求地址
     * @param builder  MultipartBody.Builder参数集合
     * @param listener 监听
     */
    public void post(String url, MultipartBody.Builder builder, JsonHttpResponseListener listener) {
        setOnJsonHttpResponseListener(listener);
        RequestBody requestBody = builder.build();
        requestBody = Progress.wrapRequestBody(requestBody, this);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        enqueueCall(request);
    }

    /**
     * 可执行UI处理的POST请求
     *
     * @param url      请求地址
     * @param listener 监听
     */
    @Deprecated
    public void postHandler(String url, JsonHttpResponseListener listener) {
        setOnJsonHttpResponseListener(listener);
        Request request = new Request.Builder().url(url).build();
        enqueueHandlerCall(request);
    }

    /**
     * 可执行UI处理的POST请求
     *
     * @param url      请求地址
     * @param builder  FormBody.Builder参数集合
     * @param listener 监听
     */
    @Deprecated
    public void postHandler(String url, FormBody.Builder builder, JsonHttpResponseListener listener) {
        setOnJsonHttpResponseListener(listener);
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        enqueueHandlerCall(request);
    }

    /**
     * 可执行UI处理的POST请求
     *
     * @param url      请求地址
     * @param builder  MultipartBody.Builder参数集合
     * @param listener 监听
     */
    @Deprecated
    public void postHandler(String url, MultipartBody.Builder builder, JsonHttpResponseListener listener) {
        setOnJsonHttpResponseListener(listener);
        RequestBody requestBody = builder.build();
        requestBody = Progress.wrapRequestBody(requestBody, this);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        enqueueHandlerCall(request);
    }


    /**
     * 网络请求监听对象的接口回掉
     *
     * @param listener
     */
    public void setOnJsonHttpResponseListener(JsonHttpResponseListener listener) {
        mListener = listener;
    }

    /**
     * 网络请求监听接口：成功，失败，进度
     */
    public static abstract class JsonHttpResponseListener {
        public abstract void onSuccess(byte[] bytes);

        public abstract void onFailure(String errMsg, byte[] bytes);

        public void onUpProgress(long bytesRead, long contentLength, boolean done) {

        }

        public void onLoadProgress(long bytesRead, long contentLength, boolean done) {

        }
    }

    private class JsonCallback implements Callback {

        @Override
        public void onFailure(Call call, IOException e) {
            if (mListener != null) {
                mListener.onFailure(e.getMessage(), null);
            }
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {

            byte[] body = null;
            try {
                body = response.body().bytes();
                if (mListener != null) {
                    mListener.onSuccess(body);
                }
            } catch (Throwable e) {
                e.printStackTrace();
                if (mListener != null) {
                    mListener.onFailure(e.getMessage(), body);
                }
            }
        }
    }

    private class HandlerJsonCallback implements Callback {

        @Override
        public void onFailure(Call call, IOException e) {
            if (onHttpRunListener != null) {
                onHttpRunListener.onComplete();
            }
            sendMsg(FAILURE, null, e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) {
            if (onHttpRunListener != null) {
                onHttpRunListener.onComplete();
            }
            byte[] body = null;
            try {
                System.out.println("response---------> " + response);
                body = response.body().bytes();
                System.out.println("body---------> " + body);
                if (body != null) {
                    sendMsg(SUCCESS, body, null);
                } else {
                    sendMsg(FAILURE, body, null);
                }
            } catch (Throwable e) {
                e.printStackTrace();
                sendMsg(FAILURE, body, e.getMessage());
            }
        }
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            String exception;
            Bundle bundle = msg.getData();
            switch (msg.what) {
                case FAILURE:
                    Log.d("sunway", "失败");
                    if (mListener != null) {
                        exception = bundle.getString("error");
                        mListener.onFailure(exception, null);
                    }
                    if (onHttpRunListener != null) {
                        onHttpRunListener.onFailure();
                    }
                    break;
                case SUCCESS:
                    if (mListener != null) {
                        byte[] s = null;
                        if (bundle.containsKey("data")) {
                            s = bundle.getByteArray("data");
                            /*测试开始*/
                            String ss = null;
                            String deskey = InitAppUtils.getDesKey();
                            Log.d("sunway", "111");
                            try {
                                if (AppModel.getSysConfig().isEncrypt() && deskey != null) {
                                    ss =new String( EncryptUtils.decryptBase64_3DES(ZipUtils.unzipStr(s), deskey.getBytes()));
                                    Log.d("sunway", "解压的ss=" + ss);
                                } else {
                                    ss = new String(s, "utf-8");
                                    Log.d("sunway", "没解压的ss=" + ss);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d("sunway", "解压异常：" + e.getMessage());
                            }
                            /*测试结束*/
                            mListener.onSuccess(s);
                        } else if (bundle.containsKey("error")) {
                            exception = bundle.getString("error");
                            s = bundle.getByteArray("data");
                            mListener.onFailure(exception, s);
                        }
                    }
                    if (onHttpRunListener != null) {
                        onHttpRunListener.onSuccess();
                    }
                    break;
                case PROGRESS_DOWNLOAD:
                    if (mListener != null) {
                        mListener.onLoadProgress(bundle.getLong("bytesRead"), bundle.getLong("contentLength"), bundle.getBoolean("done"));
                    }
                    break;
                case PROGRESS_UPLOAD:
                    if (mListener != null) {
                        mListener.onUpProgress(bundle.getLong("bytesRead"), bundle.getLong("contentLength"), bundle.getBoolean("done"));
                    }
                    break;
            }
        }
    }

    /**
     * 发送消息
     *
     * @param what
     * @param body
     * @param errMsg
     */
    private void sendMsg(int what, byte[] body, String errMsg) {
        Message message = new Message();
        message.what = what;//用户自定义的消息代码，为了让接收者确定消息内容
        Bundle bundle = new Bundle();
        bundle.putByteArray("data", body);//日期
        bundle.putString("error", errMsg);//错误信息
        message.setData(bundle);
        handler.sendMessage(message);
    }

    @Override
    public void uploadProgress(long bytesRead, long contentLength, boolean done, long networkSpeed) {
        if (mListener != null) {
            Message msg = new Message();
            msg.what = PROGRESS_UPLOAD;
            Bundle bundle = new Bundle();
            bundle.putLong("bytesRead", bytesRead);
            bundle.putLong("contentLength", contentLength);
            bundle.putBoolean("done", done);
            msg.setData(bundle);
            handler.sendMessage(msg);
        }
    }

    @Override
    public void downloadProgress(long bytesRead, long contentLength, boolean done, long networkSpeed) {
        if (mListener != null) {
            Message msg = new Message();
            msg.what = PROGRESS_DOWNLOAD;
            Bundle bundle = new Bundle();
            bundle.putLong("bytesRead", bytesRead);
            bundle.putLong("contentLength", contentLength);
            bundle.putBoolean("done", done);
            msg.setData(bundle);
            handler.sendMessage(msg);
        }
    }

    private void enqueueCall(Request request) {
        OkHttpClient.Builder mBuilder = Progress.progressClient(this);
        sOkHttpClient = mBuilder.connectTimeout(180, TimeUnit.SECONDS).build();
        call = sOkHttpClient.newCall(request);
        call.enqueue(new JsonCallback());
    }

    /**
     * 这里设置连接，读取和写入超时都为三十秒，三十秒已足够判断网络状况。
     * 因为连接超时的计时都是在丢包之后开始计算。
     * xusong,by 2017年12月19日08:58:45
     * @param request
     */
    @Deprecated
    private void enqueueHandlerCall(Request request) {
        OkHttpClient.Builder mBuilder = Progress.progressClient(this);
        sOkHttpClient = mBuilder.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .writeTimeout(30,TimeUnit.SECONDS)
                .build();
        call = sOkHttpClient.newCall(request);
        call.enqueue(new HandlerJsonCallback());
    }

    public void cancelDownload() {
        if (onHttpRunListener != null) {
            onHttpRunListener.onCancle();
        }
        sOkHttpClient.dispatcher().cancelAll();

    }


    private OnHttpRunListener onHttpRunListener;

    /**
     * 监听请求是否取消、完毕、完成和失败
     */
    public void setOnHttpRunListener(OnHttpRunListener onHttpRunListener) {
        this.onHttpRunListener = onHttpRunListener;
    }

    public static abstract class OnHttpRunListener {
        public void onCancle() {

        }

        public void onComplete() {

        }

        public void onSuccess() {

        }

        public void onFailure() {

        }
    }
}