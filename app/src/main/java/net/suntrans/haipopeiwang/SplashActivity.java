package net.suntrans.haipopeiwang;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.widget.ImageView;
import android.widget.TableLayout;

import com.bumptech.glide.Glide;

import net.suntrans.haipopeiwang.activity.LoginActivity;

import org.jetbrains.annotations.Nullable;

/**
 * Created by Looney on 2018/8/17.
 * Des:
 */
public class SplashActivity extends BaseActivity {
    @Override
    public int getLayoutResId() {
        return R.layout.activity_splash;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String access_token = App.Companion.getSharedPreferences().getString("access_token", "-1");
        if (access_token == "-1") {
            handler.sendEmptyMessageDelayed(MSG_LOGIN, 1800);
        } else {
            long expires_in = App.Companion.getSharedPreferences().getLong("expires_in", 0l);
            long loginedTime = App.Companion.getSharedPreferences().getLong("currentTimeMillis", 0l);
            long currentTimeMillis = System.currentTimeMillis();

            if (currentTimeMillis - 3600 * 1000 >= (loginedTime + expires_in * 1000)) {    //token过期了
                handler.sendEmptyMessageDelayed(MSG_LOGIN, 1800);
            } else {
                handler.sendEmptyMessageDelayed(MSG_MAIN, 1800);
            }
        }
        ImageView imageView = findViewById(R.id.imgLauncher);
        Glide.with(this)
                .load(R.drawable.splash)
                .into(imageView);
    }


    private static final int MSG_LOGIN = 1;
    private static final int MSG_MAIN = 2;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOGIN:
                    LoginActivity.Companion.start(SplashActivity.this);
                    finish();
                    break;
                case MSG_MAIN:
                    MainActivity.Companion.start(SplashActivity.this);
                    finish();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    public static void main(String[] args) {

        TabLayout tableLayout = null;
        int[] s = {11, 5, 6, 8, 2, 1, 3, 4};
        for (int i :
                s) {
            System.out.print(i + ",");
        }
        bubble_sort(s);
        System.out.println();
        for (int i :
                s) {
            System.out.print(i + ",");
        }
    }

    public static void bubble_sort(int[] arr) {
        int i, j, temp, len = arr.length;
        for (i = 0; i < len - 1; i++)
            for (j = 0; j < len - 1 - i; j++)
                if (arr[j] > arr[j + 1]) {
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
    }
}
