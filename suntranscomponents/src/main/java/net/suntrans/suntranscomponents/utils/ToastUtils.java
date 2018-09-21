package net.suntrans.suntranscomponents.utils;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;
import android.content.Context;
/**
 * Created by Looney on 2018/9/18.
 * Des:
 */
public class ToastUtils {

    private static Handler handler = new Handler(Looper.getMainLooper());
    private static Toast mToast;

    public static void showToast(final String str,final Context context) {

        if (Thread.currentThread()!= Looper.getMainLooper().getThread()){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String str1 =str;
                    if (TextUtils.isEmpty(str1)) {
                        str1 = "";
                    }
                    if (mToast == null) {
                        mToast = Toast.makeText(context, str1, Toast.LENGTH_SHORT);
                    }

                    mToast.setText(str1);
                    mToast.show();
                }
            });
        }else {
            String str1 =str;
            if (TextUtils.isEmpty(str1)) {
                str1 = "";
            }
            if (mToast == null) {
                mToast = Toast.makeText(context, str1, Toast.LENGTH_SHORT);
            }
            mToast.setText(str1);
            mToast.show();
        }

    }
}
