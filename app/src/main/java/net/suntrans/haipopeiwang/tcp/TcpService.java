package net.suntrans.haipopeiwang.tcp;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import net.suntrans.haipopeiwang.utils.LogUtil;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Created by Looney on 2017/1/20.
 */
public class TcpService extends RxService {
    private ibinder binder;
    private String ip;
    private int port;
    private TcpManager manager;
    private String checkCode;
    private static final String TAG = "TcpService";
    private ThreadPoolExecutor executor;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.i(TAG,"绑定服务");
        ip = intent.getStringExtra("ip");
        port = intent.getIntExtra("port",-1);
        checkCode = intent.getStringExtra("checkCode");

        LogUtil.i("ip="+ip+",port="+port);
        executor= new ThreadPoolExecutor(3, 30,
                1, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(6));
        manager = new TcpManager(ip,port);
        if (checkCode!=null)
                manager.setCheckCode(checkCode);
      new Thread(){
          @Override
          public void run() {
              manager.connect();
          }
      }.start();
        String isHex = intent.getStringExtra("isHex");
        if(!TextUtils.isEmpty(isHex)&&"1".equals(isHex)){
            manager.setOrginListener(new OrginListener());
        }else {
            manager.setListener(new TCPListener());
        }
        binder = new ibinder() {
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
        return binder;
    }
    Intent intent = new Intent();
    class TCPListener implements TcpManager.TcpListener{
        @Override
        public void onReceive(String result) {
            intent.putExtra("content",result);
            intent.setAction("net.suntrans.www");
            sendBroadcast(intent);
        }

        @Override
        public void onError(int code, String msg) {
            intent.putExtra("content",msg);
            intent.setAction("net.suntrans.www");
            sendBroadcast(intent);
        }
    }
    class OrginListener implements TcpManager.OrginListener{
        @Override
        public void onReceive(String result) {
            intent.putExtra("content",result);
            intent.setAction("net.suntrans.www");
            sendBroadcast(intent);
        }

        @Override
        public void onError(int code, String msg) {
            intent.putExtra("content",msg);
            intent.setAction("net.suntrans.www");
            sendBroadcast(intent);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.disConnect();
        LogUtil.i(TAG+"服务销毁");
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }


    public abstract class ibinder extends Binder {
        /****
         * 发送命令
         * @param order    控制命令内容，从控制子地址开始，到校验码之前
         * @return  发送成功返回true，失败返回false
         */
        public abstract boolean sendOrder(String order);
        public abstract boolean sendOrderNotHex(String order);
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
