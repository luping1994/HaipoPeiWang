package net.suntrans.haipopeiwang.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.suntrans.haipopeiwang.R;
import net.suntrans.haipopeiwang.tcp.TcpHelper;
import net.suntrans.haipopeiwang.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Looney on 2018/3/8.
 * Des:
 */

public class SLockFragment extends BasedFragment implements View.OnClickListener {
    private final String TAG = this.getClass().getSimpleName();
    private Button lock;
    private Button getParams;
    private Button setTempPassword;
    private Button addPassword;
    private TextView txt;

    private static Map<String, String> LockCode = new HashMap<>();
    private static Map<String, String> key = new HashMap<>();
    {
        LockCode.put("00","上线");
        LockCode.put("01","读指令");
        LockCode.put("02","读反馈，定时上报");
        LockCode.put("03","写指令");
        LockCode.put("04","写反馈");
        LockCode.put("05","触发上报");
        LockCode.put("06","升级");
        LockCode.put("07","错误");
        LockCode.put("08","报警上报");
        LockCode.put("09","生产信息读取及返回");
        LockCode.put("0a","生产信息写入及返回");
        LockCode.put("0b","安装信息读取及返回");
        LockCode.put("0c","安装信息写入及返回");
        LockCode.put("0d","心跳");
        LockCode.put("0e","复位");
        LockCode.put("0f","恢复出厂设置");

        LockCode.put("20","开锁上报");
        LockCode.put("21","写私有数据");
        LockCode.put("22","远程开锁返回");
        LockCode.put("23","远程关锁返回");

        key.put("v","微瓴程序版本");
        key.put("code","操作命令");
        key.put("type","设备类型");
        key.put("msg","返回代号");
        key.put("timestamp","时间");
        key.put("signal","信号强度");
        key.put("memory","wifi剩余内存");
        key.put("wifi","wifi固件版本");
        key.put("mcu","MCU固件版本");
        key.put("hardware","硬件版本");
        key.put("addr","设备地址");
    }

    public static SLockFragment newInstance(String ip, int port, String name) {
        Bundle args = new Bundle();
        args.putString("ip", ip);
        args.putInt("port", port);
        args.putString("name", name);
        SLockFragment fragment = new SLockFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lock, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        lock = view.findViewById(R.id.send);
        getParams = view.findViewById(R.id.getParams);
        setTempPassword = view.findViewById(R.id.setTempPassword);
        addPassword = view.findViewById(R.id.addPassword);
        txt = view.findViewById(R.id.txt);

        getParams.setOnClickListener(this);
        setTempPassword.setOnClickListener(this);
        setTempPassword.setOnClickListener(this);
        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "{\"v\":\"2\",\"code\":\"21\",\"type\":\"9200\",\"addr\":0,\"list\":[{\"k\":\"remote\",\"v\":\"open\"}]}";
                helper.binder.sendOrderNotHex(s);
            }
        });
        String ip = getArguments().getString("ip");
        int port = getArguments().getInt("port");
        connectToServer2(ip, port);
    }

    public void connectToServer2(String ip, int port) {
        helper = new TcpHelper((AppCompatActivity) getActivity(), ip, port, null,"1");
        helper.setOnReceivedListener1(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disconnect();
    }

    @Override
    public void onReceive(String content) {
        LogUtil.i(TAG, "content:" + content);

        try {
            JSONObject jsonObject = new JSONObject(content);
            String code = jsonObject.getString("code");
            if(code=="02"){
                JSONArray list = jsonObject.getJSONArray("list");
                list.get(1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addPassword:
                break;
            case R.id.setTempPassword:
                break;
            case R.id.getParams:
//            {"v":"2","code":"01","type":"9200","addr":0,"list":[{"k":"wifi","v":"0"},{"k":"hardware","v":"0"},{"k":"addr","v":"0"}]}
                String order="{\"v\":\"2\",\"code\":\"01\",\"type\":\"9200\",\"addr\":0,\"list\":[{\"k\":\"wifi\",\"v\":\"0\"}]}";


                helper.binder.sendOrderNotHex(order);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                }).start();
                break;
        }
    }
}
