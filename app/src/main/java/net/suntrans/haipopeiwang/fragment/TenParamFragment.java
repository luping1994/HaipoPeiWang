package net.suntrans.haipopeiwang.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import net.suntrans.haipopeiwang.R;
import net.suntrans.haipopeiwang.tcp.TcpHelper;
import net.suntrans.haipopeiwang.utils.Converts;
import net.suntrans.haipopeiwang.utils.LogUtil;
import net.suntrans.haipopeiwang.utils.UiUtils;
import net.suntrans.looney.widgets.SwitchButton;


/**
 * Created by Looney on 2018/3/7.
 * Des:
 */

public class TenParamFragment extends Fragment implements TcpHelper.OnReceivedListener, View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();

    private String openAllOrder = "AB 68 41 00 00 00 00 00 03 7B 00 FF 03 FF 03 07 04 0D 0A";
    private String closeAllOrder = "AB 68 41 00 00 00 00 00 03 7B 00 FF 03 00 00 06 F5 0D 0A";

    String url ="http://ota.suntrans-cloud.com/";

    private String readAllStateOrder = "AB 68 41 00 " +
            "00 00 00 00 " +
            "01 " +
            "42 00" +//过流
            "00 00 00 00" +
            "43 00" +//欠压
            "00 00 00 00" +
            "44 00" +//过压
            "00 00 00 00" +
            "49 00" +
            "00 00 00 00" +
            "9a d9" +
            "0d 0a";
    String shinengOrder = "";

    private ToggleButton quanguangshineng;
    private SwitchButton anjianshineng;
    private SwipeRefreshLayout refreshlayout;
    private TextView guoyaTx;
    private TextView guoliuSingle;
    private TextView guoliuAll;
    private TextView qianyaTx;
    private TextView getVersion;
    private TextView getWifi;
    private TextView update;
    private TextView wifi;
    private TextView version;
    private EditText updateTitle;
    
    public static TenParamFragment newInstance(String ip, int port) {

        Bundle args = new Bundle();
        args.putString("ip", ip);
        args.putInt("port", port);
        TenParamFragment fragment = new TenParamFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate( R.layout.fragment_ten_param, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        String ip = getArguments().getString("ip");
        int port = getArguments().getInt("port");
        connectToServer(ip, port);

        quanguangshineng = view.findViewById(R.id.quanguangshineng);
        anjianshineng = view.findViewById(R.id.anjianshineng);
        refreshlayout = view.findViewById(R.id.refreshlayout);
        guoyaTx = view.findViewById(R.id.guoya);
        guoliuSingle = view.findViewById(R.id.guoliuSingle);
        guoliuAll = view.findViewById(R.id.guoliuAll);
        qianyaTx = view.findViewById(R.id.qianya);
        getVersion = view.findViewById(R.id.getVersion);
        getWifi = view.findViewById(R.id.getWifi);
        update = view.findViewById(R.id.update);
        updateTitle = view.findViewById(R.id.updateTitle);
        wifi = view.findViewById(R.id.wifi);
        version = view.findViewById(R.id.version);

        quanguangshineng.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    helper.binder.sendOrder(openAllOrder);
                } else {
                    helper.binder.sendOrder(closeAllOrder);

                }
            }
        });

        refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshlayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });

        guoyaTx.setOnClickListener(this);
        guoliuSingle.setOnClickListener(this);
        guoliuAll.setOnClickListener(this);
        qianyaTx.setOnClickListener(this);

        anjianshineng.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    shinengOrder = "4100 00000000" + "03" + "4900" + "ff07 0000";
                    shinengOrder = shinengOrder.replace(" ", "");
                    byte[] bytes = Converts.HexString2Bytes(shinengOrder);
                    String crc = Converts.GetCRC(bytes, 0, bytes.length);
                    shinengOrder = "ab68" + shinengOrder + crc + "0d0a";
                    helper.binder.sendOrder(shinengOrder);
                } else {
                    shinengOrder = "4100 00000000" + "03" + "4900" + "0000 0000";
                    shinengOrder = shinengOrder.replace(" ", "");
                    byte[] bytes = Converts.HexString2Bytes(shinengOrder);
                    String crc = Converts.GetCRC(bytes, 0, bytes.length);
                    shinengOrder = "ab68" + shinengOrder + crc + "0d0a";
                    helper.binder.sendOrder(shinengOrder);
                }
            }
        });

        getVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String order = "AB68410000000000010E0000000000EE440D0A";
                helper.binder.sendOrder(order);
            }
        });
        getWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String order = "AB68410000000000010F0000000000EF950D0A";
                helper.binder.sendOrder(order);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.tips_update)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendUpdateCmd();
                            }
                        })
                        .setNegativeButton(R.string.cancel,null).create().show();

            }
        });
    }

    private void sendUpdateCmd() {
        String s = updateTitle.getText().toString();
        s =url+s;
        s = s.replace(" ","");

        String order = "41000000000003030003000000 006A";
        String length =getStringFormat(Integer.toHexString(s.length()),4);

        String urlHex = Converts.strToASCIIHex(s);

        order = Converts.getOrderWithCrc(order+length+urlHex);

        helper.binder.sendOrder(order);
    }

    private Handler handler = new Handler();
    public String toHexStringSetp2(String s) {
        String str = "";
        for (int i = 0; i < s.length()-2; i += 2) {
            String sub = s.substring(i, i + 2);
            int ch = Integer.valueOf(sub);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    @NonNull
    private String getStringFormat(String str, int targetLength) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < targetLength - str.length(); i++) {
            sb.append("0");
        }
        sb.append(str);
        return sb.toString();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
        disconnect();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getData() {

        helper.binder.sendOrder(readAllStateOrder);
    }

    private TcpHelper helper;

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
    public void onReceive(String content) {

        //aa684100df0b000002 0e00 67000000 00020d0a
//
        //aa 68 41 00 df 0b 00 00
        // 02
        // 42 00
        // 3c 00 c8 00
        // 43 00
        // 32 00 00 00
        // 44 00
        // 2c 01 00 00
        // f9 42 0d 0a 总数居
//        aa684100 df0b0000 02 4200 3c0 0c800430032000000 4400 2c01 0000 4900 0000 00 00 f942 0d0a
        try {
            LogUtil.i(TAG, content);
            if (content.equals("与服务器连接失败,重连中...") || content.equals("发送失败") || content.equals("连接中断")) {
                UiUtils.showToast(content);
                return;
            }
//            if (!content.substring(16, 18).equals("02") || !content.substring(16, 18).equals("04")||!content.substring(16, 18).equals("05")) {
//                return;
//            }

//            if (!content.substring(0, 8).equals("aa684100")) {
//                return;
//            }
            //aa6800410808000002 0e00d507000000be0d0a
            if (content.substring(18, 22).equals("0e00")) {
                //aa6800410808000002 0e00 d5070000 00be0d0a
                String substring = content.substring(22, 30);
                String s = Converts.reverse32HexString(substring);
                int i = Integer.parseInt(s, 16);
                version.setText(i+"");
                return;
            }
            //aa68410008080000020f003600000000970d0a
            if (content.substring(18, 22).equals("0f00")) {
                String substring = content.substring(22, 30);
                String s = Converts.reverse32HexString(substring);
                int i = Integer.parseInt(s, 16);
                wifi.setText(i+"");
                return;
            }

            refreshlayout.setRefreshing(false);

            if (!content.substring(18,22).equals("4200")){
                return;
            }

            String guoliuzong = content.substring(22, 26);
            String guoliuDan = content.substring(26, 30);

            String qianya = content.substring(34, 38);
            String guoya = content.substring(46, 50);
            String shineng = content.substring(58, 62);
            shineng = shineng.toLowerCase();

            guoliuDan = reserverHexString(guoliuDan);
            guoliuzong = reserverHexString(guoliuzong);
            qianya = reserverHexString(qianya);
            guoya = reserverHexString(guoya);

            guoliuDan = Integer.parseInt(guoliuDan, 16) / 10 + "";
            guoliuzong = Integer.parseInt(guoliuzong, 16) + "";

            qianya = Integer.parseInt(qianya, 16) + "";
            guoya = Integer.parseInt(guoya, 16) + "";


            guoliuAll.setText(guoliuzong + "");
            guoliuSingle.setText(guoliuDan + "");
            qianyaTx.setText(qianya + "");
            guoyaTx.setText(guoya + "");
            if (shineng.equals("ff07")) {
                anjianshineng.setChecked(true);
            } else {
                anjianshineng.setChecked(false);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }, 500);
    }

    public String reserverHexString(String hexString) {
        if (hexString.length() != 4) {
            return null;

        }
        return hexString.substring(2, 4) + hexString.substring(0, 2);
    }


    private String guoliuValue = "";
    private String guoyaValue = "";
    private String qianyaValue = "";
    private String settingOrder;

    @Override
    public void onClick(final View v) {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getContext());
//        final EditText editText = builder.getEditText();
        builder.setInputType(InputType.TYPE_CLASS_NUMBER);
        String order = "";
        switch (v.getId()) {
            case R.id.guoliuAll:
                builder.setTitle("总过流");
                builder.setPlaceholder("0~60A");
                builder.setDefaultText(guoliuAll.getText().toString());
                break;
            case R.id.guoliuSingle:
                builder.setTitle("单通道过流");
                builder.setPlaceholder("0~20A");
                builder.setDefaultText(guoliuSingle.getText().toString());

                break;
            case R.id.guoya:
                builder.setTitle("过压");
                builder.setPlaceholder("280~360V");
                builder.setDefaultText(guoyaTx.getText().toString());

                break;
            case R.id.qianya:
                builder.setTitle("欠压");
                builder.setPlaceholder("0~150");
                builder.setDefaultText(qianyaTx.getText().toString());

                break;
        }

        builder.addAction(R.string.cancel, new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                dialog.dismiss();
            }
        });
        builder.addAction(R.string.ok, new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                dialog.dismiss();
                String s =builder.getEditText().toString();
                if (!s.matches("[0-9]+")) {
                    UiUtils.showToast("请输入数字");
                    return;
                }
                switch (v.getId()) {
                    case R.id.guoliuAll:
                        guoliuAll.setText(s);
                        break;
                    case R.id.guoliuSingle:
                        guoliuSingle.setText(s);
                        break;
                    case R.id.guoya:
                        guoyaTx.setText(s);
                        break;
                    case R.id.qianya:

                        qianyaTx.setText(s);
                        break;
                }
                setSettingParam();
                sendParam(v.getId());
            }
        });

        builder.create().show();

    }

    private void sendParam(int id) {

        settingOrder = "4100 0000 0000" +
                "03";
        switch (id){
            case R.id.guoliuAll:
            case R.id.guoliuSingle:
                settingOrder = settingOrder+ "4200" + guoliuValue;
                break;
            case R.id.guoya:
                settingOrder = settingOrder+ "4400" + guoyaValue;
                break;
            case R.id.qianya:
                settingOrder = settingOrder+ "4300" + qianyaValue;
                break;
        }
        settingOrder = settingOrder.replace(" ", "");
        byte[] bytes = Converts.HexString2Bytes(settingOrder);
        String crc = Converts.GetCRC(bytes, 0, bytes.length);
        settingOrder = "ab68" + settingOrder + crc + "0d0a";
        if (helper.binder != null)
            helper.binder.sendOrder(settingOrder);

    }


    private void setSettingParam() {

        String guoliuDan = guoliuSingle.getText().toString();
        String guoliuALl = guoliuAll.getText().toString();
        String guoyaTemp = guoyaTx.getText().toString();
        String qianyaTemp = qianyaTx.getText().toString();

        guoliuDan = Integer.valueOf(guoliuDan) * 10 + "";
        guoliuDan = formatValue(guoliuDan);


        guoliuALl = formatValue(guoliuALl);


        guoliuValue = guoliuALl + guoliuDan;
        qianyaValue = formatValue(qianyaTemp) + "0000";
        guoyaValue = formatValue(guoyaTemp) + "0000";


    }

    private String formatValue(String value) {
        value = Integer.toHexString(Integer.valueOf(value));
        value = Converts.paddingHexString(value, 4);
        value = reserverHexString(value);
        return value;
    }

}
