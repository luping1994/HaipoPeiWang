package net.suntrans.haipopeiwang;

import android.os.AsyncTask;

import java.lang.reflect.Method;

/**
 * Created by Looney on 2018/3/2.
 * Des:
 */

public class Config {


    public static String EWM_SERVICE = "_easylink._tcp.local.";
    public static String WX_APP_ID = "wx614c4e669f8bbd01";
    public static String GRANT_TYPE = "authorization_code";
    public static String SECRET = "b273e345fb488b794689d371587c85e9";

    public static String ST_SLC_10 = "ST-SLC-10";
    public static String ST_SLC_6 = "ST-SLC-6";
    public static String ST_SLC_3_2 = "ST-SLC-23P";
    public static String ST_Sensus = "ST-Sensus";
    public static String ST_SRD = "ST-SRD";
    public static String ST_SECC = "ST-SECC";
    public static String ST_SLC_23P = "ST-SLC-23P";
    public static String ST_SLC_2PLUS = "ST-SLC-2P";
    public static String ST_ITL = "ST-ITL";
    public static String ST_SLC_2 = "ST-SLC-2";
    public static String ST_SLC_LOCK = "ST-SLOCK";

    //CODE_ST_SLC_6
    public static String CODE_ST_SLC_10 = "4100";
    public static String CODE_ST_SLC_6 = "4300";
    public static String CODE_ST_SLC_3_2 = "4301";
    public static String CODE_SENSUS = "6300";
    public static String CODE_ST_SLC_2PLUS = "4901";
    public static String CODE_ST_SLC_2 = "4900";

    public static String CODE_ST_SRD = "4800";
    public static String CODE_ST_SECC = "9100";
    public static String CODE_ST_SLOCK = "9200";//智能门锁

    private void say(String s) {
        System.out.println(s);
    }

    public static String FILE_PROVIDER = "net.suntrans.haipopeiwang.fileProvider";


    //    public interface Subject {
//        public void rent();
//
//        public void hello(String str);
//    }
//
//    public static class RealSubject implements Subject {
//        @Override
//        public void rent() {
//            System.out.println("I want to rent my house");
//        }
//
//        @Override
//        public void hello(String str) {
//            System.out.println("hello: " + str);
//        }
//    }
//
//    public static class DynamicProxy implements InvocationHandler {
//        //　这个就是我们要代理的真实对象
//        private Object subject;
//
//        //    构造方法，给我们要代理的真实对象赋初值
//        public DynamicProxy(Object subject) {
//            this.subject = subject;
//        }
//
//        @Override
//        public Object invoke(Object object, Method method, Object[] args)
//                throws Throwable {
//            //　　在代理真实对象前我们可以添加一些自己的操作
//            System.out.println("before rent house");
//
//            System.out.println("Method:" + method);
//
//            //    当代理对象调用真实对象的方法时，其会自动的跳转到代理对象关联的handler对象的invoke方法来进行调用
//            method.invoke(subject, args);
//
//            //　　在代理真实对象后我们也可以添加一些自己的操作
//            System.out.println("after rent house");
//
//            return null;
//        }
//
//    }




}
