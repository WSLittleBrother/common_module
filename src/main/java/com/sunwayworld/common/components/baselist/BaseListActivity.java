package com.sunwayworld.common.components.baselist;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sunwayworld.common.R;
import com.sunwayworld.common.base.BaseAppCompatActivity;
import com.sunwayworld.common.adapter.BaseSelectBeen;
import com.sunwayworld.common.base.BasePresenter;
import com.sunwayworld.common.components.adapter.BaseRecyclerViewAdapter;
import com.sunwayworld.ui.widget.MyDecoration;
import com.sunwayworld.utils.util.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 99351 on 2018/6/7.
 */

public abstract class BaseListActivity<V,T extends BasePresenter<V>> extends BaseAppCompatActivity<V, T> {
    /**
     * 下拉刷新的容器、ReyclerView、和Adapter
     */
    private SwipeRefreshLayout mSwipeRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected BaseRecyclerViewAdapter mBaseRecyclerViewAdapter;

    protected MyDecoration decoration;
    /**
     * 是否开始模拟点击，如果设置的话默认进入列表会选中第一个，否则的话进入列表会没有item被选中
     */
    private boolean isPerformClick;
    /**
     * 是否打开了下拉刷新功能
     */
    protected boolean isEnableRefresh = true;
    /**
     * 上一个被点击的item的位置
     */
    private int mPreviousPosition = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(getLayoutId());

