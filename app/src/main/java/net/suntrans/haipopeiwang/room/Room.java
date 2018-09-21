package net.suntrans.haipopeiwang.room;

import java.util.List;

/**
 * Created by Looney on 2018/9/17.
 * Des:
 */
public class Room {

    /**
     * id : 181
     * union_id : 1
     * name : 客厅-T
     * img_url : https://hpjd.suntrans-cloud.com//uploads/images/aOiua5L62tqo6GwiuGcSa88dpxIuJEKqfs8ZHzEP.jpeg
     * name_en : null
     * channels : [{"id":866,"area_id":181,"channel_id":1838,"status":0,"updated_at":"2018-09-04 09:07:11"},{"id":867,"area_id":181,"channel_id":1841,"status":0,"updated_at":"2018-07-25 09:54:51"},{"id":868,"area_id":181,"channel_id":1843,"status":0,"updated_at":"2018-07-25 09:55:10"},{"id":869,"area_id":181,"channel_id":1842,"status":0,"updated_at":"2018-08-10 13:45:25"},{"id":870,"area_id":181,"channel_id":1844,"status":0,"updated_at":"2018-07-25 09:55:06"},{"id":871,"area_id":181,"channel_id":1856,"status":0,"updated_at":"2018-07-27 11:55:21"},{"id":883,"area_id":181,"channel_id":1855,"status":0,"updated_at":"2018-08-21 15:04:19"},{"id":894,"area_id":181,"channel_id":1845,"status":0,"updated_at":"2018-09-04 09:06:50"},{"id":904,"area_id":181,"channel_id":1858,"status":0,"updated_at":"2018-08-21 15:16:27"}]
     * sub : {"total":9,"open":2}
     */

    public int id;
    public int union_id;
    public String name;
    public String img_url;
    public String name_en;
    public SubBean sub;
    public List<ChannelsBean> channels;

    public static class SubBean {
        /**
         * total : 9
         * open : 2
         */

        public int total;
        public int open;
    }

    public static class ChannelsBean {
        /**
         * id : 866
         * area_id : 181
         * channel_id : 1838
         * status : 0
         * updated_at : 2018-09-04 09:07:11
         */

        public int id;
        public int area_id;
        public int channel_id;
        public int status;
        public String updated_at;
    }
}
