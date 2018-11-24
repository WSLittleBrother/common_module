package com.sunwayworld.common.utils.webservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sunwayworld.common.R;
import com.sunwayworld.common.base.abstracts.CallBack;
import com.sunwayworld.utils.util.AD;
import com.sunwayworld.utils.util.L;
import com.sunwayworld.utils.util.NetworkUtils;
import com.sunwayworld.utils.util.RxjavaUtils;
import com.sunwayworld.utils.util.T;

import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * 公共的调用webservice方法
 * Created by 99351 on 2018/3/1.
 */

/**
 * =====================================================================================
 *    注意：mProgressDialog的新建所在的线程与CommonWebserviceRequest被调用的地方
 *    的线程是同一线程，而UI更新只能在主线程进行，所以CommonWebserviceRequest最好
 *    只在主线程进行调用
 * =====================================================================================
 */

public class CommonWebserviceRequest {
    private Context mContext;
    private String functionName;
    private Object mParams;//参数的对象
    private String paramsString;
    private ProgressDialog mProgressDialog;
    private CallBack callBack;

    /**
     * 构造方法 初始化相应的值
     * @param mContext  上下文对象
     * @param mParams  代表的实际上是数据集合（webservice参数之二，下面会将此集合转为json数组的字符串）
     * @param functionName  webservice的方法名
     * @param callBack  会掉的对象，用于访问成功后回调到调用处进行下一步操作
     */
    public CommonWebserviceRequest(Context mContext,  Object mParams, String functionName, CallBack callBack) {
        this.mContext = mContext;
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
            callBack.fail();
            return;
        }
        startRequest();
    }

    /**
     * 开始请求获得请求的响应
     */
    private void startRequest() {
        if (mParams != null&&mParams instanceof List){
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .disableHtmlEscaping()//跳过特殊符号转码
                    .enableComplexMapKeySerialization().serializeNulls()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss:SSS").create();
            paramsString = gson.toJson(mParams);
            L.d(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>:"+paramsString);
        }
        mProgressDialog = ProgressDialog.show(mContext, "", mContext.getText(R.string.network_requesting), true, false);
        RxjavaUtils.ApiObserver(Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                String actionUrl = LIMSWSHelper.NAMESPACE + functionName;
                //传递的参数
                SoapObject soapObject = new SoapObject(LIMSWSHelper.NAMESPACE, functionName);
                //下面的参数名要根据实际情况重写
                soapObject.addProperty("content", paramsString);
                String result = LIMSWSHelper.runActionDirect(soapObject, actionUrl);
                JSONObject jsonObject = new JSONObject(result);

                //这里要对result的结果进行解析来判断是否获取数据成功

            }
        }), new RxjavaUtils.ObserverHelper() {
            @Override
            public void onNext(Object o) {
                mProgressDialog.dismiss();


            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mProgressDialog.dismiss();
                L.d("========================================" + e.toString());
                AD.getADBuilder(mContext, "请求数据失败：" + e.getMessage(), "重新获取", "取消", new AD.OnClickEventListener() {
                    @Override
                    public void onClickPositive(DialogInterface dialog, int which) {
                        //第一次访问出错后点击重新获取按钮再次访问服务器
                        startRequest();
                    }

                    @Override
                    public void onClickNegative(DialogInterface dialog, int which) {
                        super.onClickNegative(dialog, which);
                        callBack.fail();

                    }
                }).setCancelable(false).create().show();
            }
        });
    }
}
