package net.suntrans.haipopeiwang.mdns.api;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;


import net.suntrans.haipopeiwang.mdns.helper.CommonFunc;
import net.suntrans.haipopeiwang.mdns.helper.MDNSErrCode;
import net.suntrans.haipopeiwang.mdns.helper.SearchDeviceCallBack;
import net.suntrans.haipopeiwang.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

//import android.util.Log;

public class MDNS {
    private String TAG = "---MiCOmDNS---";

    /**
     * 控制的参数
     */
    private CommonFunc comfunc = new CommonFunc();
    // 线程是否已经启动
    private boolean isWorking = false;
    private WifiManager wifiManager = null;
    private Map<String, JSONObject> jsonmap = null;
    // 发现设备的message.what
    private final int _ADDDEVICE = 1002;
    private final int _REMOVEDEVICE = 1003;

    /**
     * 接收的信息
     */
    private Context mContext;
    private SearchDeviceCallBack msearchdevcb;

    /**
     * callback相关
     */
    private JSONArray array;// callback的数组


    private String mserviceName;
    private JmDNS jmdns;
    private SampleListener slistener;
    private WifiManager.MulticastLock lock;


    public MDNS(Context context) {
        this.mContext = context;
    }

    /**
     * Start mDNS to search my devices.
     *
     * @param serviceName serviceName
     * @param searchdevcb searchdevcb
     */
    public void startSearchDevices(String serviceName,
                                   SearchDeviceCallBack searchdevcb) {
        if (comfunc.checkPara(serviceName)) {
            if (null != mContext)
                startMdnsService(serviceName, searchdevcb);
            else {
                comfunc.failureCBmDNS(MDNSErrCode.CONTEXT_CODE, MDNSErrCode.CONTEXT, searchdevcb);
            }
        } else {
            comfunc.failureCBmDNS(MDNSErrCode.EMPTY_CODE, MDNSErrCode.EMPTY, searchdevcb);
        }
    }

    /**
     * Stop search devices.
     *
     * @param searchdevcb searchdevcb
     */
    public void stopSearchDevices(SearchDeviceCallBack searchdevcb) {
        stopMdnsService(searchdevcb);
        stopMDNS();
    }

