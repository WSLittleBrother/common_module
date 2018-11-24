package com.sunwayworld.common.utils.retrofit2.http;

import android.util.Log;

import com.sunwayworld.common.constants.HttpConstants;
import com.sunwayworld.common.utils.retrofit2.service.BaseService;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Yanghan on 2017/12/7.
 */

public class HttpApiBase implements Progress.ProgressListener {
    private final static String TAG = HttpApiBase.class.getSimpleName();
    private BaseService baseService;
    private Retrofit retrofit;
    private Progress progress;
    //带进度条的观测者
    private ProgressObserver progressObserver;
    //baseUrl must end in / ：必须以“/”结尾
    private static String baseUrl = HttpConstants.SYSCONFIG_URL + "/";

    private static class sinalInstance {
        static final HttpApiBase instance = new HttpApiBase();
    }

    public static HttpApiBase getInstance() {
        return sinalInstance.instance;
    }

    private HttpApiBase() {
        progress = new Progress(this);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addNetworkInterceptor(new Progress.ProgressInterceptor());
        OkHttpClient client = builder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())//请求的结果转为实体类
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  //适配RxJava2.0, RxJava1.x则为RxJavaCallAdapterFactory.create()
                .build();
        baseService = retrofit.create(BaseService.class);
    }

    @Override
    public void uploadProgress(long bytesRead, long contentLength, boolean done, long networkSpeed) {
        if (progressObserver != null) {
            progressObserver.uploadProgress(bytesRead, contentLength, done, networkSpeed);
        }
    }

    @Override
    public void downloadProgress(long bytesRead, long contentLength, boolean done, long networkSpeed) {
        if (progressObserver != null) {
            progressObserver.downloadProgress(bytesRead, contentLength, done, networkSpeed);
        }
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    /**
     * 装载观察者与被观测者
     */
    public <T> void ApiSubscrbe(Observable observable, ProgressObserver<T> observer) {
        this.progressObserver = observer;
        observable.map(new Function<ResponseBody, T>() {
            @Override
            public T apply(ResponseBody responseBody) throws Exception {
                return (T) progressObserver.handlerData(responseBody);
            }
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 上传数据观测者及被观测者装载方法
     *
     * @param url     全路径url
     * @param builder 通过HttpBuilder.getMultiBuilder
     * @return Observable<ResponseBody> 观测者
     */
    public Observable<ResponseBody> postUploadMultipartData(String url, MultipartBody.Builder builder) {
        RequestBody requestBody = progress.wrapRequestBody(builder.build());
        Observable<ResponseBody> observable = baseService.uploadData(url, requestBody);
        return observable;
    }

    /**
     * 上传数据观测者及被观测者装载方法
     *
     * @param url     全路径url
     * @param builder 通过HttpBuilder.getBuilder
     * @return Observable<ResponseBody> 观测者
     */
    public Observable<ResponseBody> postUploadFormData(String url, FormBody.Builder builder) {
        RequestBody requestBody = progress.wrapRequestBody(builder.build());
        Observable<ResponseBody> observable = baseService.uploadData(url, requestBody);
        return observable;
    }
    /**
     * 下载数据观测者及被观测者装载方法
     * <p>
     * * @param url              全路径url
     *
     * @return Observable<ResponseBody> 被观察者
     */
    public Observable<ResponseBody> getData(String url) {
        return baseService.getData(url);
    }
    /**
     * 上传数据观测者及被观测者装载方法
     *
     * @param url     全路径url
     * @param builder 通过HttpBuilder.getBuilder
     * @return Observable<ResponseBody> 观测者
     */
    public Observable<ResponseBody> getData(String url, FormBody.Builder builder) {
        RequestBody requestBody = progress.wrapRequestBody(builder.build());
        Observable<ResponseBody> observable = baseService.postData(url, requestBody);
        return observable;
    }

    private static Disposable disposable;

    /**
     * 带上传和下载进度的观察者（ProgressObserver）
     */
    public abstract static class ProgressObserver<T> implements Observer<T> {
        /*
        * 网络请求数据处理，异步线程中，
        * return返回的数据发送到onNext中，异常数据发送到onError中
        * */
        public abstract T handlerData(ResponseBody responseBody) throws Exception;
        /*
        * 下载进度条
        * */
        public void downloadProgress(long bytesRead, long contentLength, boolean done, long networkSpeed) {

        }
        /*
         * 上传进度条
         * */
        public void uploadProgress(long bytesRead, long contentLength, boolean done, long networkSpeed) {

        }
        @Override
        public void onSubscribe(Disposable d) {
            disposable = d;
        }
        @Override
        public abstract void onNext(T t);
        @Override
        public abstract void onError(Throwable e);
        @Override
        public void onComplete() {
            Log.d(TAG, "onComplete--->>>下载并接收完成");
        }
    }

    public void cancelDownload() {
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
