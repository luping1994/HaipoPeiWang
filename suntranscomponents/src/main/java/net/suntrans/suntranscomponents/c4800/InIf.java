package net.suntrans.suntranscomponents.c4800;

import java.util.List;

/**
 * Created by Looney on 2018/9/18.
 * Des:感应的 条件
 */
public class InIf {

    /**
     * conditions : {"youren":1,"wuren":1}
     * systems : [{"title":"时间","id":"1533867460443","type":"time","startTime":"00:00","endTime":"23:59","repeat":"1,2,3,4,5,6,7","delay":0},{"title":"时间","id":1533867774596,"type":"time","startTime":"18:03","endTime":"18:59","delay":0}]
     * device : {"id":226,"title":"智能测控器","area":"客厅-T","parentId":2,"device_type":4800,"checked":true,"type":"srd","dev_id":226}
     */

    public ConditionsBean conditions;
    public DeviceBean device;
    public List<SystemsBean> systems;

    public static class ConditionsBean {
        /**
         * youren : 1
         * wuren : 1
         */
        public int youren;
        public int wuren;
    }

    public static class DeviceBean {
        /**
         * id : 226
         * title : 智能测控器
         * area : 客厅-T
         * parentId : 2
         * device_type : 4800
         * checked : true
         * type : srd
         * dev_id : 226
         */

        public int id;
        public String title;
        public String area;
        public int parentId;
        public int device_type;
        public boolean checked;
        public String type;
        public int dev_id;
    }

    public static class SystemsBean {
        /**
         * title : 时间
         * id : 1533867460443
         * type : time
         * startTime : 00:00
         * endTime : 23:59
         * repeat : 1,2,3,4,5,6,7
         * delay : 0
         */
        public String title;
        public String id;
        public String type;
        public String startTime;
        public String endTime;
        public String repeat;
        public String delay;

        public SystemsBean(){

        }

        public SystemsBean(String title, String id, String type, String repeat, String delay) {
            this.title = title;
            this.id = id;
            this.type = type;
            this.repeat = repeat;
            this.delay = delay;
        }
    }
}