    private void startMdnsService(String serviceName, final SearchDeviceCallBack searchdevcb) {
        mserviceName = serviceName;
        msearchdevcb = searchdevcb;

        // 如果还没有启动，则启动，否则callback
        if (!isWorking) {
            isWorking = true;

            if (wifiManager == null)
                wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);

            startMDNS();
//            // 开始线程，每3秒关掉再打开，并发送信息给前端
//            new Thread() {
//                @Override
//                public void run() {
//                    while (isWorking) {
//                        try {
//                            sendCallBack();
//                            Thread.sleep(1000 *2);
////							Thread.sleep(100);
////							Thread.sleep(1000 * 1);
//                        } catch (InterruptedException e) {
//                            comfunc.successCBmDNS(MDNSErrCode.EXCEPTION_CODE, e.getMessage(), searchdevcb);
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }.start();
        } else {
            comfunc.failureCBmDNS(MDNSErrCode.BUSY_CODE, MDNSErrCode.BUSY, searchdevcb);
        }
    }

    private void stopMdnsService(SearchDeviceCallBack searchdevcb) {
        if (isWorking) {
            isWorking = false;
            comfunc.successCBmDNS(MDNSErrCode.SUCCESS_CODE, MDNSErrCode.SUCCESS, searchdevcb);
        } else {
            comfunc.failureCBmDNS(MDNSErrCode.CLOSED_CODE, MDNSErrCode.CLOSED, searchdevcb);
        }
    }

    private void startMDNS() {
        new Thread() {
            @Override
            public void run() {


                jmdns = null;
                slistener = null;

                InetAddress intf = null;

                array = new JSONArray();

                if (jsonmap == null)
                    jsonmap = new ConcurrentHashMap<String, JSONObject>();


                jsonmap.clear();
                try {
                    boolean jmdnsTag = true;
                    while (jmdnsTag) {
                        if (intf != null && jmdns != null) {
                            jmdnsTag = false;

                            lock = wifiManager.createMulticastLock("mylock");
                            lock.setReferenceCounted(true);
                            lock.acquire();

                            slistener = new SampleListener();
                            jmdns.addServiceListener(mserviceName, slistener);

                        } else {
                            if (intf == null) {
                                intf = getLocalIpAddress(mContext);
                                LogUtil.i("MDNS", intf.toString());
                            }
                            if (jmdns == null) {
                                String hostname = InetAddress.getByName(intf.getHostName()).toString();
                                jmdns = JmDNS.create(intf, hostname);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void stopMDNS() {
            new Thread(){
                @Override
                public void run() {
                    if (null != jmdns) {
                        try {
                            jmdns.removeServiceListener(mserviceName, slistener);
                            jmdns.unregisterAllServices();
                            jmdns.close();
                            jmdns=null;
                            if (null != lock)
                                lock.release();
                            lock=null;
                            slistener=null;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
    }

    public void updateMessage() {

        JSONArray tmpArray = new JSONArray();
        Iterator it = jsonmap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            tmpArray.put(entry.getValue());
        }
        this.array = tmpArray;
        if (comfunc != null)
            comfunc.onDevsFindmDNS(array, msearchdevcb);
    }

    private void sendCallBack() {
        comfunc.onDevsFindmDNS(array, msearchdevcb);
    }

    class SampleListener implements ServiceListener {
        ServiceInfo sName;

        @Override
        public void serviceAdded(ServiceEvent event) {
            LogUtil.i("serviceAdded:" + event.getName());
            jmdns.requestServiceInfo(event.getType(), event.getName(), 1);

        }

        @Override
        public void serviceRemoved(ServiceEvent event) {

            // Log.d("---HomeFragment---", event.getName());
            jsonmap.remove(event.getName());
            // 通知handler有数据更新
            Message msg = new Message();
            msg.what = _REMOVEDEVICE;
            mdnsHandler.sendMessage(msg);
        }

        @Override
        public void serviceResolved(ServiceEvent event) {
            if (!jsonmap.containsKey(event.getName())) {
                sName = event.getInfo();
                LogUtil.i("serviceResolved:" + event.getName());
                if (null != sName) {
                    // 截取byte数组
                    ArrayList<String> mDNSList = new ArrayList();
                    byte[] allinfobyte = sName.getTextBytes();
                    int allLen = allinfobyte.length;
                    for (int index = 0; index < allLen; ) {
                        int infoLength = allinfobyte[index++];
                        byte[] temp = new byte[infoLength];
                        System.arraycopy(allinfobyte, index, temp, 0,
                                infoLength);
                        try {
                            String isoString = new String(temp, "UTF-8");
                            mDNSList.add(isoString);
                            // Log.d(TAG + "arraycopy", isoString);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        index += infoLength;
                    }

                    try {
                        JSONObject jsonObject = new JSONObject();

                        String dev_ip = "";
                        // sName.getHostAddresses()
                        String hostAddress = sName.getAddress().getHostAddress();

                        Inet4Address[] ipv4 = sName.getInet4Addresses();
                        if (ipv4.length > 0) {
                            dev_ip = ipv4[0].toString();
                            int isOtherStr = dev_ip.indexOf("/");
                            if (isOtherStr > -1) {
                                dev_ip = dev_ip.substring(isOtherStr + 1);
                            }
                        }

                        jsonObject.put("Name", sName.getName());
                        jsonObject.put("IP", dev_ip);
                        jsonObject.put("Port", sName.getPort());

                        for (String orlValue : mDNSList) {
                            if (!"".equals(orlValue)) {
                                String[] temp = orlValue.split("=");
                                jsonObject.put(temp[0], temp[1]);
                            }
                        }
                        //Log.d("---HomeFragment---", event.getName());
                        jsonmap.put(event.getName(), jsonObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // 通知handler有数据更新
                    Message msg = new Message();
                    msg.what = _ADDDEVICE;
                    mdnsHandler.sendMessage(msg);
                } else {
                    LogUtil.e("serviceAdded:serviceInfo==null");

                }
            }
        }
    }

    private Handler mdnsHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 通知线程有数据更新
                case _ADDDEVICE:
                    updateMessage();
                    break;
                // 如果有设备离线，就重新监听
                case _REMOVEDEVICE:
                    updateMessage();
                    break;
            }
        }
    };

    public InetAddress getLocalIpAddress(Context context) throws Exception {
        if (wifiManager == null)
            wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiinfo = wifiManager.getConnectionInfo();
        int intaddr = wifiinfo.getIpAddress();
        byte[] byteaddr = new byte[]{(byte) (intaddr & 0xff),
                (byte) (intaddr >> 8 & 0xff), (byte) (intaddr >> 16 & 0xff),
                (byte) (intaddr >> 24 & 0xff)};
        InetAddress addr = InetAddress.getByAddress(byteaddr);
        return addr;
    }

}