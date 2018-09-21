package net.suntrans.haipopeiwang.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.suntrans.haipopeiwang.utils.LogUtil;


/**
 * Created by Looney on 2018/3/8.
 * Des:
 */

public class SensusFragment extends BasedFragment {


    private final String TAG = this.getClass().getSimpleName();

    public static SensusFragment newInstance(String ip, int port, String name) {
        Bundle args = new Bundle();
        args.putString("ip", ip);
        args.putInt("port", port);
        args.putString("name", name);
        SensusFragment fragment = new SensusFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String ip = getArguments().getString("ip");
        int port = getArguments().getInt("port");
        connectToServer(ip,port);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disconnect();
    }

    @Override
    public void onReceive(String content){
        LogUtil.i(TAG,"content"+content);
    }

    @Override
    public void onConnected() {

    }
}
