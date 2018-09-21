package net.suntrans.haipopeiwang.tcp;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Looney on 2017/1/23.
 */

public class TcpHelper {
    public TcpHelper.ibinder binder;
    private TcpManager manager;
    private ThreadPoolExecutor executor;
    private AppCompatActivity activity;

    public TcpHelper(AppCompatActivity activity, String ip, int port, String checkCode) {
        this.activity = activity;
        init(ip, port, checkCode, "0");
    }

    public TcpHelper(AppCompatActivity activity, String ip, int port, String checkCode, String isHex) {
        this.activity = activity;

        init(ip, port, checkCode, isHex);

    }


    private void init(String ip, int port, String checkCode, String isHex) {
        executor = new ThreadPoolExecutor(3, 30,
                1, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(6));
        manager = new TcpManager(ip, port);
        if (checkCode != null)
            manager.setCheckCode(checkCode);
        new Thread() {
            @Override
            public void run() {
                manager.connect();
               if(activity!=null){
                   activity.runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           if (onReceivedListener != null) {
                               onReceivedListener.onConnected();
                           }
                           if (onReceivedListener1 != null) {
                               onReceivedListener1.onConnected();
                           }
                       }
                   });
               }
            }
        }.start();
        if (!TextUtils.isEmpty(isHex) && "1".equals(isHex)) {
            manager.setOrginListener(new TcpHelper.OrginListener());
        } else {
            manager.setListener(new TcpHelper.TCPListener());
        }
        binder = new TcpHelper.ibinder() {
            @Override
            public boolean sendOrder(final String order) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        manager.send(order);
                    }
                });
                return true;
            }

            @Override
            public boolean sendOrderNotHex(final String order) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        manager.sendNot2Hex(order);
                    }
                });
                return true;
            }
        };
    }

    public void unRegister() {
        manager.disConnect();
    }


    public void setOnReceivedListener(OnReceivedListener onReceivedListener) {
        this.onReceivedListener = onReceivedListener;
    }

    public void setOnReceivedListener1(OnReceivedListener onReceivedListener) {
        this.onReceivedListener1 = onReceivedListener;
    }

    OnReceivedListener onReceivedListener;
    OnReceivedListener onReceivedListener1;

    public interface OnReceivedListener {
        void onReceive(String content);

        void onConnected();

    }

    private void receiveData( int code ,final String msg){
        if(activity!=null){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (onReceivedListener != null) {
                        onReceivedListener.onReceive(msg);
                    }
                    if (onReceivedListener1 != null) {
                        onReceivedListener1.onReceive(msg);
                    }
                }
            });
        }
    }

    public abstract class ibinder {
        /****
         * 发送命令
         * @param order    控制命令内容，从控制子地址开始，到校验码之前
         * @return 发送成功返回true，失败返回false
         */
        public abstract boolean sendOrder(String order);

        public abstract boolean sendOrderNotHex(String order);
    }

    class TCPListener implements TcpManager.TcpListener {
        @Override
        public void onReceive(String result) {
            receiveData(200,result);

        }

        @Override
        public void onError(int code, String msg) {
            receiveData(500,msg);

        }
    }

    class OrginListener implements TcpManager.OrginListener {
        @Override
        public void onReceive(String result) {
            receiveData(200,result);

        }

        @Override
        public void onError(int code, String msg) {
            receiveData(500,msg);

        }
    }
}
