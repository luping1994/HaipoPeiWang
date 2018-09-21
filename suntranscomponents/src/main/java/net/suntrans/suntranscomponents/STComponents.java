package net.suntrans.suntranscomponents;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.Context;

import com.blankj.utilcode.util.Utils;

import net.suntrans.suntranscomponents.store.RetrofitHelper;

/**
 * Created by Looney on 2018/9/18.
 * Des:
 */
public class STComponents {
    //
//    private static STComponents stComponents ;
//    public static STComponents getInstance()
//    {
//
//        STComponents mInstance = stComponents;
//        if (stComponents == null)
//        {
//            synchronized (STComponents.class)
//            {
//                mInstance = stComponents;
//                if (mInstance == null)
//                {
//                    mInstance = new STComponents();
//                    stComponents = mInstance;
//                }
//            }
//        }
//
//        return mInstance;
//    }
    private static Application application;
    private static SharedPreferences sharedPreferences;

    /**
     * 初始化三川components
     * @param application
     * @param host 网络请求主域名
     * @param sharedPreferences
     */
    public static void init(Application application, String host, String sharedPreferences) {
        STComponents.application = application;
        STComponents.sharedPreferences = application.getSharedPreferences(sharedPreferences, Context.MODE_PRIVATE);
        RetrofitHelper.BASE_URL2 = host;
        Utils.init(application);
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public static Application getApplication() {
        return application;
    }
}
