package net.suntrans.haipopeiwang.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import net.suntrans.haipopeiwang.App;
import net.suntrans.haipopeiwang.BaseActivity;
import net.suntrans.haipopeiwang.R;
import net.suntrans.haipopeiwang.utils.LogUtil;
import net.suntrans.haipopeiwang.utils.UiUtils;

import io.fogcloud.sdk.easylink.api.EasyLink;
import io.fogcloud.sdk.easylink.helper.EasyLinkCallBack;
import io.fogcloud.sdk.easylink.helper.EasyLinkParams;

/**
 * Created by Looney on 2018/3/2.
 * Des:
 */

public class AutoLinkActivity extends BaseActivity {


    public static void start(Context context) {
        Intent starter = new Intent(context, AutoLinkActivity.class);
        context.startActivity(starter);
    }
    private EasyLink easyLink;
    private AlertDialog dialog;

    private View start;
    private EditText ssid;
    private EditText passwordTx;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_autolink;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackArrowEnable(true);
        setToolBarTitle(getString(R.string.title_auto_link));
        easyLink = new EasyLink(this);
        start = findViewById(R.id.start);
        ssid = findViewById(R.id.ssid);
        passwordTx = findViewById(R.id.password);
        listenwifichange();

        dialog = new AlertDialog.Builder(AutoLinkActivity.this)
                .setCancelable(false)
                .create();
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(ssid.getText().toString()) || TextUtils.isEmpty(passwordTx.getText().toString())) {
                    UiUtils.showToast(getString(R.string.tips_contain_ssid_password));
                    return;
                }
                start.setEnabled(false);
                startEasyLink(ssid.getText().toString(), passwordTx.getText().toString());
                View view = LayoutInflater.from(AutoLinkActivity.this)
                        .inflate(R.layout.item_loading, null, false);
                view.findViewById(R.id.close)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                stopEasyLink();
                                start.setEnabled(true);
                                dialog.dismiss();
                            }
                        });
                dialog.setView(view);
                dialog.show();
            }
        });
    }

    int settedCount = 0;

    private void startEasyLink(String ssid, String password) {

        EasyLinkParams params = new EasyLinkParams();


        params.ssid = ssid;
        params.password = password;
        params.runSecond = 60000;
        params.sleeptime = 50;


        App.Companion.getSharedPreferences()
                .edit()
                .putString(ssid, password)
                .apply();

//        easylinkP2P.startEasyLink(params, new EasyLinkCallBack() {
//            @Override
//            public void onSuccess(int code, String message) {
//                UiUtils.showToast(message);
//
//            }
//
//            @Override
//            public void onFailure(int code, String message) {
//
//            }
//        });
        easyLink.startEasyLink(params, new EasyLinkCallBack() {
            @Override
            public void onSuccess(int code, String message) {
                settedCount++;
                LogUtil.i("AutoLinkActivity",message);
            }

            @Override
            public void onFailure(int code, String message) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        stopEasyLink();
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    private void stopEasyLink() {
//
        easyLink.stopEasyLink(new EasyLinkCallBack() {
            @Override
            public void onSuccess(int code, String message) {

            }

            @Override
            public void onFailure(int code, String message) {

            }
        });

//        easylinkP2P.stopEasyLink(new EasyLinkCallBack() {
//            @Override
//            public void onSuccess(int code, String message) {
//
//            }
//
//            @Override
//            public void onFailure(int code, String message) {
//
//            }
//        });
    }

    private void listenwifichange() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (info.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                    ssid.setText(easyLink.getSSID());
                    String password = App.Companion.getSharedPreferences().getString(easyLink.getSSID(), "");
                    passwordTx.setText(password);
                }
            }
        }
    };


}
