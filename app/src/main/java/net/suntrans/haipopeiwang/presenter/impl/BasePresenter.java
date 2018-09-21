package net.suntrans.haipopeiwang.presenter.impl;

import net.suntrans.haipopeiwang.presenter.IBasePresenter;
import net.suntrans.haipopeiwang.view.IBaseView;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Looney on 2018/9/3.
 * Des:
 */
public class BasePresenter<V extends IBaseView> {
    protected CompositeDisposable mDisposable = new CompositeDisposable();
    /**
     * 绑定的view
     */
    private V mView;

    /**
     * 断开view，一般在onDestroy中调用
     */
    public void detachView() {
        mDisposable.clear();
        this.mView = null;
    }

    public void attachView(V view) {
        this.mView = view;
    }

    public void onError(String msg) {

    }

    /**
     * 是否与View建立连接
     * 每次调用业务请求的时候都要出先调用方法检查是否与View建立连接
     */
    public boolean isViewAttached() {
        return mView != null;
    }

    /**
     * 获取连接的view
     */
    public V getView() {
        return mView;
    }




}
