package com.sunwayworld.common.utils.webservice;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sunwayworld.common.base.abstracts.CallBack;
import com.sunwayworld.common.constants.Constants;
import com.sunwayworld.utils.util.L;
import com.sunwayworld.utils.util.NetworkUtils;
import com.sunwayworld.utils.util.RxjavaUtils;
import com.sunwayworld.utils.util.T;

import org.ksoap2.serialization.SoapObject;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * 公共的调用webservice方法,此方法是在Service调用，所以不能出现可视化得劲界面，例如ProgressDialog
 * Created by 99351 on 2018/3/1.
 */

/**
 * =====================================================================================
 *    注意：mProgressDialog的新建所在的线程与CommonWebserviceRequest被调用的地方
 *    的线程是同一线程，而UI更新只能在主线程进行，所以CommonWebserviceRequest最好
 *    只在主线程进行调用
 * =====================================================================================
 */

public class CommonWebserviceRequestOnService<A> {
    private Context mContext;
    private String functionName;
    private String tableName;
    private List<A> mParams;//参数的对象
    private String paramsString;
    private CallBack callBack;

    /**
     * 构造方法 初始化相应的值
     * @param mContext  上下文对象
     * @param tableName  表名（webservice参数之一）
     * @param mParams  代表的实际上是数据集合（webservice参数之二，下面会将此集合转为json数组的字符串）
     * @param functionName  webservice的方法名
     * @param callBack  会掉的对象，用于访问成功后回调到调用处进行下一步操作
     */
    public CommonWebserviceRequestOnService(Context mContext, String tableName, List<A> mParams, String functionName, CallBack callBack) {
        this.mContext = mContext;
        this.tableName = tableName;
        this.mParams = mParams;
        this.functionName = functionName;
        this.callBack = callBack;
        request();
    }

    /**
     * 开始请求
     */
    private void request() {
        if (!NetworkUtils.isConnected()) {
            T.showShort("请检查网络是否正常连接！");
            return;
        }
        startRequest();
    }

    /**
     * 开始请求获得请求的响应
     */
    private void startRequest() {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .disableHtmlEscaping()//跳过特殊符号转码
                .enableComplexMapKeySerialization().serializeNulls()
                .setDateFormat("yyyy-MM-dd HH:mm:ss:SSS").create();
        paramsString = gson.toJson(mParams);
        L.d(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>:"+paramsString);

        RxjavaUtils.ApiObserver(Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                String actionUrl = LIMSWSHelper.NAMESPACE + functionName;
                //传递的参数
                SoapObject soapObject = new SoapObject(LIMSWSHelper.NAMESPACE, functionName);
                soapObject.addProperty("table", tableName);
                soapObject.addProperty("json", paramsString);
                String result = LIMSWSHelper.runActionDirect(soapObject, actionUrl);
                emitter.onNext(result);
                emitter.onComplete();
            }
        }), new RxjavaUtils.ObserverHelper<String>() {
            @Override
            public void onNext(String s) {
                L.d("========================================" + s);
                if (Constants.SUCCESS_STRING.equals(s)) {
                    //成功响应后的回调
                    callBack.notifyHttpDataSetChanged();

                } else {
                    onError(new Throwable(s));
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }
}
