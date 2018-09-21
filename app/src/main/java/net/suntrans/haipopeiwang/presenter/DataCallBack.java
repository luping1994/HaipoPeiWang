package net.suntrans.haipopeiwang.presenter;

/**
 * Created by Looney on 2018/9/7.
 * Des:presenter 的数据回调
 */
public interface DataCallBack<T> {
    void onSuccess(T data);
    void onError(int code,String msg);
}