        initView();
        initAdapter();
        initListener();
    }

    private void initView(){
        mSwipeRefreshLayout = findViewById(R.id.swipeLayout);
        mRecyclerView = findViewById(R.id.recyclerview);
    }

    /**
     * 这个方法必须在super.onCreate(savedInstanceState);之前被调用，否则无效
     * @param performClick
     */
    public void setPerformClick(boolean performClick) {
        isPerformClick = performClick;
    }

    /**
     * RecyclerView的Adapter的初始化过程
     */
    private void initAdapter() {
        //newAdapter()是一个抽象方法，在子类实现的时候返回Adapter对象
        mBaseRecyclerViewAdapter = newAdapter();

        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mBaseRecyclerViewAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mRecyclerView.setAdapter(mBaseRecyclerViewAdapter);
        decoration = new MyDecoration(this, MyDecoration.VERTICAL_LIST);
        mRecyclerView.addItemDecoration(decoration);

        if (isPerformClick){
            performClickPosition(0);
        }
    }

    //获取布局文件
    protected abstract int getLayoutId();

    /**
     * 定义抽象方法，获得Adapter对象
     *
     * @return Adapter对象
     */
    protected abstract BaseRecyclerViewAdapter newAdapter();

    /**
     * 供子类在初始化的时候调用,控制是否允许下拉刷新,在任何时候只要调用了此方法就不能再进行下拉刷新了
     *
     * @param isEnableRefresh 是否能够下拉刷新
     */
    protected void enableRefresh(boolean isEnableRefresh) {
        this.isEnableRefresh = isEnableRefresh;
        mSwipeRefreshLayout.setEnabled(isEnableRefresh);
    }

    /**
     * 这个方法是在开始下拉刷新的时候被使用
     *
     * @param isRefresh 是否是通过下拉刷新来的，为false说明是上拉加载来的
     * @param data      下拉或上拉之后获得的数据
     */
    protected void setData(boolean isRefresh, List data) {
        final int size = data == null ? 0 : data.size();
        List mDatas = new ArrayList();
        for (Object item : data) {
            mDatas.add(new BaseSelectBeen<>(item));
        }

        if (isRefresh) {
            mBaseRecyclerViewAdapter.setNewData(mDatas);
        } else {
            if (size > 0) {
                mBaseRecyclerViewAdapter.addData(mDatas);
            }
        }
        //停止刷新手势
        mSwipeRefreshLayout.setRefreshing(false);

    }

    /**
     * 这个方法是在关闭下拉刷新的时候使用
     *
     * @param data 设置的数据
     */
    protected void setData(List data) {
        int size = data == null ? 0 : data.size();
        List mDatas = new ArrayList();
        for (Object item : data) {
            mDatas.add(new BaseSelectBeen<>(item));
        }

        if (size > 0) {
            mBaseRecyclerViewAdapter.setNewData(mDatas);
        }
    }

    protected List getData() {
        List mData = new ArrayList();
        BaseSelectBeen mItem;
        for (Object item : mBaseRecyclerViewAdapter.getData()) {
            mItem = (BaseSelectBeen) item;
            mData.add(mItem.getData());
        }
        return mData;
    }

    protected int getItemPosition(Object item){
        return getData().indexOf(item);
    }

    /**
     * 这个方法是在下拉刷新的时候供子类去重写，子类重写的方法里面应该是获取数据的内容
     * 然后再调用上面的setData(boolean isRefresh, List data)方法进行列表的刷新
     */
    public void refresh() {
        L.d("这里是刷新的方法，供子类去重写");
    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        /**
         * RecyclerView的下拉刷新的监听
         */
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //这里的refresh（）是个空的方法，之所以不将其变为抽象方法是为了开发时不希望
                //下拉刷新，那就不需要在子类出现refresh（）的方法了
                refresh();
            }
        });

        /**
         * RecyclerView的item点击事件的监听
         */
        mBaseRecyclerViewAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BaseSelectBeen item = (BaseSelectBeen) mBaseRecyclerViewAdapter.getData().get(position);
                //处于可选状态的时候点击事件变为对数据源的操作，同时刷新当前点击的item
                if (mBaseRecyclerViewAdapter.isSelectMode()) {
                    //这里进行模拟点击的过滤，如果是模拟点击那么就拦截下来
                    if (!view.isPressed()) {
                        //真正来说对模拟点击事件的触发只存在一种情况，那就是刷新了列表的数据之后要给第一个item进行模拟点击的触发
                        ((BaseSelectBeen) mBaseRecyclerViewAdapter.getData().get(position)).setCurrentPositon(true);
                        return;
                    }
                    //给item重新标记选中状态
                    ((BaseSelectBeen) mBaseRecyclerViewAdapter.getData().get(position)).setSelect(!item.isSelect());
//                    mBaseRecyclerViewAdapter.notifyItemChanged(position);
                    mBaseRecyclerViewAdapter.notifyDataSetChanged();
                } else {
                    //处于非可选状态
                    //1.重新定义当前被点击的项，并刷新这一项的布局
                    //2.重新定义上一次被点击的项，并刷新该项的布局
                    for (Object b : mBaseRecyclerViewAdapter.getData()){
                        BaseSelectBeen been = (BaseSelectBeen) b;
                        been.setCurrentPositon(false);
                    }
                    ((BaseSelectBeen) mBaseRecyclerViewAdapter.getData().get(position)).setCurrentPositon(true);
                    mBaseRecyclerViewAdapter.notifyDataSetChanged();
                    mPreviousPosition = position;
                    onClickItem(item.getData());
                }
            }
        });

    }

    protected void performClickPosition(final int clickPosition) {
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mRecyclerView.findViewHolderForAdapterPosition(clickPosition) != null) {
                    mRecyclerView.findViewHolderForAdapterPosition(clickPosition).itemView.performClick();
                }else{
                    onClickItem(null);
                }
            }
        },50);
    }

    /**
     * 这里是进入选择模式的入口，判断的依据是传入的字段是否为空
     */
    public void toggleMode() {
        //默认只要调用了此方法那就进入选择模式
        mBaseRecyclerViewAdapter.toggleMode();
    }

    /**
     * 设置可选模式
     * @param isSelectModel
     */
    public void setModel(boolean isSelectModel){
        mBaseRecyclerViewAdapter.setSelectMode(isSelectModel);
    }

    /**
     * @return  list 选中的数据
     */
    protected List confirmSelectedItems(){
        return mBaseRecyclerViewAdapter.getSelectedItems();
    }
    protected abstract  void onClickItem(Object item);
}
