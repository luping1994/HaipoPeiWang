package net.suntrans.haipopeiwang;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;

import net.suntrans.haipopeiwang.utils.LogUtil;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Looney on 2018/9/21.
 * Des:
 */
public class RouterActivity extends BaseActivity {
    private final String TAG = this.getClass().getSimpleName();

    public static void start(Context context, String fragmentPackageName, Bundle args) {
        Intent starter = new Intent(context, RouterActivity.class);
        starter.putExtra("fragment", fragmentPackageName);
        starter.putExtra("args", args);
        context.startActivity(starter);
    }


    public static class RoomFun {
        public static final String ROOM_DETAIL = "net.suntrans.haipopeiwang.room.detail.RoomDetailFragment";
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_router;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String fragment = getIntent().getStringExtra("fragment");
        LogUtil.e(TAG,fragment);
        try {
            Class clazz = Class.forName(fragment);
            Fragment instance = (Fragment) clazz.newInstance();
            Bundle args = getIntent().getBundleExtra("args");
            instance.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.content,instance).commit();
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }
}
