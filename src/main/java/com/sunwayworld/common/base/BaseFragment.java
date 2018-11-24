package com.sunwayworld.common.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * @author Yanghan 2016/5/12 17:29
 *         <p>
 *         fragment启动时候会先调用setUserVisibleHint
 *         fragment:onAttach->onCreate->onCreateView->onActivityCreated
 *         fragment切换回来后，会再次调用setUserVisibleHint
 * @version V1.0
 * @Description
 */
public abstract class BaseFragment<V, T extends BasePresenter<V>> extends Fragment {
    public final String TAG = BaseFragment.class.getSimpleName();

    // fragment是否显示了
    protected boolean mIsVisible = false;

    public T presenter;

    protected Context context;

    protected boolean isFirst = true;

    private boolean isPrepared = false;

    private boolean isViewPager = false;

    /**
     * 调用该方法可刷新fragment
     */
    public void refreshFragment() {
        if (mIsVisible) {
            loadData();
        } else {
            isFirst = true;
        }
    }

    /**
     * 在这里实现Fragment的刷新
     */
    public void refreshFragment(Bundle bundle) {
        handlerBundle(bundle);
        if (mIsVisible) {
            loadData();
        } else {
            isFirst = true;
        }
    }

    /**
     * 处理bundle数据
     */
    protected void handlerBundle(Bundle bundle) {
        if (presenter != null) {
            presenter.handlerBundle(bundle);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        presenter = onCreatePresenter();
        if (presenter != null) {
            presenter.attachView((V) this);
        }
        //要在handleBundle之前初始化presenter
        if (getArguments() != null) {
            handlerBundle(getArguments());
        }
    }

    /**
     * 在这里实现ViewPager里的Fragment数据的缓加载.
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isViewPager = true;
        if (getUserVisibleHint()) {
            mIsVisible = true;
            onVisible();
        } else {
            mIsVisible = false;
            onInvisible();
        }
    }

    /**
     * 在这里实现普通的Fragment数据的缓加载.
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!isViewPager) {
            if (!hidden) {
                mIsVisible = true;
                onVisible();
            } else {
                mIsVisible = false;
                onInvisible();
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!isViewPager && !mIsVisible) {
            mIsVisible = true;
        }
        isPrepared = true;
        onVisible();
    }

    //隐藏fragment需要做的操作,需要的可以在子类中继承
    protected void onInvisible() {

    }

    //显示fragment需要做的操作
    protected void onVisible() {
        if (!isFirst || !mIsVisible || !isPrepared) {
            return;
        }
        loadData();
        isFirst = false;
    }


    //如若使用MVP模式，则必须实现presenter层
    protected abstract T onCreatePresenter();

    /**
     * 显示时加载数据,需要这样的使用
     * 注意声明 isPrepared，先初始化
     * 生命周期会先执行 setUserVisibleHint 再执行onActivityCreated
     * 在 onActivityCreated 之后第一次显示加载数据，只加载一次
     */
    protected abstract void loadData();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.detachView();
        }
    }
}
