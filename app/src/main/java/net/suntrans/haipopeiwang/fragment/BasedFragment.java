package net.suntrans.haipopeiwang.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import net.suntrans.haipopeiwang.tcp.TcpHelper;


/**
 * Created by Looney on 2018/3/8.
 * Des:
 */

public abstract class BasedFragment extends Fragment implements TcpHelper.OnReceivedListener {


    protected TcpHelper helper;
    public void connectToServer(String ip, int port) {
        helper = new TcpHelper((AppCompatActivity) getActivity(), ip, port, null);
        helper.setOnReceivedListener(this);
    }

    public void disconnect() {
        if (helper != null)
            helper.unRegister();
        helper = null;
    }


    @Override
    public abstract void onReceive(String content);

    @Override
    public abstract void onConnected();
}
