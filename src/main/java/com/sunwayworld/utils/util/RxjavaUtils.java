package com.sunwayworld.utils.util;

import android.os.Looper;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by YY007 on 2018/1/10.
 */

public class RxjavaUtils {

    private RxjavaUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static Disposable mDisposable;

    private static Subscription mSubscription;

    /**
     * 装载观察者与被观测者
     */
    public static void ApiObserver(Observable observable, Observer observer) {
        observable.compose(io_main_Observable()).subscribe(observer);
    }

    /**
     * 装载观察者与被观测者
     */
    public static void ApiSubscrbe(Flowable flowable, Subscriber subscriber) {
        flowable.compose(io_main_Flowable()).subscribe(subscriber);
    }

    /**
     * 当被观测者为Observable时调用
     * 装载观察者与被观测者
     * 线程切换
     */
    public static <T> ObservableTransformer<T, T> io_main_Observable() {//compose简化线程
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 当被观测者为Flowable时调用
     * 装载观察者与被观测者
     * 线程切换
     */
    public static <T> FlowableTransformer<T, T> io_main_Flowable() {    //compose简化线程
        return new FlowableTransformer<T, T>() {
            @Override
            public Flowable<T> apply(Flowable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    /**
     * 取消订阅，注：不需要手动去调用，代码执行完成后会自动取消订阅
     */
    public static void dispose() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    /**
     * 取消订阅，注：不需要手动去调用，代码执行完成后会自动取消订阅
     * 如果使用了Rxjava的定时器功能，则需要去手动调用该方法进行取消订阅
     */
    public static void cancel() {
        if (mSubscription != null) {
            mSubscription.cancel();
        }
    }

    /**
     * 封装后的观察者
     */
    public static abstract class ObserverHelper<O> implements Observer<O> {
        @Override
        public void onSubscribe(Disposable d) {
            mDisposable = d;
        }

        @Override
        public abstract void onNext(O o);

        /**
         * 做异常操作才会去调用
         */
        @Override
        public void onError(Throwable throwable) {
            try {
                if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
                    T.showShort("异常：" + throwable.getMessage());
                }
                throwable.printStackTrace();
                CrashUtils.getInstance().uncaughtException(null, throwable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onComplete() {

        }
    }

    /**
     * 封装后的观察者
     */
    public static abstract class SubscriberHelper<S> implements Subscriber<S> {
        @Override
        public void onSubscribe(Subscription s) {
            s.request(Long.MAX_VALUE);
            mSubscription = s;
        }

        @Override
        public abstract void onNext(S o);

        /**
         * 做异常操作才会去调用
         */
        @Override
        public void onError(Throwable throwable) {
            try {
                if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
                    T.showShort("异常：" + throwable.getMessage());
                }
                throwable.printStackTrace();
                CrashUtils.getInstance().uncaughtException(null, throwable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onComplete() {

        }
    }
}
