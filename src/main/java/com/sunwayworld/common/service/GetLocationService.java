package com.sunwayworld.common.service;//package com.sunwayworld.monityj.common.service;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.Binder;
//import android.os.IBinder;
//import android.text.TextUtils;
//
//import com.sunwayworld.utils.util.DeviceUtils;
//import com.sunwayworld.utils.util.L;
//import com.sunwayworld.utils.util.PhoneUtils;
//import com.sunwayworld.utils.util.RxjavaUtils;
//import com.sunwayworld.utils.util.TimeUtils;
//
//import java.sql.SQLException;
//import java.util.concurrent.TimeUnit;
//
//import io.reactivex.Flowable;
//import io.reactivex.functions.Function;
//
///**
// * @author dongjs
// * @description 高德地后台图获取位置信息Service
// * @create at 2017/3/16 10:20
// */
//public class GetLocationService extends Service implements AMapLocationListener {
//    private static final String TAG = "GetLocationService";
//    private MyBinder myBinder = new MyBinder();
//    private AMapLocationClient mLocationClient = null;
//    private AMapLocationClientOption mLocationOption = null;
//    private LocationCallBack callback;
//    private AMapLocation aMapLocation;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        //初始化定位
//        mLocationClient = new AMapLocationClient(getApplicationContext());
//        //设置定位回调监听
//        mLocationClient.setLocationListener(this);
//        setLocatiomOption();
//
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return myBinder;
//    }
//
//    /**
//     * 这里实现记录GPS到数据库
//     * @param isRecordGps
//     */
//    public void setRecordGps(boolean isRecordGps) {
//        this.isRecordGps = isRecordGps;
//        if (isRecordGps) {
////            startCurrentTimeMillis = System.currentTimeMillis();
//            L.d("开始记录GPS信息。。。。。。。。。。");
//            RxjavaUtils.ApiSubscrbe(Flowable.interval(1000*30, TimeUnit.MILLISECONDS).map(new Function() {
//                @Override
//                public String apply(Object o) throws Exception {
//                    /**
//                     * 这里判断是否进行记录Gps到数据库，默认是不记录的
//                     */
//                    L.d("正在保存GPS信息。。。。。。。。。。");
//                    recordGpsInfo(aMapLocation);
//                    return Constants.SUCCESS_STRING;
//                }
//            }).onBackpressureLatest(), new RxjavaUtils.SubscriberHelper<String>() {
//                @Override
//                public void onNext(String s) {
////                L.d("sssssssss----》》》"+s);
//                }
//            });
//        } else {
//            RxjavaUtils.cancel();
//        }
//    }
//
//    private boolean isRecordGps ;
//    public void saveGPSToWebservice(boolean isClickClose){
//      /**
//        //没有关闭Gps的话，那就要开始去关闭了，并且要提交GPS信息到服务器去
//        if (isClickClose){
//            //这里开始提交Gps信息
//            startSubmitGpsInfo();
//        }else{
//            //这里需要判断是有已经提交了的
//            //如果isRecordGps==true，说明根本就没有点击停止Gps，而是程序自己被杀死的
//            //但是，我也要将Gps信息提交到服务器去
//            if (isRecordGps){
//                startSubmitGpsInfo();
//            }
//        }
//
//    }
//
//    private long startCurrentTimeMillis;
//    private void startSubmitGpsInfo(){
//        RxjavaUtils.ApiObserver(Observable.create(new ObservableOnSubscribe() {
//            @Override
//            public void subscribe(ObservableEmitter emitter) throws Exception {
//                long endCurrentTimeMillis = System.currentTimeMillis();
//              DBHelper dbHelper = new DBHelper(GetLocationService.this,DBHelperType.BUS_DBHELPER);
//                List<T_GPS_INFO> t_gps_infos = dbHelper.getT_GpsInfoDAO().queryBuilder().where().between("currentTimeMillis",startCurrentTimeMillis,endCurrentTimeMillis).query();
//                emitter.onNext(t_gps_infos);
//                emitter.onComplete();
//            }
//        }), new RxjavaUtils.ObserverHelper<List<T_GPS_INFO>>() {
//            @Override
//            public void onNext(List<T_GPS_INFO> t_gps_infos) {
//                new CommonWebserviceRequestOnService(GetLocationService.this, "T_SJ_GPSINFO", t_gps_infos, "TongbuData", new HttpCallBack() {
//                    @Override
//                    public void notifyHttpDataSetChanged() {
//                        super.notifyHttpDataSetChanged();
//                        T.showShort("GPS保存成功");
//                        //这里是完成了更新和插入的回调
//                    }
//                });
//            }
//        });
//*/
//    }
//
//
//    @Override
//    public boolean onUnbind(Intent intent) {
//        return super.onUnbind(intent);
//    }
//
//    public void setLocationCallback(LocationCallBack callback) {
//        this.callback = callback;
//    }
//
//    /**
//     * @author dongjs
//     * @description 设置定位参数
//     * @create at 2017/3/16 11:32
//     */
//    private void setLocatiomOption() {
//        //初始化定位参数
//        mLocationOption = new AMapLocationClientOption();
//        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        //设置是否返回地址信息（默认返回地址信息）
//        mLocationOption.setNeedAddress(true);
//        //设置是否只定位一次,默认为false
//        mLocationOption.setOnceLocation(false);
//        //设置是否强制刷新WIFI，默认为强制刷新
//        mLocationOption.setWifiActiveScan(true);
//        //设置是否允许模拟位置,默认为false，不允许模拟位置
//        mLocationOption.setMockEnable(false);
//        //设置定位间隔,单位毫秒,默认为2000ms
//        mLocationOption.setInterval(2000);
//        //给定位客户端对象设置定位参数
//        mLocationClient.setLocationOption(mLocationOption);
//        //启动定位
//        mLocationClient.startLocation();
//    }
//
//    @Override
//    public void onLocationChanged(AMapLocation aMapLocation) {
//        if (aMapLocation != null) {
//            if (aMapLocation.getErrorCode() == 0) {
////                L.d(TAG, "onMyLocationChange 定位成功， lat: " + aMapLocation.getLatitude() + " lon: " + aMapLocation.getLongitude());
//                if (callback != null) {
//                    callback.onLocationChanged(aMapLocation);
//                }
//                this.aMapLocation = aMapLocation;
//
//                //定位成功回调信息，设置相关消息
////                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
////                aMapLocation.getLatitude();//获取纬度
////                aMapLocation.getLongitude();//获取经度
////                aMapLocation.getAccuracy();//获取精度信息
////                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////                Date date = new Date(aMapLocation.getTime());
////                df.format(date);//定位时间
////                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
////                aMapLocation.getCountry();//国家信息
////                aMapLocation.getProvince();//省信息
////                aMapLocation.getCity();//城市信息
////                aMapLocation.getDistrict();//城区信息
////                aMapLocation.getStreet();//街道信息
////                aMapLocation.getStreetNum();//街道门牌号信息
////                aMapLocation.getCityCode();//城市编码
////                aMapLocation.getAdCode();//地区编码
////                aMapLocation.getAoiName();//获取当前定位点的AOI信息
//
//            } else {
//                if (callback != null) {
//                    callback.onLocationFail();
//                }
//                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
////                L.d("AmapError", "location Error, ErrCode:"
////                        + aMapLocation.getErrorCode() + ", errInfo:"
////                        + aMapLocation.getErrorInfo());
//            }
//        } else {
//            if (callback != null) {
//                callback.onLocationFail();
//            }
//        }
//    }
//
//    public class MyBinder extends Binder {
//        public GetLocationService getService() {
//            return GetLocationService.this;
//        }
//    }
//
//    /**
//     * @author dongjs
//     * @description 停止定位
//     * @create at 2017/3/16 11:55
//     */
//    public void stopLocation() {
//        mLocationClient.stopLocation();//停止定位
//    }
//
//    //启动定位
//    public void startLocation() {
//        mLocationClient.startLocation();
//    }
//
//    @Override
//    public void onDestroy() {
//        L.w("销毁Service............");
//        super.onDestroy();
//        RxjavaUtils.cancel();
//        mLocationClient.onDestroy();//销毁定位客户端。
//    }
//
//    public interface LocationCallBack {
//        void onLocationChanged(AMapLocation aMapLocation);
//
//        void onLocationFail();
//    }
//
//    /**
//     * 将GPS信息记录到数据库
//     *
//     * @param aMapLocation
//     */
//    private DBHelper dbHelperBus;
//    private T_GPS_INFO t_gps_info;
//
//    private void recordGpsInfo(final AMapLocation aMapLocation) throws SQLException {
//        if (aMapLocation != null && AppModel.getSysConfig() != null && !TextUtils.isEmpty(AppModel.getSysConfig().getLastLoginUsrNam())) {
//            if (dbHelperBus == null) {
//                dbHelperBus = new DBHelper(GetLocationService.this, DBHelperType.BUS_DBHELPER);
//            }
//            t_gps_info = new T_GPS_INFO();
//            t_gps_info.setLONGITUDE(DoubleUtils.getDecimalPointDouble(aMapLocation.getLongitude(), 4));//经度
//            t_gps_info.setLATITUDE(DoubleUtils.getDecimalPointDouble(aMapLocation.getLatitude(), 4));//纬度
//            t_gps_info.setLOCATION(aMapLocation.getAddress());//地址
//
//            if (TextUtils.isEmpty(PhoneUtils.getIMEI())) {
//                t_gps_info.setDEVICEID(DeviceUtils.getMacAddress());//设备唯一标识IMEI码
//                t_gps_info.setDEVICEIDTYPE("MAC");//设备类型IMEI
//            } else {
//                t_gps_info.setDEVICEID(PhoneUtils.getIMEI());//设备唯一标识IMEI码
//                t_gps_info.setDEVICEIDTYPE("IMEI");//设备类型IMEI
//            }
//            t_gps_info.setRECORDTIME(TimeUtils.millis2String(System.currentTimeMillis()));//GPS定位的时间
//            t_gps_info.setLASTUSER(AppModel.getSysConfig().getLastLoginUsrNam());
//            t_gps_info.setTASK_NUM(task_num);
//            dbHelperBus.getT_GpsInfoDAO().createOrUpdate(t_gps_info);
//        }
//
//    }
//
//    private String task_num;
//    public void setCurrentTask_num(String task_num){
//        this.task_num =task_num;
//    }
//}
