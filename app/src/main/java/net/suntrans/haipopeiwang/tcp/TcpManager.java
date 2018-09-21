package net.suntrans.haipopeiwang.tcp;


import android.os.Handler;
import android.util.Log;

import net.suntrans.haipopeiwang.utils.Converts;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;


/**
 * Created by Looney on 2017/1/20.
 */

public class TcpManager {
    public static final int CODE_CONNECT_CLOSE=1;
    private final String TAG = "TcpManager";
    private int spacingTime = 5;
    private String ip;
    private int port;
    private boolean isExit = false;

    private Socket mConnection;

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public TcpManager(String ip, int port) {
        this.port = port;
        this.ip = ip;
//        try {
//            InetAddress inetAddress = InetAddress.getByName(ip);
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
    }

    private String checkCode;


    public void connect() {
        try {
            mConnection = new Socket(ip, port);
            Thread.sleep(500);
            new TcpReadThread().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class TcpReadThread extends Thread {
        @Override
        public void run() {
            final StringBuilder sb = new StringBuilder();
            // 接收服务器信息       定义输入流
            InputStream in = null;
            try {
                in = mConnection.getInputStream();
                DataInputStream ins = new DataInputStream(in);
                byte[] content = new byte[1024];
                int count = 0;   //记录接收数据数组的长度
                while ((count = ins.read(content)) != -1) {
                    String s = "";                       //保存命令的十六进制字符串
                    String origin = "";
                    for (int i = 0; i < count; i++) {
                        String s1 = Integer.toHexString((content[i] + 256) % 256);   //byte转换成十六进制字符串(先把byte转换成0-255之间的非负数，因为java中的数据都是带符号的)
                        if (s1.length() == 1)
                            s1 = "0" + s1;
                        s = s + s1;

                    }
                    byte[] dist=new byte[count];
                    System.arraycopy(content,0,dist,0,count);
                    origin = new String(dist, "utf-8");

                    if (listener != null){
                        listener.onReceive(s);
                    }
                    if (orginRecListener != null) {
                        orginRecListener.onReceive(origin);
                    }
                }
            } catch (Exception e) {
                if (listener != null){
                    listener.onReceive("断开连接");
                }
                if (orginRecListener != null) {
                    orginRecListener.onReceive("断开连接");
                }
                e.printStackTrace();
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            } finally {

            }

        }
    }


    private void reconnect() {
        if (!isExit) {

        }
    }

    Handler handler = new Handler();

    public void send(String order) {
        DataOutputStream out = null;

        if (mConnection != null) {

            try {
                out = new DataOutputStream(mConnection.getOutputStream());
                String str = order;
                byte[] bt1 = Converts.HexString2Bytes(order);      //将完整的命令转换成十六进制
                if (!mConnection.isClosed()) {
                    if (mConnection.isConnected()) {
                        if (!mConnection.isOutputShutdown()) {
                            out.write(bt1);
                            out.flush();
                            Log.i("Order", "发送数据：" + str);
                        }
                    }

                }
            } catch (IOException e) {
                if(listener!=null){
                    listener.onError(CODE_CONNECT_CLOSE,"连接中断");
                }
                if(orginRecListener!=null){
                    orginRecListener.onError(CODE_CONNECT_CLOSE,"连接中断");
                }
                e.printStackTrace();
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            } finally {

            }

        } else {
            if (listener != null)
                listener.onError(RECONNECT_FAILED, "与服务器连接失败,重连中...");
            if (orginRecListener != null) {
                orginRecListener.onError(RECONNECT_FAILED, "与服务器连接失败,重连中...");
            }
        }
    }

    public void sendNot2Hex(String order) {
        DataOutputStream out = null;

        if (mConnection != null) {

            try {
                out = new DataOutputStream(mConnection.getOutputStream());
                String str = order;
                byte[] bt1 = order.getBytes();      //将完整的命令转换成十六进制
                if (!mConnection.isClosed()) {
                    if (mConnection.isConnected()) {
                        if (!mConnection.isOutputShutdown()) {
                            out.write(bt1);
                            out.flush();
                            Log.i("Order", "发送数据：" + str);
                        }
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            } finally {

            }

        } else {
            if (listener != null)
                listener.onError(RECONNECT_FAILED, "与服务器连接失败,重连中...");
            if (orginRecListener != null) {
                orginRecListener.onError(RECONNECT_FAILED, "与服务器连接失败,重连中...");
            }
        }
    }


    private static final int SEND_FAILED = 0;
    private static final int RECONNECT_FAILED = 1;
    private static final int CONNECT_BREAK = 2;
    private static final int CONNECT_FAILED = 3;

    /**
     * 停止服务
     */
    public void disConnect() {
        isExit = true;
        handler.removeCallbacksAndMessages(null);
        try {
            if (mConnection != null && mConnection.isConnected())
                mConnection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setListener(TcpListener listener) {
        this.listener = listener;
    }

    public void setOrginListener(OrginListener listener) {
        this.orginRecListener = listener;
    }

    private TcpListener listener;
    private OrginListener orginRecListener;

    public interface TcpListener {
        void onReceive(String result);

        void onError(int code, String msg);
    }

    public interface OrginListener {
        void onReceive(String result);

        void onError(int code, String msg);
    }

}
