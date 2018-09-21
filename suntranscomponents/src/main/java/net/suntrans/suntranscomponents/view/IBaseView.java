package net.suntrans.suntranscomponents.view;

/**
 * Created by Looney on 2018/9/3.
 * Des:
 */
public interface IBaseView<T> {
    void setPresenter(T presenter);
    void onDataError(int code, String msg);
}
